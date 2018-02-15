package com.gym.fitnesszone;

/**
 * Created by sahil on 23/06/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;


public class Welcome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1500);

                } catch (InterruptedException e) {
                    Toast t = Toast.makeText(Welcome.this, "Some error occured", Toast.LENGTH_LONG);
                    t.show();
                } finally {
                    try {
                       Intent it = new Intent(Welcome.this,MainActivity.class);
                        startActivity(it);
                    }catch(Exception e){
                        Toast pp = Toast.makeText(Welcome.this,"Error: "+e.toString(),Toast.LENGTH_LONG );
                        pp.show();
                    }
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


}

