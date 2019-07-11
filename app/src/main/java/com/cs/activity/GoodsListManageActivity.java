package com.cs.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.adapter.MainListViewAdapter;
import com.cs.entity.Goods;
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

public class GoodsListManageActivity extends Activity implements OnItemClickListener{

	private ImageView ivBack;

	private ListView listView;

	private TextView tvEmpty;

	private MainListViewAdapter adapter;

	private List<Goods> list = new ArrayList<Goods>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_goodsmanage);
		initView();
		getGoodsList();
	}

	private void initListView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.goodsmanage_listview);
		tvEmpty = (TextView) findViewById(R.id.tv_goodsmanage_empty);
		listView.setEmptyView(tvEmpty);
		adapter = new MainListViewAdapter(GoodsListManageActivity.this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		ivBack = (ImageView) findViewById(R.id.iv_goodsmanage_back);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
				finish();
			}
		});
	}



	//获取订单列表
	public void getGoodsList(){
		RequestParams params = new RequestParams();
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ ServerAddress.GET_GOODSLIST_SERVER, null , new RequestCallBack<String>(){

			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(GoodsListManageActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
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


	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
			case R.id.goodsmanage_listview:
				Intent userIntent = new Intent(GoodsListManageActivity.this,PublishGoodsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("goods", list.get(position));
				userIntent.putExtras(bundle);
				userIntent.putExtra("type", "update");
				startActivity(userIntent);
				break;

			default:
				break;
		}
	}


}
