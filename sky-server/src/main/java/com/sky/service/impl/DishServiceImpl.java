package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Autowired
    FlavorMapper flavorMapper;

    @Override
    public List<Dish> page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        return dishMapper.page(dishPageQueryDTO);
    }

    @Override
    public void update(DishVO dishVO) {
        Dish dish = new Dish();
        //对象属性拷贝
        BeanUtils.copyProperties(dishVO, dish);

        dishMapper.update(dish);
        //删除dish现有的口味，准备添加新的口味。
        flavorMapper.deleteByDishId(dish.getId());

        //添加口味
        List<DishFlavor> dishFlavors = dishVO.getFlavors();

        if(dishFlavors != null && dishFlavors.size()>0){
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
                flavorMapper.insert(dishFlavor);
            });
        }
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);

        List<DishFlavor> dishFlavors = flavorMapper.getByDishId(id);

        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }
}
