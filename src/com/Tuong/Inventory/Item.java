package com.Tuong.Inventory;

import java.awt.image.BufferedImage;

public class Item implements Cloneable{
	public String name;
	public Usable usable;
	public int scale;
	public boolean prohibited;
	public BufferedImage texture,maptexture;
	public int itemx,itemy;
	
	public Item(BufferedImage texture, String name, Usable usable, int scale, boolean prohibited){
		this.name = name;
		this.usable = usable;
		this.scale = scale;
		this.prohibited = prohibited;
		this.texture = texture;
	}
	
	public Item clone(){  
		try {
			return (Item) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}  
}
