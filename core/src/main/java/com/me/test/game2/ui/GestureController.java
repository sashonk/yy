package com.me.test.game2.ui;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.me.test.game2.BaseItem.Direction;
import com.me.test.game2.Controller;

public class GestureController extends Group implements Controller{
	GestureDetector detector;
	boolean isBlocked ;
	
	public GestureDetector getDetector(){
		return detector;
	}
	
	public GestureController(){
		isBlocked = false;
		GestureListener listener = new GestureAdapter() {
			boolean flag = false;
			
			@Override
			public boolean touchDown (float x, float y, int pointer, int button) {
				if(pointer!=0){
					return false;
				}
				flag = true;
				
				
				return true;
			}

			@Override
			public boolean fling (float velocityX, float velocityY, int button) {
				return true;
			}

		
			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				
				if(!flag || isBlocked){
					return false;
				}
				
				flag = false;
				
				System.out.println(deltaX+" "+deltaY);
				float modx = Math.abs(deltaX);
				float mody =  Math.abs(deltaY);
				
				Direction dir = null;
				
				if(modx >= mody){
					dir = deltaX > 0 ? Direction.right : Direction.left;
				}
				else{
					dir = deltaY > 0 ? Direction.down : Direction.up;
				}
				
				fire(new ButtonEvent(dir.name()));
				
				return true;
			}
			

		};
		detector = new GestureDetector(listener);
	}

	@Override
	public void block() {
		isBlocked = true;		
	}

	@Override
	public void unblock() {
		isBlocked = false;		
	}

}
