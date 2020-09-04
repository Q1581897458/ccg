package com.mr.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mr.*;
import com.mr.GoodsPageBo;
import com.mr.client.BrandClient;
import com.mr.client.CategoryClient;
import com.mr.client.GoodsClient;
import com.mr.client.SpecClient;
import com.mr.common.utils.JsonUtils;
import com.mr.common.utils.PageResult;
import com.mr.dao.GoodsRepository;
//import com.mr.pojo.*;
import com.mr.utils.HighLightUtil;
import com.mr.utils.SearchResult;
import org.apache.commons.lang.StringUtils;

import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecClient specClient;


    public Goods buildGoodsBySpu(Spu spu){

        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setSubTitle(spu.getSubTitle());
        goods.setCreateTime(spu.getCreateTime());

        //填充all
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        List<Category> categoryList= categoryClient.queryCateListByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
        List<String> cateName = categoryList.stream().map(category -> {
            return category.getName();
        }).collect(Collectors.toList());
        //all填充完毕
        goods.setAll(spu.getTitle()+""+brand.getName()+cateName.toString());//分类和品牌名称

        //填充price
        List<Long> priceList = new ArrayList<>();

        //填充sku
        List<Sku> skuList=  goodsClient.querySkus(spu.getId());
        //获取需要的数据
        List<Map> skuMapList=new ArrayList<>();
        skuList.forEach(sku->{
            Map map = new HashMap();

            map.put("id",sku.getId());
            map.put("price",sku.getPrice());
            map.put("title",sku.getTitle());
            map.put("image", StringUtils.isEmpty(sku.getImages())?"":sku.getImages().split(",")[0]);
            skuMapList.add(map);

            priceList.add(sku.getPrice());

        });

        //sku 库存
        goods.setSkus(JsonUtils.serialize(skuList));
        //price
        goods.setPrice(priceList);

        //取出规格数据 根据 cid 可用于搜索
        List<SpecParam> specParamsList = specClient.querySpecParam(null,spu.getCid3(),true,null);
        //查询spu下的 二个字段 商品信息等
        SpuDetail detail=goodsClient.queryDeatil(spu.getId());
        detail.getGenericSpec();//同用字段
        detail.getSpecialSpec();//特有属性

        //处理规格参数 json字符串 不好匹配 叫他转换json对象
        Map<Long,String> genericMap=JsonUtils.parseMap(detail.getGenericSpec(),Long.class,String.class);
        Map<Long,List<String>> specMap=JsonUtils.nativeRead(detail.getSpecialSpec(),
                new TypeReference<Map<Long, List<String>>>() {
                });

        //拼装esgoods中的规格
        Map<String,Object> goodsSpecMap = new HashMap<>();

        //拼出 name：value
        specParamsList.forEach(specParam -> {

            specParam.getName();//规格name
            specParam.getId();//规格id
            //记录
            Object value = null;
            if (specParam.getGeneric()){
                value= genericMap.get(specParam.getId());
                //规格是否是bumber类型 英寸（4-5 。5-6）
                if (specParam.getNumeric()){
                    //如果是数组类型，保存值的时候，架构一下 ，将值变成段 //5.2是值 段（0-4，0-5，0-5）
                    value= this.chooseSegment(value.toString(),specParam);
                }
            }else {
                value= specMap.get(specParam.getId());
            }
            goodsSpecMap.put(specParam.getName(),value);
        });

        goods.setSpecs(goodsSpecMap);

        return goods;
    }



    /**
     *
     * 构建段 5.2 ----> 5.0-5.5英寸
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }


    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     *查询商品分页 for es
     * @param pageBo
     * @return
     */
    public PageResult<Goods> queryGoodsPage(GoodsPageBo pageBo){

        //构造器 索引
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //索引不能为null
        if (StringUtils.isNotEmpty(pageBo.getKey())) {
            builder.withQuery(
                    QueryBuilders.boolQuery().must(
                            QueryBuilders.matchQuery("all",pageBo.getKey())
                    )
            );
        }
        //组装过滤 创建query条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //当filter有值的时候 添加过滤条件
        Map<String, String> filter = pageBo.getFilter();
        if(filter != null && filter.size() !=0){

            //循环mao组装must
            Set<String> filterSet = filter.keySet();
            for (String key : filterSet){
                if (key.equals("cid3") || key.equals("brandId")) {
                    boolQueryBuilder.must(
                            QueryBuilders.matchQuery(key, filter.get(key))
                    );
                }else {
                    boolQueryBuilder.must(
                            QueryBuilders.matchQuery("specs."+key+".keyword", filter.get(key))
                    );
                }
                 // boolQueryBuilder.must();
            }

            builder.withFilter(boolQueryBuilder);

        }

        //当前页 需要修正 -1
        int page=pageBo.getPage();
        page=page>0?page-1:0;

        //设置分页
        builder.withPageable(PageRequest.of(page,pageBo.getSize()));
        //结构过滤
        builder.withSourceFilter(new FetchSourceFilter(new String[]{"id","all","skus"},null));

        Page<Goods> goodsPage = goodsRepository.search(builder.build());
        //判断高亮
        if (StringUtils.isNotEmpty(pageBo.getKey())){
            builder.withHighlightFields(new HighlightBuilder.Field("all").preTags("<font color='red'>").postTags("</font>"));
            //elasticsearchRepository
            Map<Long,String> map = HighLightUtil.getHignLigntMap(elasticsearchTemplate,builder.build(),Goods.class,"all");
            goodsPage.getContent().forEach(goods -> {
                //填充高亮
                goods.setAll(map.get(goods.getId()));
            });

        }

        //计算分页 = 总条数/每页条数 80/10 =8 49/10=5
        Long total=goodsPage.getTotalElements();
        Long totalPage=(long) Math.ceil(total.doubleValue()/pageBo.getSize());

        //构造， 分类 品牌 规格
        //聚合分类 怎加聚会条件 查询结果，获得结果 循环取出值
        builder.addAggregation(AggregationBuilders.terms("cateGro").field("cid3"));
        builder.addAggregation(AggregationBuilders.terms("brandGro").field("brandId"));
        //查询得到结果
        AggregatedPage<Goods> goodsAggregatedPage = (AggregatedPage<Goods>) goodsRepository.search(builder.build());

        LongTerms cidTerms = (LongTerms) goodsAggregatedPage.getAggregation("cateGro");
        //循环分组数据
        List<LongTerms.Bucket> cidBuckets = cidTerms.getBuckets();
        //假设热度最搞得是cid=0 maxDocCount商品数量0
        final List<Long> maxDocCid=new ArrayList<>();
        maxDocCid.add(0l);
        final List<Long> maxDocCount=new ArrayList<>();
        maxDocCount.add(0l);
        //取出cid的集合 //取出cid的集合 拉不大表达式  引用外部变量需要说常量
        List<Long> cidList= cidBuckets.stream().map(bucket -> {
            //如果商品数量 小于下一商品数量 赋值
            if (maxDocCid.get(0)<bucket.getDocCount()){
                maxDocCount.set(0,bucket.getDocCount());
                maxDocCid.set(0,bucket.getKeyAsNumber().longValue());
            }
            return bucket.getKeyAsNumber().longValue();
        }).collect(Collectors.toList());

        //根据cids查询对应的数据
        List<Category> categoryList = categoryClient.queryCateListByIds(cidList);

        System.out.println("最终汇总的分类数据"+categoryList.get(0).getName());

        //汇总品牌
        LongTerms brandGroTerms = (LongTerms) goodsAggregatedPage.getAggregation("brandGro");
        //获得button数据
        List<LongTerms.Bucket> bramdBucket = brandGroTerms.getBuckets();

        //循环查询brand 正规：批量
        List<Brand> brandList =  bramdBucket.stream().map(bucket -> {
            return brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());

        System.out.println("最终会在的品牌数据："+bramdBucket.size());

        //汇总规格筛选条数 specMapList.add("屏幕尺寸":【2-3.4-7】 需要传热度最高的cid
        List<Map<String,Object>> specMapList=this.getSpecMapList(maxDocCid.get(0),pageBo);

        //封装返回数据
       // return new PageResult<Goods>(goodsPage.getTotalElements(),totalPage,goodsPage.getContent());
                return new SearchResult(goodsPage.getTotalElements(),totalPage,goodsPage.getContent(),categoryList,brandList,specMapList);
    }


    /**
     * 查询分类下的规格 es下的规格值
     * @return
     */
    public List<Map<String,Object>> getSpecMapList(Long cid,GoodsPageBo bo){

        List<Map<String,Object>> specMapList=new ArrayList<>();

        //根据cid查询那些规格被筛选
       List<SpecParam> specParamList= specClient.querySpecParam(null,cid,true,null);

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        //设置查询条件 不等于null
        if (StringUtils.isNotEmpty(bo.getKey())){
            builder.withQuery(QueryBuilders.boolQuery().must(
                    QueryBuilders.matchQuery("all",bo.getKey())
            ));
            builder.withPageable(PageRequest.of(0,1));
        }


        //循环规格集合，得到规格名称，根据规格名称做集合

        specParamList.forEach(specParam -> {
            builder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword"));

       });

        //查询得到聚合
        AggregatedPage<Goods> page = (AggregatedPage<Goods>) goodsRepository.search(builder.build());

        //循环规格名称 得到规格名称聚合数据
        specParamList.forEach(specParam -> {
          StringTerms specTerm= (StringTerms) page.getAggregation(specParam.getName());
          //取出单列 规格值
           List<String> specValueList= specTerm.getBuckets().stream().map(bucket -> {
                return  bucket.getKeyAsString();
            }).collect(Collectors.toList());

            //将数据 放入集合
            Map<String,Object> specMap=new HashMap<>();
            specMap.put("values",specValueList);
            specMap.put("key",specParam.getName());
            specMapList.add(specMap);


        });





        return  specMapList;

    }



    /**
     * 根据spuId新增/修改索引
     * @param spuId
     */
    public void  saveGoodForEs(Long spuId){
        Spu spu=  goodsClient.querySpuById(spuId);
        Goods goods=this.buildGoodsBySpu(spu);
        this.goodsRepository.save(goods);
    }

    /**
     * 根据spuId新增/修改索引
     * @param spuId
     */
    public void  deleteGoodForEs(Long spuId){
        this.goodsRepository.deleteById(spuId);
    }




}
