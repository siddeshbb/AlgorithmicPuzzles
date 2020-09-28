package com.csknights.algorithmicpuzzles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class Backtrack extends Activity implements OnClickListener{
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backtrack);
        View play = findViewById(R.id.bt);
	      play.setOnClickListener(this);
	      
    
    
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()) {
	      case R.id.bt:
	         Intent i = new Intent(this, PuzzleActivity.class);
	         startActivity(i);
	         break;
		 }
	}
    
}
