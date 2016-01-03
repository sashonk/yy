
package com.me.test;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.game2.BaseMenuScreen;

public class SettingsScreen2 extends BaseMenuScreen{

	   ImageButton sound ;
	   ImageButton music;
	@Override
	protected void background(){
		
		Texture splash = new Texture(Gdx.files.internal("data/pap2.jpg"));
		TextureRegion tr = new TextureRegion(splash, 0, 0, 1024, 768);
		Image im = new Image(tr);
		im.setFillParent(true);
		getStage().addActor(im);
	}
	
	public SettingsScreen2(final TestGame g) {
		super(g);
		
		final Skin skin = g.getManager().getSkin();
		final L10nManager l10n = g.getL10n();
		
	
		
		Table table = new Table();
		table.defaults().pad(15);
		
		sound = new ImageButton(skin, "sound");
	 	music = new ImageButton(skin, "music");
	 	
	 	
		float coef = 3f;
		
		String style = "huge";
		Label m = new L10nLabel(l10n, "music", skin , style);
		Label s = new L10nLabel(l10n, "sound", skin, style);
		Label l = new L10nLabel(l10n, "language", skin, style);
	//	final L10nLabel lang = new L10nLabel(l10n, "lang", skin, "big");
/*		l.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				String newLanguage = "en".equals(l10n.getLanguage()) ? "ru" : "en";				
				l10n.setLanguage(newLanguage);
				g.getSettings().setLanguage(newLanguage);
				
			}
		});*/
		
		
		final List<LangDefinition> definitions = Arrays.asList(
				new LangDefinition("ru", "русский", null),
				new LangDefinition("en", "english", null)/*,
				new LangDefinition("de", "deutsch")*/);
		
		l.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				LangDialog langDialog = new LangDialog(getGame(),definitions);
				langDialog.show(getStage());
			}
		});
		

		
		table.add(s);
		table.add(sound).width(242/coef).height(222/coef).row();
		table.add(m);
		table.add(music).width(184/coef).height(232/coef).row();
		table.add(l).colspan(2);
		//table.add(lang).colspan(2);
		table.pack();
		
		getStage().addActor(table);
		Util.center(table);
		
		
		
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
	
	@Override
	public void show(){
		super.show();
		Skin skin = getGame().getManager().getSkin();
		Settings settings = getGame().getSettings();
		String styleNameSound = settings.isSoundEnabled() ? "sound" : "sound_off";
		String styleNameMusic = settings.isMusicEnabled() ? "music" : "music_off";
		sound.setStyle(skin.get(styleNameSound, ImageButtonStyle.class));
		music.setStyle(skin.get(styleNameMusic, ImageButtonStyle.class));
	}

	@Override
	public String getName() {
		return "settings";
	}

	public boolean hasBackButton(){
		return true;
	}
	
	public static class LangRenderer{
		String _key;
		String _translation;
		public LangRenderer(TestGame game, String key){
			_key = key;
			_translation = game.getL10n().get(_key, _key);
		}
		
		public String toString(){
			return _translation;
		}
		
		public String getKey(){
			return _key;
		}
	}
}
