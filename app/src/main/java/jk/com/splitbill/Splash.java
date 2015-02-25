package jk.com.splitbill;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;


public class Splash extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();



        //// Timer to open MainMenu
        new Timer().schedule(new TimerTask() {
            public void run() {
                startActivity(new Intent(Splash.this, PersonNumber.class));
            }
        }, 2500 /*amount of time in milliseconds before execution*/);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
