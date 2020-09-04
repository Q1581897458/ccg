package com.mr;

import com.mr.Sku;
import com.mr.Spu;
import com.mr.SpuDetail;
import lombok.Data;

import java.util.List;

@Data
public class SpuBo extends Spu {

    private String categoryName;
    private String brandName;

    private SpuDetail spuDetail;//商品详细信息

    private List<Sku> skus;



}
