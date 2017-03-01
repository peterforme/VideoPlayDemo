package dlodloclose.com.videoplaydemo;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by admin on 2017/3/1.
 */
public class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.init(getApplicationContext());
    }
}
