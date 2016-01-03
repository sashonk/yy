package com.me.test.game2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import sun.awt.image.ByteArrayImageSource;
import sun.misc.IOUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.gdx.LibGdxAtlasLoader;
import com.brashmonkey.spriter.gdx.LibGdxDrawer;
import com.me.test.BaseScreen;
import com.me.test.TestGame;
import com.me.test.Util;
import com.me.test.Util.TriggerAction.Predicate;
import com.me.test.game2.item.Box;
import com.me.test.game2.item.Fall;
import com.me.test.game2.item.Wall;
import com.me.test.game2.item.Water;
import com.me.test.game2.item.Yang;
import com.me.test.game2.item.Yin;
import com.me.test.game2.item.factory.BoxFactory;
import com.me.test.game2.item.factory.FallFactory;
import com.me.test.game2.item.factory.GroundFactory;
import com.me.test.game2.item.factory.MetalFactory;
import com.me.test.game2.item.factory.MirrorFactory;
import com.me.test.game2.item.factory.WallFactory;
import com.me.test.game2.item.factory.WaterFactory;
import com.me.test.game2.item.factory.YangFactory;
import com.me.test.game2.item.factory.YinFactory;

public abstract class ItemContainer extends BaseScreen{
	
/*	protected List<Fall> falls;
	protected List<Yang> balls;
	protected List<Yin> spots;*/
	protected ItemTable table;
	protected Group gameLayer;
	protected Group backgroundLayer;
	protected Group uiLayer;
	static final short LEVEL_FILE_FORMAT_VERSION = 3;
	
	protected LevelMeta levelMeta;
	
	public LevelMeta getMeta(){
		return levelMeta;
	}
/*	

	
	protected boolean rotten;
	protected float scale;
	protected int difficulty;*/
	
	protected FileHandle lastReadFile; 
	
	public <T extends BaseItem> Collection<T> getAll(Class<T> type){
		Collection<T> result = new LinkedList<T>();
		for(Actor a : gameLayer.getChildren()){
			if(type.isAssignableFrom(a.getClass())){
				BaseItem item = (BaseItem) a;
				if(item.isBinded()){
					result.add((T) a);
				}
			}
		}
		
		return result;
	}
	
	Map<Class<? extends BaseItem>, ItemFactory<? extends BaseItem> > factories;
	
	public Map<Class<? extends BaseItem>, ItemFactory<? extends BaseItem> > getFactories(){
		return factories;
	}
	
	public ItemFactory<? extends BaseItem> findFactoryByCode(int code){
		for(ItemFactory<? extends BaseItem> factory : getFactories().values()){
			if(factory.getId()== code){
				return factory;
			}
		}
		return null;
	}
	
	Loader<Sprite> yinLoader;
	Drawer<Sprite> yinDrawer;
	final SCMLReader reader;
	public Drawer<Sprite> getDrawer(){
		return yinDrawer;
	}
	
	public 	Loader<Sprite> getLoader(){
		return yinLoader;
	}
	
	public SCMLReader getReader(){
		return reader;
	}
	
	public ItemContainer(TestGame g) {
		super(g);
		Stage s = getStage();
		
		FileHandle scmlFile = Gdx.files.internal("data/spriter/yin/yin.scml");

		reader = new SCMLReader(scmlFile.read());
		
		//this.loader = new LibGdxAtlasLoader(reader.getData(), Gdx.files.internal("data/spriter/ant/ant.atlas"));
		this.yinLoader = new LibGdxAtlasLoader(reader.getData(), g.getManager().getYinAtlas());		
		this.yinLoader.load("");	 
		this.yinDrawer = new LibGdxDrawer(yinLoader, (SpriteBatch) getStage().getBatch(), null);
		
		factories = new TreeMap<Class<? extends BaseItem>, ItemFactory<? extends BaseItem> >(new Comparator<Class<? extends BaseItem>>() {

			@Override
			public int compare(Class<? extends BaseItem> o1,
					Class<? extends BaseItem> o2) {
				
				return o1.getName().compareTo(o2.getName());
			}
		});
		ItemFactory<? extends BaseItem> [] factoryArrays = new ItemFactory[]{
				new YinFactory(),
				new YangFactory(),
				new WallFactory(),
				 new BoxFactory(),
				 new FallFactory(),
				 new WaterFactory(),
				 new MirrorFactory(),
				 new GroundFactory(),
				 new MetalFactory()
		};
		
		for(ItemFactory<? extends BaseItem> factory : factoryArrays){
			factories.put(factory.getItemClass(), factory);
		}


		
		
/*		
		
		rotten= false;
		scale = 1.0f;*/
		
		backgroundLayer = new Group();
		backgroundLayer.setName("backgroundLayer");
		s.addActor(backgroundLayer);
		backgroundLayer.setBounds(0, 0, s.getWidth()*5,s.getHeight()*5);
		
		 gameLayer = new Group();
		/// gameLayer.setBounds(x, y, stage.ge, height);
		gameLayer.setName("gamelayer");
		gameLayer.setBounds(0, 0, s.getWidth()*5,s.getHeight()*5);
		//gameLayer.set
		gameLayer.setTouchable(Touchable.enabled);
		s.addActor(gameLayer);
		
		 uiLayer = new Group();
		 uiLayer.setBounds(0, 0, s.getWidth(),s.getHeight());
		 uiLayer.setTouchable(Touchable.childrenOnly);
		uiLayer.setName("uilayer");
		s.addActor(uiLayer);
		
		
		table = new ItemTable(40, 40);

	}

	public ItemTable getTable(){
		return table;
	}



	void clear(){
		table.clear();
		gameLayer.clear();
	}
	
	public static class FadeSequence extends SequenceAction{
	
		
		
	}
	

	void read2(FileHandle file, final  Runnable callback) {
		clear();
		
		if(!file.exists() && file.type()!=FileType.Internal){
			try {
		
				file.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//meta = new LevelMeta();
		
		
		DataInputStream br = null;
		DataInputStream metaDis = null;
		InputStream rd = null;
		try{
			rd = file.read();
			br = new DataInputStream(rd);
			byte[] meta = new byte[100];
			br.read(meta, 0, 100);
			
			 metaDis = new DataInputStream(new ByteArrayInputStream(meta));
			short version = metaDis.readShort();
			if(version!=LEVEL_FILE_FORMAT_VERSION){
				Gdx.app.error(this.getClass().getName(), String.format("format: expected %d, but was %d", LEVEL_FILE_FORMAT_VERSION, version));
			}
			int totalCount = metaDis.readInt();
			boolean rotten = metaDis.readBoolean();
			 float scale = metaDis.readFloat();

			 //back compat
			 if(scale==0){
				 scale = 1;
			 }
			 
			 int difficulty = metaDis.readInt();
			 float camx = metaDis.readFloat();
			 float camy = metaDis.readFloat();
			 
			 int goal2 = metaDis.readInt();
			 int goal3 = metaDis.readInt();
			 
			 
			 if(version<LEVEL_FILE_FORMAT_VERSION){
				 goal2 = 10;
				 goal3 = 5;
			 }
			 
			 List<Byte> tips = new LinkedList<Byte>();
			 if(version==LEVEL_FILE_FORMAT_VERSION){
				 byte tip = 0;
				 while(true){
					 tip = metaDis.readByte();
					 if(tip>0){
						 tips.add(tip);
					 }
					 else{
						 break;
					 }
				 }

			 }
			 
			 
			 
			 levelMeta = new LevelMeta(version, goal2, goal3, totalCount, rotten, scale, difficulty, tips);
			 
			 gameLayer.setPosition(camx, camy);
			 backgroundLayer.setPosition(camx, camy);
			 
				gameLayer.addAction(Actions.scaleTo(scale,scale));
				backgroundLayer.addAction(Actions.scaleTo(scale,scale));
			
	
			for(int i = 0 ; i<totalCount; i++){

				int id = br.readInt();
				byte code = br.readByte();
				int x = br.readInt();
				int y = br.readInt();
				
				BaseItem newItem = findFactoryByCode(Integer.valueOf(code)).create(x, y, this);
				gameLayer.addActor(newItem);

				
				
				
			}
			
			for(Yang b : getAll(Yang.class)){
				b.toFront();
			}
			
			for(BaseItem box : getAll(Box.class)){
				box.toFront();
			}
			
			if(callback!=null){
			stage.addAction(Util.trigger(Actions.run(new Runnable() {
				
				@Override
				public void run() {
					callback.run();
					
				}
			}), new Predicate() {
				
				@Override
				public boolean accept() {
					for(Actor a : gameLayer.getChildren()){
						if(a instanceof BaseItem){
							for(Action act : a.getActions()){
								if(act instanceof FadeSequence){
									return false;
								}
							}
						}
					}
					
					return true;
				}
			}));
		
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			
			if(rd!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(metaDis!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			

		}
		

		
		lastReadFile = file;
	}

	void read2(FileHandle file) {read2(file, null);}
	

	
	void write2(FileHandle file, LevelParams params){
		
		DataOutputStream dos = null;
		try{
			dos =  new DataOutputStream(file.write(false)) ;

			ByteArrayOutputStream metaBaos = new ByteArrayOutputStream();
			DataOutputStream metaOs = new DataOutputStream(metaBaos);
			metaOs.writeShort(LEVEL_FILE_FORMAT_VERSION);

			int totalcount = 0;
			for(Actor a : gameLayer.getChildren()){
				if(a instanceof BaseItem){
					totalcount++;					
				}
			}
			metaOs.writeInt(totalcount);
			metaOs.writeBoolean(params.rotten);		
			metaOs.writeFloat(params.scale);
			metaOs.writeInt(params.difficulty);
			metaOs.writeFloat(gameLayer.getX());
			metaOs.writeFloat(gameLayer.getY());
			metaOs.writeInt(params.goal2threshold);
			metaOs.writeInt(params.goal3threshold);
			for(int i = 0 ; i<params.tips.length(); i++){
				char ch =  params.tips.charAt(i);
				char lc = Character.toLowerCase(ch);
				
				byte tip = 0;
				switch (lc){
				case 'u':
					tip = 1;
					break;
				
				case 'r':
					tip = 2;
					break;
				case 'd':
					tip=3;
					break;
				case 'l':
					tip=4;
					break;
					default:
						throw new IllegalArgumentException("unexpected symbol: " + lc);
				}
				metaOs.writeByte(tip);
			}
			metaOs.writeByte(0); // tips terminator
			
			metaOs.close();
			byte[] metaraw = metaBaos.toByteArray();
			
			byte[] metaData= Arrays.copyOf(metaraw, 100);			
			dos.write(metaData);
			/////////////////
			int id = 1;
			for(Actor a : gameLayer.getChildren()){
				if(a instanceof BaseItem){
					BaseItem item = (BaseItem) a;
					
					dos.writeInt(id);					
					dos.writeByte(factories.get(item.getClass()).getId()); //code
					dos.writeInt(item.getLocationX());
					dos.writeInt(item.getLocationY());
					
					
				}
			}

			
		}
		catch(Throwable ex){
			if(dos!=null){
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	protected static class LevelParams{
		public boolean rotten;
		public float scale;
		public int difficulty;
		public int goal2threshold;
		public int goal3threshold;
		public String tips;
	}
	

	public static class LevelMeta{
		
		public static class Tips{

			public Tips(List<Byte> data){
				_data = Collections.unmodifiableList(data);
				
				StringBuilder sb = new StringBuilder();
				for(Byte b : _data){
					char c = '0';
					switch (b){
					case 1:
						c = 'U';
					break;
					case 2:
						c = 'R';
						break;
					case 3:
						c= 'D';
						break;
					case 4:
						c= 'L';
						break;
						default:
							throw new IllegalArgumentException("unexpected value: " + b);
					}
						
					sb.append(c);
				}
				_asString = sb.toString();
			}
			
			public String asString(){
				return _asString;
			}
			
			private final String _asString;
			
			public List<Byte> getData(){
				return _data;
			}
			
			private final List<Byte> _data;
		}
		
		public LevelMeta(short version, int goal2, int goal3, int totalCnt, boolean rotten, float scale, int difficulty, List<Byte> tips){
			_version = version;
			_goal2threshold = goal2;
			_goal3threshold = goal3;
			_totalCount = totalCnt;
			_rotten = rotten;
			_scale = scale;
			_difficulty = difficulty;
			_tips = new Tips(tips);
		}
		
		public Tips getTips(){
			return _tips;
		}
		
		public short get_version() {
			return _version;
		}
		public int get_goal2threshold() {
			return _goal2threshold;
		}
		public int get_goal3threshold() {
			return _goal3threshold;
		}
		public int get_totalCount() {
			return _totalCount;
		}
		public boolean is_rotten() {
			return _rotten;
		}
		public float get_scale() {
			return _scale;
		}
		public int get_difficulty() {
			return _difficulty;
		}

		private short _version;
		private int _goal2threshold;
		private int _goal3threshold;
		private int _totalCount;
		private boolean _rotten;
		private float _scale;
		private int _difficulty;
		private final Tips _tips ;
	}

}
