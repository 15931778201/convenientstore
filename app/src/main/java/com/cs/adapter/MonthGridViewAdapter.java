package com.cs.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cs.activity.R;
import com.cs.view.BaseViewHolder;


/**
 * @Description:gridview的Adapter
 */
public class MonthGridViewAdapter extends BaseAdapter {
	private Context mContext;

	public String[] img_text = {"1月", "2月", "3月", "4月","5月","6月","7月","8月","9月","10月","11月","12月"};

	public MonthGridViewAdapter(Context mContext) {
		super();
		this.mContext = mContext;
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
					R.layout.month_gridview_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_month_grid_item);

		tv.setText(img_text[position]);
		return convertView;
	}

}
