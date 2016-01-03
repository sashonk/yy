package com.me.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SplashScreen extends BaseScreen{

	BitmapFont splashFont;
	ResourcesManager _manager;
	
	public SplashScreen(TestGame game) {
		super(game);
		
		
		Texture splash = new Texture(Gdx.files.internal("data/pap.jpg"));
		TextureRegion tr = new TextureRegion(splash, 0, 0, 1024, 768);
		Image im = new Image(tr);
		im.setFillParent(true);
		getStage().addActor(im);
		
		
		TextureAtlas splashAtlas = new TextureAtlas(Gdx.files.internal("data/loading.pack"));
		TextureRegion loading = splashAtlas.findRegion("loading_"+game.getL10n().getLanguage());
		Image loading_im = new Image(loading);
		loading_im.setSize(200, 40);
		getStage().addActor(loading_im);
		Util.center(loading_im);
		loading_im.setY(20);
		
		
		 
		 _manager = new ResourcesManager();
	
		 
		 _manager.loadResources();
	}


	
	@Override
	public void render(float delta) {
		
		if(!_manager.update()){
			super.render(delta);
		}
		else{
			_manager.finishLoading();
			
			_manager.init();
			
			
			 getGame().setManager(_manager);
			 
				TestGame tg = getGame();
				tg.createScreens();
				tg.setScreen(tg.getMenuScreen());
		}

	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "splash";
	}
}
