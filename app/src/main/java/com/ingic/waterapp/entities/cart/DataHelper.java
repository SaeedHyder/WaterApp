package com.ingic.waterapp.entities.cart;

import android.content.Context;
import android.content.Intent;

import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.realm.RealmConstants;

import java.util.Collection;

import io.realm.Realm;

public class DataHelper {
    static Realm realm = Realm.getDefaultInstance();

    // Create 3 counters and insert them into random place of the list.
    public static void randomAddItemAsync(Realm realm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < 3; i++) {
                    MyCartModel.create(realm, true);
                }
            }
        });
    }

    public static void addItemAsync(Realm realm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyCartModel.create(realm);
            }
        });
    }

    public static void deleteItemAsync(final long id) {
        if (realm != null)
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    MyCartModel.delete(realm, id);
                }
            });
    }

    public static void deleteItemsAsync(Collection<Long> ids, final Context context) {
        // Create an new array to avoid concurrency problem.

        final Long[] idsToDelete = new Long[ids.size()];
        ids.toArray(idsToDelete);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Long id : idsToDelete) {
                    MyCartModel.delete(realm, id);
                    hitBroadCast(context, getTotalQuantities());
                }
            }
        });
    }
  /*  public static void deleteItemsAsync(Realm realm, Collection<Integer> ids) {
        // Create an new array to avoid concurrency problem.
        final Integer[] idsToDelete = new Integer[ids.size()];
        ids.toArray(idsToDelete);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Integer id : idsToDelete) {
                    MyCartModel.delete(realm, id);
                }
            }
        });
    }*/

    public static void insertOrUpdate(Realm realm, final MyCartModel model, final Context context) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(model);
                hitBroadCast(context, getTotalQuantities());
            }
        });
    }

    private static void hitBroadCast(Context context, int count) {
        Intent intent = new Intent();
        intent.setAction(AppConstants.CART_COUNT_BROADCAST);
        intent.putExtra(AppConstants.CART_COUNT, count);
        context.sendBroadcast(intent);
    }


    public static int getDbSize(Realm realm) {
        return realm.where(MyCartModel.class).findAll().size();
    }

    public static int getProductQuantity(int productId) {
//        Realm realm = Realm.getDefaultInstance();
        MyCartModel searchItem = realm.where(MyCartModel.class).equalTo(RealmConstants.PRODUCT_ID, productId).findFirst();
        if (searchItem == null) {
            return 0;
        }
        return searchItem.getProductQuantity();
    }

    public static float getProductAmount(int productId) {
//        Realm realm= Realm.getDefaultInstance();
        MyCartModel searchItem = realm.where(MyCartModel.class).equalTo(RealmConstants.PRODUCT_ID, productId).findFirst();
        if (searchItem == null) {
            return 0;
        }
        return searchItem.getProductAmount();
    }

    public static int getTotalQuantities() {
        try {
//            Realm realm = Realm.getDefaultInstance();
            return realm.where(MyCartModel.class).sum(RealmConstants.PRODUCT_QUANTITY).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void addToRealm(Context context, MyCartModel obj) {
//        obj = new MyCartModel(productId, productName, productImage, productLtr, quantity, totalAmount);

        MyCartModel searchItem = realm.where(MyCartModel.class).equalTo(RealmConstants.PRODUCT_ID, obj.getProductId()).findFirst();
        if (searchItem == null) {
            obj.setId(DataHelper.getDbSize(realm) + System.currentTimeMillis());
        } else
            obj.setId(searchItem.getId());

        DataHelper.insertOrUpdate(realm, obj, context);
//         /*===testing===*/
//        MyCartModel testResult = realm.where(MyCartModel.class)
//                .equalTo(RealmConstants.PRODUCT_ID, obj.getProductId()).findFirst();
//
//
//        UIHelper.showShortToastInCenter(context,
//                "Db Id = " + testResult.getId() +
//                        "\nName =" + testResult.getProductName() + "\nQuantity =" + testResult.getProductQuantity());


//        MyCartModel result = realm.where(MyCartModel.class)
//                .equalTo(RealmConstants.PRODUCT_ID, productId).findFirst();


//
//        realm.beginTransaction();
//
//        //if data is not exist
//        if (result == null) {
//            addData(productId, productName, productImage, productLtr,
//                    quantity,
//                    totalAmount);
//        } else {
//            updateData(productId, productName, productImage, productLtr,
//                    quantity,
//                    totalAmount, result);
//        }
//        /*============*/
//
//        realm.commitTransaction();
//
//          /*===testing===*/
//        MyCartModel testResult = realm.where(MyCartModel.class)
//                .equalTo(RealmConstants.PRODUCT_ID, productId).findFirst();
//
//
//        UIHelper.showShortToastInCenter(getDockActivity(),
//                "Db Id = "+testResult.getId()+
//                "\nName ="+testResult.getProductName()+"\nQuantity ="+testResult.getProductQuantity());


//        notifyDataSetChanged();
    }

    public static MyCartModel getData(MyCartModel obj) {
//        obj = new MyCartModel(productId, productName, productImage, productLtr, quantity, totalAmount);

        return realm.where(MyCartModel.class).equalTo(RealmConstants.PRODUCT_ID, obj.getProductId()).findFirst();

    }

    public static void deleteRealmData() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(MyCartModel.class);
            }
        });
    }

}