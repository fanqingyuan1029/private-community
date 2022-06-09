package com.fqy.crm.dao.paper;

import com.fqy.crm.entity.paper.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDao {

    @Select("select * from tb_question")
    List<Question> selectAll();

    @Select("select t2.id,t2.name,t2.text from tb_paper_question t1 left join tb_question t2 on t1.question_id=t2.id where t1.paper_id=#{paperId}")
    List<Question> selectQuestionsByPaperId(@Param("paperId") Integer paperId);

    List<Question> selectQuestionNotInPaperByPaperId(@Param("paperId") Integer paperId);

    @Select("select * from tb_question where id = #{id}")
    Question selectById(@Param("id") Integer id);

    @Insert("insert into tb_question(name,text) values (#{question.name},#{question.text})")
    int register(@Param("question") Question question);

    @Delete("delete from tb_question where id = #{id}")
    int deleteById(@Param("id") Integer id );

    int update(@Param("question") Question question);
}

