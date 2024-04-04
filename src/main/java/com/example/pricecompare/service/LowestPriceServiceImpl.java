package com.example.pricecompare.service;

import com.example.pricecompare.vo.Keyword;
import com.example.pricecompare.vo.Product;
import com.example.pricecompare.vo.ProductGrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LowestPriceServiceImpl implements LowestPriceService{

    @Autowired
    private RedisTemplate myProdPriceRedis;

    @Override
    public Set getZsetValue(String key) throws Exception{
        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);

        if(myTempSet.size() < 1){
            throw new Exception("The key doesn't have any member");
        }

        return myTempSet;
    }

    @Override
    public int setNewProduct(Product product) {
        myProdPriceRedis.opsForZSet().add(product.getProdGrpId(), product.getProductId(), product.getPrice());

        return myProdPriceRedis.opsForZSet().rank(product.getProdGrpId(), product.getProductId()).intValue();

    }

    @Override
    public int setNewProductGrp(ProductGrp productGrp) {
        List<Product> products = productGrp.getProductList();
        //products 가 하나라는 가정하에...

        for(Product p : products){
            String productId = p.getProductId();
            int price = p.getPrice();
            myProdPriceRedis.opsForZSet().add(productGrp.getProductGrpId(), productId, price);
        }

        return myProdPriceRedis.opsForZSet().zCard(productGrp.getProductGrpId()).intValue();
    }

    @Override
    public int setNewProductGrpKeyword (String keyword, String prodGrpId, double score){
        myProdPriceRedis.opsForZSet().add(keyword, prodGrpId, score);
        return myProdPriceRedis.opsForZSet().rank(keyword, prodGrpId).intValue();

    }

    @Override
    public Keyword getLowestPriceProductByKeyword(String keyword) {
        Keyword answer = new Keyword();
        answer.setKeyword(keyword);
        // keyword를 통해 ProductGrp 가져오기 (10개)
        answer.setProductGrpList(setProdGrpUsingKeywords(keyword));

        return answer;
    }

    public List<ProductGrp> setProdGrpUsingKeywords(String keyword){

        List<ProductGrp> returnList = new ArrayList<>();

        //keyword 로 productGroupId 조회
        List<String> prodGrpIdList = List.copyOf(myProdPriceRedis.opsForZSet().reverseRange(keyword, 0, 9));


        for(final String prodGrpId : prodGrpIdList) {

            ProductGrp productGrp = new ProductGrp();

            //productGroupId로 Product:price 가져오기 10개
            Set prodAndPriceList =  myProdPriceRedis.opsForZSet().rangeWithScores(prodGrpId, 0, 9);
            Iterator<Object> prodPriceObj = prodAndPriceList.iterator();

            List<Product> productList = new ArrayList<>();

            while(prodPriceObj.hasNext()){
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> prodPriceMap = objectMapper.convertValue(prodPriceObj.next(), Map.class);

                Product product = new Product();
                product.setProductId(prodPriceMap.get("value").toString());
                product.setPrice(Double.valueOf(prodPriceMap.get("score").toString()).intValue());
                product.setProdGrpId(prodGrpId);

                productList.add(product);
            }

            productGrp.setProductList(productList);
            productGrp.setProductGrpId(prodGrpId);

            returnList.add(productGrp);
        }

        return returnList;
    }
}
