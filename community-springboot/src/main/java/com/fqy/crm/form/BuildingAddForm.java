package com.fqy.crm.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BuildingAddForm {

    @NotBlank(message = "楼栋名称不能为空")
    private String name;            //楼栋名字
    /**
     * @JsonIgnore 设置默认值
     */
//    @NotNull(message = "楼栋介绍不能为空")
    @JsonIgnore
    private String detail = "暂无介绍";          //楼栋介绍
}
