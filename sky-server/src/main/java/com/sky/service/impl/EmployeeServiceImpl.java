package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
//import org.springframework.data.domain.Page;
import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /***
     * @函数功能：新增员工
     * @param: employeeDTO
     * @return：void
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("EmployeeServiceImpl 当前线程id: " + Thread.currentThread().getId());

        Employee employee = new Employee();
//        对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
//        设置员工状态 1可用  0不可以
        employee.setStatus(StatusConstant.ENABLE);
//        设置初始密码,固定为123456
        employee.setPassword(PasswordConstant.DEFAULT_PASSWORD);
//      设置添加日期和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
//        设置当前记录的创建人id与修改人id
//       TODO 等待完成
       /* employee.setUpdateUser(10L);
        employee.setCreateUser(10L);*/
        employee.setUpdateUser(BaseContext.getCurrentId());
        employee.setCreateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);

    }

    /***
     * @函数功能：分页查询员工
     * @param: employeePageQueryDTO
     * @return：com.sky.result.PageResult
     */
    @Override
    public PageResult pagequery(EmployeePageQueryDTO employeePageQueryDTO) {

        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

//        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

         Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> records = page.getResult();

        return new PageResult(total, records);
    }

    /***
     * @函数功能：启用禁用员工账号
     * @param: status
     * @param: id
     * @return：void
     */
    @Override
    public void startOrStop(Integer status, long id) {
       /* Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);
*/
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.update(employee);

    }

    /***
     * @函数功能：根据员工id查询   修改员工第一步
     * @param: employeeDTO
     * @return：com.sky.result.Result<com.sky.entity.Employee>
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("******");
        return employee;
    }

    /***
     * @函数功能：编辑员工信息 修改员工第二步
     * @param: employeeDTO
     * @return：com.sky.result.Result
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }

}
