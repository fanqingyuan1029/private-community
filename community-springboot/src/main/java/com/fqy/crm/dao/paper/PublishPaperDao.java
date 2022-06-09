package com.fqy.crm.dao.paper;


import com.fqy.crm.entity.paper.PublishPaper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PublishPaperDao {

    @Select("select * from tb_paper_publish")
    List<PublishPaper> selectAll();

    @Select("select * from tb_paper_publish where id = #{id}")
    PublishPaper selectById(@Param("id") Integer id);

    @Insert("insert into tb_paper_publish(name, detail) values (#{publishPaper.name},#{publishPaper.detail})")
    int insert(@Param("publishPaper") PublishPaper paper);

    @Update("update tb_paper_publish set status=1,register_time=now() where id=#{paper.id}")
    int updateStatusToOne(@Param("paper") PublishPaper paper);

    @Update("update tb_paper_publish set status=2,register_time=now(),end_time=DATE_SUB(now(),INTERVAL -7 day),version=version+1 where id=#{paper.id}")
    int updateStatusToTwo(@Param("paper") PublishPaper paper);

}
