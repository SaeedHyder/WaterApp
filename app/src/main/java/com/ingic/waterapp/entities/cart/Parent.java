package com.ingic.waterapp.entities.cart;

import io.realm.RealmList;
import io.realm.RealmObject;

//@Parcel(implementations = { ParentRealmProxy.class }, value = Parcel.Serialization.BEAN, analyze = { Parent.class })
public class Parent extends RealmObject {
    @SuppressWarnings("unused")
    private RealmList<MyCartModel> itemList;

    public RealmList<MyCartModel> getItemList() {
        return itemList;
    }
}