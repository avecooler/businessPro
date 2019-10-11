package com.br.shopping.service;

import com.br.shopping.dao.OrderDetailMapper;
import com.br.shopping.dao.OrderInfoMapper;
import com.br.shopping.dao.ProductInfoMapper;
import com.br.shopping.dao.ShoppingCarMapper;
import com.br.shopping.dao.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("shoppingService")
@Slf4j
public class ShoppingService {

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;


    public List<ShoppingCar> createShoppCar( List<ShoppingCar> car){
        for( ShoppingCar pro : car){
            shoppingCarMapper.mergeInto(pro);
        }
        return null;
    }


    public OrderInfo createOrderFromCar(String customerId){
        log.info("从购物车创建订单："+customerId);

        Date now = new Date();

        //获取购物车信息
        ShoppingCarExample carExm = new ShoppingCarExample();
        carExm.createCriteria().andCustomerIdEqualTo(customerId);
        List<ShoppingCar> shoppingCars = shoppingCarMapper.selectByExample(carExm);



        //组织订单价格
        BigDecimal payAmtBig = new BigDecimal("0");
        List<String> productIds = new ArrayList<String>();
        for(ShoppingCar car : shoppingCars ){
            ProductInfoExample exmPro = new ProductInfoExample();
            exmPro.createCriteria().andProductIdEqualTo(car.getProductId());
            List<ProductInfo> productInfos = productInfoMapper.selectByExample(exmPro);
            payAmtBig = new BigDecimal(productInfos.get(0).getPrice()).multiply(new BigDecimal(car.getProductNum())).add(payAmtBig);
            productIds.add(car.getProductId());
        }

        //检查商品信息
        ProductInfoExample productExm = new ProductInfoExample();
        productExm.createCriteria().andProductIdIn(productIds);
        List<ProductInfo> proList = productInfoMapper.selectByExample(productExm);
        Map<String , ProductInfo> prosMap = listToMapByKey(proList);



        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPayState("00");
        orderInfo.setCustomerId(customerId);
        orderInfo.setOrderName("购物");
        orderInfo.setPayAmt(payAmtBig.floatValue());
        orderInfo.setCreateTime(now);

        int orderNo = orderInfoMapper.insertSelective(orderInfo);

        //处理orderDetail
        for( ShoppingCar c: shoppingCars){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setNum(c.getProductNum());
            orderDetail.setPrice(prosMap.get(c.getProductId()).getPrice());
            orderDetail.setOrderNo(orderInfo.getOrderNo());
            orderDetail.setProductId(c.getProductId());
            orderDetailMapper.insert(orderDetail);
        }

        return orderInfo;

    }

    public void clearShoppingCar(String customerId){
        log.info("清空购物车"+customerId);
        ShoppingCarExample exm = new ShoppingCarExample();
        exm.createCriteria().andCustomerIdEqualTo(customerId);
        shoppingCarMapper.deleteByExample(exm);
    }

    public OrderInfo updatePayState(int orderNo , String payState){
        log.info("订单支付状态变更："+orderNo+" 状态"+payState);
        OrderInfo order = new OrderInfo();
        order.setPayState(payState);
        order.setOrderNo(orderNo);
        orderInfoMapper.updateByPrimaryKeySelective(order);
        order = orderInfoMapper.selectByPrimaryKey(orderNo);
        return order;
    }


    private Map<String , ProductInfo> listToMapByKey(List<ProductInfo> list){
        Map<String, ProductInfo> resMap = new HashMap<String,ProductInfo>();
        for( ProductInfo p : list){
            resMap.put(p.getProductId(),p);
        }
        return resMap;
    }

}
