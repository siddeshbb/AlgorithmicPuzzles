package com.csknights.algorithmicpuzzles;

import android.content.Context;

import android.graphics.Canvas;


public abstract class Puzzle {
	
	Context context;
	protected int screenWidth;
    protected int screenHeight;

  
    protected int offsetX = 5;

   
    protected int offsetY = 5;

	
    protected int topBarHeight = 60;

  
	protected int bottomBarHeight = 60;
    
  
	public void setTopBarHeight(int h) {
		topBarHeight = h;
	}

   
	public void setBottomBarHeight(int h) {
		bottomBarHeight = h;
	}	    
    
   
    public void onSizeChanged(int w, int h) {
    	screenWidth = w;
    	screenHeight = h;
    }

	
	protected int [] movesTable;

	
	protected int movesTableSize = 0;

	
	protected int [] solMovesTable;

	
	protected int solMovesTableSize = 0;

	
	protected int solMovesTableIndex;

	
	protected String name;

	
	protected String family;

    
	protected boolean enableUndo = true;

	
	protected boolean enableReplay = true;

	
	protected boolean solverRunning = false;

	
	protected boolean replayRunning = false;

	
	protected boolean replayEnded = false;

	
	protected boolean isStarted = false;

	public enum MoveResult {
		MOVE_NOT_ALLOWED,
		MOVE_OUT_OF_BOUNDS,
		RIDDLE_SOLVED,
		MOVE_SUCCESSFUL,
		MOVE_EDIT,
		RIDDLE_UNSOLVABLE
	}
	
	
	protected boolean enableAdd = false;

	
	public boolean isAddAllowed() {
		return enableAdd;
	}

	
	public boolean isStarted() {
		return isStarted;
	}    
    
	
	public Puzzle(Context context) {
		this.context = context;
	}

  public String getName() {
		return name;
	}

	
	public String getFamily() {
		return family;
	}

	
	public boolean isSolverRunning() {
		return solverRunning;
	}

	
	public void setSolverRunning(boolean val) {
		solverRunning = val;
	}	
	
	
	public boolean isReplayRunning() {
		return replayRunning;
	}

	
	public boolean isReplayEnded() {
		return replayEnded;
	}

	
	public void setReplayRunning(boolean value) {
		replayRunning = value;
	}

	
	public boolean isReplayPermitted() {
		return enableReplay;
	}

	
	public boolean isUndoPermitted() {
		return enableUndo;
	}
	
	
	public int [] getSolution() {
		return movesTable;
	}

	
    public void dumpStatus() {
        for(int i=0; i < movesTableSize; i++)
            System.out.print(movesTable[i] + " ");
        System.out.print("\n");
    }

	
	public void init() {
		if (movesTableSize > 0) {
			movesTable = new int[movesTableSize];
			for(int i =0; i<movesTableSize; i++)
				movesTable[i] = -1;
		}
		solverRunning = false;
		isStarted = false;
		replayEnded = false;		
	}
	

	
    abstract public MoveResult onTouchEvent(float x, float y);

	
	abstract public void undoLastMove();

	
	abstract public void draw(Canvas canvas);

    int totalCounter = 0;
    int counter = 0;

	

	
	public boolean replay() {
		boolean result;
        if (solMovesTableSize == 0) {
            result = true;
        }
        else {
        	result = !playNextMove();
        }

        if (result) {
        	replayEnded = true;
        }
        return result;
	}

	
	public void copySolution() {
        solMovesTableSize = movesTableSize;

        if (movesTableSize == 0)
            return;

        solMovesTable = new int [movesTableSize];

        int i;
        for(i=0; i<movesTableSize && movesTable[i]!=-1; i++) {
            solMovesTable[i] = movesTable[i];
        }
        solMovesTableSize = i;
        solMovesTableIndex = 0;
    }

	
	abstract protected void initSolver();

	abstract protected int movesMade();

	
	abstract protected boolean findNextMove();

		abstract protected boolean goBack();

		abstract public boolean isSolved();

	
	abstract protected boolean playNextMove();
    
     
    abstract public boolean areMovesLeft();

}
