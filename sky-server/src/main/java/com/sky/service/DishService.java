package com.sky.service;

import com.sky.dto.DishDTO;

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


}
