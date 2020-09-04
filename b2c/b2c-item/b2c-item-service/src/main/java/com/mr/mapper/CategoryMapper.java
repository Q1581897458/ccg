package com.mr.mapper;

import com.mr.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;

import java.util.List;


@Mapper
public interface CategoryMapper extends tk.mybatis.mapper.common.Mapper<Category>, SelectByIdListMapper<Category,Long> {

    @Select("SELECT * FROM tb_category c WHERE c.id in (SELECT cd.category_id FROM tb_category_brand cd WHERE cd.brand_id = #{bid})")
    public List<Category> queryCategoryListByBid(@Param("bid") Long bid);

}

