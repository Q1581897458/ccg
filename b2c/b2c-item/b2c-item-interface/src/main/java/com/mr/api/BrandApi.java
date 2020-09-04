package com.mr.api;


import com.mr.common.utils.PageResult;
import com.mr.Brand;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("brand")
public interface BrandApi {

    @GetMapping("page")
    public PageResult getBrandPage(@RequestParam(value="page",defaultValue = "1") Integer page,
                                                   @RequestParam(value="rows",defaultValue = "5") Integer rows,
                                                   @RequestParam(value="key",required = false) String key,
                                                   @RequestParam(value="sortBy",required = false) String sortBy,
                                                   @RequestParam(value="desc",required = false) Boolean desc);


    @PostMapping
    public void save(Brand brand, @RequestParam("cids") List<Long> cids);

    @PutMapping
    public void updateBrand(Brand brand, @RequestParam("cids") List<Long> cids) ;
    @DeleteMapping("del")
    public void del(Brand brand, @RequestParam("id") Long id);


    @GetMapping("cid/{cid}")
    public List<Brand> queryBrandByCid(@PathVariable("cid") Long cid);

    @GetMapping("queryBrandById")
    public Brand queryBrandById(@RequestParam("id") Long id);

}
