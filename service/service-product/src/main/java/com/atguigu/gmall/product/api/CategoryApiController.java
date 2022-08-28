package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api(tags = "三级分类的RPC接口")
@RequestMapping("/api/inner/rpc/product")
@RestController
public class CategoryApiController {
    @Autowired
    BaseCategory2Service baseCategory2Service;

    @ApiOperation("三级分类树形结构查询")
    @GetMapping("/category/tree")
    public Result getAllcategoryWithTree(){

List<CategoryTreeTo>list=baseCategory2Service.getAllCategoryWithTree();

        return Result.ok(list);
    }
}
