package com.cs.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cs.server.ServerAddress;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class PublishInfoActivity extends Activity implements View.OnClickListener{
    private ImageView ivBack;

    private EditText etTitle,etDesc;

    private Button btUpload;

    private ProgressDialog searchDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_publishinfo);
        initView();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_publishinfo_back);
        etTitle = (EditText) findViewById(R.id.et_publishinfo_title);
        etDesc = (EditText) findViewById(R.id.et_publishinfo_jieshao);
        btUpload = (Button) findViewById(R.id.bt_publishinfo_upload);

        ivBack.setOnClickListener(this);
        btUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_publishinfo_back:
                onBackPressed();
                finish();
                break;

            case R.id.bt_publishinfo_upload:
                if(checkUser()){
                    String sTitle = etTitle.getText().toString().trim();
                    String sInfo = etDesc.getText().toString().trim();

                    searchDialog = new ProgressDialog(this);
                    searchDialog.setMessage("发布中...");
                    searchDialog.setCancelable(false);
                    searchDialog.show();
                    publishInfo(sTitle,sInfo,LoginActivity.tel);
                }
                break;
        }
    }

    //用户输入项检查
    public boolean checkUser(){
        String title = etTitle.getText().toString();
        if (title.trim().length() == 0) {
            etTitle.setError("标题不能为空");
            etTitle.requestFocus();
            return false;
        }
        String info = etDesc.getText().toString();
        if(info.trim().length() == 0){
            etDesc.setError("内容不能为空");
            etDesc.requestFocus();
            return false;
        }

        return true;
    }


    public void publishInfo(String title,  String info, String user){
        RequestParams params = new RequestParams();
        params.addBodyParameter("title", title);
        params.addBodyParameter("info", info);
        params.addBodyParameter("user", user);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ServerAddress.SERVER_ADDRESS+ServerAddress.PUBLISH_INFOS , params , new RequestCallBack<String>(){

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(PublishInfoActivity.this,"发布失败", Toast.LENGTH_SHORT).show();
                searchDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if(arg0.result.equals("success")){

                    Toast.makeText(PublishInfoActivity.this,"发布成功", Toast.LENGTH_SHORT).show();
                    PublishInfoActivity.this.finish();
                    searchDialog.dismiss();
                }else{
                    Toast.makeText(PublishInfoActivity.this,"发布失败", Toast.LENGTH_SHORT).show();
                    searchDialog.dismiss();
                }
            }

        });
    }
}
