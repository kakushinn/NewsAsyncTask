package Logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ImageLoader {

    private ImageView mImageView = null;
    private String mUrl = null;
    private LruCache<String,Bitmap> mCaches;
    public ImageLoader() {
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/4;
        mCaches = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

    }

    public void addBitmapIntoCaches(String url , Bitmap bitmap){
        if(getBitmapFromCaches(url) == null)
        mCaches.put(url , bitmap);
    }

    public Bitmap getBitmapFromCaches(String url){
        return mCaches.get(url);
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mImageView.getTag().equals(mUrl)){
                Bitmap bitmap = (Bitmap) msg.obj;
                mImageView.setImageBitmap(bitmap);
            }
        }
    };


    //通过异步线程的方法加载图片
    public void showImageByThread(ImageView imageView ,final String url){
        mImageView = imageView;
        mUrl = url;
        Bitmap bitmap = getBitmapFromCaches(url);
        if(bitmap == null){
            new Thread(){

                @Override
                public void run() {
                    super.run();
//                    try {
//                        sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    Bitmap mBitmap = getBitmapFromUrl(url);
                    if(mBitmap != null){
                        addBitmapIntoCaches(url,mBitmap);
                    }
                    Message msg = Message.obtain();
                    msg.obj = mBitmap;
                    mHandler.sendMessage(msg);
                }
            }.start();
        }else{
            Message msg = Message.obtain();
            msg.obj = bitmap;
            mHandler.sendMessage(msg);
        }

    }

    public Bitmap getBitmapFromUrl(String urlString){
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
