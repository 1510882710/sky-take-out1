package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /***
     * @函数功能：新增员工
     * @param: employeeDTO
     * @return：void
     */
    void save(EmployeeDTO employeeDTO);

/***
 * @函数功能：分页查询员工
 * @param: employeePageQueryDTO
 * @return：com.sky.result.PageResult
 */
    PageResult pagequery(EmployeePageQueryDTO employeePageQueryDTO);
}
