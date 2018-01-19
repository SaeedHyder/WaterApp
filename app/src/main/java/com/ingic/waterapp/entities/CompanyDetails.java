
package com.ingic.waterapp.entities;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyDetails {

    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("review")
    @Expose
    private Review review;

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

}
