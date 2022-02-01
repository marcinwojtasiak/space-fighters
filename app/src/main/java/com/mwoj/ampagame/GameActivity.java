package com.mwoj.ampagame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


public class GameActivity extends AppCompatActivity
{
    private GameView gameView;
    private boolean paused = false;
    private boolean helpOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        gameView = new GameView(this);
        setContentView(gameView);

        setContentView(R.layout.gameplay);

        gameView = new GameView(this);
        LinearLayout layout = findViewById(R.id.gameplay);
        layout.addView(gameView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        changeHelpVisibility(); // first change is to invisible
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gameView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gameView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.back:
                finish();
                return true;
            case R.id.help_icon:
                changePause();
                changeHelpVisibility();
                return true;
            case R.id.pause:
                changePause();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeHelpVisibility()
    {
        FragmentManager fm = getSupportFragmentManager();
        HelpFragment helpFragment = (HelpFragment) fm.findFragmentById(R.id.help_fragment_game);
        if (helpFragment != null)
        {
            if (helpFragment.isVisible)
            {
                fm.beginTransaction().hide(helpFragment).commit();
                helpFragment.isVisible = false;
                helpOpened = false;
            }
            else
            {
                fm.beginTransaction().show(helpFragment).commit();
                helpFragment.isVisible = true;
                helpOpened = true;
            }
        }
    }

    private void changePause()
    {
        if (helpOpened)
            return;
        if (paused)
        {
            gameView.onResume();
            paused = false;
        }
        else
        {
            gameView.onPause();
            paused = true;
        }
    }
}