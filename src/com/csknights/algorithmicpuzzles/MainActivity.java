package com.csknights.algorithmicpuzzles;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 View play = findViewById(R.id.backtrack);
	      play.setOnClickListener(this);
	      
	      View play1 = findViewById(R.id.sorting);
	      play1.setOnClickListener(this);
	      
	      View credits = findViewById(R.id.toh);
	      credits.setOnClickListener(this);
	     
	      View help = findViewById(R.id.help);
	      help.setOnClickListener(this);
	      
	     
	     
	      View exitButton = findViewById(R.id.exit);
	      exitButton.setOnClickListener(this);
	      
	      
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()) {
	      case R.id.backtrack:
	         Intent i = new Intent(this, Backtrack.class);
	         startActivity(i);
	         break;
	      // More buttons go here (if any) ...
	      case R.id.sorting:
		         Intent m = new Intent(this, Sort.class);
		         startActivity(m);
		         break;
	      case R.id.toh:
		         Intent y = new Intent(this, Tohal.class);
		         startActivity(y);
		         break;
	      case R.id.help:
	    	  Intent intent  = new Intent("com.csknights.algorithmicpuzzles.WEB_VIEW");
				startActivity(intent);
	        	         break;
	      
	      
	      case R.id.exit:
	         finish();
	         break;
	      
	      
	      }
	}

}
