package shsticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

public class StickerActionIcon {

    private Context context;
    private Bitmap srcIcon;
    private Rect rect;

    public StickerActionIcon(Context context) {
        this.context = context;
        rect = new Rect();
    }

    public void setSrcIcon(int resource) {
        srcIcon = BitmapFactory.decodeResource(context.getResources(), resource);
    }

    public void draw(Canvas canvas, float x, float y) {
        rect.left = (int) (x - srcIcon.getWidth() / 2);
        rect.right = (int) (x + srcIcon.getWidth() / 2);
        rect.top = (int) (y - srcIcon.getHeight() / 2);
        rect.bottom = (int) (y + srcIcon.getHeight() / 2);
        canvas.drawBitmap(srcIcon, null, rect, null);
    }

    public boolean isInActionCheck(MotionEvent event) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }
}
