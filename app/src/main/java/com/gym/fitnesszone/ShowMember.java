package com.gym.fitnesszone;

/**
 * Created by sahil on 23/06/2015.
 */

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowMember extends AppCompatActivity implements OnClickListener {
    private Toolbar toolbar;
    TextView name, age, sex, phone, dob, joining;
    ImageView im;
    MediaPlayer ss = null;
    long Rkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showmember);
        toolbar = (Toolbar) findViewById(R.id.appbarr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        try {
            Bundle extras = getIntent().getExtras();
            if (null != extras) {
                Rkey = extras.getLong("RowKey");
            }
            MemberDatabase md = new MemberDatabase(this);
            md.open();
            String result = md.getData(Rkey);
            String[] r = result.split("&");
            name = (TextView) findViewById(R.id.PName);
            age = (TextView) findViewById(R.id.PAge);
            sex = (TextView) findViewById(R.id.PSex);
            phone = (TextView) findViewById(R.id.PPhone);
            dob = (TextView) findViewById(R.id.PDob);
            joining = (TextView) findViewById(R.id.PJoining);
            im = (ImageView) findViewById(R.id.userImage);
            if ((r[2].toLowerCase()).equals("male")) {
                im.setImageResource(R.drawable.man2);
            } else if ((r[2].toLowerCase()).equals("female")) {
                im.setImageResource(R.drawable.wom);
            }
            name.setText(r[0]);
            age.setText(r[1]);
            sex.setText(r[2]);
            phone.setText(r[3]);
            dob.setText(r[4]);
            joining.setText(r[5]);
            md.close();
            Button edit = (Button) findViewById(R.id.PEdit);
            Button delete = (Button) findViewById(R.id.PDelete);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
            ss = MediaPlayer.create(this, R.raw.sound);
        } catch (Exception e) {
            Toast tt = Toast.makeText(this, "Error :" + e.toString(), Toast.LENGTH_LONG);
            tt.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PEdit:
                ss.start();
                Intent popp = new Intent("com.gym.fitnesszone.EDITCONTACT");
                popp.putExtra("RowK", Rkey);
                startActivity(popp);
                break;
            case R.id.PDelete:
                ss.start();
                MemberDatabase md = new MemberDatabase(this);
                md.open();
                int r = md.deleteContact(Rkey);
                md.close();
                if (r == 1) {
                    Toast tt = Toast.makeText(this, "Member Successfully deleted", Toast.LENGTH_SHORT);
                    tt.show();
                    Intent ip = new Intent(ShowMember.this,MainActivity.class);
                    startActivity(ip);
                    finish();
                } else {
                    Toast ttt = Toast.makeText(this, "Deletion failed due to database error", Toast.LENGTH_SHORT);
                    ttt.show();
                }
                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShowMember.this, MainActivity.class));
        finish();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(ShowMember.this, MainActivity.class));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
