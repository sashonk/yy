package com.me.test.game2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.me.test.TestGame;

public class Files {
	

	public static final FileHandle baseDirHandle = TestGame.devmode ? Gdx.files.absolute("d:\\level\\") : Gdx.files.internal("data/level/");
	
	public static final FileHandle altDirHandle = TestGame.devmode ? Gdx.files.absolute("d:\\level2\\") : Gdx.files.internal("data/level2/");

	
	public static FileHandle level(int num){
		return baseDirHandle.child(String.format("%d%s",LevelMapper.standard(num), ".dat"));
				
	}
	
	public static FileHandle diffMap(){
		return baseDirHandle.child("diffMap.ser");
	}

}
