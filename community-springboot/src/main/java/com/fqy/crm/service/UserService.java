package com.fqy.crm.service;

import com.fqy.crm.dao.UserDao;
import com.fqy.crm.entity.User;
import com.fqy.crm.exception.ValidaException;
import com.fqy.crm.form.UserLoginForm;
import com.fqy.crm.form.UserRegisterForm;
import com.fqy.crm.form.UserUpdateForm;
import com.fqy.crm.utils.BCryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.applet.Main;


import javax.annotation.Resource;

import java.util.List;

import java.util.regex.Pattern;


@Slf4j
@Service
public class UserService {

    /**
     * @Resource 自动注入：自动新建对象
     */
    @Resource
    private UserDao userDao;

    /**
     * 获取所有的用户
     *
     * @return 返回一个 List 队列
     */
    public List<User> getAll() {
        return userDao.selectAll();
    }

    /**
     * 根据id号查找用户
     *
     * @param id 用户名的id
     * @return 返回一个User对象
     */
    public User getUserById(Integer id) {
        return userDao.selectById(id);
    }


    public User getUserByUserName(String username) {
        return userDao.selectByUserName(username);
    }

    /**
     * 登录操作
     *
     * @param user 将用户的姓名以及密码封装至 User 对象中
     * @return 返回一个 User 对象
     */
    public User login(UserLoginForm user) {
        return userDao.login(user);
    }


    /**
     * 注册操作
     *
     * @param user 将注册的用户信息封装至 User 对象中
     * @return 返回一个 int 值，1代表插入成功，0代表插入失败
     */
    public int register(UserRegisterForm user) {
        User flag = userDao.selectByUserName(user.getUsername());
        if (flag != null) {
            //用户已经存在
            return 0;
        }
        String password = BCryptUtil.encode(user.getPassword());
        user.setPassword(password);
        return userDao.register(user);
    }

    /**
     * 根据用户的id做删除操作
     *
     * @param id 用户的 id
     * @return 返回一个 int 类型的值，1代表删除成功，0代表删除失败
     */
    public int delete(Integer id) {
        return userDao.deleteById(id);
    }

    /**
     * 更新操作
     *
     * @param user 将更新的用户信息封装至 User 对象中
     * @return 返回一个 int 类型的值，1代表更新成功，0代表更新失败
     */
//    public int update(User user) {
//        boolean exceptionThrows = false;
//        ValidaException exception = new ValidaException();
//        if (user.getPassword() != null) {
//            if (!Pattern.matches("^.*(?=.{6,30})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$", user.getPassword())) {
//                exceptionThrows = true;
//                exception.getExceptionMessages().put("密码不合法", "密码应该最少6位，最多30位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符");
//            }
//
//            log.info("原密码：" + user.getPassword());
//            String password = BCryptUtil.encode(user.getPassword());
//            user.setPassword(password);
//            log.info(user.getPassword());
//        }
//        if (user.getName() != null) {
//            if (!Pattern.matches("[\\u4e00-\\u9fa5]+", user.getName())) {
//                exceptionThrows = true;
//                exception.getExceptionMessages().put("姓名不合法", "姓名只能输入中文");
//            }
//        }
//        if (user.getPhone() != null) {
//            if (!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", user.getPhone())) {
//                exceptionThrows = true;
//                exception.getExceptionMessages().put("电话号码不合法", "电话号码只能长为11位的数字");
//            }
//        }
//        if (exceptionThrows) {
//            throw exception;
//        } else {
//            exception = null;
//        }
//        return userDao.update(user);
//    }
    public int update(UserUpdateForm user) {
        if (user.getName() == null && user.getPhone() == null && user.getPassword() == null && user.getType() == null) {
            return 0;
        }

        if (user.getPassword() != null) {
            String password = BCryptUtil.encode(user.getPassword());
            user.setPassword(password);
        }
        return userDao.update(user);
    }

    public static void main(String[] args) {
        String password = BCryptUtil.encode("Bb123!");
        System.out.println(password);
    }
}
