package com.mr.mapper;

import com.mr.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BrandMapper extends tk.mybatis.mapper.common.Mapper<Brand> {

    //新增关系表

    @Insert("INSERT INTO tb_category_brand(category_id,brand_id)VALUES(#{cid},#{bid})")
    public int inserBrandCategory(@Param("bid") Long bid,@Param("cid") Long cid);


    @Delete("DELETE FROM tb_category_brand cb WHERE cb.brand_id=#{bid}")
    public int deleteBr(@Param("bid") Long bid);

    //根据分类id查询
    @Select("SELECT * FROM tb_brand WHERE id in(SELECT brand_id FROM tb_category_brand WHERE category_id=${cid})")
    public List<Brand> queryBrandByCid(@Param("cid") Long cid);

}