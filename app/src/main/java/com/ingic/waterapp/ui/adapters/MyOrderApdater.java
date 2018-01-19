package com.ingic.waterapp.ui.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.ingic.waterapp.R;
import com.ingic.waterapp.activities.DockActivity;
import com.ingic.waterapp.entities.MyProjectsChildEntity;
import com.ingic.waterapp.entities.MyProjectsParentEntity;
import com.ingic.waterapp.viewholders.MyProjectsChildVH;
import com.ingic.waterapp.viewholders.MyProjectsParentVH;

import java.util.List;

public class MyOrderApdater
        extends ExpandableRecyclerAdapter<MyProjectsParentEntity, MyProjectsChildEntity, MyProjectsParentVH, MyProjectsChildVH> {
    private static final int CHILD_TEXT = 102; ///warning don't use ViewType 0 and 1 its reserved
    private static final int CHILD_IMAGE = 103;
    private static final String TEXT = "text";
    private static final String IMAGE = "image";

    private DockActivity docActivity ;


    private LayoutInflater mInflater;
    private List<MyProjectsParentEntity> parentEntityList;

    public MyOrderApdater(DockActivity context, @NonNull List<MyProjectsParentEntity> parentList) {
        super(parentList);
        this.docActivity= context;
        parentEntityList = parentList;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyProjectsParentVH onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView = mInflater.inflate(R.layout.item_my_order_parent, parentViewGroup, false);
        return new MyProjectsParentVH(recipeView ,docActivity);
    }

    @NonNull
    @Override
    public MyProjectsChildVH onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View layoutImage = mInflater.inflate(R.layout.item_my_order_child, childViewGroup, false);
        //setrecyclerview
        return new MyProjectsChildVH(layoutImage.getContext(),layoutImage);

    }

    @NonNull
    @Override
    public void onBindParentViewHolder(@NonNull MyProjectsParentVH parentViewHolder,
                                       int parentPosition, @NonNull MyProjectsParentEntity parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull MyProjectsChildVH childViewHolder,
                                      int parentPosition, int childPosition, @NonNull MyProjectsChildEntity child) {
        childViewHolder.bind(child);

    }

   /* @Override
    public void onBindChildViewHolder(@NonNull BaseChildVH childViewHolder,
                                      int parentPosition, int childPosition, @NonNull MyProjectsChildEntity child) {
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
//        MyProjectsChildEntity childEntity = parentEntityList.get(parentPosition).getChild(childPosition);
//        if (childEntity.getType() != null && childEntity.getType().equals(IMAGE)) {
//            return CHILD_IMAGE;
//        } else {
//            return CHILD_TEXT;
//        }
        return true;
    */
}
