package com.cs.entity;

import java.io.Serializable;

public class Goods implements Serializable{
    private int id;
    //标题
    private String goodsTitle;
    //类型
    private String goodsType;
    //图片
    private String goodsImage;
    //进价
    private String goodsBuyInPrice;
    //售价
    private String goodsSoldOutPrice;
    //库存
    private int goodsStock;
    //状态
    private String goodsState;
    //发布用户
    private String goodsPublishUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsBuyInPrice() {
        return goodsBuyInPrice;
    }

    public void setGoodsBuyInPrice(String goodsBuyInPrice) {
        this.goodsBuyInPrice = goodsBuyInPrice;
    }

    public String getGoodsSoldOutPrice() {
        return goodsSoldOutPrice;
    }

    public void setGoodsSoldOutPrice(String goodsSoldOutPrice) {
        this.goodsSoldOutPrice = goodsSoldOutPrice;
    }

    public int getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(int goodsStock) {
        this.goodsStock = goodsStock;
    }

    public String getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(String goodsState) {
        this.goodsState = goodsState;
    }

    public String getGoodsPublishUser() {
        return goodsPublishUser;
    }

    public void setGoodsPublishUser(String goodsPublishUser) {
        this.goodsPublishUser = goodsPublishUser;
    }

}
