package com.fqy.dao;


import com.fqy.utils.SimpleTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.entity.User;
import com.fqy.entity.entityutils.ResponseMessage;
import com.fqy.cookie.Cache;
import javafx.scene.control.Alert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class AdministratorDao {

    //更新操作
    public static boolean replace(Integer id, User user) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/user/" + id;
        HttpEntity<User> httpEntity = new HttpEntity<>(user);
        if (user.getId() != null) {
//            System.out.println(user);
            ResponseEntity<ResponseMessage<Map<String, String>>> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<ResponseMessage<Map<String, String>>>() {
            });
            ResponseMessage<Map<String, String>> response = responseEntity.getBody();
            if (response != null) {
                if (response.getCode() == 400) {
                    StringBuilder stringBuilder = new StringBuilder();
                    response.getData().forEach((key, value) -> stringBuilder.append(key).append(":").append(value).append("\n"));
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "警告", "字段不合法", stringBuilder.toString());
                    return false;
                }else if(response.getCode()==500){
                    SimpleTools.informationDialog(Alert.AlertType.WARNING, "警告", "你啥也没改", response.getMessage());
                    return false;
                }
                else{
                    SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "成功", response.getMessage());
                    return true;
                }

            }
        }
        return false;
    }

    //注册操作
    public static boolean register(String[] properties) {
        String url = SimpleTools.SERVER_URL + "/user/register";
        Map<String, Object> params = new HashMap<>();
        params.put("username", properties[0]);
        params.put("password", properties[1]);
        params.put("name", properties[2]);
        params.put("type", properties[3]);
        params.put("phone", properties[4]);

        return SimpleTools.register(url, params);
    }

    //返回管理员
    public static User getAdminById(Integer id) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/user/" + id;
        ResponseMessage<?> response = restTemplate.getForObject(url, ResponseMessage.class);
        if (response != null && response.getCode() == 200) {
            String userJsonStr = om.writeValueAsString(response.getData());
            User user = om.readValue(userJsonStr, User.class);
            System.out.println(user);
            return user;
        }
        return null;
    }

    private static boolean getUsersToSession() {
        ObjectMapper om = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/user/all";
        ResponseMessage<?> response = restTemplate.getForObject(url, ResponseMessage.class);
        if (response != null && response.getCode() == 200) {
            List<?> result = (List<?>) response.getData();
            List<User> users = new ArrayList<>(result.size());
            for (Object obj : result) {
                String userJsonStr;
                try {
                    userJsonStr = om.writeValueAsString(obj);
                    User user = om.readValue(userJsonStr, User.class);

                    System.out.println(user);

                    if (user.getType() != 4 || user.getType() != 0) {
                        users.add(user);
                    }

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            Cache.Cookie().put("UserList", users);
            //显示出来
            return true;
        }  //            毛都没有,也就不能显示东西了
        return false;
    }

    private static List<User> getUsersFromSession() {
        List<?> userList = (List<?>) Cache.Cookie().get("UserList");
        List<User> users = new ArrayList<>(userList.size());
        for (Object object : userList) {
            users.add((User) object);
        }
        return users;
    }

    //返回普通职工
    public static List<User> getUsers() {
        return getUsersToSession() ? getUsersFromSession() : new ArrayList<>();
    }


    //根据职称返回对应的User集合
    public static List<User> getUsersByType(Integer type) {

        List<User> users = getUsersFromSession();
        //遍历集合中的每一个元素,用户类型不是所需要的类型,就删除之
        users.removeIf(user -> !user.getType().equals(type));
        return users;
    }

    //移除该员工
    public static boolean remove(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/user/" + id;
        ResponseEntity<ResponseMessage<String>> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, new ParameterizedTypeReference<ResponseMessage<String>>() {
        });
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
            ResponseMessage<String> body = responseEntity.getBody();
            if (body != null) {
                if(body.getCode()==200){
                    SimpleTools.informationDialog(Alert.AlertType.INFORMATION,"操作结果","成功",body.getMessage());
                    return true;
                }else if(body.getCode().equals(500)){
                    SimpleTools.informationDialog(Alert.AlertType.ERROR, "操作结果", "失败", body.getMessage());
                }
            }
        }
        return false;
    }

    //根据用户的id返回对应的Officer
    public static User byUserId(Integer id) {
        List<?> result = (List<?>) Cache.Cookie().get("UserList");
        for (Object obj : result) {
            User user = (User) obj;
            if (Objects.equals(user.getId(), id))
                return user;
        }
        return null;
    }

    public static User getUserById(Integer id) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        String url = SimpleTools.SERVER_URL + "/user/" + id;
        ResponseMessage<?> response = restTemplate.getForObject(url, ResponseMessage.class);
        if (response != null && response.getCode() == 200) {
            String jsonStr = objectMapper.writeValueAsString(response.getData());
            return objectMapper.readValue(jsonStr, User.class);
        }
        return null;

    }


    //根据Officer集合返回对应的姓名字符串集合
    public static List<String> getNames(List<User> users) {
        List<String> nameList = new ArrayList<>();
        for (User user : users) {
            nameList.add(user.getName());
        }
        return nameList;
    }

    public static boolean isContain(String username) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/user/detail?username={?}";
        ResponseMessage<?> responseMessage = restTemplate.getForObject(url, ResponseMessage.class, username);
        if (responseMessage != null)
            return responseMessage.getCode() == 200;
        return false;
    }

    /**
     * 用户模糊查询
     *
     * @param str 输入查询的信息
     * @return 返回被查询出来的用户集
     */
    public static List<User> byName(String str) {
        //从缓存中获取所有用户的数据
        List<?> result = (List<?>) Cache.Cookie().get("UserList");
        //创建一个空的用户集，用于存储查询到的结果集
        List<User> users = new ArrayList<>(result.size());
        for (Object obj : result) {
            User tmp = (User) obj;
            if (tmp.getName().contains(str) || showType(tmp.getType()).equals(str) ||
                    tmp.getUsername().contains(str) || tmp.getPhone().equals(str)) {
                users.add(tmp);
            }
        }
        return users;
    }

    private static String showType(Integer num) {
        switch (num) {
            case 1:
                return "护工";
            case 2:
                return "护士";
            case 3:
                return "医生";
            case 0:
                return "未知生物";
            default:
                return "管理员";
        }
    }

    public static List<Integer> getIds(List<User> users) {
        List<Integer> usersIds = new ArrayList<>(users.size());
        for (User user : users) {
            usersIds.add(user.getId());
        }
        return usersIds;
    }

}
