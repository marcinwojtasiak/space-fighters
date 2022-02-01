package com.mwoj.ampagame.debug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;


public class DrawView extends View
{
    Paint paint = new Paint();
    public ArrayList<Pair<Point, Point>> points = new ArrayList<>();

    private void init()
    {
        paint.setColor(Color.BLACK);
    }

    public DrawView(Context context)
    {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        for(Pair<Point, Point> line : points)
        {
            canvas.drawLine(line.first.x, line.first.y, line.second.x, line.second.y , paint);
        }
    }

}
