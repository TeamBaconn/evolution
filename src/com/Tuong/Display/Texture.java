package com.Tuong.Display;

import java.awt.image.BufferedImage;

public class Texture {
	public String name;
	public BufferedImage texture;
	public boolean c;
	public Texture(String name, BufferedImage texture, boolean c){
		this.name = name;
		this.texture = texture;
		this.c = c;
	}
}
