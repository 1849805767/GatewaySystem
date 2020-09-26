package com.example.processingdisplay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

public class SettingFragment extends Fragment {
    private static final String TAG = "SettingFragment";
    private TextView readText;
   // private EditText writeText;
    private Spinner baudSpinner;
    private Spinner stopSpinner;
    private Spinner dataSpinner;
    private Spinner paritySpinner;
    private Button openButton, writeButton, configButton, clearButton;
    private int baud;
    private int stop_bit;
    private int data_bit;
    private int parity;
    private boolean isOpen;
    private TestService.TestBinder mtestBinder;
    private MyTestServiceConn mtconn;

    public void setEditText(String str) {
        readText.setText(str);
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_setting,container,false);
        initUI(v);
        mtconn = new MyTestServiceConn();//绑定接收服务

        getActivity().getApplicationContext().bindService(new Intent(getActivity(), TestService.class), mtconn, BIND_AUTO_CREATE);
        final UserModel viewModel = ViewModelProviders.of(getActivity()).get(UserModel.class);
        viewModel.getData().observe(this, new Observer<CopyOnWriteArrayList<String>>() {
            @Override
            public void onChanged(CopyOnWriteArrayList<String> numbers) {
                //updateUI
                readText.setText("");
                for (int i = 0; i < numbers.size(); i++) {
                    readText.append(numbers.get(i));
                }
            }
        });

        isOpen = false;
        baud = 6;
        stop_bit = 1;
        data_bit = 4;
        parity = 4;
        //writeText.setInputType(EditorInfo.TYPE_NULL);
        openButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!isOpen) {
                    if (MyApp.driver.ResumeUsbList()) {//执行ResumeUsbList枚举，打开并初始化CH9326设备;或者使用EnumerateDevice后OpenDevice
                        Toast.makeText(getActivity(), "设备打开成功!",
                                Toast.LENGTH_SHORT).show();
                        isOpen = true;
                        mtestBinder.setisOpen(isOpen);
                        openButton.setText("Close");
                        mtestBinder.receiveData(viewModel);// 开启接收数据
                        mtestBinder.sendDataToWifi();
                        configButton.setEnabled(true);
                       // writeButton.setEnabled(true);
                    } else {
                        Toast.makeText(getActivity(), "设备打开失败!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    isOpen = false;
                    MyApp.driver.CloseDevice();// 关闭设备的函数
                    mtestBinder.setisOpen(isOpen);
                    mtestBinder.closeWifiThread();
                    openButton.setText("Open");
                    configButton.setEnabled(false);
                  //  writeButton.setEnabled(false);
                }
            }
        });

        configButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (MyApp.driver.SetConfig(baud, data_bit, stop_bit, parity))//函数说明请参照编程手册
                    Toast.makeText(getActivity(), "串口设置成功!",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "串口设置失败!",
                            Toast.LENGTH_SHORT).show();
            }
        });

//        writeButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                String writeString = writeText.getText().toString();
//                boolean flag = mtestBinder.sendData(writeString);
//                if(flag)
//                    Toast.makeText(getActivity(), "数据发送失败!",
//                            Toast.LENGTH_SHORT).show();
//
//            }
//        });
        return v;
    }
    private void initUI(View v) {

        openButton = (Button)v.findViewById(R.id.open_device);
       // writeButton = (Button)v.findViewById(R.id.WriteButton);
        configButton = (Button)v.findViewById(R.id.configButton);
        clearButton = (Button)v.findViewById(R.id.clearButton);
        configButton.setEnabled(false);
//        writeButton.setEnabled(false);
        readText = (TextView)v.findViewById(R.id.ReadValues);
//        writeText = (EditText)v.findViewById(R.id.WriteValues);

        baudSpinner = (Spinner)v.findViewById(R.id.baudRateValue);
        ArrayAdapter<CharSequence> baudAdapter = ArrayAdapter
                .createFromResource(Objects.requireNonNull(this.getContext()), R.array.baud_rate,
                        R.layout.my_spinner_textview);
        baudAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        baudSpinner.setAdapter(baudAdapter);
        baudSpinner.setGravity(0x10);
        baudSpinner.setSelection(5);

        stopSpinner = (Spinner)v.findViewById(R.id.stopBitValue);
        ArrayAdapter<CharSequence> stopAdapter = ArrayAdapter
                .createFromResource(this.getContext(), R.array.stop_bits,
                        R.layout.my_spinner_textview);
        stopAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        stopSpinner.setAdapter(stopAdapter);
        stopSpinner.setGravity(0x01);

        dataSpinner = (Spinner)v.findViewById(R.id.dataBitValue);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter
                .createFromResource(this.getContext(), R.array.data_bits,
                        R.layout.my_spinner_textview);
        dataAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        dataSpinner.setAdapter(dataAdapter);
        dataSpinner.setGravity(0x11);
        dataSpinner.setSelection(3);

        paritySpinner = (Spinner)v.findViewById(R.id.parityValue);
        ArrayAdapter<CharSequence> parityAdapter = ArrayAdapter
                .createFromResource(this.getContext(), R.array.parity,
                        R.layout.my_spinner_textview);
        parityAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        paritySpinner.setAdapter(parityAdapter);
        paritySpinner.setGravity(0x11);

        baudSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                baud = arg2 + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        stopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                stop_bit = arg2 + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        dataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                data_bit = arg2 + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        paritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                parity = arg2 + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                readText.setText("");
            }
        });

        paritySpinner.setSelection(3);


    }
}
