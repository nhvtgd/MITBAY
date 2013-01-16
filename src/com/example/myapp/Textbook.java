package com.example.myapp;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.example.myapp.Sellable.Condition;
import com.example.myapp.Sellable.SellType;

public class Textbook extends Sellable{

	public Textbook(User seller, String name, String price, SellType type,
			String description, Condition condition, ArrayList<Bitmap> images) {
		super(seller, name, price, type, description, condition, images);
		// TODO Auto-generated constructor stub
	}

}
