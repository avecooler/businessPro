package com.br.activiti.controller;

import com.br.activiti.domain.ReqContinueProcess;
import com.br.activiti.domain.RespBaseTaskInfo;
import com.br.activiti.utils.JsonUtil;
import com.br.activiti.utils.JsonUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("process")
@Slf4j
public class ProcessController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessEngine engin;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FormService formService;


    @RequestMapping(value = "startProcessByKey" , method = RequestMethod.POST)
    @ResponseBody
    public String startProcessByKey(@RequestBody ReqContinueProcess request){

        Map<String, Object> vars = new HashMap<String, Object>();
        vars = JsonUtils.toMap(request);

        //执行某流程
        ProcessInstance processIns = runtimeService.startProcessInstanceByKey(request.getProcessKey(),vars);

//        ProcessInstance processIns = runtimeService.createProcessInstanceBuilder()
//                .tenantId("123").processDefinitionKey(request.getProcessKey()).variables(vars).start();


//        ProcessInstance processIns = runtimeService.startProcessInstanceByKeyAndTenantId()





        if( !StringUtils.isEmpty( request.getCustomerId() )){
            runtimeService.addUserIdentityLink(processIns.getProcessInstanceId(),request.getCustomerId(), IdentityLinkType.OWNER);
        }



        log.info("desc process info");
        log.info("defined id: "+processIns.getProcessDefinitionId());
        log.info("process id : "+processIns.getProcessInstanceId());
        log.info("info end-----");



        return "success,processId:"+processIns.getProcessInstanceId();
    }

    @RequestMapping(value = "continuedProcess" ,method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="继续执行流程")
    public String continuedSingleTaskProcess(@RequestBody ReqContinueProcess request){

        Map<String, Object> vars = new HashMap<String, Object>();
        vars = JsonUtil.toMap(JsonUtil.toJsonObj(JsonUtil.toJson(request)));
        TaskQuery taskQuery = taskService.createTaskQuery().active().processInstanceId(request.getProcessInsId());
        if( !StringUtils.isEmpty( request.getTaskId())){
            taskQuery.taskId( request.getTaskId() );
        }

        List<Task> list = taskQuery.list();
        if( list.size() != 1){
            return "找不到任务/任务不明确";
        }

        Task t = list.get(0);



        taskService.setVariablesLocal(t.getId() , vars);
        taskService.complete( t.getId());

        return "success,prcessId:"+list.get(0).getProcessInstanceId()+",taskId:"+list.get(0).getId();
    }


    public List<RespBaseTaskInfo> queryTaskByKey(@RequestBody ReqContinueProcess request){
        List<RespBaseTaskInfo> respList = new ArrayList<RespBaseTaskInfo>();
        List<Task> taskList = taskService.createTaskQuery().taskOwner(request.getCustomerId()).taskDefinitionKey(request.getTaskKey()).list();
        for(Task t : taskList){
            RespBaseTaskInfo taskInfo  = RespBaseTaskInfo.builder().taskId(t.getId()).taskKey(t.getTaskDefinitionKey())
                    .taskDesc(t.getDescription()).owner(t.getOwner()).build();
            respList.add(taskInfo);
        }

        return respList;
    }



    @RequestMapping(value = "queryDep" ,method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="查询部署信息")
    public String deploymentTest(String depKey){

        List<Deployment> deployments = repositoryService.createDeploymentQuery().deploymentKey(depKey).orderByDeploymenTime().desc()
                .listPage(0, 1);
        log.info(JsonUtil.toJson(deployments.get(0)));
        return JsonUtil.toJson(deployments.get(0));


    }

    @RequestMapping(value = "changeTenatId" ,method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="查询部署信息")
    public String changeTenatId(String tenatId , String depKey){
        List<Deployment> deployments = repositoryService.createDeploymentQuery().deploymentKey(depKey).orderByDeploymenTime().desc()
                .listPage(0, 1);
        log.info(JsonUtil.toJson(deployments.get(0)));
        String depId = deployments.get(0).getId();
        repositoryService.changeDeploymentTenantId(depId , tenatId);
        List<Deployment> deployments2 = repositoryService.createDeploymentQuery().deploymentKey(depKey).orderByDeploymenTime().desc()
                .listPage(0, 1);
        return JsonUtil.toJson(deployments2.get(0));


    }


    @RequestMapping(value = "queryTaskList" ,method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "查询激活的任务列表")
    public List<RespBaseTaskInfo> queryActiveTask(String customerId , String processKey , String taskKey){
        List<String> assigneeIds = new ArrayList<String>();
        assigneeIds.add(customerId);
        TaskQuery taskQuery = taskService.createTaskQuery().active();
        if( !StringUtils.isEmpty(customerId)){
            taskQuery = taskQuery.taskAssigneeIds(assigneeIds);
        }
        if( !StringUtils.isEmpty(processKey)){
            taskQuery.processDefinitionKey(processKey);
        }
        if( !StringUtils.isEmpty(taskKey)){
            taskQuery.taskDefinitionKey(taskKey);
        }
        List<Task> taskList = taskQuery.orderByTaskCreateTime().desc().list();
        List<RespBaseTaskInfo> resList = new ArrayList<RespBaseTaskInfo>();
        for( Task t :  taskList){
            RespBaseTaskInfo taskInfo = RespBaseTaskInfo.builder().taskId(t.getId())
                    .taskDesc(t.getDescription()).taskKey(t.getTaskDefinitionKey())
                    .owner(t.getOwner()).assigneeId(t.getAssignee())
                    .createTime( new SimpleDateFormat("yyyyMMddHHmmss").format(t.getCreateTime()))
                    .taskName(t.getName()).proInstanceId(t.getProcessInstanceId()).build();
            resList.add(taskInfo);
        }


        return resList;
    }




}
