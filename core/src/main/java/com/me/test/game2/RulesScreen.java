package com.me.test.game2;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.L10nButton;
import com.me.test.L10nLabel;
import com.me.test.Sounds;
import com.me.test.TestGame;
import com.me.test.Util;


public class RulesScreen  extends BaseMenuScreen{
	
	int page;
	Pagination pagination;
	
	@Override
	protected void background(){
		
		Texture splash = new Texture(Gdx.files.internal("data/pap1.jpg"));
		TextureRegion tr = new TextureRegion(splash, 0, 0, 1024, 768);
		Image im = new Image(tr);
		im.setFillParent(true);
		getStage().addActor(im);
	}

	public RulesScreen(TestGame g) {
		super(g);
		

		
		Table t1 = new Table();
		t1.defaults().width(getStage().getWidth()*4/5);
		Label label = new L10nLabel( g.getL10n(), "legend", g.getManager().getSkin(), "small");
		//t1.setWidth(getStage().getWidth()*4/5);
	
		label.setWrap(true);
		label.setAlignment(Align.center);
		t1.add(label);
		t1.pack();
		getStage().addActor(t1);
		Util.center(t1);
		
		Table t2 = new Table();
		t2.defaults().width(getStage().getWidth()*4/5);
		Label label2 =  new L10nLabel( g.getL10n(), "legend2", g.getManager().getSkin(), "small");
	
		label2.setWrap(true);
		label2.setAlignment(Align.center);
		t2.add(label2);
		t2.pack();
		
		getStage().addActor(t2);
		Util.center(t2);
		
/*		Table t3 = new Table();
		t3.defaults().width(getStage().getWidth()*4/5);
		Label label3 = new L10nLabel(g.getL10n(), "aboutText", g.getManager().getSkin());
		t3.add(label3);
		t3.pack();
		
		
		
			label3.setWrap(true);
			label3.setAlignment(Align.center);
			getStage().addActor(t3);
			Util.center(t3);
			
			Table t4 = new Table();
			t4.defaults().width(getStage().getWidth()*4/5);
			Label label4 = new L10nLabel(g.getL10n(),"aboutText2", g.getManager().getSkin());
			t4.add(label4);
			t4.pack();
		
			label4.setWrap(true);
			label4.setAlignment(Align.center);
			getStage().addActor(t4);
			Util.center(t4);*/
		
		
		//label.set
		//ScrollPane pane = new ScrollPane(label, g.getManager().getSkin());
		//pane.setFadeScrollBars(false);
		
	//	pane.setWidth(getStage().getWidth()*3/5);
	//	pane.setHeight(getStage().getHeight()*1/2);
		
		
	//	getStage().addActor(pane);
	

		//label.setY(-150);

		

		
		Table navi = new Table();
		navi.defaults().pad(10);
		final ImageButton forward = new ImageButton(g.getManager().getSkin(), "navi");
		forward.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					if(!forward.isDisabled()){
						pagination.forward();
						//Gdx.audio.
						//getGame().getSoundManager().play(Sounds.MENU_SCROLL);
					}
				}
			});
		
		final ImageButton backward = new ImageButton(g.getManager().getSkin(), "navi_back");
		backward.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				if(!backward.isDisabled()){
					pagination.backward();
					//getGame().getSoundManager().play(Sounds.MENU_SCROLL);
				}
			}
		});
		
		navi.add(backward);
		navi.add(forward);
		navi.pack();
		stage.addActor(navi);
		Util.center(navi);
		navi.setY(20);
		navi.setX(stage.getWidth()-navi.getWidth()-20);

		
		 pagination = new Pagination(stage, new Pagination.Callback() {
				
			@Override
			public void atIndex(Pagination object, int index) {
				backward.setDisabled(false);
				backward.setVisible(true);
				forward.setDisabled(false);
				forward.setVisible(true);
				if(index == 0){
					backward.setDisabled(true);
					backward.setVisible(false);
				}
				else if(index == object.getPages().size()-1){
					forward.setDisabled(true);
					forward.setVisible(false);
				}
				
				
/*				
				backward.setDisabled(false);
				forward.setDisabled(false);
				if(index == 0){
					backward.setDisabled(true);
				}
				else if(index == object.getPages().size()-1){
					forward.setDisabled(true);
				}*/

				
			}
		},  label, label2);
	}

	@Override
	public String getName() {
		return "rules";
	}

	public boolean hasBackButton(){return true;}

	static class Pagination{
		Stage _stage;
		Callback _call;
		Pagination(Stage stage, Callback callback, Actor...pages){
			_stage = stage;
			_call= callback;
			for(Actor page : pages){
				
				_pages.add(page);

				page.setVisible(false);
				
			}
			
			_index = 0;
			_pages.get(_index).setVisible(true);
			_call.atIndex(this, _index);
		}
		
		public static interface Callback{
			void atIndex(Pagination pg, int index);
		}
		
		public void setIndex(int ind){
			if(ind > _pages.size()-1 || ind< 0){
				return ;
			}
			
			for(Actor page : _pages){
				page.setVisible(false);

			}
			
			
			_index = ind;
			_pages.get(_index).setVisible(true);
			_call.atIndex(this, _index);
		}
		

		
		void forward(){
			int newIndex = _index+1;
			setIndex(newIndex);
		}
		
		void backward(){	
			int newIndex = _index-1;
			setIndex(newIndex);
			}
		
		public LinkedList<Actor> getPages(){
			return _pages;
		}
		
		LinkedList<Actor> _pages = new LinkedList<Actor>();
		int _index;
	}
}
