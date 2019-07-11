package com.cs.adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs.activity.R;
import com.cs.entity.Goods;
import com.cs.server.ServerAddress;
import com.cs.view.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:gridview的Adapter
 */
public class MainListViewAdapter extends BaseAdapter {
	private Context mContext;

	private List<Goods> goodslist = new ArrayList<Goods>();

	private String[] images;

	public MainListViewAdapter(Context mContext,List<Goods> goodslist) {
		super();
		this.mContext = mContext;
		this.goodslist = goodslist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return goodslist.size();
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
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.main_listview_item, parent, false);
		TextView title = BaseViewHolder.get(convertView, R.id.tv_main_list_item_title);
		TextView price = BaseViewHolder.get(convertView, R.id.tv_main_list_item_price);
		TextView type = BaseViewHolder.get(convertView, R.id.tv_main_list_item_type);
		TextView stock = BaseViewHolder.get(convertView, R.id.tv_main_list_item_stock);
		ImageView image = BaseViewHolder.get(convertView, R.id.iv_main_list_item);
		image.setImageResource(R.mipmap.icon_nopicture);
		if(goodslist.get(position).getGoodsImage() != null && !goodslist.get(position).getGoodsImage().isEmpty()  && goodslist.get(position).getGoodsImage().indexOf(",") != -1){
			images = goodslist.get(position).getGoodsImage().split(",");
			Glide.with(mContext).load(ServerAddress.SERVER_ADDRESS+images[0]).error(R.mipmap.icon_nopicture)
					.diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
		}else{
			Glide.with(mContext).load(ServerAddress.SERVER_ADDRESS+goodslist.get(position).getGoodsImage()).error(R.mipmap.icon_nopicture)
					.diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
		}
		title.setText(goodslist.get(position).getGoodsTitle());
		type.setText(goodslist.get(position).getGoodsType());
		stock.setText("库存剩余："+goodslist.get(position).getGoodsStock());
		if(goodslist.get(position).getGoodsStock() <= 10){
			stock.setTextColor(Color.RED);
		}
		price.setText(goodslist.get(position).getGoodsSoldOutPrice()+"元");

		return convertView;
	}

}
