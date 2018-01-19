package com.ingic.waterapp.entities;


public class ChildEntity {
    private String title;
    private String desc;
    private String type;

    public ChildEntity(String title, String desc, String type) {
        this.title = title;
        this.desc = desc;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
