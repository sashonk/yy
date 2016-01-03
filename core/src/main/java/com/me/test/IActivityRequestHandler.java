package com.me.test;

import java.util.Map;
import java.util.Set;

public interface IActivityRequestHandler {

	public boolean clear();
	
	public boolean dev();

	public void showAdd(int id, boolean show);

//	public void getLevelPackPrice(GetLevelPackPriceCallback callback);
	
	public void getPrices(GetPricesCallback callback);
	
	public static interface GetPricesCallback extends IErrorHandler{
		public void onResult(Map<Feature, String> prices);
	}
	
	public static interface InventoryCallback extends IErrorHandler{
		public void onResult(Set<Feature> prices);
	}
	
	public void purchase(Feature feature, PurchaseCallback callback);
	
	public static interface PurchaseCallback extends IErrorHandler{
		void onResult(boolean ok);
		
		void alreadyPurchased();
	}
	
	public void rate();
	
	public Set<Feature> getEnabledFeatures();
	
	
	public void checkInternetConnection(OnlineStatusCallback callback);
	
	public static interface OnlineStatusCallback{
		public void call(boolean online);
	}
	
	
	
	public void shareOnFacebook2(String title, String desc, ShareCallback shareClb);
	
	public static interface ShareCallback extends IErrorHandler{
		void success();
	}
	
	public void likeOnFacebook();
	
	public void tweet(String tweetText);
	
	public interface VideoCompletedStatusCallback{
		public void call(boolean completed);
		
		public void noVideoAvailable();
	}
	
	public void showVideoAd(VideoCompletedStatusCallback clb);

	public void joinUs(String url);
	
	
	public void getInventory(InventoryCallback callback);

}


