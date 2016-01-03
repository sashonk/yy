package com.me.test;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.test.LangDefinition.CardinalDeclention;

public class L10nLabel extends Label{
	L10nManager _l10n;
	String _key;
	Object[] _params;
	boolean _withCardinalDeclension;
	
	public Object[] getParams(){
		return _params;
	}
	
	public L10nLabel(L10nManager mgr, String l10nKey, Skin skin, String styleName) {
		super(mgr.get(l10nKey), skin, styleName);
		// TODO Auto-generated constructor stub
		_l10n = mgr;
		_key = l10nKey;
		
		_l10n.registerLabel(this);
	}
	
	public L10nLabel(L10nManager mgr, String l10nKey, Skin skin, String styleName, boolean withCardinalDeclension,final  Object...params) {	
		super(withCardinalDeclension ? "" : Util.replace(mgr.get(l10nKey), params), skin, styleName);
		


		// TODO Auto-generated constructor stub
		_l10n = mgr;
		_key = l10nKey;
		_params = params;
		_withCardinalDeclension = withCardinalDeclension;
		
		if(withCardinalDeclension){
			update(params);
		}
		
		_l10n.registerLabel(this);
	}
	
	public void setKey(String key){
		_key = key;
		if(_params!=null){
		setText(Util.replace(_l10n.get(_key), _params));
		}
		else{
			setText(_l10n.get(_key));
		}
		pack();
	}
	
	public void update( Object...params){
		_params = params;
		String key = _key;
		if(_withCardinalDeclension){
			CardinalDeclention declension =  _l10n.getDifinitions().get(_l10n.getLanguage()).getCardinalDeclension();
			if(declension!=null){
				Integer number = (Integer) params[0];
				key = declension.getDeclinedForm(_key, number);
			}
		}
		
		setText(Util.replace(_l10n.get(key), _params));
		pack();
	}
	



	public L10nLabel(L10nManager mgr,String l10nKey, Skin skin) {		
		super(mgr.get(l10nKey), skin);
		// TODO Auto-generated constructor stub
		_l10n = mgr;
		_key = l10nKey;
		
		_l10n.registerLabel(this);
	}
	
	public String getKey(){
		return _key;
	}
	
}
