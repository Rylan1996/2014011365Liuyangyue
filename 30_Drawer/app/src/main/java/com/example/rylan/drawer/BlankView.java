package com.example.rylan.drawer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Rylan on 2016/9/9.
 */
public class BlankView extends View {

    public BlankView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        //三角形
        Path path=new Path();
        path.moveTo(300, 600);
        path.lineTo(200, 120);
        path.lineTo(100, 200);
        path.close();
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        //文本
        paint.setTextSize(60);
        paint.setColor(Color.BLUE);
        paint.setStrikeThruText(true);
        canvas.drawText("hello world", 600, 1400, paint);

        //矩形
        canvas.drawRect(90, 50, 400, 400, paint);
        Path pathText=new Path();


        pathText.addCircle(590,800,300, Path.Direction.CCW);

        canvas.drawTextOnPath("Acting as if nothing borne in mind is the best revenge.It's all for myself to live better.",pathText,0,10,paint);
    }


}
