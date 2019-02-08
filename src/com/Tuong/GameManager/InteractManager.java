package com.Tuong.GameManager;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.Tuong.Display.Button;
import com.Tuong.Display.EditorFrame;
import com.Tuong.Entity.Entity;
import com.Tuong.EntityType.EntityType;
import com.Tuong.Inventory.Item;

public class InteractManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	public GameManager manager;
	public boolean[] luu = new boolean[200];

	public int chuot = 0; // 1 : chuot trai, 2 : chuot phai
	public int type_img = 0; // 0 -> 4: so hinh anh
	public Item itemclick = null;

	public InteractManager() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (itemclick != null) {
							itemclick.itemx = manager.mouse.x - itemclick.texture.getWidth() / 2;
							itemclick.itemy = manager.mouse.y - itemclick.texture.getHeight() / 2;
						}
						if (manager.game_stage == Stage.EDITING) {
							int x = (manager.Scr.x + manager.mouse.x) / manager.display.tile_size,
									y = (manager.Scr.y + manager.mouse.y) / manager.display.tile_size;
							if(chuot == 1 && manager.editor_pane.textField_1.getText().length() > 0 && !manager.map_id[x][y].contains("-l")){
								manager.map_id[x][y] += "-l"+manager.editor_pane.textField_1.getText().toUpperCase();
							}else if(chuot == 1 && !manager.editor_pane.comboBox.getSelectedItem().equals(EntityType.NONE)){
								if(!manager.map_id[x][y].contains("e")) manager.map_id[x][y] += "-e"+((EntityType)manager.editor_pane.comboBox.getSelectedItem()).toString();
							}else if(chuot == 1 && manager.editor_pane.chckbxEditPath.isSelected()){
								if(!manager.path_edit.contains(new Point(x,y)))manager.path_edit.add(new Point(x, y));
							}else if (chuot == 1 && !manager.map_id[x][y].contains(type_img + "")) {
								if (!manager.editor_pane.chckbxDiRaDang.isSelected())
									manager.map_id[x][y] += "-" + type_img;
								else{
									manager.map_id[x][y] += "-a" + type_img;
								}
							} else if (chuot == 2) {
								if (manager.map_id[x][y].split("-").length == 1)
									manager.map_id[x][y] = "$";
								else{
									manager.map_id[x][y] = manager.map_id[x][y].substring(0,
											manager.map_id[x][y].length()
													- manager.map_id[x][y]
															.split("-")[manager.map_id[x][y].split("-").length-1].length()
													- 1);
									Thread.sleep(40);
								}
							}
						}
						Thread.sleep(5);
					} catch (Exception e) {
					}
				}
			}
		});
		thread.start();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		luu[e.getKeyCode()] = true;
		if (manager.game_stage == Stage.PLAYING) {
			if (e.getKeyChar() == 'e') {
				if (!manager.gameplay.open_inv)
					manager.gameplay.openInventory(manager.gameplay.playermanager);
				else
					manager.gameplay.open_inv = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (manager.display.cursor.name.equals("cursor_normal.png"))
					manager.display.cursor = manager.display.getTexture("cursor_attack.png");
				else
					manager.display.cursor = manager.display.getTexture("cursor_normal.png");
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		luu[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			chuot = 1;
			if (manager.game_stage == Stage.MENU) {
				int i = 0;
				for (Button button : manager.display.menu.buttons) {
					if (e.getX() >= button.x && e.getX() <= button.x + button.image.getWidth() && e.getY() >= button.y
							&& e.getY() <= button.y + button.image.getHeight()) {
						if (i == 0) {
							// Play
							manager.game_stage = Stage.PLAYING;
							manager.gameplay = new GameplayManager(manager, "testmap");
						} else if (i == 1) {
							// Edit
							manager.game_stage = Stage.EDITING;
							manager.editor_pane = new EditorFrame(manager);
						}
					}
					i++;
				}
			} else if (manager.game_stage == Stage.PLAYING) {
				for (Entity en : manager.entity) {
					if (Math.abs(en.getX() - (manager.mouse.x + manager.Scr.x)) < en.hitbox_x
							&& Math.abs(en.getY() - (manager.mouse.y + manager.Scr.y)) < en.hitbox_y) {
						if (!manager.display.cursor.name.equals("cursor_normal.png")) {
							if (manager.gameplay.playermanager.target == null
									|| !manager.gameplay.playermanager.target.equals(en))
								manager.gameplay.playermanager.target = en;
							else
								manager.gameplay.playermanager.target = null;
						}
					}
				}
				int lmx = (manager.mouse.x + manager.Scr.x) / manager.display.tile_size,
						lmy = (manager.mouse.y + manager.Scr.y) / manager.display.tile_size;
				if (manager.item_map[lmx][lmy] != null
						&& manager.gameplay.playermanager.addItem(manager.item_map[lmx][lmy], lmx, lmy, false)) {
					manager.item_map[lmx][lmy] = null;
				}

				if (manager.gameplay.open_inv) {
					if (itemclick == null) {
						for (Item item : manager.gameplay.inv_owner.inventory.items) {
							if (manager.mouse.x >= item.itemx && manager.mouse.x <= item.itemx + item.texture.getWidth()
									&& manager.mouse.y >= item.itemy
									&& manager.mouse.y <= item.itemy + item.texture.getHeight()) {
								itemclick = item;
								break;
							}
						}
					} else {
						itemclick = null;
					}
				}
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			chuot = 2;
			if (manager.game_stage == Stage.PLAYING) {
				int i = 0;
				if (manager.gameplay.open_inv)
					if (manager.item_map[manager.gameplay.playermanager.getX()
							/ manager.display.tile_size][manager.gameplay.playermanager.getY()
									/ manager.display.tile_size] == null) {
						for (Item item : manager.gameplay.inv_owner.inventory.items) {
							if (manager.mouse.x >= item.itemx && manager.mouse.x <= item.itemx + item.texture.getWidth()
									&& manager.mouse.y >= item.itemy
									&& manager.mouse.y <= item.itemy + item.texture.getHeight()) {
								manager.item_map[manager.gameplay.playermanager.getX()
										/ manager.display.tile_size][manager.gameplay.playermanager.getY()
												/ manager.display.tile_size] = manager.gameplay.inv_owner.inventory.items
														.get(i);
								manager.gameplay.inv_owner.removeItem(i);
								break;
							}
							i++;
						}
					}
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			chuot = 3;
			manager.tScr.x = e.getX();
			manager.tScr.y = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		chuot = 0; // nha chuot ra
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		manager.mouse.x = e.getX(); // keo chuot
		manager.mouse.y = e.getY();
		if (chuot == 3) {
			manager.Scr.x -= manager.mouse.x - manager.tScr.x;
			manager.Scr.y -= manager.mouse.y - manager.tScr.y;
			manager.tScr.x = manager.mouse.x;
			manager.tScr.y = manager.mouse.y;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		manager.mouse.x = e.getX();
		manager.mouse.y = e.getY();
		if (manager.game_stage == Stage.MENU) {
			for (Button button : manager.display.menu.buttons)
				if (e.getX() >= button.x && e.getX() <= button.x + button.image.getWidth() && e.getY() >= button.y
						&& e.getY() <= button.y + button.image.getHeight()) {
					button.h = true;
				} else
					button.h = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		try {
			int notches = e.getWheelRotation();

			if (notches > 0) {
				if (type_img < manager.display.environment.size() - 1)
					type_img++;
				else
					type_img = 0;
			} else if (notches < 0) {
				if (type_img > 0)
					type_img--;
				else
					type_img = manager.display.environment.size() - 1;
			}
			manager.editor_pane.lblId.setText("ID: " + type_img);
			manager.img_review = type_img;
		} catch (Exception ex) {
		}
	}
}