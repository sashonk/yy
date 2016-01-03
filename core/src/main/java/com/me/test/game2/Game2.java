package com.me.test.game2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.L10nLabel;
import com.me.test.LightActor;
import com.me.test.Pair;
import com.me.test.ParticleEffectActor;
import com.me.test.Session;
import com.me.test.Settings;
import com.me.test.SmartListener;
import com.me.test.Sounds;
import com.me.test.TestGame;
import com.me.test.Util;
import com.me.test.Util.TriggerAction;
import com.me.test.Util.TriggerAction.Predicate;
import com.me.test.Values;
import com.me.test.game2.BaseItem.Direction;
import com.me.test.game2.Game2.History.ItemData;
import com.me.test.game2.Game2.History.TableSnapshot;
import com.me.test.game2.ItemContainer.LevelMeta.Tips;
import com.me.test.game2.WinScenario.EmitterAction;
import com.me.test.game2.item.Box;
import com.me.test.game2.item.Fall;
import com.me.test.game2.item.Ground;
import com.me.test.game2.item.Metal;
import com.me.test.game2.item.Wall;
import com.me.test.game2.item.Yang;
import com.me.test.game2.item.Yin;
import com.me.test.game2.ui.ButtonEvent;
import com.me.test.game2.ui.ButtonEvent.Source;
import com.me.test.game2.ui.GestureController;
import com.me.test.game2.ui.Keyboard;


public class Game2 extends ItemContainer {
	
	@Override
	public void dispose() {
		super.dispose();
		
		if(rayHandler!=null){
			rayHandler.dispose();
			rayHandler = null;
		}
		
		if(world!=null){
			world.dispose();
		}
	}

	static final int MAX_HISTORY_SIZE = 50;
	static float AWAIT_YY_FALL = 0.0f;
	static boolean YY_STAND_STILL_WHEN_FALL = false;
	long timer;
	int proposeCount;
	Window walkWindow;
	
	Action proposeWlakthrough;
	 
	int stepCount;
	boolean win;
	L10nLabel personalBestLabel2;
	Label stepLabel;
	final Label levelLabel;
	
	Table rightTops;
	Table leftTop;
	int level = -1;
	//final TextButton reset;
	final ImageButton back;
	final ImageButton undo;
	final TextField dotips;
//	final TextButton tipsButton;
	boolean IN_TIPS;
	final ImageButton nextTipButton;
	Table tipsTable;
	SubMenu subMenu;
	Label ownedYangsLabel;
	
	public SubMenu getSubmenu(){
		return subMenu;
	}

	Controller controller;
	
	void renderStep(){
		String value = Integer.toString(stepCount);
/*		if(value.length()<2){
			value = " "+value;
		}*/
		
		stepLabel.setText(value);
	}
	
	public void enableTips(){
		IN_TIPS = true;
		
		tipsTable.setVisible(true);
		
		//getGame().getNative().
		
		if(stepCount>0){
		reset(true, new ResetCallback(){

			@Override
			public void onResetComplete() {

				
				if(level>0){
					start(level);
				}
				
				IN_TIPS = true;
				tipsTable.setVisible(true);
				nextTipButton.setDisabled(false);
				//nextTipButton.setVisible(true);
				//game.getSession().setWalkthroughLastLevel(level);
				walkWindow.clearActions();
				walkWindow.setY(uiLayer.getHeight());
				educationWalkthrough();
			}});
		}
		else{
			IN_TIPS = true;
			tipsTable.setVisible(true);
			nextTipButton.setDisabled(false);
			//next
			//game.getSession().setWalkthroughLastLevel(level);
			walkWindow.clearActions();
			walkWindow.setY(uiLayer.getHeight());
			educationWalkthrough();
		}
		

	}
	
	private void educationWalkthrough(){
		Preferences p = getGame().getPreferences();
		if(!p.getBoolean(Scripts.education_walkthrough_done)){
			
			Scripts.educationWalkthrough(this);
			
			p.putBoolean(Scripts.education_walkthrough_done, true);
		}
	}

	static class History{
		
		History(){
			_data = new LinkedList<TableSnapshot>();
			
		}
		
		static class ItemData{
			Class<? extends BaseItem> cls;
			int locationX;
			int locationY;
		}
		
		static class TableSnapshot{
			TableSnapshot(Game2 game, Collection<BaseItem> items, Direction move){
				_move = move;
				_items = new ArrayList<ItemData>(items.size());
				for(BaseItem item : items){
					ItemData data = new ItemData();
					data.cls = item.getClass();
					data.locationX = item.getLocationX();
					data.locationY = item.getLocationY();
					_items.add(data);
				}
			}
			
			Collection<ItemData> _items ;
			Direction _move;
		}
		
		public LinkedList<TableSnapshot> getData(){
			return _data;
		}
		
		void clear(){
			_data.clear();
		}
		
		
		LinkedList<TableSnapshot> _data;
	}
	
	
	History history;
	Table editTips;
	

	ParticleEffectPool starPool;
	ParticleEffectPool greenPool;
	ParticleEffectPool bluePool;
	ParticleEffectPool redPool;
	
	
	
	public Game2(final TestGame g) {
		super(g);
		

	
	//	bubble.getColor().a = 0;
	//	bubble.addAction(Actions.fadeIn(1));
		
		

		
		
		getStage().addListener(onButtonExit());
		
		ownedYangsLabel = yangPanel(uiLayer, g);
		
		subMenu = new SubMenu(g);
		
		
		
		IN_TIPS = false;
		history = new History();
		
		final Stage s = getStage();
		final Skin skin = getGame().getManager().getSkin();
		
		executor = Executors.newFixedThreadPool(8);
		

		ParticleEffect starEff = new ParticleEffect();
		starEff.load(Gdx.files.internal("data/effects/star.par"), Gdx.files.internal("data/effects"));
		starPool = new ParticleEffectPool(starEff, 4, 4);
		
		 ParticleEffect fire_red = new ParticleEffect();
			fire_red.load(Gdx.files.internal("data/effects/fire_red.par"), Gdx.files.internal("data/effects"));
		redPool = new ParticleEffectPool(fire_red, 1, 2);
		
		 ParticleEffect fire_green = new ParticleEffect();
		fire_green.load(Gdx.files.internal("data/effects/fire_green.par"), Gdx.files.internal("data/effects"));
		greenPool = new ParticleEffectPool(fire_green, 1, 2);
		
		 ParticleEffect fire_blue = new ParticleEffect();
		fire_blue.load(Gdx.files.internal("data/effects/fire_blue.par"), Gdx.files.internal("data/effects"));
		bluePool = new ParticleEffectPool(fire_blue, 1, 2);
		
		
		rightTops = new Table();
		rightTops.defaults().align(Align.left);
		 
		float rightTopWidth = 150;
		Window levelWin = new Window("", skin);	
		levelWin.padLeft(10);
		levelWin.setBackground("wall");
		L10nLabel levelLbl = new L10nLabel(g.getL10n(), "level", game.getManager().getSkin(),"yellow");
		levelLabel = new Label(Integer.toString(level), skin, "yellow");
		float levelPadFix = 140;
		levelWin.add(levelLbl).width(levelPadFix);
		levelLabel.setAlignment(Align.right);
		levelWin.add(levelLabel).width(50);
		levelWin.pack();
		rightTops.add(levelWin).row();//.padRight(toppad);
		

		Window stepWin = new Window("", skin);
		stepWin.padLeft(10);
		stepWin.setBackground("wall");
		L10nLabel stepLbl = new L10nLabel(g.getL10n(), "steps", game.getManager().getSkin(),"yellow");
		stepLabel = new Label(Integer.toString(stepCount), skin, "yellow");
		stepWin.add(stepLbl).width(levelPadFix);
		stepLabel.setAlignment(Align.right);
		stepWin.add(stepLabel).width(50);
		stepWin.pack();
		rightTops.add(stepWin);


		rightTops.pack();
		uiLayer.addActor(rightTops);
		rightTops.setPosition(s.getWidth()-rightTops.getWidth(), s.getHeight()-rightTops.getHeight());
		
		 leftTop = new Table();

		
		personalBestLabel2= new L10nLabel(g.getL10n(), "best", game.getManager().getSkin(),"digit",false, "");
		leftTop.add(personalBestLabel2).padRight(10);

		
		leftTop.pack();
		uiLayer.addActor(leftTop);
		leftTop.setPosition(40, s.getHeight()-leftTop.getHeight()-20);
		
		 editTips = new Table();		
		dotips = new TextField("", skin);
		
		editTips.add(new Label("tips", skin, "tiny"));
		editTips.add(dotips);
		editTips.pack();
		uiLayer.addActor(editTips);
		editTips.setPosition(40, leftTop.getY()-30);


		back = new ImageButton(skin, "pause");
		back.setPosition(20, 20);
		back.setSize(60, 80);
		back.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				if(back.isDisabled()){
					return;
				}
				
				TestGame tg = getGame();
			//	tg.setScreen(tg.getMenuScreen());
				tg.setScreen(getSubmenu());
			}
		});
		uiLayer.addActor(back);

		
		controller = createGestureController();		
		Group controllerUi = (Group)controller;
		controllerUi.setPosition(0, 0);
		uiLayer.addActor(controllerUi);
		
		

		//undo = new L10nButton(g.getL10n(), "undo", getGame().getManager().getSkin(), "game");
		undo = new ImageButton(skin, "replay");
		undo.setSize(80,80);
		
		uiLayer.addActor(undo);
		undo.setPosition(uiLayer.getWidth()-undo.getWidth()-20, 20);
	//	bottomCenter.add(undo).width(80).height(80) .padRight(10);
		
		 tipsTable = new Table();
		tipsTable.defaults().width(100).height(70).pad(10);
		//nextTipButton = new L10nButton(g.getL10n(), "forward", getGame().getManager().getSkin(), "game");
		nextTipButton = new ImageButton( skin,"forward");
		nextTipButton.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				if(nextTipButton.isDisabled()){
					return;
				}
				

				
				Tips tips = getMeta().getTips();
				String tp = tips.asString();
				
				if(tp.length()<=stepCount){
					return;
				}
				
				char nextStep = Character.toLowerCase(tp.charAt(stepCount));
				
				Direction direction = null;
				for(Direction dir : Direction.values()){
					if(dir.name().charAt(0)==nextStep){
						direction = dir;
						break;
					}
				}
				
				if(direction==null){
					throw new IllegalStateException("direction==null");
				}
				
					ButtonEvent be = new ButtonEvent(direction.name(), Source.TIPS);
					uiLayer.fire(be);
			
					
			}
		});

		
		
	//	tipsTable.add(prevTipButton);
		tipsTable.add(nextTipButton);
		//nextTipButton.setVisible(false);
		tipsTable.setVisible(IN_TIPS);
		
		tipsTable.pack();
		uiLayer.addActor(tipsTable);
		Util.center(tipsTable);
		tipsTable.setY(20);
/*		undo.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				handleUndo((Button)event.getListenerActor());
				
			}
		});*/
		
		undo.addListener(new SmartListener(new Runnable() {
			
			@Override
			public void run() {
				handleUndo(undo);
			}
		}, new Runnable() {
			
			@Override
			public void run() {

				
				if(uiLayer.findActor("education-bubble")!=null){
					
				}
				else{
					Game2.this.reset(false,new ResetCallback() {
						
						@Override
						public void onResetComplete() {
						
							start(level);
							
						}
					});
				}
				
			
			}
		}, 1.5f));

		
		undo.setVisible(history._data.size()>0);		
		s.addListener(new EventListener() {
			
			@Override
			public boolean handle(Event event) {
				if(event instanceof ButtonEvent){
					ButtonEvent bEvent = (ButtonEvent)event;
					String button = bEvent.getButton();
					final Direction d = Direction.valueOf(button);
					final Preferences p = getGame().getPreferences();


					
					List<Yang> queue = new LinkedList<Yang>(getAll(Yang.class));
					Collections.sort(queue, new Comparator<Yang>() {

						@Override
						public int compare(Yang o1, Yang o2) {
							if(d == Direction.up){
								return Integer.valueOf(o2.getLocationY()).compareTo(Integer.valueOf(o1.getLocationY()));
							}
							else if(d == Direction.down){
								return Integer.valueOf(o1.getLocationY()).compareTo(Integer.valueOf(o2.getLocationY()));
							}
							else if(d == Direction.left){
								return Integer.valueOf(o1.getLocationX()).compareTo(Integer.valueOf(o2.getLocationX()));
							}
							else if(d == Direction.right){
								return Integer.valueOf(o2.getLocationX()).compareTo(Integer.valueOf(o1.getLocationX()));
							}
							return 0;
						}
					});
					

					TableSnapshot snapshot =  new TableSnapshot(Game2.this, Collections.unmodifiableCollection(getAll(BaseItem.class)), d);
  
					boolean step = false;
					List<Pair<Integer, Integer>> rottenCoors  = new LinkedList<Pair<Integer, Integer>>();
					for(Yang ball : queue){	
						Pair<Integer, Integer> coor = new Pair<Integer, Integer>(ball.getLocationX(), ball.getLocationY());
						boolean result = ball.move(Direction.valueOf(button));
						if(result){
							rottenCoors.add(coor);
						}
						step |= result;
					}
					
					if(step){
						g.getSoundManager().play(Sounds.YANG_FLING);
						

						history._data.addFirst(snapshot);
						if(history._data.size()>MAX_HISTORY_SIZE){
							history._data.pollLast();
						}
						

						undo.setVisible(history._data.size()>0);	
						nextTipButton.setDisabled(true);
						//undo.setDisabled(history._data.size()==0);
						
						stepCount++;
						
						
						if(bEvent.getSource()==Source.PLAYER){
							tipsTable.setVisible(false);
							IN_TIPS = false;
						}
						
						for(int i = uiLayer.getChildren().size -1 ; i>=0 ; i--){
							Actor a = uiLayer.getChildren().get(i);
							if(a.getName()!=null && a.getName().startsWith("education")){
								a.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.removeActor()));
								back.setVisible(true);
							}
						}
						
						
						if(stepCount==2 && (p.getBoolean(Scripts.education_step_cancel_done)==false)){
							Scripts.educationStepCancelling(Game2.this);
						}
						
						///////////////////////////////////////
						// END OF THE GAME SCENARIO /////
						///////////////////////////////////////
						if(stepCount == 2 && level == 120){
							Scripts.endOfGame(Game2.this);
							return true;
						}
					}					
					renderStep();
					
					if(getMeta().is_rotten()){
						for(Pair<Integer, Integer> pair : rottenCoors){
							BaseItem item = table.get(pair.getFirst(), pair.getSecond());
							BaseItem spot = table.getSpot(pair.getFirst(), pair.getSecond());
							if(item==null && spot==null){
								//wall(pair.getFirst(), pair.getSecond());
								BaseItem wall =  factories.get(Wall.class).create(pair.getFirst(), pair.getSecond(), Game2.this);
								gameLayer.addActor(wall);
								wall.toFront();
							}
						}
					}
					
					
					boolean allInSpots = true;
					for(Yang ball : getAll(Yang.class)){
						BaseItem yin = getTable().getSpot(ball.getLocationX(), ball.getLocationY());
						if(yin==null){
							allInSpots=false;
							break;
						}
					}
					
					if(allInSpots && !win && !TestGame.demoMode){
						
						walkWindow.clearActions();
						walkWindow.setY(uiLayer.getHeight());
/*						
						if(TestGame.test && level >= 15){
							 getGame().setScreen(getGame().getChooseLevelScreen());
							 return false;
						}
						*/
						
						win = true;
						tipsTable.setVisible(false);						
						controller.block();
						undo.setVisible(false);
						
						back.setVisible(false);
						back.addAction(Actions.sequence(Actions.delay(3), Actions.run(new Runnable() {
							
							@Override
							public void run() {
								back.setVisible(true);
								
							}
						})));
												
						p.putBoolean("is_done_"+level, true);
						int lastDone = p.getInteger("last_done");
						if(level>lastDone){
							p.putInteger("last_done", level);
						}
						
						String bestString = p.getString("best"+level, null);
						final int achived = GameHelper.calculateAchivement(getMeta(), stepCount);
						final boolean newBest = (bestString==null || stepCount<Integer.parseInt(bestString));
						if(newBest){
							p.putString("best"+level, Integer.toString(stepCount));
							p.putInteger("achivement"+level, achived);							
						}
						p.flush();
						
						final Window winWindow= new Window("", getGame().getManager().getSkin(), "win");
						winWindow.setName("winWindow");
						winWindow.defaults().pad(3);
						
						Label winLabel = new L10nLabel(g.getL10n(), "youWin", skin, "default");
						winLabel.setAlignment(Align.left, Align.center);
						winWindow.add(winLabel).colspan(3). row();
						
						
						final Table goals = new Table();
						goals.align(Align.center);
						goals.defaults().pad(5);					

						for(int i = 0; i<3; i++){
							Image ico = new Image(g.getManager().getAtlas().findRegion("yy"));
							ico.setName("ico"+i);
							ico.setSize(80, 80);
							goals.add(ico);
							
							if(i > achived-1){
								ico.getColor().a = 0.5f;
							}
						}
						goals.setName("goals");
						goals.pack();
						goals.setClip(false);
						winWindow.add(goals).colspan(3).row();
						
						Label steps = new L10nLabel(g.getL10n(),"stepCount", skin, "default", true, stepCount);
						winWindow.add(steps).colspan(3).row();

						///////// WIN DIALOG BUTTONS ///////
						{
							//float width = 40;
							//float height = 50;
							float pad = 20;
							float sidePad = 40;
							
							float mul = 1.2f;
							Button list = new ImageButton(skin, "list");
							winWindow.add(list).width(50*mul).height(40*mul).pad(pad).padLeft(sidePad).padRight(sidePad);
							list.addListener(new ClickListener(){
								public void clicked (InputEvent event, float x, float y) {
									event.getListenerActor().getParent().remove();
									
									if(level>60){
										getGame().setScreen(getGame().getChooseLevelScreen2());
									}
									else{
										getGame().setScreen(getGame().getChooseLevelScreen());
									}
																		
								}
							});
							
							
							//TextButton resetBtn = new L10nButton(g.getL10n(),"restart", skin, "reset");
							Button resetBtn = new ImageButton(skin, "replay");
							winWindow.add(resetBtn).width(45*mul).height(45*mul).pad(pad).padLeft(sidePad).padRight(sidePad);
							resetBtn.addListener(new ClickListener(){
								public void clicked (InputEvent event, float x, float y) {
									event.getListenerActor().getParent().remove();
									reset(false,new ResetCallback() {
										
										@Override
										public void onResetComplete() {
											start(level);
											
										}
									});
																		
								}
							});
							
							
						//	TextButton nextBtn = new L10nButton(g.getL10n(), "next", skin, "reset");
							Button nextBtn = new ImageButton(skin, "forward");
							winWindow.add(nextBtn).width(50*mul).height(40*mul).pad(pad).padLeft(sidePad).padRight(sidePad);
							nextBtn.addListener(new ClickListener(){
								public void clicked (InputEvent event, float x, float y) {
									if(level==60 || level==120){
										g.setScreen(g.getChooseListScreen());
									}
									
									else{
										event.getListenerActor().getParent().remove();
										reset(true, new ResetCallback() {
											
											@Override
											public void onResetComplete() {
												start(++level);
												
											}
										});
									}
									
								}
							});
						}
						
						winWindow.setMovable(false);
						winWindow.setClip(false);
						winWindow.setKeepWithinStage(false);
						winWindow.pack();
						
	
						
						uiLayer.addActor(winWindow);
						Util.center(winWindow);
						winWindow.setY(stage.getHeight()+10);

						
						Collection<Actor> winItems = new ArrayList<Actor>();
						winItems.addAll(getAll(Yang.class));
						winItems.addAll(getAll(Yin.class));
						final float swingDur = 0.1f;
						final int swingCount = 3;
						final float delay = 0.2f;
						for(Actor a : winItems){
							a.toFront();
							
							float width = a.getWidth();
							float height = a.getHeight();
							float deltaW = 0.1f*width;
							float deltaH = 0.1f*height;
							Action parallel = Actions.parallel(Actions.sequence(Actions.parallel(Actions.scaleTo(1.1f, 1.1f, swingDur, Interpolation.sineIn), Actions.moveBy(-deltaW/2, -deltaH/2, swingDur, Interpolation.sineIn)),Actions.parallel( Actions.scaleTo(1, 1, swingDur, Interpolation.sineOut),Actions.moveBy(deltaW/2, deltaH/2, swingDur, Interpolation.sineOut) ))
									);
							
							SpriterItem item = (SpriterItem) a;
							//item.getActions().clear();
							item.setGuesture(false);
							item.getPlayer().setTime(0);
							item.getPlayer().speed = 15;
							item.getPlayer().setAnimation("close");
							//a.addAction(Actions.sequence(Actions.delay(delay),Actions.repeat(swingCount,parallel) ));
						}

						if(achived==3){

		/*					fireBlueActor.toFront();
							fireRedActor.toFront();
							fireGreenActor.toFront();*/
							if(getMeta().get_difficulty()>2){
								uiLayer.addAction(WinScenario.starAction(Game2.this));
							}
							uiLayer.addAction(WinScenario.emitterActions(Game2.this, swingDur, delay, swingCount));
						}
						
						if(level==60){
							
							Scripts.endOfLight(Game2.this, new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Scripts.winMoveDown(winWindow, newBest, swingDur, swingCount, delay, Game2.this);
								}
								
							});
							
						}
						else{
							Scripts.winMoveDown(winWindow, newBest, swingDur, swingCount, delay, Game2.this);
						}
						
					
						
					}
					else{
						nextTipButton.addAction(Actions.sequence(Actions.delay(0.3f), Actions.run(new  Runnable() {
							public void run() {
								nextTipButton.setDisabled(false);
							}
						})));
						
					//	int g3t = getMeta().get_goal3threshold();						
/*						if(proposeCount==0 && (stepCount>=g3t+5 || System.currentTimeMillis()-timer>TestGame.proposeWalkthroughTimeout) && level > 1){
							Scripts.tryWalkthrough(Game2.this);
							timer= System.currentTimeMillis();
							proposeCount++;
						}
						if(proposeCount==1 && (stepCount>=g3t+25) && level > 1){
							Scripts.tryWalkthrough(Game2.this);
							timer= System.currentTimeMillis();
							proposeCount++;	
						}*/
						
/*						if(stepCount>=g3t+5+20*proposeCount && level> 1){
							Scripts.tryWalkthrough(Game2.this);
							timer= System.currentTimeMillis();
							proposeCount++;							
						}*/
						
					}
					
					List<Fall> orderedFalls= new LinkedList<Fall>(getAll(Fall.class));
					Collections.sort(orderedFalls, new Comparator<Fall>() {

						@Override
						public int compare(Fall o1, Fall o2) {
							return Integer.valueOf(o1.getLocationY()).compareTo(Integer.valueOf(o2.getLocationY()));
						}
					});
					
					List<Pair<Integer, Integer>> fallTargets = new LinkedList<Pair<Integer,Integer>>();
					for(Fall oFall : orderedFalls){
						fallTargets.add(new Pair<Integer, Integer>(oFall.getLocationX(), oFall.getLocationY()));
					}
					
					Iterator<Fall> fallIt = orderedFalls.iterator();
					for(Pair<Integer, Integer> target : fallTargets){
						Fall fall = fallIt.next();
						
						int c = 1;
						while(true){
							int x = target.getFirst();
							int y = target.getSecond() - c++;
							
							Pair<Integer, Integer > newTarget = new Pair<Integer, Integer>(x, y);
							
							if(table.get(x, y)==null && !fallTargets.contains(newTarget)){
								target.setSecond(y);
								c = 1;
							}
							else{
								if(fall.getLocationY()!=target.getSecond()){
									table.put(null, fall.getLocationX(), fall.getLocationY());
									table.put(fall, target.getFirst(),target.getSecond());
								}
								
								break;
							}
						}
					}
					
					if(TestGame.devmode){
						StringBuilder sb = new StringBuilder();
						List<TableSnapshot> coll = new LinkedList<TableSnapshot>(history.getData());
						Collections.reverse(coll);
						for(TableSnapshot ss : coll){

							Direction dir = ss._move;
							char ch = dir.name().charAt(0);
							sb.append(Character.toUpperCase(ch));
						}
						
						dotips.setText(sb.toString());
					}

					return true;
				}
				return false;
			}
		});
		
		
	fillBg();
	
		

		Actor darkFrame = new Actor();
		darkFrame.setName("darkFrame");
		s.getRoot().addActorBefore(uiLayer, darkFrame);
		
		float scale = 1;
		gameLayer.addAction(Actions.scaleTo(scale,scale));
		backgroundLayer.addAction(Actions.scaleTo(scale,scale));
		
		gravity = new Vector2(0, -10);
		world = new World(gravity, false);
		world.setContactFilter(new ContactFilter() {
			
			@Override
			public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
				return false;
	/*			Body a =fixtureA.getBody();
				Body b = fixtureB.getBody();
				
				BaseItem itemA =  (BaseItem) a.getUserData();
				BaseItem itemB = (BaseItem) b.getUserData();
				
				Set<Class<? extends BaseItem>> clss = new HashSet<Class<? extends BaseItem>>();
				clss.add(itemA.getClass());
				clss.add(itemB.getClass());
				if(clss.contains(Yin.class) && clss.size()>1){
					return false;
				}
				if(clss.contains(Yang.class) && clss.size()>1){
					return false;
				}
				
				
				
				return true;*/
			}
		});
		
		RayHandler.setGammaCorrection(true);
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(world);
		rayHandler.setCulling(false);
		
		//final ConeLight cone = new ConeLight(rayHandler, 32, Color.WHITE, 700, 0,700, coneDirection, 60);
		//PointLight light = new PointLight(rayHandler, 32, Color.WHITE, 300, 0,0);
		
		walkWindow = new Window("", g.getManager().getSkin());
		walkWindow.setBackground("frame_small");
		Label walkText = new L10nLabel(g.getL10n(), "propose_walkthrough", g.getManager().getSkin());
		ImageButton hand = new ImageButton(g.getManager().getSkin(), "footstep");
		walkWindow.pad(5);
		walkWindow.add(walkText).padRight(20);
		walkWindow.add(hand).width(60).height(60) .align(Align.center);		
		hand.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {

				Scripts.turnonWalkthroughDialog(Game2.this);

			}
		});
		walkWindow.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
		
				Scripts.turnonWalkthroughDialog(Game2.this);

			}
		});
		walkWindow.pack();
		uiLayer.addActor(walkWindow);
		Util.center(walkWindow);
		walkWindow.setY(uiLayer.getHeight());
	}
	
	RayHandler rayHandler;
	PointLight light ;
	boolean lights;
	
	@Override
	public void render (float delta){
		
		if(lights){
			uiLayer.setVisible(false);
			
			//LightActor a = uiLayer.findActor("la");

			
		}
	
		
		super.render(delta);		
		world.step(Gdx.graphics.getDeltaTime(), 5, 5);
		
		for(int i = 0 ; i < uiLayer.getChildren().size; i++){
			Actor a = uiLayer.getChildren().get(i);
			if(a.getName()!=null && a.getName().contains("effect")){
				ParticleEffectActor effect = (ParticleEffectActor)a;
				if(effect.getEffect().isComplete()){
					effect.getEffect().free();
					effect.remove();
				}
			}
		}
		
	
		if(lights){
			LightActor r =uiLayer.findActor("lr");
			LightActor c = uiLayer.findActor("lc");
			LightActor l =uiLayer.findActor("ll");
			
			if(r!=null)
				r.getLight().setActive(undo.isVisible());
			if(l!=null)
				l.getLight().setActive(back.isVisible());
			if(c!=null)
				c.getLight().setActive(tipsTable.isVisible());
			
			
			rayHandler.setCombinedMatrix((OrthographicCamera) getStage().getCamera());
			rayHandler.updateAndRender();
			uiLayer.setVisible(true);
			gameLayer.setVisible(false);
			backgroundLayer.setVisible(false);
			
			
	
			stage.draw();
			gameLayer.setVisible(true);
			backgroundLayer.setVisible(true);
		}
		
		//System.out.println(light.getPosition());
		
		
/*		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
		    effect.draw(batch, delta);
		    if (effect.isComplete()) {
		        effect.free();
		        effects.removeIndex(i);
		    }
		}*/

/*		if(Gdx.input.isKeyPressed(Keys.S) || TestGame.demoMode && (count % 2 == 0)){
			ScreenShot worker = new ScreenShot();
			worker.prepare();			// grab screenshot
			executor.execute(worker);
			
		}*/
		
		count ++;
	}
	
	float coneDirection = -45;
	
	int count = 1;

	Vector2 gravity;
	World world;
	public static float B2D_SCALE = 100f;

	void fillBg(){
		for(int i  = 0; i<40; i++){
			for(int j  = 0; j<40; j++){
				Image img = new Image(game.getManager().getAtlas().findRegion("back"));
				backgroundLayer.addActor(img);
				img.setBounds(i*BaseItem.ITEM_WIDTH, j*BaseItem.ITEM_HEIGHT, BaseItem.ITEM_WIDTH, BaseItem.ITEM_HEIGHT);
				
			}
		}
	}

	 Action createAbandonSequence(){
		float abandonDuration = 0.7f;
		Random rnd = new Random(System.currentTimeMillis());
		Action abandon;
		if(rnd.nextBoolean()){
			abandon = Actions.rotateBy(-90, abandonDuration);
		}
		else{
			abandon = Actions.moveTo(0, getStage().getHeight(), abandonDuration);
		}
		
		return Actions.sequence(abandon, Actions.run(new Runnable(){

			@Override
			public void run() {
			//	reset.setDisabled(false);
			}
			
		}), Actions.removeActor());

	}
	 
	 
	 static class AbandonTrigger extends TriggerAction{

		public AbandonTrigger(Action runnable, Predicate predicate) {
			super(runnable, predicate);
		}
		 
	 }
	 
	 static interface ResetCallback{
		 void onResetComplete();
	 }
	 
	 
	 void setPhysics(final BaseItem item, Random rnd, boolean standStill){
	//	 item.setOrigin(item.getWidth()/2, item.getHeight()/2);
			BodyDef bdef = new BodyDef();
			bdef.position.set(new Vector2(item.getX()/B2D_SCALE, item.getY()/B2D_SCALE));
			bdef.type = BodyType.DynamicBody;
			
			if(!standStill){
			bdef.angularVelocity = (rnd.nextFloat()-0.5f) * 4;
			float linearVel = 1000f;
			bdef.linearVelocity.set(new Vector2((rnd.nextFloat()-0.5f)*linearVel/B2D_SCALE, (rnd.nextFloat()-0.5f)*linearVel/B2D_SCALE)) ;
			}
			
			
			bdef.gravityScale = 2.5f;
			Body body = world.createBody(bdef);
			
			PolygonShape shape = new PolygonShape();
			List<Float> ff = new LinkedList<Float>();
			ff.add(0f);
			ff.add(0f);
			ff.add(item.getWidth());
			ff.add(0f);
			ff.add(item.getWidth());
			ff.add(item.getHeight());
			ff.add(0f);
			ff.add(item.getHeight());
			
			float[] v = new float[ff.size()];
			for(int i = 0 ; i<ff.size() ; i++){
				v[i] = ff.get(i) / B2D_SCALE;
			}
			shape.set(v);
			body.createFixture(shape, 1);
			
			item.setBody(body);
			body.setUserData(item);
			item.addAction(new AbandonTrigger(Actions.run(new Runnable() {
				
				@Override
				public void run() {
					System.out.println(item.getClass().getName()+" removed");
					world.destroyBody(item.getBody());
					item.getBody().setUserData(null);
					item.remove();
					
				}
			}), new Predicate() {
				
				@Override
				public boolean accept() {
					
					if(item.getY()<-100){
						
						return true;
					}
					
					return false;
				}
			}));
	 }
	 

	
	void reset(final boolean staight, final ResetCallback clb){
		//starPool.
		tipsTable.setVisible(false);
		IN_TIPS = false;
		
		Actor e = uiLayer.findActor("end-of-game");
		if(e!=null){
			e.remove();
		}
		
		Actor ww = getStage().getRoot().findActor("winWindow");
		if(ww!=null){
			ww.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.removeActor()));
		}
		
		for(int i = 0; i<uiLayer.getChildren().size; i++){
			Actor a = uiLayer.getChildren().get(i);
			if(a.getName()!=null && a.getName().contains("effect")){
				ParticleEffectActor actor = (ParticleEffectActor) a;
				actor.getEffect().reset();
				actor.getEffect().free();
				actor.stop();
				actor.remove();
			}
		}
		
		List<Action> actionsToRm = new LinkedList<Action>();
		Iterator<Action> actionIt = uiLayer.getActions().iterator();
		while(actionIt.hasNext()){
			Action a = actionIt.next();
			if(a instanceof EmitterAction){
				a.reset();
				actionsToRm.add(a);
				
			}
		}
		for(Action a : actionsToRm){
			uiLayer.removeAction(a);
		}
		

	//	this.lights = false;
		
	/*	starActor.stop();
		fireBlueActor.stop();
		fireGreenActor.stop();
		fireRedActor.stop();*/
		
		//uiLayer.rem
	
		//System.gc();
		if(!staight){
	
				//reset.setDisabled(true);
				undo.setVisible(false);
				
		/*		for(BaseItem item : getAll(BaseItem.class)){
					ItemFactory factory = factories.get(item.getClass());
					factory.destroy(item, this);
				}*/
				
				final Random rnd = new Random(System.currentTimeMillis());
				final Collection<BaseItem> yy = new LinkedList<BaseItem>();
				yy.addAll(getAll(Yin.class));
				yy.addAll(getAll(Yang.class));
				
				Collection<BaseItem> bb = new LinkedList<BaseItem>();
				bb.addAll(getAll(Box.class));
				bb.addAll(getAll(Wall.class));
				bb.addAll(getAll(Ground.class));
				bb.addAll(getAll(Metal.class));
				
				Collection<BaseItem> all = new LinkedList<BaseItem>(yy);
				all.addAll(bb);
				
				for(BaseItem item : all){
					ItemFactory factory = factories.get(item.getClass());
					factory.destroy(item, this);
				}
				
				for(final BaseItem item : bb){

					setPhysics(item, rnd, false);
					
				}
				

				
				
				
				gameLayer.addAction(Actions.sequence(Actions.delay(AWAIT_YY_FALL), Actions.run(new Runnable(){

					@Override
					public void run() {
					
						for(final BaseItem item : yy){
							setPhysics(item, rnd, YY_STAND_STILL_WHEN_FALL);
						}
						
						List<LightActor> lights = new LinkedList<LightActor>();
						for(Actor a : uiLayer.getChildren()){
							if(a instanceof LightActor){
								lights.add((LightActor) a);
							}
						}
						
						for(final LightActor la : lights){
							la.addAction(Util.trigger(Actions.run(new  Runnable() {
								public void run() {
									la.getLight().remove();
									la.getLight().dispose();
									la.remove();
								}
							}), new Predicate() {
								
								@Override
								public boolean accept() {
									
									return gameLayer.getChildren().size==0;
								}
							}));
						
						}
						
						gameLayer.addAction(Util.trigger(Actions.run(new Runnable() {
							
							@Override
							public void run() {
								

								
								System.out.println("ALL REMOVED, invoke callback");
								
							//	reset.setDisabled(false);
								
								if(clb!=null){
									clb.onResetComplete();
								}
								
							}
						}), new Predicate() {
							
							@Override
							public boolean accept() {
								
								return gameLayer.getChildren().size==0;
								
							}
						}));
					}
					
				})));
			


		}
		else{
			
			
			List<LightActor> lights = new LinkedList<LightActor>();
			for(Actor a : uiLayer.getChildren()){
				if(a instanceof LightActor){
					lights.add((LightActor) a);
				}
			}
			
			for(LightActor la : lights){
				la.getLight().remove();
				la.getLight().dispose();
				la.remove();
			
			}
			
			
			for(BaseItem item : getAll(BaseItem.class)){
				ItemFactory factory = factories.get(item.getClass());
				factory.destroy(item, this);
				
				item.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.removeActor()));
			}
			
			gameLayer.addAction(Util.trigger(Actions.run(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("ALL REMOVED, invoke callback");
					
				//	reset.setDisabled(false);
					
					
					if(clb!=null){
						clb.onResetComplete();
					}
					
				}
			}), new Predicate() {
				
				@Override
				public boolean accept() {
				//	gameLayer.
					return gameLayer.getChildren().size==0;
					
				}
			}));
			

		}
		
		
		stepCount=0;
		renderStep();
	
		win = false;

		
		//fillBg();
		
		controller.unblock();


	}
	

	
	
	

	



	public void start(final int lev){

		
		
		timer = System.currentTimeMillis();
		proposeCount = 0;
		
		Actor e = uiLayer.findActor("end-of-game");
		if(e!=null){
			e.remove();
		}
		
		if(gameLayer!=null){
			gameLayer.remove();
			gameLayer = null;
		}
		
		 gameLayer = new Group();
		gameLayer.setName("gamelayer");
		//tryWalkAction(gameLayer);
		
		gameLayer.setBounds(0, 0, stage.getWidth()*5,stage.getHeight()*5);
		stage.getRoot().addActorAfter(backgroundLayer, gameLayer);
		
		FileHandle next = Files.level(lev);
		if(!next.exists()){
			TestGame tg = getGame();
			tg.setScreen(tg.getChooseLevelScreen());
			return;
		}
		
		gameLayer.getColor().a = 0;
		history.clear();
		
		
		
		
		Actor darkFrame = stage.getRoot().findActor("darkFrame");
		if(darkFrame!=null){
			darkFrame.addAction(Actions.alpha(0));
		}
		read2(Files.level(lev), new Runnable() {
			
			@Override
			public void run() {
				
				//undo.setDisabled(true);
				undo.setVisible(false);
				Actor ww = getStage().getRoot().findActor("winWindow");
				if(ww!=null){
					ww.remove();
				}
				
				stepCount = 0;
				renderStep();
				
				//education hack
				if(!getGame().getSession().isEducation()){ 
					controller.unblock();
				}
				win = false;
			}
		});
		checkConsistent();
		
		Preferences p = getGame().getPreferences();
		p.putBoolean(Values.played, true);
		p.flush();
		String bestString = p.getString("best"+lev, null);
		
		if(bestString!=null){
			personalBestLabel2.update(bestString);
			personalBestLabel2.setVisible(true);
		}
		else{
			personalBestLabel2.setVisible(false);
		}

		
		level = lev;
		levelLabel.setText(Integer.toString(level));
		
		gameLayer.addAction(Actions.sequence( Actions.fadeIn(0.3f)));
		
		if(TestGame.devmode){
			dotips.setText("");
		}
		
		
		
		boolean education_hand_done = p.getBoolean(Scripts.education_hand_done, false);
		if(!education_hand_done){
			//Scripts.educationHand(this);
			Scripts.education(this);
		}
		
		lights = level == 36 ;
		
		if(lights){
	
			
			Light light = new PointLight(rayHandler, 32, Color.WHITE, 200, 100,0);
			Light center = new PointLight(rayHandler, 32, Color.WHITE, 200, 100,0);
			Light left = new PointLight(rayHandler, 32, Color.WHITE, 200, 100,0);
			Light right = new PointLight(rayHandler, 32, Color.WHITE, 200, 100,0);

			final LightActor la = new LightActor(light);
			la.setName("la");
			final LightActor ll = new LightActor(left);
			ll.setName("ll");
			final LightActor lc = new LightActor(center);
			lc.setName("lc");

			ll.setPosition(back.getX(), back.getY());
			final LightActor lr = new LightActor(right);
			lr.setName("lr");
			lr.setPosition(undo.getX()+50, undo.getY());
			lc.setPosition(tipsTable.getX()+50, tipsTable.getY());;

			uiLayer.addActor(la);
			uiLayer.addActor(ll);
			uiLayer.addActor(lr);
			uiLayer.addActor(lc);
			
			la.addAction(Actions.forever(Actions.run(new Runnable() {
				
				@Override
				public void run() {
					 //Iterator<Yang> yangIt =  getAll(Yang.class).iterator();
					Iterator<Actor> iter =  gameLayer.getChildren().iterator();
					while(iter.hasNext()){
						Actor a = iter.next();
						if(a instanceof Yang){
								 float x = a.getX();
								 float y = a.getY();
								 la.setPosition(x/2,y/2);
								 return;
						}
					}
					 // yangIt.next();

					
					
				}
			})));
		}
		else{
			uiLayer.setVisible(true);
		}
		
		for(int i = 0; i<uiLayer.getChildren().size; i++){
			Actor a = uiLayer.getChildren().get(i);
			if(a.getName()!=null && a.getName().contains("effect")){
				ParticleEffectActor actor = (ParticleEffectActor) a;
				actor.getEffect().reset();
				actor.getEffect().free();
				actor.stop();
				actor.remove();
			}
		}
		
		p.putInteger("last_played",level);
		p.flush();
		
		
		game.getSession().setLastLevel(level);
		
	
	//	if(!IN_TIPS){
			tipsTable.setVisible(false);
	//	}
			walkWindow.clearActions();
			walkWindow.setY(uiLayer.getHeight());
			
		
				walkWindow.addAction(Actions.forever(Actions.sequence( Actions.delay(com.me.test.TestGame.walkthroughDelay),Actions.moveBy(0, -walkWindow.getHeight(), 1), Actions.delay(com.me.test.TestGame.walkthroughShowTime), Actions.moveBy(0, walkWindow.getHeight(), 1 ))));
			

	}
	

	
	
	boolean musicListenerRegistered;
	
	@Override
	public void show() {
	//	Gdx.app.log("java heap", "jh:"+Gdx.app.getJavaHeap());
	//	Gdx.app.log("native heap","nh:"+Gdx.app.getJavaHeap());

		
		if(controller instanceof GestureController){
			InputMultiplexer mp = new InputMultiplexer(stage,((GestureController) controller).getDetector());		
			Gdx.input.setInputProcessor(mp);		
		}
		else{
			Gdx.input.setInputProcessor(stage);
		}
		
		if( getGame().getSettings().isMusicEnabled()){			
			final Music music = getGame().getManager().getMusic();
			if(!music.isPlaying()){
				music.setVolume( getGame().getSettings().getMusicVolume());
				music.setLooping(true);	
				music.play();			
			}		
		}
		
		if(!musicListenerRegistered){
			
			getGame().getSettings().registerListener(new Settings.SettingsAdapter(){
				final Music music = getGame().getManager().getMusic();				
				public void onMusicEnabledChange(boolean enabled){
					Music m = music;
					if( enabled){
						if(!m.isPlaying()){
							m.play();
						}
					}
					else{
						m.stop();
					}
				}
				
			});
			musicListenerRegistered = true;
		}
		
		getGame().getSession().setInGame(true);
		
		editTips.setVisible(TestGame.devmode);
		
		 update();
	//	getGame().getNative().showView(100500);
		//getGame().getNative().showAdd(100, true);
	}
	

	
	private ExecutorService executor;

	
	
	Controller createKeyboardController(){
		Keyboard kb = new Keyboard(this);

		return kb;
	}
	
	Controller createGestureController(){
		GestureController gc = new GestureController();

		return gc;
	}
	
	void checkConsistent(){
		String message = null;
		Collection<Yang> yangs = getAll(Yang.class);
		Collection<Yin> yins = getAll(Yin.class);
		if(yangs.size()==0){
			message = "level inconsitent: no balls";
		}
		else if(yangs.size()!=yins.size()){
			message =  "level inconsitent: amount of balls not matches to amount of spots";
		}
		
		if(message!=null){
			Gdx.app.log(getClass().getName(), message);
			throw new IllegalStateException(message);
		}
	}


	@Override
	public String getName() {
		return "game2";
	}

	
	void handleUndo(Button target){

		if(target.isDisabled()){
			return ;
		}
	
			TableSnapshot snapshot = history._data.pollFirst();
			if(snapshot!=null){

				for(BaseItem it: getAll(BaseItem.class)){
					ItemFactory factory = getFactories().get(it.getClass());
					factory.destroy(it, Game2.this);
					it.addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.removeActor()));						
				}
				
				for(ItemData itemData : snapshot._items){
					BaseItem historyItem = factories.get(itemData.cls).create(itemData.locationX, itemData.locationY, Game2.this);
					gameLayer.addActor(historyItem);
				}
				
				for(Yang b : getAll(Yang.class)){
					b.toFront();
				}
				
				for(BaseItem box : getAll(Box.class)){
					box.toFront();
				}
				
				
				stepCount--;
				renderStep();
				
				//undo.setDisabled(history._data.size()==0);
				target.setVisible(history._data.size()>0);
			}

		
		
			for(int i = uiLayer.getChildren().size -1 ; i>=0 ; i--){
				Actor a = uiLayer.getChildren().get(i);
				if(a.getName()!=null && a.getName().startsWith("education")){
					a.remove();
					back.setVisible(true);
				}
			}
	
	}
	
	
	InputListener onButtonExit(){
		return new InputListener(){
			public boolean keyDown (InputEvent event, int keycode) {
				
				if(keycode == Keys.BACK  || keycode==Keys.ESCAPE){
					Actor bubble = uiLayer.findActor("education-bubble");
					if(bubble==null || !bubble.isVisible()){					
						getGame().setScreen(getSubmenu());
					}
				}
				
				return false;
			}
		};
	}

	@Override
	public void update() {
		Session sess = getGame().getSession();
		ownedYangsLabel.setText(Integer.toString(sess.getOwnerYangs()));
		
	}
}
