package com.me.test.game2.item.factory;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.ItemFactory;
import com.me.test.game2.item.Wall;
import com.me.test.game2.item.Water;

public class WaterFactory extends ItemFactory<Water>{

	@Override
	public Water create(int x, int y, ItemContainer context) {
		Water water = new Water(context);
		context.getTable().put(water, x, y);				
		return water;
	}

	@Override
	public int getId() {
		return 6;
	}

	@Override
	public String toolStyleName() {
		return "water_icon";
	}

	@Override
	public Class<Water> getItemClass() {
		return Water.class;
	}


	@Override
	public void destroy(Water item, ItemContainer context) {
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
	}
	
	@Override
	public void translate(Water item, int targetLocationX, int targetLocationY,
			ItemContainer context) {
		BaseItem targetItem = context.getTable().get(targetLocationX, targetLocationY);
		if(targetItem!=null){
			throw new IllegalStateException();
		}

		
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
		context.getTable().put(item, targetLocationX, targetLocationY);

		
	}
}
