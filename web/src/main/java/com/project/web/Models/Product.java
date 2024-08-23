package com.project.web.Models;

public class Product {

    String productName;

    String productImage;
    String productUrl;

    String productRegularPrice;


    String productSalePrice;
    String productRatingStars;
    String productRatingCounts;

    //Function:get product name
    //Input: name of the product
    //Output: String
    public String getProductName() {
        return productName;
    }

    //Function:set product name
    //Input: name of the product
    //Output: void
    public void setProductName(String productName) {
        this.productName = productName;
    }

    //Function:get product image
    //Input: image of the product
    //Output: String
    public String getProductImage() {
        return productImage;
    }

    //Function:set product image
    //Input: image of the product
    //Output: void
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    //Function:get product url
    //Input: url of the product
    //Output: String
    public String getProductUrl() {
        return productUrl;
    }

    //Function:set product url
    //Input: url of the product
    //Output: void
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }


    //Function:get product  regular price
    //Input: regular price of the product
    //Output: String
    public String getProductRegularPrice() {
        return productRegularPrice;
    }

    //Function:set product  regular price
    //Input: regular price of the product
    //Output: void
    public void setProductRegularPrice(String productRegularPrice) {
        this.productRegularPrice = productRegularPrice;
    }

    //Function:get product  sale price
    //Input: sale price of the product
    //Output: String
    public String getProductSalePrice() {
        return productSalePrice;
    }

    //Function:set product  sale price
    //Input: sale price of the product
    //Output: void
    public void setProductSalePrice(String productSalePrice) {
        this.productSalePrice = productSalePrice;
    }

    //Function:get product Rating Stars
    //Input:  Rating Stars of the product
    //Output: String
    public String getProductRatingStars() {
        return productRatingStars;
    }

    //Function:set product Rating Stars
    //Input:  Rating Stars of the product
    //Output: void
    public void setProductRatingStars(String productRatingStars) {
        this.productRatingStars = productRatingStars;
    }

    //Function:get product Rating Counts
    //Input:  Rating Counts of the product
    //Output: String
    public String getProductRatingCounts() {
        return productRatingCounts;
    }

    //Function:set product Rating Counts
    //Input:  Rating Counts of the product
    //Output: void
    public void setProductRatingCounts(String productRatingCounts) {
        this.productRatingCounts = productRatingCounts;
    }
}
