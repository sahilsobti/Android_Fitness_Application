package com.gym.fitnesszone;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by sahil on 23/06/2015.
 */
public class MainActivity extends ActionBarActivity {
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    private Toolbar toolbar;
    CharSequence Titles[] = {"Alerts", "Members"};
    int Numboftabs = 2;
    ViewPager pager;
    MediaPlayer ss = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        toolbar =(Toolbar) findViewById(R.id.appbarr);
        setSupportActionBar( toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        try {
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
            pager = (ViewPager) findViewById(R.id.pa);
            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
            pager.setAdapter(adapter);
            tabs.setDistributeEvenly(true);
            tabs.setViewPager(pager);
            ss = MediaPlayer.create(this, R.raw.sound);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SendingAlert.class);
            startService(intent);
        } catch (Exception e) {
            Toast ttt = Toast.makeText(this, "Error : " + e.toString(), Toast.LENGTH_LONG);
            ttt.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ss != null) {
            ss.release();
            ss = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.iiadd:
                try {
                    Intent ii =  new Intent(MainActivity.this,Tab3.class);
                    startActivity(ii);
                } catch (Exception e) {
                    Toast ttt = Toast.makeText(this, "Error : " + e.toString(), Toast.LENGTH_LONG);
                    ttt.show();
                }
                break;
        }
        return true;
    }
}
