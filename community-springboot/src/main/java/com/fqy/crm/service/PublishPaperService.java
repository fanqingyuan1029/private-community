package com.fqy.crm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fqy.crm.dao.paper.PublishPaperDao;
import com.fqy.crm.dao.paper.QuestionDao;
import com.fqy.crm.entity.paper.Paper;
import com.fqy.crm.entity.paper.PublishPaper;
import com.fqy.crm.entity.paper.PublishQuestion;
import com.fqy.crm.entity.paper.Question;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PublishPaperService {

    @Resource
    private PublishPaperDao publishPaperDao;

    @Resource
    private QuestionDao questionDao;

    public List<PublishPaper> getAllPublishPaper() {
        return publishPaperDao.selectAll();
    }

    public int addPublishPaper(Paper paper) {
//        Paper select = paperDao.selectById(paper.getId());
        List<Question> questionList = questionDao.selectQuestionsByPaperId(paper.getId());
        if (questionList.size() < 4) {
            return 0;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        List<PublishQuestion> publishQuestionList = new ArrayList<>(questionList.size());
        PublishPaper publishPaper = new PublishPaper();
        try {
            for (int i = 0; i < questionList.size(); i++) {
                Question question = questionList.get(i);
                PublishQuestion publishQuestion = new PublishQuestion();
                publishQuestion.setId(question.getId());
                publishQuestion.setName(question.getName());

                Map<String, String> optionMap = objectMapper.readValue(question.getText(), new TypeReference<Map<String, String>>() {
                });
                publishQuestion.getOptions().addAll(optionMap.values());
                publishQuestionList.add(i, publishQuestion);
            }
            String detailJsonStr = objectMapper.writeValueAsString(publishQuestionList);
            publishPaper.setName(paper.getName());
            publishPaper.setDetail(detailJsonStr);
            return publishPaperDao.insert(publishPaper);
        } catch (JsonProcessingException e) {
            return 0;
        }
    }

    public int update(PublishPaper paper) {
        if(paper.getStatus()==1){
            return publishPaperDao.updateStatusToOne(paper);
        }else {
            return publishPaperDao.updateStatusToTwo(paper);
        }

    }

    public PublishPaper getPublishPaperById(Integer id){
        return publishPaperDao.selectById(id);
    }
}
