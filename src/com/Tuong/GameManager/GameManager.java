package com.Tuong.GameManager;

import java.awt.Point;
import java.util.ArrayList;

import com.Tuong.Display.DisplayCore;
import com.Tuong.Display.EditorFrame;
import com.Tuong.Entity.Entity;
import com.Tuong.Inventory.Item;


public class GameManager{
	public final int max_block_x = 1000,max_block_y = 1000;
	
	public GameplayManager gameplay;
	
	public Stage game_stage;
	public DisplayCore display;
	public InteractManager interact;
	
	public EditorFrame editor_pane;
	
	public Point Scr = new Point(0,0), mouse = new Point(0,0), tScr = new Point(0,0); 
	
	public int img_review;
	
	public boolean[][] walkable = new boolean[max_block_x][max_block_y];
	
	public String[][] map_id = new String[max_block_x][max_block_y]; 
	public String[][] top_id = new String[max_block_x][max_block_y];
	public Item[][] item_map = new Item[max_block_x][max_block_y];
	
	public ArrayList<Point> path_edit = new ArrayList<Point>();
	
	public ArrayList<Entity> entity = new ArrayList<Entity>();
	
	public ArrayList<EventListener> listener = new ArrayList<EventListener>();
	
	public GameManager(DisplayCore display, InteractManager interact){
		for(int i = 0; i < map_id[0].length; i++) for(int j = 0; j < map_id.length; j++) map_id[i][j] = "$";
		Scr.x = 0; Scr.y = 0;
		this.game_stage = Stage.MENU;
		this.display = display;
		this.interact = interact;
		setup();
	}

	public void setup(){
		interact.manager = this;
	}
	
}