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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.ui.fragment.AspectRatioFragment;
import com.google.android.cameraview.demo.util.ImageUtil;
import com.google.android.cameraview.demo.util.urlhttp.CallBackUtil;
import com.google.android.cameraview.demo.util.urlhttp.UrlHttpUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        AspectRatioFragment.Listener {
    private static final int COMPLETED = 0;
    public static boolean is_16_9 = true;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                tv_projectAdd.setText(str_location);
            }
        }
    };

    private static final String TAG = "MainActivity";
    private static final String HTTP_PRE = "http://wthrcdn.etouch.cn/weather_mini?city=";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final String FRAGMENT_DIALOG = "dialog";

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private int mCurrentFlash;

    private CameraView mCameraView;

    private Handler mBackgroundHandler;
    //变量参数
    private boolean b_watermark_switch;
    private boolean b_weather_switch;
    private boolean b_longitude_switch;
    private boolean b_add_switch;
    private boolean b_projectname_switch;
    private boolean b_place_switch;
    private boolean b_time_switch;
    private boolean b_custom_switch;
    private boolean b_voice_switch;
    private boolean b_abtain_switch;
    private boolean b_titileShow_switch;
    private boolean b_content;
    private int background_color_depth_flag = 1;
    private int background_color =0;
//    private int front_color_flag = -1;
    private int front_color = -1;
    private int front_size_flag = 0;
    private int  front_size = 1;

//    private String str_weather = "天气：";
//    private String str_longitude = "经度：";
//    private String str_latitude = "纬度：";
//    private String str_add = "";
//    private String str_projectname = "工程名称：";
//    private String str_place = "施工地点：";
//    private String str_time = "时间：";
    private String str_weather = "";
    private String str_longitude = "";
    private String str_latitude = "";
    private String str_add = "";
    private String str_projectname = "(待填)";
    private String str_place = "(待填)";
    private String str_time = "";
    private String str_abtain = "str_取证单位";
    private String str_titileShow = "str_标题名称";
    private String str_location = "";
    private String str_content = "str_作业内容";
    private String str_longitude_latitude = "str_经纬度数";
    LinearLayout ll_titile_background;
    LinearLayout ll_add;
    LinearLayout ll_project_name;
    LinearLayout ll_place;
    LinearLayout ll_weather;
    LinearLayout ll_abtain;
    LinearLayout ll_logitude;
    LinearLayout ll_time;
    LinearLayout ll_content;
    TextView project_weather;
    TextView project_logitude_latitude;
    TextView tv_fixed_add;
    TextView jtv_weather;
    TextView jtv_logitude;
    TextView jtv_time;
    TextView tv_content;
    TextView jtv_content;
    TextView jtv_projectName;
    TextView jtv_abtain;
    TextView jtv_place;
    TextView tv_custom;
    TextView tv_titile;
    TextView tv_abtain;

    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID

    float paint_size ;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        if (b_voice_switch)
                            sp.play(music, 1, 1, 0, 0, 1);
                        mToast = Toast.makeText(MainActivity.this,"图片保存中...",Toast.LENGTH_LONG);
                        mToast.show();
                        mCameraView.takePicture();
                        /*if (is_16_9){
                            mCameraView.setAspectRatio(AspectRatio.parse("16:9"));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mCameraView.takePicture();

                                }
                            },500 );
                        }else{
                            mCameraView.takePicture();
                        }*/
                    }
                    break;
            }
        }
    };
    private ImageView mIm_setup;
    ImageView imageView;
    TextView tv_projectName;
    TextView tv_projectAdd;//地址信息内容
    TextView project_place;//施工单位内容
    TextView project_time;
    TextView tv_project_add;

    double lat;
    double lng;

    public static final int LOCATION_CODE = 301;
    private LocationManager locationManager;
    private String locationProvider = null;
    private String mLocality = "娄底";
    private Toast mToast;
    private List<String> list_keyword;
    private static int LIGHT_FLAG = 0;//0：自动；1：关闭；2：打开
    ImageView iv_light;
    private SharedPreferences mSharedPreferences;
    private LinearLayout mLl_takened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager wm = this.getWindowManager();
        double width = wm.getDefaultDisplay().getWidth();
        double height = wm.getDefaultDisplay().getHeight();
        double raio = height / width;
        Toast.makeText(this,Double.toString(raio),Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //Density.setDensity(getApplication(),this);
        setContentView(R.layout.activity_main);
        mCameraView = findViewById(R.id.camera);

        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        ImageView fab = findViewById(R.id.take_picture);
        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //imageView = findViewById(R.id.imview);
        TextView etName = findViewById(R.id.my_name);
        mIm_setup = findViewById(R.id.iv_setup);

        project_place = findViewById(R.id.project_place);
        project_time = findViewById(R.id.project_time);
        tv_project_add = findViewById(R.id.project_add);
        ll_titile_background = findViewById(R.id.ll_titile_background);
        ll_add = findViewById(R.id.ll_add);
        ll_project_name = findViewById(R.id.ll_project_name);
        ll_place = findViewById(R.id.ll_place);
        ll_weather = findViewById(R.id.ll_weather);
        ll_abtain = findViewById(R.id.ll_abtain);
        ll_logitude = findViewById(R.id.ll_logitude);
        ll_content = findViewById(R.id.ll_content);
        ll_time = findViewById(R.id.ll_time);
        //ll_titile_background.setVisibility(View.GONE);
        project_weather = findViewById(R.id.project_weather);
        project_logitude_latitude = findViewById(R.id.project_logitude_latitude);
        tv_titile = findViewById(R.id.tv_titile);
        tv_fixed_add = findViewById(R.id.tv_fixed_add);
        jtv_weather = findViewById(R.id.jtv_weather);
        jtv_logitude = findViewById(R.id.jtv_logitude);
        jtv_time = findViewById(R.id.jtv_time);
        jtv_content = findViewById(R.id.jtv_content);
        jtv_projectName = findViewById(R.id.jtv_projectName);
        jtv_abtain = findViewById(R.id.jtv_abtain);
        jtv_place = findViewById(R.id.jtv_place);
        tv_abtain = findViewById(R.id.tv_abtain);
        tv_custom = findViewById(R.id.tv_custom);
        tv_projectAdd = findViewById(R.id.project_add);
        tv_projectName = findViewById(R.id.project_name);
        tv_content = findViewById(R.id.tv_content);
        tv_projectName.setText(str_projectname);
        project_place = findViewById(R.id.project_place);
        project_place.setText(str_place);
        etName.setFocusableInTouchMode(false);
        //setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time= sdf.format( new Date());
        str_time = ""+time;
        project_time.setText(str_time); //更新时间
        mIm_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setUpActivity = new Intent(MainActivity.this, SetUpActivity.class);
                setUpActivity.putExtra("str_projectname",str_projectname);
                setUpActivity.putExtra("str_place",str_place);
                setUpActivity.putExtra("str_weather",str_weather);
                setUpActivity.putExtra("sh_voice_switch",b_voice_switch);
                setUpActivity.putExtra("str_titileShow",str_titileShow);
                setUpActivity.putExtra("str_abtainCompany",str_abtain);
                setUpActivity.putExtra("str_add",str_add);
                setUpActivity.putExtra("str_location",str_location);
                setUpActivity.putExtra("str_content",str_content);
                setUpActivity.putExtra("str_time",str_time);
                setUpActivity.putExtra("str_longitude_latitude",str_longitude_latitude);
                startActivityForResult(setUpActivity, 0);
            }
        });

        //经纬度和地址显示
        getLocation();
        sp = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.takend, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        list_keyword =  new ArrayList<String>();
   /*    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AspectRatio t_ratio = AspectRatio.parse("4:3");
                mCameraView.setAspectRatio(t_ratio);
                onStart();
                onResume();
            }
        },500 );*/
        iv_light = findViewById(R.id.switch_flash);
        iv_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    if (mCameraView != null) {
                        switch (LIGHT_FLAG){
                            case 0://当闪关灯自动状态
                                //设置为关闭
                                LIGHT_FLAG = 1;
                                mCameraView.setFlash(FLASH_OPTIONS[LIGHT_FLAG]);
                                iv_light.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.icon_light_close));
                                break;
                            case 1://当闪光灯关闭状态
                                //设置为打开
                                LIGHT_FLAG = 2;
                                mCameraView.setFlash(FLASH_OPTIONS[LIGHT_FLAG]);
                                iv_light.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.icon_light_open));
                                break;
                            case 2://当闪光灯打开状态
                                //设置为打开
                                LIGHT_FLAG = 0;
                                mCameraView.setFlash(FLASH_OPTIONS[LIGHT_FLAG]);
                                iv_light.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.icon_light));
                                break;
                        }
                    }
                }
            }
        });
        findViewById(R.id.switch_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
            }
        });
        //打开相册
        findViewById(R.id.iv_photos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用有返回值的相册
                /*Intent i = new Intent(Intent.ACTION_PICK,null);
                i.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivity(i);*//*
                Intent intent = new Intent(Intent.ACTION_PICK);
                context.startActivity(intent);*/
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_PICK);
//      intent.setType("image/*");
//      startActivityForResult(intent, 500);
                //直接调用打开相册
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("image/*");
                startActivity(intent);
            }
        });

        mSharedPreferences = getSharedPreferences("camera", MODE_PRIVATE);
        b_voice_switch = mSharedPreferences.getBoolean("sh_voice_switch",true);
        b_watermark_switch = mSharedPreferences.getBoolean("sh_watermark_switch",true);
        b_abtain_switch = mSharedPreferences.getBoolean("sh_abtain_switch",true);
        b_place_switch = mSharedPreferences.getBoolean("sh_watermark_projectadd",true);
        b_titileShow_switch = mSharedPreferences.getBoolean("sh_titileShow_switch",true);
        b_projectname_switch = mSharedPreferences.getBoolean("sh_watermark_projectname",true);
        b_add_switch = mSharedPreferences.getBoolean("sh_watermark_add",true);
        b_content = mSharedPreferences.getBoolean("sh_content",true);
        b_time_switch = mSharedPreferences.getBoolean("sh_watermark_projecttime",true);
        b_longitude_switch = mSharedPreferences.getBoolean("sh_watermark_longitude",true);
        b_weather_switch = mSharedPreferences.getBoolean("sh_watermark_weather",true);
        background_color_depth_flag = mSharedPreferences.getInt("background_color_depth_flag",1);
        background_color = mSharedPreferences.getInt("background_color",-1);
        //front_color_flag = mSharedPreferences.getInt("front_color_flag",-1);
        front_color = mSharedPreferences.getInt("front_color",-1);
        front_size_flag = mSharedPreferences.getInt("front_size_flag",-1);
        str_abtain = mSharedPreferences.getString("et_abtainCompany","str_取证单位");
        str_projectname = mSharedPreferences.getString("et_projectName","str_项目名称");
        //str_location = mSharedPreferences.getString("et_location","str_位置信息");
        str_content = mSharedPreferences.getString("et_content","str_作业内容");
        str_titileShow = mSharedPreferences.getString("et_titileShow","str_作业内容");
        str_place = mSharedPreferences.getString("et_projectAdd","str_施工单位");
        //str_time = mSharedPreferences.getString("et_time","");
        //str_longitude_latitude = mSharedPreferences.getString("et_longitude_latitude","");
        iniData();
        //友盟推送
        PushAgent.getInstance(this).onAppStart();
        mLl_takened = findViewById(R.id.ll_takened);
        if (raio>2.0){
            // 移动相机
            RelativeLayout.LayoutParams  layoutParams =
                    (RelativeLayout.LayoutParams) mCameraView.getLayoutParams();
            layoutParams.bottomMargin = (int) (getResources().getDimension(R.dimen.px_190))/2;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            mCameraView.setLayoutParams(layoutParams);

            // 移动拍照布局
            RelativeLayout.LayoutParams  layoutParams1 =
                    (RelativeLayout.LayoutParams) mLl_takened.getLayoutParams();
            layoutParams1.bottomMargin = (int) ((getResources().getDimension(R.dimen.px_190))/2);//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            mLl_takened.setLayoutParams(layoutParams1);
        }else if (raio>1.9){
            // 移动相机
            RelativeLayout.LayoutParams  layoutParams =
                    (RelativeLayout.LayoutParams) mCameraView.getLayoutParams();
            layoutParams.bottomMargin = (int) (getResources().getDimension(R.dimen.px_150))/2;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            mCameraView.setLayoutParams(layoutParams);
            // 移动拍照布局
            RelativeLayout.LayoutParams  layoutParams1 =
                    (RelativeLayout.LayoutParams) mLl_takened.getLayoutParams();
            layoutParams1.bottomMargin = (int) (getResources().getDimension(R.dimen.px_150))/2;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            mLl_takened.setLayoutParams(layoutParams1);
        }else if (raio>1.8){
            // 移动相机
            RelativeLayout.LayoutParams  layoutParams =
                    (RelativeLayout.LayoutParams) mCameraView.getLayoutParams();
            layoutParams.bottomMargin = (int) (getResources().getDimension(R.dimen.px_100))/2;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            mCameraView.setLayoutParams(layoutParams);
            // 移动拍照布局
            RelativeLayout.LayoutParams  layoutParams1 =
                    (RelativeLayout.LayoutParams) mLl_takened.getLayoutParams();
            layoutParams1.bottomMargin = (int) (getResources().getDimension(R.dimen.px_100))/2;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            mLl_takened.setLayoutParams(layoutParams1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aspect_ratio:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (mCameraView != null
                        && fragmentManager.findFragmentByTag(FRAGMENT_DIALOG) == null) {
                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
                    AspectRatio currentRatio = mCameraView.getAspectRatio();
                   /* if (is_16_9){
                        currentRatio = AspectRatio.parse("16:9");
                    }*/
                    AspectRatioFragment.newInstance(ratios, currentRatio)
                            .show(fragmentManager, FRAGMENT_DIALOG);
                }
                return true;
           /* case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;*/
            /*case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            mCameraView.setAdjustViewBounds(true);
            mCameraView.setAspectRatio(ratio);
            /*if ( ratio.toString().equals("16:9")){
                Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show();
                mCameraView.setAdjustViewBounds(false);
                AspectRatio t_ratio = AspectRatio.parse("4:3");
                mCameraView.setAspectRatio(t_ratio);
                is_16_9 = true;
            }else {
                mCameraView.setAdjustViewBounds(true);
                mCameraView.setAspectRatio(ratio);
                is_16_9 = false;
            }*/
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    float canvasTextSize1 = 42;
    float canvasTextSize2 = 44;
    float canvasTextSize3 = 46;
    float canvasTextSize4 = 48;

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
//            if (is_16_9){
//                mCameraView.setAdjustViewBounds(false);
//                AspectRatio t_ratio = AspectRatio.parse("4:3");
//                mCameraView.setAspectRatio(t_ratio);
//            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            float bitmapWidth = bitmap.getWidth();
            float bitmapHeight = bitmap.getHeight();
            float ratio = getPxRatio(bitmapWidth,bitmapHeight);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(front_color);
            switch (front_size){
                case 0:
                    paint_size = canvasTextSize1*ratio;
                    break;
                case 1:
                    paint_size = canvasTextSize2*ratio;
                    break;
                case 2:
                    paint_size = canvasTextSize3*ratio;
                    break;
                case 3:
                    paint_size = canvasTextSize4*ratio;
                    break;
            }
            paint.setTextSize(paint_size);
            list_keyword.clear();
            if (b_place_switch)
                list_keyword.add("施工单位："+str_place);
            if (b_abtain_switch)
                list_keyword.add("取证单位："+str_abtain);
            if (b_projectname_switch)
                list_keyword.add("项目名称："+str_projectname);
            if (b_add_switch)
                list_keyword.add("^_^"+str_location);
            if (b_content){
                list_keyword.add("作业内容："+str_content);
            }
            if (b_time_switch)
                list_keyword.add("当前日期："+str_time);
            if (b_longitude_switch){
                list_keyword.add("经纬度数："+str_longitude_latitude);
            }
            if (b_weather_switch)
                list_keyword.add("天气状况："+str_weather);
//            if (b_titileShow_switch)
//                list_keyword.add("^^^_^^^："+str_titileShow);
            if (!b_watermark_switch)
                list_keyword.clear();
            float paddingBottom = 100 * getPxRatio(bitmap.getWidth(), bitmap.getHeight());
            Bitmap toLeftBottom1 = ImageUtil.drawTextToLeftBottom(MainActivity.this, bitmap,
                    list_keyword,b_titileShow_switch,str_titileShow,
                    paint,40*getPxRatio(bitmap.getWidth(),bitmap.getHeight()),paddingBottom,background_color_depth_flag,background_color);
            //imageView.setImageBitmap(toLeftBottom1);
//            saveImage(toLeftBottom1);
//            saveImageToGallery(toLeftBottom1);
            saveImageToGallery_test(toLeftBottom1);
        }
    };

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }
    }

    public void saveImageToGallery(Bitmap bmp) {
        String[] PERMISSIONS = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };
        //检测是否有写的权限
        int permission = ContextCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有写的权限，去申请写的权限，会弹出对话框
            ActivityCompat.requestPermissions(this, PERMISSIONS,1);
        }

            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        /*
         * 保存文件，文件名为当前日期
         */
            String fileName ;
            File file ;
            if(Build.BRAND .equals("Xiaomi") ){ // 小米手机  ----> 电企通相机改为了“DCIM”
                fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+format.format(new Date())+".JPEG" ;
            }else{ // Meizu 、Oppo
                fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+format.format(new Date())+".JPEG" ;
            }
            file = new File(fileName);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream out;
            try{
                out = new FileOutputStream(file);
                // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
                if(bmp.compress(Bitmap.CompressFormat.JPEG, 90, out))
                {
                    out.flush();
                    out.close();
                    // 插入图库
                    MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), format.format(new Date())+".JPEG", null);
                }
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            // 发送广播，通知刷新图库的显示
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        //通知系统相册刷新
        MainActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(file.getPath()))));

    }

    public  void saveImage(Bitmap bmp) {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
        File appDir = new File(Environment.getExternalStorageDirectory(), "电企通相机");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            saveImageToGallery(bmp);
        }
        // 发送广播，通知刷新图库的显示
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        //通知系统相册刷新
        MainActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(file.getPath()))));
    }

    public void saveImageToGallery_test(final Bitmap bmp) {
        //生成路径
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String root = getFilesDir().getAbsolutePath();
        String dirName = "电企通相机";
        File appDir = new File(root, dirName);
        if (!appDir.exists()) {
            boolean mkdirs = appDir.mkdirs();
        }

        //文件名为时间
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        String sd = sdf.format(new Date(timeStamp));
        String fileName = sd + ".jpg";

        //获取文件
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            final FileOutputStream finalFos = fos;
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, finalFos);
            mToast.makeText(MainActivity.this,"已保存",Toast.LENGTH_SHORT).show();
            fos.flush();
            //通知系统相册刷新
            MainActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));

        } catch (FileNotFoundException e) {
            saveImage(bmp);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            background_color = data.getExtras().getInt("background_color");
            front_color = data.getExtras().getInt("front_color");
            str_projectname = "" + data.getExtras().getString("name");
            str_place = "" + data.getExtras().getString("add");
            str_time = "" + data.getExtras().getString("time");
            str_abtain = "" + data.getExtras().getString("et_abtainCompany");
            front_size = data.getExtras().getInt("front_size");
            b_watermark_switch = data.getExtras().getBoolean("b_watermark_switch");  
            b_weather_switch = data.getExtras().getBoolean("b_weather_switch");  
            b_longitude_switch = data.getExtras().getBoolean("b_longitude_switch");  
            b_add_switch = data.getExtras().getBoolean("b_add_switch");  
            b_projectname_switch = data.getExtras().getBoolean("b_projectname_switch");  
            b_place_switch = data.getExtras().getBoolean("b_place_switch");  
            b_time_switch = data.getExtras().getBoolean("b_time_switch");  
            b_custom_switch = data.getExtras().getBoolean("b_custom_switch");
            background_color_depth_flag = data.getExtras().getInt("background_color_depth");
            b_voice_switch = data.getExtras().getBoolean("sh_voice_switch");
            b_abtain_switch = data.getExtras().getBoolean("b_abtain_switch");
            b_titileShow_switch = data.getExtras().getBoolean("b_titileShow_switch");
            str_titileShow = data.getExtras().getString("et_titileShow");
            str_content = data.getExtras().getString("et_content");
            str_weather = data.getExtras().getString("et_weather");
            str_location = data.getExtras().getString("et_location");
            b_content = data.getExtras().getBoolean("b_content");

            if (!str_projectname.isEmpty()) {
                tv_projectName.setText(str_projectname);
            }
            if (!str_add.isEmpty()) {
                project_place.setText(str_place);
            }
            if (!str_time.isEmpty()) {
                project_time.setText(str_time);
            }
            if (background_color != -1) {
                setTitleBackgroundColor();
            }
            if (front_color != 0) {
                setTitleColor();
            }
            if (front_size != -1) {
                setFrontSize();
            }
            iniData();
        }
    }

    private void iniData() {
        //编辑内容
        tv_abtain.setText(str_abtain);
        tv_titile.setText(str_titileShow);
        project_place.setText(str_place);
        tv_projectName.setText(str_projectname);
        tv_project_add.setText(str_location);
        tv_content.setText(str_content);
        project_time.setText(str_time);
        project_logitude_latitude.setText(str_longitude_latitude);
        project_weather.setText(str_weather);
        //设置颜色深度 //设置背景色
        setTitleBackgroundColor();
        //设置字体颜色
        setTitleColor();
        //设置字体大小
        setFrontSize();
        //水印开关
        if (b_watermark_switch){
            ll_titile_background.setVisibility(View.VISIBLE);
        }else {
            ll_titile_background.setVisibility(View.INVISIBLE);
        }
        //标题是否显示
        if (b_titileShow_switch){
            tv_titile.setVisibility(View.VISIBLE);
        }else{
            tv_titile.setVisibility(View.GONE);
        }
        //施工单位开关
        if (b_place_switch){
            ll_place.setVisibility(View.VISIBLE);
        }else {
            ll_place.setVisibility(View.GONE);
        }
        //取证单位
        if (b_abtain_switch){
            ll_abtain.setVisibility(View.VISIBLE);
        }else{
            ll_abtain.setVisibility(View.GONE);
        }
        //项目名称开关
        if (b_projectname_switch){
            ll_project_name.setVisibility(View.VISIBLE);
        }else {
            ll_project_name.setVisibility(View.GONE);
        }
        //位置信息开关
        if (b_add_switch){
            ll_add.setVisibility(View.VISIBLE);
        }else {
            ll_add.setVisibility(View.GONE);
        }
        //作业内容开关
        if(b_content){
            ll_content.setVisibility(View.VISIBLE);
        }else{
            ll_content.setVisibility(View.GONE);
        }
        //当前日期开关
        if (b_time_switch){
            ll_time.setVisibility(View.VISIBLE);
        }else {
            ll_time.setVisibility(View.GONE);
        }
        //经纬度数开关
        if (b_longitude_switch){
            ll_logitude.setVisibility(View.VISIBLE);
        }else {
            ll_logitude.setVisibility(View.GONE);
        }
        //天气状况开关
        if (b_weather_switch){
            ll_weather.setVisibility(View.VISIBLE);
        }else {
            ll_weather.setVisibility(View.GONE);
        }
        if (b_custom_switch){
            tv_custom.setVisibility(View.VISIBLE);
        }else {
            tv_custom.setVisibility(View.GONE);
        }

    }

    private void setFrontSize() {
        switch (front_size) {
            case 0:
                float dimension = getResources().getDimension(R.dimen.px_text_7);
                specificFrontSize(dimension);
                break;
            case 1:
                float dimension1 = getResources().getDimension(R.dimen.px_text_8);
                specificFrontSize(dimension1);
                break;
            case 2:
                float dimension2 = getResources().getDimension(R.dimen.px_text_9);
                specificFrontSize(dimension2);
                break;
            case 3:
                float dimension3 = getResources().getDimension(R.dimen.px_text_10);
                specificFrontSize(dimension3);
                break;
        }
    }

    private void specificFrontSize(float dimension) {
        project_weather.setTextSize(dimension);
        tv_fixed_add.setTextSize(dimension);
        jtv_weather.setTextSize(dimension);
        project_logitude_latitude.setTextSize(dimension);
        jtv_logitude.setTextSize(dimension);
        jtv_time.setTextSize(dimension);
        tv_content.setTextSize(dimension);
        jtv_content.setTextSize(dimension);
        jtv_projectName.setTextSize(dimension);
        tv_abtain.setTextSize(dimension);
        jtv_abtain.setTextSize(dimension);
        jtv_place.setTextSize(dimension);
        tv_projectAdd.setTextSize(dimension);
        tv_projectName.setTextSize(dimension);
        project_place.setTextSize(dimension);
        project_time.setTextSize(dimension);
    }

    private void setTitleBackgroundColor() {
        switch (background_color_depth_flag) {
            case 0:
                ll_titile_background.setBackgroundColor(
                        getResources().getColor(R.color.titi_background_color_white_transparent));
                break;
            case 1:
                switch (background_color) {
                    case 0:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_white_1));
                        break;
                    case 1:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_blue_1));
                        break;
                    case 2:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_green_1));
                        break;
                    case 3:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_yellow_1));
                        break;
                    case 4:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_red_1));
                        break;
                    case 5:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_black_1));
                        break;
                    case 6:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_pruple_1));
                        break;
                }
                break;
            case 2:

                switch (background_color) {
                    case 0:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_white_2));
                        break;
                    case 1:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_blue_2));
                        break;
                    case 2:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_green_2));
                        break;
                    case 3:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_yellow_2));
                        break;
                    case 4:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_red_2));
                        break;
                    case 5:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_black_2));
                        break;
                    case 6:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_pruple_2));
                        break;
                }
                break;
            case 3:
                switch (background_color) {
                    case 0:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_white_3));
                        break;
                    case 1:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_blue_3));
                        break;
                    case 2:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_green_3));
                        break;
                    case 3:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_yellow_3));
                        break;
                    case 4:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_red_3));
                        break;
                    case 5:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_black_3));
                        break;
                    case 6:
                        ll_titile_background.setBackgroundColor(
                                getResources().getColor(R.color.titi_background_color_pruple_3));
                        break;
                }
                break;
        }
    }

    private void setTitleColor() {
        project_weather.setTextColor(front_color);
        tv_fixed_add.setTextColor(front_color);
        jtv_weather.setTextColor(front_color);
        project_logitude_latitude.setTextColor(front_color);
        jtv_logitude.setTextColor(front_color);
        jtv_time.setTextColor(front_color);
        tv_content.setTextColor(front_color);
        jtv_content.setTextColor(front_color);
        jtv_projectName.setTextColor(front_color);
        tv_abtain.setTextColor(front_color);
        jtv_abtain.setTextColor(front_color);
        jtv_place.setTextColor(front_color);
        tv_projectAdd.setTextColor(front_color);
        tv_projectName.setTextColor(front_color);
        project_place.setTextColor(front_color);
        project_time.setTextColor(front_color);
    }
    /**
     * dip转pix
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *  * @param context
     *  * @return true 表示开启
     *  */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        /*if (gps || network) {
       		return true;
       	}*/
        if (gps) {
            return true;
        }
        return false;
    }

    /**	 * 强制帮用户打开GPS	 * @param context	 */
    public  void openGPS( Context context) {
        //强制打开
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        //弹窗打开
        new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_info).setTitle("开启定位").setMessage("请打开GPS定位获取位置").setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,887);
                dialogInterface.dismiss();
            }
        }).show();
    }



    private void getLocation () {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
            } else {
                //监视地理位置变化
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
                Location location = locationManager.getLastKnownLocation(locationProvider);
                if (location != null) {
                    //输入经纬度
                   // Toast.makeText(this, location.getLongitude() + " " + location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            List<String> list = locationManager.getAllProviders();
            boolean bfind = false;
            for(String c : list) {
                System.out.println("LocationManager provider:" + c);
                if (c.equals(LocationManager.NETWORK_PROVIDER)){
                    bfind = true;
                    //监视地理位置变化
                    locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
                    Location location = locationManager.getLastKnownLocation(locationProvider);
                    if (location != null) {
                        //不为空,显示地理位置经纬度
                        Geocoder geocoder=new Geocoder(MainActivity.this);
                        List places = null;
                        try {
                            places = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String placename = "";
                        if (places != null && places.size() > 0) {
                            // placename=((Address)places.get(0)).getLocality();
                            //一下的信息将会具体到某条街
                            //其中getAddressLine(0)表示国家，getAddressLine(1)表示精确到某个区，getAddressLine(2)表示精确到具体的街((Address) places.get(0)).getAddressLine(1)
                            placename = ((Address) places.get(0)).getAddressLine(0) + ""
                                     + ","
                                    + ((Address) places.get(0)).getAddressLine(2);
                            String str1 = ((Address) places.get(0)).getAddressLine(0) + "" ;
                            String str2 =((Address) places.get(0)).getAddressLine(1) + "" ;
                            String str3 = ((Address) places.get(0)).getAddressLine(2) + "" ;
                            mLocality = ((Address) places.get(0)).getLocality();
                            str_location = (str1+str3).replace("null","");
                            mLocality = ((Address) places.get(0)).getLocality();
                            if (mLocality==null || mLocality==""){
                                mLocality = "娄底";
                            }
                        }
                        //不为空,显示地理位置经纬度
                        tv_projectAdd.setText(str_location);
                        getNetWeather();
                    }
                    break;
                }
            }
        }
    }


    public LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
        }
        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                str_longitude = ""+lng;
                str_latitude  = ""+lat;
                str_longitude_latitude = str_longitude+"/"+str_latitude;
                project_logitude_latitude.setText(str_longitude_latitude);
                final Geocoder geocoder = new Geocoder(MainActivity.this);
                final List[] places = {null};
                //  ！！！！！华为 安卓8.0 需要放在子线程中获取 ！！！！
                new Thread() {
                    @Override
                    public void run() {
                        //需要在子线程中处理的逻辑
                        if (location != null) {
                            try {
                                // 获取当前线程的Looper
                                Address tempAddress = getAddress(MainActivity.this,location.getLatitude(),location.getLongitude());
                                String str1 = tempAddress.getAddressLine(0);
                                String str2 = tempAddress.getAddressLine(2);
                                str_location = (str1+str2).replace("null","");
                                Message msg = new Message();
                                msg.what = COMPLETED;
                                handler.sendMessage(msg);
                                Locale locale = tempAddress.getLocale();
                                //更新UI等
                                Looper.prepare();
                                //Log.d(TAG, "Looper_addressLine: "+str_location);
                                //Toast.makeText(MainActivity.this,addressLine,Toast.LENGTH_LONG).show();
                                //tv_projectAdd.setText(addressLine);
                                Looper.loop();
                                geocoder.getFromLocation(location.getLatitude(),
                                        location.getLongitude(), 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();

                if (places[0] != null && places[0].size() > 0) {
                    //一下的信息将会具体到某条街
                    //其中getAddressLine(0)表示国家，getAddressLine(1)表示精确到某个区，getAddressLine(2)表示精确到具体的街
                    /*str_location = ((Address) places[0].get(0)).getAddressLine(0) + ""

                            + ((Address) places[0].get(0)).getAddressLine(2);*/

                    String addressLine1 = ((Address) places[0].get(0)).getAddressLine(0);
                    String addressLine2 = ((Address) places[0].get(0)).getAddressLine(1);
                    String addressLine3 = ((Address) places[0].get(0)).getAddressLine(2);
                    String addressLine4 = ((Address) places[0].get(0)).getAddressLine(3);
                    String addressLine5 = ((Address) places[0].get(0)).getAddressLine(4);
                    Log.d(TAG, "onLocationChanged: "+addressLine1+","+addressLine2+","+addressLine3+","+addressLine4+","+addressLine5+",");
                    mLocality = ((Address) places[0].get(0)).getLocality();
                    if (mLocality==null){
                        mLocality = "娄底";
                    }
                }
                //不为空,显示地理位置经纬度
                tv_projectAdd.setText(str_location.replace("null",""));
                UrlHttpUtil.get(HTTP_PRE + mLocality, new CallBackUtil.CallBackString() {
                    @Override
                    public void onFailure(int code, String errorMessage) {

                    }

                    @Override
                    public void onResponse(String response) {
                        JSONObject  dataJson = null;
                        try {
                            dataJson = new JSONObject(response);
                            JSONObject  response1 = dataJson.getJSONObject("data");
                            JSONArray data = response1.getJSONArray("forecast");
                            JSONObject info=data.getJSONObject(0);
                            String high=info.getString("high").substring(2);
                            String low=info.getString("low").substring(2);
                            String type=info.getString("type");
                            String fengxiang=info.getString("fengxiang");
                            str_weather = ""+type+","+fengxiang+","+low+" ~"+high;
                            project_weather.setText(str_weather);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
       // getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "申请权限", Toast.LENGTH_LONG).show();
                    try {
                        List<String> providers = locationManager.getProviders(true);
                        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                            //如果是Network
                            locationProvider = LocationManager.NETWORK_PROVIDER;
                        }else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                            //如果是GPS
                            locationProvider = LocationManager.GPS_PROVIDER;
                        }
                        //监视地理位置变化
                        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
                        Location location = locationManager.getLastKnownLocation(locationProvider);
                        if (location != null) {
                            //不为空,显示地理位置经纬度
                            Geocoder geocoder=new Geocoder(MainActivity.this);
                            List places = null;
                            try {
                                places = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String placename = "";
                            if (places != null && places.size() > 0) {
                                // placename=((Address)places.get(0)).getLocality();
                                //一下的信息将会具体到某条街
                                //其中getAddressLine(0)表示国家，getAddressLine(1)表示精确到某个区，getAddressLine(2)表示精确到具体的街
                                str_location = ((Address) places.get(0)).getAddressLine(0) + ""
                                        //+ ((Address) places.get(0)).getAddressLine(1) + ","
                                        + ","
                                        + ((Address) places.get(0)).getAddressLine(2);

                                String addressLine1 = ((Address) places.get(0)).getAddressLine(0);
                                String addressLine2 = ((Address) places.get(0)).getAddressLine(1);
                                String addressLine3 = ((Address) places.get(0)).getAddressLine(2);
                                String addressLine4 = ((Address) places.get(0)).getAddressLine(3);
                                String addressLine5 = ((Address) places.get(0)).getAddressLine(4);
                                Log.d(TAG, "onLocationChanged: "+addressLine1+","+addressLine2+","+addressLine3+","+addressLine4+","+addressLine5+",");
                                str_location = str_location.replace("null","");
                                mLocality = ((Address) places.get(0)).getLocality();
                                if (mLocality==null || mLocality==""){
                                    mLocality = "娄底";
                                }
                                getNetWeather();
                            }
                            //不为空,显示地理位置经纬度
                            //Toast.makeText(MainActivity.this,  "city:"+placename, Toast.LENGTH_SHORT).show();
                            tv_projectAdd.setText(str_location);

                        }
                    }catch (SecurityException e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "缺少权限", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }



    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
                return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int baseBitmapWidth = 1080;//在拍出照片的bitmap为1080*1920的真机下测试
    private static int baseBitmapHeight = 1920;
    /**
     * 设置默认的百分比，所有拍照画到画布上的文字、底色都必须走此方法
     */
    public static float getPxRatio(float realBitmapWidth, float realBitmapHeight) {
        float ratioWidth = realBitmapWidth / baseBitmapWidth;
        float ratioHeight = realBitmapHeight / baseBitmapHeight;
        return Math.min(ratioWidth, ratioHeight);
    }

    /**
     * dip转pix
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px_(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void getNetWeather(){
        //不为空,显示地理位置经纬度
        tv_projectAdd.setText(str_location.replace("null",""));
        UrlHttpUtil.get(HTTP_PRE + mLocality, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {

            }

            @Override
            public void onResponse(String response) {
                JSONObject  dataJson = null;
                try {
                    dataJson = new JSONObject(response);
                    JSONObject  response1 = dataJson.getJSONObject("data");
                    JSONArray data = response1.getJSONArray("forecast");
                    JSONObject info=data.getJSONObject(0);
                    String high=info.getString("high").substring(2);
                    String low=info.getString("low").substring(2);
                    String type=info.getString("type");
                    String fengxiang=info.getString("fengxiang");
                    str_weather = ""+type+","+fengxiang+","+low+" ~"+high;
                    project_weather.setText(str_weather);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}







