package com.cs.server;

/**
 * @author wsx
 */

public class ServerAddress {
    //服务器地址
    public static String SERVER_ADDRESS = "http://10.91.95.206:8080/CashierSystemServer/";
//    public static String SERVER_ADDRESS = "http://192.168.1.48:8080/CashierSystemServer/";

    //用户操作接口
    public static String USER_OP_SERVER = "UserOpServlet";

    //添加商品接口
    public static String ADD_GOODS_SERVER = "AddGoodsServlet";

    //上传商品图片
    public static String ADD_GOODS_IMAGE_SERVER = "UploadImageServlet";

    //获取商品列表
    public static String GET_GOODSLIST_SERVER = "GetGoodsListServlet";

    //搜索商品
    public static String SEARCH_GOOD_SERVER = "SearchGoodsByPointServlet";

    //根据分类获取商品列表
    public static String GET_GOODLIST_BYTYPE = "GetGoodsListByTypeServlet";

    //获取用户列表
    public static String ADMIN_GET_USERLIST = "GetUserListServlet";

    public static String UPLOAD_ORDERS = "AddOrdersServlet";

    public static String GET_ORDERSLIST_BYMONTH = "GetOrdersListByMonthServlet";

    public static String PUBLISH_INFOS = "AddInfosServlet";

    public static String GET_INFOS_LIST = "GetInfosListServlet";

    public static String UPDATE_GOODS = "UpdateGoodsServlet";

    public static String UPDATE_GOODS_IMAGE = "UpdateImageServlet";

    public static String GET_GOODS_BYID = "GetGoodsByIdServlet";

}
