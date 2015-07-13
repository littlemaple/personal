package com.example.yanyan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class MyReceiver extends BroadcastReceiver{

	private static Handler mhandler = null;
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if (mhandler == null) {
			return;
		}
		mhandler.sendEmptyMessage(1000);
	}
	

public void sethandler(Handler handler){
	mhandler = handler;
}
}
