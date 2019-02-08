package com.Tuong.Entity;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import com.Tuong.GameManager.EventListener;

public class AIController {
	public Entity entity;
	public ArrayList<PathGoal> pathgoal;
	public int currentPath = 0;

	public Entity attack;

	public ArrayList<Point> path;

	public Thread thread, thread1;

	public Entity follower;

	public HashMap<Integer, ArrayList<PathGoal>> routine;
	public int x=-1,y=-1;
	public AIController(Entity entity, ArrayList<PathGoal> pathgoal) {
		this.entity = entity;
		this.pathgoal = pathgoal;
		go();
	}

	public void goToLocation(int x, int y) {
		if(this.x == x/entity.display.tile_size && this.y == y/entity.display.tile_size) return;
		this.x = x/entity.display.tile_size; this.y = y/entity.display.tile_size;
		path = getPathMove(entity.getX(), entity.getY(), x, y);
	}

	public void go() {
		thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						entity.set = false;
						if (path == null || path.size() == 0 || !entity.moveable) {
							Thread.sleep(10);
							continue;
						}
						int luu_x = entity.x, luu_y = entity.y, xt = entity.x, yt = entity.y;
						if (entity.getY() < path.get(path.size() - 1).y) {
							yt += 1;
							if (!entity.set) {
								entity.set = true;
								entity.direction = Direction.UP;
								entity.stage = entity.direction.id;
							}
						}
						if (entity.getX() < path.get(path.size() - 1).x) {
							xt += 1;
							if (!entity.set) {
								entity.set = true;
								entity.direction = Direction.RIGHT;
								entity.stage = entity.direction.id;
							}
						}
						if (entity.getX() > path.get(path.size() - 1).x) {
							xt -= 1;
							if (!entity.set) {
								entity.set = true;
								entity.direction = Direction.LEFT;
								entity.stage = entity.direction.id;
							}
						}
						if (entity.getY() > path.get(path.size() - 1).y) {
							yt -= 1;
							if (!entity.set) {
								entity.set = true;
								entity.direction = Direction.DOWN;
								entity.stage = entity.direction.id;
							}
						}
						if (entity.set) {
							boolean b = true;
							for (EventListener listener : entity.display.manager.listener)
								if (!listener.EntityMoveEvent(entity, new Point(luu_x, luu_y), new Point(xt, yt))) {
									b = false;
									break;
								}
							if (b) {
								entity.x = xt;
								entity.y = yt;
							} else {
								entity.x = luu_x;
								entity.y = luu_y;
								entity.set = false;
							}
						}
						if (entity.getX() == path.get(path.size() - 1).x
								&& entity.getY() == path.get(path.size() - 1).y)
							path.remove(path.size() - 1);
						Thread.sleep(7);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread1.start();
	}

	public void checkWalkable(int luu_x, int luu_y, int xt, int yt) {
		if (entity.set) {
			boolean b = true;
			for (EventListener listener : entity.display.manager.listener)
				if (!listener.EntityMoveEvent(entity, new Point(luu_x, luu_y), new Point(xt, yt))) {
					b = false;
					break;
				}
			if (b) {
				entity.x = xt;
				entity.y = yt;
			} else {
				entity.x = luu_x;
				entity.y = luu_y;
				entity.set = false;
			}
		}
	}

	public void next() {
		if (currentPath < pathgoal.size() - 1)
			currentPath++;
		else
			currentPath = 0;
	}

	int[] col = { 0, -1, 0, 0, 1, 1, 1, -1, -1 };
	int[] row = { 0, 0, -1, 1, 0, 1, -1, 1, -1 };

	public boolean CheckWalkable(int x, int y) {
		boolean cc = !entity.display.manager.walkable[x][y];
		return cc;
	}

	public ArrayList<Point> getPathMove(int x, int y, int dx, int dy) {
		Point S = new Point(dx / entity.display.tile_size, dy / entity.display.tile_size),
				T = new Point(x / entity.display.tile_size, y / entity.display.tile_size);
		ArrayList<AIPoint> point = new ArrayList<AIPoint>();
		ArrayList<AIPoint> cache = new ArrayList<AIPoint>();
		ArrayList<Point> visited = new ArrayList<Point>();
		point.add(new AIPoint(T, null));
		for (int i = 0; i < 100; i++) {
			for (AIPoint p : point) {
				visited.add(p.point);
				for (int k = 0; k < col.length; k++) {
					Point p1 = new Point(p.point.x + col[k], p.point.y + row[k]);
					if (CheckWalkable(p1.x, p1.y) && !visited.contains(p1)) {
						cache.add(new AIPoint(p1, p));
					}
					if (p1.equals(S)) {
						p = new AIPoint(p1, p);
						visited.clear();
						while (p != null) {
							p.point.x = p.point.x * entity.display.tile_size + entity.display.tile_size / 2;
							p.point.y = p.point.y * entity.display.tile_size + entity.display.tile_size / 2;
							visited.add(p.point);
							p = p.prevpoint;
						}
						if (visited.size() > 1) {
							Point l = visited.get(visited.size() - 2);
							visited.set(visited.size() - 1, new Point((l.x + T.x) / 2, (l.y + T.y) / 2));
						}
						return visited;
					}
				}
			}
			point = new ArrayList<AIPoint>(cache);
			cache.clear();
		}
		return null;
	}
}