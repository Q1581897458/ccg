package com.mr.service;



import com.mr.Sku;
import com.mr.Spu;
import com.mr.bo.UserInfo;
import com.mr.client.GoodClient;
import com.mr.common.utils.JsonUtils;
import com.mr.pojo.Cart;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodClient goodClient;

    //规定redis中的key
    static final String KEY_PREFIX = "b2c:cart:uid:";

    public void addCart(Cart cart, UserInfo userInfo) {

        //redos的key怎加用户id
        String key=KEY_PREFIX + userInfo.getId();
        //获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        //查询是否存在
        Long skuId=cart.getSkuId();
        Integer num=cart.getNum();
        //是否购买过
        Boolean boo = hashOps.hasKey(skuId.toString());
        if (boo){
            //存在证明购买过，要进行数量新增
            String json = hashOps.get(skuId.toString()).toString();
            cart= JsonUtils.parse(json,Cart.class);
            cart.setNum(cart.getNum() + num);
        }else {
            //不存在证明没有购买过商品
            cart.setUserId(userInfo.getId());
            //前台只传来skuid和购买数量 其他数据要进行查询
            Sku sku = goodClient.querySkuById(skuId);

            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "":
            StringUtils.split(sku.getImages(),",")[0]);

            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        //将购物车数据写入redis中到hash类行
        hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    public List<Cart> queryCartList(UserInfo userInfo) {

        //判断是否有购物车信息
        String key = KEY_PREFIX + userInfo.getId();
        if (!this.redisTemplate.hasKey(key)){
            //存在，直接返回
            return null;

        }
        //获得绑定key的hash对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);

        List<Object> cartList=hashOps.values();
        //判断是否有数据
        if (CollectionUtils.isEmpty(cartList)){
            return null;
        }

        return cartList.stream().map(o->JsonUtils.parse(o.toString(),Cart.class)).collect(Collectors.toList());
    }

    public void updateNum(Long skuId, Integer num, UserInfo userInfo) {

        String key = KEY_PREFIX + userInfo.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        //拼取购物车某个具体商品
        String json = hashOps.get(skuId.toString()).toString();
        Cart cart = JsonUtils.parse(json, Cart.class);

        cart.setNum(num);

        //写入购物车
        hashOps.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    public void deleteCart(String skuId, UserInfo userInfo) {

        //获取当前购物车信息
       String key = KEY_PREFIX + userInfo.getId();

        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);

        hashOps.delete(skuId);

    }
}
