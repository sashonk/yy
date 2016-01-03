package com.me.test;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;


public class SmartListener extends InputListener{
	Runnable _longPressAction;
	boolean _handleNormalAction;
	float _longPressDuration;
	Runnable _normalAction;
	Action _delayedAction;
	
	public SmartListener(Runnable normalAction, Runnable longPressAction, float longPressDuration){
		_longPressDuration= longPressDuration;
		_normalAction = normalAction;
		_longPressAction = longPressAction;
	}
	
	@Override
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		_handleNormalAction= true;
		_delayedAction = Actions.sequence(Actions.delay(_longPressDuration), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				_handleNormalAction = false;
				_longPressAction.run();
				
			}
		}));
		event.getTarget().addAction(_delayedAction);	
		return true;
	}
	@Override
	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {		
		if(_delayedAction!=null){
			event.getTarget().removeAction(_delayedAction);
		}
		
		if(_handleNormalAction){
			_normalAction.run();
		}

		
	}
}
