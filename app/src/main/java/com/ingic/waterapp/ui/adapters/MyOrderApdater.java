package com.ingic.waterapp.ui.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.ingic.waterapp.R;
import com.ingic.waterapp.activities.DockActivity;
import com.ingic.waterapp.entities.MyOrdersChildEntity;
import com.ingic.waterapp.entities.MyOrdersParentEntity;
import com.ingic.waterapp.interfaces.OnChildViewHolderItemClick;
import com.ingic.waterapp.viewholders.MyProjectsChildVH;
import com.ingic.waterapp.viewholders.MyProjectsParentVH;

import java.util.ArrayList;
import java.util.List;

public class MyOrderApdater
        extends ExpandableRecyclerAdapter<MyOrdersParentEntity, MyOrdersChildEntity, MyProjectsParentVH, MyProjectsChildVH>

{
    private static final int CHILD_TEXT = 102; ///warning don't use ViewType 0 and 1 its reserved
    private static final int CHILD_IMAGE = 103;
    private static final String TEXT = "text";
    private static final String IMAGE = "image";

    private DockActivity docActivity;
    OnChildViewHolderItemClick childItemlistener;

    private LayoutInflater mInflater;
    private List<MyOrdersParentEntity> parentEntityList;

    public MyOrderApdater(DockActivity context, OnChildViewHolderItemClick listener) {
        super(new ArrayList<MyOrdersParentEntity>());
        this.docActivity = context;
        childItemlistener = listener;
        mInflater = LayoutInflater.from(context);
    }
//    public MyOrderApdater(DockActivity context, @NonNull List<MyOrdersParentEntity> parentList) {
//        super(parentList);
//        this.docActivity= context;
//        parentEntityList = parentList;
//        mInflater = LayoutInflater.from(context);
//    }

    @NonNull
    @Override
    public MyProjectsParentVH onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView = mInflater.inflate(R.layout.item_my_order_parent, parentViewGroup, false);
        return new MyProjectsParentVH(recipeView, docActivity);
    }

    @NonNull
    @Override
    public MyProjectsChildVH onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View layoutImage = mInflater.inflate(R.layout.item_my_order_child, childViewGroup, false);
        //setrecyclerview
        return new MyProjectsChildVH(docActivity, layoutImage, new OnChildViewHolderItemClick() {
            @Override
            public void onReorderClick(View view, int position, int orderId) {
                childItemlistener.onReorderClick(view, position, orderId);
            }

            @Override
            public void onCancelOrderClick(View view, int position, int orderId) {
                childItemlistener.onCancelOrderClick(view, position, orderId);

            }
        });

    }

    @NonNull
    @Override
    public void onBindParentViewHolder(@NonNull MyProjectsParentVH parentViewHolder,
                                       int parentPosition, @NonNull MyOrdersParentEntity parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull MyProjectsChildVH childViewHolder,
                                      int parentPosition, int childPosition, @NonNull MyOrdersChildEntity child) {
        childViewHolder.bind(child , parentPosition, childPosition);

    }

   /* @Override
    public void onBindChildViewHolder(@NonNull BaseChildVH childViewHolder,
                                      int parentPosition, int childPosition, @NonNull MyOrdersChildEntity child) {
        if (childViewHolder instanceof MyProjectsChildVH) {
            MyProjectsChildVH vh = (MyProjectsChildVH) childViewHolder;
            vh.bind(child, childPosition); // set it according to sub item recycler view



//        }
// else if (childViewHolder instanceof ChildTextVH) {
//            ChildTextVH vh = (ChildTextVH) childViewHolder;
//            vh.bind(child);
//        }
        }
*/
 /*   @Override
    public int getChildViewType(int parentPosition, int childPosition) {
//        MyOrdersChildEntity childEntity = parentEntityList.get(parentPosition).getChild(childPosition);
//        if (childEntity.getType() != null && childEntity.getType().equals(IMAGE)) {
//            return CHILD_IMAGE;
//        } else {
//            return CHILD_TEXT;
//        }
        return true;
    */
}
