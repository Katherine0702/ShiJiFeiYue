package com.bojing.gathering.domain;

import java.util.ArrayList;

/**
 * Created by admin on 2018/10/17.
 */

public class OrderInfoBack extends Back {
    ArrayList<OrderInfo> data;

    public ArrayList<OrderInfo> getData() {
        return data;
    }

    public void setData(ArrayList<OrderInfo> data) {
        this.data = data;
    }
}
