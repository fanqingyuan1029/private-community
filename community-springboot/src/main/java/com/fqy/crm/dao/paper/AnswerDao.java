package com.fqy.crm.dao.paper;

import com.fqy.crm.entity.paper.Answer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnswerDao {

    @Insert("insert into tb_answer(publish_paper_id, old_man_id, publish_paper_version, answer) values(#{answer.publishPaperId},#{answer.oldManId},#{answer.publishPaperVersion},#{answer.answer})")
    int insert(@Param("answer") Answer answer);

    @Select("select 1 from tb_answer where publish_paper_id = #{answer.publishPaperId} and old_man_id = #{answer.oldManId} and publish_paper_version = #{answer.publishPaperVersion} limit 1")
    Integer selectByPublishPaperIdOldManId(@Param("answer") Answer answer);

    @Update("update tb_answer set answer = #{answer.answer}, create_time = now() where publish_paper_id = #{answer.publishPaperId} and old_man_id = #{answer.oldManId} and publish_paper_version = #{answer.publishPaperVersion}")
    int update(@Param("answer") Answer answer);

    @Delete("delete from tb_answer where id = #{id}")
    int deleteById(@Param("id") Integer id);

    @Select("select * from tb_answer where publish_paper_id = #{publishPaperId}")
    List<Answer> selectByPaperId(@Param("publishPaperId") Integer id);

    @Select("select t1.id, t1.answer, t1.create_time, t1.publish_paper_version,t1.old_man_id,t1.publish_paper_id,t2.name as oldManName,t3.name as publishPaperName from tb_answer t1 left join tb_oldman t2 on t1.old_man_id=t2.id left join tb_paper_publish t3 on t1.publish_paper_id = t3.id ORDER BY t1.old_man_id , t1.create_time desc")
    List<Answer> selectAll();

}
