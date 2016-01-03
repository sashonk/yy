package com.me.test.game2.item.factory;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.ItemFactory;
import com.me.test.game2.item.Water;
import com.me.test.game2.item.Yin;

public class YinFactory extends ItemFactory<Yin>{

	@Override
	public Yin create(int x, int y, ItemContainer context) {
		Yin yin = new Yin(context);
		context.getTable().putSpot(yin, x, y);
		
		
		return yin;
	}



	@Override
	public int getId() {
		return 2;
	}

	@Override
	public String toolStyleName() {
		return "yin";
	}



	@Override
	public Class<Yin> getItemClass() {
		return Yin.class;
	}


	@Override
	public void destroy(Yin item, ItemContainer context) {
		context.getTable().putSpot(null, item.getLocationX(), item.getLocationY());
	}
	
	@Override
	public void translate(Yin item, int targetLocationX, int targetLocationY,
			ItemContainer context) {
		BaseItem targetItem = context.getTable().getSpot(targetLocationX, targetLocationY);
		if(targetItem!=null){
			throw new IllegalStateException();
		}

		
		context.getTable().putSpot(null, item.getLocationX(), item.getLocationY());
		context.getTable().putSpot(item, targetLocationX, targetLocationY);

		
	}
}
