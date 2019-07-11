package com.cs.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs.activity.R;
import com.cs.view.BaseViewHolder;


/**
 * @Description:gridview的Adapter
 */
public class MainAdminGridViewAdapter extends BaseAdapter {
	private Context mContext;

	private String[] img_text ;
	private int[] imgs ;

	public MainAdminGridViewAdapter(Context mContext,String[] strings,int[] ints) {
		super();
		this.mContext = mContext;
		this.img_text = strings;
		this.imgs = ints;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.length;
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
					R.layout.mainadmin_gridview_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_mainadmin_grid_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_mainadmin_grid_item);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		return convertView;
	}

}
