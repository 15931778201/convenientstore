package com.cs.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


/**
 */

public class ChangeUserInfoActivity extends Activity implements View.OnClickListener{

    private ImageView ivBack;

    private EditText etOldPwd,etNewPwd;

    private Button btSure;

    private UserBean user;

    /*进度条*/
    private ProgressDialog searchDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_changeuserinfo);
        initView();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_changeuserinfo_back);
        ivBack.setOnClickListener(this);
        etOldPwd = (EditText) findViewById(R.id.et_changeuserinfo_old);
        etNewPwd = (EditText) findViewById(R.id.et_changeuserinfo_new);
        btSure = (Button) findViewById(R.id.bt_changeuserinfo_sure);
        btSure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_changeuserinfo_back:
                onBackPressed();
                finish();
                break;

            case R.id.bt_changeuserinfo_sure:
                if(checkUser()){
                    String oldPwd = etOldPwd.getText().toString().trim();
                    String newPwd = etNewPwd.getText().toString().trim();
                    searchDialog = new ProgressDialog(this);
                    searchDialog.setMessage("密码修改中...");
                    searchDialog.setCancelable(false);
                    searchDialog.show();
                    try{
                        changeUserInfo("changepwd",LoginActivity.tel,MD5Utils.getMd5Encryption(oldPwd),MD5Utils.getMd5Encryption(newPwd));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    //用户输入项检查
    public boolean checkUser(){
        String uoldpwd = etOldPwd.getText().toString();
        if(uoldpwd.trim().length() == 0){
            etOldPwd.setError("旧密码不能为空");
            return false;
        }
        String unewpwd = etNewPwd.getText().toString();
        if(unewpwd.trim().length() == 0){
            etNewPwd.setError("新密码不能为空");
            return false;
        }
        return true;
    }

    /*修改密码*/
    public void changeUserInfo(String action,String userTel,String oldPwd,String newPwd){
        RequestParams params = new RequestParams();
        params.addBodyParameter("action", action);
        params.addBodyParameter("userTel", userTel);
        params.addBodyParameter("oldPwd", oldPwd);
        params.addBodyParameter("newPwd", newPwd);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.USER_OP_SERVER, params , new RequestCallBack<String>(){

            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(ChangeUserInfoActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                searchDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if(arg0.result.equals("fail")){
                    Toast.makeText(ChangeUserInfoActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChangeUserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }
                searchDialog.dismiss();
            }
        });
    }

}
