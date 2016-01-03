package com.me.test.game2.item.factory;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.ItemFactory;
import com.me.test.game2.item.Box;
import com.me.test.game2.item.Fall;

public class FallFactory extends ItemFactory<Fall>{

	@Override
	public Fall create(int x, int y, ItemContainer context) {
		Fall fall = new Fall(context);
		context.getTable().put(fall, x, y);
		return fall;
	}



	@Override
	public int getId() {
		return 5;
	}

	@Override
	public String toolStyleName() {
		return "fall_icon";
	}



	@Override
	public Class<Fall> getItemClass() {
		return Fall.class;
	}

	@Override
	public void destroy(Fall item, ItemContainer context) {
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
	}
	@Override
	public void translate(Fall item, int targetLocationX, int targetLocationY,
			ItemContainer context) {
		BaseItem targetItem = context.getTable().get(targetLocationX, targetLocationY);
		if(targetItem!=null){
			throw new IllegalStateException();
		}

		
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
		context.getTable().put(item, targetLocationX, targetLocationY);

		
	}
}
