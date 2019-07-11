
package com.cs.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs.activity.R;
import com.cs.entity.UserBean;
import com.cs.view.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


//用户列表适配器
public class UserListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<UserBean> list = new ArrayList<UserBean>();

	public UserListViewAdapter(Context mContext, List<UserBean> list) {
		super();
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position; 
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.user_listview_item, parent, false);
		}
		TextView username = BaseViewHolder.get(convertView, R.id.tv_userlist_list_item_username);
		TextView useraccount = BaseViewHolder.get(convertView, R.id.tv_userlist_list_item_account);
		TextView usertype = BaseViewHolder.get(convertView, R.id.tv_userlist_list_item_usertype);
		username.setText("姓名："+list.get(position).getUserName());
		useraccount.setText("电话："+list.get(position).getUserTel());
		usertype.setText("用户类型：普通用户");
		return convertView;
	}

}

