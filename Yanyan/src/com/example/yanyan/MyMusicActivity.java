package com.example.yanyan;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.nsd.NsdManager.RegistrationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;

import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyMusicActivity extends Activity implements OnItemClickListener {

private Toast mToast;
private Context mcontext;
private AudioService mAudioService;
private boolean bflag = false;
private boolean bRet = false;
private MyAdapter mMyAdapter;
private ListView mlistView;
private int currentpostion;
private static int currentid = 0;
private ArrayList<HashMap<String,Object>> mList;
private MyReceiver mMyReceiver;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
	System.gc();
	setContentView(R.layout.activity_mymusic); 
	mcontext = this;
	initnalize();
}


private ServiceConnection conn = new ServiceConnection() {
	
	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		// TODO Auto-generated method stub
		mAudioService = null;
		
	}
	
	@Override
	public void onServiceConnected(ComponentName arg0, IBinder binder) {
		// TODO Auto-generated method stub
		mAudioService = ((AudioService.AudioBinder)binder).getService();
	    mList =  mAudioService.scanAllAudioFiles();
	    mMyAdapter = new MyAdapter(mcontext,mList);
		mlistView.setAdapter(mMyAdapter);
		updateListState(currentid);
	}
};


protected Handler mHandler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		if(msg.what == 1000){
			if(currentid == mList.size() -1){
				currentid = 0;
				updateListState(0);
			}else {
				currentid ++;
				updateListState(currentid);
			}
			
		}
	}	
};

private void initnalize(){
	Intent intent = new Intent();
	intent.setClass(this, AudioService.class);
	startService(intent);
	bindService(intent, conn, Context.BIND_AUTO_CREATE);
	mlistView = (ListView) findViewById(R.id.listView);
	mlistView.setOnItemClickListener(this);
	mlistView.setOnScrollListener(new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
			// TODO Auto-generated method stub
			if(scrollState==OnScrollListener.SCROLL_STATE_IDLE){  
				currentpostion=mlistView.getFirstVisiblePosition();  //ListPos记录当前可见的List顶端的一行的位置  
        }  
		}
		
		@Override
		public void onScroll(AbsListView arg0, int visiablefrist, int visiablecount, int totalcount) {
			// TODO Auto-generated method stub
		}
	});
	mlistView.setCacheColorHint(Color.TRANSPARENT);
	mlistView.setSelection(currentpostion); 
	mMyReceiver = new MyReceiver();
	mMyReceiver.sethandler(mHandler);
	registerReceiver(mMyReceiver, new IntentFilter("com.example.yanyan.intent.USER_ACTION"));
	bflag = false;
	bRet = false;
}


private void showTip(final String str){
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
			mToast.setText(str);
			mToast.show();
		}
	});
}

public void onClick(View v){
	switch (v.getId()) {
	case R.id.backBtn:
		finish();
		mAudioService.releaseMedia();
		break;
	case R.id.preBtn: 
		if(currentid == 0){
			currentid = (mList.size() - 1);
		}else{
			currentid --;
		}
		updateListState(currentid);
		mAudioService.playBackMusic();
		bflag = true;
		((ImageButton)findViewById(R.id.Start)).setImageResource(R.drawable.pause);
		mAudioService.continuePlay();
		break;
	case R.id.Start:
		if(bRet == false){
			mAudioService.startPaly(currentid);
			bRet = true;
		}
		if(bflag){
			bflag = false;
			((ImageButton)findViewById(R.id.Start)).setImageResource(R.drawable.start);
			mAudioService.pausePlayMusic();
		}else{
			bflag = true;
			((ImageButton)findViewById(R.id.Start)).setImageResource(R.drawable.pause);
			mAudioService.continuePlay();
		}
		
		break;
	case R.id.nextBtn:
		if(currentid == mList.size() -1){
			currentid = 0;
		}else {
			currentid ++;
		}
		updateListState(currentid);
		mAudioService.playNextMusic();
		bflag = true;
		((ImageButton)findViewById(R.id.Start)).setImageResource(R.drawable.pause);
		mAudioService.continuePlay();
		break;
	default:
		break;
	}
}

public  static void startActivity(Context mcontext){
	Intent intent = new Intent(mcontext,MyMusicActivity.class);
	mcontext.startActivity(intent);
}

@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	if(mMyReceiver != null){
		unregisterReceiver(mMyReceiver);
	}
//	unregisterReceiver(mMyReceiver);
}


@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	// TODO Auto-generated method stub
	currentid = position;
	
	bRet = true;
	mAudioService.startPaly(currentid);
	
	bflag = true;
	((ImageButton)findViewById(R.id.Start)).setImageResource(R.drawable.pause);
	mAudioService.continuePlay();
	
	
	updateListState(currentid);
//	if(mPreView != null){
//		LinearLayout mLayout = (LinearLayout) mPreView.findViewById(R.id.layout_list);
//		
//		mLayout.setBackgroundColor(Color.TRANSPARENT);
//	}
//	
//	mPreView = view;
//		
//	LinearLayout mLayout = (LinearLayout) view.findViewById(R.id.layout_list);
//		
//	mLayout.setBackgroundColor(R.drawable.bottom_bg);
	

}

private void updateListState(int postion){
	for(int i = 0; i< mList.size();i++){
		if(i == postion){
			mList.get(i).put("state",true);
		}else{
			mList.get(i).put("state",false);
		}
	}
	if(mMyAdapter != null){
		mMyAdapter.notifyDataSetChanged();
	}	
}

 
}
