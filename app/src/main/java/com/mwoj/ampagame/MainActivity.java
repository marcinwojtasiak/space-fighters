package com.mwoj.ampagame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

        changeHelpVisibility(); // first change is to invisible

        final Intent intentStartGame = new Intent(this, GameActivity.class);
        ImageButton startGame = findViewById(R.id.start);
        startGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(intentStartGame);
            }
        });

        ImageButton help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeHelpVisibility();
            }
        });

        ImageButton exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finishAffinity();
            }
        });
    }

    private void changeHelpVisibility()
    {
        FragmentManager fm = getSupportFragmentManager();
        HelpFragment helpFragment = (HelpFragment) fm.findFragmentById(R.id.help_fragment);
        if (helpFragment != null)
        {
            if (helpFragment.isVisible)
            {
                fm.beginTransaction().hide(helpFragment).commit();
                helpFragment.isVisible = false;
            }
            else
            {
                fm.beginTransaction().show(helpFragment).commit();
                helpFragment.isVisible = true;
            }
        }
    }
}