package com.br.activiti.service.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Customer implements Serializable {
    private String name;
    private int age;
}
