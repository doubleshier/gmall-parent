package com.atguigu.gmall.item.service.Impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;
    @Autowired
    ThreadPoolExecutor executor;
    @Autowired
    StringRedisTemplate redisTemplate;

    public SkuDetailTo getSkuDetailFromRpc(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();

        //以前   一次远程超级调用【网络交互比较浪费时间】  查出所有数据直接给我们返回
        // 一次远程用 2s
//        CountDownLatch downLatch = new CountDownLatch(6);

        // 6次远程调用 6s + 0.5*6 = 9s

        //同步调用
        //远程调用其实不用等待，各查各的。 异步的方式


        //CompletableFuture.runAsync()// CompletableFuture<Void>  启动一个下面不用它返回结果的异步任务
        //CompletableFuture.supplyAsync()//CompletableFuture<U>  启动一个下面用它返回结果的异步任务

        //1、查基本信息   1s
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
            SkuInfo skuInfo = result.getData();
            detailTo.setSkuInfo(skuInfo);
            return skuInfo;
        }, executor);


        //2、查商品图片信息  1s
        CompletableFuture<Void> imageFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
            skuInfo.setSkuImageList(skuImages.getData());
        }, executor);




        //3、查商品实时价格 2s
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> price = skuDetailFeignClient.getSku1010Price(skuId);
            detailTo.setPrice(price.getData());
        }, executor);


        //4、查销售属性名值
        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Long spuId = skuInfo.getSpuId();
            Result<List<SpuSaleAttr>> saleattrvalues = skuDetailFeignClient.getSkuSaleattrvalues(skuId, spuId);
            detailTo.setSpuSaleAttrList(saleattrvalues.getData());
        }, executor);


        //5、查sku组合
        CompletableFuture<Void> skuVlaueFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<String> sKuValueJson = skuDetailFeignClient.getSKuValueJson(skuInfo.getSpuId());
            detailTo.setValuesSkuJson(sKuValueJson.getData());
        }, executor);



        //6、查分类
        CompletableFuture<Void> categoryFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
            detailTo.setCategoryView(categoryView.getData());
        },executor);
        CompletableFuture
                .allOf(imageFuture,priceFuture,saleAttrFuture,skuVlaueFuture,categoryFuture)
                .join();

        return detailTo;
    }
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {

        String jsonStr = redisTemplate.opsForValue().get("sku:info:" + skuId);
        if("x".equals(jsonStr)){

            return null;
        }
        //
        if(StringUtils.isEmpty(jsonStr)){

            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            //2.2、放入缓存【查到的对象转为json字符串保存到redis】
            String cacheJson = "x";
            if(fromRpc!=null){
                cacheJson =  Jsons.toStr(fromRpc);
                redisTemplate.opsForValue().set("sku:info:" + skuId,cacheJson,7, TimeUnit.DAYS);
            }else {
                redisTemplate.opsForValue().set("sku:info:" + skuId,cacheJson,30,TimeUnit.MINUTES);
            }

            return fromRpc;
        }
        //3、缓存中有. 把json转成指定的对象
        SkuDetailTo skuDetailTo = Jsons.toObj(jsonStr,SkuDetailTo.class);
        return skuDetailTo;
    }

}
