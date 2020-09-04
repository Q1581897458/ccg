package com.mr.web;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.NbException;
import com.mr.Category;
import com.mr.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("list")
    public ResponseEntity<List<Category>> getCateList(@RequestParam(value="pid",defaultValue = "0") Long pid){

        List<Category> list= service.queryCateByPid(pid);

        if (list == null && list.size() ==0){

            throw new NbException(ExceptionEnums.CATEGOUY_CANNTO_BE_NULL);

        }
        return ResponseEntity.ok(list);
    }


        @PostMapping("handleAdd")
        public void handleAdd(@RequestBody Category ory){
            service.save(ory);
        }

        @DeleteMapping("del")
        public ResponseEntity del(@RequestParam(value="id") Long id){

            try{
                service.del(id);
            }catch (NbException n){

                throw new NbException(ExceptionEnums.DEL_ID_NULL);

            }

            return ResponseEntity.ok("删除成功");



}
        @PutMapping("handleEdit")
        public void handleEdit(Long id , String name){
            service.handleEdit(id ,name);
        }


        @GetMapping("queryCateoryByBrandId/{id}")
        public ResponseEntity<List<Category>> queryCategoryByBrandId(@PathVariable("id") Long id){

        List<Category> list = service.queryByBid(id);

        if (list.size() ==0){

        }

        return ResponseEntity.ok(list);
        }


        @GetMapping("queryCateListByIds")
        public ResponseEntity<List<Category>> queryCateListByIds(@RequestParam("ids") List<Long> ids){

          List<Category>  list= service.queryCateListByIds(ids);
            return ResponseEntity.ok(list);
        }

}

