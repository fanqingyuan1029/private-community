package com.fqy.crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    private Long id;
    private String url;
    private String method;
    private String startTime;
    private Long executionTime;
    private String responseBody;
}
