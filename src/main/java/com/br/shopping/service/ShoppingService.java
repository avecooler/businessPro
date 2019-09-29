package com.br.shopping.service;

import com.br.shopping.dao.OrderInfoMapper;
import com.br.shopping.dao.ProductInfoMapper;
import com.br.shopping.dao.ShoppingCarMapper;
import com.br.shopping.dao.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("shoppingService")
public class ShoppingService {

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ProductInfoMapper productInfoMapper;


    public List<ShoppingCar> createShoppCar( List<ShoppingCar> car){
        for( ShoppingCar pro : car){
            shoppingCarMapper.mergeInto(pro);
        }
        return null;
    }

    public void updateOrderPayState(int orderNo ,String payState){

        OrderInfoExample exmp = new OrderInfoExample();
        exmp.createCriteria().andOrderNoEqualTo(orderNo);

        OrderInfo order = new OrderInfo();
        order.setPayState(payState);
        orderInfoMapper.updateByExampleSelective(order , exmp);

    }

    public void createOrderFromCar(String customerId){
        //获取购物车信息
        ShoppingCarExample carExm = new ShoppingCarExample();
        carExm.createCriteria().andCustomerIdEqualTo(customerId);
        List<ShoppingCar> shoppingCars = shoppingCarMapper.selectByExample(carExm);

        //组织订单价格
        BigDecimal payAmtBig = new BigDecimal("0");
        for(ShoppingCar car : shoppingCars ){
            ProductInfoExample exmPro = new ProductInfoExample();
            exmPro.createCriteria().andProductIdEqualTo(car.getProductId());
            List<ProductInfo> productInfos = productInfoMapper.selectByExample(exmPro);
            payAmtBig = new BigDecimal(productInfos.get(0).getPrice()).multiply(new BigDecimal(car.getProductNum())).add(payAmtBig);

        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPayState("00");
        orderInfo.setCustomerId(customerId);
        orderInfo.setOrderName("购物");
        orderInfo.setPayAmt(payAmtBig.floatValue());




    }
}
