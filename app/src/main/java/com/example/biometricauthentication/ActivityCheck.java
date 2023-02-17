package com.example.biometricauthentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;

import java.security.Key;

public class ActivityCheck extends AppCompatActivity {


    public static final String SP_NAME = "lock";
    public static final String SP_KEY = "lock_key";
    private final int CHECK_BIOMETRIC_KEY = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_check);

        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME,MODE_PRIVATE);
        boolean isLock = sharedPreferences.getBoolean(SP_KEY, false);
        checkBiometric(isLock);

    }

    private void checkBiometric(boolean isLock){
        if (isLock) {
            startActivity(new Intent(ActivityCheck.this, MainActivity.class));
            finish();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                KeyguardManager manager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                Intent intent = manager.createConfirmDeviceCredentialIntent("Unlock App", "Scan your fingerprint");
                startActivityForResult(intent, CHECK_BIOMETRIC_KEY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECK_BIOMETRIC_KEY && resultCode == RESULT_OK){
            startActivity(new Intent(ActivityCheck.this,MainActivity.class));
            finish();
        }
    }
}