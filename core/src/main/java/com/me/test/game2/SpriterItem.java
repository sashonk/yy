package com.me.test.game2;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Rectangle;

public abstract class SpriterItem extends BaseItem{
	protected Player _player;
	protected Drawer<Sprite> _drawer;
	
	protected boolean gesture;
	
	
	public void setGuesture(boolean g){
		gesture = g;
	}
	
	public abstract String entityName();
	
	public SpriterItem(ItemContainer game) {
		super(game);
		_player = new Player(game.getReader().getData().getEntity(entityName()));
		_drawer = game.getDrawer();
		Rectangle bb =  _player.getBoudingRectangle(null);
		float scale = BaseItem.ITEM_WIDTH / (float) bb.right-bb.left;		
		_player.setScale(scale);
		_update = true;
	//	_player.setAngle(90);
		 
		
		//System.out.println(new StringBuilder().append(bb.left).append(' ').append(bb.right).append(' ').append(bb.top).append(' ').append(bb.bottom));
		//System.out.println(_player.getX()+" "+_player.getY());
		
		
		//this.setWidth(width);
		//this.setWidth(width);
	//	this.setWidth(bb.right-bb.left);
	//	this.setHeight(bb.top-bb.bottom);
		
		
	//	_player.setPosition(getX(), getY());
		setTouchable(Touchable.disabled);
		
		
	}
	
	boolean _update;
	@Deprecated
	public void setDoUpdate(boolean update){
		_update = update;
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		_player.setPosition(getX(), getY());
		//_player.setAngle(getRotation());
		
		if(_update)
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
