package com.gym.fitnesszone;

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
import android.widget.ListView;
import android.widget.Toast;

;import static com.gym.fitnesszone.R.raw.sound;


/**
 * Created by sahil on 23/06/2015.
 */
public class Tab1 extends Fragment implements
        AlertCustomAdapter.customButtonListener {
    View v;
    ViewGroup c;
    AlertCustomAdapter adapter;
    ListView lv;
    String[] Namess, Sexx, Payy, Rowss;
    MediaPlayer ss = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        c = container;
        v = inflater.inflate(R.layout.tab1, container, false);
        ListView lv = (ListView) v.findViewById(R.id.listV2);
        new GetData().execute();
        return v;
    }

    @Override
    public void onButtonClickListner(int position, String value) {
        boolean itworked = true;
        try {
            ss = MediaPlayer.create(getActivity(), sound);
            ss.start();
            MemberDatabase mp = new MemberDatabase(getActivity());
            mp.open();
            mp.resetPaymentFlag(value);
            mp.close();
        } catch (Exception e) {
            itworked = false;
            Toast pp = Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT);
            pp.show();
        }finally{
            if(itworked){
                Toast pp = Toast.makeText(getActivity(), "Member removed", Toast.LENGTH_SHORT);
                pp.show();
                Intent io = new Intent(getActivity(),MainActivity.class);
                startActivity(io);
            }
        }
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

    class GetData extends AsyncTask<Void, Void, String[]>{
        String[] result;
        @Override
        protected String[] doInBackground(Void... voidz) {
            try {
                MemberDatabase mDD = new MemberDatabase(getActivity());
                mDD.open();
                mDD.setPaymentFlag();
                result = mDD.getalertData();
                mDD.close();
                StringBuilder iRow = new StringBuilder();
                StringBuilder iName = new StringBuilder();
                StringBuilder iSex = new StringBuilder();
                StringBuilder iPay = new StringBuilder();
                for (int i = 0; i < result.length; i++) {
                    String[] arr = result[i].split("&");
                    iRow.append(arr[0].toString() + "&");
                    iName.append(arr[1].toString() + "&");
                    iSex.append(arr[2].toString() + "&");
                    iPay.append(arr[3].toString() + "&");
                }
                Rowss = iRow.toString().split("&");
                Namess = iName.toString().split("&");
                Sexx = iSex.toString().split("&");
                Payy = iPay.toString().split("&");
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
        protected void onPostExecute( String[] result) {
            try {
                if (null != result && result.length != 0) {
                    adapter = new AlertCustomAdapter(getActivity(), Rowss, Sexx, Namess, Payy);
                    adapter.setCustomButtonListner(Tab1.this);
                    lv.setAdapter(adapter);
                }
            } catch (SQLException e) {

                Toast tt = Toast.makeText(getActivity(),
                        "Error: " + e.toString(), Toast.LENGTH_LONG);
                tt.show();
            } catch (Exception e) {
                Toast tt = Toast.makeText(getActivity(),
                        "Error: " + e.toString(), Toast.LENGTH_LONG);
                tt.show();
            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}