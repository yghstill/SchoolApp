package ui.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import ui.adapter.FilmAdapter;

/**
 * Created by Chen on 2017/4/17.
 */
public class ImageLoader {
    private ImageView imageView;
    private String url;
    //创建Cache
    private LruCache<String,Bitmap> mCaches;
    private Set<BitmapAsyncTask> mTask;
    private ListView listView;

    public ImageLoader(ListView listView){
        this.listView = listView;
        mTask = new HashSet<>();
        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mCaches = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用，返回的是Bitmap的大小
                return value.getByteCount();
            }
        };
    }

    public void loadImages(int start,int end){
        for (int i = start; i < end; i++) {
            String url = FilmAdapter.URLS[i];
            //从缓存中取出对应的图片
            Bitmap bitmap = getBitmapFromCache(url);
            //如果缓存中没有，那么必须去下载
            if(bitmap == null){
                BitmapAsyncTask task = new BitmapAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else{
                ImageView imageView = (ImageView) listView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    //增加到缓存
    public void addBitmapToCache(String url,Bitmap bitmap){
        if(getBitmapFromCache(url) == null){
            mCaches.put(url,bitmap);
        }
    }

    //从缓存中获取数据
    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);
    }

    public void showImageByAsyncTask(ImageView imageView,String url){
        //从缓存中取出对应的图片
        Bitmap bitmap = getBitmapFromCache(url);
        //如果缓存中没有，那么必须去下载
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
    }

    public void cancleAllTasks(){
        if(mTask != null){
            for (BitmapAsyncTask task : mTask ) {
                task.cancel(false);
            }
        }
    }

    private class BitmapAsyncTask extends AsyncTask<String, Void,Bitmap>{

//        private ImageView mImageView;
        private String mUrl;
        public BitmapAsyncTask(String url){
//            mImageView = imageView;
            mUrl = url;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            //从网络上获取图片
            Bitmap bitmap = getBitmapFromURL(params[0]);
            if(bitmap != null){
                //将不在缓存的图片加入缓存
                addBitmapToCache(params[0],bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) listView.findViewWithTag(mUrl);
            if(imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            mTask.remove(this);
        }
    }

    private Bitmap getBitmapFromURL(String urlString){
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
