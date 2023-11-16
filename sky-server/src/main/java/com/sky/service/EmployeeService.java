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

    /***
     * @函数功能：启用禁用员工账号
     * @param: status
     * @param: id
     * @return：void
     */
    void startOrStop(Integer status, long id);

    /***
     * @函数功能：根据员工id查询   修改员工第一步
     * @param: employeeDTO
     * @return：com.sky.result.Result<com.sky.entity.Employee>
     */
    Employee getById(Long id);

    /***
     * @函数功能：编辑员工信息 修改员工第二步
     * @param: employeeDTO
     * @return：com.sky.result.Result
     */
    void update(EmployeeDTO employeeDTO);
}
