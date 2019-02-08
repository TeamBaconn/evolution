package com.Tuong.EntityType;

import java.util.ArrayList;

import com.Tuong.Display.DisplayCore;
import com.Tuong.Entity.AIController;
import com.Tuong.Entity.Entity;
import com.Tuong.Entity.Job;
import com.Tuong.Entity.PathGoal;

public class AmericanSoldier extends Entity{
	public AmericanSoldier(String name, DisplayCore display, int x,int y) {
		super(display.entity_mau.get(0).image, x, y, display.entity_mau.get(0).hitbox_x, display.entity_mau.get(0).hitbox_y, name, display);
		chay();
		job = Job.SOLDIER;
		ArrayList<PathGoal> path = new ArrayList<PathGoal>();
		path.add(PathGoal.PASSIVEATTACK);
		path.add(PathGoal.DITUAN);
		AI = new AIController(this, path);
	}
}
