package com.example.yanyan;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeskAdapter extends BaseAdapter{

	
	private Context mcontext;
	private ArrayList <MyAppInfo> plist = null;
	public DeskAdapter(Context context, ArrayList <MyAppInfo> list){
		mcontext = context;
		plist = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return plist.size();
	}

	@Override
	public Object getItem(int postion) {
		// TODO Auto-generated method stub
		return plist.get(postion);
	}

	@Override
	public long getItemId(int postion) {
		// TODO Auto-generated method stub
		return postion;
	}

	@Override
	public View getView(int postion, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(view == null){
			 view = LayoutInflater.from(mcontext).inflate(  
	                 R.layout.desk_icon, null);  
			}
			ImageView imageView = (ImageView) view  
	                .findViewById(R.id.imageview);  
	        TextView textView = (TextView) view  
	                .findViewById(R.id.textview);  
	        MyAppInfo res = plist.get(postion);  
	        imageView.setImageDrawable(res.icon);  
	        textView.setText(res.title); 
		return view;
	}

}
