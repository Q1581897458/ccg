package com.mr;

import lombok.Data;

import java.util.Map;

@Data
public class GoodsPageBo {

    private String key;//品牌名

    private Integer page=0;//当前页

    private Integer size=10;//每页条数

    private Map<String,String> filter;//筛选条件

}
