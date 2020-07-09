package app.buu.znsz;

import android.app.Application;

import com.tencent.mmkv.MMKV;

import org.song.http.QSHttp;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);//初始化MMKV
        QSHttp.init(this);//初始化QSHttp
    }
}
