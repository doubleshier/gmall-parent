package com.atguigu.gmall.product.service.impl;


import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 王
 * @description 针对表【spu_info(商品表)】的数据库操作Service实现
 * @createDate 2022-08-23 22:16:28
 */
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
        implements SpuInfoService {

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    SpuImageService spuImageService;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SpuSaleAttrValueService spuSaleAttrValueService;

    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo info) {
        spuInfoMapper.insert(info);
        Long spuId = info.getId();
        List<SpuImage> list = info.getSpuImageList();
        for (SpuImage image : list) {
            image.setSpuId(spuId);
        }

        spuImageService.saveBatch(list);


        List<SpuSaleAttr> attrNameList = info.getSpuSaleAttrList();
        for (SpuSaleAttr attr : attrNameList) {
            attr.setSpuId(spuId);
            List<SpuSaleAttrValue> spuSaleAttrList=attr.getSpuSaleAttrValueList();

            for (SpuSaleAttrValue value : spuSaleAttrList) {
                value.setSpuId(spuId);
                String saleAttrName = attr.getSaleAttrName();
                value.setSaleAttrName(saleAttrName);
            }
            spuSaleAttrValueService.saveBatch(spuSaleAttrList);
        }
        spuSaleAttrService.saveBatch(attrNameList);
    }
}




