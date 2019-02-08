package com.Tuong.EntityType;

import java.awt.Point;

import com.Tuong.Core.Core;
import com.Tuong.Entity.Direction;
import com.Tuong.Entity.Entity;
import com.Tuong.GameManager.EventListener;
import com.Tuong.GameManager.GameManager;

public class Player extends Entity {
	public Player(GameManager manager, int x, int y, String name) {
		super(manager.display.entity_mau.get(0).image, x, y, manager.display.entity_mau.get(0).hitbox_x,
				manager.display.entity_mau.get(0).hitbox_y, name, manager.display);
		this.manager = manager;
		chay();
		controller();
	}

	public GameManager manager;
	public Thread thread, thread1;

	public void controller() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						int luu_x = x, luu_y = y, xt = x, yt = y;
						set = false;
						if(!moveable)  {
							Thread.sleep(50);
							continue;
						}
						if (manager.interact.luu[83]) {
							yt += speed;
							if (!set) {
								set = true;
								direction = Direction.UP;
								stage = direction.id;
							}
						}
						if (manager.interact.luu[68]) {
							xt += speed;
							if (!set) {
								set = true;
								direction = Direction.RIGHT;
								stage = direction.id;
							}
						}
						if (manager.interact.luu[65]) {
							xt -= speed;
							if (!set) {
								set = true;
								direction = Direction.LEFT;
								stage = direction.id;
							}
						}
						if (manager.interact.luu[87]) {
							yt -= speed;
							if (!set) {
								set = true;
								direction = Direction.DOWN;
								stage = direction.id;
							}
						}
						if (set) {
							boolean b = true;
							for (EventListener listener : manager.listener){
								if (!listener.PlayerMoveEvent(owner, new Point(luu_x, luu_y), new Point(xt, yt))) {
									b = false;
									break;
								}
							}
							if (b) {
								x = xt;
								y = yt;				
							}else {
								x = luu_x;
								y = luu_y;
								set = false;
							}
						}
						int sx = manager.Scr.x, sy = manager.Scr.y;
						int xx = x - sx - Core.WIDTH/2, yy = y - sy - Core.HEIGHT/2;
						sx += xx;
						sy += yy;
						if (sx < manager.display.tile_size) sx = manager.display.tile_size; 
						if (sx > Core.WIDTH/2) sx = Core.WIDTH/2;
						if (sy < manager.display.tile_size) sy = manager.display.tile_size; 
						if (sy > Core.HEIGHT/2) sy = Core.HEIGHT/2;
						manager.Scr.x = sx;
						manager.Scr.y = sy;
						Thread.sleep(5);
					} catch (Exception e) {
					}
				}
			}
		});
		thread.start();
	}
}