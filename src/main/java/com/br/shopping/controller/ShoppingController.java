package com.br.shopping.controller;

import com.br.shopping.dao.ShoppingCarMapper;
import com.br.shopping.dao.domain.ShoppingCar;
import com.br.shopping.dao.domain.ShoppingCarExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("shopping")
@Slf4j
public class ShoppingController {

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @RequestMapping("test")
    public void test(){
        ShoppingCarExample exm = new ShoppingCarExample();
        exm.createCriteria().andProductIdEqualTo("123");

        List<ShoppingCar> shoppingCars = shoppingCarMapper.selectByExample(exm);

        log.info(""+shoppingCars.size());
    }

}
