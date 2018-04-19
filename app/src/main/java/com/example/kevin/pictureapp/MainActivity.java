package com.example.kevin.pictureapp;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "PictureAPP";
    public static final int REQUEST_READ_STORAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 尚未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
        } else {
            // 已經有權限
            readThumbnails();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_READ_STORAGE:
                Log.d(TAG, "onRequestPermissionsResult: requestCode: REQUEST_READ_STORAGE");

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 取得外部存取權限，讀取縮圖
                    readThumbnails();
                } else {
                    // 使用者拒絕授權權限，開啟 alertDialog
                    new AlertDialog.Builder(this)
                            .setMessage("必須允許讀取外部儲存權限，才能顯示圖檔")
                            .setPositiveButton("OK", null)
                            .show();
                }
                break;
            default:
                break;
        }
    }

    private void readThumbnails() {
        Log.d(TAG, "readThumbnails");

    }
}
