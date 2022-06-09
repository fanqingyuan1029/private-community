package com.fqy.crm.dao;

import com.fqy.crm.entity.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


//@Component
@Mapper
public interface LogDao {

    @Insert("insert into tb_log(url, method, start_time," +
            "execution_time, response_body)" +
            "VALUES(#{log.url},#{log.method},#{log.startTime}," +
            "#{log.executionTime},#{log.responseBody})")
    void insertLog(@Param("log") Log log);
}
