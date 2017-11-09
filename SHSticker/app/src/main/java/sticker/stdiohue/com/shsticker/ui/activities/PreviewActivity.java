package sticker.stdiohue.com.shsticker.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import sticker.stdiohue.com.shsticker.R;
import sticker.stdiohue.com.shsticker.databinding.ActivityPreviewBinding;
import sticker.stdiohue.com.shsticker.eventhandlers.PreviewActivityEH;
import sticker.stdiohue.com.shsticker.ui.bases.BaseActivity;

public class PreviewActivity extends BaseActivity<ActivityPreviewBinding> implements PreviewActivityEH {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void init() {
        viewDataBinding.ivPreview.setImageURI(Uri.parse(getIntent().getStringExtra("path")));
        viewDataBinding.setEvent(this);
    }

    public static void start(Context context, String path){
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
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

    }

    @Override
    public void onBackClick() {
        onBackPressed();
    }
}
