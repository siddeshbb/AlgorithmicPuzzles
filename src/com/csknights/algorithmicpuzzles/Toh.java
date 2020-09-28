package com.csknights.algorithmicpuzzles;


import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;



public class Toh extends SimpleBaseGameActivity{
	
	private static final int DIALOG = 0;
	private Dialog GameSolveDialog;
	private int count=0;
	static final int DIALOG_HIGH_SCORE_NAME = 1;
	private static int CAMERA_WIDTH = 720;
    private static int CAMERA_HEIGHT = 480;
	private ITextureRegion mBackgroundTextureRegion, mTowerTextureRegion, mRing1, mRing2, mRing3;
	private Sprite mTower1, mTower2, mTower3;
		//@SuppressWarnings("rawtypes")
	private Stack<Ring> mStack1, mStack2, mStack3;
		
		@Override
		public EngineOptions onCreateEngineOptions() {
	    	final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	    	return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		}

		//@SuppressWarnings({ "rawtypes" })
		@Override
		protected void onCreateResources() {
	        try {
	        	// 1 - Set up bitmap textures
	            ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
	                @Override
	                public InputStream open() throws IOException {
	                    return getAssets().open("gfx/back.png");
	                }
	            });
	            ITexture towerTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
	                @Override
	                public InputStream open() throws IOException {
	                    return getAssets().open("gfx/toer.png");
	                }
	            });
	            ITexture ring1 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
	                @Override
	                public InputStream open() throws IOException {
	                    return getAssets().open("gfx/ring1.png");
	                }
	            });
	            ITexture ring2 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
	                @Override
	                public InputStream open() throws IOException {
	                    return getAssets().open("gfx/ring2.png");
	                }
	            });
	            ITexture ring3 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
	                @Override
	                public InputStream open() throws IOException {
	                    return getAssets().open("gfx/ring3.png");
	                }
	            });
	            // 2 - Load bitmap textures into VRAM
	            backgroundTexture.load();
	            towerTexture.load();
	            ring1.load();
	            ring2.load();
	            ring3.load();
	            
	            // 3 - Set up texture regions
	            this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
	            this.mTowerTextureRegion = TextureRegionFactory.extractFromTexture(towerTexture);
	            this.mRing1 = TextureRegionFactory.extractFromTexture(ring1);
	            this.mRing2 = TextureRegionFactory.extractFromTexture(ring2);
	            this.mRing3 = TextureRegionFactory.extractFromTexture(ring3);
	            // 4 - Create the stacks
	            this.mStack1 = new Stack<Ring>();
	            this.mStack2 = new Stack<Ring>();
	            this.mStack3 = new Stack<Ring>();
	        } catch (IOException e) {
	            Debug.e(e);
	        }
		}
		
		
		
		//@SuppressWarnings("unchecked")
		@Override
		protected Scene onCreateScene() {
			// 1 - Create new scene
			final Scene scene = new Scene();
			Sprite backgroundSprite = new Sprite(330, 240, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(backgroundSprite);
			
			// 2 - Add the towers
			mTower1 = new Sprite(142, 200, this.mTowerTextureRegion, getVertexBufferObjectManager());
			mTower2 = new Sprite(350, 200, this.mTowerTextureRegion, getVertexBufferObjectManager());
			mTower3 = new Sprite(554, 200, this.mTowerTextureRegion, getVertexBufferObjectManager());
			scene.attachChild(mTower1);
			scene.attachChild(mTower2);
			scene.attachChild(mTower3);
			// 3 - Create the rings
			Ring ring1 = new Ring(1, 139, 190, this.mRing1, getVertexBufferObjectManager()) {
			    @Override
			    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			        if (((Ring) this.getmStack().peek()).getmWeight() != this.getmWeight())
			            return false;
			        this.setPosition(pSceneTouchEvent.getX() - this.getWidth()/3, pSceneTouchEvent.getY() - this.getHeight()/3);
			        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			            checkForCollisionsWithTowers(this);
			        }
			        return true;
			    }
			};
			Ring ring2 = new Ring(2, 135, 155, this.mRing2, getVertexBufferObjectManager()) {
			    @Override
			    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			        if (((Ring) this.getmStack().peek()).getmWeight() != this.getmWeight())
			            return false;
			        this.setPosition(pSceneTouchEvent.getX() - this.getWidth()/2, pSceneTouchEvent.getY() - this.getHeight()/2);
			        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			            checkForCollisionsWithTowers(this);
			        }
			        return true;
			    }
			};
			Ring ring3 = new Ring(3, 130, 105, this.mRing3, getVertexBufferObjectManager()) {
			    @Override
			    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
			        if (((Ring) this.getmStack().peek()).getmWeight() != this.getmWeight())
			            return false;
			        this.setPosition(pSceneTouchEvent.getX() - this.getWidth(), pSceneTouchEvent.getY() - this.getHeight());
			        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			            checkForCollisionsWithTowers(this);
			        }
			        return true;
			    }
			};
			scene.attachChild(ring1);
			scene.attachChild(ring2);
			scene.attachChild(ring3);
			// 4 - Add all rings to stack one
			this.mStack1.add(ring3);
			this.mStack1.add(ring2);
			this.mStack1.add(ring1);
			// 5 - Initialize starting position for each ring
			ring3.setmStack(mStack1);
			ring2.setmStack(mStack1);
			ring1.setmStack(mStack1);
			ring3.setmTower(mTower1);
			ring2.setmTower(mTower1);
			ring1.setmTower(mTower1);
			// 6 - Add touch handlers
			scene.registerTouchArea(ring3);
			scene.registerTouchArea(ring2);
			scene.registerTouchArea(ring1);
			scene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
			scene.setTouchAreaBindingOnActionDownEnabled(true);
			return scene;

		  
		
		
		}
		
		
		
		
		@SuppressWarnings({ "unchecked", "deprecation" })
		private void checkForCollisionsWithTowers(Ring ring) {
			count++;
		    Stack<Ring> stack = null;
		    Sprite tower = null;
		    if (ring.collidesWith(mTower1) && (mStack1.size() == 0 || ring.getmWeight() < mStack1.peek().getmWeight())) {
		        stack = mStack1;
		        tower = mTower1;
		    } else if (ring.collidesWith(mTower2) && (mStack2.size() == 0 || ring.getmWeight() < mStack2.peek().getmWeight())) {
		        stack = mStack2;
		        tower = mTower2;
		    } else if (ring.collidesWith(mTower3) && (mStack3.size() == 0 || ring.getmWeight() < mStack3.peek().getmWeight())) {
		        stack = mStack3;
		        tower = mTower3;
		    } else {
		    	
		    	stack = (Stack<Ring>) ring.getmStack();
		        tower = ring.getmTower();
		    }
		    ring.getmStack().remove(ring);
		    if (stack != null && tower !=null && stack.size() == 0) {
		        ring.setPosition(tower.getX() + tower.getWidth()/25 - ring.getWidth()/30, tower.getY() + tower.getHeight()/100 - ring.getHeight()*2);
		    } else if (stack != null && tower !=null && stack.size() > 0) {
		        ring.setPosition(tower.getX() + tower.getWidth()/50 - ring.getWidth()/20, stack.peek().getY()+tower.getHeight() - ring.getHeight()*5);
		    }
		    stack.add(ring);
		    ring.setmStack(stack);
		    ring.setmTower(tower);
		

		if(mStack3.size()==3){
			 new Thread(){
				 public void run(){
					 Toh.this.runOnUiThread(new Runnable(){
						 public void run(){
							 Toast.makeText(Toh.this,"No of moves needed by you:"+count,Toast.LENGTH_LONG).show();
							  showDialog(DIALOG); 
							 
						 }
					 });
				 }
			 }.start();
			}
			  
				  
						
					
		    }
		
		
		
		 @Override
		    protected Dialog onCreateDialog(int id) {
		        Dialog dialog;
		        switch(id) {
		        
		       
		        case DIALOG:
		        	dialog = createGameSolveDialog();
		        	GameSolveDialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.algp);
		        	break;
		     
		        default:
		            dialog = null;
		        }

		        return dialog;
		    }
		 private Dialog createGameSolveDialog() {
			 GameSolveDialog = new Dialog(this);
			 GameSolveDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			 GameSolveDialog.setContentView(R.layout.dialog);
			 GameSolveDialog.setTitle(R.string.app_name);

		        Button button = (Button)GameSolveDialog.findViewById(R.id.diaok);

		        button.setOnClickListener(new OnClickListener() {
		        			public void onClick(View v) {
		        				GameSolveDialog.dismiss();
		        				
		        			}
		        		});

		        return GameSolveDialog;
		    
		
		
	}
		
	
	
}
	
