package com.example.pricecompare.service;

import com.example.pricecompare.vo.Keyword;
import com.example.pricecompare.vo.Product;
import com.example.pricecompare.vo.ProductGrp;

import java.util.Set;

public interface LowestPriceService {
    Set getZsetValue(String key) throws Exception;

    int setNewProduct(Product product);

    int setNewProductGrp(ProductGrp productGrp);

    public int setNewProductGrpKeyword (String keyword, String prodGrpId, double score);

    Keyword getLowestPriceProductByKeyword(String keyword);
}
