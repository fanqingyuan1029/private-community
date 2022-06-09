package com.fqy.entity.paper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishQuestion {

    private Integer id;
    private String name;
    private List<String> options = new ArrayList<>(4);
}
