package com.mr.web;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.NbException;
import com.mr.common.utils.PageResult;
import com.mr.Brand;
import com.mr.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "brand")
public class BrandController {


    @Autowired
    private BrandService service;

    @GetMapping("page")
    public ResponseEntity<PageResult> getBrandPage(@RequestParam(value="page",defaultValue = "1") Integer page,
                                                   @RequestParam(value="rows",defaultValue = "5") Integer rows,
                                                   @RequestParam(value="key",required = false) String key,
                                                   @RequestParam(value="sortBy",required = false) String sortBy,
                                                   @RequestParam(value="desc",required = false) Boolean desc){

        System.out.println(page);

        PageResult<Brand> pageResult = service.getBrandPage(page,rows,key,sortBy,desc);

        if (pageResult == null || pageResult.getItems().size()==0 ){

            throw  new NbException(ExceptionEnums.BRAND_CANNTO_BE_NULL);

        }

        return ResponseEntity.ok(pageResult);
    }


    @PostMapping
    public ResponseEntity<Void> save(Brand brand, @RequestParam("cids") List<Long> cids) {


         /*   System.out.println(brand);
            System.out.println(cids.size());*/

        service.save(brand,cids);

        return ResponseEntity.ok(null);
    }


    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> cids) {


         /*   System.out.println(brand);
            System.out.println(cids.size());*/

        service.update(brand,cids);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("del")
    public ResponseEntity<Void> del(Brand brand, @RequestParam("id") Long id){

        service.del(id,brand);

        return ResponseEntity.ok(null);
    }


    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid){
        List<Brand> list = service.queryBrandByCid(cid);
        return  ResponseEntity.ok(list);

    }

    @GetMapping("queryBrandById")
    public ResponseEntity<Brand> queryBrandById(@RequestParam("id") Long id){

        return ResponseEntity.ok(service.queryBrandById(id)) ;

    }


}
