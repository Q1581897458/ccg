package com.mr.web;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.NbException;
import com.mr.Item;
import com.mr.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService service;

    @PostMapping
    public ResponseEntity addItme(Item item){

        if (item.getPrice() == null){

            throw new NbException(ExceptionEnums.PRICE_CANNOT_BE_NULL);

//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("GOULENICUOLE");

        }

        return  ResponseEntity.status(HttpStatus.OK).body(service.saveItem(item));
    }

}
