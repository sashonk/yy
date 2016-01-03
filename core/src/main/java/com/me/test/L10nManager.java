package com.me.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class L10nManager {
	private FileHandle root;
	
	void addLanguage(LangDefinition langdef){

		String lang = langdef.getLocale();
		FileHandle file = root.child(lang+".txt");
		if(!file.exists()){
			throw new RuntimeException("l10n file not found: "+lang);
		}
		
		Map<String, String> localization =new HashMap<String, String>();
		localizations.put(lang, localization);
		definitions.put(lang, langdef);
		
		BufferedReader br =null;
		try{ 
			br = new BufferedReader(file.reader("utf-8"));
			
			while(true){
				String line = br.readLine();
				if(line==null){
					break;
				}
				
				String[] parts = line.split("=");
				if(parts==null || parts.length<2){
					continue;
				}
				
				String name = parts[0].trim();
				
				String rawValue = parts[1].trim();
				//StringBuilder value =  new StringBuilder(parts[1].trim()) ;
				StringBuilder value = new StringBuilder();
				
				if(rawValue.indexOf('[')>-1){
					while(true){
						if(rawValue==null){
							break;
						}
						
						int begin = 0;
						int end = rawValue.length();
						
						boolean exit = false;
						if(rawValue.indexOf("[")>-1){
							begin = rawValue.indexOf("[")+1;
						}
						
						if(rawValue.indexOf("]")>-1){
							end = rawValue.indexOf("]");
							exit = true;
						}
						
						value.append(rawValue.substring(begin, end));
						
						if(exit){
							break;
						}
						else{
							value.append('\n');
							rawValue = br.readLine();
						}
						
						
					}						
				}
				else{
					value.append(parts[1].trim());
				}
				
				if(name.length()==0 || value.length()==0){
					continue;
				}
				
				
				localization.put(name, value.toString());
			}
		}
		catch(Exception ex){
			Gdx.app.error(L10nManager.this.getClass().getName(), "error reading file" ,ex);
		}
		finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Gdx.app.error(L10nManager.this.getClass().getName(), "error closing stream" ,e);
					
				}
			}
		}
		
		
		
	
	}
	
	public L10nManager(LangDefinition...langs){
		root = Gdx.files.internal("data/l10n/");
		
		for(LangDefinition lang : langs){
			addLanguage(lang);
		}
	
	}
	
	
	public String getLanguage(){
		return language;
	}
	
	private String language;
	public void setLanguage(String lang){
		language = lang;

		for(L10nLabel label : labels){
			Object[] params = label.getParams();
			String key = label.getKey();
			String text = params!=null ? Util.replace(get(key), params) : get(key);
			label.setText(text);
			label.pack();
		}
		
		for(L10nButton button : buttons){
			String key = button.getKey();
			String text = get(key);
			button.setText(text);
			button.pack();
		}
	}
	
	public void registerLabel(L10nLabel label){
		labels.add(label);
	}
	
	public void registerButton(L10nButton label){
		buttons.add(label);
	}
	
	private List<L10nLabel> labels= new LinkedList<L10nLabel>();
	
	private List<L10nButton> buttons= new LinkedList<L10nButton>();
	

	
	public String get(String name, String lang){
		Map<String, String> l10n = localizations.get(lang);
		if(l10n==null){
			l10n = localizations.get("en");
			if(l10n==null){
				l10n = localizations.size() > 0 ? localizations.values().iterator().next() : null;
			}
		}
		
		String result = l10n !=null ? l10n.get(name) : null;
		if(result==null){
			result = String.format("[unlocalized:%s]", name);
		}
		
		return result;
	
	}
	
	public String get(String name, boolean withCardinalDeclension){
		String result =  get(name, language);
		
		return result;
	}

	public String get(String name){
		String result =  get(name, language);
		
		return result;
	}
	

	
	public Map<String, String> getLocalizedLanguages(){
		Map<String, String> result = new TreeMap<String, String>();
		for(String lang : localizations.keySet()){
			String localizedLang = get(lang);

			
			result.put(lang, localizedLang);
		}
		
		return Collections.unmodifiableMap(result);
	}
	
	public Set<String> getLanguages(){
		return Collections.unmodifiableSet(localizations.keySet());
	}
	
	 public Map<String,LangDefinition> getDifinitions(){
		 return definitions;
	 }

	Map<String,LangDefinition> definitions = new HashMap<String, LangDefinition>();

	Map<String, Map<String, String>> localizations = new HashMap<String, Map<String, String>>();
	
}
