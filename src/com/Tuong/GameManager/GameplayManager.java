package com.Tuong.GameManager;

import java.awt.Point;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

import com.Tuong.Display.Texture;
import com.Tuong.Entity.Entity;
import com.Tuong.Entity.Job;
import com.Tuong.Entity.PathGoal;
import com.Tuong.EntityType.AmericanSoldier;
import com.Tuong.EntityType.Cow;
import com.Tuong.EntityType.EntityType;
import com.Tuong.EntityType.NguoiDan;
import com.Tuong.EntityType.Player;

public class GameplayManager {
	public GameManager manager;
	public Player playermanager;

	public String map;

	public boolean open_inv;
	public Entity inv_owner;
	public Thread thread;

	public float tick = 360;
	public float time_speed = 25;
	public void quick(int l, int r) {
		if (r <= 0)
			return;
		int i = l, j = r, m = (l + r) / 2;
		m = manager.entity.get(m).y;
		while (i <= j) {
			while (manager.entity.get(i).y < m)
				++i;
			while (manager.entity.get(j).y > m)
				--j;
			if (i <= j) {
				Entity temp = manager.entity.get(i);
				manager.entity.set(i, manager.entity.get(j));
				manager.entity.set(j, temp);
				++i;
				--j;
			}
		}
		if (i < r)
			quick(i, r);
		if (j > l)
			quick(l, j);
	}

	Random random = new Random();

	public GameplayManager(GameManager manager, String map) {
		this.map = map;
		this.manager = manager;
		loadTexture(map);
		manager.item_map[2][2] = manager.display.item_list.get(1);
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						quick(0, manager.entity.size() - 1);
						if (manager.game_stage == Stage.PLAYING) {
							tick = tick + ((float) time_speed / 1000);
							if (TimeFrame.getType(tick) == TimeFrame.MORNING) {
								int lamviec = 0;
								for (Entity entity : manager.entity) {
									if (entity.AI == null)
										continue;
									if (entity.AI.pathgoal.contains(PathGoal.DITUAN) && entity.job.equals(Job.SOLDIER)
											&& (entity.AI.path == null || entity.AI.path.size() <= 0)) {
										int dituan = random.nextInt(manager.display.location.get("DT").size() - 1);
										entity.AI.goToLocation(
												manager.display.location.get("DT").get(dituan).x
														* manager.display.tile_size,
												manager.display.location.get("DT").get(dituan).y
														* manager.display.tile_size);
									}
									if (entity.AI.pathgoal.contains(PathGoal.LAMVIEC)
											&& entity.job.equals(Job.FARMER)) {
										entity.AI.goToLocation(
												manager.display.location.get("F").get(lamviec).x
														* manager.display.tile_size,
												manager.display.location.get("F").get(lamviec).y
														* manager.display.tile_size);
										lamviec++;
									}
								}
							}
							if (tick > 1439) {
								tick = 0;
							}
						} else
							tick = 0;
						Thread.sleep(25);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	boolean[][] dd = new boolean[1000][1000];
	int[] col = { -1, 0, 1, 1, 1, 0, -1, -1 };
	int[] row = { -1, -1, -1, 0, 1, 1, 1, 0 };

	public void Loang(int x, int y, int k) {
		dd[x][y] = true;
		manager.top_id[x][y] += "b" + k;
		for (int i = 0; i <= 7; ++i) {
			int l = x + col[i], r = y + row[i];
			if (l > 0 && l < 1000 && r > 0 && r < 1000 && dd[l][r] == false && manager.top_id[l][r] != null)
				Loang(l, r, k);
		}
	}

	public void DuyetTop() {
		int dem = 0;

		for (int i = 1; i < 1000; ++i)
			for (int j = 1; j < 1000; ++j)
				if (dd[i][j] == false && manager.top_id[i][j] != null) {
					++dem;
					Loang(i, j, dem);
				}
	}

	public void loadTexture(String name) {
		try {
			File file = new File("Data/Map");
			File f = new File(file, name + ".json");
			try (Stream<String> lines = Files.lines(Paths.get(f.getPath()), StandardCharsets.UTF_8)) {
				int n = 0;
				for (String line : ((Iterable<String>) lines::iterator)) {
					int m = -1;
					for (String s : line.split(" ")) {
						m++;
						for (String s1 : s.split("-")) {
							if (s1.contains("l")) {
								String sub = s1.substring(1);
								if(sub.contains("LIGHT")) {
									manager.display.lightlist.put(new Point(n,m),Integer.valueOf(sub.split("\\*")[1]));
									continue;
								}
								if (manager.display.location.containsKey(sub))
									manager.display.location.get(sub).add(new Point(n, m));
								else {
									ArrayList<Point> list = new ArrayList<Point>();
									list.add(new Point(n, m));
									manager.display.location.put(sub, list);
								}
								continue;
							}
							if (s1.contains("e")) {
								switch (EntityType.getType(s1.substring(1))) {
								case COW:
									new Cow("ABC", manager.display, n * manager.display.tile_size,
											m * manager.display.tile_size);
									break;
								case AMERICANSOLDIER:
									new AmericanSoldier("ABC", manager.display, n * manager.display.tile_size,
											m * manager.display.tile_size);
									break;
								case NGUOIDAN:
									new NguoiDan("ABC", manager.display, n * manager.display.tile_size,
											m * manager.display.tile_size);
									break;
								case PLAYER:
									playermanager = new Player(manager, n * manager.display.tile_size,
											m * manager.display.tile_size, "ABC");
									break;
								default:
									break;
								}
								continue;
							}
							if (s1.contains("a")) {
								if (manager.top_id[n][m] == null)
									manager.top_id[n][m] = "";
								manager.top_id[n][m] += "-" + s1.replace("a", "");
							} else {
								if (s1.contains("$")) {
									manager.walkable[n][m] = true;
									continue;
								}
								Texture texture = manager.display.environment
										.get(Integer.valueOf(s1.length() > 0 ? s1.replace("a", "") : "0"));
								manager.walkable[n][m] = texture.c;
								manager.map_id[n][m] += "-" + s1.replace("a", "");
							}
						}

					}
					++n;
				}
			}
			DuyetTop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openInventory(Entity entity) {
		open_inv = true;
		inv_owner = entity;
	}
}