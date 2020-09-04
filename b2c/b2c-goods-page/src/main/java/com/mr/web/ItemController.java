package com.mr.web;

import com.mr.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     *
     * 跳转商品详情界面 接受参数
     * @param spuId
     * @return
     */
    @GetMapping("{id}.html")
    public String toGoodInfo(@PathVariable("id") Long spuId , ModelMap map){

        Map<String, Object> goodMap = itemService.getGoodInfo(spuId);

        map.putAll(goodMap);
        map.put("key","value");
        return "item";
    }


}
