package com.me.test;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

public class Settings {
	boolean musicEnabled;
	private Defaults defaults;
	
	TestGame _game;
	public Settings(TestGame game){
		_game = game;
		defaults = new Defaults();
	}
	
	public Defaults defaults(){
		return defaults;
	}

	public void toDefaults(){
		setMusicEnabled(defaults.isMusicEnabled()) ;
		setSoundEnabled(defaults.isSoundEnabled());
		setSoundVolume(defaults.getSoundVolume());
		setMusicVolume(defaults.getMusicVolume());
		setLanguage(defaults.getLanguage());

	}
	
	public void write(){
		Preferences prefs = _game.getPreferences();
		prefs.putBoolean("musicEnabled", musicEnabled);
		prefs.putBoolean("soundEnabled", soundEnabled);
		prefs.putFloat("soundVolume", soundVolume);
		prefs.putFloat("musicVolume", musicVolume);
		prefs.putString("language", language);
		prefs.flush();
	}


	public void read(){
		Preferences prefs = _game.getPreferences();
		setMusicEnabled(prefs.getBoolean("musicEnabled", defaults.isMusicEnabled())) ;
		setSoundEnabled(prefs.getBoolean("soundEnabled", defaults.isSoundEnabled()))  ;
		setSoundVolume( prefs.getFloat("soundVolume", defaults.getSoundVolume()));
		setMusicVolume( prefs.getFloat("musicVolume", defaults.getMusicVolume())) ;
		setLanguage(prefs.getString("language", defaults.getLanguage())) ;
	}

	public boolean isMusicEnabled() {
		return musicEnabled;
	}

	public void setMusicEnabled(boolean musicEnabled) {
		this.musicEnabled = musicEnabled;		
		for(SettingsListener l : listeners){
			l.onMusicEnabledChange(this.musicEnabled);
		}		
	}

	public boolean isSoundEnabled() {
		return soundEnabled;
	}

	public void setSoundEnabled(boolean soundEnabled) {
		this.soundEnabled = soundEnabled;
		for(SettingsListener l : listeners){
			l.onSoundEnabledChange(this.soundEnabled);
		}
	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(float soundVolume) {
		this.soundVolume = soundVolume;
		for(SettingsListener l : listeners){
			l.onSoundVolumeChange(this.soundVolume);
		}
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
		for(SettingsListener l : listeners){
			l.onMusicVolumeChange(this.musicVolume);
		}
	}

	boolean soundEnabled;
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
		for(SettingsListener l : listeners){
			l.onLangChange(this.language);
		}
	}

	float soundVolume;
	
	float musicVolume;
	
	String language;
	 
	List<SettingsListener> listeners = new LinkedList<SettingsListener>();
	
	public void registerListener(SettingsListener listener){
			listeners.remove(listener);
			listeners.add(listener);		
	}

	public static interface SettingsListener{
		public void onMusicEnabledChange(boolean enabled);
		public void onSoundEnabledChange(boolean enabled);
		public void onMusicVolumeChange(float newValue);
		public void onSoundVolumeChange(float newValue);
		public void onLangChange(String newLang);
	}
	
	public static class SettingsAdapter implements SettingsListener{

		@Override
		public void onMusicEnabledChange(boolean enabled) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSoundEnabledChange(boolean enabled) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMusicVolumeChange(float newValue) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSoundVolumeChange(float newValue) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLangChange(String newLang) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public  class Defaults{

		public String getLanguage() {
			return Locale.getDefault().getLanguage();
		}
		
		public float getMusicVolume() {
			return 0.5f;
		}
		
		public float getSoundVolume() {
			return 0.5f;
		}
		
		public boolean isMusicEnabled() {
			return true;
		}

		public boolean isSoundEnabled() {
			return true;
		}
	}
}
