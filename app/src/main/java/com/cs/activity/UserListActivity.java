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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.adapter.UserListViewAdapter;
import com.cs.entity.UserBean;
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

public class UserListActivity extends Activity implements OnItemClickListener{

	private ImageView ivBack;

	private ListView listView;

    private TextView tvEmpty;

	private UserListViewAdapter adapter;

	private List<UserBean> list = new ArrayList<UserBean>();

	private String message;

	private String uType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_userlist);
		initView();
		getUserList();
	}

	private void initListView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.userlist_listview);
        tvEmpty = (TextView) findViewById(R.id.tv_userlist_empty);
        listView.setEmptyView(tvEmpty);
		adapter = new UserListViewAdapter(UserListActivity.this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		ivBack = (ImageView) findViewById(R.id.iv_userlist_back);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
				finish();
			}
		});
	}

	

	//获取用户列表
	public void getUserList(){
		RequestParams params = new RequestParams();
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.ADMIN_GET_USERLIST, null , new RequestCallBack<String>(){

			public void onFailure(HttpException arg0, String arg1) { 
				// TODO Auto-generated method stub
				Toast.makeText(UserListActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<UserBean>>() {}.getType();
				list = gson.fromJson(arg0.result, type);
				initListView();
			}

		});
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.userlist_listview:
			Intent userIntent = new Intent(UserListActivity.this,RegisterActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("user", list.get(position));
			userIntent.putExtras(bundle);
			userIntent.putExtra("type", "update");
			startActivity(userIntent);
			break;

		default:
			break;
		}
	}


}
