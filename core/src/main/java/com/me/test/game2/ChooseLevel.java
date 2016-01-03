package com.me.test.game2;

import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.Feature;
import com.me.test.IActivityRequestHandler.GetPricesCallback;
import com.me.test.IActivityRequestHandler.PurchaseCallback;
import com.me.test.L10nButton;
import com.me.test.L10nLabel;
import com.me.test.Pair;
import com.me.test.Sounds;
import com.me.test.TestGame;
import com.me.test.Util;

public class ChooseLevel extends BaseMenuScreen{
	static final int lastActive = 111;
	static final int PAGE_WIDTH = 6;
	static final int PAGE_HEIGHT = 5;
	static final int CELL_WIDTH = 130;
	static final int CELL_HEIGHT = 90;
	
	static final float PAGE_OVER_DURATION = 0.3f;
	
	Label page ;
	
	
	Map<Integer, Integer> diffMap;
	@Override
	protected void background(){
		
		Texture splash = new Texture(Gdx.files.internal("data/pap0.jpg"));
		TextureRegion tr = new TextureRegion(splash, 0, 0, 1024, 768);
		Image im = new Image(tr);
		im.setFillParent(true);
		getStage().addActor(im);
	}

	public ChooseLevel(final TestGame g) {
		super(g);
	
		
	Stage s = getStage();		


	try {
		ObjectInputStream diffStream = new ObjectInputStream(Files.diffMap().read());
		 diffMap = (Map<Integer, Integer>) diffStream.readObject();
		diffStream.close();
	} catch (Exception e) {
		Gdx.app.log(getClass().getName(), e.toString());
		e.printStackTrace();
		diffMap = new TreeMap<Integer, Integer>();
	}
	

	
		
	Table page1 = page(1);
	s.addActor(page1);
	Table page1Labels= pageLabels(1);
	s.addActor(page1Labels);
		
	Table page2 = page(31);
	s.addActor(page2);
	Table page2Labels= pageLabels(31);
	s.addActor(page2Labels);
	
/*	Table page3 = page(61,LevelMapper.lastStandard, false);
	s.addActor(page3);
	Table page3Labels= pageLabels(61,LevelMapper.lastStandard, false);
	//page3Labels.setY(300);
	s.addActor(page3Labels);
		
	Table page4 = page(LevelMapper.lastStandard+1, LevelMapper.lastStandard+31, true);
	s.addActor(page4);
	Table page4Labels= pageLabels(LevelMapper.lastStandard+1, LevelMapper.lastStandard+31, true);
	page4Labels.setClip(false);
	s.addActor(page4Labels);*/
	
	
	
//	page3Labels.setHeight(800);
	
//	page3.setHeight(page1.getHeight());
	//page3Labels.setHeight(page1.getHeight());
//	page3Labels.moveBy(0, 170);
	//page3.padBottom(page1.getHeight()-page3.getHeight());
	//page3Labels.padBottom(page1Labels.getHeight()-page3Labels.getHeight());

		
			Table navi = new Table();
			navi.defaults().pad(10);
			final ImageButton back = new ImageButton(g.getManager().getSkin(), "navi_back");
			back.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					if(!back.isDisabled()){
						pagination.backward();
						getGame().getSoundManager().play(Sounds.PAGE_SCROLL);
					}
				}
			});
			navi.add(back);
			
			page = new Label("1/5", g.getManager().getSkin(), "big");
			//s.addActor(page);
			//page.setPosition(x, y);
			s.addActor(page);
			Util.center(page);
			page.setY(650);
			
			final ImageButton forw = new ImageButton(g.getManager().getSkin(), "navi");
			forw.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					if(!forw.isDisabled()){
						pagination.forward();
						getGame().getSoundManager().play(Sounds.PAGE_SCROLL);
					}
				}
			});
			navi.add(forw);
			navi.pack();
			
			s.addActor(navi);
			Util.center(navi);
			navi.setY(20);
			
			pagination= new Pagination(getStage(), new Pagination.Callback() {
				
				@Override
				public void atIndex(Pagination object, int index) {
					back.setDisabled(false);
					back.setVisible(true);
					forw.setDisabled(false);
					forw.setVisible(true);
					if(index == 0){
						back.setDisabled(true);
						back.setVisible(false);
					}
					else if(index == object.getPages().size()-1){
						forw.setDisabled(true);
						forw.setVisible(false);
					}
			
					page.setText(String.format("%d/%d", index+1, object.getPages().size()));
				} 
			}, 
			new Pair<Table, Table>(page1, page1Labels), 
			new Pair<Table, Table>(page2, page2Labels)/*,
			new Pair<Table,Table>(page3, page3Labels),
			new Pair<Table,Table>(page4, page4Labels),
			new Pair<Table,Table>(page5, page5Labels),
			new Pair<Table,Table>(page6, page6Labels)*/
			
					);
		
			
			

			
/*			if(!getGame().getNative().getEnabledFeatures().contains(Feature.LEVELPACK)){
				_lock = new Image(getGame().getManager().getAtlas().findRegion("lock"));
				Actor lock = _lock;
				lock.setSize(150, 230);
				lock.setName("levelpack-lock");
				//s.addActor(lock);
			//	float page4X = page4.getX()+page4.getWidth()/2;
			//	float page4Y = page4.getY()+page4.getHeight()/2;
			//	lock.setPosition(page4X,page4Y);
				
				//page4Labels.addActor(lock);
				//Util.center(lock);
				lock.toFront();
				lock.setPosition(750, -100);
				
				
			//	page4Labels.getColor().r = 1f;
			//	page4.getColor().r = 1f;
				
				
				lock.addListener(new ClickListener(){
					public void clicked (InputEvent event, float x, float y) {
						levelpackHandler();
					}
				});
			}*/
			

			
			
			Preferences prefs= getGame().getPreferences();			
			final int lastDone = prefs.getInteger("last_played");
			int ind = 0;
			if(lastDone>30 && lastDone<=60){
				ind= 1;
			}

			pagination.setIndex(ind,false);

	}
	
	Actor _lock;
	
	public boolean hasBackButton(){return true;}

	
	Table page(int beginNumber, int maxNumber, boolean extra){
		Preferences prefs = getGame().getPreferences();
		
		Table page1 = new Table();
		page1.align(Align.top);
		page1.setName("page-"+System.currentTimeMillis());
		page1.setClip(true);
		page1.defaults().pad(5);
		page1.defaults().width(CELL_WIDTH);
		page1.defaults().height(CELL_HEIGHT);

		int num = beginNumber;
		int width = PAGE_WIDTH;
		int height = PAGE_HEIGHT;
outer:		for(int i = 0; i<height; i++){
			for(int j = 0; j<width; j++){
				final int c = num++;
				
				if(maxNumber > 0 &&  c>maxNumber){
					break outer;
				}
				
				 boolean isDone = prefs.getBoolean("is_done_"+c, false);
				 final boolean inactive = c>lastActive;
				 
				 Integer difficulty = diffMap.get(c);
				 String choiceStyle;
				 

					 int achivement = prefs.getInteger("achivement"+c);
				if(extra){
					 if(achivement>0){
						 choiceStyle = new StringBuilder("choice_extra").append(achivement).toString();
					 }
					 else{
						 choiceStyle = new StringBuilder("choice_extra").toString();
					 }
				}
				else{
					 if(achivement>0){
						 choiceStyle = new StringBuilder("choice_star").append(achivement).toString();
					 }
					 else{
						 choiceStyle = new StringBuilder("choice").toString();
					 }
				}
				 

				 ImageButton tb = new ImageButton(getGame().getManager().getSkin(), choiceStyle);	
				 tb.setName(String.format("image-%d-%d", c, System.currentTimeMillis()));
				 if(inactive){
					 tb.setDisabled(true);
				 }
				 
	
	
				 tb.addListener(createStartGameListener(inactive, c, extra ));
				
				page1.add(tb);

			}
			page1.row();
		}
		
		page1.pack();		
		return page1;
	}
	
	Table page(int beginNumber){
		return page(beginNumber, -1, false);
	}
	
	boolean prevDone;
	
	
	Table pageLabels(int beginNumber, int maxNumber, boolean extra){
		
		
		Preferences prefs = getGame().getPreferences();
		
		Table page1 = new Table();
	
		page1.align(Align.top);
		page1.setName("labels-"+System.currentTimeMillis());
		
		page1.setClip(true);
		page1.align(Align.center);
		page1.defaults().pad(5);
		page1.defaults().align(Align.center);
		page1.defaults().width(CELL_WIDTH);
		page1.defaults().height(CELL_HEIGHT);
		page1.defaults().center();
		//page1.padLeft(125);

		int num = beginNumber;
		int width = PAGE_WIDTH;
		int height = PAGE_HEIGHT;
		outer:		for(int i = 0; i<height; i++){
			for(int j = 0; j<width; j++){
				final int c = num++;
				
				if(maxNumber > 0 &&  c>maxNumber){
					break outer;
				}
				
				 boolean isDone = prefs.getBoolean("is_done_"+c, false);
				
				 
				  boolean inactive = c>lastActive;
				 if(!prevDone && c>1){
					 inactive = false;
				 }
				 prevDone = isDone;

				 
				TextButton tb = new TextButton(Integer.toString( c), getGame().getManager().getSkin(), "choice");
				 tb.setName(String.format("button-%d-%d", c, System.currentTimeMillis()));
				 if(inactive){
					 tb.setDisabled(true);
				 }

	
				 tb.addListener(createStartGameListener(inactive, c, extra ));
				
				page1.add(tb);

			}
			page1.row();
		}
		
		page1.pack();		
		return page1;
	}
	
	Table pageLabels(int beginNumber){
		return pageLabels(beginNumber, -1, false);
	}
	
	Pagination pagination;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "choose";
	}


	
	static class Pagination{
		Stage _stage;
		Callback _call;
		Pagination(Stage stage, Callback callback, Pair<Table,Table>...pages){
			_stage = stage;
			_call= callback;
			float translationX = 0;
			for(Pair<Table,Table> page : pages){
				
				_pages.add(page);
				

				Util.center(page.getFirst());
				Util.center(page.getSecond());
				page.getFirst().moveBy(translationX, 0);
				page.getSecond().moveBy(translationX, 0);
				
				translationX += _stage.getWidth();
			}
			
			_index = 0;
			_call.atIndex(this, _index);
		}
		
		public static interface Callback{
			void atIndex(Pagination pg, int index);
		}
		
		public void setIndex(int ind, boolean animate){
			

			if(ind > _pages.size()-1 || ind< 0){
				return ;
			}
			float translationX = (ind - _index)*_stage.getWidth();
			for(Pair<Table,Table> page : _pages){
				//page.translate(-translationX, 0);
				page.getSecond().addAction(Actions.moveBy(-translationX, 0, animate ? PAGE_OVER_DURATION : 0));
				page.getFirst().addAction(Actions.moveBy(-translationX, 0, animate ? PAGE_OVER_DURATION : 0));

			}
			
			
			_index = ind;
			
			_call.atIndex(this, _index);
		
		}
		
		public void setIndex(int ind){
			setIndex(ind,true);
		}
		
		void forward(){
			int newIndex = _index+1;
			setIndex(newIndex);
		}
		
		void backward(){	
			int newIndex = _index-1;
			setIndex(newIndex);
			}
		
		public LinkedList<Pair> getPages(){
			return _pages;
		}
		
		LinkedList<Pair> _pages = new LinkedList<Pair>();
		int _index;
	}

	@Override
	public void show(){
		super.show();
		
		Preferences prefs=getGame().getPreferences();
		final int lastDone = prefs.getInteger("last_done");
		
		
		for(Actor a : stage.getRoot().getChildren()){
			if(a.getName()!=null){
				
				
				if(a.getName().startsWith("page-")){
					Table t = (Table)a;
					for(Actor child : t.getChildren()){
						if(child.getName()!=null && child.getName().startsWith("image")){
							ImageButton tb = (ImageButton) child;
				
							String level = child.getName().substring(6, child.getName().indexOf("-", 7)); 
							Integer levNum = Integer.parseInt(level);
							 boolean isDone = prefs.getBoolean("is_done_"+levNum, false);
							int achievement = prefs.getInteger("achivement"+levNum);
							String choiceStyle;
							

								 if(achievement>0){
									 choiceStyle = new StringBuilder("choice_star").append(achievement).toString();
								 }
								 else{
									 choiceStyle = new StringBuilder("choice").toString();
								 }
							
							

							 
							 tb.setStyle(getGame().getManager().getSkin().get(choiceStyle, ImageButtonStyle.class));
							 
							 boolean prevDone = prefs.getBoolean("is_done_"+(levNum-1), false);
							 if(!prevDone && levNum>1){
								 tb.setDisabled(true);
							 }
							 else{
								 tb.setDisabled(false);
							 }
						}
					}
				}
				

				if(a.getName().startsWith("labels-")){
					Table t = (Table)a;
					for(Actor child : t.getChildren()){
						if(child.getName()!=null && child.getName().startsWith("button")){
							TextButton tb = (TextButton) child;
							
							String level = child.getName().substring(7, child.getName().indexOf("-", 8)); 
							Integer levNum = Integer.parseInt(level);
							int achievement = prefs.getInteger("achivement"+levNum);
							boolean extra = levNum>LevelMapper.lastStandard;
							 boolean isDone = prefs.getBoolean("is_done_"+levNum, false);

							 boolean prevDone = prefs.getBoolean("is_done_"+(levNum-1), false);
							 if(!prevDone && levNum>1){
								 tb.setDisabled(true);
							 }
							 else{
								 tb.setDisabled(false);
							 }
						}
					}
				}
			}
		}
		

	}
	
	

	
	
	ClickListener createStartGameListener(final boolean inactive, final int level, final boolean extra){
		return new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Button bt = (Button) event.getListenerActor();
				
				if(bt.isDisabled()){
					return;
				}
/*				
				if(extra && !getGame().getNative().getEnabledFeatures().contains(Feature.LEVELPACK)){
					levelpackHandler();
				}*/
				
				if(true){
					Game2 gm = getGame().getGame2Screen();
					gm.start(level);
				//gm.read(Files.level(c));
					getGame().setScreen(gm);
				}
		/*		else{
					IntroScreen intro = new IntroScreen(getGame());
					getGame().setScreen(intro);
				}*/
			}
		};
	}
}


