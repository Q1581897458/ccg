package com.mr.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.common.utils.PageResult;
import com.mr.mapper.BrandMapper;
import com.mr.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper mapper;

    public PageResult<Brand> getBrandPage(Integer page,Integer rows,String key,String sortBy,Boolean desc){

        //设置分页 每页条数
        PageHelper.startPage(page,rows);
        //过滤条件 排序
        Example example = new Example(Brand.class);
//么胡查询
        if (key != null && !key.equals("")) {
            example.createCriteria().andLike("name","%"+key.trim()+"%");
        }
//排序
        if (sortBy!=null && !sortBy.equals("")){

            example.setOrderByClause(sortBy + (desc?" asc":" desc"));

        }

        Page<Brand> pageInfo=(Page<Brand>) mapper.selectByExample(example);
        return new PageResult<Brand>(pageInfo.getTotal(),pageInfo.getResult());
    }

    @Transactional //多表这间空之事务
    public void save(Brand brand, List<Long> cids){


        mapper.insertSelective(brand);

        for (long cid: cids) {
            mapper.inserBrandCategory(brand.getId(),cid);
        }



    }
    @Transactional //多表这间空之事务
    public void update(Brand brand, List<Long> cids){


        mapper.updateByPrimaryKey(brand);

        mapper.deleteBr(brand.getId());

        for (long cid: cids) {
            mapper.inserBrandCategory(brand.getId(),cid);
        }



    }



    public void del (Long id,Brand brand){

        mapper.deleteByPrimaryKey(id);

        mapper.deleteBr(brand.getId());

    }

    public  List<Brand> queryBrandByCid (Long cid){

        return mapper.queryBrandByCid(cid);
    }


    public Brand queryBrandById(Long id) {

        return mapper.selectByPrimaryKey(id);
    }
}
