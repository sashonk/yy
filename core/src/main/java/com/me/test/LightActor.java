package com.me.test;

import com.badlogic.gdx.scenes.scene2d.Actor;

import box2dLight.Light;

public class LightActor extends Actor{
	Light _light;
	Actor _followActor;
 
	public LightActor(Light light){
		_light = light;
	}

	@Override
	public void act(float dt){
		super.act(dt);
		if(_followActor!=null){
			this.setX(_followActor.getX());
			this.setY(_followActor.getY());
		}
		
		_light.setPosition(this.getX(), this.getY());		
	}
	
	public Light getLight(){
		return _light;
	}
	
	public void follow(Actor followActor){
		_followActor =followActor;
	}
	
	public void unfollow(){
		_followActor = null;
	}
}
