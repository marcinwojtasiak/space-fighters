package com.mwoj.ampagame.entities;

import android.content.Context;

import com.mwoj.ampagame.R;


public class Bullet extends Entity
{
    private static final int speed = 5;

    public Bullet(Context context)
    {
        super(context, R.drawable.projectile, 14, 32);
    }

    @Override
    public void update()
    {
        setY(getY() - speed);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }
}
