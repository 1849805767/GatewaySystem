package com.example.processingdisplay.ShapeClass;

import com.example.processingdisplay.ShapeClass.Shape;

public class Triangle extends Shape {
    private int X1, Y1, X2, Y2, X3, Y3, Rf, Gf, Bf, Af, Rs, Gs, Bs, As, isFill;

    public Triangle(int Id)
    {
        initIdSty(Id);
    }

   public void initMyShape(int X1, int  Y1, int X2, int  Y2, int X3, int  Y3, int Rs, int Gs, int Bs,int As, int isFill, int Rf, int Gf, int Bf, int Af)
    {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 =Y2;
        this.X3 = X3;
        this.Y3 =Y3;
        this.Rs = Rs;
        this.Gs = Gs;
        this.Bs = Bs;
        this.As = As;
        this.isFill = isFill;
        this.Rf = Rf;
        this.Gf = Gf;
        this.Bf = Bf;
        this.Af = Af;
    }

    public  void drawMyShape() {
        if (isFill==0) getMysketch().noFill();
        else
            getMysketch().fill(Rf, Gf, Bf,Af);
        getMysketch().stroke(Rs, Gs, Bs,As);
        getMysketch().triangle(X1, Y1, X2, Y2, X3, Y3);
    }

}
