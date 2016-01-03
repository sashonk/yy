package com.me.test.game2.item.factory;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.ItemFactory;
import com.me.test.game2.item.Mirror;
import com.me.test.game2.item.Yang;

public class MirrorFactory extends ItemFactory<Mirror>{

	@Override
	public Mirror create(int x, int y, ItemContainer context) {
		Mirror yang = new Mirror(context);
		context.getTable().put(yang, x, y);
		return yang;
	}
	


	@Override
	public int getId() {
		return 7;
	}



	@Override
	public String toolStyleName() {
		return "mirror_icon";
	}



	@Override
	public Class<Mirror> getItemClass() {
		return Mirror.class;
	}

	@Override
	public void destroy(Mirror item, ItemContainer context) {
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
	}

	
	@Override
	public void translate(Mirror item, int targetLocationX, int targetLocationY,
			ItemContainer context) {
		BaseItem targetItem = context.getTable().get(targetLocationX, targetLocationY);
		if(targetItem!=null){
			throw new IllegalStateException();
		}

		
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
		context.getTable().put(item, targetLocationX, targetLocationY);

		
	}
}

