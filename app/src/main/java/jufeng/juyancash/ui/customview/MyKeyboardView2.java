package jufeng.juyancash.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

import jufeng.juyancash.R;

/**
 * Created by Administrator102 on 2016/12/19.
 */

public class MyKeyboardView2 extends KeyboardView {

    private Context context;

    public MyKeyboardView2(Context context, AttributeSet attrs) {
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
                if(key.label.equals("整单打折") || key.label.equals("部分打折") || key.label.equals("方案打折")){
                    resetBtn2(key,canvas,context.getResources().getColor(R.color.orange),context.getResources().getColor(R.color.white));
                }
                else if(key.label.equals("清空所有") || key.label.equals("结账完毕")){
                    resetBtn2(key,canvas,context.getResources().getColor(R.color.red),context.getResources().getColor(R.color.white));
                }
                else if(key.label.equals("现金") || key.label.equals("银行卡") || key.label.equals("微信支付")
                        || key.label.equals("支付宝") || key.label.equals("团购") || key.label.equals("挂账")
                        || key.label.equals("会员支付") || key.label.equals("会员优惠") || key.label.equals("返回")){
                    resetBtn2(key,canvas,context.getResources().getColor(R.color.blue),context.getResources().getColor(R.color.white));
                }else {
                    resetBtn2(key,canvas,context.getResources().getColor(R.color.white),context.getResources().getColor(R.color.dark));
                }

            }
        }
    }

    private void resetBtn2(Keyboard.Key key, Canvas canvas,int backgroundColor,int textColor) {
        //将OK键重新绘制
        Rect targetRect = new Rect(key.x, key.y + 1, key.x + key.width, key.y + key.height + 1);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);
        paint.setTextSize(18);
        paint.setColor(backgroundColor);
        canvas.drawRect(targetRect,paint);
        paint.setColor(textColor);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(key.label.toString(), targetRect.centerX(), baseline, paint);
    }
}