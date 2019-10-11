package com.br.shopping.busiListener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.*;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.task.IdentityLinkType;


@Slf4j
@Data
public class AssignProOwnerListener implements TaskListener {

    private FixedValue customerId;

    @Override
    public void notify(DelegateTask delegateTask) {
        String ownerId = getStringValueFromVariable(customerId,delegateTask);
        delegateTask.setOwner(ownerId);
        delegateTask.addUserIdentityLink(ownerId , IdentityLinkType.ASSIGNEE);
        delegateTask.setAssignee(ownerId);
        log.info( "流程实例 owner："+ownerId);

    }

    private String getValueFromFixedVale(FixedValue fixedValue , DelegateTask delegateTask){
        return (String)fixedValue.getValue(delegateTask.getExecution());
    }

    private String getStringValueFromVariable(FixedValue fixedValue , DelegateTask delegateTask){
        String paramKey = getValueFromFixedVale(fixedValue , delegateTask);
        String value = (String)delegateTask.getVariable(paramKey);
        return value;
    }
}
