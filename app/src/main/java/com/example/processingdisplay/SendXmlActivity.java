package com.example.processingdisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SendXmlActivity extends AppCompatActivity {
    private static final String TAG = "SendXmlActivity";
    private EditText ed_ip,ed_port;
    private RadioGroup mTransportGroup;
    private RadioButton rb_socket,rb_http,rb_serial;
    private LinearLayout ll_setname;
    private Button btn_cancel,btn_submit;
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_xml);
        SharedPreferences sp = getSharedPreferences("dataReceive", MODE_PRIVATE);
        count = sp.getInt(getString(R.string.sdatacount),0);
        ed_ip = (EditText)findViewById(R.id.edit_ip);
        ed_port = (EditText)findViewById(R.id.edit_port);
        mTransportGroup = (RadioGroup)findViewById(R.id.radioGroup_transport);
        rb_http = (RadioButton)findViewById(R.id.rb_ts_http);
        rb_socket = (RadioButton)findViewById(R.id.rb_ts_socket);
        rb_serial = (RadioButton)findViewById(R.id.rb_ts_serial);
        ll_setname = (LinearLayout)findViewById(R.id.ll_set_name_contain);
        btn_cancel = (Button)findViewById(R.id.button_cancel);
        btn_submit = (Button)findViewById(R.id.button_submit);
        showSetNameView();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendXmlActivity.this.finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subToXml();
                SendXmlActivity.this.finish();

            }
        });
    }

    private void subToXml() {
        String ip = ed_ip.getText().toString();
        String port = ed_port.getText().toString();
        String protocol = ((RadioButton)findViewById(mTransportGroup.getCheckedRadioButtonId())).getText().toString();
        SharedPreferences spSend = getSharedPreferences("dataSend", MODE_PRIVATE);
        SharedPreferences.Editor edit = spSend.edit();
        edit.putString(getString(R.string.sip), ip);
        edit.putString(getString(R.string.sport), port);
        edit.putString(getString(R.string.sagreement), protocol);
        for(int j = 0;j<count;j++){
            EditText ed = (EditText)findViewById(R.id.edit_text_0+j);
           String name = ed.getText().toString();
            Log.d(TAG, "subToXml: "+name);
            edit.putString(getString(R.string.sname)+j, name);
        }
        edit.apply();
    }

    private void showSetNameView() {

        for(int i = 0;i<count;i++){
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(lLayoutlayoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            Log.d(TAG, "addView: linearlayout is ok");
            TextView childnum = new TextView(this);
            int num = i+1;
            childnum.setText(""+num);
            LinearLayout.LayoutParams txParam1 = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            childnum.setLayoutParams(txParam1);
            layout.addView(childnum);

            EditText chilname = new EditText(this);
            LinearLayout.LayoutParams txParam2 = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f);
            chilname.setLayoutParams(txParam2);
            chilname.setId(R.id.edit_text_0+i);
            chilname.setTextSize(12);
            layout.addView(chilname);
            ll_setname.addView(layout);
            Log.d(TAG, "addView: add over");
        }

    }
}
