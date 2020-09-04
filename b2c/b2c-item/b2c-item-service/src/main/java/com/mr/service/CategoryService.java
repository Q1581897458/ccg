package com.mr.service;

import com.mr.mapper.CategoryMapper;
import com.mr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper mappera;

    public List<Category> queryCateByPid(Long pid) {

        Category cate = new Category();
        cate.setParentId(pid);
        return mappera.select(cate);
    }


    public void save(Category ory) {

//        Category a = new Category();
//
//        a = mappera.selectByPrimaryKey(ory.getParentId());

//        if (a != null) {
//            a.setIsParent(true);
//            mappera.updateByPrimaryKeySelective(a);
//        } else {
        mappera.insert(ory);
        // }

    }

    public void del(Long id){

        mappera.deleteByPrimaryKey(id);

//        Category ddd= new Category();
//        ddd.setId(id);
//        mappera.delete(ddd);
    }

    public void handleEdit(Long id , String name){

        Category ory = new Category();
        ory.setId(id);
        ory.setName(name);
        mappera.updateByPrimaryKey(ory);
    }
    //**
    public List<Category> queryByBid(Long id){



        return mappera.queryCategoryListByBid(id);

    }


    public List<Category> queryCateListByIds(List<Long> ids) {

        return mappera.selectByIdList(ids);
    }
}