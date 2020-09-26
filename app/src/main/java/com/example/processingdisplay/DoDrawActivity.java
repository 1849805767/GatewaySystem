package com.example.processingdisplay;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import cn.bmob.v3.Bmob;
import cn.wch.ch9326driver.CH9326UARTDriver;



public class  DoDrawActivity extends AppCompatActivity {
    private static final String TAG = "DoDrawActivtiy";
    private final String ACTION_USB_PERMISSION = "cn.wch.CH9326UARTDemoDriver.USB_PERMISSION";
    private TestService.TestBinder mtestBinder;
    private MyTestServiceConn mtconn;
    private class MyTestServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mtestBinder = (TestService.TestBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_draw);
        mtconn = new MyTestServiceConn();//绑定接收服务
        bindService(new Intent(DoDrawActivity.this, TestService.class), mtconn, BIND_AUTO_CREATE);
        Bmob.initialize(this, "61819170a5beb34dffab23434e14a46d");
        MyApp.driver = new CH9326UARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), this,
                ACTION_USB_PERMISSION);
        if (!MyApp.driver.UsbFeatureSupported())// 判断系统是否支持USB HOST
        {
            Dialog dialog = new AlertDialog.Builder(DoDrawActivity.this)
                    .setTitle("提示")
                    .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    System.exit(0);
                                }
                            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.xmldialog_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.receive_setting:
               i = new Intent(DoDrawActivity.this, ReceiveXmlActivity.class);
               startActivity(i);
               break;
            case R.id.send_setting:
                i = new Intent(DoDrawActivity.this, SendXmlActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}