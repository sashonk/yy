package com.me.test.game2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.me.test.Pair;
import com.me.test.ParticleEffectActor;
import com.me.test.TestGame;
import com.me.test.Util;
import com.me.test.Util.TriggerAction.Predicate;
import com.me.test.game2.BaseItem.Direction;
import com.me.test.game2.Game2.ResetCallback;
import com.me.test.game2.ItemContainer.LevelMeta.Tips;
import com.me.test.game2.item.Yang;
import com.me.test.game2.item.Yin;
import com.me.test.game2.ui.ButtonEvent;

public class GoogleDemoScript {
	
	static class FirePositionTriple{
		 FirePositionTriple(Pair<Float, Float> p1,Pair<Float, Float> p2, Pair<Float, Float> p3){
			 _p1 = p1;
			 _p2 = p2;
			 _p3 = p3;
		 }
		 
		Pair<Float, Float> _p1;
		Pair<Float, Float> _p2;
		Pair<Float, Float> _p3;
	}
	
	static List<FirePositionTriple> firePositions = Collections.unmodifiableList( Arrays.asList(new FirePositionTriple(new Pair<Float, Float>(300f, 200f),new Pair<Float, Float>(700f, 400f),new Pair<Float, Float>(500f, 800f)),
			 new FirePositionTriple(new Pair<Float, Float>(200f, 500f),new Pair<Float, Float>(500f, 200f),new Pair<Float, Float>(750f, 500f)),
			 new FirePositionTriple(new Pair<Float, Float>(500f, 450f),new Pair<Float, Float>(700f,750f),new Pair<Float, Float>(200f, 200f))
			 ));
	
	private static boolean haveLost = false;
	
	static List<Integer> levelsForDemo = Collections.unmodifiableList(Arrays.asList(1,9,60,92,87)) ;
	//List<Integer> levelsForDemo = Collections.unmodifiableList(Arrays.asList(35)) ;

	static LinkedList<Integer> remainedLevels ;
	
	public static void demo(Game2 game, final int lev){
		//uiLayer.setVisible(false);
		
		
		
		remainedLevels = new LinkedList<Integer>(levelsForDemo);
		 
		if(lev> 50){
			game.level = 1;
			demo(game,1);
			return;
		}
		
		FileHandle next = Files.level(lev);
		if(!next.exists()){
			TestGame tg = game.getGame();
			tg.setScreen(tg.getChooseLevelScreen());
			return;
		}

		game.gameLayer.getColor().a = 0;
		
		
		int nextLevel = remainedLevels != null ? remainedLevels.pollFirst() : lev;

		
		game.read2(Files.level(nextLevel), demoRunnable(game,nextLevel));
		//checkConsistent();
		
		Preferences p = game.getGame().getPreferences();
		String bestString = p.getString("best"+lev, null);
		
		if(bestString!=null){
			game.personalBestLabel2.update(bestString);
			game.personalBestLabel2.setVisible(true);
			//personalBestLabel2.setText(bestString);
		
		}
		else{
			game.personalBestLabel2.setVisible(false);
		}

		
		game.level = lev;
	}

	public static Runnable demoRunnable(final Game2 game,final int lev){
		return new Runnable() {
			
			@Override
			public void run() {
				
				for(Actor a : game.uiLayer.getChildren()){
					a.setVisible(false);
				}
				
				Collection<BaseItem> items = game.getAll(BaseItem.class);
				for(BaseItem item : items){
					item.getColor().a = 0;
				}
				game.gameLayer.getColor().a = 1;
				
				float delay = 0;
				for(BaseItem item : items){
					item.addAction(Actions.sequence(Actions.delay(delay), Actions.fadeIn(0.3f)));
					delay+=0.01f;
				}
				
				game.gameLayer.addAction(Util.trigger(Actions.run(new Runnable() {
					
					@Override
					public void run() {
						
						
						Tips tips =  game.getMeta().getTips();
						 String tp = tips.asString();
					
						if(lev==22 && !haveLost){
							tp = "DLURUURDL";
							haveLost= true;
						}
						
						 final String moves = tp;
						final float yinMoveDuration =  0.25f;
						float delay = 0;
						
						for(int i = 0 ; i<tp.length(); i++){
							char ch = Character.toLowerCase( tp.charAt(i));
							
							Direction direction = null;
							for(Direction dir : Direction.values()){
								if(dir.name().charAt(0)==ch){
									direction = dir;
									break;
								}
							}
							
							final Direction dd = direction;
							
							game.uiLayer.addAction(Actions.sequence(Actions.delay(delay), Actions.run(new Runnable() {
								
								@Override
								public void run() {
									ButtonEvent event = new ButtonEvent(dd.name(), ButtonEvent.Source.DEMO);
									game.uiLayer.fire(event);
									
								}
							})));
							
							delay+=yinMoveDuration;
							//ButtonEvent event = new ButtonEvent(direction.name());
							//uiLayer.fire(event);
						}
						

						
						game.gameLayer.addAction(Util.trigger(Actions.run(new Runnable() {
							
							@Override
							public void run() {
								
								Collection<Actor> winItems = new ArrayList<Actor>();
								winItems.addAll(game.getAll(Yang.class));
								winItems.addAll(game.getAll(Yin.class));
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
									a.addAction(Actions.sequence(Actions.delay(delay),Actions.repeat(swingCount,parallel) ));
								}
								
								final Window demoWin = new Window("",game. getGame().getManager().getSkin(), "win");
								final Table goals = new Table();
								goals.pad(15);
								goals.align(Align.center);
								goals.defaults().pad(5);		
								


								 

								 
								 int achieve =  new Random(System.currentTimeMillis()).nextInt(5)+1;
								if(  lev ==5 ){
									achieve = 2;
									 
								}

								else if (lev == 22 && moves.length()>5){
									achieve = 1;
								}
								else if(lev == 1 || lev == 9 || lev == 56 || lev == 22){
									achieve = 3;
								}
								else if(lev == 60  || lev == 92 || lev ==35){
									achieve = 5;
								}

								else if(lev==51 || lev==  80 || lev== 100 || lev == 72 || lev==30){
									achieve = 4;
								}
								
								
								final Integer nextLevel = (achieve == 1) ? Integer.valueOf(lev) : (levelsForDemo!=null ? remainedLevels.pollFirst() : Integer.valueOf(lev+1)); 

								final boolean fireCondition = achieve > 4;
								final float cleanupTimeout = nextLevel == null ? 60 :  (fireCondition ? 6 : 3);
								final float fadeTimeout = 20;
								final float windowMoveTimeout = fireCondition ? 3 : 1;
								final float labelModeTimeout = fireCondition ? 3.5f : 1.5f;
								final FirePositionTriple trio = firePositions.get(new Random(System.currentTimeMillis()).nextInt(3)) ;
								final float labelSwingDelay = 5f;
								
								if(fireCondition){
									
									if(lev==35)
									game.uiLayer.addAction(Actions.sequence(Actions.delay(3f), Actions.run(new Runnable() {
										
										@Override
										public void run() {
											//starActor.start();
											PooledEffect starEff= game.starPool.obtain();
											starEff.reset();
											ParticleEffectActor a = new ParticleEffectActor(starEff);
											game.uiLayer.addActor(a);
											a.toBack();
											a.setName("effect-"+System.currentTimeMillis());
											Util.center(a);
											//effects.add(starEff);
											a.start();
										}
									})));
									
									
									game.uiLayer.addAction(Actions.sequence(Actions.delay(2*swingDur* swingCount+delay), Actions.run(new Runnable() {
										
										@Override
										public void run() {
											//fireRedActor.start();
											PooledEffect starEff= game.redPool.obtain();
											ParticleEffectActor a = new ParticleEffectActor(starEff);
											game.uiLayer.addActor(a);
											a.toFront();
											a.setName("effect-"+System.currentTimeMillis());
											a.setPosition(trio._p1.getFirst(), trio._p1.getSecond());
											//effects.add(starEff);
											a.start();
											
										}
									}) ,Actions.delay(0.3f), Actions.run(new Runnable() {
										
										@Override
										public void run() {
											PooledEffect starEff=game. greenPool.obtain();
											ParticleEffectActor a = new ParticleEffectActor(starEff);
											game.uiLayer.addActor(a);
											a.toFront();
											a.setName("effect-"+System.currentTimeMillis());
											a.setPosition(trio._p2.getFirst(), trio._p2.getSecond());
											//effects.add(starEff);
											a.start();
											
										}
									}), Actions.delay(0.3f), Actions.run(new Runnable() {
										
										@Override
										public void run() {
											PooledEffect starEff= game.bluePool.obtain();
											ParticleEffectActor a = new ParticleEffectActor(starEff);
											game.uiLayer.addActor(a);
											a.toFront();
											a.setName("effect-"+System.currentTimeMillis());
											a.setPosition(trio._p3.getFirst(), trio._p3.getSecond());
											//effects.add(starEff);
											a.start();
											
										}
									})));
								}
								
								
								Map<Integer, String> encouragings = new HashMap<Integer, String>();
								encouragings.put(1, "Poor");
								encouragings.put(2, "Not Bad");
								encouragings.put(3, "Good!");
								encouragings.put(4, "Perfect!");
								encouragings.put(5, "MASTERMIND!");
								
								
								Label goalLabel = new Label(moves.length()+" moves", game.getGame().getManager().getSkin());
								goals.add(goalLabel).colspan(achieve).row();

								for(int i = 0; i<achieve; i++){
									Image ico = new Image(game.getGame().getManager().getAtlas().findRegion("yy"));
									ico.setName("ico"+i);
									ico.setSize(80, 80);
									goals.add(ico);
								}
								goals.setName("goals");
								goals.pack();
								goals.setClip(false);
								demoWin.add(goals).colspan(2).row();
								demoWin.setMovable(false);
								demoWin.setClip(false);
								demoWin.setKeepWithinStage(false);
								demoWin.pack();
								Action winMoveDown = Actions.moveTo( game.getStage().getWidth()/2-demoWin.getWidth()/2,game. getStage().getHeight()/2-demoWin.getHeight()/2, 1f, Interpolation.elasticOut);
								demoWin.addAction(Actions.sequence(Actions.delay(windowMoveTimeout),winMoveDown));
			
								final Label encouragingLabel = new Label(encouragings.get(achieve),game. getGame().getManager().getSkin(), "encourage");
								final Group encouragingGroup = new Group();
								
								encouragingGroup.addActor(encouragingLabel);
								encouragingGroup.setSize(encouragingLabel.getWidth(), encouragingLabel.getHeight());
								Action labelMoveDown = Actions.moveTo( game.getStage().getWidth()/2-encouragingLabel.getWidth()/2, game.getStage().getHeight()/2-demoWin.getHeight()/2+200, 1f, Interpolation.elasticOut);
							//	Action labelShake = Actions.forever(Actions.sequence(Actions.delay(5), Actions.moveBy(0, 50, 0.3f, Interpolation.swingIn),Actions.moveBy(0, -50, 0.6f, Interpolation.swingOut)));
								float width = encouragingLabel.getWidth();
								float height = encouragingLabel.getHeight();
								float deltaW = 0.1f*width;
								float deltaH = 0.1f*height;
								Action parallel = Actions.parallel(Actions.sequence(Actions.parallel(Actions.scaleTo(1.1f, 1.1f, swingDur, Interpolation.sineIn), Actions.moveBy(-deltaW/2, -deltaH/2, swingDur, Interpolation.sineIn)),Actions.parallel( Actions.scaleTo(1, 1, swingDur, Interpolation.sineOut),Actions.moveBy(deltaW/2, deltaH/2, swingDur, Interpolation.sineOut) ))
										);
								
								Action labelAction = Actions.sequence(Actions.repeat(3,Actions.sequence(Actions.delay(labelSwingDelay),Actions.repeat(swingCount,parallel)) ), Actions.delay(5), Actions.moveBy(0, -1000, 1, Interpolation.swingIn), Actions.removeActor());
								encouragingGroup.addAction(labelAction);
								
								
								//encouragingLabel.addAction(labelShake);

								game.getStage().addActor(encouragingGroup);
								Util.center(encouragingGroup);
								encouragingGroup.setY(1000);
								encouragingGroup.addAction(Actions.sequence(Actions.delay(labelModeTimeout), labelMoveDown));
								
								game.getStage().getRoot().addActorBefore(game.uiLayer,demoWin);
								Util.center(demoWin);
								demoWin.setY(game.getStage().getHeight()+10);

								
								if(nextLevel==null){
									 Pixmap pmap = new Pixmap(1, 1, Format.RGBA8888);
										pmap.setColor(Color.BLACK.cpy());
										pmap.fill();
										
										final Image fade = new Image(new Texture(pmap));
										game.getStage().addActor(fade);
										fade.toFront();
										fade.setBounds(0, 0, game.getStage().getWidth(), game.getStage().getHeight());
										fade.getColor().a = 0;
										
										fade.addAction(Actions.sequence(Actions.delay(fadeTimeout), Actions.alpha(1, 3f)));
										
										fade.addAction(Util.trigger(Actions.run(new Runnable() {
											
											@Override
											public void run() {
												game.getGame().setScreen(game.getGame().getMenuScreen());
											}
										}), new Predicate() {
											
											@Override
											public boolean accept() {
												if(fade.getColor().a == 1){
													return true;
												}
												return false;
											}
										}));
								}
								
								final int ach = achieve;																								
								game.getStage().addAction(Actions.sequence(Actions.delay(cleanupTimeout), Actions.run(new Runnable() {
									
									@Override
									public void run() {
										encouragingGroup.remove();
										demoWin.remove();
										game.reset(ach>1, new ResetCallback() {
											
											@Override
											public void onResetComplete() {
												game.gameLayer.getColor().a = 0;
												if(nextLevel!=null){
													game.read2(Files.level(nextLevel), demoRunnable(game,nextLevel));
												}
												else{
													
													game.getGame().setScreen(game.getGame().getMenuScreen());
													
												
													
													
												}
												
											}
										});
										
									}
								})));

								
							}
						}), new Predicate() {
							
							@Override
							public boolean accept() {
								if(game.uiLayer.getActions().size==0){
									return true;
								}
								
								return false;
							}
						}));
						
						
/*						gameLayer.addAction(Actions.sequence(Actions.delay(DEMO_DELAY), Actions.run(new Runnable() {
							
							@Override
							public void run() {
								reset(false, new ResetCallback() {
									
									@Override
									public void onResetComplete() {
										gameLayer.getColor().a = 0;
										read2(Files.level(lev+1), demoRunnable(lev+1));
										//start(lev+1);
									}
								});
								
							}
						})));*/
						
					}
				}), new Predicate() {
					
					@Override
					public boolean accept() {
						for(BaseItem item : game.getAll(BaseItem.class)){
							if(item.getColor().a < 1){
								return false;
							}
						}
						return true;
					}
				}));
			}
		};
	}

}
