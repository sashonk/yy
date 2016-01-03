package com.me.test.game2;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class ItemFactory<T extends BaseItem> {

	public abstract T create(int x, int y, ItemContainer context);
	
	public abstract void destroy(T item , ItemContainer context);

	public abstract Class<T> getItemClass();

	public ImageButton createTool(Skin skin){
		ImageButton btn = new ImageButton(skin, toolStyleName());
		btn.setName("factory-tool-"+getId());
		return btn;
	}
	
	public abstract String toolStyleName();
	

	public boolean isOwner(Button btn){
		return ("factory-tool-"+getId()).equals(btn.getName());
	}
	
	public abstract int getId();
	
	public abstract void translate(T item, int targetLocationX, int targetLocationY, ItemContainer context);
}
