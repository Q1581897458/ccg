package com.mr.api;

import com.mr.SpuBo;
import com.mr.common.utils.PageResult;
import com.mr.Sku;
import com.mr.Spu;
import com.mr.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("good")
public interface GoodsApi {


    @GetMapping("page")
    public PageResult<SpuBo> list(@RequestParam(value="page",defaultValue = "1") Integer page,
                                                  @RequestParam(value="rows",defaultValue = "5") Integer rows,
                                                  @RequestParam(value="key") String key,
                                                  @RequestParam(value="saleable",defaultValue = "true") Boolean saleable);


    @PostMapping
    public void saveGoods(@RequestBody SpuBo bo);


    @DeleteMapping
    public void del(@RequestParam("id") long id);


    @GetMapping("/spu/detail/{spuId}")
    public SpuDetail queryDeatil(@PathVariable("spuId") Long spuId);

    @GetMapping("skuList/{spuId}")
    public List<Sku> querySkus(@PathVariable("spuId") Long spuId);

    //修改商品
    @PutMapping
    public void upDateGoods(@RequestBody SpuBo bo);

    //根据spuid查询spu
    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long spuId);
    @GetMapping("sku/{id}")
    public Sku querySkuById(@PathVariable("id") Long id);

}
