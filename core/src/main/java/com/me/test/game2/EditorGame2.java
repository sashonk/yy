package com.me.test.game2;


import java.util.Collections;
import java.util.Comparator;

import java.util.LinkedList;
import java.util.List;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.me.test.TestGame;

import com.me.test.game2.item.Yang;
import com.me.test.game2.item.Yin;


public class EditorGame2 extends ItemContainer{


	final Window loadWin;
	ButtonGroup bg;
	int level = -1;
	final TextButton r_icon;
	final TextField scaleField;
	final TextField difficultyField;
	final TextField goal2threshold;
	final TextField goal3threshold;
	final TextField tips;
	

	final InputProcessor inputProcessor;
	final Window controlWin;

	final class FloatValueTextFieldFilter implements TextField.TextFieldFilter{

		@Override
		public boolean acceptChar(TextField textField, char key) {
			return Character.isDigit(key) || key == '.';
		}
		
	}
	
	final class TipsTextFieldFilter implements TextField.TextFieldFilter{

		@Override
		public boolean acceptChar(TextField textField, char key) {
			char lowerCase = Character.toLowerCase(key);
			return lowerCase=='u' || lowerCase == 'l' || lowerCase=='d' || lowerCase == 'r';
		}
		
	}
	
	public EditorGame2(final TestGame g) {
		super(g);
		final Stage s = getStage();
		
		s.addListener(new InputListener(){
			
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			
				Actor a =  event.getTarget();
				System.out.println(a);
				System.out.println(a.getName());
				//System.out.println(a.equals(gameLayer));
				
				return true;
			}
			
		});
		
		final Skin skin =g.getManager().getSkin();
		


		for(int i  = 0; i<40; i++){
			for(int j  = 0; j<40; j++){
				Image img = new Image(game.getManager().getAtlas().findRegion("back"));
				backgroundLayer.addActor(img);
				img.setBounds(i*BaseItem.ITEM_WIDTH, j*BaseItem.ITEM_HEIGHT, BaseItem.ITEM_WIDTH, BaseItem.ITEM_HEIGHT);
				
			}
		}
		

		inputProcessor = new InputAdapter(){
			public boolean keyDown (int keycode) {
				boolean result = true;
				
				//Directio
				int dx= 0;
				int dy = 0;
				if(keycode==Keys.UP){
					dy = 1;
				}
				else if(keycode==Keys.DOWN){
					dy = -1;
				}
				else if(keycode==Keys.RIGHT){
					dx = 1;
				}
				else if(keycode==Keys.LEFT){
					dx = -1;;
				}
				else{
					result = false;
				}
				
				final int fdx= dx;
				final int fdy = dy;
				
				List<BaseItem> upToDown = new LinkedList<BaseItem>( getAll(BaseItem.class));
				Collections.sort(upToDown, new Comparator<BaseItem>(){

					@Override
					public int compare(BaseItem a, BaseItem b) {
						if(fdy>0){
							return Integer.valueOf(b.getLocationY()).compareTo(Integer.valueOf(a.getLocationY()));
						}
						else if(fdy<0){
							return Integer.valueOf(a.getLocationY()).compareTo(Integer.valueOf(b.getLocationY()));
						}
						else if(fdx>0){
							return Integer.valueOf(b.getLocationX()).compareTo(Integer.valueOf(a.getLocationX()));
						}
						else if(fdx<0){
							return Integer.valueOf(a.getLocationX()).compareTo(Integer.valueOf(b.getLocationX()));
						}
					
						return 0;
					}
					
				});
				if(dx!=0 || dy!=0){
					for(BaseItem item : upToDown){
						ItemFactory factory = factories.get(item.getClass());
						factory.translate(item, item.getLocationX()+dx, item.getLocationY()+dy, EditorGame2.this);
					}
				}
				
				return result;
			}
		};
	
		TextButton back = new TextButton("back", skin);
		back.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				TestGame tg = getGame();
				tg.setScreen(tg.getMenuScreen());			
			}
		} );
		//uiLayer.addActor(back);
		//back.setPosition(s.getWidth()-100, 30);
		
		
		controlWin = new Window("drag me", skin,"win");
		

		r_icon = new TextButton("R",skin, "press");
		r_icon.setName("R");
	
		scaleField = new TextField("", skin);
		scaleField.setMessageText("scale");
		scaleField.setTextFieldFilter(new FloatValueTextFieldFilter());
		scaleField.addListener(new InputListener(){
			

			/** Called when a key is typed. When true is returned, the event is {@link Event#handle() handled}. */
			public boolean keyTyped (InputEvent event, char character) {
				try{
					float orScale = gameLayer.getScaleX();
					Vector2 localPoint = gameLayer.screenToLocalCoordinates(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2));
					
					float  scale = Float.parseFloat(scaleField.getText());
					if(scale>2 || scale < 0.4){
						return true;
					}
					
					gameLayer.scaleBy(scale-orScale);
					backgroundLayer.scaleBy(scale-orScale);
					
		
				}
				catch(NumberFormatException ex){
					System.err.println(ex);
				}
				
				return true;
			}
		});
		

		
		difficultyField = new TextField("", skin);
		difficultyField.setMessageText("difficulty");
		difficultyField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

		goal2threshold = new TextField("", skin);
		goal2threshold.setMessageText("goal2");
		goal2threshold.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

		
		goal3threshold = new TextField("", skin);
		goal3threshold.setMessageText("goal3");
		goal3threshold.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
		
		tips = new TextField("", skin);
		tips.setMessageText("tips");
		tips.setTextFieldFilter(new TipsTextFieldFilter());
		
		TextButton save = new TextButton("save", skin);
		save.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				LevelParams params = new LevelParams();
				params.rotten = r_icon.isChecked();
				params.goal2threshold = Integer.parseInt(goal2threshold.getText());
				params.goal3threshold = Integer.parseInt(goal3threshold.getText());
				params.scale = Float.parseFloat(scaleField.getText());
				params.difficulty = Integer.parseInt(difficultyField.getText());
				params.tips = tips.getText();
				 

				write2(Files.level(level),params);

			}
		});
		
		
		TextButton load = new TextButton("load", skin);
		load.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				if(loadWin.isVisible()){
					loadWin.setVisible(false);
					//Util.center(loadWin);
				}
				else{
					loadWin.setVisible(true);
				}
				
			}});
		
		TextButton next = new TextButton("next", skin);
		next.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				loadLevel(level+1);
			
				
			}});
		
		TextButton prev = new TextButton("prev", skin);
		prev.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				loadLevel(level-1);
			
				
			}});
		
		TextButton clear = new TextButton("clear", skin);
		clear.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
			
				clear();
			}});
		
		Label levelLabel = new Label("level:", skin, "tiny");
		levelLabel.setName("levelLabel");
		

		
		
		 controlWin.defaults().pad(2);
		 bg = new ButtonGroup();
		 int rowCounter = 0;
		for(ItemFactory factory : getFactories().values()){
			ImageButton tool = factory.createTool(skin);
			bg.add(tool);
			
			controlWin.add(tool);
			rowCounter++;
			
			if(rowCounter==3){
				rowCounter = 0;
				controlWin.row();
			}
		}
		
		if(rowCounter>0){
			controlWin.row();
		}

		 controlWin.add(new Label("sc", skin, "tiny"));
		 controlWin.add(scaleField).colspan(3).row();
		 
		 controlWin.add(new Label("di", skin, "tiny"));
		 controlWin.add(difficultyField).colspan(3).row();
		 
		 controlWin.add(new Label("g2", skin, "tiny"));
		 controlWin.add(goal2threshold).colspan(3).row();
		 
		 controlWin.add(new Label("g3", skin, "tiny"));
		 controlWin.add(goal3threshold).colspan(3).row();
		 
		 controlWin.add(new Label("ti", skin, "tiny"));
		 controlWin.add(tips).colspan(3).row();

		 controlWin.add(clear);
		 controlWin.add(r_icon).row();

		 controlWin.add(save);
		 controlWin.add(load).row();
		 controlWin.add(prev);
		 controlWin.add(next).row();
		 controlWin.add(levelLabel).colspan(3).row();
		 controlWin.add(back).colspan(3).row();
		 controlWin.pack();
		 uiLayer.addActor(controlWin);
		 controlWin.setClip(false);

		// controlWin.setWidth(100);
		 
			loadWin = new Window("load level", skin, "win");
			loadWin.setMovable(false);
			loadWin.defaults().pad(5);
			

			int num = 1;
			int width = 10;
			int height = 15;
			for(int i = 0; i<height; i++){
				for(int j = 0; j<width; j++){
					final int levelNumber = num++;
					TextButton tb = new TextButton(Integer.toString(levelNumber), g.getManager().getSkin());
				
		
					tb.addListener(new ClickListener(){
						public void clicked (InputEvent event, float x, float y) {
							loadLevel(levelNumber);
							loadWin.setVisible(false);
						}
					});
					
					loadWin.add(tb);
					//tb.setSize(120, 60);
				}
				loadWin.row();
			}
			
			loadWin.pack();		
			controlWin.addActor(loadWin);
			//Util.center(table);
			loadWin.setPosition(s.getWidth()/2-loadWin.getWidth()/2, s.getHeight()/2-loadWin.getHeight()/2);
			loadWin.setVisible(false);
		 
			

			
			gameLayer.addListener(gameLayerListener());
		
		Preferences prefs = getGame().getPreferences();
		int last = prefs.getInteger("lastEdited", -1);
/*		if(last!=-1){
			read2(Files.level(last));
			
			for(Actor item : gameLayer.getChildren()){
				if(item instanceof BaseItem){
					item.addListener(editorItemListener());
				}
			}
			
			
			r_icon.setChecked(getMeta().is_rotten());
			scaleField.setText(Float.toString(getMeta().get_scale()));
			difficultyField.setText(Integer.toString(getMeta().get_difficulty()));
			goal2threshold.setText(Integer.toString(getMeta().get_goal2threshold()));
			goal3threshold.setText(Integer.toString(getMeta().get_goal3threshold()));

			
			Label levelLab = (Label) controlWin.findActor("levelLabel");
			if(levelLab!=null){
				levelLab.setText("level: "+last);
			}
			
			
			
			level = last;
		}*/
		
/*		gameLayer.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {				
				stage.setKeyboardFocus(null);
			}
		});*/
	}

	@Override
	public String getName() {
		return "editor";
	}
	
	void clear(){
		super.clear();
		
		
		gameLayer.addListener(gameLayerListener());
	}
	
	
	public void loadLevel(int levelNum){

		
		clear();
		read2(Files.level(levelNum));
		Label levelLab = (Label) controlWin.findActor("levelLabel");
		if(levelLab!=null){
			levelLab.setText("level: "+levelNum);
		}
		
		level = levelNum;
		
		for(Actor item : gameLayer.getChildren()){
			if(item instanceof BaseItem){
				item.addListener(editorItemListener());
			}
		}
		
		
		
		Preferences prefs = getGame().getPreferences();
		prefs.putInteger("lastEdited", level);
		prefs.flush();

		LevelMeta meta = getMeta();
		r_icon.setChecked(meta.is_rotten());
		scaleField.setText(Float.toString(meta.get_scale()));
		difficultyField.setText(Integer.toString(meta.get_difficulty()));
		goal2threshold.setText(Integer.toString(meta.get_goal2threshold()));
		goal3threshold.setText(Integer.toString(meta.get_goal3threshold()));
		tips.setText(meta.getTips().asString());
	}
	
	EventListener gameLayerListener(){
		return new InputListener(){
			Vector2 p;
			Vector2 origin;
			boolean camera;
			
			public boolean mouseMoved (InputEvent event, float x, float y) {
			//	OrthographicCamera cam = (OrthographicCamera) stage.getCamera();
				if(Gdx.input.isKeyPressed(Keys.G) && !camera){
					p = new Vector2( Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY());
					origin = //new Vector2(cam.position.x, cam.position.y);
							new Vector2(gameLayer.getX(), gameLayer.getY());
					camera = true;
				}
				else if(!Gdx.input.isKeyPressed(Keys.G)){
					camera = false;
				}
				else{
					//System.out.println(3);
					
					Vector2 newP = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()- Gdx.input.getY());
					Vector2 result = origin.cpy().add(newP.cpy().sub(p));
					
					//cam.position.set(result.x, result.y, 0);
					gameLayer.setPosition(result.x, result.y);
					backgroundLayer.setPosition(result.x, result.y);
				}
				
	
				
				return true;
			}

		
		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		

	
			 if(event.getButton()==Buttons.LEFT){

				 
					if(Gdx.input.isKeyPressed(Keys.NUM_1)){
						getStage().getRoot().scaleBy(-.05f);
					}
					else if(Gdx.input.isKeyPressed(Keys.NUM_2)){
						getStage().getRoot().scaleBy(.05f);
					}
				
/*						else if(Gdx.input.isKeyPressed(Keys.R)){
					int tx = table.getTableX(x);
					int ty = table.getTableY(y);
					
					BaseItem item = getTable().get( tx, ty);
					if(item!=null){
						item.remove();
						getTable().put(null, tx, ty);
					}
				}*/
				else if(Gdx.input.isKeyPressed(Keys.Q)){
					if(controlWin.isVisible()){
						controlWin.setVisible(false);
					}
					else{
						controlWin.setVisible(true);
					}
				}
				else{
				
					Button checked = bg.getChecked();
						if(checked!=null){
							String name = checked.getName();
							
							ItemFactory factory = null;
							for(ItemFactory f : getFactories().values()){
								if(f.isOwner(checked)){
									factory = f;
								}
							}
							
							int tx = table.getTableX(x);
							int ty = table.getTableY(y);
							
							BaseItem item = factory.create(tx, ty, EditorGame2.this);
							gameLayer.addActor(item);
							
							item.addListener(editorItemListener());
						}
				}
			}
			 
			 return true;
		}
	};
	}
	
	
	public EventListener editorItemListener(){
		return  new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					event.setBubbles(false);
				
					BaseItem thisItem = (BaseItem) event.getListenerActor();
				if(Gdx.input.isKeyPressed(Keys.R)){
					ItemFactory f = factories.get(thisItem.getClass());
					f.destroy(thisItem, EditorGame2.this);
					thisItem.remove();
					return true;
				}
				else if(event.getListenerActor() instanceof Yin){
					Button checked = bg.getChecked();
					ItemFactory factory = null;
					for(ItemFactory f : getFactories().values()){
						if(f.isOwner(checked)){
							factory = f;
						}
					}
					
					if(factory.getItemClass()==Yang.class){
						Vector2 globalXY = gameLayer.screenToLocalCoordinates(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
						//Vector2 globalXY = event.getListenerActor().localToStageCoordinates(new Vector2(x,y));
						int tx = table.getTableX(globalXY.x);
						int ty = table.getTableY(globalXY.y);
						
						
						
						BaseItem item = factory.create(tx, ty, EditorGame2.this);
						item.addListener(editorItemListener());
						gameLayer.addActor(item);
					}
					
				}
				
				return false;
			}
		};
	}
	
	
	@Override
	public void show() {
		InputMultiplexer m = new InputMultiplexer();
		m.addProcessor(stage);
		m.addProcessor(inputProcessor);
		//Gdx.input.setInputProcessor
		Gdx.input.setInputProcessor(m);
		
	
	}
	

}
