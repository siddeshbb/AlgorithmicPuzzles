package com.csknights.algorithmicpuzzles;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class Fifth extends Activity {
	private TextView[] t3=new TextView[8];
	private TextView[] t4=new TextView[9];
	private Button bt2,bt3;
	private Object[] ob2=new Object[8];
	private Object[] ob3=new Object[8];
	private int i=0;
	private int k=0;
	private String[] s6=new String[8];
	private String[] s7=new String[8];
	Timer t = new Timer(false);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen5);
		bt2=(Button)findViewById(R.id.buttn_1);
		bt3=(Button)findViewById(R.id.buttn_2);
		t3[0]=(TextView)findViewById(R.id.text_1);
		t3[1]=(TextView)findViewById(R.id.text_2);
		t3[2]=(TextView)findViewById(R.id.text_3);
		t3[3]=(TextView)findViewById(R.id.text_4);
		t3[4]=(TextView)findViewById(R.id.text_10);
		t3[5]=(TextView)findViewById(R.id.text12);
		t3[6]=(TextView)findViewById(R.id.text16);
		t3[7]=(TextView)findViewById(R.id.text31);
		calltime(4,5000);
		calltime(0,6400);
		calltime(1,8200);
		calltime(2,9000);
		calltime(5,10800);
		calltime(3,11400);
		calltime(6,14000);
		calltime(7,15600);
		calltime(8,17200);
		ob2[0]=t3[4].getTag();
		ob2[1]=t3[0].getTag();
		ob2[2]=t3[1].getTag();
		ob2[3]=t3[2].getTag();
		ob2[4]=t3[5].getTag();
		ob2[5]=t3[3].getTag();
		ob2[6]=t3[6].getTag();
		ob2[7]=t3[7].getTag();
		t4[0]=(TextView)findViewById(R.id.text_5);
		t4[1]=(TextView)findViewById(R.id.text_6);
		t4[2]=(TextView)findViewById(R.id.text_7);
		t4[3]=(TextView)findViewById(R.id.text_8);
		t4[4]=(TextView)findViewById(R.id.text11);
		t4[5]=(TextView)findViewById(R.id.text13);
		t4[6]=(TextView)findViewById(R.id.text14);
		t4[7]=(TextView)findViewById(R.id.text21);
		t4[8]=(TextView)findViewById(R.id.text_9);
		
		t3[0].setOnTouchListener(new ChoiceTouchListener());
		t3[1].setOnTouchListener(new ChoiceTouchListener());
		t3[2].setOnTouchListener(new ChoiceTouchListener());
		t3[3].setOnTouchListener(new ChoiceTouchListener());
		t3[4].setOnTouchListener(new ChoiceTouchListener());
		t3[5].setOnTouchListener(new ChoiceTouchListener());
		t3[6].setOnTouchListener(new ChoiceTouchListener());
		t3[7].setOnTouchListener(new ChoiceTouchListener());
		
		t4[0].setOnDragListener(new ChoiceDragListener());
		t4[1].setOnDragListener(new ChoiceDragListener());
		t4[2].setOnDragListener(new ChoiceDragListener());
		t4[3].setOnDragListener(new ChoiceDragListener());
		t4[4].setOnDragListener(new ChoiceDragListener());
		t4[5].setOnDragListener(new ChoiceDragListener());
		t4[6].setOnDragListener(new ChoiceDragListener());
		t4[7].setOnDragListener(new ChoiceDragListener());
		
		
		
	}
	private void calltime(final int i,int j)
	{
	 
	    t.schedule(new TimerTask() {
	  @Override
	  public void run() {
	       runOnUiThread(new Runnable() {
	            public void run() {
	            	if(i!=8)
	            	 {		
	                   t3[i].setVisibility(View.INVISIBLE);
	            	 }
	            	else
	            	 {
	            	    for(int l=0;l<8;l++)	
	                    t3[l].setVisibility(View.VISIBLE);
	            	 }
	            }
	        });
	    }
	}, j);
	 
	 }
	
	private void check(int i) {
		
		  ob3[0]=t4[0].getTag();
		  ob3[1]=t4[1].getTag();
		  ob3[2]=t4[2].getTag();
		  ob3[3]=t4[3].getTag();
		  ob3[4]=t4[4].getTag();
		  ob3[5]=t4[5].getTag();
		  ob3[6]=t4[6].getTag();
		  ob3[7]=t4[7].getTag();
		  for(k=0;k<8;k++)
		  {
			  s6[k]=ob2[k].toString();
		      s7[k]=ob3[k].toString();
		  }
		
		
		if(((s6[0].equals(s7[0]))&&(s6[1].equals(s7[1])))&&(s6[2].equals(s7[2]))&&(s6[3].equals(s7[3]))&&(s6[4].equals(s7[4]))&&(s6[5].equals(s7[5]))&&(s6[6].equals(s7[6]))&&(s6[7].equals(s7[7])))
		{
			
		    bt2.setVisibility(View.VISIBLE);
			
		}
		else
		{
			bt3.setVisibility(View.VISIBLE);
		
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
		    	 if(i==8)
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
	
   public void Method9(View arg0)
   {
	   Intent intent1=new Intent(Fifth.this,Fifth.class);
	   startActivity(intent1); 
   }
   public void Method10(View arg0)
   {
	   Intent intent2=new Intent(Fifth.this,Sixth.class);
	   startActivity(intent2);
   }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.screen5, menu);
		return true;
	}
}

