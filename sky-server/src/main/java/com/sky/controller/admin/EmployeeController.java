package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关的controller接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }


    /***
     * @函数功能：保存新添加用户
     * @param: employeeLoginDTO
     * @return：com.sky.result.Result
     */
    @PostMapping
    @ApiOperation("添加员工save")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新添加员工：{}",employeeDTO);
        System.out.println("Controller 当前线程id: " + Thread.currentThread().getId());

        employeeService.save(employeeDTO);
        return Result.success();
    }

    /****
     * @函数功能：分页查询员工page
     * @param: employeePageQueryDTO
     * @return：com.sky.result.Result<com.sky.result.PageResult>
     */
    @GetMapping("/page")
    @ApiOperation("分页查询员工page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询员工page：{}",employeePageQueryDTO);
       PageResult page = employeeService.pagequery(employeePageQueryDTO);
        return Result.success(page);
    }

    /***
     * @函数功能：启用禁用员工账号
     * @param: status
     * @param: id
     * @return：com.sky.result.Result
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result startOrStop(@PathVariable Integer status ,long id){
        log.info("启用禁用员工账号：status {}   id {}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /***
     * @函数功能：根据员工id查询   修改员工第一步
     * @param: employeeDTO
     * @return：com.sky.result.Result<com.sky.entity.Employee>
     */
    @GetMapping("/{id}")
    @ApiOperation("根据员工id查询")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("根据员工id查询:  id {}",id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /***
     * @函数功能：编辑员工信息 修改员工第二步
     * @param: employeeDTO
     * @return：com.sky.result.Result
     */
    @PutMapping
    @ApiOperation("根据员工id修改")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("根据员工id修改:  employeeDTO {}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }


}
