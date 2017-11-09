package sticker.stdiohue.com.shsticker.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import java.io.File;

import shsticker.utils.BitmapUtils;
import shsticker.utils.FileUtils;
import sticker.stdiohue.com.shsticker.R;
import sticker.stdiohue.com.shsticker.databinding.ActivityMainBinding;
import sticker.stdiohue.com.shsticker.eventhandlers.MainActivityEH;
import sticker.stdiohue.com.shsticker.ui.bases.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainActivityEH {
    private CompressTask mCompressTask;
    private Thread mThread;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        viewDataBinding.stickerLayout.setZoomRes(R.mipmap.ic_resize);
        viewDataBinding.stickerLayout.setRemoveRes(R.mipmap.ic_remove);
        viewDataBinding.stickerLayout.setRotateRes(R.mipmap.ic_rotate);
        viewDataBinding.stickerLayout.setFlipRes(R.mipmap.ic_mirror);
        viewDataBinding.setEvent(this);
    }

    @Override
    protected void startScreen() {

    }

    @Override
    protected void resumeScreen() {

    }

    @Override
    protected void pauseScreen() {

    }

    @Override
    protected void destroyScreen() {
        mHandler.removeCallbacks(mCompressTask);
        if (mThread != null) {
            mThread = null;
        }
        if (mCompressTask != null){
            mCompressTask = null;
        }
    }

    @Override
    public void onShowListStickerClick() {
        Intent intent = new Intent(this, ListStickerActivity.class);
        startActivityForResult(intent, 200);
    }

    @Override
    public void onShowPreviewClick() {
        Bitmap dstBitmap = viewDataBinding.stickerLayout.generateCombinedBitmap();
        mCompressTask = new CompressTask(dstBitmap);
        mThread = new Thread(mCompressTask);
        mThread.start();
    }

    private class CompressTask implements Runnable {

        private Bitmap dstBitmap;

        public CompressTask(Bitmap dstBitmap) {
            this.dstBitmap = dstBitmap;
        }

        @Override
        public void run() {
            if (dstBitmap == null) {
                mHandler.sendEmptyMessage(0);
                return;
            }
            File successFile = FileUtils.getCacheFile();
            if (BitmapUtils.saveBitmap(dstBitmap, successFile) && successFile != null && successFile.exists()) {
                mHandler.obtainMessage(1, successFile.toString()).sendToTarget();
            } else {
                mHandler.sendEmptyMessage(0);
            }
            BitmapUtils.recycle(dstBitmap);
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                String path = (String) msg.obj;
                PreviewActivity.start(MainActivity.this, path);
            }
            return true;
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK && requestCode == 200) {
            viewDataBinding.stickerLayout.addSticker(data.getIntExtra("res", 0));
        }
    }
}
