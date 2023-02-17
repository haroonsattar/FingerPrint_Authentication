package com.example.biometricauthentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch switch2;
    private SharedPreferences sharedPreferences;
    private final int BIOMETRIC_ON = 1;
    private final int BIOMETRIC_OFF = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch2 = findViewById(R.id.switch2);

        sharedPreferences = getSharedPreferences(ActivityCheck.SP_NAME,MODE_PRIVATE);
        boolean isLock = sharedPreferences.getBoolean(ActivityCheck.SP_KEY, false);

        switch2.setChecked(isLock);

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setBiometric(isChecked);

            }
        });

        }
    private void setBiometric(boolean isChecked){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            KeyguardManager manager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if (manager.isKeyguardSecure()){
                if (isChecked) {
                    Intent intent = manager.createConfirmDeviceCredentialIntent("Biometric On", "Scan your fingerprint");
                    startActivityForResult(intent, BIOMETRIC_ON);
                }else{
                    Intent intent = manager.createConfirmDeviceCredentialIntent("Biometric Off", "Scan your fingerprint");
                    startActivityForResult(intent, BIOMETRIC_OFF);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String result = null;
            switch (requestCode) {
                case BIOMETRIC_ON:
                    editor.putBoolean(ActivityCheck.SP_KEY,true);
                    editor.apply();
                    result = "On";
                    break;

                case BIOMETRIC_OFF:
                    editor.putBoolean(ActivityCheck.SP_KEY,false);
                    editor.apply();
                    result = "Off";
                    break;
            }
            Toast.makeText(this, "Biometric is " + result, Toast.LENGTH_SHORT).show();
        }
    }
}