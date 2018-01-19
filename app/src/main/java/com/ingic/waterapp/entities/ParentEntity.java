package com.ingic.waterapp.entities;


import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class ParentEntity implements Parent<ChildEntity> {
    private final List<ChildEntity> childList;
    private String title;
    private String date;

    public ParentEntity(String title, String date, List<ChildEntity> list) {
        this.title = title;
        this.date = date;
        childList = list;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public ChildEntity getChild(int position) {
//        if (childList == null || childList.size() < position) {
//            return null;
//        } else
            return childList.get(position);
    }

    @Override
    public List<ChildEntity> getChildList() {
        return childList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
