package com.me.test.game2;

import com.me.test.game2.ItemContainer.LevelMeta;

public class GameHelper {
	public static int calculateAchivement(LevelMeta meta,  int stepCount){
		int achivement;
		if(stepCount <=meta.get_goal3threshold()){
			achivement = 3;
		}
		else if(stepCount <=meta.get_goal2threshold()){
			achivement = 2;			
		}
		else {
			achivement = 1;
		}
		
		return achivement;
	}
}
