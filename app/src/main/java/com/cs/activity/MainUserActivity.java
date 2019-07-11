package com.cs.activity;


import android.Manifest;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.adapter.MainListViewAdapter;
import com.cs.adapter.MainUserGridViewAdapter;
import com.cs.entity.Goods;
import com.cs.entity.Orders;
import com.cs.entity.UserBean;
import com.cs.server.ServerAddress;
import com.cs.util.GsonUtils;
import com.cs.view.MyGridView;
import com.cs.view.MyListView;
import com.cs.zxing.android.CaptureActivity;
import com.cs.zxing.common.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
 * 首页
 */
public class MainUserActivity extends Activity implements OnClickListener,OnItemClickListener
{
    private static final int IMAGE_OPEN =1 ;
    private MyGridView gridView;

    private MyListView listView;

    private LinearLayout headViewLayout;

    private ImageView ivShopCar,ivScan;

    private RelativeLayout searchLayout;

    private EditText etSearchView;

    private Button btSeatch;

    private List<Goods> goodslist = new ArrayList<Goods>();

    private Goods getGoods;

    private MainListViewAdapter adapter;

    private int selectNum = 1;

    private EditText etNum;

    public static List<Orders> ordersList = new ArrayList<Orders>();

    private Orders orders = new Orders();

    private static final int QRCODE_SCAN = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainuser);
        initView();
        initGridView();
        getGoodListByType("牛奶饮料");
    }

    private void initView() {
        searchLayout = (RelativeLayout) findViewById(R.id.rl_main_searchview);
        etSearchView = (EditText) findViewById(R.id.et_main_search);
        ivShopCar = (ImageView) findViewById(R.id.iv_main_shopcar);
        ivScan = (ImageView) findViewById(R.id.iv_main_scan);

        searchLayout.setOnClickListener(this);
        etSearchView.setOnClickListener(this);
        ivShopCar.setOnClickListener(this);
        ivScan.setOnClickListener(this);

        etSearchView.setInputType(InputType.TYPE_NULL);

        ordersList = new ArrayList<Orders>();
    }

    /*
     * 初始化导航菜单
     */
    private void initGridView() {
        // TODO Auto-generated method stub
        gridView = (MyGridView) findViewById(R.id.mainuser_gridview_menu);
        gridView.setAdapter(new MainUserGridViewAdapter(this));
        gridView.setOnItemClickListener(this);
    }

    /*
     * 初始化商品列表
     */
    private void initListView() {
        // TODO Auto-generated method stub
        listView = (MyListView) findViewById(R.id.mainuser_listview_info);
        adapter = new MainListViewAdapter(MainUserActivity.this,goodslist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        switch (parent.getId()) {
            case R.id.mainuser_gridview_menu:
                if(position == 0){
                    getGoodListByType("牛奶饮料");
                }else if(position == 1){
                    getGoodListByType("糕点蜜饯");
                }else if(position == 2){
                    getGoodListByType("麻辣肉脯");
                }else if(position == 3){
                    getGoodListByType("其他品类");
                }
                break;
            case R.id.mainuser_listview_info:
                /*Intent intent = new Intent(MainUserActivity.this, GoodsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goods", goodslist.get(position));
                intent.putExtras(bundle);
                intent.putExtra("type", "0");
                startActivity(intent);*/
                if(LoginActivity.userType != null && LoginActivity.userType.equals("youke")){
                    Toast.makeText(MainUserActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }else{
                    if(goodslist.get(position).getGoodsStock() == 0){
                        Toast.makeText(MainUserActivity.this,"该商品已售完",Toast.LENGTH_SHORT).show();
                    }else {

                        List<Integer> sList = new ArrayList<Integer>();
                        if (ordersList.size() > 0) {
                            for (int i = 0; i < ordersList.size(); i++) {
                                sList.add(ordersList.get(i).getGoodsId());
                            }
                            if (sList.contains(goodslist.get(position).getId())) {
                                Toast.makeText(MainUserActivity.this, "该商品已加入购物车", Toast.LENGTH_SHORT).show();
                                Log.i("TAG", "------已存在-----");
                            } else {
                                showAlertDialog(goodslist.get(position));
                            }
                        } else {
                            showAlertDialog(goodslist.get(position));
                        }
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MainUserActivity.this, SearchGoodsActivity.class);
        switch (view.getId()) {
            case R.id.rl_main_searchview:
                if(LoginActivity.userType != null && LoginActivity.userType.equals("youke")){
                    Toast.makeText(MainUserActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(intent);
                }
                break;
            case R.id.et_main_search:
                if(LoginActivity.userType != null && LoginActivity.userType.equals("youke")){
                    Toast.makeText(MainUserActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(intent);
                }
                break;

            case R.id.iv_main_shopcar:
                if(LoginActivity.userType != null && LoginActivity.userType.equals("youke")){
                    Toast.makeText(MainUserActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }else {
                    Intent shopCarIntent = new Intent(MainUserActivity.this, ShopCarActivity.class);
                    startActivity(shopCarIntent);
                }
                break;

            case R.id.iv_main_scan:
                if(LoginActivity.userType != null && LoginActivity.userType.equals("youke")){
                    Toast.makeText(MainUserActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }else {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // 没有照相机权限，去请求系统获取权限
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
                    }else{
                        // 已经有相机权限
                        Intent qrIntent = new Intent(MainUserActivity.this, CaptureActivity.class);
                        startActivityForResult(qrIntent,QRCODE_SCAN);
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==IMAGE_OPEN)
            switch (requestCode) {
            case QRCODE_SCAN:
                try {
                    getGoodsById( data.getStringExtra( Constant.CODED_CONTENT ) );
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 有权限
                    Toast.makeText(MainUserActivity.this, "您开启了相机权限", Toast.LENGTH_SHORT).show();
                    Intent qrIntent = new Intent(MainUserActivity.this, CaptureActivity.class);
                    startActivityForResult(qrIntent,QRCODE_SCAN);
                } else {
                    // 没有权限
                    Toast.makeText(MainUserActivity.this, "您没有开启相机权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    //获取商品列表
    public void getGoodsList(){
        RequestParams params = new RequestParams();
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.GET_GOODSLIST_SERVER , null , new RequestCallBack<String>(){

            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(MainUserActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Log.i("TAG","----"+arg0.result);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Goods>>() {}.getType();
                goodslist = gson.fromJson(arg0.result, type);
                initListView();
            }
        });
    }

    //根据id获取商品信息
    public void getGoodsById(String id){
        RequestParams params = new RequestParams();
        params.addBodyParameter("id",id);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.GET_GOODS_BYID , params , new RequestCallBack<String>(){

            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(MainUserActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Log.i("TAG","----"+arg0.result);
                getGoods =  GsonUtils.jsonToBean(arg0.result, Goods.class);
                if(getGoods == null){
                    Toast.makeText(MainUserActivity.this,"暂无该商品", Toast.LENGTH_SHORT).show();
                }else {
                    showAlertDialog(getGoods);
                }
            }
        });
    }

    //搜索商品
    public void getGoodListByType(String type){
        RequestParams params = new RequestParams();
        params.addBodyParameter("type",type);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.GET_GOODLIST_BYTYPE , params , new RequestCallBack<String>(){

            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(MainUserActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Goods>>() {}.getType();
                goodslist = gson.fromJson(arg0.result, type);
                if(goodslist.size() == 0){
                    Toast.makeText(MainUserActivity.this,"该分类下暂无商品", Toast.LENGTH_SHORT).show();
                    initListView();
                    //getGoodsList();
                }else{
                    initListView();
                }

            }
        });
    }

    private void showAlertDialog(final Goods goods){
        selectNum = 1;
        orders = new Orders();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainUserActivity.this);
        View view = View.inflate(MainUserActivity.this, R.layout.alertdialog_layout, null);
        TextView goodsName = (TextView) view.findViewById(R.id.tv_alert_goodname);
        TextView goodsType = (TextView) view.findViewById(R.id.tv_alert_goodstype);
        TextView goodsPrice = (TextView) view.findViewById(R.id.tv_alert_price);
        TextView goodsStock = (TextView) view.findViewById(R.id.tv_alert_stock);
        ImageView addNum = (ImageView) view.findViewById(R.id.iv_alert_add);
        ImageView countNum = (ImageView)view.findViewById(R.id.iv_alert_sub);
        etNum = (EditText) view.findViewById(R.id.et_alert_number);
        etNum.setText(""+selectNum);
        etNum.setClickable(false);
        etNum.setFocusable(false);
        etNum.setFocusableInTouchMode(false);
        addNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNum < Integer.valueOf(goods.getGoodsStock())){
                    selectNum ++;
                    etNum.setText(""+selectNum);
                }else{
                    Toast.makeText(MainUserActivity.this,"已达最大库存",Toast.LENGTH_SHORT).show();
                }
            }
        });
        countNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNum <= 1){
                    Toast.makeText(MainUserActivity.this,"已达最小购买数量",Toast.LENGTH_SHORT).show();
                }else{
                    selectNum --;
                    etNum.setText(""+selectNum);
                }
            }
        });
        goodsName.setText(goods.getGoodsTitle());
        goodsType.setText(goods.getGoodsType());
        goodsPrice.setText(goods.getGoodsSoldOutPrice());
        goodsStock.setText(String.valueOf(goods.getGoodsStock()));
        builder.setTitle("请选择数量");
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orders.setGoodsId(goods.getId());
                orders.setGoodsTitle(goods.getGoodsTitle());
                orders.setSoldNum(etNum.getText().toString().trim());
                orders.setGoodsTotalBuyinPrice(String.valueOf( String.format("%.2f",(Double.valueOf(etNum.getText().toString().trim()) * Double.valueOf(goods.getGoodsBuyInPrice())))  ));
                orders.setGoodsTotalSoldOutPrice(String.valueOf(  String.format("%.2f",(Double.valueOf(etNum.getText().toString().trim()) * Double.valueOf(goods.getGoodsSoldOutPrice())))));
                orders.setGoodsTotalProfit(String.valueOf(     (String.format("%.2f", ((Double.valueOf(etNum.getText().toString().trim()) * Double.valueOf(goods.getGoodsSoldOutPrice())) - (Double.valueOf(etNum.getText().toString().trim()) * Double.valueOf(goods.getGoodsBuyInPrice()))) ))  ));
                orders.setSoldTime(getMomentTime());
                orders.setSoldMonth(getThisMonth());
                orders.setSoldUser(LoginActivity.tel);
                ordersList.add(orders);
                startShakeByProperty(ivShopCar,1,2,1,1000);
                dialog.dismiss();//关闭对话框
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL,"取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭对话框
            }
        });

        dialog.show();

    }

    private void startShakeByProperty(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    public static String getMomentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    public static String getThisMonth(){
        Calendar cale = null;
        cale = Calendar.getInstance();
        int month = cale.get(Calendar.MONTH) + 1;
        return String.valueOf(month);
    }
    @Override
    public void onResume() {
        super.onResume();
        getGoodListByType("牛奶饮料");
    }
}
