package com.Tuong.Inventory;

import com.Tuong.Entity.Entity;

public class IDCard extends Item{
	public Entity entity;
	public IDCard(Item item, Entity entity) {
		super(item.texture,item.name,item.usable,item.scale,item.prohibited);
		this.entity = entity;
	}
	
}
