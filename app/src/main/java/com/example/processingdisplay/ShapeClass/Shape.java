package com.example.processingdisplay.ShapeClass;

import com.example.processingdisplay.Sketch;

public abstract class Shape {
    private int Id = 0;
    private Sketch mysketch = new Sketch();
    public Sketch getMysketch() {
        return mysketch;
    }

    int GetId() {
        return Id;
    }


    void initIdSty(int id) {
        Id = id;
    }

    public void setSketch(Sketch sketch)
    {
        this.mysketch = sketch;
    }

    public abstract void drawMyShape();


}
