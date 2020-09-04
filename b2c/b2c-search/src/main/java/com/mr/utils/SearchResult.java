package com.mr.utils;

import com.mr.common.utils.PageResult;
import com.mr.Brand;
import com.mr.Category;
import com.mr.Goods;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class SearchResult extends PageResult<Goods> {

    private List<Category> categoryList;//品牌集合

    private List<Brand> brandList; //分类集合

    private List<Map<String,Object>> specMapList;

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categoryList, List<Brand> brandList,List<Map<String,Object>> specMapList) {
        super(total, totalPage, items);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.specMapList =specMapList;
    }
}


















