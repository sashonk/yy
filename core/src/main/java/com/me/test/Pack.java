package com.me.test;

import java.io.File;

//import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Pack {
    public static void main (String[] args) throws Exception {
    	String baseDir = "D:\\testpack\\";
    	String []dirs = {"main", "loading", "yin"};   
    	
    	
   
    	for(String dir : dirs){
    		File packed = new File(baseDir+dir+"\\packed");
    		if(packed.exists())
    		for(File f : packed.listFiles()){
    			f.delete();
    		}
    	//	TexturePacker.process(baseDir+dir, baseDir+dir+"\\packed", dir);
    	}
    }
}
