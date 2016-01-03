package com.me.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Values {

	public static String guess_click_count = "interstitial_show_count";
	
	public static String exit_click_count = "rate_proposal_count";
	
	public static String played = "played";
	
	public static String rated = "rated";
	
	public static String playsCount = "playsCount";
	
	
	public static String tweeted = "tweeted";
	
	public static String shared = "shared";
	
	public static String yangs = "yangs";
	
	public static String permanentWalkthrough(int lev){
		return String.format("perm_wlk_%d", lev);
	}
	
	public static String levelPack = "level_pack";
	
	public static String videoWatchedTimesValue(Date date){
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String fmt = df.format(date);
		return "token_"+fmt;
	}
	
	public static String dateToken="date_token";
	
	public static String timesWatched = "video_times_watched";
	
	public static int levelPackYins = 10;
	

}
