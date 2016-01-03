package ru.asocial.yy.html;

import ru.asocial.yy.core.YinYang;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class YinYangHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new YinYang();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
