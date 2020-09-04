package com.mr.web;

import com.mr.SpecGroup;
import com.mr.SpecParam;
import com.mr.service.SpecServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {

    @Autowired
    private SpecServce service;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> list(@PathVariable("cid") Long cid){

        List<SpecGroup> list = service.list(cid);

        return ResponseEntity.ok(list);
    }

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParam(
            @RequestParam(value="gid", required = false) Long gid
            ,@RequestParam(value="cid", required = false) Long cid
            ,@RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic)
            {

        List<SpecParam> list = this.service.querySpecParams(gid,cid,searching,generic);

        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

}
