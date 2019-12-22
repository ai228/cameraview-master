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

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.demo.urlhttp.CallBackUtil;
import com.google.android.cameraview.demo.urlhttp.UrlHttpUtil;

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
    private boolean b_watermark_switch=true;
    private boolean b_weather_switch=true;
    private boolean b_longitude_switch=true;
    private boolean b_add_switch=true;
    private boolean b_projectname_switch=true;
    private boolean b_place_switch=true;
    private boolean b_time_switch=true;
    private boolean b_custom_switch=true;

    private int background_color =0;
    private int front_color = -1;
    private int front_size = 0;
    private int background_color_depth = 1;

    private String str_weather = "天       气：";
    private String str_longitude = "经       度：";
    private String str_latitude = "纬       度：";
    private String str_add = "";
    private String str_projectname = "工程名称：(待写)";
    private String str_place = "施工地点：(待写)";
    private String str_time = "时       间：";

    LinearLayout ll_titile_background;
    LinearLayout ll_add;
    TextView project_weather;
    TextView project_logitude;
    TextView project_latitue;
    TextView project_altitude;
    TextView tv_fixed_add;
    TextView tv_custom;

    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID

    int paint_size = 40;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
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
    TextView tv_projectAdd;
    TextView project_place;
    TextView project_time;

    double lat;
    double lng;

    public static final int LOCATION_CODE = 301;
    private LocationManager locationManager;
    private String locationProvider = null;
    private String mLocality;
    private Toast mToast;
    private List<String> list_keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCameraView = findViewById(R.id.camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        FloatingActionButton fab = findViewById(R.id.take_picture);
        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.imview);
        EditText etName = findViewById(R.id.my_name);
        mIm_setup = findViewById(R.id.iv_setup);

        project_place = findViewById(R.id.project_place);
        project_time = findViewById(R.id.project_time);
        ll_titile_background = findViewById(R.id.ll_titile_background);
        ll_add = findViewById(R.id.ll_add);
        //ll_titile_background.setVisibility(View.GONE);
        project_weather = findViewById(R.id.project_weather);
        project_logitude = findViewById(R.id.project_logitude);
        project_latitue = findViewById(R.id.project_latitue);
        project_altitude = findViewById(R.id.project_altitude);
        tv_fixed_add = findViewById(R.id.tv_fixed_add);
        tv_custom = findViewById(R.id.tv_custom);
        tv_projectAdd = findViewById(R.id.project_add);
        tv_projectName = findViewById(R.id.project_name);
        tv_projectName.setText(str_projectname);
        project_place = findViewById(R.id.project_place);
        project_place.setText(str_place);
        etName.setFocusableInTouchMode(false);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time= sdf.format( new Date());
        str_time = "时        间："+time;
        project_time.setText(str_time); //更新时间
        mIm_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setUpActivity = new Intent(MainActivity.this, SetUpActivity.class);
                setUpActivity.putExtra("str_projectname",str_projectname.substring(5));
                setUpActivity.putExtra("str_place",str_place.substring(5));

                startActivityForResult(setUpActivity, 0);
            }
        });

        //经纬度和地址显示
        getLocation();
        sp = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.takend, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

        list_keyword =  new ArrayList<String>();
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
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
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
                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
                    AspectRatioFragment.newInstance(ratios, currentRatio)
                            .show(fragmentManager, FRAGMENT_DIALOG);
                }
                return true;
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show();
            mCameraView.setAspectRatio(ratio);
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
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(front_color);
            switch (front_size){
                case 0:
                    paint_size = (int) getResources().getDimension(R.dimen.px_65);
                    break;
                case 1:
                    paint_size = (int) getResources().getDimension(R.dimen.px_70);
                    break;
                case 2:
                    paint_size = (int) getResources().getDimension(R.dimen.px_75);
                    break;
                case 3:
                    paint_size = (int) getResources().getDimension(R.dimen.px_80);
                    break;
            }
            Log.d(TAG, "onPictureTaken: "+paint_size+"");
            paint.setTextSize(paint_size);
            list_keyword.clear();
            if (b_weather_switch)
                list_keyword.add(str_weather);
            if (b_longitude_switch){
                list_keyword.add(str_longitude);
                list_keyword.add(str_latitude);
            }
            if (b_add_switch)
                list_keyword.add("^_^"+str_add);
            if (b_projectname_switch)
                list_keyword.add(str_projectname);
            if (b_place_switch)
                list_keyword.add(str_place);
            if (b_time_switch)
                list_keyword.add(str_time);
            if (!b_watermark_switch)
                list_keyword.clear();
            Bitmap toLeftBottom1 = ImageUtil.drawTextToLeftBottom(MainActivity.this, bitmap,
                    list_keyword,
                    paint,(int) getResources().getDimension(R.dimen.px_20), (int) getResources().getDimension(R.dimen.px_100),background_color_depth,background_color);
            // imageView.setImageBitmap(toLeftBottom1);
            //saveImage(toLeftBottom1);
            //saveImageToGallery(toLeftBottom1);
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
            if(Build.BRAND .equals("Xiaomi") ){ // 小米手机
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
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
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
        String dirName = "工业相机";
        File appDir = new File(root, dirName);
        if (!appDir.exists()) {
            boolean mkdirs = appDir.mkdirs();
        }

        //文件名为时间
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
     *
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            background_color = data.getExtras().getInt("background_color");  
            front_color = data.getExtras().getInt("front_color");
            str_projectname = "工程名称：" + data.getExtras().getString("name");
            str_place = "施工地点：" + data.getExtras().getString("add");  
            str_time = "时        间：" + data.getExtras().getString("time");  
            front_size = data.getExtras().getInt("front_size");  
            b_watermark_switch = data.getExtras().getBoolean("b_watermark_switch");  
            b_weather_switch = data.getExtras().getBoolean("b_weather_switch");  
            b_longitude_switch = data.getExtras().getBoolean("b_longitude_switch");  
            b_add_switch = data.getExtras().getBoolean("b_add_switch");  
            b_projectname_switch = data.getExtras().getBoolean("b_projectname_switch");  
            b_place_switch = data.getExtras().getBoolean("b_place_switch");  
            b_time_switch = data.getExtras().getBoolean("b_time_switch");  
            b_custom_switch = data.getExtras().getBoolean("b_custom_switch");  
            background_color_depth = data.getExtras().getInt(
                    "background_color_depth");  

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
            //水印开关
            if (b_watermark_switch){
                ll_titile_background.setVisibility(View.VISIBLE);
            }else {
                ll_titile_background.setVisibility(View.INVISIBLE);
            }
            //天气开关
            if (b_weather_switch){
                project_weather.setVisibility(View.VISIBLE);
            }else {
                project_weather.setVisibility(View.GONE);
            }
            //经纬度开关
            if (b_longitude_switch){
                project_logitude.setVisibility(View.VISIBLE);
                project_latitue.setVisibility(View.VISIBLE);
            }else {
                project_logitude.setVisibility(View.GONE);
                project_latitue.setVisibility(View.GONE);
            }
            //地址开关
            if (b_add_switch){
                ll_add.setVisibility(View.VISIBLE);
            }else {
                ll_add.setVisibility(View.GONE);
            }
            //工程名称开关
            if (b_projectname_switch){
                tv_projectName.setVisibility(View.VISIBLE);
            }else {
                tv_projectName.setVisibility(View.GONE);
            }
            //施工地点开关
            if (b_place_switch){
                project_place.setVisibility(View.VISIBLE);
            }else {
                project_place.setVisibility(View.GONE);
            }
            //日期开关
            if (b_time_switch){
                project_time.setVisibility(View.VISIBLE);
            }else {
                project_time.setVisibility(View.GONE);
            }
            //自定义开关
            if (b_custom_switch){
                tv_custom.setVisibility(View.VISIBLE);
            }else {
                tv_custom.setVisibility(View.GONE);
            }
        }
    }

    private void setFrontSize() {
        switch (front_size) {
            case 0:
                float dimension = getResources().getDimension(R.dimen.px_7);
                project_weather.setTextSize(dimension);
                project_logitude.setTextSize(dimension);
                project_latitue.setTextSize(dimension);
                project_altitude.setTextSize(dimension);
                tv_fixed_add.setTextSize(dimension);
                tv_projectAdd.setTextSize(dimension);
                tv_projectName.setTextSize(dimension);
                project_place.setTextSize(dimension);
                project_time.setTextSize(dimension);
                break;
            case 1:
                float dimension1 = getResources().getDimension(R.dimen.px_8);
                project_weather.setTextSize(dimension1);
                project_logitude.setTextSize(dimension1);
                project_latitue.setTextSize(dimension1);
                project_altitude.setTextSize(dimension1);
                tv_fixed_add.setTextSize(dimension1);
                tv_projectAdd.setTextSize(dimension1);
                tv_projectName.setTextSize(dimension1);
                project_place.setTextSize(dimension1);
                project_time.setTextSize(dimension1);
                break;
            case 2:
                float dimension2 = getResources().getDimension(R.dimen.px_9);
                project_weather.setTextSize(dimension2);
                project_logitude.setTextSize(dimension2);
                project_latitue.setTextSize(dimension2);
                project_altitude.setTextSize(dimension2);
                tv_fixed_add.setTextSize(dimension2);
                tv_projectAdd.setTextSize(dimension2);
                tv_projectName.setTextSize(dimension2);
                project_place.setTextSize(dimension2);
                project_time.setTextSize(dimension2);
                break;
            case 3:
                float dimension3 = getResources().getDimension(R.dimen.px_10);
                project_weather.setTextSize(dimension3);
                project_logitude.setTextSize(dimension3);
                project_latitue.setTextSize(dimension3);
                project_altitude.setTextSize(dimension3);
                tv_fixed_add.setTextSize(dimension3);
                tv_projectAdd.setTextSize(dimension3);
                tv_projectName.setTextSize(dimension3);
                project_place.setTextSize(dimension3);
                project_time.setTextSize(dimension3);
                break;
        }
    }

    private void setTitleBackgroundColor() {
        switch (background_color_depth) {
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
        project_logitude.setTextColor(front_color);
        project_latitue.setTextColor(front_color);
        project_altitude.setTextColor(front_color);
        tv_fixed_add.setTextColor(front_color);
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
                            str_add = str1+str3;
                        }
                        //不为空,显示地理位置经纬度
                        tv_projectAdd.setText(str_add);
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
                str_longitude = "经       度："+lng;
                project_logitude.setText(str_longitude);
                str_latitude = "纬       度："+lat;
                project_latitue.setText(str_latitude);
                Geocoder geocoder = new Geocoder(MainActivity.this);
                List places = null;
                try {

                    Address tempAddress = getAddress(MainActivity.this,location.getLatitude(),location.getLongitude());
                    String str1 = tempAddress.getAddressLine(0);
                    String str2 = tempAddress.getAddressLine(2);
                    String addressLine = str1+str2;

                    //更新UI等
                    tv_projectAdd.setText(addressLine);
                    places = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String placename = "";
                if (places != null && places.size() > 0) {
                    //一下的信息将会具体到某条街
                    //其中getAddressLine(0)表示国家，getAddressLine(1)表示精确到某个区，getAddressLine(2)表示精确到具体的街
                    placename = ((Address) places.get(0)).getAddressLine(0) + ""

                            + ((Address) places.get(0)).getAddressLine(2);
                    mLocality = ((Address) places.get(0)).getLocality();
                }
                //不为空,显示地理位置经纬度
                tv_projectAdd.setText(placename);
                UrlHttpUtil.get(HTTP_PRE + "娄底", new CallBackUtil.CallBackString() {
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
                            str_weather = "天       气："+type+","+fengxiang+","+low+" ~"+high;
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
                                placename = ((Address) places.get(0)).getAddressLine(0) + ""
                                        + ((Address) places.get(0)).getAddressLine(1) + ","
                                        + ((Address) places.get(0)).getAddressLine(2);
                                mLocality = ((Address) places.get(0)).getLocality();
                            }
                            //不为空,显示地理位置经纬度
                            //Toast.makeText(MainActivity.this,  "city:"+placename, Toast.LENGTH_SHORT).show();
                            tv_projectAdd.setText(placename);
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
}







