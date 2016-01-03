package com.me.test.game2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.BaseScreen;
import com.me.test.L10nLabel;
import com.me.test.Sounds;
import com.me.test.TestGame;
import com.me.test.Util;

public class IntroScreen extends BaseMenuScreen{
	L10nLabel text;
	boolean first;
	public IntroScreen(TestGame g) {
		super(g);
		
		
		 text = new L10nLabel(g.getL10n(), "legend", g.getManager().getSkin(), "small");
		text.setAlignment(Align.center);
		getStage().addActor(text);
		Util.center(text);
		
		final ImageButton forw = new ImageButton(g.getManager().getSkin(), "navi");
		forw.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				if(first){
					String legend2= getGame().getL10n().get("legend2");
					text.getColor().a = 0;
					text.addAction(Actions.sequence(Actions.fadeIn(1)));
					text.setText(legend2);
					first = false;
					return;
				}
				
				Game2 gm = getGame().getGame2Screen();
				gm.start(1);
			//gm.read(Files.level(c));
				getGame().setScreen(gm);
				
			}
		});
		
		getStage().addActor(forw);
		forw.setX(getStage().getWidth()-forw.getWidth()-20);
		forw.setY(20);
	}

	
	@Override
	public void show(){
		super.show();
		first = true;
		
		text.getColor().a = 0;
		text.addAction(Actions.sequence(Actions.delay(1), Actions.fadeIn(1)));
		
	}
	
	@Override
	protected void background(){
		
		Texture splash = new Texture(Gdx.files.internal("data/pap2.jpg"));
		TextureRegion tr = new TextureRegion(splash, 0, 0, 1024, 768);
		Image im = new Image(tr);
		im.setFillParent(true);
		getStage().addActor(im);
	}


	@Override
	public String getName() {
		return "intro";
	}
}
