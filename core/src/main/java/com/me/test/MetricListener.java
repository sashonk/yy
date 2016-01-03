package com.me.test;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class MetricListener extends InputListener{

	
	public boolean mouseMoved (InputEvent event, float x, float y) {
		System.out.println(String.format("obj=%s [x=%2f y=%2f]", event.getTarget().toString(), x, y));
		return false;
	}
}
