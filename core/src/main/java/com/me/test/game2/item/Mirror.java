package com.me.test.game2.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.test.game2.ItemContainer;


public class Mirror extends Yang{

	@Override
	public String entityName() {
		return "mirror";
	}
	
	public Mirror(ItemContainer game) {
		super(game);
	
		
	}


	

	
	public boolean move(Direction dir){
		return super.move(dir.getCounter());
	}
	


}
