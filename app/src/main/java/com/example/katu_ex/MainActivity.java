package com.example.katu_ex;

import android.graphics.Color;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    //parametre anahtarları
    private static final String TEXT = "text";
    private static final String COLOR = "color";

    FirebaseRemoteConfig mRemoteConfig;
    View container;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (View) findViewById(R.id.container);
        text = (TextView) findViewById(R.id.textView);

        //FirebaseRemoteConfig sınıfı nesnelerini kullanmamızı sağlayan metod
        mRemoteConfig = FirebaseRemoteConfig.getInstance();


        //Bir Remote Config Settings olusturup developer modunu aktif hale getiriyoruz.
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        mRemoteConfig.setConfigSettings(configSettings);

        //Hazırlanan key-value ları set ediyoruz
        mRemoteConfig.setDefaults(R.xml.remote_config_defaults);


        fetch();
    }
    private void fetch() {

        long cacheExpiration = 3600;

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

                            String colorName = mRemoteConfig.getString(COLOR);
                            Log.d(TAG, "colorName: " + colorName);
                            container.setBackgroundColor(Color.parseColor(colorName));

                            String textLabel = mRemoteConfig.getString(TEXT);
                            Log.d(TAG, "textLabel: " + textLabel);
                            text.setText(textLabel);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
}