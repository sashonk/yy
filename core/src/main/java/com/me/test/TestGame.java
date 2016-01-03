package com.me.test;




import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.me.test.game2.ChooseLevel;
import com.me.test.game2.ChooseLevel2;
import com.me.test.game2.ChooseList;
import com.me.test.game2.ChooseListYY;
import com.me.test.game2.CreditsScreen;
import com.me.test.game2.EditorGame2;
import com.me.test.game2.Game2;
import com.me.test.game2.MenuScreen;
import com.me.test.game2.RulesScreen;





public class TestGame extends Game implements IGame{
	
	L10nManager l10n;
	Session session;
	Settings settings;
	public static String yypuzzleUrl = "https://www.facebook.com/yypuzzle";
	private Random random ;

	public Settings getSettings(){
		return settings;
	}

	public Session getSession(){
		return session;
	}
	
	public L10nManager getL10n(){
		return l10n;
	}
	
	boolean inited;
	IActivityRequestHandler natiff;
	 boolean error = false;
	
	public IActivityRequestHandler getNative(){
		return natiff;
	}
	
	public TestGame(IActivityRequestHandler platform){
		this.natiff = platform;
		
		clear = natiff.clear();
		devmode = natiff.dev();
	}
	
	public static  boolean clear ;
	public static  boolean devmode ;
	public static final boolean positionMenuButtons = false;
	public static final boolean disableLevel = true;
	public static final boolean lockExtras = true;
	public static final boolean demoMode = false;
	public static final boolean test = false;
	  public static final int maxVideosPerDay = 10;
	  public static final int walkthroughDelay = 90;
	  public static final int walkthroughShowTime = 10;

	  
	MenuScreen menuScreen;
	ChooseLevel chooseLevelScreen;
	ChooseLevel2 chooseLevelScreen2;
	ChooseListYY chooseListScreen;
	Game2 game2Screen;
	EditorGame2 editorScreen;
	CreditsScreen creditsScreen;
	SettingsScreen2 settingsScreen;
	RulesScreen rulesScreen;
	
	public CreditsScreen getCreditsScreen(){
		return creditsScreen;
	}
	
	public EditorGame2 getEditorScreen(){
		return editorScreen;
	}
	
	public MenuScreen getMenuScreen() {
		return menuScreen;
	}

	public ChooseLevel getChooseLevelScreen() {
		return chooseLevelScreen;
	}

	public Game2 getGame2Screen() {
		return game2Screen;
	}

	public ChooseListYY getChooseListScreen() {
		return chooseListScreen;
	}

	public BaseScreen getSettingsScreen() {
		return settingsScreen;
	}

	public RulesScreen getRulesScreen() {
		return rulesScreen;
	}
	

	ResourcesManager manager ;
	
	void setManager(ResourcesManager m){
		manager = m;
	}
	
	public ResourcesManager getManager(){
		return manager;
	}
	
	
	private SoundManager soundManager ;
	public SoundManager getSoundManager(){
		return soundManager;
	}
	
	void clearPrefs(){
		getPreferences().clear();
	}
	
	public Preferences getPreferences(){
		Preferences p = Gdx.app.getPreferences(this.getClass().getName());
		
			return new SecuredPreferences(p);
	
	}


	@Override
	public void create() {
		
		random = new Random(System.currentTimeMillis());
/*		// TODO Auto-generated method stub
		try{
			KeyStore kstore = KeyStore.getInstance("jceks");
			kstore.load((Gdx.files.internal("data/yy.ks").read()), new char[]{'8', '9', '2','6','y','y','p', 'a', 's', 's'});		
			secret = kstore.getKey("yy", new char[]{'8', '9', '2','6','y','y','p', 'a', 's', 's'});					
		}
		catch(Exception ex){
			Gdx.app.error( "error","failed to get YY secret", ex);
		}
		*/
		List<LangDefinition> langDefinitions = new LinkedList<LangDefinition>();
		LangDefinition en = new LangDefinition("en", "english", new LangDefinition.CardinalDeclention() {
			
			@Override
			public String getDeclinedForm(String baseKey, int number) {
				
				return Util.getEnCardinalDeclensionKey(baseKey, number);
			}
		});
		langDefinitions.add(en);
		LangDefinition ru = new LangDefinition("ru", "русский", new LangDefinition.CardinalDeclention() {
			
			@Override
			public String getDeclinedForm(String baseKey, int number) {
				
				return Util.getRuCardinalDeclensionKey(baseKey, number);
			}
		});
		langDefinitions.add(ru);
		
	
		
		Gdx.input.setCatchBackKey(true);
		Preferences prefs = getPreferences();

		if(clear){
			clearPrefs();
			int ld = 119;

					
			//prefs.putInteger(Values.last_done, ld);
			for(int i = 1; i<=ld; i++ ){
				prefs.putBoolean("is_done_"+i, true);
			}
		}

	/*	
	LevelReplaceTask task = new LevelReplaceTask();
		task.process();*/
		
/*	
	int c = 120;
		for(int i = 121 ;i<=150 ;i++){
			com.me.test.game2.Files.level(c).copyTo(com.me.test.game2.Files.level(i));
		}*/
		
/*		//int c = 50;
		for(int i = 1 ;i<100 ;i++){
			com.me.test.game2.Files.level(i).delete();
		}*/
	
		
		Gdx.app.log(TestGame.class.getName(), "game started");		
		l10n = new L10nManager();
		for(LangDefinition def : langDefinitions){
			l10n.addLanguage(def);
		}
		

		settings = new Settings(this);
		settings.read();
		if(devmode){
			settings.setMusicVolume(0.01f);
			settings.setSoundVolume(0.01f);
		}
		
		 soundManager = new SoundManager(this);
		//settings.toDefaults();
		//settings.write();

		l10n.setLanguage(settings.getLanguage());
		
		session = new Session();
		session.setInGame(false);
		
		int ownedYangs = prefs.getInteger(Values.yangs, 3);
		session.setOwnedYangs(ownedYangs);

		
		BaseScreen splash = new SplashScreen(this);
		setScreen(splash);
	}


	@Override
	public void dispose () {
		if (getScreen() != null) getScreen().hide();
		
		menuScreen.dispose();
		game2Screen.dispose();
		rulesScreen.dispose();
		chooseLevelScreen.dispose();
		creditsScreen.dispose();
		editorScreen.dispose();
		settingsScreen.dispose();
		chooseListScreen.dispose();
		chooseLevelScreen2.dispose();
		
		if(manager!=null){
			manager.dispose();
		}
	}
	
	public void createScreens(){
		menuScreen = new MenuScreen(this);
		game2Screen = new Game2(this);
		settingsScreen = new SettingsScreen2(this);
		rulesScreen = new RulesScreen(this);
		chooseLevelScreen = new ChooseLevel(this);
		editorScreen = new EditorGame2(this);
		creditsScreen = new CreditsScreen(this);
		chooseListScreen = new ChooseListYY(this);
		chooseLevelScreen2 = new ChooseLevel2(this);
	}
	
/*	@Override
	public void setScreen(Screen screen){

		BaseScreen base = (BaseScreen) screen;
	//	screens.put(base.getName(), base);
		super.setScreen(screen);
	}*/
	
/*	public void setScreen(String name){
		BaseScreen base = (BaseScreen) screens.get(name);
		if(base!=null){
			super.setScreen(base);
		}
	}*/


	public ChooseLevel2 getChooseLevelScreen2() {
		return chooseLevelScreen2;
	}

	@Override
	public void pause () {
		if (getScreen() != null) getScreen().pause();
		

	}

	@Override
	public Random getRandom() {
		return random;
	}

	

}


