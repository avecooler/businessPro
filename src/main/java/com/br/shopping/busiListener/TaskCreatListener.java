package com.br.shopping.busiListener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

@Slf4j
public class TaskCreatListener implements ActivitiEventListener {



    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        log.info("ENTITY_CREATED");
        String processInstanceId = activitiEvent.getProcessInstanceId();
        String execId = activitiEvent.getExecutionId();
        log.info( "execId:"+execId );



    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
