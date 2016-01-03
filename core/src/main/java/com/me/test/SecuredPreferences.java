package com.me.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Base64Coder;

public class SecuredPreferences implements Preferences{
	
	private Preferences prefs;
	private TEA tea;
	
	public SecuredPreferences(Preferences p) {
		prefs = p;		
		tea = new TEA(new byte[]{12,53,23,53,12,65,87,54,23,111,42,12,54,34,66,43});	
	}
	
	private String getTransformedValue(String value){
		if(Util.isBlank(value)){
			return null;
		}
		
		try{
		 byte[] enc = tea.encrypt(value.getBytes("utf-8"));
		 return new String(Base64Coder.encode(enc));
		}
		catch(Throwable t){
			Gdx.app.error(TestGame.class.getName(), "getTransformedValue exception", t);
			return null;
		}
		
	}
	
	private String getPlainValue(String transformed){
		if(Util.isBlank(transformed)){
			return null;
		}
		

		try{
				byte[] enc = Base64Coder.decode(transformed);
				byte[] plain =  tea.decrypt(enc);
				return new String(plain, "utf-8");
			
			}
			catch(Throwable t){
				Gdx.app.error(TestGame.class.getName(), "getPlainValue exception", t);
				return null;
			}
	}
	

	@Override
	public void clear() {
		prefs.clear();
		
	}

	@Override
	public boolean contains(String key) {
		
		return prefs.contains(getTransformedValue(key));
	}

	@Override
	public void flush() {
		prefs.flush();
		
	}

	@Override
	public Map<String, ?> get() {
		Map<String, ?> mm = prefs.get();
		Map<String, Object> plain = new HashMap<String, Object>();
		for(String tkey : mm.keySet()){
			String tval = (String) mm.get(tkey);
			String val = getPlainValue(tval);						
			String key = getPlainValue(tkey);
			
			plain.put(key, val);
			
		}
		
		return plain;
	}

	@Override
	public boolean getBoolean(String key) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		String val = getPlainValue(tval);
		return val!=null ? Boolean.parseBoolean(val) : false;
	}

	@Override
	public boolean getBoolean(String key, boolean def) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		String val =  getPlainValue(tval);
		return val!=null ? Boolean.parseBoolean(val) : def;
	}

	@Override
	public float getFloat(String key) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		String val = getPlainValue(tval);
		return val!=null ? Float.parseFloat(val) : 0;
	}

	@Override
	public float getFloat(String key, float def) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		String val =  getPlainValue(tval);
		return val!=null ? Float.parseFloat(val) : def;
	}

	@Override
	public int getInteger(String key) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		String val = getPlainValue(tval);
		return val!=null ? Integer.parseInt(val) : 0;
	}

	@Override
	public int getInteger(String key, int def) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		String val =  getPlainValue(tval);
		return val!=null ? Integer.parseInt(val) : def;
	}

	@Override
	public long getLong(String key) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		String val = getPlainValue(tval);
		return val!=null ? Long.parseLong(val) : 0;
	}

	@Override
	public long getLong(String key, long def) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		String val =  getPlainValue(tval);
		return val!=null ? Long.parseLong(val) : def;
	}

	@Override
	public String getString(String key) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		return getPlainValue(tval);
	}

	@Override
	public String getString(String key, String def) {
		String tkey = getTransformedValue(key);
		String tval = prefs.getString(tkey);
		String val =  getPlainValue(tval);
		return val!=null ? val : def;
	}

	@Override
	public void put(Map<String, ?> vals) {
		
		for (Entry<String, ?> val : vals.entrySet()) {
			if (val.getValue() instanceof Boolean) putBoolean(val.getKey(), (Boolean)val.getValue());
			if (val.getValue() instanceof Integer) putInteger(val.getKey(), (Integer)val.getValue());
			if (val.getValue() instanceof Long) putLong(val.getKey(), (Long)val.getValue());
			if (val.getValue() instanceof String) putString(val.getKey(), (String)val.getValue());
			if (val.getValue() instanceof Float) putFloat(val.getKey(), (Float)val.getValue());
		}
	}

	@Override
	public void putBoolean(String key, boolean val) {
		String tkey = getTransformedValue(key);
		String tval = getTransformedValue(Boolean.toString(val));
		
		prefs.putString(tkey, tval);
	}

	@Override
	public void putFloat(String key, float val) {
		String tkey = getTransformedValue(key);
		String tval = getTransformedValue(Float.toString(val));
		
		prefs.putString(tkey, tval);
	}

	@Override
	public void putInteger(String key, int val) {
		String tkey = getTransformedValue(key);
		String tval = getTransformedValue(Integer.toString(val));
		
		prefs.putString(tkey, tval);
	}

	@Override
	public void putLong(String key, long val) {
		String tkey = getTransformedValue(key);
		String tval = getTransformedValue(Long.toString(val));
		
		prefs.putString(tkey, tval);
	}

	@Override
	public void putString(String key, String val) {
		String tkey = getTransformedValue(key);
		String tval = getTransformedValue(val);
		
		prefs.putString(tkey, tval);
		
	}

	@Override
	public void remove(String key) {
		String tkey = getTransformedValue(key);
		prefs.remove(tkey);
		
	}

}
