package com.example.processingdisplay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.processingdisplay.ShapeClass.Circle;
import com.example.processingdisplay.ShapeClass.Rectangle;
import com.example.processingdisplay.ShapeClass.Shape;
import com.example.processingdisplay.ShapeClass.Text;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import processing.android.PFragment;


public class DrawFragment extends PFragment {
    private static final String TAG = "DrawFragment";
    private Sketch sketch = new Sketch();

    @Override
    public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {

        this.setSketch(sketch);
        final UserModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserModel.class);
        viewModel.getData().observe(this, new Observer<CopyOnWriteArrayList<String>>() {
            @Override
            public void onChanged(CopyOnWriteArrayList<String> numbers) {
                creatShape(numbers);
                //sketch.setShapes(numbers);
            }
        });
        return super.onCreateView(var1, var2, var3);
    }

    private void creatShape(CopyOnWriteArrayList<String> numbers) {
        SharedPreferences sharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("dataReceive", Context.MODE_PRIVATE);
        int count=sharedPreferences.getInt(getString(R.string.sdatacount),0);
        String shapeType = sharedPreferences.getString(getString(R.string.sshape),"aaa");
        Log.d(TAG, "creatShape:shapetype is "+shapeType);
        String shapeColor = sharedPreferences.getString(getString(R.string.scolor),"000000");
        String[] col = shapeColor.split(",");
        float height = ((float)(sketch.height))/count;
        float width = ((float)(sketch.width))/20;
        for(int i = 0;i<numbers.size();i++){
            String str = numbers.get(i);
            if(!(str.equals("null")&&count>1)){
                String[] shapestr = str.split(" ");
                for(int j = 0;j<count;j++){
                    int min = sharedPreferences.getInt(getString(R.string.smin)+j,0);
                    int max = sharedPreferences.getInt(getString(R.string.smax)+j,0);
                    String type = sharedPreferences.getString(getString(R.string.stype)+j,"");
                    switch (type){
                        case"int":
                            double threshold = Integer.parseInt(shapestr[j]);
                            Log.d("DrawFragment", "creatShape:alpha "+max+" "+min+" "+threshold);
                            int alpha = (int)((threshold/(max-min))*255);
                            Shape s;
                            if(shapeType.equals("circle")){
                                s = new Circle((width*i)%sketch.width+(width/2),(height*j)%sketch.height+(height/2),width,height,
                                        Integer.parseInt(col[0],16),
                                        Integer.parseInt(col[1],16),
                                        Integer.parseInt(col[2],16),alpha);
                            }else{
                                s = new Rectangle((width*i)%sketch.width,(height*j)%sketch.height,width,height,
                                        Integer.parseInt(col[0],16),
                                        Integer.parseInt(col[1],16),
                                        Integer.parseInt(col[2],16),alpha);
                            }
                            sketch.setShapes(s);
                            break;
                        case "string":
                            Text t = new Text(shapestr[j],(width*i)%sketch.width,(height*j)%sketch.height,width,height,
                                    Integer.parseInt(col[0],16), Integer.parseInt(col[1],16), Integer.parseInt(col[2],16));
                            sketch.setShapes(t);
                            break;
                        default:
                            Log.d("DrawFragment", "creatShape: can not verify this shape");
                            break;
                    }

                }
            }


        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(
                    requestCode, permissions, grantResults);
        }
    }

}
