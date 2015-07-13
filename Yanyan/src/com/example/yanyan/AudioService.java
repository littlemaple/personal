package com.example.yanyan;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class AudioService extends Service{
	
	private static String TAG = AudioService.class.getSimpleName();

	private final Binder binder = new AudioBinder();
	private PlayManager mPlayManager = null;
	private Context mcontext;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId){  
		if(mPlayManager == null){
			mPlayManager = PlayManager.getInstance(mcontext);
		}
        return START_STICKY;  
    }  
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mcontext = MainApp.getContextObject();
		mPlayManager = PlayManager.getInstance(mcontext);
	}
	
	public class AudioBinder extends Binder{
		AudioService getService(){
			return AudioService.this;
		}
	}

	public void startPaly(int postion){
		try {
			mPlayManager.startPlayVideo(postion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopPlay(){
		mPlayManager.stopPlay();
	}
	
	public void continuePlay(){
		mPlayManager.continuePlay();
	}
	
	public void playNextMusic(){
		try {
			mPlayManager.playNextMusic();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playBackMusic(){
		try {
			mPlayManager.playBackMusic();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void pausePlayMusic(){
		mPlayManager.pausePlayMusic();
	}

	public void releaseMedia(){
		mPlayManager.releaseMedia();
	}
	
	public ArrayList<HashMap<String, Object> > scanAllAudioFiles(){
		return mPlayManager.scanAllAudioFiles();
	}
}
