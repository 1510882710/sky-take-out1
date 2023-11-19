package com.sky.aspect;

/**
 * @基本功能:
 * @program:sky-take-out
 * @author:www wfg
 * @create:2023-11-19 15:36:14
 **/
import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 主要逻辑都在切面类里  拦截1的方法，反射为公共字段赋值
 */
@Aspect//成为切面
@Component//是一个bean交给spring容器管理
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     切入点表达式，对哪些类里的哪些方法进行拦截
     首先定位到哪些包，然后精确到 insert update方法
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     因为sql语句执行后再赋值，无意义
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充...");

        //1,获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象,向下转型，转成methodSignature
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获得数据库操作类型

        //2,获取到当前被拦截的方法的参数--实体对象
        //因为是为实体的对象赋值 如employee
        Object[] args = joinPoint.getArgs();//获得所有参数,约定，获得的实体对象employee作为第一个
        if(args == null || args.length == 0){
            return;
        }

//取第一个参数，放在第一位
//通过object接收  不能如employee 会变
        Object entity = args[0];

        //3,准备赋值的数据
        //如何获取登录用户的id 通过线程 threadlocal  在basecontext工具类调用getCurrentId()
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //4,根据当前不同的操作类型，为对应的属性通过反射来赋值
        //是插入操作时
        if(operationType == OperationType.INSERT){
            //为4个公共字段赋值
            try {
                //反射来赋值
                //通过set赋值，1，获得set方法 得到method对象                                 创建日期方法    参数
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE){
            //是修改操作 2个字段赋值
            //为2个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

