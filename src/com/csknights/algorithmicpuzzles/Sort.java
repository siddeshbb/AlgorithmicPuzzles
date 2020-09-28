package com.csknights.algorithmicpuzzles;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;


public class Sort extends Activity {
	private TextView[] t1=new TextView[4];
	private TextView[] t2=new TextView[5];
	private  int i=0;
	private int k=0; 
	private Button bt,bt1;
	private Object[] ob=new Object[4];
	private Object[] ob1=new Object[4];
	private String[] s2=new String[5];
	private String[] s3=new String[5];
	Timer t = new Timer(false);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sortmain);
		bt=(Button)findViewById(R.id.button1);
		bt1=(Button)findViewById(R.id.button2);
		t1[0]=(TextView)findViewById(R.id.textView1);
		calltime(0,5000);
		t1[1]=(TextView)findViewById(R.id.textView2);
		calltime(1,7000);
		t1[2]=(TextView)findViewById(R.id.textView3);
		calltime(2,9000);
		t1[3]=(TextView)findViewById(R.id.textView4);
		calltime(3,11000);
		calltime(4,13000);
		ob[0]=t1[0].getTag();
		ob[1]=t1[1].getTag();
		ob[2]=t1[2].getTag();
		ob[3]=t1[3].getTag();
		t2[0]=(TextView)findViewById(R.id.textView5);
		t2[1]=(TextView)findViewById(R.id.textView6);
		t2[2]=(TextView)findViewById(R.id.textView7);
		t2[3]=(TextView)findViewById(R.id.textView8);
		t2[4]=(TextView)findViewById(R.id.textView9);
		t1[0].setOnTouchListener(new ChoiceTouchListener());
		t1[1].setOnTouchListener(new ChoiceTouchListener());
		t1[2].setOnTouchListener(new ChoiceTouchListener());
		t1[3].setOnTouchListener(new ChoiceTouchListener());
		t2[0].setOnDragListener(new ChoiceDragListener());
		t2[1].setOnDragListener(new ChoiceDragListener());
		t2[2].setOnDragListener(new ChoiceDragListener());
		t2[3].setOnDragListener(new ChoiceDragListener());
		
		
	}
	private void calltime(final int i,int j)
	{
	 
	    t.schedule(new TimerTask() {
	  @Override
	  public void run() {
	       runOnUiThread(new Runnable() {
	            public void run() {
	            	if(i!=4)
	            	 {		
	                   t1[i].setVisibility(View.INVISIBLE);
	            	 }
	            	else
	            	 {
	            	    for(int l=0;l<4;l++)	
	                    t1[l].setVisibility(View.VISIBLE);
	            	 }
	            }
	        });
	    }
	}, j);
	 
	 }
	
	private void check(int i) {
		
		  ob1[0]=t2[0].getTag();
		  ob1[1]=t2[1].getTag();
		  ob1[2]=t2[2].getTag();
		  ob1[3]=t2[3].getTag();
		  for(k=0;k<4;k++)
		  {
			  s2[k]=ob[k].toString();
		      s3[k]=ob1[k].toString();
		    
		  }	
		
		if(((s2[0].equals(s3[0]))&&(s2[1].equals(s3[1])))&&(s2[2].equals(s3[2]))&&(s2[3].equals(s3[3])))
		{
			
			bt.setVisibility(View.VISIBLE);
			
		}
		else
		{
			bt1.setVisibility(View.VISIBLE);
		
		}
		
		
	}

	private final class ChoiceTouchListener implements OnTouchListener {
 
		
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
			    return true;
			}
			else {
			    return false;
			}
		}
	}
	private class ChoiceDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
		    case DragEvent.ACTION_DRAG_STARTED:
		        break;
		    case DragEvent.ACTION_DRAG_ENTERED:
		        break;
		    case DragEvent.ACTION_DRAG_EXITED:
		        break;
		    case DragEvent.ACTION_DROP:
		    	View view = (View) event.getLocalState();
		    	view.setVisibility(View.INVISIBLE);
		    	TextView dropTarget = (TextView) v;
		    	TextView dropped = (TextView) view;
		    	dropTarget.setText(dropped.getText());
		    	dropTarget.setBackgroundDrawable(dropped.getBackground());
		    	Object tag = dropTarget.getTag();
		    	if(tag!=null)
		    	{
		    	    int existingID = (Integer)tag;
		    	    findViewById(existingID).setVisibility(View.VISIBLE);
		    	    --i;
		    	}
		    	
		    	dropTarget.setTag(dropped.getId());
		    	 ++i;
		    	 if(i==4)
		    	  {	 
		    	    check(i); 
		    	  }
		        break;
		    case DragEvent.ACTION_DRAG_ENDED:
		        break;
		    default:
		        break;
		}
			
		    return true;
		}
		
	}
	
  
   public void Method1(View arg0)
   {
	   Intent intent2=new Intent(Sort.this,SecondActivity.class);
	   startActivity(intent2);
   }
   public void Method2(View arg0)
   {
	   Intent intent1=new Intent(Sort.this,Sort.class);
	   startActivity(intent1); 
   }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sortmain, menu);
		return true;
	}
}



