package com.me.yin_yang;

import java.awt.Desktop;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Base64Coder;
import com.me.test.Feature;
import com.me.test.IActivityRequestHandler;
import com.me.test.TestGame;
import com.me.test.Util;

public class Main implements IActivityRequestHandler{
	
	Main(String[] argc){
		for(String arg : argc){
				if("dev".equals(arg)){
					dev = true;
				}
				else if("clear".equals(arg)){
					clear = true;
				}
			
		}
	}
	
	private boolean dev;
	private boolean clear;
	
	public static void main(String[] args) {
		

		
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Yin & Yang puzzle";
		//cfg.useGL20 = true;
	//cfg.fullscreen = true;
		cfg.width = 1024;
		cfg.height = 768;
		
		new LwjglApplication(new TestGame(new Main(args)), cfg);
	}
	
	

	@Override
	public void showAdd(int id, boolean show) {
		// do nothing
		
	}

	private Map<Feature, String> prices = new HashMap<Feature, String>();
	{
		prices.put(Feature.LEVELPACK, "0.91 $");
		prices.put(Feature.WALKTHROUGH, "70 руб.");
		prices.put(Feature.YANG_10, "21 000 VND");
		prices.put(Feature.YANG_30, "41 000 VND");
		prices.put(Feature.YANG_50, "911 000 VND");
	}

	@Override
	public void getPrices(final GetPricesCallback callback) {
		
		Executors.newSingleThreadExecutor().submit(new Runnable() {
			
			@Override
			public void run() {
			
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				callback.onResult(prices);
				
			}
		});
	
		
		
	}



	@Override
	public void purchase(final Feature feature, final PurchaseCallback callback) {
		
		Executors.newSingleThreadExecutor().submit(new Runnable() {
			
			@Override
			public void run() {
			
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				enabledFeatures.add(feature);
				
				callback.onResult(true);
				
			}
		});
		

		
	}



	@Override
	public Set<Feature> getEnabledFeatures() {
		
		return enabledFeatures;
	}


	Set<Feature> enabledFeatures = new HashSet<Feature>();


	@Override
	public void rate() {

		if(Desktop.isDesktopSupported())
		{
			try{
				Desktop.getDesktop().browse(new URI("http://www.google.com/"));
			}
			catch(Exception ex){
				System.err.println(ex);
			}
		}
	
	
		
	}



	@Override
	public void checkInternetConnection(OnlineStatusCallback callback) {
		callback.call(true);
		
	}







	@Override
	public void shareOnFacebook2(String title, String desc, ShareCallback clb) {
		clb.success();
		
	}



	@Override
	public void likeOnFacebook() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void tweet(String tweetText) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void showVideoAd(VideoCompletedStatusCallback clb) {
		//clb.call(true);
		//clb.noVideoAvailable();
		clb.call(true);
		
	}





	@Override
	public void joinUs(String url) {
		// TODO Auto-generated method stub
		openWebPage(url) ;
	}



	private static void openWebPage(String address) {
		if(Desktop.isDesktopSupported())
		{
			try{
				Desktop.getDesktop().browse(new URI(address));
			}
			catch(Exception ex){
				System.err.println(ex);
			}
		}
	
	}



	@Override
	public void getInventory(InventoryCallback callback) {
		
	
		callback.onResult(enabledFeatures);
		
	}



	@Override
	public boolean clear() {
		return clear;
	}



	@Override
	public boolean dev() {
		return dev;
	}





/*	@Override
	public void purchaseLevelPack(PurchaseLevelPackCallback callback) {
		// TODO Auto-generated method stub
		
	}*/
}
