package com.me.test.game2.ui;

import com.badlogic.gdx.scenes.scene2d.Event;

public class ButtonEvent extends Event{
	String _button;
	Source _source;
	
	public ButtonEvent(String button){
		_button = button;
		_source = Source.PLAYER;
	}
	
	public ButtonEvent(String button, Source source){
		_button = button;
		_source = source;
	}
	
	public Source getSource(){
		return _source;
	}
	
	public String getButton(){
		return _button;
	}
	
	public enum Source{
		PLAYER,
		TIPS,
		DEMO;
	}

}
