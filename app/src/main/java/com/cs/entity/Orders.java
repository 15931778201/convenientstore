package com.cs.entity;

import java.io.Serializable;

public class Orders implements Serializable{

    private int id;
    //商品id
    private int goodsId;
    //商品标题
    private String goodsTitle;
    //数量
    private String soldNum;
    //总进价
    private String goodsTotalBuyinPrice;
    //总售价
    private String goodsTotalSoldOutPrice;
    //总利润
    private String goodsTotalProfit;
    //操作收银员
    private String soldUser;
    //出售时间
    private String soldTime;
    //出售月份
    private String soldMonth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(String soldNum) {
        this.soldNum = soldNum;
    }

    public String getGoodsTotalBuyinPrice() {
        return goodsTotalBuyinPrice;
    }

    public void setGoodsTotalBuyinPrice(String goodsTotalBuyinPrice) {
        this.goodsTotalBuyinPrice = goodsTotalBuyinPrice;
    }

    public String getGoodsTotalSoldOutPrice() {
        return goodsTotalSoldOutPrice;
    }

    public void setGoodsTotalSoldOutPrice(String goodsTotalSoldOutPrice) {
        this.goodsTotalSoldOutPrice = goodsTotalSoldOutPrice;
    }

    public String getGoodsTotalProfit() {
        return goodsTotalProfit;
    }

    public void setGoodsTotalProfit(String goodsTotalProfit) {
        this.goodsTotalProfit = goodsTotalProfit;
    }

    public String getSoldUser() {
        return soldUser;
    }

    public void setSoldUser(String soldUser) {
        this.soldUser = soldUser;
    }

    public String getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(String soldTime) {
        this.soldTime = soldTime;
    }

    public String getSoldMonth() {
        return soldMonth;
    }

    public void setSoldMonth(String soldMonth) {
        this.soldMonth = soldMonth;
    }



}
