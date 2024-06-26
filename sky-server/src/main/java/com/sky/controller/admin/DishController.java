package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api("菜品相关接口")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    RedisTemplate redisTemplate;


    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        dishService.startOrStop(status, id);
        cleanCache("dish_*");
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除相关菜品")
    public Result delete(@RequestParam List<Long> ids){
        dishService.deleteBatch(ids);
        cleanCache("dish_*");
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新建菜品")
    public Result insert(@RequestBody DishDTO dishDTO){
        dishService.insert(dishDTO);
        cleanCache("dish_" + dishDTO.getCategoryId());
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        //service层查询
       PageResult pageResult = dishService.page(dishPageQueryDTO);
        //整合查询结果

        return Result.success(pageResult);
    }

    @PutMapping
    @ApiOperation("更新菜品")
    public Result updateWithFlavor(@RequestBody DishVO dishVO){
        dishService.update(dishVO);
        cleanCache("dish_*");
        return Result.success();
    }


    @GetMapping("/{id}")
    @ApiOperation("获取菜品")
    public Result getById(@PathVariable Integer id){
        DishVO dishVO = dishService.getByIdWithFlavor((long)id);
        return Result.success(dishVO);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }


}
