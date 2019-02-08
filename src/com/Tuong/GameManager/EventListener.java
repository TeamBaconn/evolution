package com.Tuong.GameManager;

import java.awt.Point;

import com.Tuong.Entity.Entity;

public interface EventListener {
	public abstract boolean EntityMoveEvent(Entity entity, Point from, Point to);
	public abstract boolean PlayerMoveEvent(Entity player, Point from, Point to);
	public abstract boolean EntityAttackEntity(Entity attacker, Entity target);
}
