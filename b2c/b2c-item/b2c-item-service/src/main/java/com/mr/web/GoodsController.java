package com.mr.web;

import com.mr.SpuBo;
import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.NbException;
import com.mr.common.utils.PageResult;
import com.mr.Sku;
import com.mr.Spu;
import com.mr.SpuDetail;
import com.mr.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("good")
public class GoodsController {

    @Autowired
    private GoodsService service;

    @GetMapping("page")
    public ResponseEntity<PageResult<SpuBo>> list(@RequestParam(value="page",defaultValue = "1") Integer page,
                                                  @RequestParam(value="rows",defaultValue = "5") Integer rows,
                                                  @RequestParam(value="key") String key,
                                                  @RequestParam(value="saleable",defaultValue = "true") Boolean saleable){
        PageResult<SpuBo> pageResult = service.list(page,rows,key,saleable);

        if (pageResult ==null){

            throw new NbException(ExceptionEnums.SPU_ID_NULL);
        }


        return ResponseEntity.ok(pageResult);
    }


    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo bo){

        System.out.println(bo);

        try {
            service.save(bo);
        }catch (Exception e){

            e.printStackTrace();
            throw new NbException(ExceptionEnums.GOODS_ID_NULL);
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }


    @DeleteMapping
    public ResponseEntity<Void> del(@RequestParam("id") long id){

        System.out.println(id);

        service.del(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> queryDeatil(@PathVariable("spuId") Long spuId){

        return ResponseEntity.ok(this.service.queryDeatil(spuId));

    }

    @GetMapping("skuList/{spuId}")
    public ResponseEntity<List<Sku>> querySkus(@PathVariable("spuId") Long spuId){

        return ResponseEntity.ok(this.service.querySkus(spuId));
    }


    //修改商品
    @PutMapping
    public ResponseEntity<Void> upDateGoods(@RequestBody SpuBo bo){

        try {
            service.upDateGoods(bo);
        }catch (Exception e){

            e.printStackTrace();
            throw new NbException(ExceptionEnums.GOODS_ID_NULL);
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }


    //根据spuid查询spu
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long spuId){

        return ResponseEntity.ok(this.service.querySpuById(spuId));
    }

    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id") Long id){

        return ResponseEntity.ok(service.querySkuIByd(id));
    }

}
