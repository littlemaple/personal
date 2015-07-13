package com.example.yanyan;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

public class AudioActivity extends Activity{
		
private Context mContext;
private static Handler mHandler;
private int currentPostion = 0;
private Toast mToast;
private MediaPlayer mediaPlayer=null; //声频
private AudioManager audioManager=null; //音频
private int playPostition = 0; // 当前播放文件的位置，可用于暂停和继续播放的
private boolean bflag = false;

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
	mContext = this;
	setContentView(R.layout.activity_audio); 
	initnalize();
	}


private void initnalize(){
	 bflag = false;
	 audioManager=(AudioManager)getSystemService(Service.AUDIO_SERVICE);
	 audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
}

public void onClick(View v){
	switch (v.getId()) {
	case R.id.image_set:
		releaseMedia();
		MyMusicActivity.startActivity(mContext);
		break;
	case R.id.backBtn:
		releaseMedia();
		if(mHandler != null){
			SwiPictureActivity.bRet = true;
			mHandler.removeMessages(10);
			mHandler.sendEmptyMessageDelayed(10,3000);
		}
		finish();
		break;
	case R.id.audioStart:
		releaseMedia();
		mediaPlayer=MediaPlayer.create(AudioActivity.this, R.raw.music_one);
        mediaPlayer.setLooping(true);//设置循环播放
        mediaPlayer.start();//播放声音   
        currentPostion ++;
		break;
	case R.id.audioStop:
		if(bflag){
			bflag = false;
			((Button)findViewById(R.id.audioStop)).setText("暂停");
			if (mediaPlayer != null) {  //  停止播放器  
				mediaPlayer.seekTo(playPostition);  //  从记录的位置开始播放  
	            mediaPlayer.start(); 
	        }  
		}else{
			bflag = true;
			((Button)findViewById(R.id.audioStop)).setText("继续");
			if (mediaPlayer != null) {  //  停止播放器  
				playPostition = mediaPlayer.getCurrentPosition();
	            mediaPlayer.pause();  
	        }  
		}
		break;
//	case R.id.Soundadd:
//		if(audioManager != null){
//			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
//	                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
//		}
//		break;
//	case R.id.Soundmiute:
//		if(audioManager != null){
//			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
//					AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
//		}
//		break;
	default:
		break;
	}
}


public  static void startActivity(Handler handler,Context mcontext){
	mHandler = handler;
	Intent intent = new Intent(mcontext,AudioActivity.class);
	mcontext.startActivity(intent);
}

public void releaseMedia() {    //  这里是释放mediaPlayer播放对象  
    if (mediaPlayer != null) {  
        try {  
            mediaPlayer.release();  
            mediaPlayer = null;  
            System.gc();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
} 

@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	releaseMedia();
}
}
