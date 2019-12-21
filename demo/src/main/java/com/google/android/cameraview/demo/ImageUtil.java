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

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//import sun.misc.BASE64Decoder;

/**
 *
 *
 * 类名:ImageUtil.java 作者： 许 创建时间：2014-11-1 说明:图片处理工具类.
 *
 * 修改时间： 修改者： 修改说明：
 *
 */
@SuppressLint("SimpleDateFormat")
public class ImageUtil {
	private static Paint mPaint;

	/**
	 * 图片压缩，按比例大小压缩方法
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		newOpts.inPreferredConfig = Config.RGB_565;// 降低图片从ARGB888到RGB565
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 质量压缩.
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

		/*
		 * int options = 100; while ( baos.toByteArray().length / 1024>500) {
		 * //循环判断如果压缩后图片是否大于100kb,大于继续压缩 baos.reset();//重置baos即清空baos options -=
		 * 30;//每次都减少10 image.compress(Bitmap.CompressFormat.JPEG, options,
		 * baos);//这里压缩options%，把压缩后的数据存放到baos中 }
		 */
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static void resetImage(String path, String fileName) {
		Bitmap bitmap = BitmapFactory.decodeFile(path + fileName);
		bitmap = compressImage(bitmap);
		saveMyBitmap(path, fileName, bitmap);
	}

	public static void saveMyBitmap(String filePath, String fileName,
									Bitmap bitmap) {
		try {
			File f = new File(filePath);
			if (f.exists()) {
				f.delete();
			}
			f.mkdirs();
			try {
				f = new File(filePath + fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveWaterBitmap(String filePath, String fileName,
									Bitmap bitmap) {
		try {
			File f = new File(filePath+fileName);
			if (f.exists()) {
				f.delete();
			}
			try {
				f.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static File getFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(filePath + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * bitmap转为base64
	 *
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		bitmap.recycle();
		return result;
	}
	/**
	 * base64转为bitmap
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	public static Bitmap getBitMapBySrc(String imagepath){
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 先设置为TRUE不加载到内存中，但可以得到宽和高
		options.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeFile(imagepath, options); // 此时返回bm为空
		options.inJustDecodeBounds = false;

		int w = options.outWidth;
		int h = options.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 960f;//这里设置高度为800f
		float ww = 640f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (options.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (options.outHeight / hh);
		}

		// 计算缩放比
		//int be = (int) (options.outHeight / (float) 300);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		// 这样就不会内存溢出了
		bm = BitmapFactory.decodeFile(imagepath, options);
		return bm;
	}
	/**
	 * 用当前时间给取得的图片命名
	 *
	 */
	@SuppressWarnings("unused")
	private String getPhotoFileName(int type) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		return type + "" + ".png";
	}

	public static Bitmap getMap(String src, int width, int height)
			throws Exception {
		InputStream inputStream = new FileInputStream(src);
		// 通过一个InputStream创建一个BitmapDrawable对象
		BitmapDrawable drawable = new BitmapDrawable(inputStream);
		// 通过BitmapDrawable对象获得Bitmap对象
		Bitmap bm = drawable.getBitmap();
		// 利用Bitmap对象创建缩略图
		bm = ThumbnailUtils.extractThumbnail(bm, width, height);
		return bm;
	}

	public static Drawable convertBitmap2Drawable(Resources res, Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(res, bitmap);
		return bd;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
								: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark,
										 String title) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		// 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		Paint paint = new Paint();
		// 加入图片
		if (watermark != null) {
			// paint.setAlpha(50);
			// cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);//
			// 在src的右下角画入水印
			cv.drawBitmap(watermark, 0, 0, paint);// 在src的左上角画入水印
		} else {
			return null;
		}
		// 加入文字
		if (title != null) {
			String familyName = "宋体";
			Typeface font = Typeface.create(familyName, Typeface.NORMAL);
			TextPaint textPaint = new TextPaint();
			textPaint.setColor(Color.RED);
			textPaint.setTypeface(font);
			textPaint.setTextSize(40);
			// 这里是自动换行的
			// StaticLayout layout = new
			// StaticLayout(title,textPaint,w,Alignment.ALIGN_OPPOSITE,1.0F,0.0F,true);
			// layout.draw(cv);
			// 文字就加左上角算了
			cv.drawText(title, w - 400, h - 40, textPaint);
		}
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newb;
	}

	public static boolean isExist(String path) {
		boolean flag = true;
		try {
			File f = new File(path);
			if (!f.exists()) {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图
	 * 此方法有两点好处：
	 *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
	 *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
	 *        用这个工具生成的图像不会被拉伸。
	 * @param imagePath 图像的路径
	 * @param width 指定输出图像的宽度
	 * @param height 指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static  Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	/**
	 * 进行添加水印图片和文字
	 *
	 * @param src
	 * @param waterMak
	 * @return
	 */
	public static Bitmap createBitmap(Bitmap src, Bitmap waterMak) {
		if (src == null) {
			return src;
		}
		// 获取原始图片与水印图片的宽与高
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = waterMak.getWidth();
		int wh = waterMak.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas mCanvas = new Canvas(newBitmap);
		// 往位图中开始画入src原始图片
		mCanvas.drawBitmap(src, 0, 0, null);
		// 在src的右下角添加水印
		Paint paint = new Paint();
		//paint.setAlpha(100);
		mCanvas.drawBitmap(waterMak, w - ww - 5, h - wh - 5, paint);
		// 开始加入文字
		Paint textPaint = new Paint();
		textPaint.setColor(Color.RED);
		textPaint.setTextSize(16);
		String familyName = "宋体";
		Typeface typeface = Typeface.create(familyName,
				Typeface.BOLD_ITALIC);
		textPaint.setTypeface(typeface);
		textPaint.setTextAlign(Align.CENTER);
		mCanvas.drawText("已提交", w / 2, 25, textPaint);
		mCanvas.save(Canvas.ALL_SAVE_FLAG);
		mCanvas.restore();
		return newBitmap;
	}
	public static Bitmap handBitmap(Bitmap bitMap){
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		// 设置想要的大小
		int newWidth = 1024;
		int newHeight = 576;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,matrix, true);
		return bitMap;
	}
	/*public static void changeImage(String fromPath,String toPath,String text){
		Bitmap bitMap = BitmapFactory.decodeFile(fromPath);
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		int newWidth = 1024;
		int newHeight = 576;
		if ((float)width/(float) height==(float)16/(float) 9){

		}else if ((float)width/(float) height==(float) 4/(float) 3){
			newWidth=960;
			newHeight=720;
		}else if ((float)width/(float) height==(float) 9/(float) 16){
			newWidth = 576;
			newHeight = 1024;
		}else if ((float)width/(float) height==(float) 3/(float) 4){
			newWidth=720;
			newHeight=960;
		}
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,matrix, true);
		String[] texts = text.split(",");
		for (int i = 0; i < texts.length; i++) {
			bitMap = drawTextToRightBottom(App.context, bitMap, texts[i], 10, Color.RED, 5, 5+10*i);
		}

		try {
			File photo = new File(toPath);
			FileOutputStream fos = new FileOutputStream(photo);
			bitMap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	/*public static byte[] generateImage(String imgStr) {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr != null){ // 图像数据为空
			BASE64Decoder decoder = new BASE64Decoder();
			// Base64解码
			byte[] bytes;
			try {
				bytes = decoder.decodeBuffer(imgStr);
				for (int i = 0; i < bytes.length; ++i) {
					if (bytes[i] < 0) {// 调整异常数据
						final int  a=1024;
						bytes[i] += a;
					}
				}
				return bytes;
			}catch (IOException e) {
				e.toString();
			}
		}
		return null;
	}*/
	public static Bitmap getimage(byte[] imgeData) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeByteArray(imgeData, 0, imgeData.length, newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		newOpts.inPreferredConfig = Config.RGB_565;// 降低图片从ARGB888到RGB565
		bitmap = BitmapFactory.decodeByteArray(imgeData, 0, imgeData.length, newOpts);
//        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
		return bitmap;

	}
//	public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text,
//										   int size, int color, int paddingLeft, int paddingTop) {
//		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		paint.setColor(color);
//		paint.setTextSize(dp2px(context, size));
//		Rect bounds = new Rect();
//		paint.getTextBounds(text, 0, text.length(), bounds);
//		return drawTextToBitmap(context, bitmap, text, paint, bounds,
//				dp2px(context, paddingLeft),
//				dp2px(context, paddingTop) + bounds.height());
//	}

	/**
	 * 绘制文字到右下角
	 * @param context
	 * @param bitmap
	 * @param text
	 * @param size
	 * @param color
	 * @return
	 */
	public static Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, String text,
											   int size, int color, int paddingRight, int paddingBottom) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(color);
		paint.setTextSize(dp2px(context, size));
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		return drawTextToBitmap(context, bitmap, text, paint, bounds,
				bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
				bitmap.getHeight() - dp2px(context, paddingBottom),bitmap.getWidth()/2,bitmap.getHeight()/2);
	}

	/**
	 * 绘制文字到右上方
	 * @param context
	 * @param bitmap
	 * @param text
	 * @param size
	 * @param color
	 * @param paddingRight
	 * @param paddingTop
	 * @return
	 */
//	public static Bitmap drawTextToRightTop(Context context, Bitmap bitmap, String text,
//											int size, int color, int paddingRight, int paddingTop) {
//		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		paint.setColor(color);
//		paint.setTextSize(dp2px(context, size));
//		Rect bounds = new Rect();
//		paint.getTextBounds(text, 0, text.length(), bounds);
//		return drawTextToBitmap(context, bitmap, text, paint, bounds,
//				bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
//				dp2px(context, paddingTop) + bounds.height());
//	}

	/**
	 * 绘制文字到左下方
	 * @param context
	 * @param bitmap
	 * @param paddingLeft
	 * @param paddingBottom
	 * @return
	 */
	public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap,String str_weather,String str_longitude,String str_latitude,String str_altitude,String str_add,
			String str_projectname,String str_place,String str_time,Paint paint, int paddingLeft, int paddingBottom,int background_color_depth,int background_color) {
//		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		paint.setColor(color);
//		paint.setTextSize(dp2px(context, paintSize));
//		Rect bounds = new Rect();
//		paint.getTextBounds(text, 0, text.length(), bounds);
		return drawTextToBitmap(context,bitmap, str_weather,str_longitude,str_latitude,str_altitude,str_add,str_projectname, str_place,str_time, paint,dp2px(context, paddingLeft),bitmap.getHeight() - dp2px(context, paddingBottom),background_color_depth,background_color);
	}

	//图片上绘制文字
	public static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String str_weather,String str_longitude,String str_latitude,String str_altitude,String str_add,
			String str_projectname,String str_place,String str_time,Paint paint,int paddingLeft, int paddingBottom,int background_color_depth,int background_color) {
		Config bitmapConfig = bitmap.getConfig();
		paint.setDither(true); // 获取清晰的图像采样
		paint.setFilterBitmap(true);// 过滤一些
		if (bitmapConfig == null) {
			bitmapConfig = Config.ARGB_8888;
		}
		bitmap = bitmap.copy(bitmapConfig, true);
		Canvas canvas = new Canvas(bitmap);
		Rect rect = new Rect();
		paint.getTextBounds(str_weather, 0, str_weather.length(), rect);
		int h = rect.height()+15;//行与行之间的间距
		Paint paint_title = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint_title.setColor(Color.rgb(252, 100, 8));
		paint_title.setTextSize(context.getResources().getDimension(R.dimen.px_180));
		canvas.drawText("电企通相机", paddingLeft+20, dp2px(context, 150), paint_title);
		mPaint = new Paint();
		//paint.setStyle(Style.STROKE);//空心矩形框
		mPaint.setStyle(Paint.Style.FILL);//实心矩形框
	 
	   switch (background_color_depth) {
            case 0:
                mPaint.setColor(
						context.getResources().getColor(R.color.titi_background_color_white_transparent));
                break;
            case 1:
                switch (background_color) {
                    case 0:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_white_1));
                        break;
                    case 1:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_blue_1));
                        break;
                    case 2:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_green_1));
                        break;
                    case 3:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_yellow_1));
                        break;
                    case 4:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_red_1));
                        break;
                    case 5:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_black_1));
                        break;
                    case 6:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_pruple_1));
                        break;
                }
                break;
            case 2:

                switch (background_color) {
                    case 0:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_white_2));
                        break;
                    case 1:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_blue_2));
                        break;
                    case 2:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_green_2));
                        break;
                    case 3:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_yellow_2));
                        break;
                    case 4:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_red_2));
                        break;
                    case 5:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_black_2));
                        break;
                    case 6:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_pruple_2));
                        break;
                }
                break;
            case 3:
                switch (background_color) {
                    case 0:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_white_3));
                        break;
                    case 1:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_blue_3));
                        break;
                    case 2:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_green_3));
                        break;
                    case 3:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_yellow_3));
                        break;
                    case 4:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_red_3));
                        break;
                    case 5:
                        mPaint.setColor(
								context.getResources().getColor(R.color.titi_background_color_black_3));
                        break;
                    case 6:
                        mPaint.setColor(
                                context.getResources().getColor(R.color.titi_background_color_pruple_3));
                        break;
                }
                break;
        }
        Log.d(TAG, "drawTextToBitmap: "+(paddingLeft-10)+"+++++"+(paddingBottom-7*h-10));
		canvas.drawRect(new RectF(paddingLeft-10, paddingBottom-8*h-(context.getResources().getDimension(R.dimen.px_20)), 4*(bitmap.getWidth()/5), paddingBottom+context.getResources().getDimension(R.dimen.px_20)), mPaint);
		canvas.drawText(str_weather, paddingLeft, paddingBottom-7*h, paint);
		canvas.drawText(str_longitude, paddingLeft, paddingBottom-6*h, paint);
		canvas.drawText(str_latitude, paddingLeft, paddingBottom-5*h, paint);
		if (str_add.length()>10){
            String substring_front= str_add.substring(0,10);
			String substring_end = str_add.substring(10);
            canvas.drawText("地       址："+substring_front, paddingLeft, paddingBottom-4*h, paint);
            canvas.drawText("                   "+substring_end, paddingLeft, paddingBottom-3*h, paint);
            canvas.drawText(str_projectname, paddingLeft, paddingBottom-2*h, paint);
            canvas.drawText(str_place, paddingLeft, paddingBottom-h, paint);
            canvas.drawText(str_time, paddingLeft, paddingBottom, paint);
        }else{
            canvas.drawText("地       址："+str_add, paddingLeft, paddingBottom-4*h, paint);
            canvas.drawText(str_projectname, paddingLeft, paddingBottom-3*h, paint);
            canvas.drawText(str_place, paddingLeft, paddingBottom-2*h, paint);
            canvas.drawText(str_time, paddingLeft, paddingBottom-1*h, paint);
        }
		return bitmap;
	}

	//图片上绘制文字
	private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
										   Paint paint, Rect bounds, int paddingLeft, int paddingTop,int width,int high) {
		Config bitmapConfig = bitmap.getConfig();

		paint.setDither(true); // 获取跟清晰的图像采样
		paint.setFilterBitmap(true);// 过滤一些
		if (bitmapConfig == null) {
			bitmapConfig = Config.ARGB_8888;
		}
		bitmap = bitmap.copy(bitmapConfig, true);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawText(text, paddingLeft, paddingTop, paint);
//		TextPaint textPaint = new TextPaint();
//		textPaint.setAntiAlias(true);
//		textPaint.setTextSize(26.0F);
//		textPaint.setColor(Color.RED);
//
//		StaticLayout sl= new StaticLayout(text, textPaint, bitmap.getWidth()-8, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//		canvas.translate(width/2, high/2);
//		sl.draw(canvas);
		return bitmap;
	}
	/**
	 * dip转pix
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}
