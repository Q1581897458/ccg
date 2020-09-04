package com.test;

import com.mr.SearchApplication;
import com.mr.SpuBo;

import com.mr.client.GoodsClient;

import com.mr.common.utils.PageResult;
import com.mr.Goods;
import com.mr.service.GoodsService;
import com.mr.dao.GoodsRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= SearchApplication.class)
public class TestClient {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void testGoodsClient() {

        int  page = 0;
        int count=0;
        boolean load = true;
        while (load) {

            List<Goods> goodsList = new ArrayList();

            //调用client 查询商品
            PageResult<SpuBo> list = goodsClient.list(page++, 10, "", true);
            list.getItems().forEach(oo -> {
                System.out.println(oo.getTitle());
                goodsList.add(goodsService.buildGoodsBySpu(oo));
            });
            goodsRepository.saveAll(goodsList);
            count += goodsList.size();

            //最后一页循环结束
            if (list.getItems().size() <10){
                load =false;
            }
        }
        System.out.println("一共输出多少条数据"+count);

    }

    @Autowired
 private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void createGoodInteger(){
        //创建 goods索引
        elasticsearchTemplate.createIndex(Goods.class);
        //创建
        elasticsearchTemplate.putMapping(Goods.class);

    }


}
