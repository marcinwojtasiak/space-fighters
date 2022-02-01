package com.mwoj.ampagame.entities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;


public abstract class Entity
{
    private int x, y;
    private int width, height;
    private Bitmap sprite;

    protected Entity(Context context, int drawableId, int width, int height)
    {
        x = 0;
        y = 0;
        this.width = width;
        this.height = height;
        sprite = BitmapFactory.decodeResource(context.getResources(), drawableId);
        sprite = Bitmap.createScaledBitmap(sprite, width, height, false);
    }

    public boolean collides(Entity other)
    {
        return (new Rect(x, y, x + width, y + height))
                .intersect(new Rect(other.x, other.y, other.x + other.width, other.y + other.height));
    }

    public boolean collides(int x, int y)
    {
        return (new Rect(x, y, x + width, y + height)).contains(x, y);
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setPosition(int x,  int y)
    {
        setX(x);
        setY(y);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Bitmap getSprite()
    {
        return sprite;
    }

    public abstract void update();

    public abstract void pause();

    public abstract void resume();

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("Entity{Center: (%d, %d), Width: %d, Height: %d}", x, y, width, height);
    }
}
