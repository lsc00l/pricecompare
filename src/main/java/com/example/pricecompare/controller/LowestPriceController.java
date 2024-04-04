package com.example.pricecompare.controller;

import com.example.pricecompare.service.LowestPriceService;
import com.example.pricecompare.vo.Keyword;
import com.example.pricecompare.vo.Product;
import com.example.pricecompare.vo.ProductGrp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/")
public class LowestPriceController {

    @Autowired
    private LowestPriceService lowestPriceService;

    @GetMapping("/product")
    public Set getZSETvalue(String key){

        try {
            return lowestPriceService.getZsetValue(key);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e
            );
        }

    }

    @PutMapping("/product1")
    public int setNewProduct(@RequestBody Product product){

        return lowestPriceService.setNewProduct(product);
    }

    @PutMapping("/product-group")
    public int setNewProductGroup(@RequestBody ProductGrp productGrp){

        return lowestPriceService.setNewProductGrp(productGrp);
    }
    @PutMapping("/product-group-keyword")
    public int setNewProductGrpKeyword (String keyword, String prodGrpId, double score){
        return lowestPriceService.setNewProductGrpKeyword(keyword, prodGrpId, score);
    }

    @GetMapping("/productprice/lowest")
    public Keyword getLowestPriceProductByKeyword(String keyword){
        return lowestPriceService.getLowestPriceProductByKeyword(keyword);
    }
}
