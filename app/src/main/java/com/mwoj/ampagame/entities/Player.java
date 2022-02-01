package com.mwoj.ampagame.entities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.mwoj.ampagame.GameView;
import com.mwoj.ampagame.R;


public class Player extends Entity implements SensorEventListener
{
    private static final float speedMultiplier = 50;

    private final SensorManager sensorManager;

    private final Sensor accelerometer;
    private final Sensor magnetometer;

    private boolean accelerometerSet = false;
    private boolean magnetometerSet = false;

    private float[] accelerometerValues = new float[3];
    private float[] magnetometerValues = new float[3];

    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];


    public Player(Context context)
    {
        super(context, R.drawable.player, 150, 150);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void resume()
    {
        accelerometerSet = false;
        magnetometerSet = false;
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void update()
    {
        setX(Math.round((float)getX() - orientation[1] * speedMultiplier));
        if(getX() < 0)
        {
            setX(0);
        }
        else if(getX() + getWidth() > GameView.fractionToX(1))
        {
            setX(GameView.fractionToX(1) - getWidth());
        }
    }

    @Override
    public void pause()
    {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor == accelerometer)
        {
            System.arraycopy(event.values, 0, accelerometerValues, 0, event.values.length);
            accelerometerSet = true;
        }
        else if (event.sensor == magnetometer)
        {
            System.arraycopy(event.values, 0, magnetometerValues, 0, event.values.length);
            magnetometerSet = true;
        }
        if (accelerometerSet && magnetometerSet) {
            SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magnetometerValues);
            SensorManager.getOrientation(rotationMatrix, orientation);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}
