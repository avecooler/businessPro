package com.br.activiti.controller;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.NativeGroupQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("id")
@Slf4j
public class IdentityController {

    @Autowired
    private IdentityService identityService;


    @RequestMapping("queryGroup")
    @ResponseBody
    public String queryGroup(){
        NativeGroupQuery query = identityService.createNativeGroupQuery();
        List<Group> gs = query.sql("select * from act_id_group").list();
        log.info(gs.get(0).getName());
        return "success";
    }
}
