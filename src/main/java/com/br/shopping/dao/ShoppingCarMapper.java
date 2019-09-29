package com.br.shopping.dao;

import com.br.shopping.dao.domain.ShoppingCar;
import com.br.shopping.dao.domain.ShoppingCarExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCarMapper {
    int countByExample(ShoppingCarExample example);

    int deleteByExample(ShoppingCarExample example);

    int insert(ShoppingCar record);

    int insertSelective(ShoppingCar record);

    List<ShoppingCar> selectByExample(ShoppingCarExample example);

    int updateByExampleSelective(@Param("record") ShoppingCar record, @Param("example") ShoppingCarExample example);

    int updateByExample(@Param("record") ShoppingCar record, @Param("example") ShoppingCarExample example);

    int mergeInto(ShoppingCar record);
}