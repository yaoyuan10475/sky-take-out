package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Autowired
    FlavorMapper flavorMapper;
    
    @Autowired
    SetmealMapper setmealMapper;

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
        Long dishId = dish.getId();
        flavorMapper.deleteByDishId(dishId);

        //添加口味
        List<DishFlavor> dishFlavors = dishVO.getFlavors();

        if(dishFlavors != null && dishFlavors.size()>0){
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
        }
        flavorMapper.insertBatch(dishFlavors);
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

    @Override
    public void insert(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);

        Long dishId = dish.getId();


        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
        }
        flavorMapper.insertBatch(flavors);

    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {

        for(Long id:ids){
            Dish dish = dishMapper.getById(id);
            // 起售中的菜品不能删除
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> setmealIdsByDishIds = setmealMapper.getSetmealIdsByDishIds(ids);

        if (setmealIdsByDishIds != null && setmealIdsByDishIds.size() > 0) {
            //当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        for(Long id:ids){
            dishMapper.deleteById(id);//删除菜品
            flavorMapper.deleteByDishId(id);//删除对应的口味
        }

    }
}
