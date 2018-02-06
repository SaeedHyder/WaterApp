package com.ingic.waterapp.ui.adapters.realm;

import android.content.Context;

import com.ingic.waterapp.entities.cart.MyCartModel;

import io.realm.RealmResults;

public class RealmMyCartAdapter extends RealmModelAdapter<MyCartModel> {

    public RealmMyCartAdapter(Context context, RealmResults<MyCartModel> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}