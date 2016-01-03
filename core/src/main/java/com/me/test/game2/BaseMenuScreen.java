package com.me.test.game2;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.me.test.BaseScreen;
import com.me.test.TestGame;

public abstract class BaseMenuScreen extends BaseScreen{

	
	public BaseMenuScreen(TestGame g) {
		super(g);
		
		
/*		TextureRegion b = new TextureRegion(g.getManager().getAtlas().findRegion("wall"));	
		TiledDrawable td = new TiledDrawable(b);
		Image background = new Image(td);	
		stage.addActor(background);
		background.setFillParent(true);*/
		
/*		Random rnd = new Random(System.currentTimeMillis());

		float w = 150, h = 150;
		float x = rnd.nextFloat()*w- w , y = rnd.nextFloat()*h - h;
		float tw = stage.getWidth();
		float th = stage.getHeight();
		boolean sw = false;
		do{
			TextureRegion b = new TextureRegion(g.getManager().getAtlas().findRegion("wall"));
			Image ib = new Image(b);
			ib.setBounds(x, y, w, h);
			stage.addActor(ib);
			
			if(x >= tw){
				x = sw ? 0 : -w / 2;
				y+= h;
				sw ^= true;
			}else{
				x += w;
			}
			
			
		}while(y < th);*/
		
		background();
		
		
/*		TextureAtlas a = g.getManager().getAtlas();
		TextureRegion lu = new TextureRegion(a.findRegion("corner"));
		TextureRegion ru = new TextureRegion(a.findRegion("corner"));
		ru.flip(true, false);
		TextureRegion rb = new TextureRegion(a.findRegion("corner"));
		rb.flip(true, true);
		TextureRegion lb = new TextureRegion(a.findRegion("corner"));
		lb.flip(false, true);
		
		float width = 125;
		float height = 125;
		Stage s = getStage();
		Image iLu = new Image(lu);
		iLu.setSize(width, height);
		iLu.setY(s.getHeight()-iLu.getHeight());
		Image iRu = new Image(ru);
		iRu.setSize(width, height);
		iRu.setPosition(s.getWidth()-iRu.getWidth(), s.getHeight()-iRu.getHeight());
		Image iRb = new Image(rb);
		iRb.setSize(width, height);
		iRb.setX(s.getWidth()-iRb.getWidth());
		Image iLb = new Image(lb);
		iLb.setSize(width, height);
		
		s.addActor(iLu);
		s.addActor(iRu);
		s.addActor(iRb);
		s.addActor(iLb);*/
		
		if(hasBackButton()){
			ImageButton back = new ImageButton(g.getManager().getSkin(), "return");
			back.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					
					TestGame tg = getGame();
					tg.setScreen(tg.getMenuScreen());
					//getGame().setScreen("menu");
				}
			});
			getStage().addActor(back);
		//	Util.center(back);
			back.setPosition(20, 20);
			float mul = 0.4f;
			back.setSize(back.getWidth()*mul, back.getHeight()*mul);
			
		}
	}

	public boolean hasBackButton(){
		return false;
	}
	
	@Override
	public void hide(){
	//	getGame().getNative().showAdd(100, false);
	}
	
	protected void background(){
		
		Texture splash = new Texture(Gdx.files.internal("data/papyrus.jpg"));
		TextureRegion tr = new TextureRegion(splash, 0, 0, 1024, 768);
		Image im = new Image(tr);
		im.setFillParent(true);
		getStage().addActor(im);
	}

}
