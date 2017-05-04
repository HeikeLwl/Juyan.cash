package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

import jufeng.juyancash.R;

/**
 * Created by Administrator102 on 2016/12/19.
 */

public class MyKeyboardView1 extends KeyboardView {

    private Context context;

    public MyKeyboardView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if(key.label != null) {
                if(key.label.equals("0") || key.label.equals("1") || key.label.equals("2")
                        || key.label.equals("3") || key.label.equals("4") || key.label.equals("5")
                        || key.label.equals("6") || key.label.equals("7") || key.label.equals("8") || key.label.equals("9") || key.label.equals(".")){
                    int[] colores = {0xFF938077,0xFFA48F84,0xFFC2AA9D,0xFFD9BDAF};
                    resetBtn2(key,canvas,colores,context.getResources().getColor(R.color.white));
                }else if(key.label.equals("切换") || key.label.equals("删除") ){
                    int[] colores = {0xFF2D6F8C,0xFF419EC7,0xFF46AAD7,0xFF4CB9EA};
                    resetBtn2(key,canvas,colores,context.getResources().getColor(R.color.white));
                }else{
                    int[] colores = {0xFFbbbbbb,0xffdddddd,0xFFfefefe,0xFFffffff};
                    resetBtn2(key,canvas,colores,context.getResources().getColor(R.color.dark));
                }
            }
        }
    }

    private void resetBtn2(Keyboard.Key key, Canvas canvas,int[] backgroundColor,int textColor) {
        //将OK键重新绘制
        RectF targetRect = new RectF(key.x, key.y + 1, key.x + key.width, key.y + key.height + 1);
        RectF targetRect1 = new RectF(key.x+1, key.y + 2, key.x + key.width-1, key.y + key.height - 1);
        RectF targetRect2 = new RectF(key.x+1, key.y + 2, key.x + key.width-2, key.y + key.height - 2);
        RectF targetRect3 = new RectF(key.x+1, key.y + 2, key.x + key.width-3, key.y + key.height - 3);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);
        paint.setTextSize(18);
        paint.setColor(backgroundColor[0]);
        canvas.drawRoundRect(targetRect,5,5,paint);
//        paint.setColor(backgroundColor[1]);
//        canvas.drawRoundRect(targetRect1,5,5,paint);
//        paint.setColor(backgroundColor[2]);
//        canvas.drawRoundRect(targetRect2,5,5,paint);
//        paint.setColor(backgroundColor[3]);
//        canvas.drawRoundRect(targetRect3,5,5,paint);
        paint.setColor(textColor);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (int) ((targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2);
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(key.label.toString(), targetRect.centerX(), baseline, paint);
    }
}