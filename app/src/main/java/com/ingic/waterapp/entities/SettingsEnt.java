package com.ingic.waterapp.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsEnt {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("company_id")
    @Expose
    private int company_id;
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("vat_tax")
    @Expose
    private String vatTax;
    @SerializedName("service_charges")
    @Expose
    private String serviceCharges;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("company_term")
    @Expose
    private String companyTerm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getVatTax() {
        return vatTax;
    }

    public void setVatTax(String vatTax) {
        this.vatTax = vatTax;
    }

    public String getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(String serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCompanyTerm() {
        return companyTerm;
    }

    public void setCompanyTerm(String companyTerm) {
        this.companyTerm = companyTerm;
    }

}