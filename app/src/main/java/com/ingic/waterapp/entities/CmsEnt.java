package com.ingic.waterapp.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CmsEnt {

@SerializedName("id")
@Expose
private int id;
@SerializedName("type")
@Expose
private String type;
@SerializedName("title")
@Expose
private String title;
@SerializedName("body")
@Expose
private String body;

public int getId() {
return id;
}

public void setId(int id) {
this.id = id;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getBody() {
return body;
}

public void setBody(String body) {
this.body = body;
}

}