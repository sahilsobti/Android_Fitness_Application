package com.gym.fitnesszone;

/**
 * Created by sahil on 23/06/2015.
 */

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
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

public class Tab3 extends AppCompatActivity implements View.OnClickListener{
    EditText name, age, phone;
    private Toolbar toolbar;
    EditText dob, joining;
    Button submit,bjoine,bdob;
    RadioGroup sexGroup;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
    String s1, s2,Pname,Pph,String_age;
    MediaPlayer mp = null;
    int Page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.tab3);
            toolbar = (Toolbar) findViewById(R.id.appbarr);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            initializeVars();
            submit.setOnClickListener(this);
            bjoine.setOnClickListener(this);
            bdob.setOnClickListener(this);
        } catch (Exception e) {
            Toast tt = Toast.makeText(this,
                    e.toString(), Toast.LENGTH_SHORT);
            tt.show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDob:
                mp.start();
                selectDate1();
                break;
            case R.id.bJoin:
                mp.start();
                selectDate2();
                break;
            case R.id.bSubmit:
                try {
                    mp.start();
                    boolean didItWork = true;
                    Pname = name.getText().toString();
                    Pph = phone.getText().toString();
                    String_age = age.getText().toString();
                    Page = Integer.parseInt(String_age);
                    if(Pname.matches("") ){
                        didItWork = false;
                        Toast tt = Toast.makeText(this,
                                "Name field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                    if(String_age.matches("") ){
                        didItWork = false;
                        Toast tt = Toast.makeText(this,
                                "Age field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                    else if (!((Page > 0) && (Page < 100))) {
                        didItWork = false;
                        Toast tt = Toast.makeText(this,
                                "Error: Age is not correct", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                    else if(Pph.matches("")){
                        didItWork = false;
                        Toast tt = Toast.makeText(this,
                                "Phone field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                    return;
                    }
                    else if(dob.getText().toString().matches("") ){
                        didItWork = false;
                        Toast tt = Toast.makeText(this,
                                "Date of birth field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                    else if(joining.getText().toString().matches("") ){
                        didItWork = false;
                        Toast tt = Toast.makeText(this,
                                "Joining field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                    else {
                        try {
                            int selectedId = sexGroup.getCheckedRadioButtonId();
                            String Psex = "";
                            switch (selectedId) {
                                case R.id.radioMale:
                                    Psex = "male";
                                    break;
                                case R.id.radioFemale:
                                    Psex = "female";
                                    break;
                                default: Psex="male";
                                        break;
                            }
                            java.util.Date d = sdf.parse(s2);
                            Calendar calling = Calendar.getInstance();
                            calling.setTime(d);
                            calling.add(calling.MONTH,1);
                            String s3 = sdf.format(calling.getTime());
                            // find payment date
                            MemberDatabase mdd = new MemberDatabase(this);
                            mdd.open();
                            mdd.createEntry(Pname, Page, Psex, Pph, s1, s2, s3, 0);
                            mdd.close();
                        } catch (SQLException e) {
                            didItWork = false;
                            Toast tt = Toast.makeText(this,
                                    "Error: " + e.toString(), Toast.LENGTH_SHORT);
                            tt.show();
                        } finally {
                            if (didItWork) {
                                Toast tt = Toast.makeText(this,
                                        "Member added!", Toast.LENGTH_SHORT);
                                tt.show();
                                setFields();
                            }
                        }
                    }
                } catch (Exception e) {
                    if(Pname.matches("") ){

                        Toast tt = Toast.makeText(this,
                                "Name field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                    else if (String_age.matches("")) {

                        Toast tt = Toast.makeText(this,
                                "Age field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }

                    else if (!((Page > 0) && (Page < 100))) {

                        Toast tt = Toast.makeText(this,
                                "Error: Age is not correct", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                    else if(Pph.matches("")){

                        Toast tt = Toast.makeText(this,
                                "Phone field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                    else if(dob.getText().toString().matches("") ){
                        Toast tt = Toast.makeText(this,
                                "Date of birth field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                    else if(joining.getText().toString().matches("") ){
                        Toast tt = Toast.makeText(this,
                                "Joining field is empty", Toast.LENGTH_SHORT);
                        tt.show();
                        return;
                    }
                }
                break;
        }
    }
    private void setFields(){
        name.setText("");
        age.setText("");
        sexGroup.clearCheck();
        phone.setText("");
        dob.setText("");
        joining.setText("");
    }
    private void initializeVars() {
        name = (EditText) findViewById(R.id.etName);
        age = (EditText) findViewById(R.id.etAge);
        sexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        phone = (EditText) findViewById(R.id.etPhone);
        dob = (EditText) findViewById(R.id.etDob);
        joining = (EditText) findViewById(R.id.etJoin);
        submit = (Button) findViewById(R.id.bSubmit);
        bdob =(Button) findViewById(R.id.bDob);
        bjoine = (Button) findViewById(R.id.bJoin);
        mp = MediaPlayer.create(this, R.raw.sound);
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

        public SelectDateFrag(){

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
    public void onBackPressed() {
        super.onBackPressed();
        
        startActivity(new Intent(Tab3.this, MainActivity.class));
        finish();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(Tab3.this, MainActivity.class));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
