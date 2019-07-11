
package com.cs.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cs.activity.R;
import com.cs.entity.Infos;
import com.cs.entity.UserBean;
import com.cs.view.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


public class InfoListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<Infos> list = new ArrayList<Infos>();

	public InfoListViewAdapter(Context mContext, List<Infos> list) {
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
					R.layout.info_listview_item, parent, false);
		}
		TextView title = BaseViewHolder.get(convertView, R.id.tv_infolist_item_title);
		TextView desc = BaseViewHolder.get(convertView, R.id.tv_infolist_item_desc);
		TextView time = BaseViewHolder.get(convertView, R.id.tv_infolist_item_time);
		title.setText(list.get(position).getInfoTitle());
		desc.setText(list.get(position).getInfoDesc());
		time.setText(list.get(position).getInfoTime());
		return convertView;
	}

}

