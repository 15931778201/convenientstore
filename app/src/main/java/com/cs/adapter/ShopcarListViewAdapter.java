package com.cs.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs.activity.MainUserActivity;
import com.cs.activity.R;
import com.cs.entity.Orders;
import com.cs.view.BaseViewHolder;

import java.util.List;

//右侧列表适配器
public class ShopcarListViewAdapter extends BaseAdapter {
	//上下文
	private Context mContext;

	public static List<Orders> shopCarList;

	private TextView tvTitle;

	private TextView tvPrice;

	private ImageView ivDelete;

	public ShopcarListViewAdapter(Context mContext, List<Orders> shopCarList) {
		this.mContext = mContext;
		this.shopCarList = shopCarList;
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shopCarList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.shopcar_listview_item, parent, false);
		}
		tvTitle = BaseViewHolder.get(convertView, R.id.tv_shopcar_listview_title);
		tvPrice = BaseViewHolder.get(convertView, R.id.tv_shopcar_listview_price);
		ivDelete = BaseViewHolder.get(convertView, R.id.iv_shopcar_listview_del);

		tvTitle.setText(shopCarList.get(position).getGoodsTitle());
		tvPrice.setText(shopCarList.get(position).getGoodsTotalSoldOutPrice()+"元");


		ivDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainUserActivity.ordersList.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}



}

