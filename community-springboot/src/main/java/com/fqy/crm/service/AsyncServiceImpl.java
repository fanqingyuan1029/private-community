package com.fqy.crm.service;

import com.fqy.crm.dao.LogDao;
import com.fqy.crm.entity.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {


    @Resource
    private LogDao logDao;

    @Override
    @Async("logExecutor")
    public void saveControllerLog(Log log) {
        logDao.insertLog(log);
    }
}
