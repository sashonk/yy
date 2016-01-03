package com.me.test.game2.item.factory;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.ItemFactory;
import com.me.test.game2.item.Box;
import com.me.test.game2.item.Fall;
import com.me.test.game2.item.Ground;

public class GroundFactory extends ItemFactory<Ground>{

	@Override
	public Ground create(int x, int y, ItemContainer context) {
		Ground fall = new Ground(context);
		context.getTable().put(fall, x, y);
		return fall;
	}



	@Override
	public int getId() {
		return 8;
	}

	@Override
	public String toolStyleName() {
		return "ground_icon";
	}



	@Override
	public Class<Ground> getItemClass() {
		return Ground.class;
	}

	@Override
	public void destroy(Ground item, ItemContainer context) {
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
	}
	@Override
	public void translate(Ground item, int targetLocationX, int targetLocationY,
			ItemContainer context) {
		BaseItem targetItem = context.getTable().get(targetLocationX, targetLocationY);
		if(targetItem!=null){
			throw new IllegalStateException();
		}

		
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
		context.getTable().put(item, targetLocationX, targetLocationY);

		
	}
}
