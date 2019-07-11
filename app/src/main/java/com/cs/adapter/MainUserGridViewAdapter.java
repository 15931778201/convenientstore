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
public class MainUserGridViewAdapter extends BaseAdapter {
	private Context mContext;

	public String[] img_text = {"牛奶饮料", "糕点蜜饯", "麻辣肉脯", "其他品类"};
	public int[] imgs = { R.mipmap.icon_yinliao, R.mipmap.icon_gaodian,R.mipmap.icon_mala, R.mipmap.icon_qita,};

	public MainUserGridViewAdapter(Context mContext) {
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
					R.layout.mainuser_gridview_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_mainuser_grid_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_mainuser_grid_item);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		return convertView;
	}

}
