package com.mr.web;

import com.mr.GoodsPageBo;
import com.mr.common.utils.PageResult;
import com.mr.Goods;
import com.mr.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService  goodsService;

    /**
     * 前天请求
     * key
     * page
     * size
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> queryGoodsPage(@RequestBody GoodsPageBo pageBo) {

        System.out.println("接受到后台"+pageBo.getKey());

        return ResponseEntity.ok(goodsService.queryGoodsPage(pageBo));
    }


}
