package com.mr.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnums {

    PRICE_CANNOT_BE_NULL(400,"价格不能是空"),
    CATEGOUY_CANNTO_BE_NULL(404,"找不着分类"),
    BRAND_CANNTO_BE_NULL(404,"找不商品分类"),
    DEL_ID_NULL(500,"删除失败"),
    GOODS_ID_NULL(500,"商品新增错误"),
    SPU_ID_NULL(404,"SPU无数据");
    private int code;//状态码
    private String msg;//显示信息



}
