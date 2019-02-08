package com.Tuong.Entity;

import java.util.ArrayList;

import com.Tuong.Display.Texture;

public class EntityTextureData {
	public ArrayList<Texture> image;
	public int hitbox_x,hitbox_y;
	public EntityTextureData(ArrayList<Texture> image, int hitbox_x, int hitbox_y){
		this.image = image;
		this.hitbox_x = hitbox_x;
		this.hitbox_y = hitbox_y;
	}
}
