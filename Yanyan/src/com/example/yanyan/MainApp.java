package com.example.yanyan;

import android.app.Application;
import android.content.Context;

public class MainApp extends Application{

	private static Context context;
	
	 @Override  
	    public void onCreate() {  
	        //获取Context  
	        context = getApplicationContext();  
	    }  
	      
	    //返回  
	    public static Context getContextObject(){  
	        return context;  
	    }  
}
