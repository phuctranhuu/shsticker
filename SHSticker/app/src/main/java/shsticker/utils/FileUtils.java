package shsticker.utils;

import android.os.Environment;

import java.io.File;

import sticker.stdiohue.com.shsticker.App;

public class FileUtils {

    private static File mCacheFile;

    public static File getCacheFile() {
        File file = new File(getAppCacheDir(), "image");
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = "temp_" + System.currentTimeMillis() + ".jpg";
        return new File(file, fileName);
    }

    private static File getAppCacheDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            mCacheFile = App.getInstance().getExternalCacheDir();
        }
        if (mCacheFile == null) {
            mCacheFile = App.getInstance().getCacheDir();
        }
        return mCacheFile;
    }
}
