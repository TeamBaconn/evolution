package com.Tuong.Inventory;

import java.util.ArrayList;

import com.Tuong.Entity.Entity;

public class Inventory {
	
	public Entity entity;
	public int storage;
	public ArrayList<Item> items;
	
	public Inventory(Entity entity, int storage, ArrayList<Item> item){
		this.entity = entity; 
		this.storage = storage;
		this.items = item;
	}
}
