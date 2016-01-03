package com.me.test.game2.item;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.brashmonkey.spriter.Animation;
import com.me.test.PlayerAdapter;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.SpriterItem;

public class Yin extends SpriterItem{
	String prevAnim;
	
	@Override
	public String entityName() {
		return "yin";
	}
	
	public Yin(final ItemContainer game) {
		super(game);

		gesture = true;
		
		
		final PlayerAdapter adapter =  new PlayerAdapter(){
			@Override
			public void animationFinished(Animation animation) {
				getPlayer().speed = 0;
				if("close".equals(animation.name)){
					getPlayer().setTime(120);
					
				}
				//getPlayer().update();
				
				
			}
		};
		
		getPlayer().speed = 15;
		//getPlayer().setAnimation("blink");
		getPlayer().addListener(adapter);
		final float delay = game.getGame().getRandom().nextFloat()*3 + 5;
		Action g = forever(sequence(delay(delay), run(new Runnable() {
			
			@Override
			public void run() {
				if(!gesture){
					return;
				}
			//	getPlayer().speed = 2;
				
			//	System.out.println(delay);
				int p = game.getGame().getRandom().nextInt(100);
				String anim = null;
				int speed = 15;
				if(p <= 50 || !"blink".equals(prevAnim)){
					System.out.println("yin blink");
					//speed = 100;
					anim = "blink";
				}
				else if(p <=75){
					anim = "wide";
				}
				else if(p <=87){
					
					anim = "left";
				}
				else{
					anim = "right";
				}
				getPlayer().speed = speed;
				getPlayer().setAnimation(anim);
				prevAnim = anim;
				
				//setDoUpdate(true);
			}
		})));
		this.addAction(g);
		
		//this.addAction(gesture);

	}
	
	 

	@Override
	public boolean isBinded(){
		return _game.getTable().getSpot(getLocationX(), getLocationY())==this;
	}




}
