package com.example.yanyan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener,OnTouchListener,OnGestureListener{

	private final String[] packs = {"com.android.dialer",
			"com.android.browser",
			"com.android.settings",
			"com.tencent.mm",
			"com.eg.android.AlipayGphone",
			"com.tencent.mobileqq",
			"com.example.yanyan"};
	
	
	private Context mcontext;
	private GridView mGridView;

	private int count = 0;
	private int[] resId = {R.drawable.bg_one,R.drawable.bg_two,R.drawable.bg_three,
			R.drawable.bg_four,R.drawable.bg_five,R.drawable.bg_six};
	
	private BitmapDrawable[] mDrawable_bg = new BitmapDrawable[resId.length];
//	private LinearLayout bgLayout;
	private static final String TAG = MainActivity.class.getSimpleName();
	private ArrayList<MyAppInfo> mApplications;
	
	private PackageManager manager;
	private DeskAdapter mAdapter;
	private MyInstalledReceiver mMyInstalledReceiver; 

	private GestureDetector mGestureDetector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		initialize();
	}

	private void initialize() {
		mcontext = this;
		mGestureDetector = new GestureDetector((OnGestureListener)this);        
		mApplications = new ArrayList<MyAppInfo>();
		mGridView = (GridView) findViewById(R.id.grid_view);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnTouchListener(this);    
		mGridView.setLongClickable(true); 
		
		getMangetInfo();
		mAdapter = new DeskAdapter(this, mApplications);
		mGridView.setAdapter(mAdapter);
		mMyInstalledReceiver = new MyInstalledReceiver();
		
		mMyInstalledReceiver.sethandler(mHandler);
		
//		backBtn = (Button) findViewById(R.id.backBtn);
//		nextBtn = (Button) findViewById(R.id.NextBtn);
		
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.backBtn:
			System.exit(0);
			break;
		case R.id.NextBtn:
			for (int i = 0; i < resId.length; i++) {
				if(i == count){
					continue;
				}
				BitmapDrawable drawable = mDrawable_bg[i];
				if (drawable != null) {
					Bitmap bmp = mDrawable_bg[i].getBitmap();
					if (bmp != null) {
						if (!bmp.isRecycled()) {
							bmp.recycle();
						}
					} 
					drawable = null;
				}
				mDrawable_bg[i] = null;
			}
			Intent register = new Intent(this,SwiPictureActivity.class);
			startActivity(register);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for (int i = 0; i < resId.length; i++) {
			BitmapDrawable drawable = mDrawable_bg[i];
			if (drawable != null) {
				Bitmap bmp = mDrawable_bg[i].getBitmap();
				if (bmp != null) {
					if (!bmp.isRecycled()) {
						bmp.recycle();
					}
				} 
				drawable = null;
			}
			mDrawable_bg[i] = null;
		}
	}
	
	protected Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String packname = data.getString("packname");

			String str = "";
			if(packname == null){
				return;
			}
			str = packname.substring(8);
			for (int i = 0; i < mApplications.size(); i++) {
				MyAppInfo application = mApplications.get(i);
				if(data.getBoolean("pack")){
					getMangetInfo();
					mAdapter.notifyDataSetChanged();
				}else{
					if(application.pkgName.equals(str)){
						mApplications.remove(i);
						mAdapter.notifyDataSetChanged();
					}
				}		
			}
		}

	};

	private void getMangetInfo() {
		manager = this.getPackageManager();
		mApplications.removeAll(mApplications);
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> apps = manager.queryIntentActivities(
				mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
		int count = apps.size();
		for (int i = 0; i < count; i++) {
			MyAppInfo application = new MyAppInfo();
			ResolveInfo info = apps.get(i);
			application.title = info.loadLabel(manager).toString();
			application.pkgName = info.activityInfo.packageName;

			application.setActivity(new ComponentName(
					info.activityInfo.applicationInfo.packageName,
					info.activityInfo.name), Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			application.icon = info.activityInfo.loadIcon(manager);

			for(int j = 0;j< packs.length;j++){
				String temp = packs[j];
				if(application.pkgName.equals(temp)){
					mApplications.add(application);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		MyAppInfo res = mApplications.get(position);
		if (res == null) {
//			gLogger.debug(TAG + ":" + "myAppInfo is null");
			return;
		}
		view.getContext().startActivity(res.intent);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}
	
    private int verticalMinDistance = 20;
    private int minVelocity         = 0;

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
//            Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
        	System.gc();
        	if(count == 0){
        		count = resId.length;
        	}
        	count -- ;
        	setResToBitmap();
        	Log.d("11111111111 ----",String.valueOf(count));
        	
        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
        	System.gc();
        	count ++ ;
        	setResToBitmap();
        	Log.d("11111111111 ++++",String.valueOf(count));
        	
//            Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void setResToBitmap(){
    	count = count % resId.length;
    	BitmapDrawable drawable = mDrawable_bg[count];
    	if(drawable == null){
    		Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId[count]);
    		mDrawable_bg[count] = new BitmapDrawable(bmp);
    	}
    	if(mGridView != null){
    		mGridView.setBackgroundDrawable(mDrawable_bg[count]);
    	}
    }
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
