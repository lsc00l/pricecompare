package com.example.pricecompare.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductGrp {
    private String productGrpId; //FPG0001
    private List<Product> productList; // [{"score":88510.0,"value":"35d6ae5f-f41f-4278-ad31-8a454c5e55c0"}]
}
