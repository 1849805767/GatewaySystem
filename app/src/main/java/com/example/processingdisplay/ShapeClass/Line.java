package com.example.processingdisplay.ShapeClass;

import com.example.processingdisplay.ShapeClass.Shape;

public class Line extends Shape {
   private int X1, Y1, X2, Y2, R, G, B, Alpha;

    public Line(int Id) {
        initIdSty(Id);
    }

    public void initMyShape(int X1, int Y1, int X2, int Y2, int R, int G, int B, int Alpha) {

        this.X1 = X1;
        this.X2 = X2;
        this.Y1 = Y1;
        this.Y2 = Y2;
        this.R = R;
        this.G = G;
        this.B = B;
        this.Alpha = Alpha;
    }

    public void drawMyShape() {
        getMysketch().stroke(R, G, B, Alpha);
        getMysketch().line(X1, Y1, X2, Y2);

        // 颜色初始化
    }

}
