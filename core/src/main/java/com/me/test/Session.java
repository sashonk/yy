package com.me.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Session {

	boolean inGame;
	int lastLevel;
	int walkthroughLastLevel;
	int ownedYangs;
	boolean edu;
	
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	
	public boolean isInGame() {
		return inGame;
	}
	
	public int getLastLevel(){
		return lastLevel;
	}
	
	public void setLastLevel(int l){
		lastLevel = l;
	}
	
	public void setWalkthroughLastLevel(int l){
		walkthroughLastLevel =l;
	}
	
	public int getWalkthroughLastLevel(){
		return walkthroughLastLevel;
	}
	
	public void setOwnedYangs(int oy){
		ownedYangs = oy;
	}
	
	public int getOwnerYangs(){
		return ownedYangs;
	}
	
	public void setEducation(boolean v){
		edu = v;
	}
	
	public boolean isEducation(){
		return edu;
	}

	/*public List<Feature> getEnabledFeatures(){
		return enabledFeatures;
	}
	

	
	List<Feature> enabledFeatures = new LinkedList<Feature>();
*/
}
