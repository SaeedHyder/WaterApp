package com.ingic.waterapp.entities.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ingic.waterapp.entities.CreateOrder;

import java.lang.reflect.Type;

/**
 * Created by syedatafseer on 2/3/2018.
 */

public class CreateOrderSerializer implements JsonSerializer<CreateOrder> {
    @Override
    public JsonElement serialize(CreateOrder src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("company_id", src.getCompany_id());
        object.addProperty("company_name", src.getCompany_name());
        object.addProperty("cost", src.getCost());
        object.addProperty("service_charge", src.getService_charge());
        object.addProperty("vat_tax", src.getVat_tax());
        object.addProperty("total", src.getTotal());
//        object.addProperty("order_product", src.getOrder_product());
        return object;
    }
}
