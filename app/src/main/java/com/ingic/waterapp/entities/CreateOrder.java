package com.ingic.waterapp.entities;

import java.io.Serializable;

/**
 * Created by syedatafseer on 2/2/2018.
 */
public class CreateOrder implements Serializable {
    int company_id;
    String company_name;
    String cost;
    String service_charge;
    String vat_tax;
    String total;

    public CreateOrder() {
    }

    public CreateOrder(int company_id, String company_name, String cost,
                       String service_charge, String vat_tax, String total) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.cost = cost;
        this.service_charge = service_charge;
        this.vat_tax = vat_tax;
        this.total = total;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getVat_tax() {
        return vat_tax;
    }

    public void setVat_tax(String vat_tax) {
        this.vat_tax = vat_tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
