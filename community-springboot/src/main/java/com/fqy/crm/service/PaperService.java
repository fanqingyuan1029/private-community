package com.fqy.crm.service;

import com.fqy.crm.dao.paper.AnswerDao;
import com.fqy.crm.dao.paper.PaperDao;
import com.fqy.crm.dao.paper.QuestionDao;
import com.fqy.crm.entity.paper.Answer;
import com.fqy.crm.entity.paper.Paper;
import com.fqy.crm.entity.paper.Question;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PaperService {

    @Resource
    private PaperDao paperDao;

    @Resource
    private QuestionDao questionDao;

    @Resource
    private AnswerDao answerDao;

    /**
     * 获取所有的问卷
     *
     * @return 问卷list
     */
    public List<Paper> getAllPapers() {
        List<Paper> papers = paperDao.selectAll();
        for (Paper paper : papers) {
            List<Question> questions = questionDao.selectQuestionsByPaperId(paper.getId());
            paper.setQuestionList(questions);
        }
        System.out.println(papers);
        return papers;
    }

    /**
     * 添加问卷
     *
     * @param paperName 问卷名称
     * @return 修改的行数
     */
    public int addPaper(String paperName) {
        return paperDao.insert(paperName);
    }

    /**
     * 修改问卷的名称
     *
     * @param paper 问卷对象,包含id和准备修改的名字
     * @return 修改的行数
     */
    public int updatePaperName(Paper paper) {
        return paperDao.update(paper);
    }

    /**
     * 删除问卷
     *
     * @param id 问卷id
     * @return 修改的行数
     */
    public int deletePaperById(Integer id) {
        return paperDao.deleteById(id);
    }

    /**
     * 修改问卷中的问题
     *
     * @param paperId    问卷id
     * @param questionId 问题id
     * @param operator   是添加还是删除?
     * @return 改变的行数
     */
    public int updatePaperQuestion(Integer paperId, Integer questionId, String operator) {
        switch (operator) {
            case "添加问题": {
                return paperDao.insertQuestionToPaper(paperId, questionId);
            }
            case "删除问题": {
                return paperDao.deleteQuestionFromPaper(paperId, questionId);
            }
            default:
                return 0;
        }
    }

    /**
     * 用户回答问卷,如果该问题已经回答过了，则覆盖以前的回答，如果没有则插入一条新的数据
     * @param answer 答案实体类,id publishPaperId userId answer
     * @return 改变的行数
     */
    public int answerPaper(Answer answer){
        if(answerDao.selectByPublishPaperIdOldManId(answer)!=null)
            return answerDao.update(answer);
        return answerDao.insert(answer);
    }


    public List<Answer> getAnswersByPaperId(Integer id){
        return answerDao.selectByPaperId(id);
    }

    public List<Answer> getAllAnswers(){
        return answerDao.selectAll();
    }
}
