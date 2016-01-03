package com.me.test.game2;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.BaseScreen;
import com.me.test.Feature;
import com.me.test.IActivityRequestHandler;
import com.me.test.IActivityRequestHandler.GetPricesCallback;
import com.me.test.IActivityRequestHandler.PurchaseCallback;
import com.me.test.IGame;
import com.me.test.L10nButton;
import com.me.test.L10nLabel;
import com.me.test.Session;
import com.me.test.TestGame;
import com.me.test.Util;
import com.me.test.Util.TriggerAction.Predicate;
import com.me.test.Values;
import com.me.test.game2.Game2.ResetCallback;

public class Scripts {
	
	public static Dialog waitDlg(final TestGame g){
		Skin s = g.getManager().getSkin();
		Dialog  d = new Dialog("", g.getManager().getSkin(), "win");
		float sidePad = 10;
		d.padLeft(sidePad).padRight(sidePad).padTop(0).padBottom(0);
		Label label = new L10nLabel(g.getL10n(), "wait",s, "small");
		label.setAlignment(Align.center);
		d.getContentTable().add(label);
		return d;
	}
	

	
	public static void walkthroughHandler2(final BaseScreen screen){
		
		
		final Stage stage = screen.getStage();
		final TestGame g = screen.getGame();
		final Preferences prefs = g.getPreferences();
		final boolean hasPermanentWalkthrough = prefs.getBoolean(Values.permanentWalkthrough(g.getGame2Screen().level));
		if(hasPermanentWalkthrough || g.getSession().getWalkthroughLastLevel()==g.getGame2Screen().level ){
			proposeWalkthrough(screen);
		}
			
		else{
			
			Skin skin = g.getManager().getSkin();
			final Dialog optionsDialog = new Dialog("",skin,"win");
			optionsDialog.setModal(true);
			optionsDialog.setMovable(false);
			Label cost = new L10nLabel(g.getL10n(), "walkthrough_cost",skin, "default");
			cost.setAlignment(Align.center);
			optionsDialog.pad(5);
			optionsDialog.getContentTable().pad(3);
			optionsDialog.getContentTable().add (cost).row();
			
			
/*			// description
			Label desc = new L10nLabel(g.getL10n(), "walkthrough_desc",skin, "small");
			desc.setAlignment(Align.center);
			priceDlg.getContentTable().add(desc).row();*/
			
			
			Button close = new L10nButton(g.getL10n(), "cancel", skin,"btn");
			close.addListener(new ClickListener(){
				public void clicked (InputEvent event, float x, float y) {
					optionsDialog.hide();
				}
			});
			

			Label or = new L10nLabel(g.getL10n(), "or", skin, "small");
			optionsDialog.getContentTable().add(or).row();
			
			Table options = new Table();
			options.align(Align.center);
			float pd = 30;
			options.defaults().padLeft(pd).padRight(pd);
			
			
			List<Actor> optionActors = new LinkedList<Actor>();						
/*			if(!prefs.getBoolean(com.me.test.Values.tweeted)){
				Table twitter = Util.twitterButton(g);
				options.add(twitter);
				optionActors.add(twitter);
				
			}*/
			
			if(!prefs.getBoolean(com.me.test.Values.shared)){
				final Dialog wait = waitDlg(g);
				Table facebook = Util.shareButton( screen, new IActivityRequestHandler.ShareCallback() {
					
					@Override
					public void onFailure(String message) {
						wait.hide();
						
					}
					
					@Override
					public void success() {
						wait.hide();
						optionsDialog.hide();
						
						g.getSession().setWalkthroughLastLevel(g.getGame2Screen().level);
						proposeWalkthrough(screen);
						
						
						Preferences prefs = screen.getGame().getPreferences();
						prefs.putBoolean(Values.shared, true);
						prefs.flush();
						
					}
				}, wait);
				options.add(facebook);
				//optionActors.add(facebook);
			}
			

			Table watchVideo = cinemaButton(stage, g, new IActivityRequestHandler.VideoCompletedStatusCallback() {
				
				@Override
				public void noVideoAvailable() {
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {
							Skin s = g.getManager().getSkin();
							final Dialog novid = new Dialog("", s, "win");
							novid.getContentTable().pad(15);
							Label text = new L10nLabel(g.getL10n(), "novid", s);
							text.setAlignment(Align.center);
							novid.getContentTable().add(text).row();
							
							Button close = new L10nButton(g.getL10n(), "close", s, "btn");
							close.addListener(new ClickListener(){
								public void clicked (InputEvent event, float x, float y) {
									novid.hide();
								}
							});
							
							novid.button(close);
							novid.pack();
							novid.show(stage);	
						}
					});																			
				}
				
				@Override
				public void call(boolean completed) {
					if(completed){
						optionsDialog.hide();
						g.getSession().setWalkthroughLastLevel(g.getGame2Screen().level);
						proposeWalkthrough(screen);
						
						Preferences p = g.getPreferences();
						String videoWatchesDateToken = p.getString(Values.dateToken, null);
						String token = Values.videoWatchedTimesValue(new Date());
						if(!token.equals(videoWatchesDateToken)){
							p.putInteger(Values.timesWatched, 1);
							p.putString(Values.dateToken, token);
						}
						else{
							int timesWatched = p.getInteger(Values.timesWatched, 0);
							p.putInteger(Values.timesWatched, timesWatched+1);
						}
						p.flush();
						
				
						
					}
					
				}
			});
			
			final String videoWatchesDateToken = prefs.getString(Values.dateToken, null);
			final String todayToken = Values.videoWatchedTimesValue(new Date());
			final int timesWatched = prefs.getInteger(Values.timesWatched, 0);
			if(!todayToken.equals(videoWatchesDateToken) || timesWatched < TestGame.maxVideosPerDay){
				options.add(watchVideo);
			}

			Table purchase = oneYangButton(optionsDialog,screen); 		
			options.add(purchase);
			
			
			for(Actor a : optionActors){
				a.addListener(new ClickListener(){
					public void clicked (InputEvent event, float x, float y) {
						optionsDialog.hide();
						final Dialog wait = waitDlg(g);
						wait.show(stage);
						wait.addAction(Actions.sequence(Actions.delay(5), Actions.run(new Runnable() {
							
							@Override
							public void run() {
								wait.hide();
								g.getSession().setWalkthroughLastLevel(g.getGame2Screen().level);
								proposeWalkthrough(screen);
							}
						})));
						
					}
				});
			}

			options.pack();			
			optionsDialog.getContentTable().add(options).row();
			optionsDialog.button(close);
			

			optionsDialog.show(stage);
			
			
		}
}
	
	

	

	public static Table cinemaButton(final Stage stage, final TestGame theGame, IActivityRequestHandler.VideoCompletedStatusCallback callback){
		Table fbTable = new Table();
		fbTable.defaults().align(Align.center);
		fbTable.defaults().pad(4);
		
		ImageButton fbButton = new ImageButton(theGame.getManager().getSkin(), "cinema");
		float w = 100;
		float h = 100;
		//fbButton.setSize(w, h);
		fbButton.addListener(cinemaListener(stage, theGame, callback));		
		fbTable .add(fbButton).width(w).height(h).row();
		
		Label shareLabel = new L10nLabel(theGame.getL10n(), "video",theGame.getManager().getSkin(), "small");
		shareLabel.setAlignment(Align.center);
		
		fbTable.add(shareLabel);
		shareLabel.addListener(cinemaListener(stage, theGame,callback));
	
		fbTable.pack();
		return fbTable;
	}
	
	 static EventListener cinemaListener(final Stage stage, final TestGame theGame,final IActivityRequestHandler.VideoCompletedStatusCallback callback){
		return new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log(theGame.getClass().getName(), "watch video ad clicked");
		
				theGame.getNative().showVideoAd(callback);
			}
		};
	}
	 
	static void proposeWalkthrough(final BaseScreen screen){

		final Stage stage = screen.getStage();
		final TestGame theGame= screen.getGame();
		Skin skin = theGame.getManager().getSkin();						
		final Dialog window = new Dialog("", skin,"win");
		window.getButtonTable().defaults().pad(20).padLeft(40).padRight(40);
		window.setMovable(false);
		window.setModal(true);
	
		//window.setBackground("frame");
		Label confirmation = new L10nLabel(theGame.getL10n(), "sure_want_use_tips", skin);
		confirmation.setAlignment(Align.center);
		window.text(confirmation);
		
		TextButton yes = new L10nButton(theGame.getL10n(), "yes", skin, "btn");
		window.button(yes);
		
		
		TextButton no = new L10nButton(theGame.getL10n(), "no", skin, "btn");
		window.button(no);
	//	window.pack();
		
		//stage.addActor(window);
		
		
		
		
		yes.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				window.remove();
				
				Game2 gm = theGame.getGame2Screen();
				gm.enableTips();
				theGame.setScreen(gm);
			}
			
		});
		
		
		no.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				
				window.hide();
			}
			
		});
	
		window.show(stage);
	
	}
	
	public static void turnonWalkthroughDialog(BaseScreen screen){

		if(!screen.getGame().getNative().getEnabledFeatures().contains(Feature.WALKTHROUGH)){
			walkthroughHandler2(screen);
		}
		else{
			proposeWalkthrough(screen);
		}
	}
	
	public static void failureDialog(String message, String details, Stage stage, TestGame theGame){
		
		final Dialog dialog = new Dialog("ERROR", theGame.getManager().getSkin(), "win");
		dialog.pad(10);
		//dialog.setSkin(theGame.getManager().getSkin());
		dialog.setModal(true);
		dialog.setMovable(false);
		
		String displayMessage = Util.isBlank(message) ? "unknown error" : message;
		dialog.getContentTable().add(displayMessage);		
		if(!Util.isBlank(details)){
		dialog.getContentTable().add(details);
		}
		
		Button button = new L10nButton(theGame.getL10n(), "cancel", theGame.getManager().getSkin(),"btn");
		button.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				dialog.hide();
			}
		});
		
		dialog.button(button);
		
		
		dialog.show(stage);
		
		
	}
	
	public static void messageDialog(String key,  Stage stage, TestGame theGame){
		
		final Dialog dialog = new Dialog("", theGame.getManager().getSkin(), "win");
		dialog.pad(10);
		//dialog.setSkin(theGame.getManager().getSkin());
		dialog.setModal(true);
		dialog.setMovable(false);
		
		Label displayMessage = new L10nLabel(theGame.getL10n(), key, theGame.getManager().getSkin() );
		dialog.getContentTable().add(displayMessage);		

		
		Button button = new L10nButton(theGame.getL10n(), "cancel", theGame.getManager().getSkin(),"btn");
		button.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				dialog.hide();
			}
		});
		
		dialog.button(button);
		
		
		dialog.show(stage);
		
		
	}
	
	
	public static void purchaseFailed(String message, Stage stage, TestGame theGame){
		
		final Dialog dialog = new Dialog("", theGame.getManager().getSkin());
		dialog.setModal(true);
		dialog.setMovable(false);
		
		dialog.add("Purchase failed!");
		dialog.add(message);
		
		Button button = new L10nButton(theGame.getL10n(), "cancel", theGame.getManager().getSkin(),"reset");
		button.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				dialog.hide();
			}
		});
		
		
		dialog.show(stage);
		
		
	}
	
	public static void endOfLight(final Game2 game, final Runnable closeDlgCallback){
		Skin s =  game.getGame().getManager().getSkin();
		final Dialog bubble = new Dialog("", s, "win");
		bubble.setModal(true);
		bubble.setMovable(false);
		//bubble.setBackground("bubble");	
		bubble.setName("end-of-game");

		
				Label label = new L10nLabel(game.getGame().getL10n(), "end_of_light", s, "small");
				label.setAlignment(Align.center);
				
			//	label.setWidth(700);
				//label.setWrap(true);
				float pad = 5;
				bubble.getContentTable() .add(label).pad(pad).row();
				
				TextButton close = new L10nButton(game.getGame().getL10n(), "close", s, "btn");
				bubble.button(close);
				close.addListener(new ClickListener()
				{
					public void clicked (InputEvent event, float x, float y) {
						bubble.hide();
						closeDlgCallback.run();
					}
					
				});

				bubble.show(game.getStage());
		
	}
	
	
	public static void winMoveDown(final Window winWindow, final boolean newBest, float swingDur, int swingCount, float delay, final Game2 g){
		Action winMoveDown = Actions.moveTo(g.getStage().getWidth()/2-winWindow.getWidth()/2, g.getStage().getHeight()/2-winWindow.getHeight()/2, 1f, Interpolation.elasticOut);						
		winWindow.addAction(Actions.sequence(Actions.delay(4*swingDur* swingCount+delay), Actions.sequence(winMoveDown, Actions.run(new Runnable() {
			
			@Override
			public void run() {

				
				if(newBest){
					Image newBestAsset = new Image(g.getGame().getManager().getAtlas().findRegion("best"));
					
					final float assetWidth = 150;
					final float assetHeight = 150;
					final float assetDuration = 1f;
					
					float originX = assetWidth/2,	 originY=  assetHeight/2;		

					

					newBestAsset.setOrigin(originX, originY);

					newBestAsset.setSize(assetWidth, assetHeight);
					newBestAsset.setPosition(-assetWidth/2, winWindow.getHeight()-assetHeight/2);
					
					
					Action assetParallel = Actions.parallel(Actions.moveTo(winWindow.getWidth()-assetWidth/2,winWindow.getHeight()-assetHeight/2 , assetDuration, Interpolation.sineOut),
							Actions.rotateBy(-360, assetDuration, Interpolation.sineOut)
							);
					newBestAsset.addAction(assetParallel);
					winWindow.addActor(newBestAsset);

				}
			}
		}))));
	}
	
	public static void endOfGame(final Game2 game){
		game.reset(false, new ResetCallback() {
			
			@Override
			public void onResetComplete() {
				
		
		Skin skin = game.getGame().getManager().getSkin();
				
		Window bubble = new Window("", skin, "win");
		bubble.setModal(false);
		bubble.setMovable(false);
		//bubble.setBackground("bubble");	
		bubble.setName("end-of-game");

		
				Label label = new L10nLabel(game.getGame().getL10n(), "end_of_game", skin);
				label.setAlignment(Align.center);
				
			//	label.setWidth(700);
				//label.setWrap(true);
				float pad = 5;
				bubble.add(label).pad(pad).row();
				
				Table join = joinButton(game.getGame());
				bubble.add(join);
				
				bubble.pack();
				
				game.uiLayer.addActor(bubble);
				//bubble.show(game.getStage());
				Util.center(bubble);
				
				bubble.toFront();
				bubble.getColor().a = 0;
				bubble.addAction(Actions.fadeIn(0.5f));
				game.controller.block();
			}
		});
	}
	
	public static void education(final Game2 game){
		Preferences p = game.getGame().getPreferences();
		game.getGame().getSession().setEducation(true);
		
		//Image hand = new Image(new TextureRegion(game.getGame().getManager().getAtlas().findRegion("hand")));
		//hand.setName("education");
		final Window bubble = new Window("", game.getGame().getManager().getSkin());
		bubble.setBackground("bubble");
		bubble.setName("education-bubble");
		L10nLabel l= new L10nLabel(game.getGame().getL10n(), "education", game.getGame().getManager().getSkin(), "small");
		l.setAlignment(Align.center);
		bubble.add(l).align(Align.center);
		bubble.pack();
		
		game.uiLayer.addActor(bubble);
		Util.center(bubble);
		bubble.moveBy(150, 150);
		bubble.getColor().a= 0 ;
		bubble.addAction(Actions.sequence(Actions.delay(1),  Actions.fadeIn(1)) );
		//bubble.addAction(Actions.moveBy(0, 750, 1));
		
		//game.uiLayer.addActor(hand);
	//	hand.setPosition(100, 100);
	//	hand.addAction(Actions.forever(Actions.sequence(Actions.moveBy(200, 0, 1), Actions.delay(0.5f), Actions.moveBy(-200, 0))));
		
		
		p.putBoolean(education_hand_done, true);
		p.flush();
	
		game.back.setVisible(false);
		game.controller.block();
		game.getStage().addAction(Actions.sequence(Actions.delay(5), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				bubble.addAction(Actions.sequence(Actions.fadeOut(1), Util.trigger(Actions.run(new Runnable() {
					
					@Override
					public void run() {
						bubble.remove();
						educationHand(game);
						game.controller.unblock();
						game.getGame().getSession().setEducation(false);
					}
				}), new Predicate() {
					
					@Override
					public boolean accept() {
						if(bubble.getColor().a == 0){

							return true;
						}
						return false;
					}
				})));

			}
		})));
	}
	
	
	public static void tryWalkthrough2(final Game2 game){
		
	}
	
	public static void tryWalkthrough(final Game2 game){
		
		game.controller.block();
		game.getStage().addAction(Actions.sequence(Actions.delay(1), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				game.controller.unblock();
				
			}
		})));

		ImageButton hand = new ImageButton(game.getGame().getManager().getSkin(), "footstep");
		
		final Window bubble = new Window("", game.getGame().getManager().getSkin());
		bubble.setBackground("bubble");
		bubble.setName("education-bubble");
		L10nLabel l= new L10nLabel(game.getGame().getL10n(), "propose_walkthrough", game.getGame().getManager().getSkin(), "small");
		l.setAlignment(Align.center);
		bubble.add(l).align(Align.center).row();
		bubble.add(hand).width(80).height(80) .align(Align.center);		
		bubble.pack();
	//	bubble.getColor().a = 0;
	//	bubble.addAction(Actions.fadeIn(1));
		
		
		hand.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				bubble.addAction(Actions.sequence(Actions.fadeOut(1),Actions.removeActor()));
				game.back.setVisible(true);
				Scripts.turnonWalkthroughDialog(game);

			}
		});
		
		game.uiLayer.addActor(bubble);
		Util.center(bubble);
		bubble.moveBy(150, 150-750);
		bubble.getColor().a= 0 ;
		bubble.addAction(Actions.fadeIn(1));
		bubble.addAction(Actions.moveBy(0, 750, 1, Interpolation.sineOut));
	
	
		game.back.setVisible(false);
	}

	public static void educationHand(Game2 game){
		Preferences p =game.getGame().getPreferences();
		
		Image hand = new Image(new TextureRegion(game.getGame().getManager().getAtlas().findRegion("hand")));
		hand.setName("education-hand");
		Window bubble = new Window("", game.getGame().getManager().getSkin());
		bubble.setBackground("bubble");
		bubble.setName("education-bubble");
		L10nLabel l= new L10nLabel(game.getGame().getL10n(), "education_hand", game.getGame().getManager().getSkin(), "small");
		l.setAlignment(Align.center);
		bubble.add(l).align(Align.center);
		bubble.pack();
		
		game.uiLayer.addActor(bubble);
		Util.center(bubble);
		bubble.moveBy(150, 150);
		bubble.getColor().a= 0 ;
		bubble.addAction(Actions.fadeIn(1));
		
		game.uiLayer.addActor(hand);
		hand.setPosition(100, 100);
		hand.addAction(Actions.forever(Actions.sequence(Actions.moveBy(200, 0, 1), Actions.delay(0.5f), Actions.moveBy(-200, 0))));
		
		
		p.putBoolean(education_hand_done, true);
		p.flush();
	
		game.back.setVisible(false);
	}

	
	public static void educationStepCancelling(Game2 game){
		Preferences p = game.getGame().getPreferences();
		
		Image hand = new Image(new TextureRegion(game.getGame().getManager().getAtlas().findRegion("hand")));
		
		hand.rotateBy(180);
		hand.setName("education-hand");
		Window bubble = new Window("", game.getGame().getManager().getSkin());
		bubble.setBackground("bubble");
		bubble.setName("education-bubble");
		bubble.getColor().a= 0 ;
		bubble.addAction(Actions.fadeIn(1));
		
		L10nLabel l= new L10nLabel(game.getGame().getL10n(), "education_step_cancel", game.getGame().getManager().getSkin(), "small");
		l.setAlignment(Align.center);
		bubble.add(l).align(Align.center);
		bubble.pack();
		
		game.uiLayer.addActor(bubble);
		Util.center(bubble);
		bubble.moveBy(-150, 150);
		
		game.uiLayer.addActor(hand);
		hand.setPosition(1050, 490);
		//hand.addAction(Actions.forever(Actions.sequence(Actions.moveBy(200, 0, 1), Actions.delay(0.5f), Actions.moveBy(-200, 0))));
		hand.addAction(Actions.forever(Actions.sequence(Actions.moveBy(0, -50, 0.5f), Actions.moveBy(0, 50, 0.5f))));
		
		
		p.putBoolean(education_step_cancel_done, true);
		p.flush();
	
		game.back.setVisible(false);
	}
	
	public static void educationWalkthrough(Game2 game){
		Preferences p = game.getGame().getPreferences();
		
		Image hand = new Image(new TextureRegion(game.getGame().getManager().getAtlas().findRegion("hand")));
		hand.setOrigin(hand.getWidth()/2, hand.getHeight()/2);
		hand.rotateBy(180);
		hand.setName("education-hand");
		Window bubble = new Window("", game.getGame().getManager().getSkin());
		bubble.defaults().pad(40).padTop(70);
		bubble.setBackground("bubble");
		bubble.setName("education-bubble");
		L10nLabel l= new L10nLabel(game.getGame().getL10n(), "education_walkthrough", game.getGame().getManager().getSkin(), "small");
		l.setAlignment(Align.center);
		bubble.add(l).align(Align.center);
		bubble.pack();
		
		game.uiLayer.addActor(bubble);
		Util.center(bubble);
		bubble.moveBy(200, 200);
		
		game.uiLayer.addActor(hand);
		//hand.setPosition(1050, 490);
		
		Util.center(hand);
		hand.moveBy(-50, -70);
		//hand.addAction(Actions.forever(Actions.sequence(Actions.moveBy(200, 0, 1), Actions.delay(0.5f), Actions.moveBy(-200, 0))));
		hand.addAction(Actions.forever(Actions.sequence(Actions.moveBy(0, -50, 0.5f), Actions.moveBy(0, 50, 0.5f))));
		
		
		p.putBoolean(education_walkthrough_done, true);
		p.flush();
	
		game.back.setVisible(false);
	}
	
	public static Table oneYangButton(final Dialog parent,final BaseScreen screen){
		final TestGame theGame = screen.getGame(); 
		Table fbTable = new Table();
		fbTable.defaults().align(Align.center);
		fbTable.defaults().pad(4);
		
		ImageButton fbButton = new ImageButton(theGame.getManager().getSkin(), "yy");
		float fbWidth = 100;
		fbButton.setSize(fbWidth, fbWidth);
		fbButton.addListener(oneYangListener(parent, screen));		
		fbTable.add(fbButton).width(fbWidth).height(fbWidth).row();
		
		String yy = theGame.getL10n().get("yang");
		Label shareLabel = new Label("1 "+yy ,theGame.getManager().getSkin(), "small");
		shareLabel.setAlignment(Align.center);
		
		fbTable.add(shareLabel);
		shareLabel.addListener(oneYangListener(parent,screen));
	
		fbTable.pack();
		return fbTable;
	}
	
	private static EventListener oneYangListener(final Dialog parent,final BaseScreen screen){
		return new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				TestGame theGame = screen.getGame();
				final Stage stage = screen.getStage();				
				parent.hide();
				
				Preferences prefs = theGame.getPreferences();
				Session s = theGame.getSession();
				int ownedYy = s.getOwnerYangs();
				
				
				if(ownedYy>0){
					prefs.putInteger(Values.yangs, ownedYy-1);
					prefs.putBoolean(Values.permanentWalkthrough(theGame.getGame2Screen().level), true);
					prefs.flush();
					s.setOwnedYangs(ownedYy-1);				
					
					screen.update();
					proposeWalkthrough(screen);
				}
				else{
					
					final Dialog moreYangs = moreYangsDlg(parent, screen);
					moreYangs.show(stage);
					
				}
				
			}
			
		};
	}
	
	public static Dialog moreYangsDlg(final Dialog parent,  final BaseScreen screen){
		Skin skin = screen.getGame().getManager().getSkin();
		final Dialog moreYangs = new Dialog("", screen.getGame().getManager().getSkin(), "win");
		moreYangs.getContentTable().pad(15);
		Label purchaseMoreYangs = new L10nLabel(screen.getGame().getL10n(), "purchase_more_yangs", screen.getGame().getManager().getSkin());
		purchaseMoreYangs.setAlignment(Align.center);
		
		moreYangs.getContentTable().add(purchaseMoreYangs);
		Button yes = new L10nButton(screen.getGame().getL10n(), "yes", skin, "btn");
		Button no = new L10nButton(screen.getGame().getL10n(), "no", skin, "btn");
		
		moreYangs.getButtonTable().add(yes).padRight(50);
		yes.addListener(shopListener(moreYangs, parent, screen));
							
		moreYangs.button(no);
		no.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				moreYangs.hide();
				if(parent!=null){
					parent.show(screen.getStage());
				}
			}
			
		});
		
		return moreYangs;
	}
	
	public static Table shopButton(Dialog parent,  final BaseScreen screen){
		TestGame theGame = screen.getGame();
		
		Table fbTable = new Table();
		fbTable.defaults().align(Align.center);
		fbTable.defaults().pad(4);
		
		ImageButton fbButton = new ImageButton(theGame.getManager().getSkin(), "shopping");
		float fbWidth = 120;
		fbButton.setSize(fbWidth, fbWidth);
		fbButton.addListener(shopListener(parent,parent, screen));		
		fbTable.add(fbButton).width(fbWidth).height(fbWidth).row();
		
		Label shareLabel = new L10nLabel(theGame.getL10n(), "shopping",theGame.getManager().getSkin(), "small");
		shareLabel.setAlignment(Align.center);
		
		fbTable.add(shareLabel);
		shareLabel.addListener(shopListener(parent,parent,screen));
	
		fbTable.pack();
		return fbTable;
	}
	
	private static EventListener shopListener(final Dialog parent, final Dialog target,  final BaseScreen screen){
		return new ClickListener(){

			public void clicked (InputEvent event, float x, float y) {
				final Stage stage = screen.getStage();
				final TestGame g = screen.getGame();
				final Dialog waitDlg = Scripts.waitDlg(g);
				waitDlg.show(stage);
				
				if(parent!=null){
					parent.hide();
				}
				g.getNative().getPrices(new GetPricesCallback() {
					
					@Override
					public void onFailure(String message) {
						waitDlg.hide();
						
						failureDialog("Get prices failed!", message, stage, g);
					}
					
					@Override
					public void onResult(Map<Feature, String> prices) {
						waitDlg.hide();
						if(parent!=null){
							parent.hide();
						}
						
						Skin s = g.getManager().getSkin();
						final Dialog pricesDlg = new Dialog("", s,"win");
						pricesDlg.getContentTable().pad(15);
						//dlg.getContentTable().defaults().pad(15);
						String style = "button";
						String labelStyle= "huge";
						
						if(prices.get(Feature.YANG_10)!=null)
						{
							Label label = new Label("10", s, labelStyle);
							Image image = new Image(g.getManager().getAtlas().findRegion("yy"));
							float size = 60;
							image.setSize(size, size);					
							TextButton buy = new TextButton(prices.get(Feature.YANG_10), s, style);
							buy.addListener(new ClickListener(){
								public void clicked (InputEvent event, float x, float y) {
									waitDlg.show(stage);
									g.getNative().purchase(Feature.YANG_10, new IActivityRequestHandler.PurchaseCallback() {
										
										@Override
										public void onFailure(String message) {
											waitDlg.hide();
											Scripts.failureDialog("Purchase failed!", message, stage, g);
										}
										
										@Override
										public void onResult(boolean ok) {
											waitDlg.hide();
											if(ok){
												Preferences prefs = g.getPreferences();
												final int ownedYangs = g.getSession().getOwnerYangs();
												final int yangsAquired = 10;
												final int totalYangs = ownedYangs + yangsAquired;
												prefs.putInteger(Values.yangs, totalYangs);
												prefs.flush();
												
												g.getSession().setOwnedYangs(totalYangs);
												pricesDlg.hide();
												if(target!=null){
													target.show(stage);
												}
												screen.update();
											}
										}

										@Override
										public void alreadyPurchased() {
											waitDlg.hide();
											messageDialog("already_purchased", stage, g);
											
										}
									});
								}
							});
							
							Table row = new Table();
							
							float p = 15;
							row.add(label).padRight(p);
							row.add(image).padRight(p*4);
							row.add(buy);
							row.pack();
							
							pricesDlg.getContentTable().add(row).row();
						}
						
						if(prices.get(Feature.YANG_30)!=null)
						{
							Label label = new Label("30", s, labelStyle);
							Image image = new Image(g.getManager().getAtlas().findRegion("yy"));
							float size = 60;
							image.setSize(size, size);					
							TextButton buy = new TextButton(prices.get(Feature.YANG_30), s, style);
							buy.addListener(new ClickListener(){
								public void clicked (InputEvent event, float x, float y) {
									waitDlg.show(stage);
									g.getNative().purchase(Feature.YANG_30, new IActivityRequestHandler.PurchaseCallback() {
										
										@Override
										public void onFailure(String message) {
											waitDlg.hide();
											Scripts.failureDialog("Purchase failed!", message, stage, g);
										}
										
										@Override
										public void onResult(boolean ok) {
											waitDlg.hide();
											if(ok){
												Preferences prefs = g.getPreferences();
												final int ownedYangs = g.getSession().getOwnerYangs();
												final int yangsAquired = 30;
												final int totalYangs = ownedYangs + yangsAquired;
												prefs.putInteger(Values.yangs, totalYangs);
												prefs.flush();
												
												g.getSession().setOwnedYangs(totalYangs);
												pricesDlg.hide();
												if(target!=null){
													target.show(stage);
												}
												screen.update();
											}
										}

										@Override
										public void alreadyPurchased() {
											waitDlg.hide();
											messageDialog("already_purchased", stage, g);
											
										}
									});
								}
							});
							
							Table row = new Table();
							float p = 15;
							row.add(label).padRight(p);
							row.add(image).padRight(p*4);
							row.add(buy);
							row.pack();
							
							pricesDlg.getContentTable().add(row).row();
						}
						
						if(prices.get(Feature.YANG_50)!=null)
						{
							Label label = new Label("50", s,labelStyle);
							Image image = new Image(g.getManager().getAtlas().findRegion("yy"));
							float size = 60;
							image.setSize(size, size);					
							TextButton buy = new TextButton(prices.get(Feature.YANG_50), s, style);
							buy.addListener(new ClickListener(){
								public void clicked (InputEvent event, float x, float y) {
									waitDlg.show(stage);
									g.getNative().purchase(Feature.YANG_50, new IActivityRequestHandler.PurchaseCallback() {
										
										@Override
										public void onFailure(String message) {
											waitDlg.hide();
											Scripts.failureDialog("Purchase failed!", message, stage, g);
										}
										
										@Override
										public void onResult(boolean ok) {
											waitDlg.hide();
											if(ok){
												Preferences prefs = g.getPreferences();
												final int ownedYangs = g.getSession().getOwnerYangs();
												final int yangsAquired = 50;
												final int totalYangs = ownedYangs + yangsAquired;
												prefs.putInteger(Values.yangs, totalYangs);
												prefs.flush();
												
												g.getSession().setOwnedYangs(totalYangs);
												pricesDlg.hide();
												if(target!=null){
													target.show(stage);
												}
												screen.update();
											}
										}

										@Override
										public void alreadyPurchased() {
											waitDlg.hide();
											messageDialog("already_purchased", stage, g);

										}
									});
								}
							});
							
							Table row = new Table();
							float p = 15;
							row.add(label).padRight(p);
							row.add(image).padRight(p*4);
							row.add(buy);
							row.pack();
							
							pricesDlg.getContentTable().add(row).row();
						}
						
						Button close = new L10nButton(g.getL10n(), "cancel", s,"btn");
						close.addListener(new ClickListener(){
							public void clicked (InputEvent event, float x, float y) {
								pricesDlg.hide();
								if(target!=null){
									target.show(stage);
								}
								
							}
						});
						pricesDlg.button(close);
						
						pricesDlg.show(stage);
					}
					
					
				});
				
			}
		};

	}
	
	
	 static Table joinButton(IGame game){
		Table fbTable = new Table();
		fbTable.defaults().align(Align.center);
		fbTable.defaults().pad(4);
		
		ImageButton fbButton = new ImageButton(game.getManager().getSkin(), "fb");
		float fbWidth = 75;
		fbButton.setSize(fbWidth, fbWidth);
		fbButton.addListener(joinListener(game));		
		fbTable.add(fbButton).row();
		
	//	Label shareLabel = new L10nLabel(getGame().getL10n(), "joinus", getGame().getManager().getSkin(), "small");
	//	shareLabel.setAlignment(Align.center);
		
	//	fbTable.add(shareLabel);
	//	shareLabel.addListener(joinListener());
	
		fbTable.pack();
		return fbTable;
		
	
	}
	 

	

	static private EventListener joinListener(final IGame game){
		return new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				Gdx.app.log(game.getClass().getName(), "join us on Facebook");
				
				game.getNative().joinUs(TestGame.yypuzzleUrl);
			}
		};
	}

	public static boolean lightDone(TestGame game){
		Preferences p = game.getPreferences();
		boolean lightDone = true;
		for(int i = 1; i<=60; i++){					
			if(!p.getBoolean("is_done_"+i) ){
				lightDone = false;
			}
		}
		return lightDone;
	}
	
	public static final String education_walkthrough_done = "education_walkthrough";
	public static final String education_hand_done = "education_hand_done";
	public static final String education_step_cancel_done = "education_step_cancel_done";
}
