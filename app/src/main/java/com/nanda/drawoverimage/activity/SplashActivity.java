package com.nanda.drawoverimage.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.nanda.drawoverimage.R;
import com.nanda.drawoverimage.base.BaseActivity;
import com.nanda.drawoverimage.utils.UiUtils;

/**
 * Created by nandagopal on 4/24/17.
 */

public class SplashActivity extends BaseActivity {

    private static final int REQUEST_PERMISSION_CODE = 1234;
    private String mPermission[] = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            readPermission();
        } else {
            permissionGranted();
        }
    }

    private void readPermission() {
        boolean allPermissionsGranted = true;
        for (int i = 0, mPermissionLength = mPermission.length; i < mPermissionLength; i++) {
            String permission = mPermission[i];
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, mPermission, REQUEST_PERMISSION_CODE);
        } else {
            permissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            boolean allPermissionGranted = true;
            if (grantResults.length == permissions.length) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = false;
                        break;
                    }
                }
            } else {
                allPermissionGranted = false;
            }

            if (allPermissionGranted) {
                permissionGranted();
            } else {
                permissionDenied();
            }
        }
    }

    private void navigateToHome() {
        new Handler().postDelayed(runnable, 1000);
    }

    private void permissionGranted() {
        navigateToHome();
    }

    private void permissionDenied() {
        UiUtils.showToast(getApplicationContext(), "Permission Denied");
    }
}
