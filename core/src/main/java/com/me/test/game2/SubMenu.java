package com.me.test.game2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.me.test.BaseScreen;
import com.me.test.L10nButton;
import com.me.test.L10nLabel;
import com.me.test.Session;
import com.me.test.Settings;
import com.me.test.TestGame;
import com.me.test.Util;
import com.me.test.game2.Game2.ResetCallback;
 
public class SubMenu extends BaseScreen {
	  Texture fadeTex ;
	  
	  Image screen;
	  Image fade;
	  
	  Window toolBar;
	  Window toolBarLeft;
	  ImageButton walkthrough;
	   ImageButton sound ;
	   ImageButton music;
		Label ownedYangsLabel;

	  static final float BAR_HEIGHT = 740;
	  static final float BAR_PAD_TOP = 30;
	  static final float BAR_CELL_SPACE = 15;
	  
	public SubMenu(TestGame g) {
		super(g);
		Stage s = getStage();
		

		
	
		
         screen= new Image(); 
        screen.setBounds(0, 0, s.getWidth(),s.getHeight());
        s.addActor(screen);

		Pixmap pmap = new Pixmap(1, 1, Format.RGBA8888);
		pmap.setColor(Color.BLACK.cpy());
		pmap.fill();
		
		 fadeTex = new Texture(pmap);		
		 fade = new Image(fadeTex );
		fade.setBounds(0, 0, s.getWidth(),s.getHeight());
		s.addActor(fade);
		fade.getColor().a= 0.5f;
		
	
		createRightBar();
		createLeftBar();
		
		ownedYangsLabel = yangPanel(getStage().getRoot(), g);
	}

	@Override
	public String getName() {
		return "submenu";
	}
	
	void createLeftBar(){
		TestGame g = getGame();
		final Skin skin = g.getManager().getSkin();
		Stage s = getStage();
			
		
		toolBarLeft = new Window("", g.getManager().getSkin());
		toolBarLeft.defaults().width(100).height(100).pad(BAR_CELL_SPACE).padLeft(50);
	//	toolBarLeft.padTop(50);
		
		toolBarLeft.setBackground("frame");
		
		sound = new ImageButton(skin, "sound");
	 	music = new ImageButton(skin, "music");

		Table t = new Table();
		t.padTop(BAR_PAD_TOP);
		t.defaults().width(100).height(100).pad(BAR_CELL_SPACE).padLeft(50);
		
		float coef = 2.5f;
		t.add(sound).width(242/coef).height(222/coef) .row();
		t.add(music).width(184/coef).height(232/coef).row();
		t.pack();
		//toolBarLeft.add(sound).width(75).height(90) .row();
		///toolBarLeft.add(music).width(90).height(90) .spaceBottom(430).row();
		toolBarLeft.addActor(t);
		
		toolBarLeft.pack();
		toolBarLeft.setHeight(BAR_HEIGHT);
		t.setY(toolBarLeft.getHeight() - t.getHeight());
		
		s.addActor(toolBarLeft);
		
		toolBarLeft.setWidth(160);
		toolBarLeft.setY(10);
	//	toolBar.setX(s.getWidth()-toolBar.getWidth());
		toolBarLeft.setKeepWithinStage(false);
		toolBarLeft.setModal(false);
		toolBarLeft.setMovable(false);
		
		
		sound.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Settings settings = getGame() .getSettings();
				
				settings.setSoundEnabled(!settings.isSoundEnabled());
				settings.write();
				
				String styleName = settings.isSoundEnabled() ? "sound" : "sound_off";
				
				sound.setStyle(skin.get(styleName, ImageButtonStyle.class));
			}
			
		});
		
		music.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Settings settings = getGame() .getSettings();
				
				settings.setMusicEnabled(!settings.isMusicEnabled());
				settings.write();
				
				String styleName = settings.isMusicEnabled() ? "music" : "music_off";
				
				music.setStyle(skin.get(styleName, ImageButtonStyle.class));
			}
			
		});
		
		

	}
	
	void createRightBar(){
		TestGame g = getGame();
		Skin skin = g.getManager().getSkin();
		Stage s = getStage();
			
		
		toolBar = new Window("", g.getManager().getSkin());
		toolBar.defaults().width(100).height(100).pad(BAR_CELL_SPACE).padRight(50);
		
		
		toolBar.setBackground("frame");
		
		ImageButton play = new ImageButton(skin, "play");
		ImageButton replay = new ImageButton(skin, "replay");
		 walkthrough = new ImageButton(skin, "footstep");
		ImageButton list = new ImageButton(skin, "list");
		
		Table t = new Table();
		t.padTop(BAR_PAD_TOP);
		t.defaults().width(100).height(100).pad(BAR_CELL_SPACE);
		
		float coef = 2.5f;
		t.add(play).width(75).height(90) .row();
		t.add(replay).width(90).height(90) .row();
		t.add(walkthrough).width(80).height(100) .spaceBottom(230) .row();
		t.pack();
		//t.add(list).width(100).height(80) .row();
		toolBar.addActor(t);
		toolBar.pack();
		
		toolBar.setHeight(BAR_HEIGHT);
		t.setY(toolBar.getHeight() - t.getHeight());

		s.addActor(toolBar);
		
		toolBar.setWidth(160);
		toolBar.setY(10);
	//	toolBar.setX(s.getWidth()-toolBar.getWidth());
		toolBar.setKeepWithinStage(false);
		toolBar.setModal(false);
		toolBar.setMovable(false);
		
		list.setSize(100, 80);
		toolBar.addActor(list);
		list.setPosition(BAR_CELL_SPACE, BAR_CELL_SPACE);
		
		
		play.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				getGame().setScreen(getGame().getGame2Screen());
			}
			
		});
		
		replay.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				final Game2 gm = getGame().getGame2Screen();
				gm.reset(false, new ResetCallback(){

					@Override
					public void onResetComplete() {
						if(gm.level>0){
							gm.start(gm.level);
						}
						
					}});
				
				getGame().setScreen(gm);
			}
			
		});
		
		
		list.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				if(getGame().getSession().getLastLevel()>60){
					getGame().setScreen(getGame().getChooseLevelScreen2());
				}
				else{
					getGame().setScreen(getGame().getChooseLevelScreen());
				}
			}
			
		});
		


		
		walkthrough.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				
				Scripts.turnonWalkthroughDialog(SubMenu.this);
		/*		Dialog confirmationWindow = createConfirmationWindow();
				confirmationWindow.show(getStage());
				Util.center(confirmationWindow);*/

			
				//Gdx.input.
				
			}
			
		});
	}
	
	Dialog createConfirmationWindow(){
		Skin skin = getGame().getManager().getSkin();
				
		
		final Dialog window = new Dialog("", skin,"win");
		window.getButtonTable().defaults().pad(20).padLeft(40).padRight(40);
		window.setMovable(false);
		window.setModal(true);
	
		//window.setBackground("frame");
		Label confirmation = new L10nLabel(getGame().getL10n(), "sure_want_use_tips", skin);
		confirmation.setAlignment(Align.center);
		window.text(confirmation);
		
		TextButton yes = new L10nButton(getGame().getL10n(), "yes", skin, "btn");
		window.button(yes);
		
		
		TextButton no = new L10nButton(getGame().getL10n(), "no", skin, "btn");
		window.button(no);
	//	window.pack();
		
		//stage.addActor(window);
		
		
		
		
		yes.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				window.remove();
				
				Game2 gm = getGame().getGame2Screen();
				gm.enableTips();
				getGame().setScreen(gm);
			}
			
		});
		
		
		no.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				window.hide();
			}
			
		});
	
		return window;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		screen.setDrawable(new TextureRegionDrawable(ScreenUtils.getFrameBufferTexture(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
		
		toolBar.setX(getStage().getWidth());
		toolBar.addAction(Actions.moveBy(-toolBar.getWidth()+30, 0, 0.5f, Interpolation.sineOut));
		
		toolBarLeft.setX(-toolBarLeft.getWidth());
		toolBarLeft.addAction(Actions.moveBy(toolBarLeft.getWidth()-30, 0, 0.5f, Interpolation.sineOut));
		
		if(getGame().getGame2Screen().level==81){
			walkthrough.setVisible(false);
		}
		else{
			walkthrough.setVisible(true);
		}
		
		Skin skin = getGame().getManager().getSkin();
		Settings settings = getGame().getSettings();
		String styleNameSound = settings.isSoundEnabled() ? "sound" : "sound_off";
		String styleNameMusic = settings.isMusicEnabled() ? "music" : "music_off";
		sound.setStyle(skin.get(styleNameSound, ImageButtonStyle.class));
		music.setStyle(skin.get(styleNameMusic, ImageButtonStyle.class));
		//confirmationWindow.setVisible(false);
		
		//getGame().getNative().showAdd(101, true);
		//getGame().getNative().showAdd(102, true);
		
		update();
	}
	
	@Override
	public void hide() {
	//	getGame().getNative().showAdd(101, false);
	//	getGame().getNative().showAdd(102, false);
	}
	
	@Override
	public void dispose(){
		if(fadeTex!=null){
			fadeTex.dispose();
			fadeTex = null;
		}
	}

	@Override
	public void update() {
		super.update();
		ownedYangsLabel.setText(Integer.toString(getGame().getSession().getOwnerYangs()));
	
		
	}

}
