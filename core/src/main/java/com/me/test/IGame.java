package com.me.test;

import java.util.Random;

public interface IGame {

	L10nManager getL10n();
	
	ResourcesManager getManager();
	
	Settings getSettings();
	
	SoundManager getSoundManager();
	
	IActivityRequestHandler getNative();
	
	Random getRandom();

}
