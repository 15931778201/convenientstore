package com.cs.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.constant.Constants;
import com.cs.entity.Goods;
import com.cs.entity.UserBean;
import com.cs.server.ServerAddress;
import com.cs.util.GsonUtils;
import com.cs.util.MD5Utils;
import com.cs.util.SPUtils;
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


/**
 * @author wsx
 *
 * 登录页
 */
public class LoginActivity extends Activity implements OnClickListener,CompoundButton.OnCheckedChangeListener {

	private EditText etAccount,etPassword;

	private Button btLogin;

	private TextView tvRegister;

	private TextView tvYouke;

	private CheckBox cbIsRemember;

	//用户账号  即手机号
	public static String tel ;

	public static String userType ;
	/*进度条*/
	private ProgressDialog searchDialog;

	private boolean isRemember = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*设置窗口为不显示标题*/
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		initView();
	}

	/*初始化各个控件*/
	private void initView() {
		// TODO Auto-generated method stub
		etAccount = (EditText) findViewById(R.id.et_login_account);
		etPassword = (EditText) findViewById(R.id.et_login_password);
		tvYouke = (TextView) findViewById(R.id.tv_login_youke);
		btLogin = (Button) findViewById(R.id.bt_login_login);
		cbIsRemember = (CheckBox) findViewById(R.id.cb_login_remember);

		/*设置点击事件*/
		btLogin.setOnClickListener(this);
		tvYouke.setOnClickListener(this);

		cbIsRemember.setOnCheckedChangeListener(this);
		isRemember = false;


		if(SPUtils.get(LoginActivity.this, Constants.USER_ACCOUNT,"").toString() != null &&
				SPUtils.get(LoginActivity.this, Constants.USER_PWD,"").toString() != null &&
				!SPUtils.get(LoginActivity.this, Constants.USER_ACCOUNT,"").toString().equals("") &&
				!SPUtils.get(LoginActivity.this, Constants.USER_PWD,"").toString().equals("")
				){
			etAccount.setText(SPUtils.get(LoginActivity.this, Constants.USER_ACCOUNT,"").toString());
			etPassword.setText(SPUtils.get(LoginActivity.this, Constants.USER_PWD,"").toString());
			cbIsRemember.setChecked(true);
			isRemember = true;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			isRemember = true;
		}else{
			isRemember = false;
		}
	}

	/*点击事件*/
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			case R.id.bt_login_login:
			/*获取输入框的值*/
				String account = etAccount.getText().toString().trim();
				String pass = etPassword.getText().toString().trim();

				if(account != null && !account.equals("")
						&& pass != null && !pass.equals("") ){
					searchDialog = new ProgressDialog(this);
					searchDialog.setMessage("登录中...");
					searchDialog.setCancelable(false);
					searchDialog.show();
					try{
						loginCheck("login",account, MD5Utils.getMd5Encryption(pass));
					}catch (Exception e){
						e.printStackTrace();
					}

				}else{
					Toast.makeText(LoginActivity.this,"请输入完整", Toast.LENGTH_SHORT).show();
				}
				break;

			/*case R.id.tv_login_register:

				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				intent.putExtra("type","register");
				startActivity(intent);*/

			case R.id.tv_login_youke:
				LoginActivity.userType = "youke";
				Intent intent2 = new Intent(LoginActivity.this,MainUserMenuActivity.class);
				startActivity(intent2);
				break;

			default:
				break;
		}
	}


	//用户登录判断
	public void loginCheck(String action , final String userTel, final String userPwd){
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", action);
		params.addBodyParameter("userTel", userTel);
		params.addBodyParameter("userPwd", userPwd);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS + ServerAddress.USER_OP_SERVER , params , new RequestCallBack<String>(){

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(LoginActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
				searchDialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if(arg0.result.equals("fail")){
					Toast.makeText(LoginActivity.this,"账号或密码错误", Toast.LENGTH_SHORT).show();
					searchDialog.dismiss();
				}else{
					Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
					if(isRemember){
						SPUtils.put(LoginActivity.this, Constants.USER_ACCOUNT,String.valueOf(userTel));
						SPUtils.put(LoginActivity.this, Constants.USER_PWD,String.valueOf(etPassword.getText().toString().trim()));
					}else{
						if(SPUtils.contains(LoginActivity.this,Constants.USER_ACCOUNT)){
							SPUtils.remove(LoginActivity.this, Constants.USER_ACCOUNT);
						}
						if(SPUtils.contains(LoginActivity.this,Constants.USER_PWD)){
							SPUtils.remove(LoginActivity.this, Constants.USER_PWD);
						}
					}
					UserBean userBean =  GsonUtils.jsonToBean(arg0.result, UserBean.class);
					LoginActivity.userType = userBean.getUserType();
					LoginActivity.tel = userTel;
					if(userType.equals("user")){
						Intent intent = new Intent(LoginActivity.this,MainUserMenuActivity.class);
						startActivity(intent);
					}else{
						Intent intent = new Intent(LoginActivity.this,MainAdminActivity.class);
						startActivity(intent);
						Log.i("TAG","admin login");
					}
					finish();
					searchDialog.dismiss();
				}
			}
		});
	}
}
