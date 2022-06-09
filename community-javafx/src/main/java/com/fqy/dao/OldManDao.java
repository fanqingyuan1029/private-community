package com.fqy.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.entity.OldMan;
import com.fqy.entity.entityutils.ResponseMessage;
import com.fqy.cookie.Cache;
import com.fqy.utils.SimpleTools;
import javafx.scene.control.Alert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.fqy.utils.SimpleTools.SERVER_URL;

public class OldManDao {


    private static boolean addOldMenToSession() {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/oldMan/all";
        /*
//        请求头
//        HttpHeaders requestHeaders = new HttpHeaders();
//        请求的参数
//        Map<String,String> params = new HashMap<>();
//        params.put("xxx","xxx");
//        ...
//        1.@RequestParam
//        HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(params);
//        2.@RequestBody
//        ObjectMapper = new ObjectMapper();
//        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(params));
//        MultiValueMap<Object, Object> objectObjectMultiValueMap = new LinkedMultiValueMap<>();
*/
        ResponseEntity<ResponseMessage<List<OldMan>>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseMessage<List<OldMan>>>() {
        });
        ResponseMessage<List<OldMan>> response = responseEntity.getBody();
        if (response != null && response.getCode() == 200) {
            List<OldMan> oldMen = response.getData();

            Cache.Cookie().put("OldMenList", oldMen);
            //显示出来
            return true;
        }  //            毛都没有,也就不能显示东西了
        return false;
    }

    private static List<OldMan> getOldMenFromSession() {
        List<?> oldMenList = (List<?>) Cache.Cookie().get("OldMenList");
        List<OldMan> oldMen = new ArrayList<>(oldMenList.size());
        for (Object object : oldMenList) {
            oldMen.add((OldMan) object);
        }
        return oldMen;
    }

    //返回所有病人
    public static List<OldMan> getOldMen() {
        return addOldMenToSession() ? getOldMenFromSession() : new ArrayList<>();
    }


    //注册病人
    public static boolean addOldMan(OldMan oldMan) {
        String url = SERVER_URL + "/oldMan/register";
        return SimpleTools.register(url, oldMan);
    }

    //更新病人信息
    public static boolean updateOldMan(OldMan oldMan) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/oldMan";
        HttpEntity<OldMan> requestEntity = new HttpEntity<>(oldMan);
        ResponseEntity<ResponseMessage<Object>> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ResponseMessage<Object>>() {
        });
        ResponseMessage<Object> response = responseEntity.getBody();
        if (response != null) {
            if (response.getCode() == 200) {
                SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "成功", "更新成功！");
                return true;
            } else if (response.getCode() == 400) {
                SimpleTools.informationDialog(Alert.AlertType.ERROR, "错误", "请检查您输入的字段", "该电话已被使用");
            } else if (response.getCode() == 500)
                SimpleTools.informationDialog(Alert.AlertType.ERROR, "错误", "你啥也没改", response.getMessage());
        }
        return false;
    }

    //删除病人床位
    public static boolean removeOldManBed(Integer id) {
        RestTemplate restTemplate = new RestTemplate();

        String url = SERVER_URL + "/oldMan/removeBedId";
        HttpEntity<Integer> requestEntity = new HttpEntity<>(id);
        ResponseEntity<ResponseMessage<Object>> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ResponseMessage<Object>>() {
        });
        ResponseMessage<Object> response = responseEntity.getBody();
        if (response != null) {
            return response.getCode() == 200;
        }
        return false;
    }

    //删除病人
    public static boolean deleteOldMan(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/oldMan/" + id;
        ResponseEntity<ResponseMessage<String>> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, new ParameterizedTypeReference<ResponseMessage<String>>() {
        });
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
            ResponseMessage<String> body = responseEntity.getBody();
            if (body != null) {
                if(body.getCode().equals(200)){
                    SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "提示", "成功", body.getMessage());
                    return true;
                }else {
                    SimpleTools.informationDialog(Alert.AlertType.ERROR, "提示", "失败", body.getMessage());
                }
            }
        }
        return false;
    }

    //搜索病人
    public static List<OldMan> searchOldMan(String str) {
        List<?> result = (List<?>) Cache.Cookie().get("OldMenList");
        List<OldMan> oldMen = new ArrayList<>(result.size());
        for (Object obj : result) {
            OldMan tmp = (OldMan) obj;
            if (tmp.getName().contains(str) || (tmp.getSex().equals(1) && str.equals("男")) ||
                    (tmp.getSex().equals(2) && str.equals("女")) ||
                    String.valueOf(tmp.getAge()).equals(str) || tmp.getPhone().equals(str) ||
                    tmp.getContactName().contains(str) || tmp.getContactPhone().equals(str)) {
                oldMen.add(tmp);
            }
        }
        System.out.println("oldMen:" + oldMen);
        return oldMen;
    }

    public static List<OldMan> getNoBedOldMan() {
        ObjectMapper om = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + "/oldMan/add/notHaveBed";
        ResponseMessage<?> response = restTemplate.getForObject(url, ResponseMessage.class);
        if (response != null && response.getCode() == 200) {
            List<?> result = (List<?>) response.getData();
            List<OldMan> oldMen = new ArrayList<>(result.size());
            for (Object obj : result) {
                String oldManJsonStr;
                try {
                    oldManJsonStr = om.writeValueAsString(obj);
                    OldMan oldMan = om.readValue(oldManJsonStr, OldMan.class);
                    oldMen.add(oldMan);
                    System.out.println(oldMan);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            //显示出来
            return oldMen;

        }  // 毛都没有,也就不能显示东西了
        return new ArrayList<>();
    }
}
