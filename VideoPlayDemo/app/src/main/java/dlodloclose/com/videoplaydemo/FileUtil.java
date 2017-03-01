package dlodloclose.com.videoplaydemo;

import java.io.File;

/**
 * Created by admin on 2017/3/1.
 */
public class FileUtil {

    public static String getFileName(String filePath) {
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }
}
