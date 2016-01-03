package com.me.test.game2;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class BaseItem extends Image{
	public static int ITEM_WIDTH = 100;
	public static int ITEM_HEIGHT = 100;
	public int locationX;
	public int locationY;
	
	private Body body;

	
	public Body getBody(){
		return body;
	}
	
	public void setBody(Body b){
		body = b;
	}
	
	public int getLocationX() {
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}

	protected ItemContainer _game;

	public BaseItem(ItemContainer game){
		_game = game;
		setSize(ITEM_WIDTH, ITEM_HEIGHT);
	}

	public boolean isBinded(){
		return _game.getTable().get(getLocationX(), getLocationY())==this;
	}
	

	
	public void animateMove(int startX, int startY, int endX, int endY){
		float d = 0.2f * Math.max(Math.abs(endY - startY), Math.abs(endX - startX));
		this.addAction(Actions.moveTo(endX*BaseItem.ITEM_WIDTH, endY*BaseItem.ITEM_HEIGHT, d));
	}

	@Override
	public void act(float delta){
		super.act(delta);
		
		if(body!=null){
			this.setPosition(body.getPosition().x*Game2.B2D_SCALE,body.getPosition().y*Game2.B2D_SCALE);
			this.setRotation(body.getAngle()*MathUtils.radiansToDegrees);
		}
	}
	
	public enum Direction{
		up {
			@Override
			public int getStepX() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getStepY() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public Direction getCounter() {
				// TODO Auto-generated method stub
				return Direction.valueOf("down");
			}
		},
		down {
			@Override
			public int getStepX() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getStepY() {
				// TODO Auto-generated method stub
				return -1;
			}

			@Override
			public Direction getCounter() {
				// TODO Auto-generated method stub
				return Direction.up;
			}
		},
		left {
			@Override
			public int getStepX() {
				// TODO Auto-generated method stub
				return -1;
			}

			@Override
			public int getStepY() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Direction getCounter() {
				// TODO Auto-generated method stub
				return Direction.valueOf("right");
			}
		},
		right {
			@Override
			public int getStepX() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public int getStepY() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Direction getCounter() {
				// TODO Auto-generated method stub
				return Direction.left;
			}
		};
		
		public abstract Direction getCounter();
		
		public abstract int getStepX();
		
		public abstract int getStepY();
	}
}


