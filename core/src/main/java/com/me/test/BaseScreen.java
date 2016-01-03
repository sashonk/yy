package com.me.test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.me.test.video.ScreenShot;

public abstract class BaseScreen implements Screen{
	protected TestGame game;
	protected Stage stage;
	
	FPSLogger logger = new FPSLogger();
	
	public Stage getStage(){
		return stage;
	}
	


	public abstract String getName();

	
	public TestGame getGame(){
		return game;
	}
	
	public BaseScreen(TestGame g){
		 game = g;
		 
		 Viewport view = new StretchViewport(1024, 768);

		 //view.set
		 stage = new Stage(view);
		 stage.getRoot().setBounds(0, 0, stage.getWidth(), stage.getHeight());
		 stage.getRoot().setCullingArea(new Rectangle(0, 0, stage.getWidth(), stage.getHeight()));

			executor = Executors.newFixedThreadPool(8);
			
	}
	
	ExecutorService executor;

	
	public void render (float delta){
		///System.out.println(getStage().getCamera().combined);

		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		

	
		stage.act();
		stage.draw();

		///System.out.println(getStage().getCamera().position);

	//	upper.act();
	//	upper.draw();
		
		//logger.log();
		if(Gdx.input.isKeyPressed(Keys.S) /*|| TestGame.demoMode*/ && (count % 2 == 0)){
			System.out.println(Gdx.app.getJavaHeap()); 
			
			ScreenShot worker = new ScreenShot();
			worker.prepare();			// grab screenshot
			executor.execute(worker);
			
		}

		count ++;
	}
	
	int count = 1;


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Gdx.app.log("java heap", "jh:"+Gdx.app.getJavaHeap());
		Gdx.app.log("native heap","nh:"+Gdx.app.getJavaHeap());
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		//stage.get
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
	
		
		stage.dispose();
	}

	
	public void update(){
		
	}
	
	public Label yangPanel(Group parent, TestGame g){
		Session sess = g.getSession();
		Label ownedYangsLabel = new Label(Integer.toString(sess.getOwnerYangs()) , g.getManager().getSkin());
		ownedYangsLabel.setAlignment(Align.center);
		Window yangsTable = new Window("", g.getManager().getSkin());
		yangsTable.setKeepWithinStage(false);
		float yangTablePad = 20;
		yangsTable.padLeft(yangTablePad).padRight(yangTablePad);
		yangsTable.setBackground("frame_small");
		yangsTable.add(ownedYangsLabel).width(40) .padRight(20);
		
		Image ownedYangs = new Image(g.getManager().getAtlas().findRegion("yy"));
		float ownedYangDim = 30;
		yangsTable.add(ownedYangs).width(ownedYangDim).height(ownedYangDim);
		yangsTable.pack();
		parent.addActor(yangsTable);
		Util.center(yangsTable);
		yangsTable.setY(parent.getHeight()-yangsTable.getHeight()+10);
		return ownedYangsLabel;
	}
}
