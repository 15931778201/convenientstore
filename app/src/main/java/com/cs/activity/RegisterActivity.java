package com.cs.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.entity.UserBean;
import com.cs.server.ServerAddress;
import com.cs.util.GsonUtils;
import com.cs.util.MD5Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


/*
 * 注册页
 */
public class RegisterActivity extends Activity implements OnClickListener{

    private EditText etTel,etPwd,etUserName,etMoney;

    private Button btRegister;

    private TextView tvTitle;

    private ImageView ivBack;

    private LinearLayout userLayout;
    /*进度条*/
    private ProgressDialog searchDialog;

    private String type;

    private UserBean userBean,getUserBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        try{
            Intent intent = getIntent();
            type = intent.getStringExtra("type");
            getUserBean = (UserBean) intent.getSerializableExtra("user");
        }catch (Exception e){
            e.printStackTrace();
        }
        initView();
    }


    /*初始化各个控件*/
    private void initView() {
        // TODO Auto-generated method stub
        etTel = (EditText) findViewById(R.id.et_register_tel);
        etPwd = (EditText) findViewById(R.id.et_register_pwd);
        etMoney = (EditText) findViewById(R.id.et_register_money);
        etUserName = (EditText) findViewById(R.id.et_register_username);

        userLayout = (LinearLayout) findViewById(R.id.register_user_layout);

        tvTitle = (TextView) findViewById(R.id.tv_register_title);
        ivBack = (ImageView) findViewById(R.id.iv_register_back);
        btRegister = (Button) findViewById(R.id.bt_register_register);

        ivBack.setOnClickListener(this);
        btRegister.setOnClickListener(this);

        if(type != null && type.equals("update")){
            setData();
        }

    }

    private void setData() {
        etTel.setText(getUserBean.getUserTel());
       /* etTel.setClickable(false);
        etTel.setFocusable(false);
        etTel.setFocusableInTouchMode(false);
        etTel.setTextColor(Color.GRAY);*/
        etUserName.setText(getUserBean.getUserName());
        etMoney.setText(getUserBean.getUserMoney());
        etPwd.setText("");
        btRegister.setText("修改");

    }

    /*设置点击事件*/
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.iv_register_back:
                /*返回上一页*/
                onBackPressed();
                this.finish();
                break;

            /*注册按钮   获取各个输入框的值  执行数据库插入语句 插入user表中*/
            case R.id.bt_register_register:
                /*首先进行输入项为空判断*/
                if(type.equals("register")){
                    if(checkUser()){
                        String tel = etTel.getText().toString().trim();
                        String pass = etPwd.getText().toString().trim();
                        String user = etUserName.getText().toString().trim();
                        String money = etMoney.getText().toString().trim();

                        userBean = new UserBean();
                        userBean.setUserName(user);
                        userBean.setUserTel(tel);
                        userBean.setUserMoney(money);
                        try{
                            userBean.setUserPwd(MD5Utils.getMd5Encryption(pass));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        searchDialog = new ProgressDialog(this);
                        searchDialog.setMessage("注册中...");
                        searchDialog.setCancelable(false);
                        searchDialog.show();
                        try{
                            userRegister("register", user, tel,MD5Utils.getMd5Encryption(pass),money,"0","user","");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    if(checkUser()){
                        String tel = etTel.getText().toString().trim();
                        String pass = etPwd.getText().toString().trim();
                        String user = etUserName.getText().toString().trim();
                        String money = etMoney.getText().toString().trim();

                        userBean = new UserBean();
                        userBean.setUserName(user);
                        userBean.setUserTel(tel);
                        userBean.setUserMoney(money);
                        try{
                            userBean.setUserPwd(MD5Utils.getMd5Encryption(pass));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        searchDialog = new ProgressDialog(this);
                        searchDialog.setMessage("修改中...");
                        searchDialog.setCancelable(false);
                        searchDialog.show();
                        try{
                            changeUserInfo("changeinfo", tel,MD5Utils.getMd5Encryption(pass),user,money,String.valueOf(getUserBean.getId()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                break;

            default:
                break;
        }
    }

    //用户输入项检查
    public boolean checkUser(){
        String uUserName = etUserName.getText().toString();
        if (uUserName.trim().length() == 0) {
            etUserName.setError("昵称不能为空");
            etUserName.requestFocus();
            return false;
        }
        String utel = etTel.getText().toString();
        if(utel.trim().length() == 0){
            etTel.setError("手机号不能为空");
            etTel.requestFocus();
            return false;
        }else if(utel.trim().length() != 11){
            etTel.setError("请输入11位手机号");
            etTel.requestFocus();
            return false;
        }

        String upass = etPwd.getText().toString();
        if(upass.trim().length() == 0){
            etPwd.setError("密码不能为空");
            etPwd.requestFocus();
            return false;
        }
        String umoney = etMoney.getText().toString();
        if(umoney.trim().length() == 0){
            etMoney.setError("工资不能为空");
            etMoney.requestFocus();
            return false;
        }

        return true;
    }

    //用户注册接口
    public void userRegister(String action,final String userName, final String userTel,final String userPwd,final String userMoney,final String userState,final String userType,final String userOther){
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("userTel", userTel);
        params.addBodyParameter("userPwd", userPwd);
        params.addBodyParameter("userMoney", userMoney);
        params.addBodyParameter("userState", userState);
        params.addBodyParameter("userType", userType);
        params.addBodyParameter("userOther", userOther);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.USER_OP_SERVER , params , new RequestCallBack<String>(){

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(RegisterActivity.this,"注册失败", Toast.LENGTH_SHORT).show();
                searchDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if(arg0.result.equals("success")){
                    Toast.makeText(RegisterActivity.this,"操作成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    searchDialog.dismiss();
                }else if(arg0.result.equals("exits")){
                    Toast.makeText(RegisterActivity.this,"账号已存在", Toast.LENGTH_SHORT).show();
                    searchDialog.dismiss();
                }else{
                    Toast.makeText(RegisterActivity.this,"操作失败", Toast.LENGTH_SHORT).show();
                    searchDialog.dismiss();
                }
            }

        });
    }

    public void changeUserInfo(String action,final String userTel, final String userPwd,final String userName,final String userMoney,final String id){
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("userTel", userTel);
        params.addBodyParameter("userPwd", userPwd);
        params.addBodyParameter("userMoney", userMoney);
        params.addBodyParameter("id", id);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.USER_OP_SERVER , params , new RequestCallBack<String>(){

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(RegisterActivity.this,"操作失败", Toast.LENGTH_SHORT).show();
                searchDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if(arg0.result.equals("success")){
                    Toast.makeText(RegisterActivity.this,"操作成功", Toast.LENGTH_SHORT).show();
                    finish();
                    searchDialog.dismiss();
                }else{
                    Toast.makeText(RegisterActivity.this,"操作失败", Toast.LENGTH_SHORT).show();
                    searchDialog.dismiss();
                }
            }

        });
    }


}
