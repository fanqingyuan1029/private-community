package com.fqy.crm.controller;

import com.fqy.crm.entity.entityutils.ResponseMessage;
import com.fqy.crm.entity.paper.Answer;
import com.fqy.crm.entity.paper.Paper;
import com.fqy.crm.entity.paper.PublishPaper;
import com.fqy.crm.entity.paper.Question;
import com.fqy.crm.service.PaperService;
import com.fqy.crm.service.PublishPaperService;
import com.fqy.crm.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/paper")
public class PaperController {

    @Resource
    private PaperService paperService;

    @Resource
    private QuestionService questionService;

    @Resource
    private PublishPaperService publishPaperService;

    /*
      问卷模板部分
     */

    @GetMapping("/all")
    public ResponseMessage<List<Paper>> findAllPapers() {
        List<Paper> allPapers = paperService.getAllPapers();
        if (allPapers.isEmpty()) {
            return new ResponseMessage<>(600, "还没有问卷呢!");
        } else {
            return new ResponseMessage<>(200, "查询成功", allPapers);
        }
    }

    @PutMapping("")
    public ResponseMessage<?> updatePaper(@RequestBody Paper paper) {
        int flag = paperService.updatePaperName(paper);
        if (flag != 0) {
            return new ResponseMessage<>(200, "修改成功");
        }
        return new ResponseMessage<>(500, "出问题了");
    }

    @DeleteMapping("/{id}")
    public ResponseMessage<?> deletePaper(@PathVariable("id") Integer id) {
        int flag = paperService.deletePaperById(id);
        if(flag != 0) return new ResponseMessage<>(200, "ok");
        return new ResponseMessage<>(500, "failed");
    }

    @PutMapping("/add/{paperId}/{questionId}")
    public ResponseMessage<?> addQuestionToPaper(@PathVariable("paperId") Integer paperId, @PathVariable("questionId") Integer questionId) {
        int flag = paperService.updatePaperQuestion(paperId, questionId, "添加问题");
        if (flag != 0) return new ResponseMessage<>(200, "ok");
        return new ResponseMessage<>(500, "failed");
    }

    @DeleteMapping("/remove/{paperId}/{questionId}")
    public ResponseMessage<?> deleteQuestionFromPaper(@PathVariable("paperId") Integer paperId, @PathVariable("questionId") Integer questionId) {
        int flag = paperService.updatePaperQuestion(paperId, questionId, "删除问题");
        if (flag != 0) return new ResponseMessage<>(200, "ok");
        return new ResponseMessage<>(500, "failed");
    }

    @PostMapping("/add")
    public ResponseMessage<?> addPaper(@RequestBody String paperName){
        int flag = paperService.addPaper(paperName);
        if(flag != 0)
            return new ResponseMessage<>(200,"添加成功");
        return new ResponseMessage<>(500,"添加失败");
    }
    /*
     发布问卷部分
     */
    @GetMapping("/publish/all")
    public ResponseMessage<List<PublishPaper>> findAllPublishPapers() {
        List<PublishPaper> allPapers = publishPaperService.getAllPublishPaper();
        if (allPapers.isEmpty()) {
            return new ResponseMessage<>(600, "还没有问卷呢!");
        } else {
            return new ResponseMessage<>(200, "查询成功", allPapers);
        }
    }

    @GetMapping("/publish/{id}")
    public ResponseMessage<PublishPaper> findPublishPaperById(@PathVariable("id") Integer id){
        PublishPaper publishPaper = publishPaperService.getPublishPaperById(id);
        if(publishPaper != null){
            return new ResponseMessage<>(200, "查询成功", publishPaper);
        }else {
            return new ResponseMessage<>(600, "查询失败!");
        }
    }

    @PostMapping("/publish/add")
    public ResponseMessage<?> addPublishPaper(@RequestBody Paper paper){
        int flag = publishPaperService.addPublishPaper(paper);
        if(flag != 0)
            return new ResponseMessage<>(200,"添加成功");
        return new ResponseMessage<>(500,"添加失败","题目数量应大于4道,请检查,如满足,服务器正忙,您请稍后");
    }

    @PutMapping("/publish/update")
    public ResponseMessage<?> updateStatusPublishPaper(@RequestBody PublishPaper paper){
        int flag = publishPaperService.update(paper);
        if(flag != 0) return new ResponseMessage<>(200,"更新成功");
        return new ResponseMessage<>(500,"更新失败","更新失败");
    }
    /*
      问题部分
     */
    @GetMapping("/question")
    public ResponseMessage<List<Question>> findAllQuestions() {
        List<Question> allQuestions = questionService.getAllQuestions();
        if (allQuestions.isEmpty()) {
            return new ResponseMessage<>(600, "还没有问题呢!");
        } else {
            return new ResponseMessage<>(200, "查询成功", allQuestions);
        }
    }

    @GetMapping("/question/notIn/{paperId}")
    public ResponseMessage<List<Question>> findAllQuestionsNotInPaper(@PathVariable("paperId") Integer id) {
        List<Question> notInQuestions = questionService.getAllQuestionsNotInPaperById(id);
        if (notInQuestions.isEmpty()) {
            return new ResponseMessage<>(600, "还没有问题呢!");
        } else {
            return new ResponseMessage<>(200, "查询成功", notInQuestions);
        }
    }

    @GetMapping("/question/{paperId}")
    public ResponseMessage<List<Question>> findAllQuestionsInPaper(@PathVariable("paperId") Integer id) {
        List<Question> InQuestions = questionService.getAllQuestionsInPaperById(id);
        System.out.println(InQuestions.size());
//        System.out.println((InQuestions!=null));
//        System.out.println(InQuestions+" "+InQuestions.isEmpty()+""+(InQuestions.size()>0)+"");
        if (InQuestions.isEmpty()) {
            return new ResponseMessage<>(600, "还没有问题呢!");
        } else {
            return new ResponseMessage<>(200, "查询成功", InQuestions);
        }
    }

    @PostMapping("/question")
    public ResponseMessage<?> addQuestion(@RequestBody Question question) {
        int flag = questionService.addQuestion(question);
        if (flag != 0) return new ResponseMessage<>(200, "ok");
        return new ResponseMessage<>(500, "failed","failed");
    }

    @PutMapping("/question")
    public ResponseMessage<?> updateQuestion(@RequestBody Question question) {
        int flag = questionService.updateQuestion(question);
        if (flag != 0) return new ResponseMessage<>(200, "ok");
        return new ResponseMessage<>(500, "failed","failed");
    }

    @DeleteMapping("/question/{id}")
    public ResponseMessage<?> deleteQuestion(@PathVariable("id") Integer id) {
        int flag = questionService.deleteQuestion(id);
        if (flag != 0) return new ResponseMessage<>(200, "ok");
        return new ResponseMessage<>(500, "failed","failed");
    }

    /*
      回答部分
     */


    @PostMapping("/answer")
    public ResponseMessage<?> answerPaper(@RequestBody Answer answer) {
        int flag = paperService.answerPaper(answer);
        if (flag != 0) return new ResponseMessage<>(200, "ok");
        return new ResponseMessage<>(500, "failed","failed");
    }

//    @GetMapping("/{paperId}/answer")
//    public ResponseMessage<List<Answer>> getAnswers(@PathVariable("paperId")Integer id){
//        List<Answer> answers = paperService.getAnswersByPaperId(id);
//        if (answers.isEmpty()) {
//            return new ResponseMessage<>(600, "还没有人回答呢!");
//        } else {
//            return new ResponseMessage<>(200, "查询成功", answers);
//        }
//    }

    @GetMapping("/answer/all")
    public ResponseMessage<List<Answer>> getAllAnswers(){
        List<Answer> answers = paperService.getAllAnswers();
        if (answers.isEmpty()) {
            return new ResponseMessage<>(600, "还没有人回答呢!");
        } else {
            return new ResponseMessage<>(200, "查询成功", answers);
        }
    }
}
