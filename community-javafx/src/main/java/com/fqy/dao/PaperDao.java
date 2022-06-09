package com.fqy.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.entity.entityutils.ResponseMessage;
import com.fqy.entity.paper.Answer;
import com.fqy.entity.paper.Paper;
import com.fqy.entity.paper.PublishPaper;
import com.fqy.entity.paper.Question;
import com.fqy.cookie.Cache;
import com.fqy.utils.SimpleTools;
import javafx.scene.control.Alert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PaperDao {

    //更新问卷模板名字
    public static boolean updatePaperName(Paper paper) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper";
        HttpEntity<Paper> requestBody = new HttpEntity<>(paper);
        return getResult(restTemplate, url, HttpMethod.PUT, requestBody);
        /*
//        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(url, HttpMethod.PUT, requestBody, new ParameterizedTypeReference<ResponseMessage<?>>() {
//        });
//
//        HttpStatus statusCode = exchange.getStatusCode();
//        if (statusCode != HttpStatus.OK) {
//            return false;
//        }
//        ResponseMessage<?> response = exchange.getBody();
//        if (response != null) {
//            return response.getCode() == 200;
//        }
//        return false;

         */
    }

    //将问卷模板保存至Session中去
    private static boolean addPapersToSession() {
        ObjectMapper om = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/all";
        ResponseMessage<?> response = restTemplate.getForObject(url, ResponseMessage.class);
        if (response != null && response.getCode() == 200) {
            List<?> result = (List<?>) response.getData();
            List<Paper> papers = new ArrayList<>(result.size());
            for (Object obj : result) {
                String paperJsonStr;
                try {
                    paperJsonStr = om.writeValueAsString(obj);
                    Paper paper = om.readValue(paperJsonStr, Paper.class);
                    papers.add(paper);
                    System.out.println(paper);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            Cache.Cookie().put("PapersList", papers);
            //显示出来
            return true;
        }  //            毛都没有,也就不能显示东西了
        return false;
    }

    //从Session中获取问卷模板
    private static List<Paper> getPapersFromSession() {
        List<?> paperList = (List<?>) Cache.Cookie().get("PapersList");
        List<Paper> papers = new ArrayList<>(paperList.size());
        for (Object object : paperList) {
            papers.add((Paper) object);
        }
        return papers;
    }

    //获取问卷模板的全部信息
    public static List<Paper> getPapersFromServer() {
        return addPapersToSession() ? getPapersFromSession() : new ArrayList<>();
    }

    //添加新的问卷模板
    public static boolean addNewPaper(String paperName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/add";
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        HttpEntity<String> requestParams = new HttpEntity<>(paperName);
        return getResult(restTemplate, url, HttpMethod.POST, requestParams);
        /*
//        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(url, HttpMethod.POST, requestParams, new ParameterizedTypeReference<ResponseMessage<?>>() {
//        });
//        ResponseMessage<?> response = exchange.getBody();
//        if (response != null) {
//            return response.getCode() == 200;
//        }
//        return false;

         */
    }

    //从缓存中查询是否存在同名的问卷模板
    public static boolean searchPaperName(String paperName){
        List<Paper> papers = getPapersFromSession();
        for (Paper paper:papers){
            if(paper.getName().equals(paperName))
                return true;
        }
        return false;
    }
    //获取不在问卷里的问题
    public static List<Question> getQuestionNotInPaper(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/question/notIn/" + id;
        ResponseEntity<ResponseMessage<List<Question>>> exchange = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseMessage<List<Question>>>() {
        });
        ResponseMessage<List<Question>> response = exchange.getBody();
        if (response != null && response.getCode() == 200) {
            if (response.getData() != null)
                return response.getData();
        }
        return new ArrayList<>();
    }

    //获取在问卷里的问题
    public static List<Question> getQuestionInPaper(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/question/" + id;
        ResponseEntity<ResponseMessage<List<Question>>> exchange = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseMessage<List<Question>>>() {
        });
        ResponseMessage<List<Question>> response = exchange.getBody();
//        List<Question> questionsInPaper = new ArrayList<>();
        if (response != null && response.getCode() == 200) {
            if (response.getData() != null)
                return response.getData();
        }
        return new ArrayList<>();
    }

    //删除问卷模板
    public static boolean deletePaper(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/" + id;
//        return getResult(restTemplate, url, HttpMethod.DELETE, null);
        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(url, HttpMethod.DELETE, null, new ParameterizedTypeReference<ResponseMessage<?>>() {
        });
        ResponseMessage<?> response = exchange.getBody();
        if (response != null) {
            if(response.getCode()==200){
                SimpleTools.informationDialog(Alert.AlertType.INFORMATION,"提示","成功","删除成功");
                return true;
            }else if(response.getCode()==400){
                SimpleTools.informationDialog(Alert.AlertType.ERROR,"错误","触发了外键约束","请先移除该模板中的问题");
            }
        }
        return false;
    }

    //从当前问卷模板中移除问题
    public static boolean removeQuestionFromPaper(Integer paperId, Integer questionId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/remove/" + paperId + "/" + questionId;
        return getResult(restTemplate, url, HttpMethod.DELETE, null);
        /*
//        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(url, HttpMethod.DELETE, null, new ParameterizedTypeReference<ResponseMessage<?>>() {
//        });
//        ResponseMessage<?> response = exchange.getBody();
//        if (response != null) {
//            return response.getCode() == 200;
//        }
//        return false;
*
         */
    }

    //向当前问卷模板中添加问题
    public static boolean addQuestionToPaper(Integer paperId, Integer questionId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/add/" + paperId + "/" + questionId;
        return getResult(restTemplate, url,  HttpMethod.PUT, null);
    }

    //回答问卷
    public static boolean answerPaper(Answer answer) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/"  + "/answer";
        HttpEntity<Answer> requestEntity = new HttpEntity<>(answer);
        return getResult(restTemplate, url, HttpMethod.POST, requestEntity);
    }

    //提取方法，获取返回结果
    private static boolean getResult(RestTemplate restTemplate, String url, HttpMethod method, HttpEntity<?> requestEntity) {
        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(url, method, requestEntity, new ParameterizedTypeReference<ResponseMessage<?>>() {
        });
        ResponseMessage<?> response = exchange.getBody();
        if (response != null) {
            if(response.getCode() == 200)
                return true;
            else{
//               "题目数量不足,请检查,如数量足够,可能为服务器异常,请稍后再试"
                SimpleTools.informationDialog(Alert.AlertType.ERROR, "错误", response.getMessage(), response.getData().toString());
            }
        }
        return false;
    }

    //根据模板制作问卷
    public static boolean makePaper(Paper paper) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/publish/add";
        HttpEntity<Paper> requestEntity = new HttpEntity<>(paper);
        return getResult(restTemplate, url, HttpMethod.POST, requestEntity);
        /*
//        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ResponseMessage<?>>() {
//        });
//        ResponseMessage<?> response = exchange.getBody();
//        if (response != null) {
//            return response.getCode() == 200;
//        }
//        return false;

         */
    }

    //获取要发布的问卷全部的信息
    public static List<PublishPaper> getAllPublishPaper() {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/publish/all";
        //ParameterizedTypeReference<ResponseMessage<List<PublishPaper>>> extends ParameterizedTypeReference<T> xxx.class
        ResponseEntity<ResponseMessage<List<PublishPaper>>> exchange = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseMessage<List<PublishPaper>>>() {
        });
        ResponseMessage<List<PublishPaper>> response = exchange.getBody();
        if (response != null) {
            return response.getData();
        }
        return new ArrayList<>();
    }

    //更新发布问卷的状态
    public static boolean updatePublishPaperStatus(PublishPaper paper) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = SimpleTools.SERVER_URL + "/paper/publish/update";
        HttpEntity<PublishPaper> requestEntity = new HttpEntity<>(paper);
        return getResult(restTemplate, requestUrl, HttpMethod.PUT, requestEntity);
        /*
//        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(requestUrl, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<ResponseMessage<?>>() {
//        });
//        ResponseMessage<?> response = exchange.getBody();
//        if (response != null) {
//            return response.getCode() == 200;
//        }
//        return false;

         */
    }

    public static boolean getPublishPaperById(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = SimpleTools.SERVER_URL + "/paper/publish/" + id;
        ResponseEntity<ResponseMessage<PublishPaper>> exchange = restTemplate.exchange(requestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseMessage<PublishPaper>>() {
        });
        ResponseMessage<PublishPaper> response = exchange.getBody();
        if (response != null) {
            if (response.getCode() == 200) {
                PublishPaper data = response.getData();
                if (data == null) {
                    return false;
                }else {
                    Cache.Cookie().put("currentPublishPaper",data);
                    return true;
                }

            }
            return false;
        }
        return false;
    }

    public static List<Answer> getAllAnswers() {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/answer/all";
        //ParameterizedTypeReference<ResponseMessage<List<PublishPaper>>> extends ParameterizedTypeReference<T> xxx.class
        ResponseEntity<ResponseMessage<List<Answer>>> exchange = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<ResponseMessage<List<Answer>>>() {
        });
        ResponseMessage<List<Answer>> response = exchange.getBody();
        if (response != null) {
            return response.getData();
        }
        return new ArrayList<>();
    }
}
