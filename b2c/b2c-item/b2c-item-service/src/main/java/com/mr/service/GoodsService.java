package com.mr.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


import com.mr.*;
import com.mr.SpuBo;
import com.mr.common.routintket.MqMessageConstant;
import com.mr.common.utils.PageResult;
import com.mr.mapper.*;
import com.mr.mq.MqMessage;
//import com.mr.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper mapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SpuDatailMapper spuDatailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private MqMessage mqMessage;

    //分页函数
    public PageResult<SpuBo> list(Integer page,Integer rows,String key,Boolean saleable){

        //分页
        PageHelper.startPage(page,rows);

        //条数
        Example example = new Example(Spu.class);


        Example.Criteria  crice= example.createCriteria();

        //判断 名称 key非空
        if (!StringUtils.isNotEmpty(key)) {
        } else {

            crice.andLike("title","%" + key + "%");

        }


        if (saleable != null) {
            //上下架
            crice.andEqualTo("saleable",saleable);
        }



        Page<Spu> pageInfo = (Page<Spu>) mapper.selectByExample(example);

        //返回值加工 商铺--spubo
        List<Spu> spuList = pageInfo.getResult();

        //循环 数据 填充 品牌名 分类
        List<SpuBo> spuBoList = spuList.stream().map(spu -> {

            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu,spuBo);//复制对象属性

            //填充品牌名
            spuBo.setBrandName(brandMapper.selectByPrimaryKey(spuBo.getBrandId()).getName());

            //将三个分类id转为list进行ids查询
            List<Category> categoryList=  categoryMapper.selectByIdList(Arrays.asList(spuBo.getCid1(),spuBo.getCid2(),spuBo.getCid3()));
            //名称list
            List<String> cnameList = categoryList.stream().map(category -> {
                //循环分类 category：循环体
                return category.getName();
            }).collect(Collectors.toList());

            //填充分类                             //list转换字符串
            spuBo.setCategoryName(StringUtils.join(cnameList,"/"));
            return spuBo;
        }).collect(Collectors.toList());



        return new PageResult<SpuBo>(pageInfo.getTotal(),spuBoList);
    }




    @Transactional //多表这间空之事务
    public void save(SpuBo bo){

        Spu spu=new Spu();
        //赋值
        BeanUtils.copyProperties(bo,spu);

        Date now = new Date();
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(now);
        spu.setLastUpdateTime(now);

        mapper.insert(spu);

        //保存详情
        SpuDetail spuDetail = bo.getSpuDetail();
        //取spu id挡住建
        spuDetail.setSpuId(spu.getId());
        spuDatailMapper.insert(spuDetail);

        //保存sku集合
        List<Sku> skuList= bo.getSkus();

        this.saveSpuBoAndStok(skuList,spu.getId());

        mqMessage.sendMessage(MqMessageConstant.SPU_EXCHANGE_NAME,MqMessageConstant.SPU_ROUT_KEY_SAVE,spu.getId());

  /*      skuList.forEach(sku -> {

            sku.setSpuId(spu.getId());
            sku.setCreateTime(now);
            sku.setLastUpdateTime(now);

            skuMapper.insert(sku);
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
        });
 */
    }

    //商品详情
    public SpuDetail queryDeatil(Long spuId){

        return spuDatailMapper.selectByPrimaryKey(spuId);
    }//查询sku
    public List<Sku> querySkus(Long spuId){

        Sku sku = new Sku();
        sku.setSpuId(spuId);


        List<Sku> skuList = skuMapper.select(sku);

        skuList.forEach(skus -> {
            //根据id查询对应库存
            skus.setStock(this.stockMapper.selectByPrimaryKey(skus.getId()).getStock());

        });

        //查询 有啥值用啥值查询
        return skuList;
    }

    public void saveSpuBoAndStok(List<Sku> skuList,Long spuId){

        Date now= new Date();
        //循环保存
        skuList.forEach(sku -> {

            sku.setSpuId(spuId);
            sku.setCreateTime(now);
            sku.setLastUpdateTime(now);
            skuMapper.insert(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
        });

    }

    @Transactional
    public void  upDateGoods(SpuBo bo){


        //修改 spu
        Spu spu = new Spu();
        BeanUtils.copyProperties(bo,spu);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setSaleable(null);
        spu.setValid(null);
        mapper.updateByPrimaryKeySelective(spu);

        //修改 detail
        SpuDetail spuDetail = bo.getSpuDetail();
        spuDatailMapper.updateByPrimaryKey(spuDetail);


        //库存修改 先删除库存表在添加已有的库存表 再删除sku表

        //实体类条件查询 设置spuid
        Sku sku = new Sku();
        sku.setSpuId(bo.getId());

        List<Sku> skuList= skuMapper.select(sku);

        if (skuList != null) {
            //删除
            List<Long> skuIds = skuList.stream().map(s -> s.getId()).collect(Collectors.toList());
            //批量删除
            stockMapper.deleteByIdList(skuIds);
            //根据spuId删除
            skuMapper.delete(sku);


            this.saveSpuBoAndStok(bo.getSkus(), spu.getId());
        }
    }





    @Transactional //多表这间空之事务 删除
    public void del(Long spuId){

        mapper.deleteByPrimaryKey(spuId);

        spuDatailMapper.deleteByPrimaryKey(spuId);

  /*      SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spuId);
        spuDatailMapper.delete(spuDetail);
*/
        Sku sku =  new Sku();
        sku.setSpuId(spuId);


        List<Sku> skuList = skuMapper.select(sku);
        System.out.println(skuList);
        List<Long> skuIds = skuList.stream().map(s-> s.getId()).collect(Collectors.toList());
        //批量删除
        stockMapper.deleteByIdList(skuIds);

        skuMapper.delete(sku);
    }

    public Spu querySpuById(Long id) {


        return mapper.selectByPrimaryKey(id);
    }


    public Sku querySkuIByd(Long id){

        return skuMapper.selectByPrimaryKey(id);
    }
}
