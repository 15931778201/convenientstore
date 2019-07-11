package com.cs.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cs.adapter.MonthGridViewAdapter;
import com.cs.entity.Goods;
import com.cs.entity.Orders;
import com.cs.server.ServerAddress;
import com.cs.util.MPChartHelper;
import com.cs.view.MyGridView;
import com.github.mikephil.charting.charts.BarChart;
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
import java.util.Calendar;
import java.util.List;

public class ShopProfitActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ImageView ivBack;

    private MyGridView gridView;

    private MonthGridViewAdapter monthGridViewAdapter;

    private List<Orders> ordersList = new ArrayList<Orders>();

    private BarChart barChart;

    private List<Integer> xAxisValue = new ArrayList<Integer>();

    private List<Float> yAxisValue1 = new ArrayList<Float>();

    private List<Float> yAxisValue2 = new ArrayList<Float>();

    private List<String> list = new ArrayList<String>();

    private Double totalProfit,totalInMoney;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shopprofit);
        initView();
        getOrdersListByMonth(getThisMonth());
    }

    private void initBarChart() {
        barChart = (BarChart) findViewById(R.id.shopprofit_barchart);
        barChart.clear();


        try {
            MPChartHelper.setTwoBarChartData(barChart,xAxisValue,yAxisValue1,yAxisValue2,"营业额(元)","利润(元)");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_shopprofit_back);
        gridView = (MyGridView) findViewById(R.id.shopprofit_gridview);

        monthGridViewAdapter = new MonthGridViewAdapter(ShopProfitActivity.this);
        gridView.setAdapter(monthGridViewAdapter);
        gridView.setOnItemClickListener(this);

        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_shopprofit_back:
                onBackPressed();
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(position == 0){
            getOrdersListByMonth("1");
        }else if(position ==1){
            getOrdersListByMonth("2");
        }else if(position ==2){
            getOrdersListByMonth("3");
        }else if(position ==3){
            getOrdersListByMonth("4");
        }else if(position ==4){
            getOrdersListByMonth("5");
        }else if(position ==5){
            getOrdersListByMonth("6");
        }else if(position ==6){
            getOrdersListByMonth("7");
        }else if(position ==7){
            getOrdersListByMonth("8");
        }else if(position ==8){
            getOrdersListByMonth("9");
        }else if(position ==9){
            getOrdersListByMonth("10");
        }else if(position ==10){
            getOrdersListByMonth("11");
        }else if(position ==11){
            getOrdersListByMonth("12");
        }
    }

    public void getOrdersListByMonth(final String month){
        RequestParams params = new RequestParams();
        params.addBodyParameter("month",month);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.GET_ORDERSLIST_BYMONTH , params , new RequestCallBack<String>(){

            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(ShopProfitActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Orders>>() {}.getType();
                ordersList = gson.fromJson(arg0.result, type);
                if(ordersList.size() == 0){
                    Toast.makeText(ShopProfitActivity.this,"该月暂无记录", Toast.LENGTH_SHORT).show();
                    barChart.clear();
                }else{
                    totalInMoney = 0.0;
                    totalProfit = 0.0;

                    for(int i = 0 ;i <ordersList.size();i++){
                        totalProfit = totalProfit + Double.valueOf(ordersList.get(i).getGoodsTotalProfit());
                        totalInMoney = totalInMoney + Double.valueOf(ordersList.get(i).getGoodsTotalSoldOutPrice());
                    }

                    yAxisValue1 = new ArrayList<Float>();
                    yAxisValue2 = new ArrayList<Float>();

                    yAxisValue1.clear();
                    yAxisValue2.clear();

                    yAxisValue1.add(Float.valueOf(String.valueOf(   String.format("%.2f",totalInMoney)  )));
                    yAxisValue2.add(Float.valueOf(String.valueOf(  String.format("%.2f",totalProfit)  )));

                    xAxisValue = new ArrayList<Integer>();
                    xAxisValue.clear();
                    xAxisValue.add(Integer.valueOf(0));

                    list = new ArrayList<String>();
                    list.clear();
                    list.add(month+"月");

                    initBarChart();
                }

            }
        });
    }

    public static String getThisMonth(){
        Calendar cale = null;
        cale = Calendar.getInstance();
        int month = cale.get(Calendar.MONTH) + 1;
        return String.valueOf(month);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barChart.clear();
    }
}
