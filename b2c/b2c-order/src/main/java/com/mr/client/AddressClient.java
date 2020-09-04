package com.mr.client;

import com.mr.order.bo.AddressBo;
//import com.mr.service.api.GoodApi;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;


public class AddressClient  {
    /**
     * 根据地址id查询地址详细
     * @param id
     * @return
     */
    public static AddressBo getAddressByID(Long id){
        return AddressBo.addreddMap.get(id);
    };
}
