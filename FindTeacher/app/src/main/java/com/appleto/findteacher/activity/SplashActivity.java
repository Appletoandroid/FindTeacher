package com.appleto.findteacher.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.appleto.findteacher.R;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private Context context;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;
        storage = new Storage(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
                gotoNext();
            }
        } else {
            gotoNext();
        }
    }

    private boolean checkPermissions() {
        int coarseLocPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int finelocPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (coarseLocPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (finelocPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (permissions.length > 0) {
                    if ((permissions.length == 3 && (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED)) ||
                            (permissions.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) ||
                            (permissions.length == 1 && (grantResults[0] == PackageManager.PERMISSION_GRANTED))) {

                        gotoNext();
                    } else {
                        checkRemainPermission();
                    }
                } else {
                    checkRemainPermission();
                }
                return;
        }
    }

    private void checkRemainPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Utils.showToast(context, "Please Allow Required Permission For Access Application");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.cancelToast();
                    checkPermissions();
                }
            }, 1800);
        } else {
            Utils.showToast(context, "Go To Application Setting And Enable Required Permissions");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.cancelToast();
                    showAppSetupScreen();
                }
            }, 1800);
        }
    }

    private void showAppSetupScreen() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 100);
    }

    private void gotoNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(storage.getLogInState()) {
                    Utils.navigateTo(SplashActivity.this, MainActivity.class);
                } else {
                    Utils.navigateTo(SplashActivity.this, StartActivity.class);
                }
            }
        }, 2500);
    }

    @Override
    protected void onStop() {
        Utils.cancelToast();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Utils.cancelToast();
        super.onDestroy();
    }
}
