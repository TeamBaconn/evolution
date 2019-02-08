package com.Tuong.Display;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GUI {
	public ArrayList<Button> buttons = new ArrayList<Button>();
	public GUI(){
		
	}
	public void createButton(BufferedImage image,BufferedImage hover, int x, int y){
		buttons.add(new Button(image,hover, x, y));
	}
}
