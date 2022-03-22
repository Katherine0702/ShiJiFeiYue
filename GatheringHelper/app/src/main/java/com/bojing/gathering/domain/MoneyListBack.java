package com.bojing.gathering.domain;

import java.util.ArrayList;

/**
 * Created by admin on 2018/10/23.
 */

public class MoneyListBack extends Back{
    ArrayList<MoneyList> data;

    public ArrayList<MoneyList> getData() {
        return data;
    }

    public void setData(ArrayList<MoneyList> data) {
        this.data = data;
    }
}
