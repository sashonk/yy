package com.me.test.game2;

import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.Feature;
import com.me.test.IActivityRequestHandler.GetPricesCallback;
import com.me.test.IActivityRequestHandler.InventoryCallback;
import com.me.test.IActivityRequestHandler.PurchaseCallback;
import com.me.test.L10nButton;
import com.me.test.L10nLabel;
import com.me.test.TestGame;
import com.me.test.Util;

public class ChooseList  extends BaseMenuScreen{

	public ChooseList(final TestGame g) {
		super(g);
		final Skin s = g.getManager().getSkin();
		
		Table table = new Table();
		//String style = "huge";
		
		Table lightTable = new Table();
		lightTable.align(Align.center);
		Label levelOfLight = new L10nLabel(g.getL10n(), "levels_of_light", s);
		levelOfLight.setAlignment(Align.center);
		Image light = new Image(g.getManager().getAtlas().findRegion("light"));
		float dim = 200;
		light.setSize(dim, dim);
		lightTable.add(levelOfLight).row();
		lightTable.add(light);
		lightTable.pack();
		
		Table darkTable = new Table();
		darkTable.align(Align.center);
		Label levelOfDark = new L10nLabel(g.getL10n(), "levels_of_dark", s);
		levelOfDark.setAlignment(Align.center);
		Image dark = new Image(g.getManager().getAtlas().findRegion("dark"));
		dark.setSize(dim, dim);
		darkTable.add(levelOfDark).row();
		darkTable.add(dark);
		darkTable.pack();
		
		
		table.add(lightTable).padRight(110);
		table.add(darkTable);	
		table.pack();
		
		getStage().addActor(table);
		Util.center(table);
		

		
		light.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				g.setScreen(g.getChooseLevelScreen());
			}
			
		});
		
		
		
		dark.addListener(new ClickListener(){
			public void clicked (InputEvent event, float x, float y) {
				final Dialog wait = Scripts.waitDlg(getGame());
				wait.show(getStage());
				getGame().getNative().getInventory(new InventoryCallback() {
					
					@Override
					public void onFailure(final String message) {
						Gdx.app.postRunnable(new Runnable() {
							
							@Override
							public void run() {
								wait.hide();
								Scripts.failureDialog("ERROR", message, stage, getGame());
							}
						});
					
						
					}
					
					@Override
					public void onResult(final Set<Feature> features) {
						Gdx.app.postRunnable(new Runnable() {
							
							@Override
							public void run() {
								
								if(features.contains(Feature.LEVELPACK)){
									wait.hide();
									if(Scripts.lightDone(getGame()) || true){
										g.setScreen(g.getChooseLevelScreen2());
									}
									else{
										
										final Dialog d = new Dialog("", s, "win");
										d.setModal(true);
										d.setMovable(false);
										Label mustDoLight = new L10nLabel(g.getL10n(), "must_do_light", s);
										mustDoLight.setAlignment(Align.center);
										d.getContentTable().add(mustDoLight).pad(5).row();
										
										TextButton close = new L10nButton(g.getL10n(), "close", s, "btn");
										d.button(close);
										close.addListener(new ClickListener(){
											public void clicked (InputEvent event, float x, float y) {
												d.hide();
											}
										});
										
										d.show(getStage());
									}
								}
								else{
									
									getGame().getNative().getPrices(new GetPricesCallback() {
										
										@Override
										public void onFailure(String message) {
												Gdx.app.postRunnable(new Runnable() {
												
													@Override
													public void run() {
														wait.hide();
														// TODO Auto-generated method stub
														
													}
											});
										}
										
										@Override
										public void onResult(final Map<Feature, String> prices) {
											Gdx.app.postRunnable(new Runnable() {
												
												@Override
												public void run() {
													wait.hide();
													if(!prices.containsKey(Feature.LEVELPACK)){
														Scripts.failureDialog("no price", "", stage, getGame());
													}
													else{
														final Dialog d = new Dialog("", s, "win");
														d.getButtonTable().defaults().padLeft(40).padRight(40);
														d.getContentTable().defaults().padLeft(20).padRight(20);
														d.setModal(true);
														d.setMovable(false);
														Label darkCost = new L10nLabel(g.getL10n(), "dark_cost", s, "default",false, prices.get(Feature.LEVELPACK));
														darkCost.setAlignment(Align.center);
														d.getContentTable().add(darkCost).pad(5).row();
														
														TextButton yes = new L10nButton(g.getL10n(), "yes", g.getManager().getSkin(), "btn");
														d.button(yes);
														yes.addListener(new ClickListener(){
															@Override
															public void clicked(InputEvent ev , float x ,float y){
																d.hide();
																final Dialog wait =  Scripts.waitDlg(getGame());
																//wait.show(getStage());
																getGame().getNative().purchase(Feature.LEVELPACK, new PurchaseCallback() {
																	
																	@Override
																	public void onFailure(final String message) {
																		Gdx.app.postRunnable(new Runnable() {
																			
																			@Override
																			public void run() {
																				wait.hide();
																				Scripts.failureDialog("ERROR", message, stage, getGame());
																				
																			}
																		});
																		
																	}
																	
																	@Override
																	public void onResult(final boolean ok) {
																			Gdx.app.postRunnable(new Runnable() {
																			
																			@Override
																			public void run() {
																				wait.hide();
																				if(!ok){
																					Scripts.failureDialog("ERROR", "unknown", stage, getGame());
																				}
																			}
																		});
																		
																	}

																	@Override
																	public void alreadyPurchased() {
																		wait.hide();
																		Scripts.messageDialog("already_puchased", stage, getGame());
																		
																	}
																});
															}
														});
														
														TextButton no = new L10nButton(g.getL10n(), "no", g.getManager().getSkin(), "btn");
														no.addListener(new ClickListener(){
															@Override
															public void clicked(InputEvent ev , float x ,float y){
																d.hide();
															}
														});
														d.button(no);									
														d.show(getStage());
													}
													
													
													
												}
											});
											
										}
									});
									

									
									
								}
								
								
							}
						});
						
						

						
					}
				});
		

				
				
			}
			
		});
	}
	
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "chooseList";
	}

	

	public boolean hasBackButton(){
		return true;
	}
}
