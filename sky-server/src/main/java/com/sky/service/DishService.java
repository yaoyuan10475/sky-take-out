package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    List<Dish> page(DishPageQueryDTO dishPageQueryDTO);

    void update(DishVO dishVO);

    DishVO getByIdWithFlavor(Long id);
}
