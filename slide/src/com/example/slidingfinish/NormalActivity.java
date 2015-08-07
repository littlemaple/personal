package com.example.slidingfinish;

import android.app.Activity;
import android.os.Bundle;
import com.espresso.slide.R;

public class NormalActivity extends SwipeBackActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal);
	}

	@Override
	public Activity getAttachActivity() {
		return this;
	}

	@Override
	public void popBack() {
		this.finish();
	}
}
