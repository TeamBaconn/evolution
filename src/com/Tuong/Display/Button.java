package com.Tuong.Display;

import java.awt.image.BufferedImage;

public class Button {
	public int x,y;
	public BufferedImage image;
	public BufferedImage hover;
	public boolean h = false;
	public Button(BufferedImage image,BufferedImage hover, int x, int y){
		this.x = x;
		this.y = y;
		this.image = image;
		this.hover = hover;
	}
}
