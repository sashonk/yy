package com.me.test.game2.item.factory;

import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.ItemFactory;
import com.me.test.game2.item.Metal;

public class MetalFactory extends ItemFactory<Metal>{

	@Override
	public Metal create(int x, int y, ItemContainer context) {
		Metal fall = new Metal(context);
		context.getTable().put(fall, x, y);
		return fall;
	}



	@Override
	public int getId() {
		return 9;
	}

	@Override
	public String toolStyleName() {
		return "metal_icon";
	}



	@Override
	public Class<Metal> getItemClass() {
		return Metal.class;
	}

	@Override
	public void destroy(Metal item, ItemContainer context) {
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
	}
	@Override
	public void translate(Metal item, int targetLocationX, int targetLocationY,
			ItemContainer context) {
		BaseItem targetItem = context.getTable().get(targetLocationX, targetLocationY);
		if(targetItem!=null){
			throw new IllegalStateException();
		}

		
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
		context.getTable().put(item, targetLocationX, targetLocationY);

		
	}
}
