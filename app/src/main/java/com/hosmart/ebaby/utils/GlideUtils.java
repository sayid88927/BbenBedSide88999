package com.hosmart.ebaby.utils;

import android.content.Context;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hosmart.ebaby.base.BaseApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;


import java.io.File;
import java.util.UUID;

/**
 * Created by Administrator on 2018/7/30.
 */

public class GlideUtils {

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(BaseApplication.getContext()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(BaseApplication.getContext()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(BaseApplication.getContext()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 清除图片所有缓存
     */
    public static void clearImageAllCache() {
        clearImageDiskCache();
        clearImageMemoryCache();

    }

    /**
     * 加载本地图片
     */

    public static void loadLocation(Context context, int resourceId, ImageView iv) {
        Glide.with(context).load(resourceId).into(iv);
    }


    /**
     * 加载SD卡图片iv
     */

    public static void loadSD(Context context, File file, ImageView iv) {
        Glide.with(context).load(file).into(iv);
    }

    /**
     * 网络基本图片加载
     */

    public static void load(Context context, String imgUrl, ImageView iv,int imgid) {
        Glide.with(context).load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(imgid)
                .error(imgid)
                .into(iv);
    }

    /**
     * 网络基本图片加载
     */

    public static void load(Context context, String imgUrl, ImageView iv) {
        Glide.with(context).load(imgUrl).signature(new StringSignature(UUID.randomUUID().toString())).into(iv);
    }
    /**
     * 网络基本图片加载
     * 使用centerCrop是利用图片图填充ImageView设置的大小，如果ImageView的Height是match_parent则图片就会被拉伸填充
     */

    public static void loadCenterCrop(Context context, String imgUrl, ImageView iv) {
        Glide.with(context).load(imgUrl).centerCrop().into(iv);
    }

    /**
     * 网络基本图片加载
     *  * 使用fitCenter是利用图片图填充ImageView设置的大小
     */

    public static void loadFitCenter(Context context, String imgUrl, ImageView iv) {
        Glide.with(context).load(imgUrl).fitCenter().thumbnail(0.1f).into(iv);
    }

    /**
     * 网络基本图片加载
     *  * 使用fitCenter是利用图片图填充ImageView设置的大小
     */

    public static void loadFitCenter(Context context, String imgUrl, ImageView iv,int def) {
        Glide.with(context).load(imgUrl).fitCenter().placeholder(def).error(def).thumbnail(0.1f).into(iv);
    }


    /**
     * 图片自定义显示大小
     */
    public static void loadOverride(Context context, String imgUrl,ImageView iv,int width,int height) {
        Glide.with(context).load(imgUrl).override(width, height).into(iv);
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }
    //---------------------------------------------------相关属性---------------------------------------------------------------------

//                 thumbnail(float sizeMultiplier). 请求给定系数的缩略图。如果缩略图比全尺寸图先加载完，就显示缩略图，否则就不显示。系数sizeMultiplier必须在(0,1)之间，可以递归调用该方法。
//                 sizeMultiplier(float sizeMultiplier). 在加载资源之前给Target大小设置系数。
//                 diskCacheStrategy(DiskCacheStrategy strategy).设置缓存策略。DiskCacheStrategy.SOURCE：缓存原始数据，DiskCacheStrategy.RESULT：缓存变换(如缩放、裁剪等)后的资源数据，DiskCacheStrategy.NONE：什么都不缓存，DiskCacheStrategy.ALL：缓存SOURC和RESULT。默认采用DiskCacheStrategy.RESULT策略，对于download only操作要使用DiskCacheStrategy.SOURCE。
//                 priority(Priority priority). 指定加载的优先级，优先级越高越优先加载，但不保证所有图片都按序加载。枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW。默认为Priority.NORMAL。
//                 dontAnimate(). 移除所有的动画。
//                 animate(int animationId). 在异步加载资源完成时会执行该动画。
//                 animate(ViewPropertyAnimation.Animator animator). 在异步加载资源完成时会执行该动画。
//                 placeholder(int resourceId). 设置资源加载过程中的占位Drawable。
//                 placeholder(Drawable drawable). 设置资源加载过程中的占位Drawable。
//                 fallback(int resourceId). 设置model为空时要显示的Drawable。如果没设置fallback，model为空时将显示error的Drawable，如果error的Drawable也没设置，就显示placeholder的Drawable。
//                 fallback(Drawable drawable).设置model为空时显示的Drawable。
//                 error(int resourceId).设置load失败时显示的Drawable。
//                 error(Drawable drawable).设置load失败时显示的Drawable。
//
//                 skipMemoryCache(boolean skip). 设置是否跳过内存缓存，但不保证一定不被缓存（比如请求已经在加载资源且没设置跳过内存缓存，这个资源就会被缓存在内存中）。
//
//                 override(int width, int height). 重新设置Target的宽高值（单位为pixel）。
//                 into(Y target).设置资源将被加载到的Target。
//                 into(ImageView view). 设置资源将被加载到的ImageView。取消该ImageView之前所有的加载并释放资源。
//                 into(int width, int height). 后台线程加载时要加载资源的宽高值（单位为pixel）。
//                 preload(int width, int height). 预加载resource到缓存中（单位为pixel）。
//                 asBitmap(). 无论资源是不是gif动画，都作为Bitmap对待。如果是gif动画会停在第一帧。
//                 asGif().把资源作为GifDrawable对待。如果资源不是gif动画将会失败，会回调.error()。

}
