package com.fqy.crm.dao;

import com.fqy.crm.entity.User;
import com.fqy.crm.form.UserLoginForm;
import com.fqy.crm.form.UserRegisterForm;
import com.fqy.crm.form.UserUpdateForm;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Mapper  使用@Mapper注解要定义成一个接口interface
 * 作用：
 *
 * 1.使用 @Mapper 将 UserDao 接口交给 Spring 进行管理
 *
 * 2.不用写 Mapper 映射文件（XML）
 *
 * 3.为这个 UserDao 接口生成一个实现类，让别的类进行引用
 */
@Mapper
public interface UserDao {

    // select()
    /**
     * 查找所有用户：
     * 方便在使用了删除操作或者更新操作后的从前端获取用户的信息
     * @return  List 队列
     */
    @Select("select * from tb_user where visible = 1")
    List<User> selectAll();

    /**
     * 根据用户的id查找用户的信息
     * @param id  用户的 Id 号
     * @return  返回一个 User 对象
     */
    @Select("select * from tb_user where id = #{id} and visible = 1")
    User selectById(@Param("id") Integer id);


    /**
     * 登录操作
     * @param user  将用户的姓名以及密码封装至 User 对象中
     * @return  返回一个 User 对象
     */
    @Select("select * from tb_user where username = #{user.username} and visible = 1")
    User login(@Param("user") UserLoginForm user);

    /**
     * 根据用户名查找用户的信息
     * @param username  用户名
     * @return  返回一个 User 对象
     */
    @Select("select * from tb_user where username = #{username} and visible = 1" )
    User selectByUserName(@Param("username") String username);


    // @Insert()
    /**
     *  注册操作
     * @param user  将注册的用户信息封装至 User 对象中
     * @return  返回一个 int 值，1代表插入成功，0代表插入失败
     */
    @Insert("insert into tb_user(username,password,name,type,phone) " +
            "values (#{user.username},#{user.password},#{user.name},#{user.type},#{user.phone})")
    int register(@Param("user") UserRegisterForm user);


    // @Delete()
    /**
     * 根据用户的id做删除操作
     * @param id 用户的 id
     * @return  返回一个 int 类型的值，1代表删除成功，0代表删除失败
     */
//    @Delete("delete from tb_user where id = #{id}")
    @Update("update tb_user set visible = 0,last_update_time=now() where id = #{id}")
    int deleteById(@Param("id") Integer id);


    // @Update()
    /**
     * 更新操作
     * @param user  将更新的用户信息封装至 User 对象中
     * @return  返回一个 int 类型的值，1代表更新成功，0代表更新失败
     */
    int update(@Param("user") UserUpdateForm user);
}
