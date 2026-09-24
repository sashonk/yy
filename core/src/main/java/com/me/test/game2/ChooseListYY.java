package com.me.test.game2;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.test.*;

public class ChooseListYY  extends BaseMenuScreen{
	Label ownedYangsLabel;
	
	/** set when the player tried to buy the dark side but lacked yangs;
	 * after buying yangs the deal completes automatically (TODO.txt item 2) */
	boolean pendingDarkPurchase;
	
	public void show(){
		super.show();
		update();
	}
	
	@Override
	public void update() {
		ownedYangsLabel.setText(Integer.toString(getGame().getSession().getOwnerYangs()));
		
		if(!pendingDarkPurchase){
			return;
		}
		
		Preferences prefs = getGame().getPreferences();
		
		// already unlocked (e.g. bought elsewhere) - just go there
		if(prefs.getBoolean(Values.levelPack, false)){
			pendingDarkPurchase = false;
			goDark();
			return;
		}
		
		int owned = getGame().getSession().getOwnerYangs();
		if(owned >= Values.levelPackYins){
			pendingDarkPurchase = false;
			int result = owned - Values.levelPackYins;
			getGame().getSession().setOwnedYangs(result);
			prefs.putInteger(Values.yangs, result);
			prefs.putBoolean(Values.levelPack, true);
			prefs.flush();
			goDark();
		}
	}
	
	/** switch to the dark levels screen after a successful purchase */
	void goDark(){
		getStage().addAction(Actions.sequence(Actions.delay(1, Actions.run(new Runnable() {
			@Override
			public void run() {
				getGame().setScreen(getGame().getChooseLevelScreen2());
			}
		}))));
	}
	


	public ChooseListYY(final TestGame g) {
		super(g);
		
		ownedYangsLabel = yangPanel(getStage().getRoot(), g);

		
		
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
				
				final Preferences prefs = getGame().getPreferences();
				if(prefs.getBoolean(Values.levelPack, false)){
					g.setScreen(g.getChooseLevelScreen2());
				}
				else{

					final Dialog d = new Dialog("", s, "win");
					d.getButtonTable().defaults().padLeft(40).padRight(40);
					d.getContentTable().pad(10);
					d.setModal(true);
					d.setMovable(false);
					Label darkCost = new L10nLabel(g.getL10n(), "dark_cost", s, "default");
					darkCost.setAlignment(Align.center);
					d.getContentTable().add(darkCost).pad(5).row();
					
					String labelStyle= "huge";
					Label label = new Label("10", s, labelStyle);
					Image image = new Image(g.getManager().getAtlas().findRegion("yy"));
					float size = 60;
					image.setSize(size, size);	
					
					Table yy10 = new Table();
					yy10.defaults().pad(3);
					yy10.add(label);
					yy10.add(image);
					yy10.pack();
					d.getContentTable().add(yy10).row();
					
					TextButton yes = new L10nButton(g.getL10n(), "yes", g.getManager().getSkin(), "btn");
					d.button(yes);
					yes.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent ev , float x ,float y){
							d.hide();
							int ownedYangs = getGame().getSession().getOwnerYangs();
							if(ownedYangs>=Values.levelPackYins){
								int resultYangs= ownedYangs-Values.levelPackYins;
								getGame().getSession().setOwnedYangs(resultYangs);
								prefs.putInteger(Values.yangs, resultYangs);
																
								prefs.putBoolean(Values.levelPack, true);
								prefs.flush();
								
								update();
								getStage().addAction(Actions.sequence(Actions.delay(1, Actions.run(new Runnable() {
									
									@Override
									public void run() {
										g.setScreen(g.getChooseLevelScreen2());
										
									}
								}))));
								
							}
							else{
								// remember intent: after buying yangs in the shop,
								// update() completes the purchase automatically
								ChooseListYY.this.pendingDarkPurchase = true;
								Dialog moreYangs =  Scripts.moreYangsDlg(null, ChooseListYY.this);
								moreYangs.show(getStage());
							}
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
	
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "chooseList";
	}

	

	public boolean hasBackButton(){
		return true;
	}
}
