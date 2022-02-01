package com.mwoj.ampagame.entities.enemies;

import android.content.Context;

import com.mwoj.ampagame.R;


public class BasicEnemy extends Enemy
{
    private static final float speed = 7;

    public BasicEnemy(Context context)
    {
        super(context, R.drawable.enemy, 100, 100);
    }

    @Override
    public void update()
    {
        setY(Math.round((float)getY() + speed));
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
