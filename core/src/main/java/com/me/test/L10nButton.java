package com.me.test;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class L10nButton extends TextButton{
	L10nManager _l10n;
	String _key;
	public L10nButton(L10nManager mgr, String l10nKey, Skin skin, String styleName) {
		super(mgr.get(l10nKey), skin, styleName);
		_l10n = mgr;
		_key = l10nKey;
		
		_l10n.registerButton(this);
	}


	public L10nButton(L10nManager mgr,String l10nKey, Skin skin) {		
		super(mgr.get(l10nKey), skin);
		_l10n = mgr;
		_key = l10nKey;
		
		_l10n.registerButton(this);
	}
	
	public String getKey(){
		return _key;
	}


	
}
