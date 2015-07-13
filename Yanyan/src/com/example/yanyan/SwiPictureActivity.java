package com.example.yanyan;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class SwiPictureActivity extends Activity{
	
private TextView myText;	
private ImageSwitcher mImageSwitch;
private Context mContext;
private Toast mToast;
//缓冲进度
private int mPercentForBuffering = 0;	
//播放进度
private int mPercentForPlaying = 0;

private final String[] text = {
		"nice to meet you ","此時全世界都是美得",
		"開心下去，每天，永遠"
};

//private final String[] text = {
//		"123131","asdgsag","agsadgwg"
//};

private int[] resId = new int[]{R.drawable.yanyan_two,R.drawable.yanyan_three,R.drawable.yanyan_four};

private BitmapDrawable[] mDrawable_ad = new BitmapDrawable[resId.length];

private boolean bflag = false;
static public boolean bRet = false;
private String resPath = null;
private int index = 0;
private static final int TIMERID = 10;
private SpeechSynthesizer mTts;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
	mContext = this;
	bRet = true;
	setContentView(R.layout.activity_pic);  
	initnalize();
	}

@SuppressLint("ShowToast")
private void initnalize(){
	StringBuffer param = new StringBuffer();
	resPath = ResourceUtil.generateResourcePath(mContext, RESOURCE_TYPE.assets,"tts/common.jet")+";" 
			+ ResourceUtil.generateResourcePath(mContext,RESOURCE_TYPE.assets,"tts/xiaoyan.jet");
	
	param.append(","+ResourceUtil.ASR_RES_PATH +"=" + resPath);
	param.append(","+ResourceUtil.ENGINE_START +"= " + SpeechConstant.ENG_TTS);
	param.append(";"+SpeechConstant.APPID+"=54bcc893");
	bflag = false;
	SpeechUtility.createUtility(mContext, param.toString());
//	
//	StringBuffer buffer = new StringBuffer();
//	
//	buffer.append(ResourceUtil.A)
	StringBuffer buffer = new StringBuffer();
	buffer.append(ResourceUtil.ASR_RES_PATH+"=" + resPath);
	buffer.append(","+ResourceUtil.ENGINE_START +"= " + SpeechConstant.ENG_TTS);
	SpeechUtility.getUtility().setParameter(ResourceUtil.ENGINE_START, buffer.toString());
	
	mTts = SpeechSynthesizer.createSynthesizer(mContext, new InitListener() {
		
		@Override
		public void onInit(int code) {
			// TODO Auto-generated method stub
			if(code!= ErrorCode.SUCCESS){
				showTip("初始化失败,错误码："+code);
			}
		}
	});
	
	
	mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
	myText = (TextView) findViewById(R.id.selftext);
	mImageSwitch =  (ImageSwitcher)findViewById(R.id.mypng);
	mImageSwitch.setFactory(new FactoryImpl());
//	mImageSwitch.setImageResource(resId[index]);
	setResToBitmap();
	mImageSwitch.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
	mImageSwitch.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right));
	mHandler.sendEmptyMessageDelayed(TIMERID,3000);
}

@SuppressLint("NewApi")
private void setParam(){
	mTts.setParameter(SpeechConstant.PARAMS, null);
	
	mTts.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);
	//设置发音人
	mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaofeng");
	
	//设置语速
	mTts.setParameter(SpeechConstant.SPEED,"50");

	//设置音调
	mTts.setParameter(SpeechConstant.PITCH,"50");

	//设置音量
	mTts.setParameter(SpeechConstant.VOLUME,"50");
			
	//设置播放器音频流类型
	mTts.setParameter(SpeechConstant.STREAM_TYPE,"3");
}
private class FactoryImpl implements ViewFactory{

	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		ImageView img = new ImageView(mContext);
		img.setScaleType(ImageView.ScaleType.FIT_XY);
		img.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		return img;
	}
}

private Handler mHandler = new Handler(){
	@Override
	public void handleMessage(Message msg){
		if(msg.what ==  TIMERID){
			System.gc();
			
//			index = index % resId.length;
//			mImageSwitch.setImageResource(resId[index]);
//			myText.setText(text[index]);
//			
//			int code = mTts.startSpeaking(text[index], mTtsListener);
//			
//			if (code != ErrorCode.SUCCESS) {
//				showTip("语音合成失败,错误码: " + code);	
//			}
			
			if(bRet){
				index ++ ;
				setResToBitmap();
				mHandler.sendEmptyMessageDelayed(TIMERID, 3000);
			}
		}
		
	}
};

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
		bRet = false;
		finish();
		break;
	case R.id.NextBtn:
		bRet = false;
//		for (int i = 0; i < resId.length; i++) {
//			if(i == index){
//				continue;
//			}
//			BitmapDrawable drawable = mDrawable_ad[i];
//			if (drawable != null) {
//				Bitmap bmp = mDrawable_ad[i].getBitmap();
//				if (bmp != null) {
//					if (!bmp.isRecycled()) {
//						bmp.recycle();
//					}
//				} 
//				drawable = null;
//			}
//			mDrawable_ad[i] = null;
//		}
		AudioActivity.startActivity(mHandler, mContext);
		break;
	case R.id.audioBtn:
		if(bflag){
			bflag = false;
			((Button)findViewById(R.id.audioBtn)).setText("显示");
			myText.setVisibility(View.GONE);
		}else{
			bflag = true;
			((Button)findViewById(R.id.audioBtn)).setText("隐藏");
			myText.setVisibility(View.VISIBLE);
		}
		break;
	default:
		break;
	}
}

private void setResToBitmap(){
	index = index % resId.length;
	BitmapDrawable drawable = mDrawable_ad[index];
	if(drawable == null){
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId[index]);
		mDrawable_ad[index] = new BitmapDrawable(bmp);
	}
	mImageSwitch.setImageDrawable(mDrawable_ad[index]);
	
	myText.setText(text[index]);
	
//	setParam();
//	if(bflag){
//		int code = mTts.startSpeaking(text[index], mTtsListener);
//		
//		if (code != ErrorCode.SUCCESS) {
//			showTip("语音合成失败,错误码: " + code);	
//		}		
//	}
}

private SynthesizerListener mTtsListener = new SynthesizerListener() {
	@Override
	public void onSpeakBegin() {
		showTip("开始播放");
	}

	@Override
	public void onSpeakPaused() {
		showTip("暂停播放");
	}

	@Override
	public void onSpeakResumed() {
		showTip("继续播放");
	}

	@Override
	public void onBufferProgress(int percent, int beginPos, int endPos,
			String info) {
		mPercentForBuffering = percent;
		mToast.setText(String.format(getString(R.string.tts_toast_format),
				mPercentForBuffering, mPercentForPlaying));
		
		mToast.show();
	}

	@Override
	public void onSpeakProgress(int percent, int beginPos, int endPos) {
		mPercentForPlaying = percent;
		showTip(String.format(getString(R.string.tts_toast_format),
				mPercentForBuffering, mPercentForPlaying));
	}

	@Override
	public void onCompleted(SpeechError error) {
		if(error == null)
		{
			showTip("播放完成");
		}
		else if(error != null)
		{
			showTip(error.getPlainDescription(true));
		}
	}

	@Override
	public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
		// TODO Auto-generated method stub
		
	}
};

@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	bRet = false;
	super.onDestroy();
	for (int i = 0; i < resId.length; i++) {
		BitmapDrawable drawable = mDrawable_ad[i];
		if (drawable != null) {
			Bitmap bmp = mDrawable_ad[i].getBitmap();
			if (bmp != null) {
				if (!bmp.isRecycled()) {
					bmp.recycle();
				}
			}
			drawable = null;
		}
		mDrawable_ad[i] = null;
	}
	System.gc();
	mTts.stopSpeaking();
	// 退出时释放连接
	mTts.destroy();
	mTtsListener = null;
}
}
