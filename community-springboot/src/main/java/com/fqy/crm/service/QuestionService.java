package com.fqy.crm.service;

import com.fqy.crm.dao.paper.QuestionDao;
import com.fqy.crm.entity.paper.Question;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class QuestionService {

    @Resource
    private QuestionDao questionDao;

    public int addQuestion(Question question){
        return questionDao.register(question);
    }

    public int updateQuestion(Question question){
        return questionDao.update(question);
    }

    public int deleteQuestion(Integer id){
        return questionDao.deleteById(id);
    }

    public List<Question> getAllQuestions() {
        return questionDao.selectAll();
    }

    public List<Question> getAllQuestionsNotInPaperById(Integer id){
        return questionDao.selectQuestionNotInPaperByPaperId(id);
    }

    public List<Question> getAllQuestionsInPaperById(Integer id) {
        return questionDao.selectQuestionsByPaperId(id);
    }
}
