package com.br.activiti.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel( description = "流程执行请求 ")
public class ReqContinueProcess {

    @ApiModelProperty( value = "流程实例ID")
    private String processInsId;

    @ApiModelProperty( value = "流程定义主键")
    private String processKey;

    @ApiModelProperty( value = "流程执行时的参数，Json格式")
    private String submitInfo;

    @ApiModelProperty( value = "task id")
    private String taskId;

    @ApiModelProperty( value = "任务Key")
    private String taskKey;

    @ApiModelProperty( value = "用户ID")
    private String customerId;

    @ApiModelProperty( value = "操作符")
    private String opt;

    /*@ApiModelProperty( value = "流程所属用户")
    private String owner;*/

}
