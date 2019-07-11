package com.cs.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.adapter.MainListViewAdapter;
import com.cs.entity.Goods;
import com.cs.entity.Orders;
import com.cs.server.ServerAddress;
import com.cs.view.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * @author wsx
 */
public class SearchGoodsActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private ImageView ivDeleteText;

    private ImageView ivBack;

    private EditText etSearch;

    private Button btSearch;

    private RelativeLayout headLayout;

    private MyListView listView;

    private List<Goods> list = new ArrayList<Goods>();

    private MainListViewAdapter adapter;

    private int selectNum = 1;

    private EditText etNum;

    private Orders orders = new Orders();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_goods);
        initView();
        initListView();
    }

    private void initListView() {
        listView = (MyListView) findViewById(R.id.searchgoods_listview);
        adapter = new MainListViewAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initView() {
        // TODO Auto-generated method stub
        headLayout = (RelativeLayout) findViewById(R.id.layout_searchgoods_title);
        ivDeleteText = (ImageView) findViewById(R.id.iv_searchgoods_deleteinput);
        ivBack = (ImageView) findViewById(R.id.iv_searchgoods_back);
        etSearch = (EditText) findViewById(R.id.et_searchgoods_search);
        btSearch = (Button) findViewById(R.id.bt_searchgoods_search);

        btSearch.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        ivDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<Goods>();
                initListView();
                etSearch.setText("");
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_searchgoods_back:
                onBackPressed();
                finish();
                break;

            case R.id.bt_searchgoods_search:
                if(etSearch.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "请输入关键词再搜索",Toast.LENGTH_SHORT).show();
                }else{
                    searchPoint("getlistbypoint",etSearch.getText().toString().trim());
                }
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        List<Integer> sList = new ArrayList<Integer>();
        if(MainUserActivity.ordersList.size() > 0){
            for(int i = 0;i < MainUserActivity.ordersList.size(); i++){
                sList.add(MainUserActivity.ordersList.get(i).getGoodsId());
            }
            if(sList.contains(list.get(position).getId())){
                Toast.makeText(SearchGoodsActivity.this,"该商品已加入购物车",Toast.LENGTH_SHORT).show();
                Log.i("TAG","------已存在-----");
            }else{
                showAlertDialog(list.get(position));
            }
        }else{
            showAlertDialog(list.get(position));
        }
    }

    //搜索
    public void searchPoint(String action,String pointName){
        RequestParams params = new RequestParams();
        params.addBodyParameter("action",action);
        params.addBodyParameter("point",pointName);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ ServerAddress.SEARCH_GOOD_SERVER , params , new RequestCallBack<String>(){

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(SearchGoodsActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Goods>>() {}.getType();
                list = gson.fromJson(arg0.result, type);
                initListView();
            }
        });
    }

    private void showAlertDialog(final Goods goods){
        selectNum = 1;
        orders = new Orders();
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchGoodsActivity.this);
        View view = View.inflate(SearchGoodsActivity.this, R.layout.alertdialog_layout, null);
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
        addNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNum < Integer.valueOf(goods.getGoodsStock())){
                    selectNum ++;
                    etNum.setText(""+selectNum);
                }else{
                    Toast.makeText(SearchGoodsActivity.this,"已达最大库存",Toast.LENGTH_SHORT).show();
                }
            }
        });
        countNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectNum <= 1){
                    Toast.makeText(SearchGoodsActivity.this,"已达最小购买数量",Toast.LENGTH_SHORT).show();
                }else{
                    selectNum --;
                    etNum.setText(""+selectNum);
                }
            }
        });
        goodsName.setText(goods.getGoodsTitle());
        goodsType.setText(goods.getGoodsType());
        goodsPrice.setText(goods.getGoodsSoldOutPrice());
        goodsStock.setText(goods.getGoodsStock());
        builder.setTitle("请选择数量");
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orders.setGoodsId(goods.getId());
                orders.setGoodsTitle(goods.getGoodsTitle());
                orders.setSoldNum(etNum.getText().toString().trim());
                orders.setGoodsTotalBuyinPrice(String.valueOf(Double.valueOf(etNum.getText().toString().trim()) * Double.valueOf(goods.getGoodsBuyInPrice())));
                orders.setGoodsTotalSoldOutPrice(String.valueOf(Double.valueOf(etNum.getText().toString().trim()) * Double.valueOf(goods.getGoodsSoldOutPrice())));
                orders.setGoodsTotalProfit(String.valueOf((Double.valueOf(etNum.getText().toString().trim()) * Double.valueOf(goods.getGoodsSoldOutPrice())) - (Double.valueOf(etNum.getText().toString().trim()) * Double.valueOf(goods.getGoodsBuyInPrice()))));
                orders.setSoldTime(MainUserActivity.getMomentTime());
                orders.setSoldMonth(MainUserActivity.getThisMonth());
                MainUserActivity.ordersList.add(orders);
                dialog.dismiss();//关闭对话框
                SearchGoodsActivity.this.finish();
                Toast.makeText(SearchGoodsActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
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
}
