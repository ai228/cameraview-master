/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.cameraview.demo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import com.google.android.cameraview.demo.BackgoundcolorSeekBar;
import com.google.android.cameraview.demo.FontsizeRaeSeekBar;
import com.google.android.cameraview.demo.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SetUpActivity extends AppCompatActivity {
    private static final String TAG = "SetUpActivity";
    EditText et_projectName;
    EditText et_projectAdd;
    EditText et_abtainCompany;
    EditText et_titileShow;
    EditText et_projectTime;
    EditText et_location;
    EditText et_content;
    EditText et_longitude_latitude;
    EditText et_weather;
    LinearLayout ll_titlecolor_1;
    LinearLayout ll_titlecolor_2;
    LinearLayout ll_titlecolor_3;
    LinearLayout ll_titlecolor_4;
    LinearLayout ll_titlecolor_5;
    LinearLayout ll_titlecolor_6;
    LinearLayout ll_titlecolor_7;
    LinearLayout ll_fontcolor_1;
    LinearLayout ll_fontcolor_2;
    LinearLayout ll_fontcolor_3;
    LinearLayout ll_fontcolor_4;
    LinearLayout ll_fontcolor_5;
    LinearLayout ll_fontcolor_6;
    LinearLayout ll_fontcolor_7;
    //变量参数
    public boolean b_voice_switch;
    public boolean b_watermark_switch = true;
    public boolean b_abtain_switch = true;
    public boolean b_weather_switch = true;
    public boolean b_longitude_switch = true;
    public boolean b_content = true;
    //private boolean b_altude_switch;//海拔开关
    public boolean b_add_switch = true;
    public boolean b_projectname_switch = true;
    public boolean b_place_switch = true;
    public boolean b_time_switch = true;
    public boolean b_custom_switch = false;
    public boolean b_titileShow_switch = true;
    public int background_color = -1;
    public int front_color = -1;
    public int front_color_flag = -1;
    public int front_size_flag=1;
    public int background_color_depth_flag=1;
    Switch sh_voice_switch;
    Switch sh_watermark_switch;
    Switch sh_abtain_switch;
    Switch sh_watermark_weather;
    Switch sh_watermark_longitude;
    //Switch sh_watermark_altitude;//海拔开关
    Switch sh_watermark_add;
    Switch sh_watermark_neighborhood;
    Switch sh_watermark_projectname;
    Switch sh_watermark_projectadd;
    Switch sh_watermark_projecttime;
    Switch sh_watermark_custom;
    Switch sh_titileShow_switch;
    Switch sh_content;

    ImageView iv_titilecolor_1;ImageView iv_fontcolor_1;
    ImageView iv_titilecolor_2;ImageView iv_fontcolor_2;
    ImageView iv_titilecolor_3;ImageView iv_fontcolor_3;
    ImageView iv_titilecolor_4;ImageView iv_fontcolor_4;
    ImageView iv_titilecolor_5;ImageView iv_fontcolor_5;
    ImageView iv_titilecolor_6;ImageView iv_fontcolor_6;
    ImageView iv_titilecolor_7;ImageView iv_fontcolor_7;

    String str_projectname;
    String str_place;
    String str_titileShow;
    String str_weather;
    String str_location;
    String str_longitude_latitude;
    String str_time;
    String str_content;
    String str_add;
    String str_abtainCompany;
    private SharedPreferences sp;
    private SharedPreferences.Editor mSpEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        sp = getSharedPreferences("camera", MODE_PRIVATE);
        mSpEdit = sp.edit();
        b_voice_switch = sp.getBoolean("sh_voice_switch",true);
        str_titileShow = sp.getString("str_titileShow","");
        str_content = sp.getString("et_content","");
        str_time = sp.getString("str_time","");
        str_weather = sp.getString("str_weather","");
        str_abtainCompany = sp.getString("et_abtainCompany","");
        b_watermark_switch = sp.getBoolean("sh_watermark_switch",true);
        b_abtain_switch = sp.getBoolean("sh_abtain_switch",true);
        b_place_switch = sp.getBoolean("sh_watermark_projectadd",true);
        b_titileShow_switch = sp.getBoolean("sh_titileShow_switch",true);
        b_projectname_switch = sp.getBoolean("sh_watermark_projectname",true);
        b_add_switch = sp.getBoolean("sh_watermark_add",true);
        b_content = sp.getBoolean("sh_content",true);
        b_time_switch = sp.getBoolean("sh_watermark_projecttime",true);
        b_longitude_switch = sp.getBoolean("sh_watermark_longitude",true);
        b_weather_switch = sp.getBoolean("sh_watermark_weather",true);
        background_color = sp.getInt("background_color",-1);
        front_color_flag = sp.getInt("front_color_flag",-1);
        front_size_flag = sp.getInt("front_size_flag",-1);
        background_color_depth_flag = sp.getInt("background_color_depth_flag",1);

        //Density.setDensity(getApplication(),this);
        Intent intent1 = getIntent();
        str_projectname = intent1.getStringExtra("str_projectname");
        str_place = intent1.getStringExtra("str_place");
        str_titileShow = intent1.getStringExtra("str_titileShow");
        str_longitude_latitude = intent1.getStringExtra("str_longitude_latitude");
        str_abtainCompany = intent1.getStringExtra("str_abtainCompany");
        str_content = intent1.getStringExtra("str_content");
        str_add = intent1.getStringExtra("str_add");
        str_time = intent1.getStringExtra("str_time");
        str_location = intent1.getStringExtra("str_location");
        str_weather = intent1.getStringExtra("str_weather");
        Toolbar toolbar = findViewById(R.id.toolbar);
        et_projectName = findViewById(R.id.et_projectName);
        et_weather = findViewById(R.id.et_weather);
        et_projectName.setText(str_projectname);
        et_projectAdd = findViewById(R.id.et_projectAdd);
        et_abtainCompany = findViewById(R.id.et_abtainCompany);
        et_titileShow = findViewById(R.id.et_titileShow);
        et_titileShow.setText(str_titileShow);
        et_weather.setText(str_weather);
        et_projectAdd.setText(str_place);
        et_abtainCompany.setText(str_abtainCompany);
        et_location = findViewById(R.id.et_location);
        et_location.setText(str_location);
        et_content = findViewById(R.id.et_content);
        et_content.setText(str_content);
        et_longitude_latitude = findViewById(R.id.et_longitude_latitude);
        et_longitude_latitude.setText(str_longitude_latitude);
        //et_projectAdd.setText(str_place);
        et_projectTime = findViewById(R.id.et_projectTime);
        et_projectTime.setText(str_time);
        iv_titilecolor_1 = findViewById(R.id.iv_titilecolor_1);
        iv_titilecolor_2 = findViewById(R.id.iv_titilecolor_2);
        iv_titilecolor_3 = findViewById(R.id.iv_titilecolor_3);
        iv_titilecolor_4 = findViewById(R.id.iv_titilecolor_4);
        iv_titilecolor_5 = findViewById(R.id.iv_titilecolor_5);
        iv_titilecolor_6 = findViewById(R.id.iv_titilecolor_6);
        iv_titilecolor_7 = findViewById(R.id.iv_titilecolor_7);
        iv_fontcolor_1 = findViewById(R.id.iv_fontcolor_1);
        iv_fontcolor_2 = findViewById(R.id.iv_fontcolor_2);
        iv_fontcolor_3 = findViewById(R.id.iv_fontcolor_3);
        iv_fontcolor_4 = findViewById(R.id.iv_fontcolor_4);
        iv_fontcolor_5 = findViewById(R.id.iv_fontcolor_5);
        iv_fontcolor_6 = findViewById(R.id.iv_fontcolor_6);
        iv_fontcolor_7 = findViewById(R.id.iv_fontcolor_7);
        sh_watermark_switch       = findViewById(R.id.sh_watermark_switch);
        sh_voice_switch       = findViewById(R.id.sh_voice_switch);
        sh_abtain_switch       = findViewById(R.id.sh_abtain_switch);
        sh_watermark_switch.setChecked(b_watermark_switch);
        sh_watermark_weather      = findViewById(R.id.sh_watermark_weather);
        sh_titileShow_switch = findViewById(R.id.sh_titileShow_switch);
        sh_watermark_weather.setChecked(b_weather_switch);
        sh_watermark_longitude    = findViewById(R.id.sh_watermark_longitude);
        sh_content    = findViewById(R.id.sh_content);
        sh_watermark_longitude.setChecked(b_longitude_switch);
        //sh_watermark_altitude     = findViewById(R.id.sh_watermark_altitude);
        sh_watermark_add          = findViewById(R.id.sh_watermark_add);
        sh_watermark_add.setChecked(b_add_switch);
        sh_watermark_neighborhood = findViewById(R.id.sh_watermark_neighborhood);
        sh_watermark_projectname  = findViewById(R.id.sh_watermark_projectname);
        sh_watermark_projectname.setChecked(b_projectname_switch);
        sh_watermark_projectadd   = findViewById(R.id.sh_watermark_projectadd);
        sh_watermark_projectadd.setChecked(b_place_switch);
        sh_watermark_projecttime  = findViewById(R.id.sh_watermark_projecttime);
        sh_watermark_projecttime.setChecked(b_time_switch);
        sh_watermark_custom       = findViewById(R.id.sh_watermark_custom);
        sh_watermark_custom.setChecked(b_custom_switch);

        ll_titlecolor_1 = findViewById(R.id.ll_titlecolor_1);
        ll_titlecolor_2 = findViewById(R.id.ll_titlecolor_2);
        ll_titlecolor_3 = findViewById(R.id.ll_titlecolor_3);
        ll_titlecolor_4 = findViewById(R.id.ll_titlecolor_4);
        ll_titlecolor_5 = findViewById(R.id.ll_titlecolor_5);
        ll_titlecolor_6 = findViewById(R.id.ll_titlecolor_6);
        ll_titlecolor_7 = findViewById(R.id.ll_titlecolor_7);
        ll_fontcolor_1 = findViewById(R.id.ll_fontcolor_1);
        ll_fontcolor_2 = findViewById(R.id.ll_fontcolor_2);
        ll_fontcolor_3 = findViewById(R.id.ll_fontcolor_3);
        ll_fontcolor_4 = findViewById(R.id.ll_fontcolor_4);
        ll_fontcolor_5 = findViewById(R.id.ll_fontcolor_5);
        ll_fontcolor_6 = findViewById(R.id.ll_fontcolor_6);
        ll_fontcolor_7 = findViewById(R.id.ll_fontcolor_7);
        if (b_voice_switch){
            sh_voice_switch.setChecked(true);
        }else {
            sh_voice_switch.setChecked(false);
        }
        if (b_abtain_switch){
            sh_abtain_switch.setChecked(true);
        }else {
            sh_abtain_switch.setChecked(false);
        }
        if (b_place_switch){
            sh_watermark_projectadd.setChecked(true);
        }else {
            sh_abtain_switch.setChecked(false);
        }
        if (b_projectname_switch){
            sh_watermark_projectname.setChecked(true);
        }else {
            sh_watermark_projectname.setChecked(false);
        }
        if (b_add_switch){
            sh_watermark_add.setChecked(true);
        }else {
            sh_watermark_add.setChecked(false);
        }
        if (b_content){
            sh_content.setChecked(true);
        }else {
            sh_content.setChecked(false);
        }
        if (b_time_switch){
            sh_watermark_projecttime.setChecked(true);
        }else{
            sh_watermark_projecttime.setChecked(false);
        }
        if (b_longitude_switch){
            sh_watermark_longitude.setChecked(true);
        }else {
            sh_watermark_longitude.setChecked(false);
        }
        if (b_weather_switch){
            sh_watermark_weather.setChecked(true);
        }else {
            sh_watermark_weather.setChecked(false);
        }
        if (b_titileShow_switch){
            sh_titileShow_switch.setChecked(true);
        }else {
            sh_titileShow_switch.setChecked(false);
        }
        switch (background_color){
            case -1:
            case 0:
                ll_titlecolor_1.setBackgroundResource(R.drawable.im_frame);
                break;
            case 1:
                ll_titlecolor_2.setBackgroundResource(R.drawable.im_frame);
                break;
            case 2:
                ll_titlecolor_3.setBackgroundResource(R.drawable.im_frame);
                break;
            case 3:
                ll_titlecolor_4.setBackgroundResource(R.drawable.im_frame);
                break;
            case 4:
                ll_titlecolor_5.setBackgroundResource(R.drawable.im_frame);
                break;
            case 5:
                ll_titlecolor_6.setBackgroundResource(R.drawable.im_frame);
                break;
            case 6:
                ll_titlecolor_7.setBackgroundResource(R.drawable.im_frame);
                break;
        }

        switch (front_color_flag){
            case -1:
            case 0:
                ll_fontcolor_1.setBackgroundResource(R.drawable.im_frame);
                break;
            case 1:
                ll_fontcolor_2.setBackgroundResource(R.drawable.im_frame);
                break;
            case 2:
                ll_fontcolor_3.setBackgroundResource(R.drawable.im_frame);
                break;
            case 3:
                ll_fontcolor_4.setBackgroundResource(R.drawable.im_frame);
                break;
            case 4:
                ll_fontcolor_5.setBackgroundResource(R.drawable.im_frame);
                break;
            case 5:
                ll_fontcolor_6.setBackgroundResource(R.drawable.im_frame);
                break;
            case 6:
                ll_fontcolor_7.setBackgroundResource(R.drawable.im_frame);
                break;
        }

        sh_voice_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                b_voice_switch = b;
            }
        });
        sh_abtain_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                b_abtain_switch = b;
            }
        });
        sh_watermark_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 b_watermark_switch = b;
             }
         });
        sh_watermark_weather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                  b_weather_switch = b;
              }
        });
        sh_watermark_longitude.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        b_longitude_switch = b;
                    }
                });
        sh_watermark_add.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        b_add_switch = b;
                    }
                });
        sh_watermark_projectname.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        b_projectname_switch = b;
                    }
                });
        sh_watermark_projectadd.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        b_place_switch = b;

                    }
                });
        sh_watermark_projecttime.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        b_time_switch = b;
                    }
                });
        sh_watermark_custom.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        b_custom_switch = b;
                    }
                });
        sh_titileShow_switch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        b_titileShow_switch = b;
                    }
                });
        sh_content.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                b_content = b;

            }
        });
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回键
        FontsizeRaeSeekBar frontsize_SeekBar = findViewById(R.id.seekBar_fontsize);
        switch (front_size_flag){
            case 0:
                frontsize_SeekBar.setProgress(0);
                break;
            case 1:
                frontsize_SeekBar.setProgress(1);
                break;
            case 2:
                frontsize_SeekBar.setProgress(2);
                break;
            case 3:
                frontsize_SeekBar.setProgress(3);
                break;
        }
        frontsize_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                front_size_flag = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        BackgoundcolorSeekBar backgroundcolor_SeekBar = findViewById(R.id.seekBar_backgroundcolor);
        switch (background_color_depth_flag){
            case 0:
                backgroundcolor_SeekBar.setProgress(0);
                break;
            case 1:
                backgroundcolor_SeekBar.setProgress(1);
                break;
            case 2:
                backgroundcolor_SeekBar.setProgress(2);
                break;
            case 3:
                backgroundcolor_SeekBar.setProgress(3);
                break;
        }
        backgroundcolor_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                background_color_depth_flag = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        findViewById(R.id.txt_left_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.txt_right_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //把返回数据存入Intent
                String name = et_projectName.getText().toString();
                String add = et_projectAdd.getText().toString();
                String time = et_projectTime.getText().toString();
                intent.putExtra("background_color",background_color);
                intent.putExtra("front_color",front_color);
                intent.putExtra("name", name);
                intent.putExtra("add", add);
                intent.putExtra("time", time);
                intent.putExtra("b_watermark_switch",b_watermark_switch);
                intent.putExtra("b_weather_switch",b_weather_switch);
                intent.putExtra("b_longitude_switch",b_longitude_switch);
                intent.putExtra("b_add_switch",b_add_switch);
                intent.putExtra("b_projectname_switch",b_projectname_switch);
                intent.putExtra("b_place_switch",b_place_switch);
                intent.putExtra("b_time_switch",b_time_switch);
                intent.putExtra("b_custom_switch",b_custom_switch);
                intent.putExtra("front_size",front_size_flag);
                intent.putExtra("background_color_depth",background_color_depth_flag);
                intent.putExtra("sh_voice_switch",b_voice_switch);
                intent.putExtra("b_abtain_switch",b_abtain_switch);
                intent.putExtra("b_titileShow_switch",b_titileShow_switch);
                intent.putExtra("b_content",b_content);
                intent.putExtra("et_abtainCompany",et_abtainCompany.getText().toString().trim());
                intent.putExtra("et_titileShow",et_titileShow.getText().toString().trim());
                intent.putExtra("et_location",et_location.getText().toString().trim());
                intent.putExtra("et_content",et_content.getText().toString().trim());
                intent.putExtra("et_longitude_latitude",et_longitude_latitude.getText().toString().trim());
                intent.putExtra("et_weather",et_weather.getText().toString().trim());
                //设置返回数据
                SetUpActivity.this.setResult(RESULT_OK, intent);
                mSpEdit.putBoolean("sh_content",b_content);
                mSpEdit.putBoolean("sh_titileShow_switch",b_titileShow_switch);
                mSpEdit.putBoolean("sh_watermark_custom",b_custom_switch);
                mSpEdit.putBoolean("sh_watermark_projecttime",b_time_switch);
                mSpEdit.putBoolean("sh_watermark_projectadd",b_place_switch);
                mSpEdit.putBoolean("sh_watermark_projectname",b_projectname_switch);
                mSpEdit.putBoolean("sh_watermark_add",b_add_switch);
                mSpEdit.putBoolean("sh_watermark_longitude",b_longitude_switch);
                mSpEdit.putBoolean("sh_watermark_switch",b_watermark_switch);
                mSpEdit.putBoolean("sh_watermark_weather",b_weather_switch);
                mSpEdit.putBoolean("sh_abtain_switch",b_abtain_switch);
                mSpEdit.putBoolean("sh_voice_switch",b_voice_switch);
                mSpEdit.putInt("background_color",background_color);
                mSpEdit.putInt("background_color",background_color);
                mSpEdit.putInt("front_color_flag",front_color_flag);
                mSpEdit.putInt("background_color_depth_flag",background_color_depth_flag);//底色深度值
                mSpEdit.putInt("front_size_flag",front_size_flag);//字体大小
                mSpEdit.putInt("front_color",front_color);//字体大小
                /*保存编辑信息*/
                mSpEdit.putString("et_abtainCompany",et_abtainCompany.getText().toString().trim());
                mSpEdit.putString("et_projectAdd",et_projectAdd.getText().toString().trim());
                mSpEdit.putString("et_titileShow",et_titileShow.getText().toString().trim());
                mSpEdit.putString("et_projectName",et_projectName.getText().toString().trim());
                //mSpEdit.putString("et_location",et_location.getText().toString().trim());
                mSpEdit.putString("et_content",et_content.getText().toString().trim());
                mSpEdit.putString("et_time",time);
                mSpEdit.putString("et_weather",et_weather.getText().toString().trim());
                mSpEdit.commit();
                //关闭Activity
                SetUpActivity.this.finish();
            }
        });



        ll_titlecolor_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                background_color = 0;
            }
        });
        ll_titlecolor_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_2.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                background_color = 1;
            }
        });
        ll_titlecolor_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_3.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                background_color = 2;
            }
        });
        ll_titlecolor_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_4.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                background_color = 3;
            }
        });
        ll_titlecolor_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_5.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                background_color = 4;
            }
        });
        ll_titlecolor_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_6.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                background_color = 5;
            }
        });
        ll_titlecolor_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_titlecolor_7.setBackgroundResource(R.drawable.im_frame);
                background_color = 6;
            }
        });

        ll_fontcolor_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                Drawable background = iv_fontcolor_1.getBackground();
                if (background instanceof ColorDrawable) {
                     front_color = ((ColorDrawable) background).getColor();
                }
                front_color_flag = 0;
            }
        });
        ll_fontcolor_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_2.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                Drawable background = iv_fontcolor_2.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                front_color_flag = 1;
            }
        });
        ll_fontcolor_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_3.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                Drawable background = iv_fontcolor_3.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                front_color_flag = 2;
            }
        });
        ll_fontcolor_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_4.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                Drawable background = iv_fontcolor_4.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                iv_fontcolor_4.setBackgroundColor(front_color);
                front_color_flag = 3;
            }
        });
        ll_fontcolor_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_5.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                Drawable background = iv_fontcolor_5.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                iv_fontcolor_5.setBackgroundColor(front_color);
                front_color_flag = 4;
            }
        });
        ll_fontcolor_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_6.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_7.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                Drawable background = iv_fontcolor_6.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                iv_fontcolor_6.setBackgroundColor(front_color);
                front_color_flag = 5;
            }
        });
        ll_fontcolor_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_2.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_3.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_4.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_5.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_6.setBackgroundColor(getResources().getColor(R.color.them_color_backgroundgreen));
                ll_fontcolor_7.setBackgroundResource(R.drawable.im_frame);
                Drawable background = iv_fontcolor_7.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                iv_fontcolor_7.setBackgroundColor(front_color);
                front_color_flag = 6;
            }
        });

        //显示当前时间
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String time= sdf.format( new Date());
//        et_projectTime.setText(""+time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
           onBackPressed();
           //Toast.makeText(SetUpActivity.this,"ssss",Toast.LENGTH_LONG).show();
           return true;
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
