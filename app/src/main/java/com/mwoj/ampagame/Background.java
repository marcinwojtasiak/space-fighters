package com.mwoj.ampagame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    private Bitmap sprite;

    Background(Context context, int width, int height)
    {
        sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.spacebg);
        sprite = Bitmap.createScaledBitmap(sprite, width, height, false);
    }

    public Bitmap getSprite()
    {
        return sprite;
    }
}
