package com.csknights.algorithmicpuzzles;

import com.csknights.graphics.ImageResizer;
import com.csknights.algorithmicpuzzles.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;


public class Q8Puzzle extends Puzzle
{
    private int squareWidth = 30;

    
    private int boardSize = 8;

	
	Bitmap queenImage;
	Bitmap whiteSquare;
	Bitmap blackSquare;
	Bitmap bgTile;

	public Q8Puzzle(Context context) {
		super(context);
		family = "Q8Puzzle";
		name = "" + boardSize + " " +
			   context.getResources().getString(R.string.queens);
	}

	

	public void onSizeChanged(int w, int h) {
		super.onSizeChanged(w, h);

		if (w == 0 || h == 0) {
			return;
		}

		int sizeX = (w - 10) / boardSize;
		int sizeY = (h - 10 - topBarHeight - bottomBarHeight) / boardSize;
		
		squareWidth = sizeX < sizeY ? sizeX : sizeY;
		
		offsetX = (screenWidth - squareWidth*boardSize)/2;
		offsetY = (screenHeight - squareWidth*boardSize)/2;
		
		if (offsetY < topBarHeight + 5) {
			offsetY = topBarHeight + 5;
		}		

		ImageResizer imageResizer = new ImageResizer();
		queenImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.queen);
		imageResizer.init(queenImage.getWidth(), queenImage.getHeight(), 
						  squareWidth - 2, squareWidth - 2);
		queenImage = imageResizer.resize(queenImage);

		blackSquare = BitmapFactory.decodeResource(context.getResources(), R.drawable.bs);
		imageResizer.init(blackSquare.getWidth(), blackSquare.getHeight(), 
				  		  squareWidth, squareWidth);		
		blackSquare = imageResizer.resize(blackSquare);

		whiteSquare = BitmapFactory.decodeResource(context.getResources(), R.drawable.ws);
		whiteSquare = imageResizer.resize(whiteSquare);

		bgTile = BitmapFactory.decodeResource(context.getResources(), R.drawable.back1);

	}

   
    @Override
	public void init() {
        movesTableSize = boardSize;
        super.init();
        name = "" + boardSize + " " + context.getResources().getString(R.string.queens);
        enableUndo = false;
    }

	
    public MoveResult onTouchEvent(float x, float y) {
        // Figure out the row/column
        int c = (int) (x - offsetX) / squareWidth;
        int r = (int) (y - offsetY) / squareWidth;
		r = boardSize - 1 - r;

        MoveResult result = makeMove(r, c);
        if (result == MoveResult.MOVE_SUCCESSFUL) {
			if (!isStarted) {
				isStarted = true;
			}
        }

        return result;
    }

    
    public MoveResult makeMove(int r, int c) {
		if (c < boardSize && r < boardSize) {
			if (movesTable[c] == -1) {
				movesTable[c] = r;
				if (movesMade()!=0) {
					if (movesMade()==boardSize) {
						return MoveResult.RIDDLE_SOLVED;
					}
					return MoveResult.MOVE_SUCCESSFUL;
				}
				else {
					movesTable[c] = -1;
					return MoveResult.MOVE_NOT_ALLOWED;
				}
			}
			else if (movesTable[c] == r) {
				movesTable[c] = -1;
				return MoveResult.MOVE_SUCCESSFUL;
			}
			else {
				return MoveResult.MOVE_NOT_ALLOWED;
			}
		}
		return MoveResult.MOVE_OUT_OF_BOUNDS;
    }

   
    public void undoLastMove() {

    }

    private void drawBackgroundRepeat(Canvas canvas, Bitmap bgTile) {
    	float left = 0, top = 0;
    	float bgTileWidth = bgTile.getWidth();
    	float bgTileHeight = bgTile.getWidth();

    	while (left < screenWidth) {
    		while (top < screenHeight) {
    			canvas.drawBitmap(bgTile, left, top, null);
    			top += bgTileHeight;
    		}
    		left += bgTileWidth;
    		top = 0;
    	}
    }

	
    public void draw(Canvas canvas) {
    	drawBackgroundRepeat(canvas, bgTile);

     	float left = 0, top = 0;

    	Paint paint = new Paint();
    	paint.setStrokeWidth(2);
    	paint.setStyle(Style.STROKE);
    	paint.setAntiAlias(true);
    	paint.setColor(Color.argb(0xFF, 0xC9, 0x7F, 0x2A));
    	paint.setColor(Color.WHITE);
    	canvas.drawRect(offsetX-2, offsetY-2,
    			offsetX + boardSize*squareWidth + 2,
    			offsetY + boardSize*squareWidth + 2, paint);

		for(int row=0; row<boardSize; row++) {
			for(int column=0;column<boardSize;column++) {
				Bitmap squareImage;
				if((row+column)%2==0) {
					// White square
			    	//paint.setColor(0xFFFFFFFF);
			    	squareImage = whiteSquare;
			    	paint.setColor(Color.WHITE);
				}
				else {
					// Black square
					//paint.setColor(0xFF000000);
					squareImage = blackSquare;
					paint.setColor(Color.BLACK);
				}
				left = offsetX + column*squareWidth;
				top = offsetY + row*squareWidth;
		    	//canvas.drawRect(left, top,
		    		//	   	    left + squareWidth, top + squareWidth, paint);
			  canvas.drawBitmap(squareImage, left, top, null);
				if((boardSize - 1 - movesTable[column])==row) {
					canvas.drawBitmap(queenImage, left, top, null);
					
				}
			}
		}
		
    }

	
    protected int movesMade() {
    	int i, j, queens = 0;

    	for(i=0;i<boardSize;i++) {
    		if (movesTable[i]!=-1) {
    			queens++;
    			for(j=i+1;j<boardSize;j++) {
	    			if (movesTable[j]==-1)
	    				continue;
	    			if (movesTable[i] == movesTable[j])
	    				return(0);
	    			if (i + movesTable[i] == j + movesTable[j])
	    				return(0);
	    			if (i - movesTable[i] == j - movesTable[j])
	    				return(0);
	    		}
    		}
    	}
    	return(queens);
    }

   
    int queensPlaced;

	
	protected void initSolver() {
		int i;

		queensPlaced = 0;
		for (i = 0; i < boardSize; i++) {
			if (movesTable[i]!= -1)
				queensPlaced = i + 1;
		}

		// Remove queens placed in subsequent columns
		for (; i < boardSize; i++) {
			movesTable[i]= -1;
		}
	}

	
    public boolean isSolved() {
        return(movesMade() == movesTableSize);
    }

	
    protected boolean findNextMove() {
    	if (queensPlaced == 0) {
    		movesTable[0] = 0;
    		queensPlaced++;
    		return(true);
    	}
    	else {
    		int r, c = queensPlaced;
    		for(r = 0; r < boardSize; r++) {
    			MoveResult result = makeMove(r,c);

    			if (result == MoveResult.MOVE_SUCCESSFUL ||
    				result == MoveResult.RIDDLE_SOLVED) {
    				queensPlaced++;
    				return(true);
    			}
    		}
    	}
        return(false);
    }

	
    protected boolean playNextMove()
    {
        if (solMovesTableIndex >= solMovesTableSize)
            return(false);

        makeMove(solMovesTable[solMovesTableIndex], solMovesTableIndex);
        solMovesTableIndex++;

        if (solMovesTableIndex == solMovesTableSize)
            return(false);

        return(true);
    }

	
    protected boolean goBack()
    {
        while(queensPlaced > 0) {
        	int c = queensPlaced - 1;
        	int r = movesTable[c];
        	makeMove(r, c);	// Undo last move
        	queensPlaced--;

        	for(r=r+1; r<boardSize; r++) {
        		if (makeMove(r,c) == MoveResult.MOVE_SUCCESSFUL) {
        			queensPlaced++;
        			return(true);
        		}
        	}
        }

    	return(false);
    }

	@Override
	public boolean areMovesLeft() {
		if (isSolved()) {
			return false;
		}
		
		int currentMoves = movesMade();
		
		for(int r=0;r<boardSize;r++) {
			for(int c=0;c<boardSize;c++) {
				if (movesTable[c] >= 0) {
					continue;
				}
				movesTable[c] = r;
				if (movesMade() > currentMoves) {
					movesTable[c] = -1;
					return false;
				}
				movesTable[c] = -1;
			}
		}
		return true;
	}
}

