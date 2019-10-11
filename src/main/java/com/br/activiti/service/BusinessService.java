package com.br.activiti.service;

import com.br.activiti.service.domain.Customer;
import com.br.activiti.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("service1")
@Slf4j
public class BusinessService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    /**
     * 无参方法,无数据准备要求
     */
    public void task1(){
        log.info("executed service1 taks1");
    }

    /**
     * 无参方法，需要流程执行中提交的参数,内存提交
     */
    public void task2(String mess ){
        log.info("print param:"+mess);
    }

    /**
     * 根据入参创建对象
     * @param name
     * @param age
     * @return
     */
    public Customer createCustomer(String name , int age){
        return Customer.builder().age(age).name(name).build();
    }

    /**
     * 已Json格式打印对象信息
     * @param o
     */
    public void printObject(Object o){
        log.info(JsonUtils.toJsonString(o));
    }


    /**
     * 清理过时的流程实例
     */
    public  void clearActiveProcess(){
        log.info("start clear.......");
        //获取超过30分钟未完成的执行流
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE,-30);
        Date deadTime = c.getTime();
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
        List<Execution> list = executionQuery.startedBefore(deadTime).onlyProcessInstanceExecutions().list();

        //挂起执行流
        for( Execution e : list){
            if( !e.isSuspended()){
                log.info(" need suspend process ins id :"+e.getProcessInstanceId());//超过三十分钟的执行流挂起
                runtimeService.suspendProcessInstanceById(e.getProcessInstanceId());
            }
        }

        //获取超过1小时未完成且挂起的执行流
        ExecutionQuery executionQuery2 = runtimeService.createExecutionQuery();
        c.add(Calendar.MINUTE,-30);
        List<Execution> exeForDel = executionQuery2.startedBefore(c.getTime()).onlyProcessInstanceExecutions().list();
        for( Execution e : exeForDel){
            log.info(" need close process ins id :"+e.getProcessInstanceId());  //超过1小时的执行流强制结束
            runtimeService.deleteProcessInstance(e.getProcessInstanceId()," after 1 hour not completed ， system auto closed" );
        }


    }

    public Map<String,Object> queryProcessParam(){
        return null;
    }

    public void addTenantId(String tenantId){

    }


}
