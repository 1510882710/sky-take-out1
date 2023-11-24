package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

/**
 * @基本功能:
 * @program:sky-take-out
 * @author:wfg
 * @create:2023-11-20 11:09:21
 **/
public interface DishService {

    /**
     * 新增菜品和对应的口味
     *
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

/***
 * @函数功能：查询所有菜品
 * @param: dishPageQueryDTO
 * @return：com.sky.result.PageResult
 */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
