package shsticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class StickerLayout extends FrameLayout {

    private Context context;
    private List<StickerView> stickerViews;
    private FrameLayout.LayoutParams stickerParams;
    private ImageView ivImage;

    private int rotateRes;
    private int zoomRes;
    private int removeRes;
    private int flipRes;

    public StickerLayout(Context context) {
        this(context, null);
    }

    public StickerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        stickerViews = new ArrayList<>();
        stickerParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        addBackgroundImage();
    }

    private void addBackgroundImage() {
        FrameLayout.LayoutParams bgParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ivImage = new ImageView(context);
        ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
        ivImage.setLayoutParams(bgParams);
        addView(ivImage);
    }

    public void setBackgroundImage(int resource) {
        ivImage.setImageResource(resource);
    }

    public void addSticker(int resource) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource);
        addSticker(bitmap);
    }

    public void addSticker(Bitmap bitmap) {
        final StickerView sv = new StickerView(context);
        sv.setImageBitmap(bitmap);
        sv.setLayoutParams(stickerParams);
        sv.setOnStickerActionListener(new OnStickerActionListener() {
            @Override
            public void onDelete() {
                removeView(sv);
                stickerViews.remove(sv);
                redraw();
            }

            @Override
            public void onEdit(StickerView stickerView) {
                int position = stickerViews.indexOf(stickerView);
                stickerView.setEdit(true);
                stickerView.bringToFront();

                int size = stickerViews.size();
                for (int i = 0; i < size; i++) {
                    StickerView item = stickerViews.get(i);
                    if (item == null) continue;
                    if (position != i) {
                        item.setEdit(false);
                    }
                }
            }
        });
        addView(sv);
        stickerViews.add(sv);
        redraw();
    }

    public void getPreview() {
        for (StickerView item : stickerViews) {
            if (item == null) continue;
            item.setEdit(false);
        }
    }

    private void redraw() {
        redraw(true);
    }

    private void redraw(boolean isNotGenerate) {
        int size = stickerViews.size();
        if (size <= 0) return;
        for (int i = size - 1; i >= 0; i--) {
            StickerView item = stickerViews.get(i);
            if (item == null) continue;
            item.setZoomRes(zoomRes);
            item.setRotateRes(rotateRes);
            item.setRemoveRes(removeRes);
            item.setFlipRes(flipRes);
            if (i == size - 1) {
                item.setEdit(isNotGenerate);
            } else {
                item.setEdit(false);
            }
            stickerViews.set(i, item);
        }
    }

    public Bitmap generateCombinedBitmap() {
        redraw(false);
        Bitmap dst = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        draw(canvas);
        return dst;
    }

    public void setRotateRes(int rotateRes) {
        this.rotateRes = rotateRes;
    }

    public void setZoomRes(int zoomRes) {
        this.zoomRes = zoomRes;
    }

    public void setRemoveRes(int removeRes) {
        this.removeRes = removeRes;
    }

    public void setFlipRes(int flipRes) {
        this.flipRes = flipRes;
    }
}
