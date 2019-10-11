package com.br.shopping.busiListener;


import com.br.activiti.utils.JsonUtils;
import com.br.shopping.service.ShoppingService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.el.FixedValue;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Data
public class JsonToObjectListenner implements TaskListener {


    /**
     * 需要转换的流程参数的变量名
     */
    public FixedValue jsonParamName ;
    /**
     * jsonParamName 变量对应的Json值是否是数组 true：是 false:否
     */
    public FixedValue isList;
    /**
     *  jsonParamName 变量对应的Json值 转换成 Class<ttClassName> 的实例
     */
    public FixedValue ttClassName;
    /**
     * 转换成功后，对象的引用名，该对象及引用名将会存储在activiti流程参数表中
     */
    public FixedValue newParamName;

    public Object convertObject(String className, String data) throws ClassNotFoundException {

        Object o = Class.forName(className);
//        JsonUtils

        return null;
    }


    @Override
    public void notify(DelegateTask delegateTask) throws RuntimeException{

//        delegateTask.te

        log.info(delegateTask.getId());
        log.info("" + delegateTask.getVariable((getValueFromFixedVale(jsonParamName,delegateTask))));
        String jsonStr  = (String)delegateTask.getVariable(getValueFromFixedVale(jsonParamName,delegateTask));

        if(StringUtils.isEmpty(jsonParamName)){
            throw new RuntimeException("jsonParamName is null");
        }
        if(StringUtils.isEmpty(isList)){
            throw new RuntimeException("isList is null");
        }
        if(StringUtils.isEmpty(ttClassName)){
            throw new RuntimeException("ttClassName is null");
        }
        if(StringUtils.isEmpty(newParamName)){
            throw new RuntimeException("newParamName is null");
        }



        if( Boolean.valueOf(getValueFromFixedVale(isList,delegateTask))){

            try {
                List<?> resObj = JsonUtils.getStringToList(jsonStr, Class.forName(getValueFromFixedVale(ttClassName,delegateTask)));
                delegateTask.setVariable(getValueFromFixedVale(newParamName,delegateTask),resObj);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("list 转换异常，泛型："+getValueFromFixedVale(ttClassName,delegateTask));
            }
            return;
        }

        try {
            Object resObj = JsonUtils.toObject(jsonStr , Class.forName(getValueFromFixedVale(ttClassName,delegateTask)));
            delegateTask.setVariable(getValueFromFixedVale(newParamName,delegateTask),resObj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("对象转换异常 ： " + getValueFromFixedVale(ttClassName,delegateTask));
        }

    }

    private String getValueFromFixedVale(FixedValue fixedValue , DelegateTask delegateTask){
        return (String)fixedValue.getValue(delegateTask.getExecution());
    }
}
