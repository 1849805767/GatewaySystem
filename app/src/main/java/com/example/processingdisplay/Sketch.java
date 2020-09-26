package com.example.processingdisplay;

import android.graphics.Color;
import android.util.Log;

import com.example.processingdisplay.ShapeClass.Shape;

import java.util.concurrent.CopyOnWriteArrayList;

import processing.core.PApplet;


public class Sketch extends PApplet {
    private static final String TAG = "Sketch";
    private CopyOnWriteArrayList<Shape> shapes = new CopyOnWriteArrayList<>();
    public void setShapes(Shape shape){
        this.shapes.add(shape);
    }

    public void settings() {
        size(900, 900);


    }

    public void setup() {

       }

    public void draw() {
        background(255);
         for (Shape myShape : shapes)//遍历数组中的每个shape，将他们画出来
        {
            if (shapes.size() > 0) {
                myShape.setSketch(this);
                myShape.drawMyShape();
            }
        }
        Log.d(TAG, "draw: sketchaaa");
         delay(1000);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }
}
