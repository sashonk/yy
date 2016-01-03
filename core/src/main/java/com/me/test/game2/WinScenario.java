package com.me.test.game2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.me.test.Pair;
import com.me.test.ParticleEffectActor;
import com.me.test.Util;
import com.me.test.game2.DemoScript.FirePositionTriple;

public class WinScenario {


	public static SequenceAction emitterActions(final Game2 g2, float swingDur, float delay, int swingCount){
		

		
		
		EmitterAction ea = new EmitterAction();
		
		for(int i = 0; i<2; i++){
			final FirePositionTriple trio = DemoScript.firePositions.get(new Random(System.currentTimeMillis()).nextInt(3)) ;
			final LinkedList<Pair<Float, Float>> values = new LinkedList<Pair<Float, Float>>();
			values.add(trio._p1);
			values.add(trio._p2);
			values.add(trio._p3);
			Collections.shuffle(values);

			ea.addAction(Actions.delay(2*swingDur* swingCount+delay));
			ea.addAction(Actions.run(new Runnable() {
				
				@Override
				public void run() {
					//fireRedActor.start();
					PooledEffect starEff= g2.redPool.obtain();
					//starEff.scaleEffect(0.5f);
					//starEff.setDuration(10000);
					//starEff.scaleEffect(1);
					ParticleEffectActor a = new ParticleEffectActor(starEff);
					g2.uiLayer.addActor(a);
					a.toFront();
					a.setName("effect-"+System.currentTimeMillis());
					Pair<Float, Float> pair = values.pollLast();
					a.setPosition(pair.getFirst(), pair.getSecond());
					//effects.add(starEff);
					a.start();
					
				}
			}));
			ea.addAction(Actions.delay(0.3f));
			ea.addAction(Actions.run(new Runnable() {
									
									@Override
									public void run() {
										PooledEffect starEff= g2.greenPool.obtain();
										ParticleEffectActor a = new ParticleEffectActor(starEff);
										g2.uiLayer.addActor(a);
										a.toFront();
										a.setName("effect-"+System.currentTimeMillis());
										Pair<Float, Float> pair = values.pollLast();
										a.setPosition(pair.getFirst(), pair.getSecond());
										//effects.add(starEff);
										a.start();
										
									}
								}));
			
			ea.addAction(Actions.delay(0.3f));
			ea.addAction(Actions.run(new Runnable() {
									
									@Override
									public void run() {
										PooledEffect starEff= g2.bluePool.obtain();
										ParticleEffectActor a = new ParticleEffectActor(starEff);
										g2.uiLayer.addActor(a);
										a.toFront();
										a.setName("effect-"+System.currentTimeMillis());
										Pair<Float, Float> pair = values.pollLast();
										a.setPosition(pair.getFirst(), pair.getSecond());
										//effects.add(starEff);
										a.start();
										
									}
								}));
		}
		return ea;
	}
	
	

	public static SequenceAction starAction(final Game2 g2){
		
		EmitterAction ea = new EmitterAction();
		ea.addAction(Actions.delay(3f));
		ea.addAction( Actions.run(new Runnable() {
			
			@Override
			public void run() {
				//starActor.start();
				PooledEffect starEff= g2.starPool.obtain();
				//starEff.reset();
				ParticleEffectActor a = new ParticleEffectActor(starEff);
				g2.uiLayer.addActor(a);
				a.toBack();
				a.setName("effect-"+System.currentTimeMillis());
				Util.center(a);
				//effects.add(starEff);
				a.start();
			}
		}));
		
	
		return ea;

		

	}
	
	
	public static class EmitterAction extends SequenceAction{
		
	}
}
