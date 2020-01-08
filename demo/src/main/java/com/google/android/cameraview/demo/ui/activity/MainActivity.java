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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.location.PoiRegion;
import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.demo.DemoApplication;
import com.google.android.cameraview.demo.R;
import com.google.android.cameraview.demo.ui.fragment.AspectRatioFragment;
import com.google.android.cameraview.demo.util.ImageUtil;
import com.google.android.cameraview.demo.util.LocationService;
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
import java.util.Set;

public class MainActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        AspectRatioFragment.Listener {
    private static final int COMPLETED = 0;
    public static boolean is_16_9 = true;

    private LocationService locationService;

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
    public boolean isXiMi = false;
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

    public static final int LOCATION_CODE = 301;
    private String mLocality = "";
    private Toast mToast;
    private List<String> list_keyword;
    private static int LIGHT_FLAG = 0;//0：自动；1：关闭；2：打开
    ImageView iv_light;
    private SharedPreferences mSharedPreferences;
    private LinearLayout mLl_takened;
    private TextView mTv_test;
    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager wm = this.getWindowManager();
        double width = wm.getDefaultDisplay().getWidth();
        double height = wm.getDefaultDisplay().getHeight();
        double raio = height / width;
        //Toast.makeText(this,Double.toString(raio),Toast.LENGTH_LONG).show();
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
        imageView = findViewById(R.id.imview);
        TextView etName = findViewById(R.id.my_name);
        mIm_setup = findViewById(R.id.iv_setup);
        mTv_test = findViewById(R.id.tv_test_1);
        project_place = findViewById(R.id.project_place);
        project_time = findViewById(R.id.project_time);
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
        tv_projectAdd = findViewById(R.id.project_add);
        jtv_weather = findViewById(R.id.jtv_weather);
        jtv_logitude = findViewById(R.id.jtv_logitude);
        jtv_time = findViewById(R.id.jtv_time);
        jtv_content = findViewById(R.id.jtv_content);
        jtv_projectName = findViewById(R.id.jtv_projectName);
        jtv_abtain = findViewById(R.id.jtv_abtain);
        jtv_place = findViewById(R.id.jtv_place);
        tv_abtain = findViewById(R.id.tv_abtain);
        tv_custom = findViewById(R.id.tv_custom);

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

        sp = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.takend, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        list_keyword =  new ArrayList<String>();
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
            layoutParams.bottomMargin = (int) (getResources().getDimension(R.dimen.px_220))/2;//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
            mCameraView.setLayoutParams(layoutParams);
            // 移动拍照布局
            RelativeLayout.LayoutParams  layoutParams1 =
                    (RelativeLayout.LayoutParams) mLl_takened.getLayoutParams();
            layoutParams1.bottomMargin = (int) ((getResources().getDimension(R.dimen.px_220))/2);//将默认的距离底部20dp，改为0，这样底部区域全被listview填满。
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

        if (isMIUI()){
            isXiMi = true;
            Log.d(TAG, "onCreate: 是小米");
        }else{
            isXiMi = false;
            Log.d(TAG, "onCreate: 不是 小米");
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
            if (!b_watermark_switch)
                list_keyword.clear();
            if (isXiMi){
                bitmap = ImageUtil.createDegree(bitmap,90);
            }
            float paddingBottom = 100 * getPxRatio(bitmap.getWidth(), bitmap.getHeight());
            Bitmap toLeftBottom1 = ImageUtil.drawTextToLeftBottom(MainActivity.this, bitmap,
                    list_keyword,b_titileShow_switch,str_titileShow,
                    paint,40*getPxRatio(bitmap.getWidth(),bitmap.getHeight()),paddingBottom,background_color_depth_flag,background_color);
            imageView.setImageBitmap(bitmap);
            ImageUtil.saveImageToGallery_test(MainActivity.this,toLeftBottom1);
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
        tv_projectAdd.setText(str_location);
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

    @Override
    protected void onStart() {
        super.onStart();
       // getLocation();
        // -----------location config ------------
        locationService = ((DemoApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
//        int type = getIntent().getIntExtra("from", 0);
//        if (type == 0) {
//            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        } else if (type == 1) {
            locationService.start();
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                int tag = 1;
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                str_latitude = location.getLatitude()+"";
                sb.append(location.getLatitude());
                sb.append("\nlongtitude : ");// 经度
                str_longitude = location.getLongitude()+"";
                str_longitude_latitude = str_longitude+"/"+str_latitude;
                project_logitude_latitude.setText(str_longitude_latitude);
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nProvince : ");// 获取省份
                sb.append(location.getProvince());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                mLocality = location.getCityCode();
                sb.append("\ncity : ");// 城市
                mLocality = location.getCity();
                getNetWeather(mLocality);
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nTown : ");// 获取镇信息
                sb.append(location.getTown());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                str_location = location.getAddrStr();
                tv_projectAdd.setText(location.getAddrStr());
                sb.append(location.getAddrStr());
                sb.append("\nStreetNumber : ");// 获取街道号码
                sb.append(location.getStreetNumber());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append("poiName:");
                        sb.append(poi.getName() + ", ");
                        sb.append("poiTag:");
                        sb.append(poi.getTags() + "\n");
                    }
                }
                if (location.getPoiRegion() != null) {
                    sb.append("PoiRegion: ");// 返回定位位置相对poi的位置关系，仅在开发者设置需要POI信息时才会返回，在网络不通或无法获取时有可能返回null
                    PoiRegion poiRegion = location.getPoiRegion();
                    sb.append("DerectionDesc:"); // 获取POIREGION的位置关系，ex:"内"
                    sb.append(poiRegion.getDerectionDesc() + "; ");
                    sb.append("Name:"); // 获取POIREGION的名字字符串
                    sb.append(poiRegion.getName() + "; ");
                    sb.append("Tags:"); // 获取POIREGION的类型
                    sb.append(poiRegion.getTags() + "; ");
                    sb.append("\nSDK版本: ");
                }
                sb.append(locationService.getSDKVersion()); // 获取SDK版本
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                    //str_weather = "离线状态获取失败";
                    //str_location = "离线状态获取失败";
                    //project_weather.setText("离线状态获取失败");
                    //tv_projectAdd.setText("离线状态获取失败");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    //project_weather.setText("服务端网络定位失败");
                    //tv_projectAdd.setText("服务端网络定位失败");
                    //str_weather = "服务端网络定位失败";
                    //str_location = "服务端网络定位失败";
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                    //project_weather.setText("请检查网络是否通畅");
                    //tv_projectAdd.setText("请检查网络是否通畅");
                    //str_weather = "请检查网络是否通畅";
                    //str_location = "请检查网络是否通畅";
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    //project_weather.setText("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    //tv_projectAdd.setText("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    //str_weather = "无法获取有效定位依据导致定位失败";
                    //str_location = "无法获取有效定位依据导致定位失败";
                }
                Log.d("AAAAAA",sb.toString());
                mTv_test.setText(sb+"sss");
                mTv_test.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }
    };
    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
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


    private void getNetWeather(String city){
        UrlHttpUtil.get(HTTP_PRE + city, new CallBackUtil.CallBackString() {
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
