package com.mr.service;

import com.mr.mapper.SpecGroupMapper;
import com.mr.mapper.SpecParamMapper;
import com.mr.SpecGroup;
import com.mr.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecServce {

    @Autowired
    private SpecGroupMapper mapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> list(Long cid){

        Example example = new Example(SpecGroup.class);
        example.createCriteria().andEqualTo("cid",cid);

        return mapper.selectByExample(example);
    }


    public List<SpecParam> querySpecParams(Long gid,Long cid,Boolean searching,Boolean generic){
        SpecParam t = new SpecParam();

        if (gid != null && gid != 0){
            t.setGroupId(gid);
        }
        if (cid != null && cid != 0){
            t.setCid(cid);
        }
        if (searching != null) {
            t.setSearching(searching);
        }
        if(generic != null){
            t.setGeneric(generic);
        }

     /*   t.setGroupId(gid);
        t.setCid(cid);
        t.setSearching(searching);
        t.setGeneric(generic);*/
        return this.specParamMapper.select(t);
    }


}
