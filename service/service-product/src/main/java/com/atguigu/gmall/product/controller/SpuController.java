package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class SpuController {
    @Autowired
    SpuInfoService spuInfoService;
@Autowired
    SpuImageService spuImageService;
    @GetMapping("/{pn}/{ps}")
    public Result getSpuPage(@PathVariable("pn")Long pn,
                             @PathVariable("ps")Long ps,
                             @RequestParam("category3Id") Long category3Id){

       Page<SpuInfo> page= new Page<>(pn,ps);
        QueryWrapper<SpuInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("category3_id",category3Id);
        Page<SpuInfo> result = spuInfoService.page(page, queryWrapper);
        return Result.ok(result);
    }
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo info){
        spuInfoService.saveSpuInfo(info);
        return Result.ok();
    }



    @GetMapping("/spuImageList/{spuId}")
    public Result spuImageList(@PathVariable("spuId")Long spuId){

        QueryWrapper<SpuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        List<SpuImage> list = spuImageService.list(wrapper);
        return Result.ok(list);
    }
}
