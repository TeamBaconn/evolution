package com.Tuong.Core;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import javax.swing.JFrame;

import com.Tuong.Display.DisplayCore;
import com.Tuong.GameManager.GameManager;
import com.Tuong.GameManager.InteractManager;

public class Core{
	
	public static JFrame frame;
	public static int WIDTH =  Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int HEIGHT =  Toolkit.getDefaultToolkit().getScreenSize().height;
	private static Scanner scanner;
	
	public static void main(String[] args){
		frame = new JFrame("Evolution");
		InteractManager interact = new InteractManager();
		DisplayCore display = new DisplayCore();
		frame.getContentPane().add(display);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(interact);
		frame.addMouseListener(interact);
		frame.addMouseMotionListener(interact);
		frame.addMouseWheelListener(interact);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setUndecorated(true);
		frame.setResizable(false);
		GameManager manager = new GameManager(display,interact);
		
		display.manager = manager;
		
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");
		frame.getContentPane().setCursor(blankCursor);
		scanner = new Scanner(System.in);
	    String cmd;  
		while (true) {
			cmd = scanner.nextLine();
			if(cmd.contains("time")) manager.gameplay.tick = Float.valueOf(cmd.replace("time ", ""));
			if(cmd.contains("speed")) manager.gameplay.time_speed = Float.valueOf(cmd.replace("speed ", ""));
	    }
	}
}
