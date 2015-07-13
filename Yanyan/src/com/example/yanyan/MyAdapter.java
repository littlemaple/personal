package com.example.yanyan;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private Context mcontext;
	private ArrayList<HashMap<String,Object>> mList = null;
	private TextView mTextSong;
	private TextView mTextSinger;
	private LinearLayout mLayoutList;
	
	public MyAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		mcontext = context;
		mList = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int postion) {
		// TODO Auto-generated method stub
		return mList.get(postion);
	}

	@Override
	public long getItemId(int postion) {
		// TODO Auto-generated method stub
		return postion;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		ViewHolder holder = null;/
		if(convertView == null){
//			holder = new ViewHolder();
			convertView = LayoutInflater.from(mcontext).inflate(R.layout.list_view,null);	
		}

		mTextSong = (TextView) convertView.findViewById(R.id.textSong);
		mTextSinger = (TextView) convertView.findViewById(R.id.textSinger);
		mLayoutList = (LinearLayout) convertView.findViewById(R.id.layout_list);
		
		String textSong = (String) mList.get(postion).get("musicTitle");
		mTextSong.setText(textSong);
		
		String textSinger = (String) mList.get(postion).get("musicSinger");
		mTextSinger.setText(textSinger);
		
		boolean  bflag = (Boolean) mList.get(postion).get("state");
		if(bflag){
			mLayoutList.setBackgroundColor(R.drawable.bottom_bg);
		}else{
			mLayoutList.setBackgroundColor(Color.TRANSPARENT);
		}
		
		return convertView;
	}
}
