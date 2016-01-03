package com.me.test.game2.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;

public class Wall extends BaseItem{

	public Wall(ItemContainer game) {
		super(game);
	
		TextureRegion region = new TextureRegion(game.getGame().getManager().getAtlas().findRegion("wall"));
		//Image image = new Image(region);
		//addActor(image);

		//image.setSize(getWidth(), getHeight());
		setDrawable(new TextureRegionDrawable(region));
	}




}
