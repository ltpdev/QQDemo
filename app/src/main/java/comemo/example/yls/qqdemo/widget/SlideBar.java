package comemo.example.yls.qqdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hyphenate.util.DensityUtil;

import comemo.example.yls.qqdemo.R;

/**
 * Created by asus- on 2017/1/5.
 */

public class SlideBar extends View {
    private OnSlideChangeLister onSlideChangeLister;
    private static final String[] SECTIONS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint mPaint;
    private int size;
private int current=-1;
    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 12));
        mPaint.setColor(getResources().getColor(R.color.slide_bar_color));
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth() / 2;
        size = getHeight() / SECTIONS.length;
        int y = size;
        for (int i = 0; i < SECTIONS.length; i++) {
            canvas.drawText(SECTIONS[i], x, y, mPaint);
            y += size;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.slide_bar_shape);
                int index2= (int) (event.getY()/size);
                if(index2<SECTIONS.length){
                    String fristLetter2=SECTIONS[index2];
                    if(onSlideChangeLister!=null) {
                        onSlideChangeLister.onSlideChange(fristLetter2);
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int index= (int) (event.getY()/size);
                if(index<SECTIONS.length){
                    String fristLetter=SECTIONS[index];
                    if(onSlideChangeLister!=null&&index!=current) {
                        onSlideChangeLister.onSlideChange(fristLetter);
                    }
                    current=index;
                }

                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                onSlideChangeLister.onSlideFinshed();
                break;
        }
        return true;
    }

    public  interface OnSlideChangeLister{
        void onSlideChange(String fristLetter);
        void onSlideFinshed();
    }
    public void setOnSlideChangeLister(OnSlideChangeLister onSlideChangeLister){
         this.onSlideChangeLister=onSlideChangeLister;
    }
}
