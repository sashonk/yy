package com.me.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.me.test.IActivityRequestHandler.ShareCallback;
import com.me.test.Util.OnEventAction.Predicate;

public class Util {
	
	public static boolean isBlank(String s){
		return s==null ? true : s.trim().length()==0;
	}
	
	public static String getEnCardinalDeclensionKey(String base, int number){
		String suffix = "1";
		
		int mod = number%10;
		if(mod!=1){
			suffix = "2";
		}
		
		return String.format("%s%s", base, suffix);
	}
	
	public static String getRuCardinalDeclensionKey(String base, int number){
		String suffix = "1";
		
		int mod = number % 100;
		
		if(mod > 10 &&  mod <15){
			 suffix = "5";
		}
		else{
		 	 int modul = mod%10;
		 	 if(modul==2 || modul==3|| modul==4){
		 		 suffix = "234";
		 	 }
		 	 else if(modul==0 || modul >4){
		 		 suffix = "5";
		 	 }
		}
		
		return String.format("%s%s", base, suffix);
	}
	
	public static String replace(String rawString, Object...params){
		int c = 0;
		int count = 0;
		while(true){
			String pat = String.format("{%d}", c);
			int index = rawString.indexOf(pat);
			if(index>-1){
				count++;
				
			}
			else{
				break;
			}
			c++;
		}
		
		if(count!=params.length){
			throw new IllegalArgumentException(String.format("invalid number of arguments: expected %d, but was %d", count, params.length));
		}
		
		for(int i =0 ;i<count;i++){
			
			
			rawString = rawString.replace(String.format("{%d}", i), params[i].toString());
		}
		
		return rawString;
	}
	
	public static Action vibration(float aplitude, float duration){
		VibrationAction action = new VibrationAction(aplitude);
		action.setDuration(duration);
		return action;
	}
	
	
	//TODO
	 static class VibrationAction extends TemporalAction{
		 float amp;
		 public VibrationAction(float amplitude){
			 amp = amplitude;
		 }
		@Override
		protected void update(float percent) {
			float degree = amp*percent;
			getActor().setRotation(degree);
		}
		 
	 }
	 
	 
		

	 
		public static Action trigger(Action triggerAction, TriggerAction.Predicate p){
			TriggerAction a = new TriggerAction(triggerAction, p);
			return a;
		}
		
		public static class TriggerAction extends Action{
			Predicate predicate;
			Action eventAction;
			public TriggerAction(Action runnable, Predicate predicate){
				this.eventAction = runnable;
				this.predicate = predicate;
			}
			
			public  static  interface Predicate{
				boolean accept();
			}

			@Override
			public boolean act(float delta) {
				if(predicate.accept()){
					getActor().addAction(eventAction);
					return true;
				}
				return false;
			}


		}
	
	public static Action onEvent(Action eventAction, Predicate p){
		OnEventAction a = new OnEventAction(eventAction, p);
		return a;
	}
	
	public static class OnEventAction extends Action{
		Predicate predicate;
		Action eventAction;
		public OnEventAction(Action runnable, Predicate predicate){
			this.eventAction = runnable;
			this.predicate = predicate;
		}
		
		public  static  interface Predicate{
			boolean accept();
		}

		@Override
		public boolean act(float delta) {
			if(predicate.accept()){
				getActor().addAction(eventAction);
				return true;
			}
			return false;
		}


	}
	

	public static TemporalAction zoomTo(float value, float duration, float x, float y, Interpolation interpolation){
		ZoomToAction zoomTo = new ZoomToAction(value, x ,y);
		zoomTo.setDuration(duration);
		if(interpolation!=null){
			zoomTo.setInterpolation(interpolation);
		}
		return zoomTo;
	}

	public static TemporalAction zoomTo(float value, float duration, Interpolation interpolation){
		ZoomToAction zoomTo = new ZoomToAction(value);
		zoomTo.setDuration(duration);
		if(interpolation!=null){
			zoomTo.setInterpolation(interpolation);
		}
		return zoomTo;
	}


	public static TemporalAction moveCameraTo(float tx, float ty, float duration, Interpolation interpolation){
		MoveCameraToAction zoomTo = new MoveCameraToAction(tx, ty,  duration, interpolation);
		zoomTo.setDuration(duration);
		if(interpolation!=null){
			zoomTo.setInterpolation(interpolation);
		}
		return zoomTo;
	}
	
	public static class MoveCameraToAction extends TemporalAction{
		float tx;
		float ty;
		float ox;
		float oy;
		boolean inited;
		public MoveCameraToAction(float targetX, float targetY, float duration, Interpolation interpolation ){

			tx = targetX;
			ty = targetY;
			setDuration(duration);
			setInterpolation(interpolation);
		}
		@Override
		protected void update(float percent) {
			Actor actor = getActor();
			Stage stage =  actor.getStage();
			OrthographicCamera camera = (OrthographicCamera) stage.getCamera();

			if(!inited){
				ox = camera.position.x;
				oy = camera.position.y;
				inited = true;
			}
			
			float x = ox*(1-percent) + tx*percent;
			float y = oy*(1-percent) + ty*percent;
			camera.position.set(x, y, 0);
			constrainPosition(camera, stage);
			constrainZoom(camera);
		}
	}
	
	public static class ZoomToAction extends TemporalAction{
		float ox;
		float oy;
		float targetZoom;
		float initialZoom;
		boolean inited;
		boolean orig;
		public ZoomToAction(float value, float x, float y){
			ox = x;
			oy = y;
			targetZoom = value;
			inited= false;
			orig = false;
		}
		
		public ZoomToAction(float value){
			targetZoom = value;
			inited= false;
			orig = true;
		}
		@Override
		protected void update(float percent) {
			Actor actor = getActor();
			Stage stage =  actor.getStage();
			OrthographicCamera camera = (OrthographicCamera) stage.getCamera();

			if(!inited){
				initialZoom = camera.zoom;						
				inited = true;

			}		
			if(orig){
				ox = actor.getOriginX();
				oy = actor.getOriginY();				
				orig = false;
			}
			
			Vector2 originGlobal = actor.localToStageCoordinates(new Vector2(ox, oy));
			camera.position.set(originGlobal.x, originGlobal.y, 0);		
			camera.zoom = targetZoom*percent + initialZoom*(1-percent);
			constrainPosition(camera, stage);
			constrainZoom(camera);
		}

	}
	
	
	public static void constrainPosition(OrthographicCamera cam, Stage s){
		
		float vw = cam.viewportWidth*.5f*cam.zoom;
		float vh = cam.viewportHeight*.5f*cam.zoom;
		
		if(cam.position.x - vw<0){
			cam.position.x = vw;
		}
		if(cam.position.y - vh< 0){
			cam.position.y = vh;
		}
		if(cam.position.x + vw > s.getWidth()){
			cam.position.x = s.getWidth() - vw;
		}
		if(cam.position.y + vh > s.getHeight()){
			cam.position.y = s.getHeight() - vh;
		}
	}
	
	private static float Zmin = 0.1f;
	public static void constrainZoom(OrthographicCamera cam){
		if(cam.zoom<Zmin){
			cam.zoom = Zmin;
		}
		if(cam.zoom>1){
			cam.zoom = 1;
		}
	}
	
	public static Actor multiColorLabel(String text, String baseStyle, String[] colorNames, Skin skin){
		Table table = new Table();
		boolean styleSet = false;
		for(int i = 0; i<text.length(); i++){
			char c = text.charAt(i);
			
			
			String style =new StringBuilder(baseStyle).append("_").append(colorNames[i]).toString() ; 									
			Label label = new Label(new String(new char[]{c}), skin, style);
			if(!styleSet){
				table.defaults().padRight(label.getStyle().font.getBounds(" ").width);
				styleSet = true;
			}
			table.add(label);
		}
		table.pack();
		
		return table;
	}
	
	

	public static void center(Actor actor){
		Actor parent = actor.getParent();
		if(parent==null){
			return;
		}
		
		float x = parent.getWidth()/2 - actor.getWidth()/2;
		float y = parent.getHeight()/2 - actor.getHeight()/2;		
		actor.setPosition(x, y);
	}
	
	
	public  static Table  twitterButton(TestGame theGame){
		Table fbTable = new Table();
		fbTable.defaults().align(Align.center);
		fbTable.defaults().pad(4);
		
		ImageButton fbButton = new ImageButton(theGame.getManager().getSkin(), "twitter");
		float fbWidth = 45;
		fbButton.setSize(fbWidth, fbWidth);
		fbButton.addListener(tweetListener( theGame));		
		fbTable.add(fbButton).row();
		
		Label shareLabel = new L10nLabel(theGame.getL10n(), "tweet", theGame.getManager().getSkin(), "small");
		shareLabel.setAlignment(Align.center);
		
	//	fbTable.add(shareLabel);
		shareLabel.addListener(tweetListener( theGame));
	
		fbTable.pack();
		//stage.addActor(fbTable);
		return fbTable;
		
		//fbTable.setPosition(20, 20);
	}
	
	 static EventListener tweetListener(final TestGame theGame){

		return new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log(theGame.getClass().getName(), "tweet Yin & Yang");
		
				String text = theGame.getL10n().get("i_want_share");				
				theGame.getNative().tweet(text);
				Preferences prefs = theGame.getPreferences();
				prefs.putBoolean(Values.tweeted, true);
				prefs.flush();
			}
		};
	
	}
	
	public static Table shareButton(final BaseScreen theGame, ShareCallback clb, Dialog waitDlg){
		Table fbTable = new Table();
		fbTable.defaults().align(Align.center);
		fbTable.defaults().pad(4);
		
		ImageButton fbButton = new ImageButton(theGame.getGame().getManager().getSkin(), "fb");
		float fbWidth = 45;
		fbButton.setSize(fbWidth, fbWidth);
		fbButton.addListener(shareListener( theGame,clb, waitDlg));		
		fbTable.add(fbButton).row();
		
		Label shareLabel = new L10nLabel(theGame.getGame().getL10n(), "share",theGame.getGame().getManager().getSkin(), "small");
		shareLabel.setAlignment(Align.center);
		
		fbTable.add(shareLabel);
		shareLabel.addListener(shareListener( theGame,clb,waitDlg));
	
		fbTable.pack();
		return fbTable;
	}
	
	



	

	 static EventListener shareListener( final BaseScreen theGame,final ShareCallback clb, final Dialog waitDlg){
		return new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				waitDlg.show(theGame.getStage());
				
				Gdx.app.log(theGame.getGame().getClass().getName(), "share Yin & Yang on Facebook");
				String i_want_share =  theGame.getGame().getL10n().get("i_want_share");
				String yin_puzzle = theGame.getGame().getL10n().get("yin_puzzle");
				theGame.getGame().getNative().shareOnFacebook2(yin_puzzle, i_want_share, clb);
				

			}
		};
	}
	 


}
