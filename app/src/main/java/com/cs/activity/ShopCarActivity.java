package com.cs.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.adapter.ShopcarListViewAdapter;
import com.cs.entity.Orders;
import com.cs.server.ServerAddress;
import com.cs.util.GsonUtils;
import com.cs.view.MyListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShopCarActivity extends Activity implements View.OnClickListener{

    private ImageView ivBack;

    private TextView tvEmpty;

    private LinearLayout layout;

    private TextView tvTotalPrice,tvOutPrice;

    private EditText etSHishou;

    private Button btUpload;

    private MyListView myListView;

    private ShopcarListViewAdapter shopcarListViewAdapter;

    /*进度条*/
    private ProgressDialog searchDialog;

    //数量
    public int intTotalPrice;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shopcar);
        initView();
        initListView();
    }

    private void initListView() {
        myListView = (MyListView) findViewById(R.id.shopcar_listview);
        tvEmpty = (TextView) findViewById(R.id.tv_shopcar_empty);
        myListView.setEmptyView(tvEmpty);
        shopcarListViewAdapter = new ShopcarListViewAdapter(this,MainUserActivity.ordersList);
        myListView.setAdapter(shopcarListViewAdapter);
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_shopcar_back);
        layout = (LinearLayout) findViewById(R.id.shopcar_layout);
        tvTotalPrice = (TextView) findViewById(R.id.tv_shopcar_totalprice);
        tvOutPrice = (TextView) findViewById(R.id.tv_shopcar_payback);
        etSHishou = (EditText) findViewById(R.id.et_shopcar_shishou);
        btUpload = (Button) findViewById(R.id.bt_shopcar_upload);

        ivBack.setOnClickListener(this);
        btUpload.setOnClickListener(this);

        if(MainUserActivity.ordersList.size() == 0){
            layout.setVisibility(View.GONE);
        }else{
            setData();
        }
    }

    private void setData() {
        tvTotalPrice.setText(countMoney());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_shopcar_back:
                onBackPressed();
                finish();
                break;

            case R.id.bt_shopcar_upload:
                if(etSHishou.getText().toString().trim().isEmpty()){
                    Toast.makeText(ShopCarActivity.this,"请输入实收金额",Toast.LENGTH_SHORT).show();
                }else{
                    if(Double.valueOf(etSHishou.getText().toString().trim()) < Double.valueOf(countMoney())){
                        Toast.makeText(ShopCarActivity.this,"实收金额过小",Toast.LENGTH_SHORT).show();
                    }else{
                        tvOutPrice.setText(String.valueOf(  String.format("%.2f",(Double.valueOf(etSHishou.getText().toString().trim()) - Double.valueOf(countMoney())))));
                        searchDialog = new ProgressDialog(this);
                        searchDialog.setMessage("上传中...");
                        searchDialog.setCancelable(false);
                        searchDialog.show();
                        uploadOrders(MainUserActivity.ordersList);
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    //获得当前时间
    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private Integer getTotalPrice(){
        Integer totalPrice = 0;
        for (int i = 0;i < MainUserActivity.ordersList.size();i++){
            totalPrice += Integer.valueOf(MainUserActivity.ordersList.get(i).getGoodsTotalSoldOutPrice());
        }
        return totalPrice;
    }

    private String countMoney(){
        Double money = 0.0;
        if(MainUserActivity.ordersList.size() != 0){
            for(int i = 0; i < MainUserActivity.ordersList.size(); i++){
                money = money + Double.valueOf(MainUserActivity.ordersList.get(i).getGoodsTotalSoldOutPrice());
            }
        }
        return String.valueOf(money);
    }


    public void uploadOrders(List<Orders> list){
        RequestParams params = new RequestParams();
        params.addBodyParameter("list", GsonUtils.createGsonString(list));
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.UPLOAD_ORDERS, params , new RequestCallBack<String>(){

            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(ShopCarActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                searchDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if(arg0.result.equals("success")){
                    Toast.makeText(ShopCarActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    MainUserActivity.ordersList.clear();
                    ShopCarActivity.this.finish();
                }else{
                    Toast.makeText(ShopCarActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                }
                searchDialog.dismiss();
            }
        });
    }
}
