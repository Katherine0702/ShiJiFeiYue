package com.cshen.tiyu.domain.main;

import java.util.ArrayList;

public class HomeAdsData {
    private long id;
    ArrayList<HomeAdsInfo> adsList = new ArrayList<HomeAdsInfo>();
    String errCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<HomeAdsInfo> getAdsList() {
        return adsList;
    }

    public void setAdsList(ArrayList<HomeAdsInfo> adsList) {
        this.adsList = adsList;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }


}
