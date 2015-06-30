package com.jzs.android.systemstyle.controls;

import android.graphics.drawable.Drawable;

public class SimpleStyleItem {
	Drawable icon;
	String title;
	int id;
	
	public SimpleStyleItem(String t, Drawable dr, int k){
		title = t;
		icon = dr;
		id = k;
	}
}
