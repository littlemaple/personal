package com.example.yanyan;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class MyAppInfo {
	public  String title;
	public  Drawable icon;
	public  Intent intent;
	public  String pkgName;
	
	public MyAppInfo(){
		
	}
	
	final void setActivity(ComponentName className, int launchFlags) {
		intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(className);
		intent.setFlags(launchFlags);
	}
}
