package com.me.test.game2.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.game2.Controller;
import com.me.test.game2.Game2;

public class Keyboard extends Group implements Controller{
	
	@Override
	public void block(){
		this.setTouchable(Touchable.disabled);
	}
	
	@Override
	public void unblock(){
		this.setTouchable(Touchable.enabled);
	}
	
	public Keyboard(Game2 theGame){
		Skin s = theGame.getGame().getManager().getSkin();
		
		float width = 110;
		float height = 40;
		Button left = new Button(s, "left");
		left.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Keyboard.this.fire(new ButtonEvent("left"));
			}
		});
		left.setBounds(0, 40, height, width);
		addActor(left);
		
		Button right = new Button(s, "right");
		right.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Keyboard.this.fire(new ButtonEvent("right"));
			}
		});
		right.setBounds(150, 40, height, width);
		addActor(right);
		
		Button up = new Button(s, "up");
		up.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Keyboard.this.fire(new ButtonEvent("up"));
			}
		});
		up.setBounds(40, 150, width, height);
		addActor(up);
		
		Button down = new Button(s, "down");
		down.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Keyboard.this.fire(new ButtonEvent("down"));
			}
		});
		down.setBounds(40, 0, width, height);
		addActor(down);
		
		this.setSize(2*width+height, 2*width+height);
		
		
	}



}
