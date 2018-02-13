package com.ingic.waterapp.entities.cart;

import com.google.gson.annotations.SerializedName;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//import org.parceler.Parcel;

/**
 * Created by syedatafseer on 1/26/2018.
 */
//@Parcel(implementations = {MyCartModelRealmProxy.class},
//        value = Parcel.Serialization.BEAN,
//        analyze = {MyCartModel.class})
public class MyCartModel extends RealmObject {
    public static final String FIELD_ID = "id";

    private static AtomicInteger INTEGER_COUNTER = new AtomicInteger(0);


    @PrimaryKey
    private long id;
    @SerializedName("product_id")
    private int productId;
    private String productName;
    private String productImage;
    private String productLtr;
    @SerializedName("quantity")
    private int productQuantity;
    @SerializedName("amount")
    private float productAmount;

    public MyCartModel() {
    }

    public MyCartModel(/*long dbId,*/int productId, String productName, String productImage,
                       String productLtr, int productQuantity, float productAmount) {

//        this.id = dbId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productLtr = productLtr;
        this.productQuantity = productQuantity;
        this.productAmount = productAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
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

// public String getCountString() {
//        return Integer.toString(id);
//    }

    //  create() & delete() needs to be called inside a transaction.
    static void create(Realm realm) {
        create(realm, false);
    }

    static void create(Realm realm, boolean randomlyInsert) {
        Parent parent = realm.where(Parent.class).findFirst();
        RealmList<MyCartModel> items = parent.getItemList();
        MyCartModel counter = realm.createObject(MyCartModel.class, increment());
        if (randomlyInsert && items.size() > 0) {
            Random rand = new Random();
            items.listIterator(rand.nextInt(items.size())).add(counter);
        } else {
            items.add(counter);
        }
    }

    static void delete(Realm realm, long id) {
        MyCartModel item = realm.where(MyCartModel.class).equalTo(FIELD_ID, id).findFirst();
        // Otherwise it has been deleted already.
        if (item != null) {
            item.deleteFromRealm();
        }
    }

    private static int increment() {
        return INTEGER_COUNTER.getAndIncrement();
    }
}
