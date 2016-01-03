package com.me.test;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Rectangle;


public class SpriterPlayer extends Actor{
	private Player _player;
	private Drawer<Sprite> _drawer;
	public SpriterPlayer(Player player, Drawer<Sprite> drawer){
		//setPosition(x, y);
		_player= player;
		_drawer = drawer;
		
		Rectangle bb =  _player.getBoudingRectangle(null);
		
		System.out.println(new StringBuilder().append(bb.left).append(' ').append(bb.right).append(' ').append(bb.top).append(' ').append(bb.bottom));
		System.out.println(_player.getX()+" "+_player.getY());
		
		
		//this.setWidth(width);
		//this.setWidth(width);
		this.setWidth(bb.right-bb.left);
		this.setHeight(bb.top-bb.bottom);
		
	
		_player.setPosition(getX(), getY());
		setTouchable(Touchable.disabled);
		
		
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		_player.setPosition(getX(), getY());
		
		
	
		_player.update();
		
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		
		_drawer.draw(_player);
		
	}
	
	public Player getPlayer(){
		return _player;
	}
}
