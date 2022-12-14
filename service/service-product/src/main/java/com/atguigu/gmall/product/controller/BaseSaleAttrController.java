package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class BaseSaleAttrController {
    @Autowired
    BaseSaleAttrService baseSaleAttrService;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @GetMapping("/baseSaleAttrList")
    public Result baseSaleAttrList() {

        List<BaseSaleAttr> list=baseSaleAttrService.list();
        return Result.ok(list);
    }

    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable("spuId")Long spuId){

        List<SpuSaleAttr> spuSaleAttrs=spuSaleAttrService.getSaleAttrAndValueBySpuId(spuId);
        return  Result.ok();
    }
}
