package com.Tuong.Display;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import com.Tuong.Core.Core;
import com.Tuong.Entity.Entity;
import com.Tuong.Entity.EntityTextureData;
import com.Tuong.Entity.Path;
import com.Tuong.EntityType.EntityType;
import com.Tuong.GameManager.GameManager;
import com.Tuong.GameManager.Stage;
import com.Tuong.GameManager.TimeFrame;
import com.Tuong.Inventory.IDCard;
import com.Tuong.Inventory.Item;
import com.Tuong.Inventory.Usable;

public class DisplayCore extends JComponent {
	private static final long serialVersionUID = 1L;

	public ArrayList<Texture> texture_list = new ArrayList<Texture>();
	public ArrayList<Texture> character = new ArrayList<Texture>();
	public ArrayList<Texture> environment = new ArrayList<Texture>();
	public ArrayList<BufferedImage> shadow = new ArrayList<BufferedImage>();

	public ArrayList<Path> paths = new ArrayList<Path>();

	public ArrayList<EntityTextureData> entity_mau = new ArrayList<EntityTextureData>();

	public ArrayList<Item> item_list = new ArrayList<Item>();

	public ArrayList<Font> fonts = new ArrayList<Font>();

	public HashMap<String, ArrayList<Point>> location = new HashMap<String, ArrayList<Point>>();

	public final int tile_size = 96;

	public GameManager manager;
	public Thread thread;

	public GUI menu;

	public long delay = System.currentTimeMillis();
	public int fps = 0;

	public boolean[] collide = new boolean[10000];

	public Texture cursor;

	public DisplayCore() {
		// Load paths
		// loadPaths();
		// Load texture
		loadID();
		loadTexture();
		loadFonts();
		// Load gui
		loadGUI();
		// Load entity
		loadEntity();
		// Load items
		loadItems();

		resizeImages();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					repaint();
				}
			}
		});
		thread.start();
	}

	public void resizeImages() {
		Texture inv = getTexture("inv.png");
		inv.texture = resize(inv.texture, (int) (inv.texture.getWidth() * 12.2),
				(int) (inv.texture.getHeight() * 12.2));
		Texture target = getTexture("target.png");
		target.texture = resize(target.texture, (int) (target.texture.getWidth() * 3),
				(int) (target.texture.getHeight() * 3));
		Item i1 = getItem("IDCard");
		i1.texture = resize(i1.texture, i1.texture.getWidth() * 4, i1.texture.getHeight() * 4);
		for (Item item : item_list)
			item.maptexture = resize(item.texture, tile_size, tile_size);
	}

	public void loadPaths() {
		try {
			File files = new File("Data/Paths");
			for (File f : files.listFiles()) {
				ArrayList<Point> points = new ArrayList<Point>();
				try (Stream<String> lines = Files.lines(Paths.get(f.getPath()), StandardCharsets.UTF_8)) {
					for (String line : ((Iterable<String>) lines::iterator))
						points.add(new Point(Integer.valueOf(line.split(" ")[0]) * tile_size + tile_size / 2,
								tile_size / 2 + tile_size * Integer.valueOf(line.split(" ")[1])));
					paths.add(new Path(points, f.getName().replace(".json", "")));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + paths.size() + " paths");
	}

	public void loadID() {
		try {
			File file = new File("Data/Settings");
			File f = new File(file, "collide_id.tuong");
			try (Stream<String> lines = Files.lines(Paths.get(f.getPath()), StandardCharsets.UTF_8)) {
				for (String line : ((Iterable<String>) lines::iterator))
					collide[Integer.valueOf(line)] = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadGUI() {
		// Menu
		menu = new GUI();
		menu.createButton(getTexture("button.png").texture, getTexture("button_hover.png").texture,
				Core.WIDTH / 2 - getTexture("button.png").texture.getWidth() / 2,
				Core.HEIGHT / 2 - getTexture("button.png").texture.getHeight() / 2);
		menu.createButton(getTexture("button.png").texture, getTexture("button_hover.png").texture,
				Core.WIDTH / 2 - getTexture("button.png").texture.getWidth() / 2,
				Core.HEIGHT / 2 - getTexture("button.png").texture.getHeight() / 2 + 100);
		menu.createButton(getTexture("button.png").texture, getTexture("button_hover.png").texture,
				Core.WIDTH / 2 - getTexture("button.png").texture.getWidth() / 2,
				Core.HEIGHT / 2 - getTexture("button.png").texture.getHeight() / 2 + 200);
		menu.createButton(getTexture("button.png").texture, getTexture("button_hover.png").texture,
				Core.WIDTH / 2 - getTexture("button.png").texture.getWidth() / 2,
				Core.HEIGHT / 2 - getTexture("button.png").texture.getHeight() / 2 + 300);
	}

	public void loadTexture() {
		try {
			File file = new File("Data/Textures");
			for (File f : file.listFiles()) {
				BufferedImage image = ImageIO.read(f);
				texture_list.add(new Texture(f.getName(), image, false));
			}
			System.out.println("Loaded " + texture_list.size() + " textures");
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadEnvironment();
		loadShadows();
		cursor = getTexture("cursor_normal.png");
	}

	public void loadItems() {
		try {
			File file = new File("Data/Items");
			for (File f : file.listFiles()) {
				BufferedImage image = ImageIO.read(f);
				String[] s = f.getName().replace(".png", "").split("-");
				item_list
						.add(new Item(image, s[0], Usable.valueOf(s[1]), Integer.valueOf(s[2]), Boolean.valueOf(s[3])));
			}
			System.out.println("Loaded " + item_list.size() + " items");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadEntity() {
		try {
			File file = new File("Data/Entities");
			for (File f : file.listFiles()) {
				System.out.println(f.getName());
				BufferedImage image = ImageIO.read(f);
				ArrayList<Texture> list = new ArrayList<Texture>();
				int k = 0;
				String[] s = f.getName().replace(".png", "").split("_");
				int t = image.getHeight() / Integer.valueOf(s[1]);
				int l = image.getWidth() / Integer.valueOf(s[2]);
				for (int j = 0; j < Integer.valueOf(s[1]); j++)
					for (int i = 0; i < Integer.valueOf(s[2]); i++) {
						list.add(new Texture(f.getName().replace(".png", "") + k,
								resize(image.getSubimage(i * l, j * t, l, t), tile_size, tile_size), true));
					}
				entity_mau.add(new EntityTextureData(list, 80, 100));
			}
			System.out.println("Loaded " + entity_mau.size() + " entities");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int[] getFirstImagePoint(BufferedImage image) {
		for (int i = 0; i < image.getHeight(); i++)
			for (int j = 0; j < image.getWidth(); j++)
				if (image.getRGB(i, j) == new Color(255, 255, 255).getRGB()) {
					int[] k = { i, j };
					return k;
				}
		return null;
	}

	public void loadFonts() {
		try {
			File file = new File("Data/Fonts");
			for (File f : file.listFiles()) {
				fonts.add(Font.createFont(Font.TRUETYPE_FONT, f));
			}
			System.out.println("Loaded " + fonts.size() + " fonts");
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

	public void loadEnvironment() {
		int tile_size = 32;
		Texture texture = getTexture("environment.png");
		for (int j = 0; j < texture.texture.getHeight() / tile_size; j++)
			for (int i = 0; i < texture.texture.getWidth() / tile_size; i++) {
				environment.add(new Texture("1",
						resize(texture.texture.getSubimage(i * tile_size, j * tile_size, tile_size, tile_size),
								this.tile_size, this.tile_size),
						collide[environment.size()]));
			}
		System.out.println("Loaded " + environment.size() + " environments");
	}
	
	public void loadShadows() {
		int tile_size = 22;
		Texture texture = getTexture("shadow.png");
		for (int j = 0; j < texture.texture.getHeight() / tile_size; j++)
			for (int i = 0; i < texture.texture.getWidth() / tile_size; i++) 
				shadow.add(texture.texture.getSubimage(i * tile_size, j * tile_size, tile_size, tile_size));
		System.out.println("Loaded " + shadow.size() + " shadows");
	}

	public Texture getTexture(String name) {
		for (Texture texture : texture_list)
			if (texture.name.equals(name))
				return texture;
		return null;
	}

	public Item getItem(String name) {
		for (Item texture : item_list)
			if (texture.name.equals(name))
				return texture;
		return null;
	}

	public int fps_display = 0;

	public int GetTopNum(int x, int y) {
		if (manager.top_id[x][y] != null && manager.top_id[x][y].contains("b")) {
			String S = manager.top_id[x][y].split("b")[1];
			return Integer.valueOf(S);
		}
		return 0;
	}

	public int map(float tick, long in_min, long in_max, long out_min, long out_max) {
		return (int) ((tick - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
	}

	public String GetTime(float t) {
		int hour, min;
		hour = (int) t / 60;
		min = (int) t - (hour * 60);
		return (hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min);
	}

	public static void drawCircle(Graphics g, int x, int y, int radius) {
		int diameter = radius * 2;
		g.fillOval(x - radius, y - radius, diameter, diameter);
	}
	public HashMap<Point, Integer> lightlist = new HashMap<Point, Integer>();
	BufferedImage lightframe = new BufferedImage(Core.WIDTH, Core.HEIGHT, BufferedImage.TYPE_INT_ARGB);
	Graphics2D graph = lightframe.createGraphics();
	public void paint(Graphics g) {
		int sx = manager.Scr.x, sy = manager.Scr.y;
		Font font = fonts.get(0);
		g.setFont(font.deriveFont(18.0f));
		fps++;
		if (System.currentTimeMillis() - delay > 1000) {
			fps_display = fps;
			fps = 0;
			delay = System.currentTimeMillis();
		}
		Point t = new Point(Math.abs(sx) % tile_size, Math.abs(sy) % tile_size);
		if (manager.game_stage == Stage.EDITING) {
			for (int i = 1; i <= 100; ++i)
				for (int j = 1; j <= 100; ++j) {
					int u = i - sx / tile_size, v = j - sy / tile_size;
					if (u >= -tile_size && u * tile_size <= Core.WIDTH + tile_size && v >= -tile_size
							&& v * tile_size <= Core.HEIGHT) {
						String letter = null;
						for (String s : manager.map_id[i][j].split("-")) {
							if (s.contains("l")) {
								letter = s.substring(1);
								continue;
							}
							if (s.contains("e")) {
								if (manager.editor_pane.chckbxHienThiEntityh.isSelected())
									g.drawImage(entity_mau.get(EntityType.getType(s.substring(1)).image).image
											.get(0).texture, u * tile_size - t.x, v * tile_size - t.y, this);
								continue;
							}
							if (s.contains("a") && !manager.editor_pane.chckbxDiRaDang.isSelected()) {
								continue;
							}
							if (!s.contains("$")) {
								Texture texture = environment
										.get(Integer.valueOf(s.length() > 0 ? s.replace("a", "") : "0"));
								g.drawImage(texture.texture, u * tile_size - t.x, v * tile_size - t.y, this);
								if (manager.editor_pane.showCollide.isSelected() && texture.c) {
									g.setColor(new Color(255, 0, 0, 200));
									g.fillRect(u * tile_size - t.x, v * tile_size - t.y, tile_size, tile_size);
								}
							} else if (manager.editor_pane.showCollide.isSelected()) {
								g.setColor(new Color(255, 0, 0, 200));
								g.fillRect(u * tile_size - t.x, v * tile_size - t.y, tile_size, tile_size);
							}
						}
						if (letter != null) {
							g.setFont(font.deriveFont(((float) tile_size / letter.length())));
							if (manager.editor_pane.chckbxHienThiLocation.isSelected())
								g.drawString(letter, u * tile_size - t.x, (v + 1) * tile_size - t.y);
							g.setFont(font.deriveFont(18.0f));
						}
					}
				}
			if (manager.editor_pane.chckbxEditPath.isSelected()) {
				for (Point point : manager.path_edit) {
					int u = point.x - sx / tile_size, v = point.y - sy / tile_size;
					if (u >= -tile_size && u * tile_size <= Core.WIDTH + tile_size && v >= -tile_size
							&& v * tile_size <= Core.HEIGHT) {
						g.setColor(new Color(0, 0, 0, 200));
						g.fillRect(u * tile_size - t.x, v * tile_size - t.y, tile_size, tile_size);
					}
				}
			}
			int x = (sx + manager.mouse.x) / tile_size, y = (sy + manager.mouse.y) / tile_size;
			x = x - sx / tile_size;
			y = y - sy / tile_size;
			g.drawImage(environment.get(manager.img_review).texture, x * tile_size - t.x, y * tile_size - t.y, this);
			g.drawRect(x * tile_size - t.x, y * tile_size - t.y, tile_size, tile_size);
		} else if (manager.game_stage == Stage.MENU) {
			g.drawImage(getTexture("background.png").texture, 0, 0, Core.WIDTH, Core.HEIGHT, this);
			for (Button button : menu.buttons) {
				if (!button.h)
					g.drawImage(button.image, button.x, button.y, this);
				else
					g.drawImage(button.hover, button.x, button.y, this);
			}
		} else if (manager.game_stage == Stage.PLAYING) {
			int t1 = 0;
			for (int i = 1; i <= 100; ++i)
				for (int j = 1; j <= 100; ++j) {
					int u = i - sx / tile_size, v = j - sy / tile_size;
					if (u >= -1 && u * tile_size <= Core.WIDTH + tile_size && v >= -1 && v * tile_size <= Core.HEIGHT) {
						for (String s : manager.map_id[i][j].split("-")) {
							if (s.contains("$"))
								continue;
							Texture texture = environment.get(Integer.valueOf(s.length() > 0 ? s : "0"));
							g.drawImage(texture.texture, u * tile_size - t.x, v * tile_size - t.y, this); // source
						}
						if (manager.item_map[i][j] != null)
							g.drawImage(manager.item_map[i][j].maptexture, u * tile_size - t.x, v * tile_size - t.y,
									this);
						int xx = 0, yy = 0;
						if (manager.gameplay.playermanager != null)
							xx = manager.gameplay.playermanager.getX() / tile_size;
						if (manager.gameplay.playermanager != null)
							yy = manager.gameplay.playermanager.getY() / tile_size;
						int t2 = 0;
						String S = "";
						if (manager.top_id[i][j] != null && manager.top_id[i][j].contains("b"))
							S = manager.top_id[i][j].split("b")[0];
						t1 = GetTopNum(xx, yy);
						t2 = GetTopNum(i, j);
						if (manager.top_id[i][j] != null && (t1 != t2 || t1 == 0))
							for (String s : S.split("-")) {
								Texture texture = environment.get(Integer.valueOf(s.length() > 0 ? s : "0"));
								g.drawImage(texture.texture, u * tile_size - t.x, v * tile_size - t.y, this); // source
							}
					}
				}
			for (Entity en : manager.entity) {
				if (en.chat.length() > 0) {
					BufferedImage pub = getTexture("khung.png").texture;
					g.drawImage(getTexture("khung3.png").texture, (-manager.Scr.x + en.getX()),
							(int) (en.y - manager.Scr.y - pub.getHeight() * 1.2), this);
					g.drawImage(getTexture("khung1.png").texture, (-manager.Scr.x + en.getX()) - pub.getWidth() / 2,
							(int) (en.y - manager.Scr.y - pub.getHeight() * 1.2), this);
					int k = 1;
					for (int i = 0; i < en.chat.length() - 3; i++)
						if (i % 3 == 0) {
							g.drawImage(getTexture("khung.png").texture,
									(-manager.Scr.x + en.getX()) + pub.getWidth() * k,
									(int) (en.y - manager.Scr.y - pub.getHeight() * 1.2), this);
							k++;
						}
					g.drawImage(getTexture("khung2.png").texture, (-manager.Scr.x + en.getX()) + pub.getWidth() * k,
							(int) (en.y - manager.Scr.y - pub.getHeight() * 1.2), this);
					g.drawString(en.chat, (-manager.Scr.x + en.getX()),
							(int) (en.y - manager.Scr.y - pub.getHeight() * 0.5));
				}
				if (manager.gameplay.playermanager.target != null && manager.gameplay.playermanager.target.equals(en)) {
					BufferedImage tar = getTexture("target.png").texture;
					g.drawImage(tar,
							(en.x - manager.Scr.x) + en.image.get(0).texture.getWidth() / 2 - tar.getWidth() / 2,
							(int) (en.y - manager.Scr.y - tar.getHeight() * 0.75), this);
				}
				int t2 = GetTopNum(en.getX() / tile_size, en.getY() / tile_size);
				if (t1 == t2 || t2 == 0) {
					/*
					 * if (en.AI != null && en.AI.path != null) for (Point point : en.AI.path) if
					 * (Math.abs(point.x - 16 - manager.Scr.x) < Core.WIDTH && Math.abs(point.y - 16
					 * - manager.Scr.y) < Core.HEIGHT) g.drawRect(point.x - 16 - manager.Scr.x,
					 * point.y - 16 - manager.Scr.y, 32, 32);
					 */
					g.drawImage(en.image.get(en.img_index + en.stage * (en.image.size() / 4)).texture,
							en.x - manager.Scr.x, en.y - manager.Scr.y, this);
				}
			}
			int night = TimeFrame.isNight(manager.gameplay.tick);
			if (night != 0) {
				int dem = map(manager.gameplay.tick, (night <= TimeFrame.MORNING.from ? TimeFrame.MORNING.from : night),
						(night <= TimeFrame.MORNING.from ? 0 : 1440), 0, 400);
				graph.setComposite(AlphaComposite.Clear);
				graph.fillRect(0, 0, Core.WIDTH, Core.HEIGHT);
				for (Point point : lightlist.keySet()) {
					AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.SRC, 0.5F);
				    graph.setComposite(ac);
					int u = point.x - sx / tile_size, v = point.y - sy / tile_size;
					if (u >= -tile_size && u * tile_size <= Core.WIDTH + tile_size && v >= -tile_size
							&& v * tile_size <= Core.HEIGHT) {
						int xcac = point.x*tile_size-sx+tile_size/2, ycac = point.y*tile_size-sy+tile_size/2;
						int value = lightlist.get(point);
						int l1 = xcac - value*tile_size/2, r1 = ycac - value*tile_size/2, l2 = xcac + value*tile_size/2, r2 = ycac + value*tile_size/2;
						int d = shadow.get(0).getWidth()/2;
						int n = (l2 - l1 + 1)/ d;
						n += (n%2);
						double m = Math.sqrt(n*n*2);
						int od = n/2 + n%2;
						int x = 0, y = 0;
						boolean[][] dd = new boolean[40][40];
						for (int i = 0; i <= n + 2; ++i) 
							for (int j = 0; j <= n + 2; ++j) dd[i][j] = false;
						for (int i = 1; i <= n + 1 ; ++i)
							for (int j = 1; j <= n + 1; ++j) dd[i][j] = true;
						BufferedImage spark;
						int tt = 0;
						System.out.println(n + " " + od);
						while (true) {
							double leng = Point.distance(x,y,od,od);
							if (leng*100 > 47*n) {spark = shadow.get(0); tt = 6;}
							else if (leng*100 > 40*n) {spark = shadow.get(1); tt = 5;}
							else if (leng*100 > 35*n) {spark = shadow.get(2); tt = 4;}
							else if (leng*100 > 25*n) {spark = shadow.get(3); tt = 3;}
							else if (leng*100 > 15*n) {spark = shadow.get(4); tt = 2;}
							else if (leng*100 > 10*n) {spark = shadow.get(5); tt = 1;}
							else {spark = shadow.get(6); tt = 0;}
							
							g.drawImage(spark, l1+ x*d, r1+ y*d, this);
							dd[x + 1][y+ 1] = false;
							if (dd[x][y + 2] == false && dd[x+1][y + 2] == true) {++y;}
							else if  (dd[x+2][y+2] == false && dd[x+2][y+1] == true) {++x;}
							else if (dd[x+2][y] == false && dd[x+1][y] == true) {--y;}
							else if (dd[x][y] == false && dd[x][y+1] == true) {--x;}
							else break;
						}
					}
				}
				graph.setComposite(AlphaComposite.DstOver);
				graph.setColor(new Color(3, 0, 46, Math.abs((dem > 220 ? 220 : dem))));
				graph.fillRect(0, 0, getWidth(), getHeight());
				g.drawImage(lightframe,0,0,this);
			}
			g.setColor(Color.black);
			g.drawString(GetTime(manager.gameplay.tick) + " | " + TimeFrame.getType(manager.gameplay.tick).prefix, 120,
					50);
			int lmx = (manager.mouse.x + sx) / tile_size, lmy = (manager.mouse.y + sy) / tile_size;
			if (lmx >= 0 && lmy >= 0 && manager.item_map[lmx][lmy] != null)
				g.drawString(manager.item_map[lmx][lmy].name, manager.mouse.x, manager.mouse.y);
			if (manager.gameplay.open_inv) {
				BufferedImage inv = getTexture("inv.png").texture;
				g.drawImage(inv, Core.WIDTH / 2 - inv.getWidth() / 2, Core.HEIGHT / 2 - inv.getHeight() / 2, this);
				for (Item item : manager.gameplay.playermanager.inventory.items) {
					if (item.itemx == 0 && item.itemy == 0) {
						Random r = new Random();
						item.itemx = (Core.WIDTH - inv.getWidth()) / 2
								+ r.nextInt(inv.getWidth() - item.texture.getWidth());
						item.itemy = (Core.HEIGHT - inv.getHeight() + 300)
								+ r.nextInt(inv.getHeight() - item.texture.getHeight() - 100);
					}
					BufferedImage img = item.texture;
					g.drawImage(img, item.itemx, item.itemy, this);
					if (item instanceof IDCard) {
						IDCard card = (IDCard) item;
						BufferedImage en = card.entity.image.get(0).texture;
						g.drawImage(card.entity.image.get(0).texture,
								(int) (item.itemx + img.getWidth() * 0.25 - en.getWidth() / 2),
								(int) (item.itemy + img.getHeight() * 0.25 - en.getHeight() / 2), this);
						g.drawString(card.entity.name, (int) (item.itemx + img.getWidth() * 0.52),
								(int) (item.itemy + img.getHeight() * 0.15));
						g.drawString(card.entity.job.toString(), (int) (item.itemx + img.getWidth() * 0.49),
								(int) (item.itemy + img.getHeight() * 0.35));
						g.setColor(Color.black);
						g.drawString("Crime (" + card.entity.wanted + "%)", (int) (item.itemx + img.getWidth() * 0.3),
								(int) (item.itemy + img.getHeight() * 0.54));
						g.drawString("Skill (" + card.entity.skill + "%)", (int) (item.itemx + img.getWidth() * 0.3),
								(int) (item.itemy + img.getHeight() * 0.74));
						g.drawString("Mind (" + card.entity.knowledge + "%)",
								(int) (item.itemx + img.getWidth() * 0.63),
								(int) (item.itemy + img.getHeight() * 0.54));
						g.drawString("Power (" + card.entity.power + "%)", (int) (item.itemx + img.getWidth() * 0.63),
								(int) (item.itemy + img.getHeight() * 0.74));
						g.setColor(new Color(116, 63, 57));
						g.fillRect((int) (item.itemx + img.getWidth() * 0.3),
								(int) (item.itemy + img.getHeight() * 0.56),
								(int) (img.getWidth() / 4.5 * card.entity.wanted / 100), img.getHeight() / 20);
						g.fillRect((int) (item.itemx + img.getWidth() * 0.3),
								(int) (item.itemy + img.getHeight() * 0.76),
								(int) (img.getWidth() / 4.5 * card.entity.skill / 100), img.getHeight() / 20);
						g.fillRect((int) (item.itemx + img.getWidth() * 0.63),
								(int) (item.itemy + img.getHeight() * 0.56),
								(int) (img.getWidth() / 4.5 * card.entity.knowledge / 100), img.getHeight() / 20);
						g.fillRect((int) (item.itemx + img.getWidth() * 0.63),
								(int) (item.itemy + img.getHeight() * 0.76),
								(int) (img.getWidth() / 4.5 * card.entity.energy / 100), img.getHeight() / 20);

					}
					if (manager.mouse.x >= item.itemx && manager.mouse.x <= item.itemx + item.texture.getWidth()
							&& manager.mouse.y >= item.itemy
							&& manager.mouse.y <= item.itemy + item.texture.getHeight()) {

					}

				}
			}
			// Draw stats
			BufferedImage health = getTexture("health.png").texture;
			g.drawImage(health, 10, 10, this);
			g.drawImage(getTexture("energy.png").texture, 10, (health.getHeight()) + 10, this);
			g.drawImage(getTexture("hunger.png").texture, 10, (health.getHeight()) * 2 + 10, this);
			g.drawImage(getTexture("wanted.png").texture, 10, (health.getHeight()) * 3 + 10, this);
			g.setColor(Color.WHITE);
			g.drawString(manager.gameplay.playermanager.health + "", 10 + health.getWidth() / 2,
					(int) (10 + health.getHeight() * 0.7));
			g.drawString(manager.gameplay.playermanager.energy + "", 10 + health.getWidth() / 2,
					(int) (10 + health.getHeight() * 1.7));
			g.drawString(manager.gameplay.playermanager.hunger + "", 10 + health.getWidth() / 2,
					(int) (10 + health.getHeight() * 2.7));
			g.drawString(manager.gameplay.playermanager.wanted + "", 10 + health.getWidth() / 2,
					(int) (10 + health.getHeight() * 3.7));
		}
		g.drawString("FPS " + fps_display + " " + Runtime.getRuntime().totalMemory() / 1024 + "/"
				+ Runtime.getRuntime().maxMemory() / 1024, 30, 30);
		g.drawImage(cursor.texture, manager.mouse.x - cursor.texture.getWidth() / 2,
				manager.mouse.y - cursor.texture.getHeight() / 2, this);
	}

	
	public Path getPath(String name) {
		for (Path path : paths)
			if (path.name.equals(name))
				return path;
		return null;
	}

	public BufferedImage resize(BufferedImage img, int width, int height) {
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}
}