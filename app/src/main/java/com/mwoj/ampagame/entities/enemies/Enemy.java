package com.mwoj.ampagame.entities.enemies;

import android.content.Context;

import com.mwoj.ampagame.entities.Entity;


public abstract class Enemy extends Entity
{
    protected Enemy(Context context, int drawableId, int width, int height)
    {
        super(context, drawableId, width, height);
    }
}
