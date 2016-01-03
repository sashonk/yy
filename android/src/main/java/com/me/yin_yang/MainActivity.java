package com.me.yin_yang;
 
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.facebook.widget.LikeView;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.heyzap.sdk.ads.HeyzapAds;
import com.heyzap.sdk.ads.HeyzapAds.OnIncentiveResultListener;
import com.heyzap.sdk.ads.IncentivizedAd;
import com.me.test.Feature;
import com.me.test.IActivityRequestHandler;
import com.me.test.TestGame;
import com.me.yin_yang.util.IabHelper;
import com.me.yin_yang.util.IabHelper.QueryInventoryFinishedListener;
import com.me.yin_yang.util.IabResult;
import com.me.yin_yang.util.Inventory;
import com.me.yin_yang.util.Purchase;
import com.me.yin_yang.util.SkuDetails;

public class MainActivity extends AndroidApplication implements IActivityRequestHandler{
	  private static final String AD_UNIT_ID = "ca-app-pub-4198140113968724/8806620094";
	  private static final String EXPLAY = "02BD43C728CE8855D954F242C3C36266";
	  private static final String LICENCE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhakA1rwYFyVrANKzBy8w6HYh7pNqw+vSJEGRWsKDFCoPuqFHJo3+l3Ne1S5i5bipe/R2+h6TYGNMgXE6GAXba8SFhuE8WSAeUPAs1aqkozpQcaSAzRrp/pqSNrJBrGQBqoPnIwztI6BUHSmRAKnpNdpUQViTyaLNn7dbxWjsrbaGINf2W+y8gyd1oSu4pOxKzjDrDzYVBEw/lur9gYum8ncGRottmt4MSVfQdrjeBbcVWoCXiCye7fG5NIzAsgs7IQVjhJtTaTiuH6+e6CCxJHuS476yWy+N00njIMscHQ/Px0o3AW8o6f5lFMbUlShcSlQpcSQQ/FOlR8VLkeL92QIDAQAB";
	  private static final String SKU_LEVELPACK = "levelpack";
	  private static final String SKU_WALKTHROUGH = "walkthrough";
	  private static final String SHARE_PICTURE = "https://scontent-lhr.xx.fbcdn.net/hphotos-xfp1/v/t1.0-9/11034264_1562490857342314_2024482528349001198_n.png?oh=78096634d420687e8d3f91cced38902c&oe=5592D45A";

	  private static final String SKU_YANG10= "yang10";
	  private static final String SKU_YANG30 = "yang30";
	  private static final String SKU_YANG50 = "yang50";

	  private static final String appId = "com.me.yin_yang";
	  private static final String GOOGLE_PLAY_YY = "https://play.google.com/store/apps/details?id="+appId;
	  protected AdView banner1;
	  protected AdView banner2;
	  protected AdView adView;
	  protected View gameView;
	
	    static final int RC_REQUEST = 10001;
	    
	    private IabHelper mHelper;
	    private UiLifecycleHelper uiHelper;
	  

	    
		@Override
		public void showAdd(final int id, final boolean show) {
			this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					View view = findViewById(id);
					if(show){
						view.setVisibility(View.VISIBLE);
					}
					else{
						view.setVisibility(View.GONE);
					}
					
				}
			});
			
		
			
		}
		

		
		
		@Override
		public void getPrices(final GetPricesCallback callback) {
			
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//mHelper.flagEndAsync();
		        mHelper.queryInventoryAsync(true, Arrays.asList(SKU_LEVELPACK, SKU_WALKTHROUGH, SKU_YANG10, SKU_YANG30, SKU_YANG50), new QueryInventoryFinishedListener() {
					
					@Override
					public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
					      if (result.isFailure()) {
					         Log.e(TAG, "Get prices failed");
					         String msg = result.getMessage();
					         if(msg==null || msg.trim().length()==0){
					        	 msg = "Get prices failed";
					         }
					         callback.onFailure(msg);
					        	 
					         return;
					      }
					      
					      
					      Map<Feature, String> callbackResult = new HashMap<Feature, String>();
					      SkuDetails detailsLevelpack = inventory.getSkuDetails(SKU_LEVELPACK);
					      if(detailsLevelpack!=null){
					    	  String levelPackPrice =
					    			  detailsLevelpack.getPrice();
					    	  callbackResult.put(Feature.LEVELPACK, levelPackPrice);
					      }
					      
					      SkuDetails detailsWalkthrough = inventory.getSkuDetails(SKU_WALKTHROUGH);
					      if(detailsWalkthrough!=null){
					    	  String walkthroughPrice =
					    			  detailsWalkthrough.getPrice();
					    	  callbackResult.put(Feature.WALKTHROUGH, walkthroughPrice);
					      }
					      SkuDetails detailsYang10 = inventory.getSkuDetails(SKU_YANG10);
					      if(detailsYang10!=null){
					    	  String price =
					    			  detailsYang10.getPrice();
					    	  callbackResult.put(Feature.YANG_10, price);
					      }
					      
					      SkuDetails detailsYang30 = inventory.getSkuDetails(SKU_YANG30);
					      if(detailsYang30!=null){
					    	  String price =
					    			  detailsYang30.getPrice();
					    	  callbackResult.put(Feature.YANG_30, price);
					      }					      
					      SkuDetails detailsYang50 = inventory.getSkuDetails(SKU_YANG50);
					      if(detailsYang50!=null){
					    	  String price =
					    			  detailsYang50.getPrice();
					    	  callbackResult.put(Feature.YANG_50, price);
					      }		
					      
			
					      
					      callback.onResult(callbackResult);
					}
				});
			}
		});

					
	


			
		
        }
	    
	  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TAG = getPackageName();
  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        
	    RelativeLayout layout = new RelativeLayout(this);
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	    layout.setLayoutParams(params);

	//    adView = createAdView();
	//    layout.addView(adView);

	    
	    FrameLayout frameLayout = new FrameLayout(this);
        RelativeLayout.LayoutParams frameParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        frameParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        frameParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
     //   frameParams.addRule(RelativeLayout.BELOW, adView.getId());
	    frameLayout.setLayoutParams(frameParams);
	    layout.addView(frameLayout);
	    
	    
	    gameView = createGameView(cfg);
	    frameLayout.addView(gameView);
	    
/*	    createBanner1();
	    frameLayout.addView(banner1);
	    
	    createBanner2();
	    frameLayout.addView(banner2);*/
	    
	    setContentView(layout);
/*	    adView.loadAd(buildRequest());
	    banner1.loadAd(buildRequest());
	    banner2.loadAd(buildRequest());
	    
	    banner1.setVisibility(View.GONE);
	    banner2.setVisibility(View.GONE);*/
	    
	  //  adView.setVisibility(View.GONE);
        
        setContentView(layout);
        
  	    //adView.loadAd(buildRequest());
        
        
        
        String base64EncodedPublicKey= loadLicenceKey();

        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
        	   public void onIabSetupFinished(IabResult result) {
        	      if (!result.isSuccess()) {
        	         // Oh noes, there was a problem.
        	         Log.d(TAG, "Problem setting up In-app Billing: " + result);
        	      }
        	         // Hooray, IAB is fully set up!
        	      
        	      Log.d(TAG, "Setup In-app Billing sucessfully");


    				//mHelper.flagEndAsync();
      		        mHelper.queryInventoryAsync(true, Arrays.asList(SKU_LEVELPACK), new QueryInventoryFinishedListener() {
      					
      					@Override
      					public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
      					      if (result.isFailure()) {
      					         Log.e(TAG, "Get prices failed");
      					         String msg = result.getMessage();
      					         if(msg==null || msg.trim().length()==0){
      					        	 msg = "Get inventory failed";
      					         }
      					         
      					        	 
      					         return;
      					      }
      					      
      					      
      					      Purchase detailsYang10 = inventory.getPurchase(SKU_YANG10);
      					      Purchase detailsYang30 = inventory.getPurchase(SKU_YANG30);
      					      Purchase detailsYang50 = inventory.getPurchase(SKU_YANG50);
      					      List<Purchase> purchases = new LinkedList<Purchase>();
      					      if(detailsYang10!=null && verifyDeveloperPayload(detailsYang10)){
      					    	purchases.add(detailsYang10);
      					      }
      					      if(detailsYang30!=null && verifyDeveloperPayload(detailsYang30)){
      					    	purchases.add(detailsYang30);
      					      }
      					      if(detailsYang50!=null && verifyDeveloperPayload(detailsYang50)){
      					    	purchases.add(detailsYang50);
      					      }
      					      
      					      if(purchases.size()>0){
    					    	mHelper.consumeAsync(purchases, null);
      					      }

      					      
      					     
      					}
      				});
  			
        	   }
        	});
        
        
        
        
        
   //     AccountManager am = AccountManager.get(this);
/*        am.get
        AuthenticatorDescription[] authDescs = am.getAuthenticatorTypes();
        for(AuthenticatorDescription description : authDescs){
        	System.out.println(description.type);
        }*/

        
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

       
		String publisherId = "f8f3615ea3366a203f8bc8bb586e0ffa";
		HeyzapAds.start(publisherId, this);		

		IncentivizedAd.fetch();
		
		
	
    }
    
    

    
    
    class MyFacebookCallback implements FacebookDialog.Callback{
    	MyFacebookCallback(ShareCallback callback){
    		_callback = callback;
    	}
    	
    	ShareCallback _callback;

		@Override
		public void onComplete(PendingCall pendingCall, Bundle data) {
            Log.i("Activity", "Success!");
       
            boolean complete =  FacebookDialog.getNativeDialogDidComplete(data);
            if(complete){
            	_callback.success();
            }
            else{
            	_callback.onFailure("cancelled");
            }
			
		}

		@Override
		public void onError(PendingCall pendingCall, Exception error,
				Bundle data) {
            Log.e("Activity", String.format("Error: %s", error.toString()));

			_callback.onFailure("failed");
		}
    }
    
    MyFacebookCallback myFacebookClb;
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	if(!mHelper.handleActivityResult(requestCode, resultCode, data)){
    		
            super.onActivityResult(requestCode, resultCode, data);

            if(myFacebookClb!=null){
            	uiHelper.onActivityResult(requestCode, resultCode, data, myFacebookClb);
            }
    		
    	}
    	else {
    		Log.d(TAG, "onActivityResult handled by IABUtil.");
    	}

        
        
        
    }
    
     String TAG ;

    
    

    
    public Set<Feature> getEnabledFeatures(){
    	return features;
    }
    
    private Set<Feature> features = new HashSet<Feature>();
    
    private boolean verifyDeveloperPayload(Purchase purchase){
    	String payload =  purchase.getDeveloperPayload();
    	return 	new String(new char[]{'I', 'u', 'n', 'd','e','r','s','t','a','n','d','r','i','s','k'}).equals(payload);

    }
    
    private String loadLicenceKey(){
    	return LICENCE_KEY;
    }
    
/*    private AdView createBanner1(){
    	banner1 = new AdView(this);
    	banner1.setAdSize(AdSize.BANNER);
    	banner1.setAdUnitId(AD_UNIT_ID);
    	banner1.setId(101); // this is an arbitrary id, allows for relative positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        
        
        banner1.setLayoutParams(params);
        banner1.setBackgroundColor(Color.BLACK);
        return banner1;
    }
    
    private AdView createBanner2(){
    	banner2 = new AdView(this);
    	banner2.setAdSize(AdSize.BANNER);
    	banner2.setAdUnitId(AD_UNIT_ID);
    	banner2.setId(102); // this is an arbitrary id, allows for relative positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        
        
        banner2.setLayoutParams(params);
        banner2.setBackgroundColor(Color.BLACK);
        return banner2;
    }*/
    
/*    private AdView createAdView() {
    	 adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setId(100); // this is an arbitrary id, allows for relative positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        adView.setBackgroundColor(Color.BLACK);
        return adView;
      }*/

      private View createGameView(AndroidApplicationConfiguration cfg) {
         gameView = initializeForView(new TestGame(this), cfg);
/*        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, adView.getId());*/
         
         FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
         
         
        gameView.setLayoutParams(params);
        return gameView;
      }
      
      private AdRequest buildRequest(){
          return  new AdRequest.Builder().
         		     addTestDevice(AdRequest.DEVICE_ID_EMULATOR).
         		     addTestDevice(EXPLAY).
           		build();
       }
    
    AdRequest createRequest(){
    	AdRequest request = new AdRequest.Builder().addTestDevice("xxx").build();
    	return request;
    }
   
    @Override
    public void onResume() {
      super.onResume();
      if (adView != null) 
    	  adView.resume();
      if(banner1!=null){
    	  banner1.resume();
      }
      if(banner2!=null){
    	  banner2.resume();
      }
      
      if(uiHelper!=null)
    	  uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    	
      if (adView != null) 
    	  adView.pause();
      if(banner1!=null){
    	  banner1.pause();
      }
      if(banner2!=null){
    	  banner2.pause();
      }
 
      
      if(uiHelper!=null){
    	  uiHelper.onPause();
      }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    	
      if (adView != null) adView.destroy();
      if(banner1!=null){
    	  banner1.destroy();
      }
      if(banner2!=null){
    	  banner2.destroy();
      }

      
      if(uiHelper!=null){
    	  uiHelper.onDestroy();
      }
      
      if (mHelper != null) mHelper.dispose();
      mHelper = null;
    }


	
	@Override
	public void purchase(final Feature feature,final PurchaseCallback callback){
		//callback.onResult(true);
	    // Called when consumption is complete
		
		
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
			    final IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
			        public void onConsumeFinished(Purchase purchase, IabResult result) {
			            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

			            // if we were disposed of in the meantime, quit.
			            if (mHelper == null) return;

			            // We know this is the "gas" sku because it's the only one we consume,
			            // so we don't check which sku was consumed. If you have more than one
			            // sku, you probably should check...
			            if (result.isSuccess()) {
			                // successfully consumed, so we apply the effects of the item in our
			                // game world's logic, which in our case means filling the gas tank a bit
			                Log.d(TAG, "Consumption successful. Provisioning.");
			               callback.onResult(true);
			            }
			            else {
			            	 Log.e(TAG, "Error while consuming: " + result);
			            	 callback.onResult(false);
			            }

			            Log.d(TAG, "End consumption flow.");
			        }
			    };

			    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
			        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			            if (result.isFailure()) {
			                if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
			                    Log.d(TAG, "already purchased item.");
			                    callback.alreadyPurchased();
			              
			                    return;
			                }
			                if(result.getResponse()==IabHelper.BILLING_RESPONSE_RESULT_USER_CANCELED){
			                    Log.d(TAG, "User cancelled");
			                	callback.onResult(false);
			                	return;
			                }
			                
			                Log.e(TAG,"Error purchasing: " + result);
			                callback.onResult(false);
			                return;
			            }
			            if (!verifyDeveloperPayload(purchase)) {
			            	String error = "Error purchasing. Authenticity verification failed.";
			                Log.e(TAG,error);
			                callback.onFailure(error);
			                
			                return;
			            }

			            Log.d(TAG, "Purchase successful.");
			            
			            if(purchase.getSku().equals(SKU_YANG10) || purchase.getSku().equals(SKU_YANG30) || purchase.getSku().equals(SKU_YANG50)){
			            	mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			            	return;
			            }
			            
			    
			             if (purchase.getSku().equals(SKU_LEVELPACK)) {
			                // bought the premium upgrade!
			                Log.d(TAG, "Purchased levelpack");
			               features.add(Feature.LEVELPACK);
			               callback.onResult(true);
			               return;
			            }
			            else if (purchase.getSku().equals(SKU_WALKTHROUGH)) {
			                // bought the infinite gas subscription
			                Log.d(TAG, "Purchased walkthrough");
			                features.add(Feature.WALKTHROUGH);
			                callback.onResult(true);
			                return;
			            }
			             
			             callback.onFailure("product unknown");
			        }
			    };
				
				
				
				String payload = new String(new char[]{'I', 'u', 'n', 'd','e','r','s','t','a','n','d','r','i','s','k'});
				
				if(feature == Feature.LEVELPACK){
					mHelper.launchPurchaseFlow(MainActivity.this, SKU_LEVELPACK, RC_REQUEST, 
		                mPurchaseFinishedListener, payload);
				}
				else if(feature == Feature.WALKTHROUGH){

					mHelper.launchPurchaseFlow(MainActivity.this, SKU_WALKTHROUGH, RC_REQUEST, 
			                mPurchaseFinishedListener, payload);			
				}
				else if(feature == Feature.YANG_10){
					mHelper.launchPurchaseFlow(MainActivity.this, SKU_YANG10, RC_REQUEST, 
			                mPurchaseFinishedListener, payload);
				}
				else if(feature == Feature.YANG_30){

					mHelper.launchPurchaseFlow(MainActivity.this, SKU_YANG30, RC_REQUEST, 
			                mPurchaseFinishedListener, payload);			
				}
				else if(feature == Feature.YANG_50){
					mHelper.launchPurchaseFlow(MainActivity.this, SKU_YANG50, RC_REQUEST, 
			                mPurchaseFinishedListener, payload);
				}
				
			}
		});
		

	}




	@Override
	public void rate() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
		
		        MainActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appId)));
     
				
			}
		});
		
	}

	@Override
	public void checkInternetConnection(final OnlineStatusCallback callback) {
	
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ConnectivityManager cm =
				        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				    NetworkInfo netInfo = cm.getActiveNetworkInfo();
				    
				    callback.call(netInfo != null && netInfo.isConnectedOrConnecting());
				
			}
		});
		
		    
		    
	}





	@Override
	public void shareOnFacebook2(final String title,final String desc, final ShareCallback callback) {

		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				myFacebookClb = new MyFacebookCallback(callback);
				
				if(FacebookDialog.canPresentShareDialog(getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)){
					FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(MainActivity.this)
			        .setLink(GOOGLE_PLAY_YY)
			        .setName(title)
			        .setPicture(SHARE_PICTURE)
			        .setCaption(title)
			        .setDescription(desc)
			        				
			        .build();
					
				
					
					uiHelper.trackPendingDialogCall(shareDialog.present());
				}
				else{
					//publishFeedDialog( title,  desc, callback);
					callback.onFailure("Facebook app not installed");
				}
				

				
			}
		});

	}

	@Override
	public void likeOnFacebook() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				//likeView.performClick();
				
			}
		});
		
	}




	@Override
	public void tweet(final String tweetText) {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s"
                        , tweetText, GOOGLE_PLAY_YY) ;
				Uri uri = Uri.parse(tweetUrl);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));				
			}
		});
		
	}




	@Override
	public void showVideoAd(final VideoCompletedStatusCallback callback) {
 		this.runOnUiThread(new Runnable() {
			
	@Override
	public void run() {
		IncentivizedAd.setOnIncentiveResultListener(new OnIncentiveResultListener() {
			
			@Override
			public void onIncomplete(String arg0) {
				MainActivity.this.log(TAG, "Heyzap video NOT completed");
				callback.call(false);
				IncentivizedAd.fetch();
				
			}
			
			@Override
			public void onComplete(String arg0) {
				MainActivity.this.log(TAG, "Heyzap video completed");
				callback.call(true);
				IncentivizedAd.fetch();
				
			}
		});
		
		if (IncentivizedAd.isAvailable()) {
			IncentivizedAd.display(MainActivity.this);
		   
		}
		else{
			callback.noVideoAvailable();
		}
		// VideoAd.fetch();
		/*
		 if (interstitial.isLoaded()) {
	          interstitial.show();
	        }	
		 else{
			 AdRequest rq = buildRequest();
			 interstitial.loadAd(rq);
			 
		 }
	*/}
});
}




	@Override
	public void joinUs(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, 
			     Uri.parse(url));
		this.startActivity(intent);
		
		
	}




	@Override
	public void getInventory(final InventoryCallback callback){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				callback.onResult(features);
				
			}
		});
	
	}




	@Override
	public boolean clear() {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean dev() {
		// TODO Auto-generated method stub
		return false;
	}



}