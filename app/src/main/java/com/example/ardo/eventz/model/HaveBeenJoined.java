package com.example.ardo.eventz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HaveBeenJoined {

    @SerializedName("detail")
    @Expose
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
