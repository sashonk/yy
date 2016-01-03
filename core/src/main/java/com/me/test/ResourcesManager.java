package com.me.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sun.font.TrueTypeFont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.StringBuilder;

public class ResourcesManager {

	
	public Music getMusic(){
		return manager.get(musicAssetName);
	}


	
	AssetManager manager;
	
	public void finishLoading(){
		manager.finishLoading();
	}
	
	public boolean update(){
		return manager.update();
	}
	
	public boolean update(int millis){
		return manager.update(millis);
	}

	public String getProgressAsString(){	
		return "Загрузка...";
		//BigDecimal bd = BigDecimal.valueOf(100* manager.getProgress()).setScale(0, RoundingMode.HALF_UP);
		//return new StringBuilder("Загрузка ").append(bd.toString()).append("%").toString();
		
		
	}
	
	public Sound getSound(String name){
		return manager.get(name);
	}
	
	public AssetManager getAssetManager(){
		return manager;
	}
	
	private static String musicAssetName = "data/music/complete.ogg";
	
	ResourcesManager() {

			manager = new AssetManager();
		//	manager.setLoader(BitmapFontBundle.class, new BitmapFontBundleLoader(new InternalFileHandleResolver()));
	}
	
	public void loadResources(){
		Gdx.app.debug(this.getClass().getName(), "begin loading resources");
	


		manager.load("data/fonts/chinaBold.fnt", BitmapFont.class);

		manager.load("data/main.atlas", TextureAtlas.class);
		manager.load("data/spriter/yin/yin.atlas", TextureAtlas.class);
		
		
		manager.load(Sounds.PAGE_SCROLL, Sound.class);
		manager.load(Sounds.MENU_BUTTON, Sound.class);
		manager.load(Sounds.YANG_FLING, Sound.class);
		
		manager.load(musicAssetName, Music.class);
		
		manager.load("data/fonts/china12.fnt", BitmapFont.class);
		manager.load("data/fonts/china24.fnt", BitmapFont.class);
		manager.load("data/fonts/china36.fnt", BitmapFont.class);
		manager.load("data/fonts/china48.fnt", BitmapFont.class);
		manager.load("data/fonts/china52.fnt", BitmapFont.class);
		manager.load("data/fonts/china52b.fnt", BitmapFont.class);
		manager.load("data/fonts/china72.fnt", BitmapFont.class);
		manager.load("data/fonts/china102.fnt", BitmapFont.class);
		manager.load("data/fonts/calibri24.fnt", BitmapFont.class);
		manager.load("data/fonts/calibri48.fnt", BitmapFont.class);
		manager.load("data/fonts/calibri36.fnt", BitmapFont.class);
		
/*		TTFLoaderParameters ttfParams = new TTFLoaderParameters();
		ttfParams.characters = DEFAULT_CHARS;
		ttfParams.flip = false;
		ttfParams.fontSizes = Collections.unmodifiableList(Arrays.asList(
					12,
					24,
					36,
					48,
					52,
					72,
					102
				
				
				));*/
		//manager.load("data/fonts/ChinaCyr.ttf", BitmapFontBundle.class, ttfParams);
		
/*		manager.load("data/fonts/wonderland.ttf", BitmapFont.class);
		
		manager.load("data/fonts/juice.ttf", BitmapFont.class);*/
	}
	
	
	
	public void init(){
		Gdx.app.debug(this.getClass().getName(), "initializing");
	

		
		skin = new Skin();
		TextureAtlas atlas = getAtlas();
		
/*///////////////////////////////////////////////
///////////	 ChinaCyr 	/////////////
////////////////////////////////////////////// */

		
//BitmapFontBundle fontBundle = manager.get("data/fonts/ChinaCyr.ttf");
		
skin.add("china12", manager.get("data/fonts/china12.fnt"));
skin.add("china24",manager.get("data/fonts/china24.fnt"));
skin.add("china36", manager.get("data/fonts/china36.fnt"));
skin.add("china48", manager.get("data/fonts/china48.fnt"));
skin.add("china52", manager.get("data/fonts/china52.fnt"));
skin.add("china52b", manager.get("data/fonts/china52b.fnt"));
skin.add("china72", manager.get("data/fonts/china72.fnt"));
skin.add("china102",manager.get("data/fonts/china102.fnt"));
skin.add("calibri24", manager.get("data/fonts/calibri24.fnt"));
skin.add("calibri48",manager.get("data/fonts/calibri48.fnt"));
skin.add("calibri36", manager.get("data/fonts/calibri36.fnt"));
/*		
try {
	Thread.sleep(10000);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		*/


		Color btnColor = Color.YELLOW;
		Color btnColorOver = Colors.SHADOW_YELLOW;
		Color labelColor = Color.BLACK;
		Color menuBtnColor = Color.BLACK;
		
		TextButtonStyle tbDefault = new TextButtonStyle();
		tbDefault.font = skin.getFont("china52");
		tbDefault.fontColor = menuBtnColor;
		tbDefault.overFontColor = btnColorOver;
		skin.add("menu", tbDefault);
		
		TextButtonStyle tbbDefault = new TextButtonStyle();
		tbbDefault.font = skin.getFont("china72");
		tbbDefault.fontColor = menuBtnColor;
		tbbDefault.overFontColor = btnColorOver;
		skin.add("menu2", tbbDefault);
/*		
		skin.add("tpad", getAtlas().findRegion("tpad2"), TextureRegion.class);
		skin.add("knob", getAtlas().findRegion("knob"), TextureRegion.class);
		
			TouchpadStyle tpStyle = new TouchpadStyle();
			tpStyle.background = skin.getDrawable("tpad");
			tpStyle.knob =skin.getDrawable("knob");
			skin.add("default", tpStyle);*/
		
		// system labelss
		{
			LabelStyle system24 = new LabelStyle();
			system24.font = skin.getFont("calibri24");
			system24.fontColor = Color.BLACK;
			skin.add("s24", system24);
			
			LabelStyle system36 = new LabelStyle();
			system36.font = skin.getFont("calibri36");
			system36.fontColor = Color.BLACK;
			skin.add("s36", system36);
			
			LabelStyle system48 = new LabelStyle();
			system48.font = skin.getFont("calibri48");
			system48.fontColor = Color.BLACK;
			skin.add("s48", system48);
			
		}
		
		
		TextureRegion wallRegion = new TextureRegion(getAtlas().findRegion("wall_texture"));
		NinePatch wallNP = new NinePatch(wallRegion,8,8,2,2 );
		skin.add("wall", wallNP);
		
		int uipad = 3;
		NinePatch ui = new NinePatch(getAtlas().findRegion("ui"), uipad,uipad,uipad,uipad);
		NinePatch ui_in = new NinePatch(getAtlas().findRegion("ui_in"), uipad,uipad,uipad,uipad);
		skin.add("ui", ui);
		skin.add("ui_in", ui_in);
		

		
		LabelStyle lsStep = new LabelStyle();
		lsStep.font  = skin.getFont("china24");
		lsStep.fontColor = Color.ORANGE;
		lsStep.background = skin.getDrawable("wall");		
		skin.add("step", lsStep);
		
		LabelStyle lsYellow = new LabelStyle();
		lsYellow.font  = skin.getFont("china36");
		lsYellow.fontColor = Color.ORANGE;
		skin.add("yellow", lsYellow);
		
		LabelStyle lsDefault = new LabelStyle();
		lsDefault.font  = skin.getFont("china36");
		lsDefault.fontColor = Color.BLACK;
		skin.add("default", lsDefault);
		
		

		{
		LabelStyle lsDigit = new LabelStyle();
		lsDigit.font  = skin.getFont("china36");
		lsDigit.background = skin.getDrawable("wall");
		lsDigit.fontColor = new Color(168f/ 255f, 210f / 255f, 127f/ 255f, 1);
		skin.add("digit", lsDigit);
		}
		
		LabelStyle lsRed= new LabelStyle();
		lsRed.font  = skin.getFont("china36");
		lsRed.fontColor = Color.RED;
		skin.add("best", lsRed);
		skin.add("red", lsRed);
		

		TextButtonStyle tbsReset = new TextButtonStyle();
		tbsReset.font = skin.getFont("china36");
		tbsReset.fontColor = btnColor;		
		tbsReset.overFontColor = btnColorOver;
		tbsReset.disabledFontColor = Color.GRAY;
		skin.add("reset", tbsReset);
		
		TextButtonStyle tbsReset2 = new TextButtonStyle();
		tbsReset2.font = skin.getFont("china36");
		tbsReset2.fontColor = Color.BLACK;
		
		tbsReset2.overFontColor = btnColorOver;
		tbsReset2.disabledFontColor = Color.GRAY;
		skin.add("reset2", tbsReset2);
		
		
		TextButtonStyle btn = new TextButtonStyle();
		btn.font = skin.getFont("china52");
		btn.fontColor = Color.BLACK;		
		btn.overFontColor = btnColorOver;
		btn.disabledFontColor = Color.GRAY;
		skin.add("btn", btn);
		
		LabelStyle lab = new LabelStyle();
		lab.font = skin.getFont("china52");
		lab.fontColor = Color.BLACK;		
		skin.add("lab", lab);
		
		
		{
			TextButtonStyle tbsGame = new TextButtonStyle();
			//	tbsReset.up = skin.getDrawable("wall");	
			tbsGame.font = skin.getFont("china36");
				tbsGame.fontColor = btnColor;
				tbsGame.up = skin.getDrawable("wall");		
				tbsGame.overFontColor = btnColorOver;
				tbsGame.disabledFontColor = Color.GRAY;
				skin.add("game", tbsGame);
		}
		
		{
			LabelStyle lsGame = new LabelStyle();
			//	tbsReset.up = skin.getDrawable("wall");	
			lsGame.font = skin.getFont("china36");
			lsGame.fontColor = btnColor;
			lsGame.background =  skin.getDrawable("wall");	
				skin.add("game", lsGame);
		}
	
		
		NinePatch frameNP = new NinePatch(getAtlas().findRegion("frame"), 2, 4, 20, 4)  ;
		skin.add("frame", frameNP);

		int fspad = 4;
		NinePatch smallframeNP = new NinePatch(getAtlas().findRegion("frame_small"), fspad,fspad , fspad, fspad)  ;
		skin.add("frame_small", smallframeNP);

		
		{
		WindowStyle wStyle = new WindowStyle();
		wStyle.background = skin.getDrawable("frame");	
		wStyle.titleFont = skin.getFont("china24");
		wStyle.titleFontColor = Color.BLACK;	
		skin.add("win", wStyle);
		}
		

		
/*		LabelStyle lsSystem = new LabelStyle();
		lsSystem.font = skin.getFont("14");
		lsSystem.fontColor = Color.BLACK;
		skin.add("default", lsSystem);*/

		
		TextButtonStyle tbSystem = new TextButtonStyle();
		tbSystem.font = skin.getFont("china12");
		tbSystem.fontColor = Color.BLACK;
		tbSystem.up = skin.getDrawable("ui");	
		tbSystem.down = skin.getDrawable("ui_in");	
		//tbSystem.checked = skin.getDrawable("ui_in");	
		skin.add("default", tbSystem);
		
		TextButtonStyle tbCheck = new TextButtonStyle();
		tbCheck.font = skin.getFont("china12");
		tbCheck.fontColor = Color.BLACK;
		tbCheck.up = skin.getDrawable("ui");	
		tbCheck.down = skin.getDrawable("ui_in");	
		tbCheck.checked = skin.getDrawable("ui_in");	
		skin.add("press", tbCheck);	
		
		
		skin.add("yin", new TextureRegion(getAtlas().findRegion("yin_icon")) );
		skin.add("yang",  new TextureRegion(getAtlas().findRegion("yang_icon")));
		skin.add("box_icon",  new TextureRegion(getAtlas().findRegion("box_icon")));
		skin.add("fall_icon",  new TextureRegion(getAtlas().findRegion("fall_icon")));
		skin.add("ground_icon",  new TextureRegion(getAtlas().findRegion("ground_icon")));
		skin.add("metal_icon",  new TextureRegion(getAtlas().findRegion("metal_icon")));
		
		skin.add("water_icon",  new TextureRegion(getAtlas().findRegion("water_icon")));
		skin.add("wall_icon",  new TextureRegion(getAtlas().findRegion("wall_icon")));
		skin.add("mirror_icon",  new TextureRegion(getAtlas().findRegion("mirror_icon")));
		
		ImageButtonStyle yinStyle = new ImageButtonStyle();
		yinStyle.imageUp = skin.getDrawable("yin");
		yinStyle.up = skin.getDrawable("ui");	
		yinStyle.checked = skin.getDrawable("ui_in");	
		skin.add("yin", yinStyle);	
		
		ImageButtonStyle yangStyle = new ImageButtonStyle();
		yangStyle.imageUp = skin.getDrawable("yang");
		yangStyle.up = skin.getDrawable("ui");	
		yangStyle.checked = skin.getDrawable("ui_in");	
		skin.add("yang", yangStyle);
		
		ImageButtonStyle boxIconStyle = new ImageButtonStyle();
		boxIconStyle.imageUp = skin.getDrawable("box_icon");
		boxIconStyle.up = skin.getDrawable("ui");	
		boxIconStyle.checked = skin.getDrawable("ui_in");	
		skin.add("box_icon", boxIconStyle);
		
		ImageButtonStyle fallIconStyle = new ImageButtonStyle();
		fallIconStyle.imageUp = skin.getDrawable("fall_icon");
		fallIconStyle.up = skin.getDrawable("ui");	
		fallIconStyle.checked = skin.getDrawable("ui_in");	
		skin.add("fall_icon", fallIconStyle);
		
		ImageButtonStyle gndIconStyle = new ImageButtonStyle();
		gndIconStyle.imageUp = skin.getDrawable("ground_icon");
		gndIconStyle.up = skin.getDrawable("ui");	
		gndIconStyle.checked = skin.getDrawable("ui_in");	
		skin.add("ground_icon", gndIconStyle);
		
		ImageButtonStyle metIconStyle = new ImageButtonStyle();
		metIconStyle.imageUp = skin.getDrawable("metal_icon");
		metIconStyle.up = skin.getDrawable("ui");	
		metIconStyle.checked = skin.getDrawable("ui_in");	
		skin.add("metal_icon", metIconStyle);
		
		ImageButtonStyle mirrorIconStyle = new ImageButtonStyle();
		mirrorIconStyle.imageUp = skin.getDrawable("mirror_icon");
		mirrorIconStyle.up = skin.getDrawable("ui");	
		mirrorIconStyle.checked = skin.getDrawable("ui_in");	
		skin.add("mirror_icon", mirrorIconStyle);
		
		ImageButtonStyle wallIconStyle = new ImageButtonStyle();
		wallIconStyle.imageUp = skin.getDrawable("wall_icon");
		wallIconStyle.up = skin.getDrawable("ui");	
		wallIconStyle.checked = skin.getDrawable("ui_in");	
		skin.add("wall_icon", wallIconStyle);
		
		ImageButtonStyle waterIconStyle = new ImageButtonStyle();
		waterIconStyle.imageUp = skin.getDrawable("water_icon");
		waterIconStyle.up = skin.getDrawable("ui");	
		waterIconStyle.checked = skin.getDrawable("ui_in");	
		skin.add("water_icon", waterIconStyle);

		

		
		{
			{
			TextureRegion tr1 =getAtlas().findRegion("choice_star1");
			skin.add("choice_star1",tr1, TextureRegion.class);
			
			TextureRegion tr2 =getAtlas().findRegion("choice_star2");
			skin.add("choice_star2",tr2, TextureRegion.class);
			
			TextureRegion tr3 =getAtlas().findRegion("choice_star3");
			skin.add("choice_star3",tr3, TextureRegion.class);

			TextureRegion tr0 =getAtlas().findRegion("choice");
			skin.add("choice",tr0, TextureRegion.class);
			
			TextureRegion choice_inactive =getAtlas().findRegion("choice_inactive");
			skin.add("choice_inactive",choice_inactive, TextureRegion.class);
		
			
			TextButtonStyle tbChoice0 = new TextButtonStyle();
			tbChoice0.disabled = skin.getDrawable("choice_inactive");
			tbChoice0.font = skin.getFont("china36");
			tbChoice0.fontColor = Color.BLACK;
			skin.add("choice", tbChoice0);
			
			ImageButtonStyle ibStyle1 = new ImageButtonStyle();
			ibStyle1.disabled = skin.getDrawable("choice_inactive");;
			ibStyle1.up = skin.getDrawable("choice_star1");
			skin.add("choice_star1", ibStyle1);
			
			ImageButtonStyle ibStyle2 = new ImageButtonStyle();
			ibStyle2.disabled = skin.getDrawable("choice_inactive");;
			ibStyle2.up = skin.getDrawable("choice_star2");
			skin.add("choice_star2", ibStyle2);
			
			ImageButtonStyle ibStyle3 = new ImageButtonStyle();
			ibStyle3.disabled = skin.getDrawable("choice_inactive");;
			ibStyle3.up = skin.getDrawable("choice_star3");
			skin.add("choice_star3", ibStyle3);
			
			ImageButtonStyle ibStyle0 = new ImageButtonStyle();
			ibStyle0.disabled = skin.getDrawable("choice_inactive");;
			ibStyle0.up = skin.getDrawable("choice");
			skin.add("choice", ibStyle0);
			}

			{
			/////////// EXTRA ////////////////
			TextureRegion tr1 =getAtlas().findRegion("choice_extra1");
			skin.add("choice_extra1",tr1, TextureRegion.class);
			
			TextureRegion tr2 =getAtlas().findRegion("choice_extra2");
			skin.add("choice_extra2",tr2, TextureRegion.class);
			
			TextureRegion tr3 =getAtlas().findRegion("choice_extra3");
			skin.add("choice_extra3",tr3, TextureRegion.class);

			TextureRegion tr0 =getAtlas().findRegion("choice_extra");
			skin.add("choice_extra",tr0, TextureRegion.class);
			
			
			ImageButtonStyle ibStyle1 = new ImageButtonStyle();
			ibStyle1.disabled = skin.getDrawable("choice_inactive");;
			ibStyle1.up = skin.getDrawable("choice_extra1");
			skin.add("choice_extra1", ibStyle1);
			
			ImageButtonStyle ibStyle2 = new ImageButtonStyle();
			ibStyle2.disabled = skin.getDrawable("choice_inactive");;
			ibStyle2.up = skin.getDrawable("choice_extra2");
			skin.add("choice_extra2", ibStyle2);
			
			ImageButtonStyle ibStyle3 = new ImageButtonStyle();
			ibStyle3.disabled = skin.getDrawable("choice_inactive");;
			ibStyle3.up = skin.getDrawable("choice_extra3");
			skin.add("choice_extra3", ibStyle3);
			
			ImageButtonStyle ibStyle0 = new ImageButtonStyle();
			ibStyle0.disabled = skin.getDrawable("choice_inactive");;
			ibStyle0.up = skin.getDrawable("choice_extra");
			skin.add("choice_extra", ibStyle0);
			
			
			}
			
		}


		
		{
		TextButtonStyle tbSimple = new TextButtonStyle();
		tbSimple.up = skin.getDrawable("ui");
		tbSimple.checked = skin.getDrawable("ui_in");
		tbSimple.font = skin.getFont("china24");
		tbSimple.fontColor = Color.BLACK;
		skin.add("small", tbSimple);
		}
		
		{
		LabelStyle tbSimple = new LabelStyle();

		tbSimple.font = skin.getFont("china24");
		tbSimple.fontColor = labelColor;
		skin.add("small", tbSimple);
		}
		
		{
		LabelStyle lsEncourage = new LabelStyle();
		//lsEncourage.
	//	BitmapFont chinaFont =  manager.get("data/fonts/china222");
		lsEncourage.font = manager.get("data/fonts/chinaBold.fnt");
		lsEncourage.fontColor = Color.WHITE;
		skin.add("encourage", lsEncourage);
		}
		
		{
		LabelStyle tbSimple = new LabelStyle();

		tbSimple.font = skin.getFont("china12");
		tbSimple.fontColor = labelColor;
		skin.add("tiny", tbSimple);
		}
		
		{
		LabelStyle tbBig = new LabelStyle();
		tbBig.font = skin.getFont("china48");
		tbBig.fontColor = labelColor;
		skin.add("big", tbBig);
		}
		
		{
			LabelStyle tbBig = new LabelStyle();
			tbBig.font = skin.getFont("china52");
			tbBig.fontColor = labelColor;
			skin.add("huge", tbBig);
		}
		
		
		{
			TextureRegion rg = new TextureRegion(getAtlas().findRegion("navi"));
			TextureRegion rg2 = new TextureRegion(getAtlas().findRegion("navi_inactive"));
			TextureRegion rg3 = new TextureRegion(getAtlas().findRegion("navi_down"));
			skin.add("navi", rg, TextureRegion.class);
			skin.add("navi_inactive", rg2, TextureRegion.class);
			skin.add("navi_down", rg3, TextureRegion.class);
			ImageButtonStyle navi = new ImageButtonStyle();
			navi.imageUp = skin.getDrawable("navi");
			navi.imageDisabled = skin.getDrawable("navi_inactive");
			navi.imageOver = skin.getDrawable("navi_down");
			navi.pressedOffsetX = 2;
			navi.pressedOffsetY = -2;

			//navi.imageDown 
		//	navi.up = skin.getDrawable("ui");	
		//	navi.down = skin.getDrawable("ui_in");	
			skin.add("navi", navi);
		}
		
		{
			TextureRegion rg = new TextureRegion(getAtlas().findRegion("navi_backi"));
			TextureRegion rg2 = new TextureRegion(getAtlas().findRegion("navi_back_inactive"));
			TextureRegion rg3 = new TextureRegion(getAtlas().findRegion("navi_back_down"));

			skin.add("navi_back", rg);
			skin.add("navi_back_inactive", rg2);
			skin.add("navi_back_down", rg3, TextureRegion.class);
			ImageButtonStyle navi = new ImageButtonStyle();
			navi.imageUp = skin.getDrawable("navi_back");
			navi.imageDisabled = skin.getDrawable("navi_back_inactive");
			navi.imageOver = skin.getDrawable("navi_back_down");
			navi.pressedOffsetX = 2;
			navi.pressedOffsetY = -2;
		//	navi.up = skin.getDrawable("ui");
			
		//	navi.down = skin.getDrawable("ui_in");	
			skin.add("navi_back", navi);
		}
		
		
		{
			

			//lstyle.
			
			NinePatch list_sel = new NinePatch(getAtlas().findRegion("list_selection"), uipad, uipad,uipad,uipad) ;
			NinePatch list_ui = new NinePatch(getAtlas().findRegion("select2"), uipad, uipad+40,uipad,uipad);
			skin.add("list_sel", list_sel);
			skin.add("list_ui", list_ui);
			
			ListStyle lstyle = new ListStyle();
			lstyle.background=skin.getDrawable("ui_in");
			lstyle.selection =skin.getDrawable("list_sel");
			lstyle.font = skin.getFont("china24");
			//ls
			//lstyle.
			
			ScrollPaneStyle scrollStyle = new ScrollPaneStyle();
			scrollStyle.background = skin.getDrawable("ui_in");
			scrollStyle.hScroll = skin.getDrawable("ui_in");
			scrollStyle.vScroll = skin.getDrawable("ui_in");
			scrollStyle.hScrollKnob = skin.getDrawable("ui_in");
			scrollStyle.vScrollKnob = skin.getDrawable("ui_in");;
			scrollStyle.corner = skin.getDrawable("ui_in");

			SelectBoxStyle selectStyle = new SelectBoxStyle();
			selectStyle.background = skin.getDrawable("list_ui");
			selectStyle.font = skin.getFont("china24");
			selectStyle.fontColor = Color.BLACK;
			selectStyle.listStyle = lstyle;
			selectStyle.scrollStyle = scrollStyle;
			//selectStyle.
	
			skin.add("default", selectStyle);
		}
		
		
		{
				ScrollPaneStyle rulesStyle = new ScrollPaneStyle();
				rulesStyle.background = skin.getDrawable("ui_in");
				rulesStyle.corner =  skin.getDrawable("ui");
				rulesStyle.vScroll = skin.getDrawable("list_sel");
				rulesStyle.vScrollKnob = skin.getDrawable("ui");
				
				skin.add("default", rulesStyle);
		
			
		}
		
		
		{
			TextureRegion knob = getAtlas().findRegion("slider_knob");
			skin.add("slider_knob", knob, TextureRegion.class);
			
			SliderStyle sliderStyle = new SliderStyle();
			sliderStyle.background = skin.getDrawable("ui_in");
			sliderStyle.knob = skin.getDrawable("slider_knob");
			
				skin.add("default-horizontal", sliderStyle);
	
		
		}
		
		
		int chpd = uipad +1;
		{
			TextureRegion checked = getAtlas().findRegion("checked");
			TextureRegion unchecked = getAtlas().findRegion("unchecked");
			NinePatch checkedNp = new NinePatch(checked, chpd, chpd, chpd, chpd);
			skin.add("checked", checkedNp);
			NinePatch uncheckedNp = new NinePatch(unchecked, chpd, chpd, chpd, chpd);
			skin.add("unchecked", uncheckedNp);
			
			CheckBoxStyle chbStyle = new CheckBoxStyle();
			chbStyle.checkboxOff = skin.getDrawable("unchecked");
			chbStyle.checkboxOn = skin.getDrawable("checked");
			chbStyle.font = skin.getFont("china36");
			chbStyle.fontColor = Color.BLACK;
			skin.add("default", chbStyle);
		
		}
		
		{
			TextFieldStyle tf = new TextFieldStyle();
			tf.background = skin.getDrawable("ui_in");
			tf.selection = skin.getDrawable("list_sel");
			//tf.
			tf.font = skin.getFont("china12");
			tf.fontColor = Color.BLACK;
			tf.messageFont = tf.font;
			tf.messageFontColor = Color.BLUE;
			skin.add("default", tf);
		}
		
		
		{
			WindowStyle winDefault = new WindowStyle();
			//winDefault.background =  atlas.findRegion("frame");
			winDefault.titleFont = skin.getFont("china12");
			winDefault.titleFontColor = Color.BLACK;
			skin.add("default", winDefault);
			
			
			ImageButtonStyle pauseStyle = new ImageButtonStyle();
			pauseStyle.up = new TextureRegionDrawable(atlas.findRegion("pause"));
			pauseStyle.over =  new TextureRegionDrawable(atlas.findRegion("pause_d"));
			skin.add("pause", pauseStyle);
			
			ImageButtonStyle replayStyle = new ImageButtonStyle();
			replayStyle.up = new TextureRegionDrawable(atlas.findRegion("repeat"));
			replayStyle.over = new TextureRegionDrawable(atlas.findRegion("repeat_d"));
			skin.add("replay", replayStyle);
			
			ImageButtonStyle footstepStyle = new ImageButtonStyle();
			footstepStyle.up = new TextureRegionDrawable(atlas.findRegion("footstep"));
			footstepStyle.over = new TextureRegionDrawable(atlas.findRegion("footstep_d"));
			skin.add("footstep", footstepStyle);
			
			ImageButtonStyle listStyle = new ImageButtonStyle();
			listStyle.up = new TextureRegionDrawable(atlas.findRegion("list"));
			listStyle.over = new TextureRegionDrawable(atlas.findRegion("list_d"));
			skin.add("list", listStyle);
			
			ImageButtonStyle playStyle = new ImageButtonStyle();
			playStyle.up = new TextureRegionDrawable(atlas.findRegion("play"));
			playStyle.over = new TextureRegionDrawable(atlas.findRegion("play_d"));
			skin.add("play", playStyle);
			
			ImageButtonStyle forwardStyle = new ImageButtonStyle();
			forwardStyle.up = new TextureRegionDrawable(atlas.findRegion("forward"));
			forwardStyle.over = new TextureRegionDrawable(atlas.findRegion("forward_d"));
			skin.add("forward", forwardStyle);
			
			ImageButtonStyle backwardStyle = new ImageButtonStyle();
			backwardStyle.up = new TextureRegionDrawable(atlas.findRegion("backward"));
			backwardStyle.over = new TextureRegionDrawable(atlas.findRegion("backward_d"));
			skin.add("backward", backwardStyle);
			
			ImageButtonStyle returnStyle = new ImageButtonStyle();
			returnStyle.up = new TextureRegionDrawable(atlas.findRegion("return"));
			returnStyle.over = new TextureRegionDrawable(atlas.findRegion("return_d"));
			skin.add("return", returnStyle);
			
			ImageButtonStyle musicStyle = new ImageButtonStyle();
			musicStyle.up = new TextureRegionDrawable(atlas.findRegion("music"));
			musicStyle.over = new TextureRegionDrawable(atlas.findRegion("music_d"));
			skin.add("music", musicStyle);
			
			ImageButtonStyle musicOffStyle = new ImageButtonStyle();
			musicOffStyle.up = new TextureRegionDrawable(atlas.findRegion("music_off"));
			skin.add("music_off", musicOffStyle);
			
			
			ImageButtonStyle soundStyle = new ImageButtonStyle();
			soundStyle.up = new TextureRegionDrawable(atlas.findRegion("sound"));
			soundStyle.over = new TextureRegionDrawable(atlas.findRegion("sound_d"));
			skin.add("sound", soundStyle);
			
			ImageButtonStyle soundOffStyle = new ImageButtonStyle();
			soundOffStyle.up = new TextureRegionDrawable(atlas.findRegion("sound_off"));
			skin.add("sound_off", soundOffStyle);
			
		}
		
		
		skin.add("bubble", atlas.findRegion("bubble"), TextureRegion.class);
		

		{
			skin.add("flag_ru", getAtlas().findRegion("flag_ru"), TextureRegion.class) ;
			skin.add("flag_en", getAtlas().findRegion("flag_en"), TextureRegion.class) ;
			skin.add("flag_de", getAtlas().findRegion("flag_de"), TextureRegion.class) ;

			
			ImageButtonStyle flag_enStyle = new ImageButtonStyle();
			flag_enStyle.up = skin.getDrawable("flag_en");
			flag_enStyle.pressedOffsetX = 2;
			flag_enStyle.pressedOffsetY = -2;
			skin.add("flag_en", flag_enStyle);
			
			ImageButtonStyle flag_ruStyle = new ImageButtonStyle();
			flag_ruStyle.up = skin.getDrawable("flag_ru");
			flag_ruStyle.pressedOffsetX = 2;
			flag_ruStyle.pressedOffsetY = -2;
			skin.add("flag_ru", flag_ruStyle);
			
			ImageButtonStyle flag_deStyle = new ImageButtonStyle();
			flag_deStyle.up = skin.getDrawable("flag_de");
			flag_deStyle.pressedOffsetX = 2;
			flag_deStyle.pressedOffsetY = -2;
			skin.add("flag_de", flag_deStyle);
		}
		
		// facebook, twitter
		{
			TextureRegion twReg= getAtlas().findRegion("twitter");
			skin.add("twitter", twReg, TextureRegion.class);
			ImageButtonStyle tw = new ImageButtonStyle();
			tw.up = skin.getDrawable("twitter");
			tw.pressedOffsetX = 2;
			tw.pressedOffsetY = -2;
			skin.add("twitter", tw);
			
			
		TextureRegion fbReg= getAtlas().findRegion("fb");
		skin.add("fb", fbReg, TextureRegion.class);
		ImageButtonStyle fb = new ImageButtonStyle();
		fb.up = skin.getDrawable("fb");
		fb.pressedOffsetX = 2;
		fb.pressedOffsetY = -2;
		skin.add("fb", fb);
		
		TextureRegion likeReg= getAtlas().findRegion("like");
		skin.add("like", likeReg, TextureRegion.class);
		ImageButtonStyle like = new ImageButtonStyle();
		like.up = skin.getDrawable("like");
		like.pressedOffsetX = 2;
		like.pressedOffsetY = -2;
		skin.add("like", like);
		}
		
		{
			TextureRegion buy= getAtlas().findRegion("buy");
			int pd= 14;
			NinePatch buyNP = new NinePatch(buy,pd,pd,pd,pd );
			skin.add("buy", buyNP, NinePatch.class);
			
			//buy button
			TextButtonStyle bbStyle = new TextButtonStyle();
			bbStyle.up = skin.getDrawable("buy");
			bbStyle.pressedOffsetX = 2;
			bbStyle.pressedOffsetY = -2;
	
			bbStyle.font = skin.getFont("calibri24");
			bbStyle.fontColor = Color.BLACK;
			skin.add("buy", bbStyle);
			
			
		}
		
		
		{
			
			
			TextureRegion cinema= getAtlas().findRegion("cinema_clapper");
			skin.add("cinema", cinema, TextureRegion.class);
			
			//cinema button
			ImageButtonStyle cinemaStyle = new ImageButtonStyle();
			cinemaStyle.up = skin.getDrawable("cinema");
			cinemaStyle.pressedOffsetX = 2;
			cinemaStyle.pressedOffsetY = -2;
			skin.add("cinema", cinemaStyle);
			
			
		}
		
		{
			//button
			TextureRegion button= getAtlas().findRegion("button");
			NinePatch buttonNP = new NinePatch(button,4,4,4,4 );
			skin.add("button", buttonNP);
			
			TextButtonStyle buttonStyle = new TextButtonStyle();
			buttonStyle.up = skin.getDrawable("button");
			buttonStyle.pressedOffsetX = 2;
			buttonStyle.pressedOffsetY = -2;
			buttonStyle.font = skin.getFont("china52");
			buttonStyle.fontColor =Color.WHITE;

			skin.add("button", buttonStyle);	
			
		}

		{
			
			TextureRegion shopping= getAtlas().findRegion("shopping");
			skin.add("shopping", shopping, TextureRegion.class);
			
			//shop button
			ImageButtonStyle shopStyle = new ImageButtonStyle();
			shopStyle.up = skin.getDrawable("shopping");
			shopStyle.pressedOffsetX = 2;
			shopStyle.pressedOffsetY = -2;
			skin.add("shopping", shopStyle);
			
			
		}
		
		{
			
			TextureRegion shopping= getAtlas().findRegion("yy");
			skin.add("yy", shopping, TextureRegion.class);
			
			//yy button
			ImageButtonStyle shopStyle = new ImageButtonStyle();
			shopStyle.up = skin.getDrawable("yy");
			shopStyle.pressedOffsetX = 2;
			shopStyle.pressedOffsetY = -2;
			skin.add("yy", shopStyle);
			
			
		}
		
		
	}
	

	
	
	public Skin getSkin(){
		return skin;
	}
	
	public TextureAtlas getAtlas(){
		return manager.get("data/main.atlas");
	}

	public TextureAtlas getYinAtlas(){
		return manager.get("data/spriter/yin/yin.atlas");
	}

	
	


	
	
	
	
	private Skin skin;


	private Map<Color, Texture> unmanaged = new HashMap<Color, Texture>();
	private Map<Integer, Map<Color, Texture>> data = new HashMap<Integer, Map<Color,Texture>>();
	private void disposeInternal(){
		

		

		if(skin!=null)
		skin.dispose();
		//nickolLetter.dispose();
		//instructScreen.dispose();
		
		Iterator<Map<Color, Texture>> rootIter = data.values().iterator();
		while(rootIter.hasNext()){
			Map<Color, Texture> map = rootIter.next();
			Iterator<Texture> texIter = map.values().iterator();
			while(texIter.hasNext()){
				Texture tx = texIter.next();
				tx.dispose();
				texIter.remove();
			}
			
			rootIter.remove();
		}

		Iterator<Texture> texIter = unmanaged.values().iterator();
		while(texIter.hasNext()){
			Texture tx = texIter.next();
			tx.dispose();
			texIter.remove();
		}
		
/*		for(Sound snd : sounds.values()){
			snd.dispose();
		}*/

		if(manager!=null){
			manager.dispose();

		}
	}
	
	
	public Texture getEmptyCircle(Color c, float D){
		int txsize = 2;
		int pow = 1;
		while(txsize<D){
			txsize = (int) Math.pow(2, pow++);
		}
		
		Map<Color, Texture> colorMap =data.get(txsize);
		if(colorMap == null){
			colorMap = new HashMap<Color, Texture>();
			data.put(Integer.valueOf(txsize), colorMap);
		}
		
		Texture tx = colorMap.get(c);
		if(tx==null){
			Pixmap pmap = new Pixmap(txsize, txsize, Format.RGBA8888);
			pmap.setColor(c);
			pmap.drawCircle(txsize/2-1, txsize/2-1, txsize/2-1);
			tx = new Texture(pmap);
			colorMap.put(c, tx);
		}
		
		return tx;

	}
	
	public Texture getCircle(Color c, float D){
		int txsize = 2;
		int pow = 1;
		while(txsize<D){
			txsize = (int) Math.pow(2, pow++);
		}
		
		Map<Color, Texture> colorMap =data.get(txsize);
		if(colorMap == null){
			colorMap = new HashMap<Color, Texture>();
			data.put(Integer.valueOf(txsize), colorMap);
		}
		
		Texture tx = colorMap.get(c);
		if(tx==null){
			Pixmap pmap = new Pixmap(txsize, txsize, Format.RGBA8888);
			pmap.setColor(c);
			pmap.fillCircle(txsize/2-1, txsize/2-1, txsize/2-1);
			tx = new Texture(pmap);
			colorMap.put(c, tx);
		}
		
		return tx;

	}
	
	public Texture getUnmanaged(Color c){
		Texture tex = unmanaged.get(c);
		if(tex == null){
			Pixmap pmap = new Pixmap(1, 1, Format.RGBA8888);
			pmap.setColor(c);
			pmap.fill();
			tex = new Texture(pmap);
			unmanaged.put(c, tex);
		}
		
		return tex;
	}
	
	public   void manage(){

		

		Iterator<Map<Color, Texture>> rootIter =data.values().iterator();
		while(rootIter.hasNext()){
			Map<Color, Texture> map = rootIter.next();
			Iterator<Texture> texIter = map.values().iterator();
			while(texIter.hasNext()){
				Texture tx = texIter.next();
				tx.dispose();
				texIter.remove();
			}
			
			rootIter.remove();
		}

		Iterator<Texture> texIter = unmanaged.values().iterator();
		while(texIter.hasNext()){
			Texture tx = texIter.next();
			tx.dispose();
			texIter.remove();
		}

	}
	
	

	public void dispose(){

		disposeInternal();
	}
}
