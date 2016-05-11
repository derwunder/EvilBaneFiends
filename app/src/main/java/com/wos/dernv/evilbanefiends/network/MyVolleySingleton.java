package com.wos.dernv.evilbanefiends.network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.wos.dernv.evilbanefiends.myapp.MyApp;

/**
 * Created by der_w on 5/9/2016.
 */
public class MyVolleySingleton {
    private static MyVolleySingleton sInstance=null;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    private MyVolleySingleton(){
        mRequestQueue= Volley.newRequestQueue(MyApp.getAppContext());
        mImageLoader=new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
           private LruCache<String,Bitmap>cache=
                   new LruCache<>((int)(Runtime.getRuntime().maxMemory()/1024)/8);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
    }
    public static MyVolleySingleton getsInstance(){
       if(sInstance==null){
           sInstance=new MyVolleySingleton();
       }
        return sInstance;
    }
    public RequestQueue getmRequestQueue(){return mRequestQueue;}
    public ImageLoader getmImageLoader(){return mImageLoader;}
}
