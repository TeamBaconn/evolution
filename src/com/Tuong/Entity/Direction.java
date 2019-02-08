package com.Tuong.Entity;

public enum Direction {
	UP(0,0,5),DOWN(3,5,5),RIGHT(2,10,5),LEFT(1,15,5),ATTACK(4,20,1);
	public int id,order,length;
	private Direction(int id, int order, int length){
		this.id = id;
		this.order = order;
		this.length = length;
	}
}
