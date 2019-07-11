package com.cs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cs.adapter.MainAdminGridViewAdapter;
import com.cs.view.MyGridView;

public class MainUserMenuActivity extends Activity implements AdapterView.OnItemClickListener{

    private MyGridView gridView;

    private MainAdminGridViewAdapter adapter;

    public String[] img_text = {"收银", "公告列表", "修改密码", "切换用户"};
    public int[] imgs = { R.mipmap.icon_shouyin, R.mipmap.icon_qita,R.mipmap.icon_mima, R.mipmap.icon_qiehuan};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_usermenu);
        initGridView();
    }

    /*
     * 初始化导航菜单
     */
    private void initGridView() {
        // TODO Auto-generated method stub
        gridView = (MyGridView) findViewById(R.id.usermenu_gridview_menu);
        gridView.setAdapter(new MainAdminGridViewAdapter(this,img_text,imgs));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(position == 0){
            Intent mainUserIntent = new Intent(MainUserMenuActivity.this,MainUserActivity.class);
            startActivity(mainUserIntent);
        }else if(position == 1){
            Intent infoListIntent = new Intent(MainUserMenuActivity.this,InfosListActivity.class);
            startActivity(infoListIntent);
        }else if(position == 2){
            if(LoginActivity.userType != null && LoginActivity.userType.equals("youke")){
                Toast.makeText(MainUserMenuActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
            }else{
                Intent changePwdIntent = new Intent(MainUserMenuActivity.this,ChangeUserInfoActivity.class);
                startActivity(changePwdIntent);
            }
        }else if(position == 3){
            Intent loginIntent = new Intent(MainUserMenuActivity.this,LoginActivity.class);
            startActivity(loginIntent);
        }
    }
}
