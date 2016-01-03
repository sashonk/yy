package com.me.test;


public  class LangDefinition{
	
	public static interface CardinalDeclention{
		
		public String getDeclinedForm(String baseKey, int number);
		
	}

	
	public LangDefinition(String locale, String title, CardinalDeclention declention){
		this.title = title;
		this.locale = locale;
		_declension = declention;
	}
	
	public String getTitle() {
		return title;
	}
	public String getLocale() {
		return locale;
	}
	
	public CardinalDeclention getCardinalDeclension(){
		return _declension;
	}

	private String title;
	private String locale;
	private CardinalDeclention _declension;
	
	
	
}
