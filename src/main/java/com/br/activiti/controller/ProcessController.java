package com.br.activiti.controller;

import com.br.activiti.domain.ReqContinueProcess;
import com.br.activiti.utils.JsonUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    private TaskService taskService;

    @RequestMapping(value = "startProcessByKey" , method = RequestMethod.POST)
    @ResponseBody
    public String startProcessByKey(@RequestBody ReqContinueProcess request){

        Map<String, Object> vars = new HashMap<String, Object>();

        if(!StringUtils.isEmpty(request.getSubmitInfo())){
            vars = JsonUtils.toMap(request.getSubmitInfo());
        }

        //执行某流程
        ProcessInstance processIns = runtimeService.startProcessInstanceByKey(request.getProcessKey(),vars);

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

        if(!StringUtils.isEmpty(request.getSubmitInfo())){
            vars = JsonUtils.toMap(request.getSubmitInfo());
        }
        TaskQuery taskQuery = taskService.createTaskQuery().active().processInstanceId(request.getProcessInsId());
        if( !StringUtils.isEmpty( request.getTaskId())){
            taskQuery.taskId( request.getTaskId() );
        }
        List<Task> list = taskQuery.list();
        if( list.size() != 1){
            return "找不到任务/任务不明确";
        }

        taskService.complete( list.get(0).getId());

        return "success,prcessId:"+list.get(0).getProcessInstanceId()+",taskId:"+list.get(0).getId();
    }






}
