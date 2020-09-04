package com.mr;

import com.mr.client.GoodsClient;
import com.mr.common.utils.PageResult;
import com.mr.service.FileStaticService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GoodPageApplication.class})
public class CreateGoodHtml {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private FileStaticService fileStaticService;

    @Test
    public void create(){

        boolean flag=true;
        int page = 0;
        while (flag) {
            PageResult<SpuBo> list = goodsClient.list(page++, 10, "", true);

            if (list.getItems().size()==0){
                flag = false;
            }

            list.getItems().forEach(spuBo -> {
                try {
                    //循环创建静态文件
                    fileStaticService.createStaticHtml(spuBo.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

    }



}
