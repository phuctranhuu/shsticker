package shsticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import shsticker.utils.PointUtils;

public class StickerView extends ImageView {

    private Context context;
    private Sticker sticker;
    private Matrix downMatrix = new Matrix();
    private Matrix moveMatrix = new Matrix();
    private PointF midPoint = new PointF();
    private PointF imageMidPoint = new PointF();
    private StickerActionIcon rotateIcon;
    private StickerActionIcon zoomIcon;
    private StickerActionIcon removeIcon;
    private StickerActionIcon flipIcon;
    private Paint paintEdge;

    private int mode;
    private boolean isEdit = true;
    private OnStickerActionListener listener;
    private int flipRes;

    public void setOnStickerActionListener(OnStickerActionListener listener) {
        this.listener = listener;
    }

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setScaleType(ScaleType.MATRIX);
        rotateIcon = new StickerActionIcon(context);
        zoomIcon = new StickerActionIcon(context);
        removeIcon = new StickerActionIcon(context);
        flipIcon = new StickerActionIcon(context);
        paintEdge = new Paint();
        paintEdge.setColor(Color.BLACK);
        paintEdge.setAlpha(170);
        paintEdge.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            sticker.getMatrix().postTranslate((getWidth() - sticker.getStickerWidth()) / 2, (getHeight() - sticker.getStickerHeight()) / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (sticker == null) return;
        sticker.draw(canvas);
        float[] points = PointUtils.getBitmapPoints(sticker.getSrcImage(), sticker.getMatrix());
        float x1 = points[0];
        float y1 = points[1];
        float x2 = points[2];
        float y2 = points[3];
        float x3 = points[4];
        float y3 = points[5];
        float x4 = points[6];
        float y4 = points[7];
        if (isEdit) {
            canvas.drawLine(x1, y1, x2, y2, paintEdge);
            canvas.drawLine(x2, y2, x4, y4, paintEdge);
            canvas.drawLine(x4, y4, x3, y3, paintEdge);
            canvas.drawLine(x3, y3, x1, y1, paintEdge);

            rotateIcon.draw(canvas, x2, y2);
            zoomIcon.draw(canvas, x3, y3);
            removeIcon.draw(canvas, x1, y1);
            flipIcon.draw(canvas, x4, y4);
        }
    }

    private float downX;
    private float downY;
    private float oldDistance;
    private float oldRotation;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        boolean isStickerOnEdit = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                if (sticker == null) {
                    return false;
                }
                if (removeIcon.isInActionCheck(event)) {
                    if (listener != null) {
                        listener.onDelete();
                    }
                } else if (flipIcon.isInActionCheck(event)) {
                    mode = ActionMode.FLIP;
                    sticker.getMatrix().preScale(-1, 1, sticker.getStickerWidth() * 1f / 2, sticker.getStickerHeight() * 1f / 2);
                    downMatrix.set(sticker.getMatrix());
                    imageMidPoint = sticker.getImageMidPoint(downMatrix);
                    invalidate();
                } else if (rotateIcon.isInActionCheck(event)) {
                    mode = ActionMode.ROTATE;
                    downMatrix.set(sticker.getMatrix());
                    imageMidPoint = sticker.getImageMidPoint(downMatrix);
                    oldRotation = sticker.getSpaceRotation(event, imageMidPoint);
                } else if (zoomIcon.isInActionCheck(event)) {
                    mode = ActionMode.ZOOM_SINGLE;
                    downMatrix.set(sticker.getMatrix());
                    imageMidPoint = sticker.getImageMidPoint(downMatrix);
                    oldDistance = sticker.getSingleTouchDistance(event, imageMidPoint);
                } else if (isInStickerArea(sticker, event)) {
                    mode = ActionMode.TRANS;
                    downMatrix.set(sticker.getMatrix());
                } else {
                    isStickerOnEdit = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ActionMode.ZOOM_MULTI;
                oldDistance = sticker.getMultiTouchDistance(event);
                midPoint = sticker.getMidPoint(event);
                downMatrix.set(sticker.getMatrix());
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ActionMode.ROTATE) {
                    moveMatrix.set(downMatrix);
                    float deltaRotation = sticker.getSpaceRotation(event, imageMidPoint) - oldRotation;
                    moveMatrix.postRotate(deltaRotation, imageMidPoint.x, imageMidPoint.y);
                    sticker.getMatrix().set(moveMatrix);
                    invalidate();
                }
                else if (mode == ActionMode.ZOOM_SINGLE) {
                    moveMatrix.set(downMatrix);
                    float scale = sticker.getSingleTouchDistance(event, imageMidPoint) / oldDistance;
                    moveMatrix.postScale(scale, scale, imageMidPoint.x, imageMidPoint.y);
                    sticker.getMatrix().set(moveMatrix);
                    invalidate();
                }
                else if (mode == ActionMode.ZOOM_MULTI) {
                    moveMatrix.set(downMatrix);
                    float scale = sticker.getMultiTouchDistance(event) / oldDistance;
                    moveMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    sticker.getMatrix().set(moveMatrix);
                    invalidate();
                }
                else if (mode == ActionMode.TRANS) {
                    moveMatrix.set(downMatrix);
                    moveMatrix.postTranslate(event.getX() - downX, event.getY() - downY);
                    sticker.getMatrix().set(moveMatrix);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                mode = ActionMode.NONE;
                midPoint = null;
                imageMidPoint = null;
                break;
            default:
                break;
        }
        if (isStickerOnEdit && listener != null) {
            listener.onEdit(this);
        }
        return isStickerOnEdit;
    }

    private boolean isInStickerArea(Sticker sticker, MotionEvent event) {
        RectF dst = sticker.getSrcImageBound();
        return dst.contains(event.getX(), event.getY());
    }

    @Override
    public void setImageResource(int resId) {
        sticker = new Sticker(BitmapFactory.decodeResource(context.getResources(), resId));
    }

    public Sticker getSticker() {
        return sticker;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        sticker = new Sticker(bm);
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        postInvalidate();
    }

    public void setRotateRes(int rotateRes) {
        rotateIcon.setSrcIcon(rotateRes);
    }

    public void setZoomRes(int zoomRes) {
        zoomIcon.setSrcIcon(zoomRes);
    }

    public void setRemoveRes(int removeRes) {
        removeIcon.setSrcIcon(removeRes);
    }

    public void setFlipRes(int flipRes) {
        flipIcon.setSrcIcon(flipRes);
    }
}
