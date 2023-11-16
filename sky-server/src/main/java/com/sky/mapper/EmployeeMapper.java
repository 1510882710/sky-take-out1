package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /***
     * @函数功能：新增员工
     * @param: employee
     * @return：void
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user,status) " +
            "values " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    void insert(Employee employee);

    /***
     * @函数功能：员工分页查询
     * @param: employeePageQueryDTO
     * @return：com.github.pagehelper.Page<com.sky.entity.Employee>
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /***
     * @函数功能：员工禁用启用
     * @param: employee
     * @return：void
     */
    void update(Employee employee);

    /***
     * @函数功能：根据员工id查询   修改员工第一步
     * @param: id
     * @return：com.sky.entity.Employee
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
