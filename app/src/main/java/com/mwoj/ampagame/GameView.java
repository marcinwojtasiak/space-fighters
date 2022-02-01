package com.mwoj.ampagame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;

import com.mwoj.ampagame.entities.Bullet;
import com.mwoj.ampagame.entities.EntityManager;
import com.mwoj.ampagame.entities.Player;
import com.mwoj.ampagame.entities.enemies.BasicEnemy;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameView extends SurfaceView implements Runnable
{
    private boolean isRunning;
    private boolean isPaused;
    private boolean hasStarted;
    private boolean isGameOver;
    private final Object pauseLock = new Object();
    private GameActivity activity;

    private Paint paint;

    private Background background;
    private EntityManager entityManager;
    private Player player;
    private ArrayList<BasicEnemy> enemies;
    private ArrayList<Bullet> bullets;
    private Timer spawnEnemy = new Timer();

    private static int score;

    private static int width;
    private static int height;

    private static Random random = new Random();
    private static int lastSpawnX = -1000;

    private static final int spawnY = -40;
    private static final long spawnInterval = 400L;
    private static final int bulletSpacing = 20;
    private static final int shootingCooldown = 1000;
    private long lastShotTime = 0;

    public GameView(Context context)
    {
        super(context);
        init((GameActivity) context);
    }

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init((GameActivity) context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init((GameActivity) context);
    }

    private void init(GameActivity activity)
    {
        this.activity = activity;
        paint = new Paint();
        paint.setTextSize(64);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        score = 0;
        entityManager = new EntityManager();

        gameStart();
    }

    private void gameStart()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;

        background = new Background(activity, width, height);

        //spawn player
        player = new Player(activity);
        entityManager.registerEntity(player);
        player.setPosition(fractionToX(0.5f), fractionToY(1f) - player.getHeight());

        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long now = System.currentTimeMillis();
                if (now - GameView.this.lastShotTime < GameView.shootingCooldown)
                    return;

                Bullet bulletL = new Bullet(getContext());
                bullets.add(bulletL);
                entityManager.registerEntity(bulletL);
                bulletL.setPosition(player.getX() + player.getWidth()/2 - bulletL.getWidth()/2 - bulletSpacing, player.getY());

                Bullet bulletR = new Bullet(getContext());
                bullets.add(bulletR);
                entityManager.registerEntity(bulletR);
                bulletR.setPosition(player.getX() + player.getWidth()/2 - bulletR.getWidth()/2 + bulletSpacing, player.getY());

                GameView.this.lastShotTime = now;
            }
        });

        hasStarted = true;
        isRunning = true;
        isPaused = false;
        Thread gameLoop = new Thread(this);
        gameLoop.start();
    }

    @Override
    public void run()
    {
        startSpawningEnemies();
        while (isRunning && hasStarted)
        {
            // pause check
            synchronized (pauseLock)
            {
                if(!isRunning)
                    break;
                if(isPaused)
                {
                    try {
                        synchronized (pauseLock)
                        {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException ex){
                        break;
                    }
                    if(!isRunning)
                        break;
                }
            }

            // on each frame
            update();
            draw();
            try {
                Thread.sleep(17); // run at approximately 60 fps
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopSpawningEnemies();
    }

    private void update()
    {
        entityManager.updateEntities();

        // collisions
        for (int i=0; i<enemies.size(); i++)
        {
            if (enemies.get(i).collides(player))
            {
                entityManager.unregisterEntity(player);
                gameOver();
                return;
            }
            for (int j=0; j<bullets.size(); j++)
            {
                if (enemies.get(i).collides(bullets.get(j)))
                {
                    entityManager.unregisterEntity(bullets.get(j));
                    bullets.remove(j);

                    entityManager.unregisterEntity(enemies.get(i));
                    enemies.remove(i--);

                    score++;
                    break;
                }
            }
        }
    }

    private void draw()
    {
        if (getHolder().getSurface().isValid())
        {
            Canvas canvas = getHolder().lockCanvas();

            // draw background
            canvas.drawBitmap(background.getSprite(), 0, 0, paint);

            // draw score
            canvas.drawText(Integer.toString(score), 100, 100, paint);

            entityManager.drawEntities(canvas, paint);

            getHolder().unlockCanvasAndPost(canvas);

            if (isGameOver)
            {
                isRunning = false;

                activity.finish();
                Intent intent = new Intent(activity, GameOverActivity.class);
                intent.putExtra("score", score);
                activity.startActivity(intent);
            }
        }
    }

    private void startSpawningEnemies()
    {
        spawnEnemy.schedule(new TimerTask() // spawn enemy task
        {
            @Override
            public void run()
            {
                BasicEnemy newEnemy = new BasicEnemy(activity);
                enemies.add(newEnemy);
                entityManager.registerEntity(newEnemy);
                int newX = fractionToX(random.nextFloat());
                while (Math.abs(lastSpawnX - newX) <= newEnemy.getWidth() || newX + newEnemy.getWidth() > width)
                {
                    newX = fractionToX(random.nextFloat());
                }
                newEnemy.setPosition(newX, spawnY);
                lastSpawnX = newX;
            }
        }, 0L, spawnInterval);
    }

    private void stopSpawningEnemies()
    {
        spawnEnemy.cancel();
        spawnEnemy = new Timer();
    }

    public void onPause()
    {
        stopSpawningEnemies();
        entityManager.pause();
        isPaused = true;
    }

    public void onResume()
    {
        entityManager.resume();
        startSpawningEnemies();
        synchronized (pauseLock)
        {
            isPaused = false;
            pauseLock.notifyAll();
        }
    }

    public void gameOver()
    {
         isGameOver = true;
    }

    public static int fractionToX(float x)
    {
        return Math.round(x * width);
    }

    public static int fractionToY(float y)
    {
        return Math.round(y * height);
    }
}
