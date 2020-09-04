package com.mr.service;

import com.mr.*;
import com.mr.client.BrandClient;
import com.mr.client.CategoryClient;
import com.mr.client.GoodsClient;
import com.mr.client.SpecClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecClient specClient;

    public Map<String,Object> getGoodInfo(Long stuId){

        //spu的商品信息
        Spu spu=goodsClient.querySpuById(stuId);
        //查询分类
        List<Category> categoryList = categoryClient.queryCateListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //spu的品牌信息brand
        Brand brand=brandClient.queryBrandById(spu.getBrandId());
        //spuDateil详情
        SpuDetail spuDetail = goodsClient.queryDeatil(stuId);
        //规格组
        List<SpecGroup> specGroups = specClient.list(spu.getCid3());
        //规格组填充 规格参数
        specGroups.forEach(specGroup -> {
            //根据规格组id 查询 specparam
           List<SpecParam> specParamsList= specClient.querySpecParam(specGroup.getId(),null,null,null);
            specGroup.setSpecParamList(specParamsList);
        });
        //sku包含库存
        List<Sku> skuList = goodsClient.querySkus(stuId);

        //规格参数
        List<SpecParam> specParamList = specClient.querySpecParam(null, spu.getCid3(), null, false);
        Map<Long, String> specParamMap = new HashMap<>();
        specParamList.forEach(specParam -> {
            specParamMap.put(specParam.getId(),specParam.getName());
        });


        //参数map
        Map<String,Object> map= new HashMap<>();

        map.put("spu",spu);
        map.put("categoryList",categoryList);
        map.put("brand",brand);
        map.put("spuDetail",spuDetail);
        map.put("skuList",skuList);
        map.put("specGroups",specGroups);
        map.put("specParamMap",specParamMap);
        return map;
    }

}
