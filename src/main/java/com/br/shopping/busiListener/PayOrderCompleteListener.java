package com.br.shopping.busiListener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@Slf4j
public class PayOrderCompleteListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("PayOrderCompleteListener started");
        Object opt = delegateTask.getVariable("opt");
        log.info(opt.toString());
        if( !"paySuccess".equals(opt)){
            log.info(opt.toString());
        }

    }
}
