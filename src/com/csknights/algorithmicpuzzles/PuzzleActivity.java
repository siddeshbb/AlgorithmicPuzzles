package com.csknights.algorithmicpuzzles;



import com.csknights.algorithmicpuzzles.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;


import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

public class PuzzleActivity extends Activity {

	static final int DIALOG_FINISH_GAME_ALERT_ID = 0;
	static final int DIALOG_PLEASE_WAIT_ID = 1;
	private Puzzle puzzle;
	private Puzzleview puzzleView;
   private boolean timerIsRunning = false;
	private Handler timerHandler;

	private TimeCounter timeCounter;

	private boolean isSolverUsed = false;
    
    private boolean menuFocus = false;

	private Runnable timerRunnable = new Runnable() {
		@Override
		public void run() {
			if (puzzle != null && puzzleView != null &&
					(puzzle.isStarted() || puzzle.isSolved()) &&
					!puzzle.isSolverRunning() ) {
				puzzleView.invalidate();
			}
			timerHandler.postDelayed(timerRunnable, 1000);
		}
	};

	private int replayIntervalMs = 500;
	private boolean replayTimerRunning = false;
	private Handler replayHandler;

	

	private int score;

	private Button undoButton;
	private Button restartButton;
	
	private Button stopButton;
	private Button replayButton;
	
	private TextView noMoreMovesMessage;
	
	private Runnable replayRunnable = new Runnable() {
		@Override
		public void run() {
			if (!puzzle.replay()) {
				replayHandler.postDelayed(replayRunnable, replayIntervalMs);
			}
			else {
				replayTimerRunning = false;
				puzzle.setReplayRunning(false);
				updateButtons();
			}
			puzzleView.invalidate();
		}
	};

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
      

       puzzle = new Q8Puzzle(this);
        	
  
        timeCounter = new TimeCounter();
		puzzle.init();
		
        setContentView(R.layout.game);
        puzzleView = (Puzzleview) findViewById(R.id.game_view);		
		puzzleView.setPuzzle(puzzle);
		puzzleView.setTimeCounter(timeCounter);

		undoButton = (Button) findViewById(R.id.game_button_undo);
		restartButton = (Button) findViewById(R.id.game_button_restart);
		
		stopButton = (Button) findViewById(R.id.game_button_stop);
		replayButton = (Button) findViewById(R.id.game_button_replay);
		
		noMoreMovesMessage = (TextView) findViewById(R.id.no_more_moves_message);
	
		undoButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				doUndo();
			}
		});
		restartButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				doRestart();
			}
		});
		
		stopButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				doStop();
			}
		});
		replayButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				doReplay();
			}
		});		
		
		updateButtons();		
		
		timerHandler = new Handler();
		replayHandler = new Handler();

		
    }

    @Override
    public void onStart() {
    	super.onStart();
    	
    	updateButtons();
    }

	private void puzzlePause() {
		if (puzzle != null && puzzleView != null &&
				puzzle.isStarted() && !puzzle.isSolved() &&
				!puzzle.isSolverRunning() ) {
    		timeCounter.pause();
    		puzzleView.setPaused(true);				
		}			
	}
	
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	if (!hasFocus && !menuFocus) {
			puzzlePause();
    	}
    	else if (menuFocus) {
    		menuFocus = false;
    	}
    }    
    
    @Override
    public void onPause() {
    	super.onPause();

    	puzzlePause();

    	if (timerIsRunning) {
    		timerHandler.removeCallbacks(timerRunnable);
    	}
    	if (replayTimerRunning) {
    		replayHandler.removeCallbacks(replayRunnable);
    	}
    }

    @Override
    public void onResume() {
    	super.onResume();
    	puzzleView.invalidate();
    	
    	if (timerIsRunning) {
    		timerHandler.postDelayed(timerRunnable, 1000);
    	}
    	if (replayTimerRunning) {
    		replayHandler.postDelayed(replayRunnable , replayIntervalMs);
    	}     	
    }

    public void resume() {
    	timeCounter.resume();   	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);

			
    	
		MenuItem undoMenuItem = menu.findItem(R.id.undo);
		undoMenuItem.setVisible(isUndoVisible());

		
		MenuItem replayMenuItem = menu.findItem(R.id.replay);
		replayMenuItem.setVisible(isReplayVisible());

		MenuItem stopMenuItem = menu.findItem(R.id.stop);
		stopMenuItem.setVisible(isStopVisible());

    	MenuItem restartMenuItem = menu.findItem(R.id.restart);
    	restartMenuItem.setVisible(isRestartVisible());
    	menuFocus = true;
    	return true;
    }

    private boolean isUndoVisible() {
    	return puzzle.isUndoPermitted() && puzzle.isStarted() &&
				!puzzle.isSolved();
    }    
    
	private boolean isRestartVisible() {
		return puzzle.isStarted() || puzzle.isSolved() &&
				 !puzzle.isReplayRunning();
	}

	private boolean isReplayVisible() {
		return puzzle.isReplayPermitted() && puzzle.isSolved() &&
				 				  !puzzle.isReplayRunning();
	}

	
	private boolean isStopVisible() {
		return puzzle.isReplayRunning();
	}

    public void updateButtons() {
    	undoButton.setVisibility(isUndoVisible() ? View.VISIBLE : View.GONE);
    	restartButton.setVisibility(isRestartVisible() ? View.VISIBLE : View.GONE);
    	
    	stopButton.setVisibility(isStopVisible() ? View.VISIBLE : View.GONE);
    	replayButton.setVisibility(isReplayVisible() ? View.VISIBLE : View.GONE);
    	
    	noMoreMovesMessage.setVisibility(puzzle.areMovesLeft() && !puzzle.isSolverRunning() ? 
    									 View.VISIBLE : View.GONE) ;
    }
    
    public void hideButtons() {
    	undoButton.setVisibility(View.GONE);
    	restartButton.setVisibility(View.GONE);
    	
    	stopButton.setVisibility(View.GONE);
    	replayButton.setVisibility(View.GONE);
    }

	
	

	private void doStop() {
		replayHandler.removeCallbacks(replayRunnable);
		puzzle.setReplayRunning(false);
		replayTimerRunning = false;
        updateButtons();
	}

	private void doRestart() {
    	stopTimer();
    	isSolverUsed = false;
    	timeCounter.reset();
    	puzzle.init();
    	puzzleView.invalidate();
        updateButtons();
	}

	private void doReplay() {
    	timeCounter.reset();
    	puzzle.copySolution();
    	puzzle.init();
    	puzzle.setReplayRunning(true);
    	replayTimerRunning = true;
    	replayHandler.postDelayed(replayRunnable , replayIntervalMs);
        updateButtons();
	}

	private void doUndo() {
		puzzle.undoLastMove();
		puzzleView.invalidate();
        updateButtons();
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
       
        case R.id.undo:
    		doUndo();
        	break;
        case R.id.replay:
        	doReplay();
            break;
        case R.id.restart:
        	doRestart();
        	break;
        case R.id.stop:
        	doStop();
        	break;
       
       
        }
        return true;
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	if (puzzle.isStarted() && !puzzle.isSolved()) {
            	showDialog(DIALOG_FINISH_GAME_ALERT_ID);
            	return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        switch(id) {
        case DIALOG_FINISH_GAME_ALERT_ID:
        	dialog = createAskUserToFinishGameAlertDialog();
            break;
       
        default:
            dialog = null;
        }

        return dialog;
    }

    private Dialog createAskUserToFinishGameAlertDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(R.string.finish_game_alert_text)
    	       .setCancelable(false)
    	       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   finish();
    	           }
    	       })
    	       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	return alert;
    }

   

    public void startTimer() {
    	if (!timerIsRunning) {
    		Log.d("PuzzleActivity", "-- Start timer -- ");
    		timerHandler.postDelayed(timerRunnable, 1000);
    		timerIsRunning = true;
    	}
    }

    public void stopTimer() {
    	if (timerIsRunning) {
    		Log.d("PuzzleActivity", "-- Stop timer -- ");
    		timerIsRunning = false;
    		timerHandler.removeCallbacks(timerRunnable);
    	}
    }

   
	public void onPuzzleSolvedByUser() {
    	if (!isSolverUsed) {
    		score = timeCounter.getTimeSeconds();
    		stopTimer();
    	
	    		String str = String.format(
	    				getResources().getString(R.string.solved_in_seconds, (Object []) null), score);
				Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    		
    	}
    }

   

}
