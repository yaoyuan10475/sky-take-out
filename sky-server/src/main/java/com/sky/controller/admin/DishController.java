package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api("菜品相关接口")
public class DishController {

    @Autowired
    DishService dishService;


    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        //service层查询
        List<Dish> dishes = dishService.page(dishPageQueryDTO);
        //整合查询结果

        return Result.success(new PageResult(dishes.size(), dishes));
    }

    @PutMapping
    @ApiOperation("更新菜品")
    public Result updateWithFlavor(@RequestBody DishVO dishVO){
        dishService.update(dishVO);

        return Result.success();
    }


    @GetMapping("/{id}")
    @ApiOperation("获取菜品")
    public Result getById(@PathVariable Integer id){
        DishVO dishVO = dishService.getByIdWithFlavor((long)id);
        return Result.success(dishVO);
    }
}
