package com.example.processingdisplay.ShapeClass;

public class Circle extends Shape {
    private int  Rf,Gf,Bf, Af;
    private float startX,startY,frameHeight,frameWidth;



    public Circle(float X,float Y,float width,float height,int r,int g,int b,int a){
        this.frameHeight = height;
        this.frameWidth = width;
        this.startX = X;
        this.startY = Y;
        this.Rf = r;
        this.Gf = g;
        this.Bf = b;
        this.Af = a;
    }


    public void drawMyShape() {
        getMysketch().fill(255);
        getMysketch().stroke(255);
        getMysketch().rect(startX, startY, frameWidth, frameHeight);
        getMysketch().fill(Rf,Gf,Bf, Af);
        getMysketch().ellipse(startX, startY, frameWidth, frameHeight);
    }

}
