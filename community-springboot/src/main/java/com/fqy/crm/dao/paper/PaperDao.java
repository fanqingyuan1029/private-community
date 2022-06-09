package com.fqy.crm.dao.paper;

import com.fqy.crm.entity.paper.Paper;
import com.fqy.crm.entity.paper.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaperDao {

    /**
     * 查找所有的问卷
     * @return 返回List队列
     */
    @Select("select * from tb_paper")
    List<Paper> selectAll();

    @Select("select * from tb_paper where id = #{id}")
    Paper selectById(@Param("id") Integer id );

    @Insert("insert into tb_paper(name) values (#{name})")
    int insert(@Param("name") String name);

    @Delete("delete from tb_paper where id = #{id}")
    int deleteById(@Param("id") Integer id);

    @Update("update tb_paper set name=#{paper.name} where id=#{paper.id}")
    int update(@Param("paper") Paper paper);

    @Insert("insert into tb_paper_question(paper_id,question_id) values(#{paperId},#{questionId})")
    int insertQuestionToPaper(@Param("paperId")Integer id,@Param("questionId")Integer id2);

    @Delete("delete from tb_paper_question where paper_id = #{paperId} and question_id = #{questionId}")
    int deleteQuestionFromPaper(@Param("paperId")Integer id,@Param("questionId")Integer id2);



}
