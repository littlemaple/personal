package com.example.yanyan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyInstalledReceiver extends BroadcastReceiver {

	private static Handler mhandler = null;

	public MyInstalledReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (mhandler == null) {
			return;
		}
		String packname = null;
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			packname = intent.getDataString();
			Bundle b = new Bundle();
			b.putString("packname", packname);
			b.putBoolean("pack", true);
			Message msg = mhandler.obtainMessage();
			msg.setData(b);
			msg.sendToTarget();
		}

		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			Bundle data = new Bundle();
			packname = intent.getDataString();
			data.putString("packname", intent.getDataString());
			data.putBoolean("pack", false);
			Message msg = mhandler.obtainMessage();
			msg.setData(data);
			msg.sendToTarget();
		}
	}

	public void sethandler(Handler handler){
		mhandler = handler;
	}
}
