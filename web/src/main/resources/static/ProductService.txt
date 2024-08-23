package com.project.web.Service;

import com.project.web.Models.Product;
import com.project.web.scrapper.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {


    @Autowired
    WebCrawler webCrawler;


    @Cacheable(cacheNames = "products",key = "#itemToSearch")
    public ResponseEntity<Map<String, List<Product>>> getAllProducts(String itemToSearch){
        System.out.println("Crawling for "+ itemToSearch + " started.");
        List<Product> amazonPoductsList = searchProductFromAmazon(itemToSearch);
        List<Product> bestBuyProductsList = searchProductFromBestBuy(itemToSearch);
          List<Product> visionsProductsList = searchProductsFromVision(itemToSearch);
        Map<String,List<Product>> allProducts = new HashMap<>();
        allProducts.put("amazon",amazonPoductsList);
        allProducts.put("bestbuy",bestBuyProductsList);
        allProducts.put("visions",visionsProductsList);
        System.out.println("Crawling for "+ itemToSearch+" completed.");
        webCrawler.pageRanking(itemToSearch);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    public List<Product> searchProductFromAmazon(String itemToSearch){
        String url = "https://www.amazon.ca/";
        List<Product> amazonPoductsList = webCrawler.fetchAllProductsFromAmazon(url,itemToSearch);
        return amazonPoductsList;
    }

    public List<Product> searchProductFromBestBuy(String itemToSearch){
        String url = "https://www.bestbuy.ca/en-ca";
        List<Product> bestBuyProductsList = webCrawler.fetchAllProductsFromBestBuy(url,itemToSearch);
        return bestBuyProductsList;
    }

    public List<Product> searchProductsFromVision(String itemToSearch){
        String url = "https://www.visions.ca/";
        List<Product> visionsProductsList = webCrawler.fetchAllProductsFromVisions(url,itemToSearch);
        return visionsProductsList;
    }


}
