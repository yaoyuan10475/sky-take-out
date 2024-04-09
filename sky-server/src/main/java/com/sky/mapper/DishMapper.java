package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {



    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    List<Dish> page(DishPageQueryDTO dishPageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    @Delete("delete from dish where id=#{id}")
    void deleteById(Long id);
}
