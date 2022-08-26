package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
public class SkuController {
    @Autowired
    SkuInfoService skuInfoService;


    @GetMapping("list/{pn}/{ps}")
    public Result getSkuList(@PathVariable("pn")Long pn ,
                             @PathVariable("ps")Long ps){
        Page<SkuInfo> page =new Page<>();
        Page<SkuInfo> result=skuInfoService.page(page);
        return  Result.ok(result);
    }
    @PostMapping("/saveSkuInfo")
    public Result saveSku(@RequestBody SkuInfo info){


        skuInfoService.saveSkuInfo(info);

        return Result.ok();


    }



    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId")Long skuId){
        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }


    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId")Long skuId){
        skuInfoService.onSale(skuId);
        return Result.ok();
    }
}
