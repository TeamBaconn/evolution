package com.Tuong.Entity;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.Tuong.Display.DisplayCore;
import com.Tuong.Display.Texture;
import com.Tuong.GameManager.EventListener;
import com.Tuong.Inventory.Inventory;
import com.Tuong.Inventory.Item;

public class Entity implements Cloneable, EventListener {
	public Direction direction;
	public int x, y, height, width;
	public ArrayList<Texture> image;
	public int speed = 1, health = 4, energy = 50, hunger = 75, wanted = 100, skill = 25, knowledge = 75, power = 50;

	public String name;

	public Entity owner;

	public Job job;

	public int stage = 1, img_index = 0;

	public int hitbox_x, hitbox_y;

	public Inventory inventory = new Inventory(this, 120, new ArrayList<Item>());

	public AIController AI;

	public DisplayCore display;

	public Thread thread;

	public boolean set = false;

	public Entity target;

	public String chat = "";

	public Item inhand;

	public Entity(ArrayList<Texture> image, int x, int y, int hitbox_x, int hitbox_y, String name,
			DisplayCore display) {
		this.owner = this;
		this.image = image;
		this.hitbox_x = hitbox_x;
		this.hitbox_y = hitbox_y;
		this.display = display;
		this.name = name;
		this.x = x;
		this.y = y;
		display.manager.entity.add(this);
		display.manager.listener.add(this);
	}

	public int other = 2;

	public boolean moveable = true;

	public double distance(Entity target) {
		return Math.sqrt((this.getX()-target.getX())*(this.getX()-target.getX())+(this.getY()-target.getY())*(this.getY()-target.getY()));
	}
	
	public double distance(double x, double y) {
		return Math.sqrt((this.getX()-x)*(this.getX()-x)+(this.getY()-y)*(this.getY()-y));
	}
	
	public void chay() {
		thread = new Thread(new Runnable() {
			long delay = 0;
			@Override
			public void run() {
				try {
					while (true) {
						// Kiem tra mau
						if (health < 0) {
							moveable = false;
							img_index = 10;
							continue;
						}
						// Kiem tra tan cong
						if (target != null && delay / 300 > speed
								&& Math.abs(getX() / display.tile_size - target.getX() / display.tile_size) < 1
								&& Math.abs(getY() / display.tile_size - target.getY() / display.tile_size) < 1) {
							boolean attack = true;
							for (EventListener listener : display.manager.listener)
								if (!listener.EntityAttackEntity(owner, target)) {
									attack = false;
									break;
								}
							if (attack) {
								target.health--;
								delay = 0;
								img_index = 9;
								moveable = false;
								Thread.sleep(300);
								moveable = true;
							}else {
								if (set) {
									if (img_index <= 3)
										img_index = 4;
									if (img_index < (image.size()) / 4 - 1 - other)
										img_index++;
									else
										img_index = 4;
									Thread.sleep(150);
								} else {
									if (img_index < 3)
										img_index++;
									else
										img_index = 0;
									Thread.sleep(300);
								}
							}
						} else {
							if (set) {
								if (img_index <= 3)
									img_index = 4;
								if (img_index < (image.size()) / 4 - 1 - other)
									img_index++;
								else
									img_index = 4;
								Thread.sleep(150);
							} else {
								if (img_index < 3)
									img_index++;
								else
									img_index = 0;
								Thread.sleep(300);
							}
						}
						if (delay / 300 < 50)
							delay += 300;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public boolean addItem(Item item, int lmx, int lmy, boolean bypass) {
		if (!bypass)
			if (inventory.storage - item.scale < 0 || Math.abs(getX() / display.tile_size - lmx) > 1
					|| Math.abs(getY() / display.tile_size - lmy) > 1)
				return false;
		inventory.items.add(item.clone());
		inventory.storage -= item.scale;
		return true;
	}

	public int getX() {
		return x + image.get(0).texture.getWidth() / 2;
	}

	public int getY() {
		return y + image.get(0).texture.getHeight();
	}

	public void removeItem(int index) {
		inventory.storage += inventory.items.get(index).scale;
		inventory.items.remove(index);
	}

	public Entity clone() {
		try {
			return (Entity) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean EntityMoveEvent(Entity entity, Point from, Point to) {
		if(target != null && target.equals(entity) && AI != null && from.distance(to) >= display.tile_size){
			AI.goToLocation(entity.getX(), entity.getY());
		}
		return true;
	}

	public boolean PlayerMoveEvent(Entity player, Point from, Point to) {
		if (!player.moveable)return false;
		BufferedImage i = player.image.get(player.img_index + player.stage * player.image.size() / 4).texture;
		int x_h = to.x + i.getWidth() / 2 - hitbox_x / 2;
		int y_h = to.y + i.getHeight();
		int xl = x_h / display.tile_size, yl = y_h / display.tile_size;
		if (display.manager.walkable[xl][yl])
			return false;
		xl = (x_h + hitbox_x) / display.tile_size;
		if (display.manager.walkable[xl][yl])
			return false;
		if(target != null && target.equals(player)){
			AI.goToLocation(player.getX(), player.getY());
		}
		return true;
	}

	public boolean EntityAttackEntity(Entity attacker, Entity target) {
		if(target.AI != null && target.AI.pathgoal.contains(PathGoal.PASSIVEATTACK)){
			target.target = attacker;
		}
		return true;
	}

}