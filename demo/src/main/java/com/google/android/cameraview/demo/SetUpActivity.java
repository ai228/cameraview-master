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

package com.google.android.cameraview.demo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SetUpActivity extends AppCompatActivity {
    private static final String TAG = "SetUpActivity";
    EditText et_projectName;
    EditText et_projectAdd;
    EditText et_projectTime;
    Button bt_save;
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
    private boolean b_watermark_switch = true;
    private boolean b_weather_switch = true;
    private boolean b_longitude_switch = true;
    //private boolean b_altude_switch;//海拔开关
    private boolean b_add_switch = true;
    private boolean b_projectname_switch = true;
    private boolean b_place_switch;
    private boolean b_time_switch = true;
    private boolean b_custom_switch = false;
    private int background_color;
    private int front_color = -1;
    private int front_size_flag=1;
    private int background_color_depth_flag=1;
    Switch sh_watermark_switch;
    Switch sh_watermark_weather;
    Switch sh_watermark_longitude;
    //Switch sh_watermark_altitude;//海拔开关
    Switch sh_watermark_add;
    Switch sh_watermark_neighborhood;
    Switch sh_watermark_projectname;
    Switch sh_watermark_projectadd;
    Switch sh_watermark_projecttime;
    Switch sh_watermark_custom;

    ImageView iv_titilecolor_1;ImageView iv_fontcolor_1;
    ImageView iv_titilecolor_2;ImageView iv_fontcolor_2;
    ImageView iv_titilecolor_3;ImageView iv_fontcolor_3;
    ImageView iv_titilecolor_4;ImageView iv_fontcolor_4;
    ImageView iv_titilecolor_5;ImageView iv_fontcolor_5;
    ImageView iv_titilecolor_6;ImageView iv_fontcolor_6;
    ImageView iv_titilecolor_7;ImageView iv_fontcolor_7;

    String str_projectname;
    String str_place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        Intent intent1 = getIntent();
        str_projectname = intent1.getStringExtra("str_projectname");
        str_place = intent1.getStringExtra("str_place");
        Toolbar toolbar = findViewById(R.id.toolbar);
        et_projectName = findViewById(R.id.et_projectName);
        et_projectName.setText(str_projectname);
        et_projectAdd = findViewById(R.id.et_projectAdd);
        et_projectAdd.setText(str_place);
        et_projectTime = findViewById(R.id.et_projectTime);
        bt_save = findViewById(R.id.save);
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
        sh_watermark_weather      = findViewById(R.id.sh_watermark_weather);
        sh_watermark_longitude    = findViewById(R.id.sh_watermark_longitude);
        //sh_watermark_altitude     = findViewById(R.id.sh_watermark_altitude);
        sh_watermark_add          = findViewById(R.id.sh_watermark_add);
        sh_watermark_neighborhood = findViewById(R.id.sh_watermark_neighborhood);
        sh_watermark_projectname  = findViewById(R.id.sh_watermark_projectname);
        sh_watermark_projectadd   = findViewById(R.id.sh_watermark_projectadd);
        sh_watermark_projecttime  = findViewById(R.id.sh_watermark_projecttime);
        sh_watermark_custom       = findViewById(R.id.sh_watermark_custom);
        sh_watermark_switch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        b_watermark_switch = b;
                    }
                });
        sh_watermark_weather.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
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
       /* sh_watermark_altitude.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        b_altude_switch = b;
                    }
                });*/
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


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回键
        FontsizeRaeSeekBar frontsize_SeekBar = findViewById(R.id.seekBar_fontsize);
        BackgoundcolorSeekBar backgroundcolor_SeekBar = findViewById(R.id.seekBar_backgroundcolor);
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
        bt_save.setOnClickListener(new View.OnClickListener() {
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
                //设置返回数据
                SetUpActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                SetUpActivity.this.finish();
            }
        });


        ll_titlecolor_1 = findViewById(R.id.ll_titlecolor_1);
        ll_titlecolor_2 = findViewById(R.id.ll_titlecolor_2);
        ll_titlecolor_3 = findViewById(R.id.ll_titlecolor_3);
        ll_titlecolor_4 = findViewById(R.id.ll_titlecolor_4);
        ll_titlecolor_5 = findViewById(R.id.ll_titlecolor_5);
        ll_titlecolor_6 = findViewById(R.id.ll_titlecolor_6);
        ll_titlecolor_7 = findViewById(R.id.ll_titlecolor_7);
        ll_titlecolor_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_7.setBackgroundResource(R.drawable.im_frame_white);

                background_color = 0;
            }
        });
        ll_titlecolor_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_2.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_7.setBackgroundResource(R.drawable.im_frame_white);
                background_color = 1;
            }
        });
        ll_titlecolor_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_3.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_7.setBackgroundResource(R.drawable.im_frame_white);
                background_color = 2;
            }
        });
        ll_titlecolor_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_4.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_7.setBackgroundResource(R.drawable.im_frame_white);
                background_color = 3;
            }
        });
        ll_titlecolor_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_5.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_7.setBackgroundResource(R.drawable.im_frame_white);
                background_color = 4;
            }
        });
        ll_titlecolor_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_6.setBackgroundResource(R.drawable.im_frame);
                ll_titlecolor_7.setBackgroundResource(R.drawable.im_frame_white);
                background_color = 5;
            }
        });
        ll_titlecolor_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_titlecolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_titlecolor_7.setBackgroundResource(R.drawable.im_frame);
                background_color = 6;
            }
        });

        ll_fontcolor_1 = findViewById(R.id.ll_fontcolor_1);
        ll_fontcolor_2 = findViewById(R.id.ll_fontcolor_2);
        ll_fontcolor_3 = findViewById(R.id.ll_fontcolor_3);
        ll_fontcolor_4 = findViewById(R.id.ll_fontcolor_4);
        ll_fontcolor_5 = findViewById(R.id.ll_fontcolor_5);
        ll_fontcolor_6 = findViewById(R.id.ll_fontcolor_6);
        ll_fontcolor_7 = findViewById(R.id.ll_fontcolor_7);
        ll_fontcolor_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_7.setBackgroundResource(R.drawable.im_frame_white);
                Drawable background = iv_fontcolor_1.getBackground();
                if (background instanceof ColorDrawable) {
                     front_color = ((ColorDrawable) background).getColor();
                }
            }
        });
        ll_fontcolor_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_2.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_7.setBackgroundResource(R.drawable.im_frame_white);
                Drawable background = iv_fontcolor_2.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
            }
        });
        ll_fontcolor_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_3.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_7.setBackgroundResource(R.drawable.im_frame_white);
                Drawable background = iv_fontcolor_3.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
            }
        });
        ll_fontcolor_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_4.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_7.setBackgroundResource(R.drawable.im_frame_white);
                Drawable background = iv_fontcolor_4.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                iv_fontcolor_4.setBackgroundColor(front_color);
            }
        });
        ll_fontcolor_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_5.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_7.setBackgroundResource(R.drawable.im_frame_white);
                Drawable background = iv_fontcolor_5.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                iv_fontcolor_5.setBackgroundColor(front_color);
            }
        });
        ll_fontcolor_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_6.setBackgroundResource(R.drawable.im_frame);
                ll_fontcolor_7.setBackgroundResource(R.drawable.im_frame_white);
                Drawable background = iv_fontcolor_6.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                iv_fontcolor_6.setBackgroundColor(front_color);
            }
        });
        ll_fontcolor_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_fontcolor_1.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_2.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_3.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_4.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_5.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_6.setBackgroundResource(R.drawable.im_frame_white);
                ll_fontcolor_7.setBackgroundResource(R.drawable.im_frame);
                Drawable background = iv_fontcolor_7.getBackground();
                if (background instanceof ColorDrawable) {
                    front_color = ((ColorDrawable) background).getColor();
                }
                iv_fontcolor_7.setBackgroundColor(front_color);
            }
        });

        //显示当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time= sdf.format( new Date());
        et_projectTime.setText(""+time);
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


}
