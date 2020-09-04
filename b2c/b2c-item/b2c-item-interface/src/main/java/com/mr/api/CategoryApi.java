package com.mr.api;

import com.mr.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {



    @GetMapping("list")
    public List<Category> getCateList(@RequestParam(value="pid",defaultValue = "0") Long pid);




    @PostMapping("handleAdd")
    public void handleAdd(@RequestBody Category ory);

    @DeleteMapping("del")
    public ResponseEntity del(@RequestParam(value="id") Long id);



    @GetMapping("queryCateoryByBrandId/{id}")
    public List<Category> queryCategoryByBrandId(@PathVariable("id") Long id);

    //查询多个分类
    @GetMapping("queryCateListByIds")
    public List<Category> queryCateListByIds(@RequestParam("ids") List<Long> ids);

}
