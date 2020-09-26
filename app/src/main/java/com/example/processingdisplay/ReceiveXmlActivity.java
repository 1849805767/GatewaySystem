package com.example.processingdisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReceiveXmlActivity extends AppCompatActivity {
    private static final String TAG = "SettingXmlActivity";
    private EditText ed_color,ed_bit,ed_thr_min,ed_thr_max;
    private LinearLayout mLayout;
    private Button btn_addData,btn_cancel,btn_submit;
    private RadioGroup mShapeGroup,mTypeGroup,mAgreementGroup;
    private RadioButton rb_shape_cir,rb_shape_rec,rb_data_int,rb_data_float,rb_data_string,rb_pro_zigbee,rb_pro_bTooth;
    private ArrayList<DataSet> mDataSets = new ArrayList<>();
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_setting);
        mLayout = (LinearLayout)findViewById(R.id.ll_add_contain);
        ed_color = (EditText)findViewById(R.id.edit_col);
        ed_bit = (EditText)findViewById(R.id.edit_data_bit);
        ed_thr_max = (EditText)findViewById(R.id.edit_datathr_max);
        ed_thr_min = (EditText)findViewById(R.id.edit_datathr_min);
        btn_addData = (Button)findViewById(R.id.button_adddata);
        btn_cancel = (Button)findViewById(R.id.button_cancel);
        btn_submit = (Button)findViewById(R.id.button_submit);
        mShapeGroup = (RadioGroup)findViewById(R.id.radioGroup_shape);
        mTypeGroup = (RadioGroup)findViewById(R.id.radioGroup_datatype);
        mAgreementGroup = (RadioGroup)findViewById(R.id.radioGroup_protocol);
        rb_shape_cir = (RadioButton)findViewById(R.id.radioButton_shape_cir);
        rb_shape_rec = (RadioButton)findViewById(R.id.radioButton_shape_rec);
        rb_data_int = (RadioButton)findViewById(R.id.radioButton_dt_int);
       // rb_data_float = (RadioButton)findViewById(R.id.radioButton_dt_float);
        rb_data_string = (RadioButton)findViewById(R.id.radioButton_dt_string);
        rb_pro_zigbee = (RadioButton)findViewById(R.id.rb_pro_zigbee);
        rb_pro_bTooth = (RadioButton)findViewById(R.id.rb_pro_bluetooth);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: layout");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        btn_addData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                DataSet data = new DataSet();
                String a = ((RadioButton)findViewById(mTypeGroup.getCheckedRadioButtonId())).getText().toString();
                data.setDataType(a);
                data.setDataBit(Integer.parseInt(ed_bit.getText().toString()));
                data.setDataThrMin(Integer.parseInt(ed_thr_min.getText().toString()));
                data.setDataThrMax(Integer.parseInt(ed_thr_max.getText().toString()));
                mDataSets.add(data);
                addView(v);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiveXmlActivity.this.finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subToXml();
                ReceiveXmlActivity.this.finish();

            }
        });


    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    public void addView(View v) {
        // 调用一个参数的addView方法
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(lLayoutlayoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        Log.d(TAG, "addView: linearlayout is ok");
        TextView childnum = new TextView(this);
        int num = ++i;
        childnum.setText(""+num);
        LinearLayout.LayoutParams txParam1 = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        childnum.setLayoutParams(txParam1);
        childnum.setBackgroundColor(333333);
        layout.addView(childnum);

        TextView childtype = new TextView(this);
        String type = ((RadioButton)findViewById(mTypeGroup.getCheckedRadioButtonId())).getText().toString();
        childtype.setText(type);
        LinearLayout.LayoutParams txParam2 = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        childtype.setLayoutParams(txParam2);
        layout.addView(childtype);

        TextView childbit = new TextView(this);
        String bit = ed_bit.getText().toString();
        childbit.setText(bit);
        LinearLayout.LayoutParams txParam3 = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        childbit.setLayoutParams(txParam3);
        layout.addView(childbit);

        TextView childmin = new TextView(this);
        String min = ed_thr_min.getText().toString();
        childmin.setText(min);
        LinearLayout.LayoutParams txParam4 = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        childmin.setLayoutParams(txParam4);
        layout.addView(childmin);

        TextView childmax = new TextView(this);
        String max = ed_thr_max.getText().toString();
        childmax.setText(max);
        LinearLayout.LayoutParams txParam5 = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        childmax.setLayoutParams(txParam5);
        layout.addView(childmax);
        Log.d(TAG, "addView: textviews is ok");
        mLayout.addView(layout);
        Log.d(TAG, "addView: add over");
    }

    public void subToXml(){
        String color = ed_color.getText().toString();
        String shape = ((RadioButton)findViewById(mShapeGroup.getCheckedRadioButtonId())).getText().toString();
        SharedPreferences sp = getSharedPreferences("dataReceive", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(getString(R.string.scolor), color);
        edit.putString(getString(R.string.sshape), shape);
        edit.putInt(getString(R.string.sdatacount),i);
        for(int j = 0;j<mDataSets.size();j++){
            String type = mDataSets.get(j).getDataType();
            int bit = mDataSets.get(j).getDataBit();
            int max = mDataSets.get(j).getDataThrMax();
            int min = mDataSets.get(j).getDataThrMin();
            Log.d(TAG, "subToXml: "+max+" "+min);
            edit.putString(getString(R.string.stype)+j, type);
            edit.putInt(getString(R.string.sbit)+j,bit);
            edit.putInt(getString(R.string.smax)+j,max);
            edit.putInt(getString(R.string.smin)+j,min);
        }
        edit.apply();
        Log.d(TAG, "subToXml: ");
    }


}
