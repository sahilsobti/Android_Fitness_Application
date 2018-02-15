package com.gym.fitnesszone;
/**
 * Created by sahil on 23/06/2015.
 */

import android.content.Intent;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by sahil on 23/06/2015.
 */
public class Tab2 extends Fragment {
    ListView lv;
    String[] Namess, Sexx;
    ListAdapter adapter;
    String[] Rowss;
    View v;
    MediaPlayer ss = null;
    ViewGroup c;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        c =container;
        ss = MediaPlayer.create(getActivity(), R.raw.sound);
        v = inflater.inflate(R.layout.tab2,container,false);
        lv =(ListView) v.findViewById(R.id.listView);
        progressBar =(ProgressBar) v.findViewById(R.id.loading);
        new GetNamesData().execute();
        return v;
    }

    @Override
    public void onResume() {
        new GetNamesData().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                long Rowidd = 0;
                try {
                    ss.start();
                    Intent ti = new Intent("com.gym.fitnesszone.SHOWMEMBER");
                    Rowidd = Integer.parseInt(Rowss[position]);
                    ti.putExtra("RowKey", Rowidd);
                    startActivity(ti);
                    
                } catch (Exception e) {
                    Toast tt = Toast.makeText(getActivity(),
                            "Error: " + e.toString() + "position is :"+ position + "and no is " , Toast.LENGTH_LONG);
                    tt.show();
                }
            }
        });
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ss != null) {
            ss.release();
            ss = null;
        }
    }

    class GetNamesData extends AsyncTask<Void,Void,String[]>{

        @Override
        protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            String[] result= null;
            try{
            MemberDatabase mDD = new MemberDatabase(getActivity());
            mDD.open();
            result = mDD.getNames();
            mDD.close();
            StringBuilder iRow = new StringBuilder();
            StringBuilder iName = new StringBuilder();
            StringBuilder iSex = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                String[] arr = result[i].split("&");
                iRow.append(arr[0].toString() + "&");
                iName.append(arr[1].toString() + "&");
                iSex.append(arr[2].toString() + "&");
            }
            Rowss = iRow.toString().split("&");
            Namess = iName.toString().split("&");
            Sexx = iSex.toString().split("&");
        }catch (SQLException e) {

             Toast tt = Toast.makeText(getActivity(),
                     "Error: " + e.toString(), Toast.LENGTH_LONG);
             tt.show();
         } catch (Exception e) {
             Toast tt = Toast.makeText(getActivity(),
                     "Error: " + e.toString(), Toast.LENGTH_LONG);
             tt.show();
         }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            try {
                if (null != result && result.length != 0) {
                    adapter = new CustomAdapter(getActivity(), Sexx, Namess);
                    lv.setAdapter(adapter);
                } else {
                    Toast ttr = Toast.makeText(getActivity(),
                            "No member is added yet!", Toast.LENGTH_SHORT);
                    ttr.show();
                }
            } catch (SQLException e) {

                Toast tt = Toast.makeText(getActivity(),
                        "Error: " + e.toString(), Toast.LENGTH_LONG);
                tt.show();
            } catch (Exception e) {
                Toast tt = Toast.makeText(getActivity(),
                        "Error: " + e.toString(), Toast.LENGTH_LONG);
                tt.show();
            }finally {
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
