package com.br.activiti.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespBaseTaskInfo {
    private String taskId;
    private String taskKey;
    private String taskDesc;
    private String owner;
    private String createTime;
    private String assigneeId;
    private String taskName;
    private String proInstanceId;

}
