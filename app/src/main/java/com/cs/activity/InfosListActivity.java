package com.cs.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.adapter.InfoListViewAdapter;
import com.cs.entity.Infos;
import com.cs.server.ServerAddress;
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

public class InfosListActivity extends Activity{

	private ImageView ivBack;

	private ListView listView;

    private TextView tvEmpty;

	private InfoListViewAdapter adapter;

	private List<Infos> list = new ArrayList<Infos>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_infolist);
		initView();
		getInfolist();
	}

	private void initListView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.infolist_listview);
        tvEmpty = (TextView) findViewById(R.id.tv_infolist_empty);
        listView.setEmptyView(tvEmpty);
		adapter = new InfoListViewAdapter(InfosListActivity.this, list);
		listView.setAdapter(adapter);
	}

	private void initView() {
		// TODO Auto-generated method stub
		ivBack = (ImageView) findViewById(R.id.iv_infolist_back);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
				finish();
			}
		});
	}

	

	//获取公告列表
	public void getInfolist(){
		RequestParams params = new RequestParams();
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.GET_INFOS_LIST, null , new RequestCallBack<String>(){

			public void onFailure(HttpException arg0, String arg1) { 
				// TODO Auto-generated method stub
				Toast.makeText(InfosListActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<Infos>>() {}.getType();
				list = gson.fromJson(arg0.result, type);
				initListView();
			}

		});
	}
	


}
