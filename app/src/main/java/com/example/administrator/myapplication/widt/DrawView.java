package com.example.administrator.myapplication.widt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义View
 *
 * @author VanishMagic
 */
public class DrawView extends View
{
    //笔触当前坐标  
    float preX;
    float preY;
    //路径  
    private Path path;
    //画笔  
    public Paint paint = null;
    //想要保存成的图片的背景大小  
    final int VIEW_WIDTH = 1000;
    final int VIEW_HEIGHT = 1000;
    //笔迹图像  
    Bitmap cacheBitmap = null;
    //画布  
    Canvas cacheCanvas = null;


    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        cacheBitmap = Bitmap.createBitmap(VIEW_WIDTH, VIEW_HEIGHT,
                Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();
        cacheCanvas.setBitmap(cacheBitmap);
        //Paint.DITHER_FLAG为抗抖动常量  
        paint = new Paint(Paint.DITHER_FLAG);
        //设置笔触颜色  
        paint.setColor(Color.BLACK);
        //设置笔触粗细  
        paint.setStrokeWidth(3);
        //设置画笔类型  
        paint.setStyle(Paint.Style.STROKE);
        //设置抗锯齿  
        paint.setAntiAlias(true);
        //抗抖动  
        paint.setDither(true);
    }

    public void clear()
    {
        //清除画布  
        cacheBitmap = Bitmap.createBitmap(VIEW_WIDTH, VIEW_HEIGHT,
                Bitmap.Config.ARGB_8888);
        cacheCanvas.setBitmap(cacheBitmap);
        //更新View  invalidate vt. 使无效；使无价值  
        invalidate();
    }

    /**
     * 为了保存成图片
     *
     * @return
     */
    public Bitmap save()
    {
        return cacheBitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //获得当前笔触位置  
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction())
        {
            //当按下时
            case MotionEvent.ACTION_DOWN:
                //路径起始点移动到此处，并记录
                path.moveTo(x, y);
                preX = x;
                preY = y;
                break;
            //当移动时  
            case MotionEvent.ACTION_MOVE:
                //平滑曲线连接
                path.quadTo(preX, preY, x, y);
                preX = x;
                preY = y;
                path.moveTo(x, y);
                break;
            //当抬起时  
            case MotionEvent.ACTION_UP:
                //在画布上画出路径
                cacheCanvas.drawPath(path, paint);
                //重置路径
                path.reset();
                break;
        }
        //更新View  
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Paint bmpPaint = new Paint();
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);
        canvas.drawPath(path, paint);


    }
}  