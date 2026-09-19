package com.me.test.game2;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class ItemTable {
	
	int _width;
	int _height;


	public ItemTable(int width, int height){
		_width=width;
		_height=height;
		
		items = new BaseItem[_width][];
		for(int i = 0;i < _width; i++){
			items[i] = new BaseItem[_height];
		}
		
		spots =  new BaseItem[_width][];
		for(int i = 0;i < _width; i++){
			spots[i] = new BaseItem[_height];
		}
		
	}
	
	public int getWidth(){
		return _width;
	}

	public int getHeight(){
		return _height;
	}

	public void appendOccupiedCellsJson(StringBuilder sb){
		sb.append('[');
		boolean first = true;
		for(int x = 0; x < _width; x++){
			for(int y = 0; y < _height; y++){
				BaseItem item = items[x][y];
				BaseItem spot = spots[x][y];
				if(item == null && spot == null){
					continue;
				}
				if(!first){
					sb.append(',');
				}
				first = false;
				sb.append("{\"x\":").append(x).append(",\"y\":").append(y);
				sb.append(",\"item\":");
				appendItemType(sb, item);
				sb.append(",\"spot\":");
				appendItemType(sb, spot);
				sb.append('}');
			}
		}
		sb.append(']');
	}

	private static void appendItemType(StringBuilder sb, BaseItem item){
		if(item == null){
			sb.append("null");
		}
		else{
			sb.append('"').append(item.getClass().getSimpleName()).append('"');
		}
	}

	public boolean hits(int x, int y){
		return x >= 0 && x<items.length && y>=0 && y<items[0].length;
	}
	
	public BaseItem get(int x, int y){
		return items[x][y];
	}
	
	public BaseItem getSpot(int x, int y){
		return spots[x][y];
	}
	
	public int getTableX(float x){
		return (int) (x / BaseItem.ITEM_WIDTH);
	}
	
	public int getTableY(float y){
		return (int) (y / BaseItem.ITEM_HEIGHT);
	}
	
	public void putSpot(BaseItem spot , int x, int y){
		spots[x][y] = spot;
		if(spot!=null){
			spot.setPosition(x*BaseItem.ITEM_WIDTH, y*BaseItem.ITEM_HEIGHT);
			spot.setLocationX(x);
			spot.setLocationY(y);
		}
	}
	

	
	public void put(BaseItem item, int x, int y){
		items[x][y] = item;
		if(item!=null){
			float ix = item.getX();
			float iy= item.getY();
			if(ix!=0 && iy!=0){
				float d = 0.2f * Math.max(Math.abs(y - item.getLocationY()), Math.abs(x - item.getLocationX()));
				item.addAction(Actions.moveTo(x*BaseItem.ITEM_WIDTH, y*BaseItem.ITEM_HEIGHT, d));
			}
			else{
				item.setPosition(x*BaseItem.ITEM_WIDTH, y*BaseItem.ITEM_HEIGHT);
			}
			
			//item.setPosition(x*BaseItem.ITEM_WIDTH, y*BaseItem.ITEM_HEIGHT);
			item.setLocationX(x);
			item.setLocationY(y);
		}
	}
	
	public void clear(){
		for(BaseItem[] ar : items){
			for(int i = 0; i<ar.length; i++){
				ar[i] = null;
			}
		}
		for(BaseItem[] ar : spots){
			for(int i = 0; i<ar.length; i++){
				ar[i] = null;
			}
		}
	}

	BaseItem[][] items;
	BaseItem[][] spots;
}
