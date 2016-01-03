package com.me.test;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LangDialog extends Dialog{
	


	
	public LangDialog(final IGame igame, List<LangDefinition> definitions) {
		super("", igame.getManager().getSkin(), "win");
		Skin s = igame.getManager().getSkin();
		
		final Dialog langDialog = this;//new Dialog("",s, "default");
		langDialog.padTop(15);
		//langDialog.setBackground("dialog");
		
		for(final LangDefinition definition : definitions){
			Table lang_en = new Table();				
			Button en = new ImageButton(s, "flag_"+definition.getLocale());
			lang_en.add(en).row();
			Label text_en = new Label(definition.getTitle(), s, "small");
			lang_en.add(text_en);
			lang_en.pack();
			
			langDialog.getContentTable().add(lang_en);
			
			lang_en.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {

					String lang =  igame.getL10n().getLanguage();
					if(!definition.getLocale().equals(lang)){
						igame.getL10n().setLanguage(definition.getLocale());
					}
					langDialog.hide();
				
					Settings s = igame.getSettings();
					s.setLanguage(definition.getLocale());
					s.write();
				}
			});
			
		}
		

		
		Button close = new L10nButton(igame.getL10n(), "cancel", s, "btn");
		langDialog.button(close);
		//langDialog.show(getStage());
		
		
	
	
	}

	

}
