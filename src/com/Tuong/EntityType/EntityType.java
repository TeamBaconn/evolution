package com.Tuong.EntityType;

public enum EntityType {
	NONE(-1),COW(0),AMERICANSOLDIER(0),PLAYER(0),NGUOIDAN(0);
	
	public int image;
	private EntityType(int image){
		this.image = image;
	}
	public static EntityType getType(String name){
		for(EntityType type : values()) if(type.toString().equals(name)) return type;
		return null;
	}
}
