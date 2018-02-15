package com.gym.fitnesszone;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sahil on 24/06/2015.
 */
public class EditContact extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    EditText name, age, phone;
    EditText dob, joining;
    Button submit, bjoine, bdob;
    RadioGroup sexGroup;
    ImageButton back;
    long Rkey;
    String s1, s2, s3;
    MediaPlayer ss = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3);
        toolbar = (Toolbar) findViewById(R.id.appbarr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        try {
            initializeVars();
            Bundle extras = getIntent().getExtras();
            if (null != extras) {
                Rkey = extras.getLong("RowK");
            }
            MemberDatabase md = new MemberDatabase(this);
            md.open();
            String result = md.getData(Rkey);
            String[] r = result.split("&");
            name.setText(r[0]);
            age.setText(r[1]);
            dob.setText(r[4]);
            joining.setText(r[5]);
            if ((r[2].trim().toLowerCase()).equals("male")) {
                sexGroup.check(R.id.radioMale);
            } else if ((r[2].trim().toLowerCase()).equals("female")) {
                sexGroup.check(R.id.radioFemale);
            }
            phone.setText(r[3]);
            md.close();
            submit.setOnClickListener(this);
            back.setOnClickListener(this);
            bjoine.setOnClickListener(this);
            bdob.setOnClickListener(this);
        } catch (Exception e) {
            Toast tt = Toast.makeText(this,
                    e.toString(), Toast.LENGTH_SHORT);
            tt.show();
        }
    }

    public void initializeVars() {

        name = (EditText) findViewById(R.id.etName);
        age = (EditText) findViewById(R.id.etAge);
        sexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        phone = (EditText) findViewById(R.id.etPhone);
        dob = (EditText) findViewById(R.id.etDob);
        joining = (EditText) findViewById(R.id.etJoin);
        submit = (Button) findViewById(R.id.bSubmit);
        bdob = (Button) findViewById(R.id.bDob);
        bjoine = (Button) findViewById(R.id.bJoin);
        ss = MediaPlayer.create(this, R.raw.sound);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDob:
                ss.start();
                selectDate1();
                break;
            case R.id.bJoin:
                ss.start();
                selectDate2();
                break;
            case R.id.bSubmit:
                try {
                    ss.start();
                    String Pname = name.getText().toString();
                    String Pph = phone.getText().toString();
                    String String_age = age.getText().toString();
                    String Psex = "";
                    int Page = Integer.parseInt(String_age);
                    if (!((Page > 0) && (Page < 100))) {
                        System.out.println("If statemnt");
                        Toast tt = Toast.makeText(this,
                                "Error: Age is not correct", Toast.LENGTH_SHORT);
                        tt.show();
                    } else {
                        boolean didItWork = true;
                        try {
                            int selectedId = sexGroup.getCheckedRadioButtonId();

                            switch (selectedId) {
                                case R.id.radioMale:
                                    Psex = "male";
                                    break;
                                case R.id.radioFemale:
                                    Psex = "female";
                                    break;
                            }
                            s1 = dob.getText().toString();
                            s2 = joining.getText().toString();
                            java.util.Date d = sdf.parse(s2);
                            Calendar calling = Calendar.getInstance();
                            calling.setTime(d);
                            calling.add(calling.MONTH, 1);
                            s3 = sdf.format(calling.getTime());
                            MemberDatabase mdd = new MemberDatabase(this);
                            mdd.open();
                            mdd.modifyEntry(Rkey, Pname, Page, Psex, Pph, s1, s2, s3, 0);
                            mdd.close();

                        } catch (Exception e) {
                            didItWork = false;
                            Toast tt = Toast.makeText(this,
                                    "result is " + Rkey + " " + Pname + " " + Page + " " + Psex + " " + Pph + " "
                                            + s1 + " " + s2 + " " + s3 + " ", Toast.LENGTH_LONG);
                            tt.show();
                        } finally {
                            if (didItWork) {
                                Toast tt = Toast.makeText(this,
                                        "Member details modified!", Toast.LENGTH_SHORT);
                                tt.show();
                            }
                            Intent it = new Intent(EditContact.this,MainActivity.class);
                            startActivity(it);
                            finish();

                        }
                    }
                } catch (Exception e) {
                    Toast tt = Toast.makeText(this,
                            e.toString(), Toast.LENGTH_SHORT);
                    tt.show();
                }
                break;
        }
    }

    public void selectDate1() {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    public void selectDate2() {
        DialogFragment newFrag = new SelectDateFrag();
        newFrag.show(getFragmentManager(), "DatePicker");
    }

    public void populateSetDate2(int year, int month, int day) {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(year, month, day);
        s2 = sdf.format(cal1.getTime());
        joining.setText(s2);
    }

    public void populateSetDate1(int year, int month, int day) {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(year, month, day);
        s1 = sdf.format(cal1.getTime());
        dob.setText(s1);
    }

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }


        @Override
        public void onDateSet(DatePicker view, int year, int mm, int dd) {
            populateSetDate1(year, mm, dd);
        }
    }

    @SuppressLint("ValidFragment")
    public class SelectDateFrag extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public SelectDateFrag() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }


        @Override
        public void onDateSet(DatePicker view, int year, int mm, int dd) {
            populateSetDate2(year, mm, dd);
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
        startActivity(new Intent(EditContact.this, MainActivity.class));
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(EditContact.this, MainActivity.class));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
