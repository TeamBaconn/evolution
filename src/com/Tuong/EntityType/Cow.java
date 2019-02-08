package com.Tuong.EntityType;

import java.util.ArrayList;

import com.Tuong.Display.DisplayCore;
import com.Tuong.Entity.AIController;
import com.Tuong.Entity.Entity;
import com.Tuong.Entity.PathGoal;

public class Cow extends Entity{
	public Cow(String name, DisplayCore display, int x,int y) {
		super(display.entity_mau.get(0).image, x, y, display.entity_mau.get(0).hitbox_x, display.entity_mau.get(0).hitbox_y, name, display);
		chay();
		ArrayList<PathGoal> path = new ArrayList<PathGoal>();
		path.add(PathGoal.WANDER);
		path.add(PathGoal.PASSIVEATTACK);
		AI = new AIController(this, path);
	}
	
	public boolean PlayerMoveEvent(){
		return moveable;
	}
}
