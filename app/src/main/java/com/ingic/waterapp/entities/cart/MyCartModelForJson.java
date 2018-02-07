package com.ingic.waterapp.entities.cart;

import com.google.gson.annotations.SerializedName;

/**
 * Created by syedatafseer on 1/26/2018.
 */
public class MyCartModelForJson  {



    @SerializedName("product_id")
    private String productId;
//    private String productName;
//    private String productImage;
//    private String productLtr;
    @SerializedName("quantity")
    private int productQuantity;
    @SerializedName("amount")
    private float productAmount;

    public MyCartModelForJson() {
    }

    public MyCartModelForJson(String  productId, int productQuantity, float productAmount) {

//        this.id = dbId;
        this.productId = productId;
//        this.productName = productName;
//        this.productImage = productImage;
//        this.productLtr = productLtr;
        this.productQuantity = productQuantity;
        this.productAmount = productAmount;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public String  getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

  /*  public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductLtr() {
        return productLtr;
    }

    public void setProductLtr(String productLtr) {
        this.productLtr = productLtr;
    }
*/

    public float getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(float productAmount) {
        this.productAmount = productAmount;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }


}
