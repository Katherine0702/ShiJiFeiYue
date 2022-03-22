package com.cshen.tiyu.net.https;

import org.xutils.x;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * Created by Chen on 2016/4/5.
 */
public class xUtilsImageUtils {
	/****
	CENTER:在视图中使图像居中，不执行缩放
	CENTER_CROP:均衡的缩放图像（保持图像原始比例），使图片的两个坐标（宽、高）都大于等于 相应的视图坐标（负的内边距）
	CENTER_INSIDE:均衡的缩放图像（保持图像原始比例），使图片的两个坐标（宽、高）都小于等于 相应的视图坐标（负的内边距）
	FIT_CENTER:使用 CENTER 方式缩放图像。
    FIT_END:使用 END 方式缩放图像。
    FIT_START:使用 START 方式缩放图像。
    FIT_XY:使用 FILL 方式缩放图像。
    MATRIX：绘制时，使用图像矩阵方式缩放。图像矩阵可以通过 setImageMatrix(Matrix) 设置。
	 **/

    /**
     * 显示图片（默认情况）
     *
     * @param imageView 图像控件
     * @param iconUrl   图片地址
     */
    public static void display(ImageView imageView,int error,String iconUrl) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setFailureDrawableId(error)             
                .build();
        x.image().bind(imageView, iconUrl,imageOptions);
    }
    
    public static void displayIN(ImageView imageView,int error,String iconUrl) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .setFailureDrawableId(error)             
                .build();
        x.image().bind(imageView, iconUrl,imageOptions);
    }
    public static void displayFitCenter(ImageView imageView,int error,String iconUrl) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setFailureDrawableId(error)             
                .build();
        x.image().bind(imageView, iconUrl,imageOptions);
    }
    
    /**
     * 显示图片（默认情况）
     *
     * @param imageView 图像控件
     * @param iconUrl   图片地址
     */
    public static void displayWithLoading(ImageView imageView,int loadingResId,int error,String iconUrl) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                .setImageScaleType(ScaleType.FIT_XY)
                .setFailureDrawableId(error)  
                .setLoadingDrawableId(loadingResId)
                .build();
        x.image().bind(imageView, iconUrl,imageOptions);
    }

    
    /**
     * 显示圆角图片
     *
     * @param imageView 图像控件
     * @param iconUrl   图片地址
     * @param radius    圆角半径，
     */
    public static void display(ImageView imageView, String iconUrl, int radius) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setRadius(DensityUtil.dip2px(radius))
                .setIgnoreGif(false)
                .setCrop(true)//是否对图片进行裁剪
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }

    /**
     * 显示圆形头像，第三个参数为true
     *
     * @param imageView  图像控件
     * @param iconUrl    图片地址
     * @param isCircluar 是否显示圆形
     */
    public static void display(ImageView imageView, String iconUrl, boolean isCircluar) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setCircular(isCircluar)
                .setCrop(true)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }
    /**
     * 显示圆形头像，第三个参数为true
     *
     * @param imageView  图像控件
     * @param iconUrl    图片地址
     * @param isCircluar 是否显示圆形
     */
    public static void display(ImageView imageView, String iconUrl, int error,boolean isCircluar) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setCircular(isCircluar)
                .setFailureDrawableId(error)
                .setCrop(true)
                .build();
        x.image().bind(imageView, iconUrl, imageOptions);
    }
}