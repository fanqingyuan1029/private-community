package com.fqy.crm.controller;

import com.fqy.crm.entity.User;
import com.fqy.crm.entity.entityutils.ResponseMessage;
import com.fqy.crm.form.UserLoginForm;
import com.fqy.crm.form.UserRegisterForm;
import com.fqy.crm.form.UserUpdateForm;
import com.fqy.crm.service.UserService;
import com.fqy.crm.utils.BCryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Slf4j 注解在类上；为类提供一个 属性名为log 的 log4j 日志对像
 * @RestController @Controller 加在类上 + @ResponseBody 加在类中的每一个方法上
 * @Controller SpringMvc注解, 代表这是一个控制器
 * @ResponseBody 加在控制器中的方法上, 那么这个方法返回的参数会格式化成json返回
 * @RequestMapping 是一个用来处理请求地址映射的注解，可用于类或方法上。用于类上，表示类中的所有响应请求的方法都是以该地址作为父路径。
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    /**
     * @Resource 自动注入：自动新建对象
     */
    @Resource
    private UserService userService;

//    /**
//     *  获取所有用户的信息
//     * @return  返回一个 List<User> 队列
//     */
//    @GetMapping("/all")
//    public List<User> getAll() {
//
//        log.info(users.toString());
//        System.out.println(users);
//
//        return userService.getAll();
//    }

    /**
     * 获取所有用户的信息
     *
     * @return 返回一个装有 List<User>的 ResponseMessage 对象
     */
    @GetMapping("/all")
    public ResponseMessage<List<User>> getAll() {
        List<User> users = userService.getAll();
        if (users != null) {
            for (User user : users) {
                user.setPassword(null);
            }
            return new ResponseMessage<>(200, "查询成功", users);
        }
        return new ResponseMessage<>(500, "查询失败", null);
    }

    /**
     * 根据id号查找用户信息
     *
     * @param id 用户的id
     * @return 返回一个User对象
     */

    @GetMapping("/{id}")
    public ResponseMessage<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword(null);
            return new ResponseMessage<>(200, "查询成功", user);
        }
        return new ResponseMessage<>(500, "查询失败", null);
    }

    /***
     * 根据用户的姓名返回指定用户信息
     * @param username
     * @return 返回一个User对象
     */
    @GetMapping("/detail")
    public ResponseMessage<User> getUserByName(@RequestParam("username") String username) {
        User user = userService.getUserByUserName(username);
        System.out.println(user);
        if (user != null) {
            user.setPassword(null);
            return new ResponseMessage<>(200, "查询成功", user);
        }
        return new ResponseMessage<>(500, "查询失败", null);
    }

    //    @GetMapping("/detail/{username}")
//    public ResponseMessage<User> getUserByName(@PathVariable("username") String username)
    /**
     * @param user 将用户的姓名以及密码封装至 User 对象中
     * @return 返回一个 User 对象
     * @PathVariable 用于获取路径参数，
     * @RequestParam 用于获取查询参数。
     * @RequestParam 将请求中的参数正文中的指定名称的参数映射到对象中
     * 参数正文既可以用 @RequestParam 指定名称依次接收参数，也可以直接映射到对象，但是这时候需要确保各个参数的名称和类中的属性名称完全对应
     * {@RequestParam(value="username",required=false) String username} 代表此 username 参数可以不传递,如果不传递,将会默认值为 null
     * 如果是一个不能为 null 的类型,报错
     * @Content-Type： http 标头的类型 Content-Type: application/json
     * @RequestBody 将请求中的(json)文本正文中的各个参数映射到对象中
     * <p>
     * <p>
     * <p>
     *
     */

//    @PostMapping("/login")
//    public User login(@RequestBody User user) {
//
//        return userService.login(user);
//    }

    /**
     * 登录操作
     * @param user 用户的账户密码封装至User对象
     * @return 返回指定用户
     */
    @PostMapping("/login")
    public ResponseMessage<User> login(@RequestBody @Valid UserLoginForm user) {

        User result = userService.login(user);
        if (result != null) {
            boolean match = BCryptUtil.match(user.getPassword(), result.getPassword());
            result.setPassword(null);
            if (match) {
                return new ResponseMessage<>(200, "登录成功", result);
            }
        }
        return new ResponseMessage<>(500, "用户名或密码不正确", null);
    }


//    public User login(@RequestParam(value="username",required=false) String username,@RequestParam("password") String password){
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//}

    //    @PostMapping("/login")
//    public User login(@RequestParam("username") String username,@RequestParam("password") String password){
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        System.out.println(username);
//        System.out.println(password);
////      System.out.println(user);
//        return userService.login(user);
//    }

    /**
     * test controller
     *  测试数据
     * @param user  封装用户的信息
     * @return 返回一个 User 对象
     */
//    @PostMapping("/test")
//    public User test(User user){
////    public User login(@RequestParam("username") String username,@RequestParam("password") String password){
////        User user = new User();
////        user.setUsername(username);
////        user.setPassword(password);
////        System.out.println(user);
//        return userService.login(user);
//    }

    /**
     * 注册操作
     *
     * @param user 将注册的用户信息封装至 User 对象中
     * @return 返回一个 String 类型的值。
     */
    @PostMapping("/register")
    public ResponseMessage<?> register(@RequestBody @Valid UserRegisterForm user) {
//        log.info(user.toString());
//
//        log.info(user.toString());
        if (userService.register(user) == 1)
            return new ResponseMessage<>(200, "注册成功");
//        System.out.println(userService.register(user));
        return new ResponseMessage<>(500, "注册失败");
    }

    /**
     * 根据用户的 id 删除用户
     *
     * @param id 用户的 id
     * @return 返回一个 String 类型的值。
     * <p>
     * 目前前端是无返回值函数
     */
    //物理删除用户信息
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") Integer id){
//
//        return userService.delete(id)==1?"删除成功":"删除失败";
//    }
    @DeleteMapping("/{id}")
    public ResponseMessage<?> delete(@PathVariable("id") Integer id) {
        if (userService.delete(id) == 1)
            return new ResponseMessage<>(200, "删除成功");
        return new ResponseMessage<>(500, "删除失败");
    }

    /**
     * 根据用户的 id ,更新用户的信息
     *
     * @param id   用户 id 号
     * @param user 将更新的用户信息 封装至 User 对象中
     * @return 返回一个 String 类型的值。
     * <p>
     * 目前前端是无返回值函数
     */
    @PutMapping("/{id}")
    public ResponseMessage<?> update(@PathVariable("id") Integer id, @RequestBody  @Valid UserUpdateForm user) {
        user.setId(id);
        System.out.println(user);
        if (userService.update(user) == 1)
            return new ResponseMessage<>(200, "更新成功");
        return new ResponseMessage<>(500, "更新失败");
    }


//    @PostMapping("/fuck/delete")
//    public Integer deleteUserById(@RequestParam("id")Integer id){
//        if(id!=)
//    }

}
