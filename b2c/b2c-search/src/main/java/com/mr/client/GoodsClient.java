package com.mr.client;

import com.mr.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")// 继承coodsclient方法
public interface GoodsClient extends GoodsApi { }
