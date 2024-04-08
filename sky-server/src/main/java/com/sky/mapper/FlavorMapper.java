package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FlavorMapper {

    @Select("select * from dish_flavor where dish_id=#{dishId}")
    public List<DishFlavor> getByDishId(Long dishId);

    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    public void deleteByDishId(Long dishId);

    @Insert("insert into dish_flavor(dish_id, name, value) VALUES (#{dishId}, #{name}, #{value})")
    public void insert(DishFlavor dishFlavor);

}
