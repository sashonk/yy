package com.me.test.game2;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.me.test.IActivityRequestHandler;
import com.me.test.L10nButton;
import com.me.test.L10nLabel;
import com.me.test.Sounds;
import com.me.test.TestGame;
import com.me.test.Util;
import com.me.test.Values;

public class MenuScreen extends BaseMenuScreen{
	
	
	static final String menuButtons = "{menuButtons:[{key:null,x:0.0,y:0.0,angle:0.0},{key:resume,x:555.0,y:591.0,angle:-15.0},{key:null,x:0.0,y:0.0,angle:0.0},{key:game,x:195.0,y:560.0,angle:15.0},{key:null,x:0.0,y:0.0,angle:0.0},{key:settings,x:180.0,y:278.0,angle:-15.0},{key:null,x:0.0,y:0.0,angle:0.0},{key:about,x:556.0,y:442.0,angle:-15.0},{key:null,x:0.0,y:0.0,angle:0.0},{key:credits,x:593.0,y:168.0,angle:30.0}]}"; 

	//static final String menuButtons = "{menuButtons:[{key:resume,x:555.0,y:591.0,angle:-15.0},{key:game,x:195.0,y:560.0,angle:15.0},{key:settings,x:533.0,y:193.0,angle:30.0},{key:about,x:556.0,y:442.0,angle:-15.0},{key:exit,x:186.0,y:92.0,angle:-15.0},{key:credits,x:243.0,y:316.0,angle:-15.0}]}"; 
	
	
	InputListener onButtonExit(){
		return new InputListener(){
			public boolean keyDown (InputEvent event, int keycode) {
				
				if(keycode == Keys.BACK  || keycode==Keys.ESCAPE){
					
					Actor rd = getStage().getRoot().findActor("rate_dialog");
					if(rd!=null && rd.isVisible()){
						return true;
					}
					
					
					final Preferences prefs = getGame().getPreferences();
					final boolean rated = prefs.getBoolean(Values.rated, false);
					final int exitClickCount = prefs.getInteger(Values.exit_click_count, 0);
					final boolean played = prefs.getBoolean(Values.played, false);
					
					
					if(!rated && played && ( exitClickCount%3==0) ){
						getGame().getNative().checkInternetConnection(new IActivityRequestHandler.OnlineStatusCallback() {
							
							@Override
							public void call(final boolean online) {
								
								Gdx.app.postRunnable(new Runnable() {
									
									@Override
									public void run() {
										if(online){
												final Dialog dialog = new Dialog("", getGame().getManager().getSkin(), "win");
												dialog.setName("rate_dialog");
												dialog.getButtonTable().defaults().pad(20).padTop(0);
												dialog.getContentTable().defaults().align(Align.center).pad(10);
												dialog.setClip(false);
												//dialog.setBackground("dialog");
												dialog.setModal(true);
												dialog.setMovable(false);
												Label label = new L10nLabel(getGame().getL10n(), "do_you_mind_rate", getGame().getManager().getSkin(), "small");
												label.setAlignment(Align.center);
												label.setWrap(true);
												dialog.getContentTable().add(label).width(200);
												
												Button yes = new L10nButton(getGame().getL10n(), "yes_rate", getGame().getManager().getSkin(), "btn");
												Button no = new L10nButton(getGame().getL10n(), "no_rate", getGame().getManager().getSkin(), "btn");
												dialog.getButtonTable().add(yes).padRight(50);
												dialog.getButtonTable().add(no);
												no.addListener(new ClickListener(){
													public void clicked (InputEvent event, float x, float y) {																			
														Gdx.app.exit();
													}
												});
												
												yes.addListener(new ClickListener(){
													public void clicked (InputEvent event, float x, float y) {
														getGame().getNative().rate();
														dialog.hide();
														prefs.putBoolean(Values.rated, true);
														prefs.flush();
													}
												});									
												dialog.show(getStage());														

										}
										else{
											Gdx.app.exit();
										}
										
									}
								});
								
								
		
								
							}
						});
					}
					else{
						Gdx.app.exit();
					}
					
					
					if(played){
						prefs.putInteger(Values.exit_click_count, exitClickCount+1);
						prefs.flush();
					}

					

				}
				
				return false;
			}
		};
	}

	Button resumeGame;
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		getStage().setKeyboardFocus(getStage().getRoot());
		
		
		if(getGame().getSession().isInGame()){
			//resumeGame.setVisible(true);
		}
		else{
			resumeGame.setVisible(false);
		}
		
		//getGame().getNative().getEnabledFeatures();
/*		Set<Feature> enabledFeatures =  getGame().getNative().getEnabledFeatures();
		

		for(Feature feature : enabledFeatures){
			getGame().getSession().getEnabledFeatures().add(feature);
		}*/
		
		update();
	}
	@Override
	public void update() {
		super.update();
		ownedYangsLabel.setText(Integer.toString(getGame().getSession().getOwnerYangs()));
	
		
	}
	
	Label ownedYangsLabel;

	public MenuScreen(final TestGame g) {
		super(g);
		
		ownedYangsLabel = yangPanel(getStage().getRoot(), g);
		getStage().addListener(onButtonExit());
		

			 resumeGame = menuButton("resume", new ClickListener(){
					public void clicked (InputEvent event, float x, float y) {
						g.getSoundManager().play(Sounds.MENU_BUTTON);
						TestGame tg = getGame();
						tg.setScreen(tg.getGame2Screen());
					}
				});


		
		
		menuButton("game", new ClickListener(){
	
			public void clicked (InputEvent event, float x, float y) {
				g.getSoundManager().play(Sounds.MENU_BUTTON);
				TestGame tg = getGame();
				tg.setScreen(tg.getChooseListScreen());
			}
		});
		
		 menuButton("settings", new ClickListener(){
			
			public void clicked (InputEvent event, float x, float y) {
				g.getSoundManager().play(Sounds.MENU_BUTTON);
				TestGame tg = getGame();
				tg.setScreen(tg.getSettingsScreen());
			}
		});

		
		 menuButton("about", new ClickListener(){
			
			public void clicked (InputEvent event, float x, float y) {
				g.getSoundManager().play(Sounds.MENU_BUTTON);
				TestGame tg = getGame();
				tg.setScreen(tg.getRulesScreen());
			}
		});
		
		

		
/*		menuButton("exit", new ClickListener(){
			
			public void clicked (InputEvent event, float x, float y) {
				g.getSoundManager().play(Sounds.MENU_BUTTON);
				Gdx.app.exit();
			}
		});*/
		
		
		menuButton("credits", new ClickListener(){
			
			public void clicked (InputEvent event, float x, float y) {
				g.getSoundManager().play(Sounds.MENU_BUTTON);
				getGame().setScreen(getGame().getCreditsScreen());
			}
		});
		layoutMenu();


		
		boolean val = TestGame.devmode;
		if(val){
			Table devTable = new Table();
			
/*			TextButton test = new TextButton("test", g.getManager().getSkin(), "reset");
			devTable.add(test).row();
			
			test.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					TestGame tg = getGame();
					GameScreen gs = new GameScreen(tg);
					tg.setScreen(gs);
				}
			});*/
			
			

			TextButton editor = new TextButton("editor", g.getManager().getSkin(), "reset");
			devTable.add(editor).row();
			
			editor.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					TestGame tg = getGame();
					EditorGame2 editorScreen = tg.getEditorScreen();
					
					Preferences prefs = getGame().getPreferences();
					int level = prefs.getInteger("lastEdited", 1);
					
					editorScreen.loadLevel(level);
					tg.setScreen(editorScreen); 
				}
			});
			
			

			TextButton resetBest = new TextButton("reset", g.getManager().getSkin(), "reset");
			devTable.add(resetBest).row();
			
			resetBest.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					Preferences prefs = getGame().getPreferences();
					for(int i = 0; i<=120; i++){
						prefs.remove("best"+i);
						prefs.remove("is_done_"+i);
						prefs.remove("achivement"+i);
					}
					
					prefs.remove(Scripts.education_hand_done);
					prefs.remove(Scripts.education_step_cancel_done);
					prefs.remove(Scripts.education_walkthrough_done);
					prefs.flush();
				}
			});
			
			
			TextButton demo = new TextButton("demo", g.getManager().getSkin(), "reset");
			devTable.add(demo).row();			
			demo.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {

					Game2 gm = getGame().getGame2Screen();
					Actor layer = gm.getStage().getRoot();
					layer.addListener(new ClickListener(){
						public void clicked (InputEvent event, float x, float y) {
							TestGame tg = getGame();
							tg.setScreen(tg.getMenuScreen());
						}
					});
					GoogleDemoScript.demo(gm, 1);
					g.getSession().setInGame(false);
					
					getGame().setScreen(gm);
				}
			});
			
			TextButton diftask = new TextButton("diftask", g.getManager().getSkin(), "reset");
			devTable.add(diftask).row();			
			diftask.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					
					Map<Integer, Integer> diffMap = new TreeMap<Integer, Integer>();
					
					int level = 1;
					while(true){
						FileHandle handle = Files.level(level);
						if(handle.exists()){
							DataInputStream is=null;
							try{
								 is = new DataInputStream(handle.read()) ;
								byte[] meta = new byte[100];
								is.read(meta, 0, 100);
								
								DataInputStream metaDis = new DataInputStream(new ByteArrayInputStream(meta));
								short version = metaDis.readShort();
								int totalCount = metaDis.readInt();
								boolean rotten =metaDis.readBoolean();
								float scale = metaDis.readFloat();
	
								 
								int difficulty = metaDis.readInt();
								if(difficulty<1 || difficulty>4){
									difficulty = 1;
								}
								
								diffMap.put(level, difficulty);
								System.out.println("level "+level+", diff "+difficulty);
								
								
								
							}catch(Exception e){
								e.printStackTrace();
							}
							finally{
								if(is!=null){
									try {
										is.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							
						}
						else{
							break;
						}
						
						level++;
					}
					
					try {
						ObjectOutputStream oos = new ObjectOutputStream(Files.diffMap().write(false));
						oos.writeObject(diffMap);
						oos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
				}
			});
		
			devTable.pack();
			devTable.setPosition(50, 50);
			getStage().addActor(devTable);
		}
		

		
		if(TestGame.positionMenuButtons){
			getStage().getRoot().addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					StringBuilder sb = new StringBuilder("{menuButtons:[");
					int c = 0;
					for(Actor a : getStage().getRoot().getChildren()){
						if(a instanceof Group){
							
							
							String name = a.getName();
							float px = a.getX();
							float py = a.getY();
							float r = a.getRotation();
							if(c>0)
								sb.append(",");
							sb.append("{").append("key:").append(name).append(',').append("x:").append(px).append(",").append("y:").append(py).append(",").append("angle:").append(r).append("}");
							c++;
						}
					
					}
					sb.append("]}");
					
					System.out.println(sb);
				}
			});
		}
		
/*		ParticleEffect eff = new ParticleEffect();
		eff.load(Gdx.files.internal("data/star.par"), Gdx.files.internal("data"));
		
	//	eff.setPosition(150, 150);
	//	eff.draw(spriteBatch, delta);
		ParticleEffectActor pea = new ParticleEffectActor(eff);
		getStage().addActor(pea);
		eff.reset();
		pea.start();*/
	}
	
	
	TextButton menuButton(String key, ClickListener listener){
		
		final Group backing = new Group();
		backing.setName(key);
		
		String style = "game".equals(key) ?  "menu2" : "menu";
		final TextButton button =  new L10nButton(getGame().getL10n(), key, getGame().getManager().getSkin(), style);
		backing.addActor(button);
		backing.setOrigin(button.getWidth()/2, button.getHeight()/2);
		
		
		if(TestGame.positionMenuButtons){
			
			button.addListener(new DragListener(){
				

				public void drag (InputEvent event, float x, float y, int pointer) {
					
					Vector2 v = new Vector2(Gdx.input.getX(), Gdx.input.getY());
					Vector2 pos = getStage().screenToStageCoordinates(v);					
					backing.setPosition(pos.x, pos.y);

				}					
			});
						
			button.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					if(Gdx.input.isKeyPressed(Keys.R)){
						backing.rotateBy(15);
						
						//backing.r
						System.out.println("rotate");
					}
					else if(Gdx.input.isKeyPressed(Keys.E)){
						backing.rotateBy(-15);
						System.out.println("rotate");
					}
				}
			});
		}
		else{
			button.addListener(listener);
		}
		
/*		Image sett = new Image(getGame().getManager().getAtlas().findRegion("settings"));
		sett.setBounds(715, 160, 60, 60);
		getStage().addActor(sett);
		
		
		Image yy = new Image(getGame().getManager().getAtlas().findRegion("yin"));
		yy.setBounds(641, 400, 50, 50);
		yy.setOrigin(yy.getWidth()/2, yy.getHeight()/2);
		getStage().addActor(yy);
		yy.rotateBy(-15);
		
		Image devs = new Image(getGame().getManager().getAtlas().findRegion("developers"));
		devs.setBounds(287, 260, 50, 60);
		getStage().addActor(devs);
		devs.rotateBy(-15);
		
		Image exit = new Image(getGame().getManager().getAtlas().findRegion("exit"));
		exit.setBounds(220, 60, 45, 35);
		getStage().addActor(exit);*/
		
		
/*		
		getStage().addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				System.out.println(new StringBuilder().append("x=").append(x).append("; y=").append(y));
			}
		});
		
		FileHandle scmlFile = Gdx.files.internal("data/spriter/ant/test.scml");

		final SCMLReader reader = new SCMLReader(scmlFile.read());
		
		//this.loader = new LibGdxAtlasLoader(reader.getData(), Gdx.files.internal("data/spriter/ant/ant.atlas"));
		this.loader = new LibGdxLoader(reader.getData());
		
		this.loader.load("data/spriter/ant");
	 
		this.drawer = new LibGdxDrawer(loader, (SpriteBatch) getStage().getBatch(), null);
		

		
		
		stage.addAction(Actions.forever(Actions.sequence(Actions.delay(between(0.3f, 0.8f)), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				
				Player player = new Player(reader.getData().getEntity(0));
				player.setScale(new Random(System.currentTimeMillis()).nextFloat()*0.3f);		
				SpriterPlayer spriterPlayer = new SpriterPlayer(player, drawer,  -500,  0);
				
				spriterPlayer.getPlayer().setAnimation("run");
				spriterPlayer.addAction(Actions.moveBy(2000, 0, between(3, 6)));
				stage.addActor(spriterPlayer);
				
			
			}
		}))));*/
		
		
		
		
		
		//twitterButton();
		Table socials = new Table();
		socials.pad(20);
		Table join = Scripts.joinButton(getGame());
		socials.add(join);
		
		Table tweeter = Util.twitterButton(this.getGame());
		socials.add(tweeter);
		
		socials.pack();
		getStage().addActor(socials);
		

		
		stage.getRoot().setCullingArea(null);
		
		getStage().addActor(backing);
		return button;
	}

	

	

	
	void layoutMenu(){
		JsonReader reader = new JsonReader();
		JsonValue map =   reader.parse(menuButtons);		
		JsonValue array = map.get("menuButtons");
		for(Object entry : array){
			JsonValue entryMap = (JsonValue)entry;
			
			 String key= entryMap.get("key").asString();
			 if(key==null){
				 continue;
			 }
			 
			Actor group = getStage().getRoot().findActor(key);
			//if(!"game".equals(key)){
				group.setRotation(entryMap.get("angle").asFloat());
		//	}
			group.setX(entryMap.get("x").asFloat());
			group.setY( entryMap.get("y").asFloat());
			
		/*	Action labelShake = Actions.forever(Actions.sequence(Actions.delay(5), Actions.moveBy(0, 50, 0.3f, Interpolation.swingIn),Actions.moveBy(0, -50, 0.6f, Interpolation.swingOut)));
			group.addAction(labelShake);*/
		}
		

	}

	@Override
	public String getName() {
		return "menu";
	}
	

	

}
