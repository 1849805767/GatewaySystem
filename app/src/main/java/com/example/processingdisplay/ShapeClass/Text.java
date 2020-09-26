package com.example.processingdisplay.ShapeClass;

public class Text extends Shape {
    private float startX,startY,frameHeight,frameWidth;
    private String context;
    private int Rf,Gf,Bf;

    public Text(String con,float X,float Y,float width,float height,int r,int g,int b){
        this.context = con;
        this.frameHeight = height;
        this.frameWidth = width;
        this.startX = X;
        this.startY = Y;
        this.Rf = r;
        this.Gf = g;
        this.Bf = b;
    }

    @Override
    public void drawMyShape() {
        getMysketch().fill(255);
        getMysketch().rect(startX, startY, frameWidth, frameHeight);
        getMysketch().textSize(30);
        getMysketch().fill(Rf,Gf,Bf);
        getMysketch().text(context, startX, startY, frameWidth, frameHeight);
    }
}
