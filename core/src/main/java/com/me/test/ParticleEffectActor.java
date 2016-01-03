package com.me.test;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class ParticleEffectActor extends Actor implements Disposable{
	PooledEffect _effect;
	boolean _started;
			
	public ParticleEffectActor(PooledEffect effect){
		_effect = effect;
		_started = false;
	}
	
	@Override
	public void draw(Batch batch, float delta){
		if(_started){
			_effect.draw(batch, delta/50);
		}
	}
	
	public void start(){
		//_effect.start();
		_started= true;
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		
		if(_started){
		_effect.setPosition(getX(),getY());
		}

	}
	
	public void stop(){
		_started = false;
	}
	
	public PooledEffect getEffect(){
		return _effect;
	}

	@Override
	public void dispose() {
		_effect.dispose();
		
	}
}
