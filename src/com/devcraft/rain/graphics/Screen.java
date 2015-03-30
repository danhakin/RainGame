package com.devcraft.rain.graphics;

public class Screen {
	
	private int width;
	
	private int height;
	
	public int[] pixels;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

}
