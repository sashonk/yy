package com.me.test.game2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.L10nButton;
import com.me.test.L10nLabel;
import com.me.test.TestGame;
import com.me.test.Util;

public class CreditsScreen extends BaseMenuScreen{


	public boolean hasBackButton(){
		return true;
	}
	
	public CreditsScreen(TestGame g) {
		super(g);
		
		

		
		
		Skin s = g.getManager().getSkin();
		Table content = new Table();
		content.defaults().pad(15);
		Label devLabel = new L10nLabel(game.getL10n(), "credits1",s);
		content.add(devLabel).row();
		Label advLabel = new L10nLabel(game.getL10n(), "credits2",s);
		content.add(advLabel).row();
		Label musicLabel = new L10nLabel(game.getL10n(), "credits3",s);
		content.add(musicLabel).row();
		Label soundLabel = new L10nLabel(game.getL10n(), "credits4",s);
		content.add(soundLabel).row();
		Label label5 = new L10nLabel(game.getL10n(), "credits5",s);
		content.add(label5).row();
		content.pack();
		getStage().addActor(content);
		Util.center(content);
	}

	@Override
	public String getName() {
		return "credits";
	}

	
	@Override
	protected void background(){
		
		Texture splash = new Texture(Gdx.files.internal("data/pap2.jpg"));
		TextureRegion tr = new TextureRegion(splash, 0, 0, 1024, 768);
		Image im = new Image(tr);
		im.setFillParent(true);
		getStage().addActor(im);
	}
}
