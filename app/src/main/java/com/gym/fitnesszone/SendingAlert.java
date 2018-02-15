package com.gym.fitnesszone;

import android.app.IntentService;
import android.content.Intent;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URLEncoder;


/**
 * Created by sahil on 30/06/2015.
 */
public class SendingAlert extends IntentService {

    private static final String TAG = "AlertService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SendingAlert(String name) {
        super(SendingAlert.class.getName());
    }

    public SendingAlert() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MemberDatabase mDD = new MemberDatabase(this);
        try {
            mDD.open();
            mDD.setPaymentFlag();
            String[] result = mDD.sendalertData();
            if(result.length > 0){
            StringBuilder iRow = new StringBuilder();
            StringBuilder iPhone = new StringBuilder();
            StringBuilder iName = new StringBuilder();
            StringBuilder iPay = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                String[] arr = result[i].split("&");
                iRow.append(arr[0].toString() + "&");
                iName.append(arr[1].toString() + "&");
                iPhone.append(arr[2].toString() + "&");
                iPay.append(arr[3].toString() + "&");
            }
            String[] Rowss = iRow.toString().split("&");
            String[] namess = iName.toString().split("&");
            String[] phones = iPhone.toString().split("&");
           // String[] pay_date = iPay.toString().split("&");
            System.out.println("namess len is: " + namess.length);
            String message = "Dear,\nApka rent iss mahina ka aaj due ho gaya hai.jaldi se jaldi dene ki koshish kare.\nThanks\n" +
                        "Sobti(property Owner)";
            String userName = "9811367135";
            String password = "189apocketb";
            int flag = 0;
            for (int i = 0; i < namess.length; i++) {
                try {
                    flag = 0;
                    namess[i] = namess[i].replace(' ', '+');
                    // Creating HTTP client
                    HttpClient httpClient = new DefaultHttpClient();
                    // Creating HTTP Post
                    HttpGet httpget = new HttpGet("https://sms-sending.herokuapp.com/?numb=" +
                            URLEncoder.encode(phones[i], "UTF-8")+ "&message="+ URLEncoder.encode(message, "UTF-8")+
                            "&userName="+ URLEncoder.encode(userName, "UTF-8")+"&passwrd="+ URLEncoder.encode(password, "UTF-8"));
                    HttpResponse response = httpClient.execute(httpget);
                    System.out.println("response is : " + response);
                } catch (Exception e) {
                    flag = 1;
                    System.out.println("Error in sending alerts to the mobile due to " + e.toString());
                } finally {
                    if (flag == 0) {
                        mDD.resetMessageFlag(Rowss[i]);
                    }
                }
            }

        }
        } catch (Exception e) {
            System.out.println("Error in sending alerts to the mobile due to " + e.toString());
        } finally {
            mDD.close();
        }
    }
}