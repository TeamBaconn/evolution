package com.Tuong.Entity;

import java.awt.Point;

public class AIPoint {
	public Point point;
	public AIPoint prevpoint;
	public boolean visted = false;
	public AIPoint(Point point, AIPoint prevpoint){
		this.point = point;
		this.prevpoint = prevpoint;
	}
}
