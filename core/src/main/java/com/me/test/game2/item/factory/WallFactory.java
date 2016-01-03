package com.me.test.game2.item.factory;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;
import com.me.test.game2.ItemFactory;
import com.me.test.game2.item.Fall;
import com.me.test.game2.item.Wall;

public class WallFactory extends ItemFactory<Wall>{

	@Override
	public Wall create(int x, int y, ItemContainer context) {
		Wall wall = new Wall(context);
		context.getTable().put(wall, x, y);
		return wall;
	}



	@Override
	public int getId() {
		return 3;
	}

	@Override
	public String toolStyleName() {
		return "wall_icon";
	}



	@Override
	public Class<Wall> getItemClass() {
		return Wall.class;
	}

	@Override
	public void destroy(Wall item, ItemContainer context) {
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
	}
	@Override
	public void translate(Wall item, int targetLocationX, int targetLocationY,
			ItemContainer context) {
		BaseItem targetItem = context.getTable().get(targetLocationX, targetLocationY);
		if(targetItem!=null){
			throw new IllegalStateException();
		}

		
		context.getTable().put(null, item.getLocationX(), item.getLocationY());
		context.getTable().put(item, targetLocationX, targetLocationY);

		
	}
}
