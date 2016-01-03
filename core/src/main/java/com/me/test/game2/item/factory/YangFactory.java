package com.me.test.game2.item.factory;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.ItemFactory;
import com.me.test.game2.item.Yang;
import com.me.test.game2.item.Yin;

public class YangFactory extends ItemFactory<Yang>{

	@Override
	public Yang create(int x, int y, ItemContainer context) {
		Yang yang = new Yang(context);
		context.getTable().put(yang, x, y);
		return yang;
	}
	


	@Override
	public int getId() {
		return 1;
	}



	@Override
	public String toolStyleName() {
		return "yang";
	}



	@Override
	public Class<Yang> getItemClass() {
		return Yang.class;
	}

	@Override
	public void destroy(Yang item, ItemContainer context) {
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
	}

	
	@Override
	public void translate(Yang item, int targetLocationX, int targetLocationY,
			ItemContainer context) {
		BaseItem targetItem = context.getTable().get(targetLocationX, targetLocationY);
		if(targetItem!=null){
			throw new IllegalStateException();
		}

		
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
		context.getTable().put(item, targetLocationX, targetLocationY);

		
	}
}

