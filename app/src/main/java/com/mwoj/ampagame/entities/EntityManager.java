package com.mwoj.ampagame.entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;


public class EntityManager
{
    private ArrayList<Entity> entities = new ArrayList<>();

    public synchronized boolean registerEntity(Entity entity)
    {
        if (entities.contains(entity))
        {
            return false;
        }
        entities.add(entity);
        return true;
    }

    public synchronized boolean unregisterEntity(Entity entity)
    {
        return entities.remove(entity);
    }

    public synchronized void updateEntities()
    {
        for(Entity entity : entities)
        {
            entity.update();
        }
    }

    public synchronized void drawEntities(Canvas canvas, Paint paint)
    {
        for(Entity entity : entities)
        {
            canvas.drawBitmap(entity.getSprite(), entity.getX(), entity.getY(), paint);
        }
    }

    public synchronized void pause()
    {
        for(Entity entity : entities)
        {
            entity.pause();
        }
    }

    public synchronized void resume()
    {
        for(Entity entity : entities)
        {
            entity.resume();
        }
    }
}
