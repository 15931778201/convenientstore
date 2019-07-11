package com.cs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.cs.adapter.MainAdminGridViewAdapter;
import com.cs.view.MyGridView;

public class MainAdminActivity extends Activity implements AdapterView.OnItemClickListener{

    private MyGridView gridView;

    private MainAdminGridViewAdapter adapter;

    public String[] img_text = {"收银", "经营状况", "商品新增", "商品管理","员工新增","员工管理","公告发布","公告列表","修改密码","切换用户"};

    public int[] imgs = { R.mipmap.icon_shouyin, R.mipmap.icon_jingying,R.mipmap.icon_addgoods, R.mipmap.icon_goodslist,R.mipmap.icon_adduser, R.mipmap.icon_userlist,R.mipmap.icon_gonggao, R.mipmap.icon_qita, R.mipmap.icon_mima, R.mipmap.icon_qiehuan};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainadmin);
        initGridView();
    }

    /*
     * 初始化导航菜单
     */
    private void initGridView() {
        // TODO Auto-generated method stub
        gridView = (MyGridView) findViewById(R.id.mainadmin_gridview_menu);
        gridView.setAdapter(new MainAdminGridViewAdapter(this,img_text,imgs));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(position == 0){
            Intent mainUserIntent = new Intent(MainAdminActivity.this,MainUserActivity.class);
            startActivity(mainUserIntent);
        }else if(position == 1){
            Intent shopProfitIntent = new Intent(MainAdminActivity.this,ShopProfitActivity.class);
            startActivity(shopProfitIntent);
        }else if(position == 2){
            Intent addGoodsIntent = new Intent(MainAdminActivity.this,PublishGoodsActivity.class);
            addGoodsIntent.putExtra("type","add");
            startActivity(addGoodsIntent);
        }else if(position == 3){
            Intent goodsListIntent = new Intent(MainAdminActivity.this,GoodsListManageActivity.class);
            startActivity(goodsListIntent);
        }else if(position == 4){
            Intent addUserintent = new Intent(MainAdminActivity.this,RegisterActivity.class);
            addUserintent.putExtra("type","register");
            startActivity(addUserintent);
        }else if(position == 5){
            Intent userListIntent = new Intent(MainAdminActivity.this,UserListActivity.class);
            startActivity(userListIntent);
        }else if(position == 6){
            Intent publishInfoIntent = new Intent(MainAdminActivity.this,PublishInfoActivity.class);
            startActivity(publishInfoIntent);
        }else if(position == 7){
            Intent infoListIntent = new Intent(MainAdminActivity.this,InfosListActivity.class);
            startActivity(infoListIntent);
        }else if(position == 8){
            Intent changePwdIntent = new Intent(MainAdminActivity.this,ChangeUserInfoActivity.class);
            startActivity(changePwdIntent);
        }else if(position == 9){
            Intent loginIntent = new Intent(MainAdminActivity.this,LoginActivity.class);
            startActivity(loginIntent);
        }
    }
}
