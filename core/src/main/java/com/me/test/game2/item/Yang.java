package com.me.test.game2.item;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.brashmonkey.spriter.Animation;
import com.me.test.PlayerAdapter;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.SpriterItem;


public class Yang extends SpriterItem{

	@Override
	public String entityName() {
		return "yang";
	}
	
	public Yang(final ItemContainer game) {
		super(game);
		gesture = true;
		
		final PlayerAdapter adapter =  new PlayerAdapter(){
			@Override
			public void animationFinished(Animation animation) {
				getPlayer().speed = 0;
				if("close".equals(animation.name)){
					getPlayer().setTime(240);
					
				}
			}
		};
		
		//getPlayer().speed = 15;
		//getPlayer().setAnimation("blink");
		getPlayer().addListener(adapter);
		final float delay = game.getGame().getRandom().nextFloat()*3 + 5;
		Action g = forever(sequence(delay(delay), run(new Runnable() {
			
			@Override
			public void run() {
				if(!gesture){
					return;
				}

				String anim = "blink";
				getPlayer().setAnimation(anim);
		
				getPlayer().speed = 15;
				//setDoUpdate(true);
			}
		})));
		this.addAction(g);
	}

	public boolean can(Direction dir){
		int stepX = dir.getStepX();
		int stepY = dir.getStepY();
		
		int targetLocationX = getLocationX() + stepX;
		int targetLocationY = getLocationY() + stepY;
		
		BaseItem item =  _game.getTable().get(targetLocationX, targetLocationY);
		
		return item==null;
	}
	
	public boolean move(Direction dir){
		boolean result = false;
		
		for(Action a : getActions()){
			if(a instanceof MoveToAction){
				return false;
			}
		}
		
		int stepX = dir.getStepX();
		int stepY = dir.getStepY();
		
		int targetLocationX = getLocationX() + stepX;
		int targetLocationY = getLocationY() + stepY;
		

			
			
			BaseItem item =  _game.getTable().get(targetLocationX, targetLocationY);
			BaseItem spot =  _game.getTable().getSpot(targetLocationX, targetLocationY);
			
			if(item==null || item instanceof Water){
				if(item!=null){
					item.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.removeActor()));
				}
				
				
				 inSpot = false;
				 int sourceX = getLocationX();
				 int sourceY = getLocationY();
				 _game.getTable().put(null, sourceX, sourceY);
				 _game.getTable().put(this, targetLocationX, targetLocationY);
				 
				 if(spot!=null){
					 inSpot = true;
				 }
				 
				 releaseGround(sourceX, sourceY);
				 result = true ;
			}
			else if(item instanceof Ground){
												
				 inSpot = false;
				 int sourceX = getLocationX();
				 int sourceY = getLocationY();
				 _game.getTable().put(null,sourceX,sourceY);
				 _game.getTable().put(this, targetLocationX, targetLocationY);
				 
				 if(spot!=null){
					 inSpot = true;
				 }
				 
				 releaseGround(sourceX, sourceY);
					_groundItem = item;
					_groundItem.addAction(Actions.fadeOut(0.5f));
				 result = true ;
			}						
			else if(item instanceof Box){
				int targetLocation2X = targetLocationX + stepX;
				int targetLocation2Y = targetLocationY + stepY;
				BaseItem item2 =  _game.getTable().get(targetLocation2X, targetLocation2Y);
				
				if(item2==null){

					 inSpot = false;
					 int sourceX = getLocationX();
					 int sourceY = getLocationY();
					 _game.getTable().put(_groundItem!=null ? _groundItem : null, sourceX, sourceY);
					 _game.getTable().put(this, targetLocationX, targetLocationY);
					 _game.getTable().put(item, targetLocation2X, targetLocation2Y);
					 
					 if(spot!=null){
						 inSpot = true;
					 }
					 
					 releaseGround(sourceX, sourceY);
					 result = true ;
				
				}
				
			}
		
		
	
		return result;
	}
	
	void releaseGround(int groundX, int groundY){
		if(_groundItem!=null){
			_groundItem.addAction(Actions.fadeIn(0.5f));
			 _game.getTable().put(_groundItem, groundX, groundY);
		}
		
		_groundItem = null;	
	}
	
/*	public boolean isBinded(){
		return _game.getTable().get(getLocationX(), getLocationY())==this;
	}*/

	public boolean isInSpot(){
		return inSpot;
	}
	boolean inSpot;

	private BaseItem _groundItem;


}
