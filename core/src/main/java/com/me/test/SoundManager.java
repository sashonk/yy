package com.me.test;

import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	private IGame _game;
	SoundManager(IGame game){
		_game = game;
	}
	
	public void play(String sound){
		
		Settings s = _game.getSettings();
		
		if(s.isSoundEnabled()){
			Sound snd = _game.getManager().getSound(sound);
			snd.play(s.getSoundVolume());
		}
	} 
}
