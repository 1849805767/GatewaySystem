package com.example.processingdisplay;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.processingdisplay.ShapeClass.Shape;

import java.util.concurrent.CopyOnWriteArrayList;


public class UserModel extends ViewModel {
    private final MutableLiveData<CopyOnWriteArrayList<String>> liveData = new MutableLiveData<>();
    private final MutableLiveData<CopyOnWriteArrayList<Shape>> liveData2 = new MutableLiveData<>();

    public LiveData<CopyOnWriteArrayList<String>> getData(){
        return liveData;
    }
//    public LiveData<CopyOnWriteArrayList<Shape>> getShapes(){
//        return liveData2;
//    }
    public UserModel(){
        liveData.postValue(new CopyOnWriteArrayList<String>());
       // liveData2.postValue(new CopyOnWriteArrayList<Shape>());
    }
    public void doDataAction(String a){
        //it is not good for create and destory list fequently
        CopyOnWriteArrayList<String> list =  liveData.getValue();
        if (list != null) {
            list.add(a);
            liveData.postValue(list);
        }
    }
    public void doShapeAction(Shape a){
        CopyOnWriteArrayList<Shape> list =  liveData2.getValue();
        if (list != null) {
            list.add(a);
            liveData2.postValue(list);
        }
    }

}
