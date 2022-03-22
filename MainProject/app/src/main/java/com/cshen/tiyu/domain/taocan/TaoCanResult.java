package com.cshen.tiyu.domain.taocan;

import java.util.ArrayList;

import com.cshen.tiyu.domain.Back;

public class TaoCanResult extends Back {
    TaoCanImage imgURL;
    public ArrayList<TaoCan> list;

    public TaoCanImage getImgURL() {
        return imgURL;
    }

    public void setImgURL(TaoCanImage imgURL) {
        this.imgURL = imgURL;
    }

    public ArrayList<TaoCan> getList() {
        return list;
    }

    public void setList(ArrayList<TaoCan> list) {
        this.list = list;
    }

}
