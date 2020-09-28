package com.csknights.algorithmicpuzzles;

	
	
    import android.annotation.SuppressLint;
	import android.app.Activity;
	import android.content.Context;
	import android.content.res.Configuration;
	import android.graphics.Canvas;
	import android.graphics.Paint;
	import android.graphics.Paint.Align;
	import android.graphics.Paint.FontMetrics;
	import android.graphics.Paint.FontMetricsInt;
	import android.graphics.Paint.Style;
	import android.util.AttributeSet;
	import android.view.MotionEvent;
	import android.view.View;
     import android.widget.Toast;

	public class Puzzleview extends View {

		private Puzzle puzzle;

	    private PuzzleActivity puzzleActivity = null;
	    
		private Activity parent;
		
		private TimeCounter timeCounter;
	    
		private boolean paused;

		private Paint textPaint;

		private static final int MESSAGES_RECT_HEIGHT = 40;
		
		private static final int BUTTON_TOOLBAR_RECT_HEIGHT = 40;
		private static final int BUTTON_TOOLBAR_LARGE_RECT_HEIGHT = 60;	
		
		private int messagesRectHeight;
		
		private int buttonToolbarHeight;

		
		public boolean isPaused() {
			return paused;
		}

		public void setPaused(boolean paused) {
			this.paused = paused;
		}    

		public void setPuzzle(Puzzle puzzle) {
			this.puzzle = puzzle;
		}

		public void setTimeCounter(TimeCounter timeCounter) {
			this.timeCounter = timeCounter;
		}

		public Puzzleview(Context context, AttributeSet attributeSet) {
			super(context, attributeSet);
			
			parent = (Activity) context;
			if (context instanceof PuzzleActivity) {
				puzzleActivity = (PuzzleActivity) context;
			}
			
			textPaint = new Paint();
		}	
		
		public Puzzleview(Context context) {
	        super(context);
	        
	        parent = (PuzzleActivity) context;
			if (context instanceof PuzzleActivity) {
				puzzleActivity = (PuzzleActivity) context;
			}
			
			textPaint = new Paint();
		}

	    @Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	    	if (parent != null) {
				float scale = parent.getResources().getDisplayMetrics().density;
				messagesRectHeight = (int) (MESSAGES_RECT_HEIGHT * scale);

		        int screenLayout = parent.getResources().getConfiguration().screenLayout;
		        if ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= 3) {
		        	buttonToolbarHeight = (int) (BUTTON_TOOLBAR_LARGE_RECT_HEIGHT * scale);
		        }
		        else {
		        	buttonToolbarHeight = (int) (BUTTON_TOOLBAR_RECT_HEIGHT * scale);
		        }
				
	    	}
	    	else {
	    		messagesRectHeight = 0;
	    		buttonToolbarHeight = 0;
	    	}
			
	    	if (puzzle != null) {
	    		puzzle.setTopBarHeight(messagesRectHeight);
	    		puzzle.setBottomBarHeight(buttonToolbarHeight);
	    		puzzle.onSizeChanged(w, h);
	    	}
		}

	    private float resizeFontToFit(Paint paint, int textHeight) {
	        if (textHeight > 0) {
	            float size = paint.getTextSize();
	            float minTextSize = 0;
	            float maxTextSize = 50;
	            FontMetricsInt fm = paint.getFontMetricsInt();
	            int lineHeight = -fm.top + fm.bottom;

	            while (lineHeight > textHeight && size > minTextSize) {
	            	size-= 1;
	            	paint.setTextSize(size);
	            	fm = paint.getFontMetricsInt();
	                lineHeight = -fm.top + fm.bottom;
	            }

	            while (lineHeight < textHeight && size < maxTextSize) {
	            	size+= 1;
	            	paint.setTextSize(size);
	            	fm = paint.getFontMetricsInt();
	                lineHeight = -fm.top + fm.bottom;
	            }
	            return size;
	        }
	        return paint.getTextSize();
	    }    
	    
		@SuppressLint("DefaultLocale")
		@Override
	    public void onDraw(Canvas canvas) {
          	int timeTextSize;
			int pauseTextSize = 40;
			
			if (canvas.getWidth() > 700 || canvas.getHeight() > 700) {
				pauseTextSize = 50;
			}

	        if (puzzle != null) {
				puzzle.draw(canvas);
			}
			textPaint.setStrokeWidth(1);
			timeTextSize = (int) resizeFontToFit(textPaint, messagesRectHeight);
			textPaint.setTextSize(timeTextSize);
			textPaint.setStyle(Style.FILL_AND_STROKE);
			textPaint.setAntiAlias(true);
			textPaint.setTextAlign(Align.LEFT);
	        textPaint.setColor(0xA0000000);
	        FontMetrics fm = textPaint.getFontMetrics();        
			if (puzzle.isStarted() || puzzle.isSolverRunning() ||
					(puzzle.isSolved() && !puzzle.isReplayEnded())) {
				int seconds = timeCounter.getTimeSeconds();
				String text = String.format("%02d:%02d", seconds/60, seconds%60);
				float x = canvas.getWidth() - textPaint.measureText(text) - 2;
				float y = -fm.top + fm.bottom;
				canvas.drawText(text, x, y, textPaint);
			}
	        
			if (paused) {
				textPaint.setColor(0xC0000000);
				textPaint.setTextSize(pauseTextSize);
				textPaint.setTextAlign(Align.CENTER);
	            fm = textPaint.getFontMetrics();
				String text = parent.getResources().getString(R.string.PAUSE);
				float x = (canvas.getWidth())/2;
				float y = (canvas.getHeight()-fm.top + fm.bottom)/2;		
				canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), textPaint);
				textPaint.setColor(0xFFFFFFFF);
				canvas.drawText(text, x, y, textPaint);	
			}  
			      
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (paused) {
				paused = false;
				if (puzzleActivity != null) {
					puzzleActivity.resume();
				}
				invalidate();
				return true;
			}    
	    
			if (puzzle == null || puzzle.isReplayRunning() || puzzle.isSolved()) {
				return true;
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				switch(puzzle.onTouchEvent(event.getX(), event.getY())){
				case MOVE_SUCCESSFUL:
					((PuzzleActivity) getContext()).startTimer();
					puzzleActivity.updateButtons();
					timeCounter.start();
					invalidate();
					break;
				case RIDDLE_SOLVED:
					timeCounter.stop();
					((PuzzleActivity) getContext()).stopTimer();
					((PuzzleActivity) getContext()).onPuzzleSolvedByUser();
					puzzleActivity.updateButtons();
					invalidate();
					break;
				case RIDDLE_UNSOLVABLE:
					timeCounter.stop();
					((PuzzleActivity) getContext()).stopTimer();
					puzzleActivity.updateButtons();
					invalidate();
					Toast.makeText(this.getContext(),
							R.string.riddle_cant_be_solved, Toast.LENGTH_SHORT).show();
					break;
				case MOVE_EDIT:
					invalidate();
					break;
				}
			}
			return true;
		}

	}
	


