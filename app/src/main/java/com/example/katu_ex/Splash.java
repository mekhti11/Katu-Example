package com.example.katu_ex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class Splash extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TEXT = "text";
    private static int TIME_OUT = 3000;
    private AlertDialog.Builder builder;
    private AlertDialog alert;
    private FirebaseRemoteConfig mRemoteConfig;
    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        text = findViewById(R.id.textView);

        if(isConnectedToNetwork()){
            getRemoteConfData();
        }else{
            noInternetAlert();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) {
                getRemoteConfData();
                if (alert!=null && alert.isShowing()){
                    alert.dismiss();
                }
            }
            else {
                if (alert!=null &&!alert.isShowing()){
                    noInternetAlert();
                }
            }
        }
    };

    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }

    private void noInternetAlert(){
        //No Internet
        builder = new AlertDialog.Builder(Splash.this);
        builder.setTitle("KATU");
        builder.setMessage("No Internet Connection!!!");
        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.dismiss();
            }
        });
        alert = builder.create();
        alert.show();
    }

    private void getRemoteConfData(){

        long cacheExpiration = 3600;
        //FirebaseRemoteConfig sınıfı nesnelerini kullanmamızı sağlayan metod
        mRemoteConfig = FirebaseRemoteConfig.getInstance();


        //Bir Remote Config Settings olusturup developer modunu aktif hale getiriyoruz.
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        mRemoteConfig.setConfigSettings(configSettings);

        //Hazırlanan key-value ları set ediyoruz
        mRemoteConfig.setDefaults(R.xml.remote_config_defaults);



        if (mRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "Fetch succeeded.");
                            mRemoteConfig.activateFetched();

                            String textLabel = mRemoteConfig.getString(TEXT);
                            Log.d(TAG, "textLabel: " + textLabel);
                            text.setText(textLabel);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(Splash.this, Anasayfa.class);
                                    startActivity(i);
                                    finish();
                                }
                            }, TIME_OUT);
                        }
                        else {
                            Toast.makeText(Splash.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}