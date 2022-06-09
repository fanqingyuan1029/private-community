package com.fqy.dao;

import com.fqy.utils.SimpleTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.entity.entityutils.ResponseMessage;
import com.fqy.entity.paper.Question;
import com.fqy.cookie.Cache;
import javafx.scene.control.Alert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class QuestionDao {

    public static boolean updateQuestionName(Question question) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/question";
        HttpEntity<Question> requestParams = new HttpEntity<>(question);
        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(url, HttpMethod.PUT, requestParams, new ParameterizedTypeReference<ResponseMessage<?>>() {
        });
        ResponseMessage<?> response = exchange.getBody();
        if (response != null) {
            return response.getCode() == 200;
        }
        return false;
    }

    private static boolean addQuestionsToSession() {
        ObjectMapper om = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/question";
        ResponseMessage<?> response = restTemplate.getForObject(url, ResponseMessage.class);
        if (response != null && response.getCode() == 200) {
            List<?> result = (List<?>) response.getData();
            List<Question> questions = new ArrayList<>(result.size());
            for (Object obj : result) {
                String questionJsonStr;
                try {
                    questionJsonStr = om.writeValueAsString(obj);
                    Question question = om.readValue(questionJsonStr, Question.class);
                    questions.add(question);
                    System.out.println(question);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            Cache.Cookie().put("QuestionsList", questions);
            //显示出来
            return true;
        }  //            毛都没有,也就不能显示东西了
        return false;
    }

    public static List<Question> getQuestionsFromSession() {
        List<?> questionList = (List<?>) Cache.Cookie().get("QuestionsList");
        List<Question> questions = new ArrayList<>(questionList.size());
        for (Object object : questionList) {
            questions.add((Question) object);
        }
        return questions;
    }

    public static List<Question> getQuestionsFromServer() {
        return addQuestionsToSession() ? getQuestionsFromSession() : new ArrayList<>();
    }

    public static boolean addNewQuestion(Question question) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/question";
        HttpEntity<Question> requestParams = new HttpEntity<>(question);
        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(url, HttpMethod.POST, requestParams, new ParameterizedTypeReference<ResponseMessage<?>>() {
        });
        ResponseMessage<?> response = exchange.getBody();
        if (response != null) {
            return response.getCode() == 200;
        }
        return false;
    }

    public static boolean deleteQuestion(Question question) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/paper/question";

        ResponseEntity<ResponseMessage<?>> exchange = restTemplate.exchange(url + "/" + question.getId(), HttpMethod.DELETE, null, new ParameterizedTypeReference<ResponseMessage<?>>() {
        });
        ResponseMessage<?> response = exchange.getBody();
        if (response != null) {
            if(response.getCode() == 200){
                SimpleTools.informationDialog(Alert.AlertType.INFORMATION,"提示","成功","删除成功");
                return true;
            }else if(response.getCode()==400){
                SimpleTools.informationDialog(Alert.AlertType.ERROR,"错误","触发了外键约束","请先移除该模板中的问题");
            }
        }
        return false;
    }

    public static List<Question> searchQuestions(String str) {
        List<?> questionList = getQuestionsFromSession();
        List<Question> questions = new ArrayList<>(questionList.size());
        for (Object obj : questionList) {
            Question tmp = (Question) obj;
            if (tmp.getName().contains(str) || String.valueOf(tmp.getId()).equals(str)) {
                questions.add(tmp);
            }
        }
        System.out.println(questions);
        return questions;
    }
}
