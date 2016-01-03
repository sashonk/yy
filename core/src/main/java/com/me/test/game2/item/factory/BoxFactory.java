package com.me.test.game2.item.factory;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.ItemFactory;
import com.me.test.game2.item.Box;

public class BoxFactory extends ItemFactory<Box>{

	@Override
	public Box create(int x, int y, ItemContainer context) {
		Box box = new Box(context);
		context.getTable().put(box, x, y);
		return box;
	}

	

	@Override
	public int getId() {
		return 4;
	}



	@Override
	public String toolStyleName() {
		return "box_icon";
	}



	@Override
	public Class<Box> getItemClass() {
		return Box.class;
	}



	@Override
	public void destroy(Box item, ItemContainer context) {
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
	}



	@Override
	public void translate(Box item, int targetLocationX, int targetLocationY,
			ItemContainer context) {
		BaseItem targetItem = context.getTable().get(targetLocationX, targetLocationY);
		if(targetItem!=null){
			throw new IllegalStateException();
		}

		
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
		context.getTable().put(item, targetLocationX, targetLocationY);

		
	}



	



}

