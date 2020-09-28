package com.csknights.algorithmicpuzzles;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


public class Tenth extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen10);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.screen10, menu);
		return true;
	}
}

