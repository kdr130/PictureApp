package com.example.kevin.pictureapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    public static final String TAG = "PictureAPP";
    public static final int REQUEST_READ_STORAGE = 3;

    private SimpleCursorAdapter adapter;

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

        GridView gridView = (GridView) findViewById(R.id.grid);
        String[] from = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Media.DISPLAY_NAME};
        int[] to = new int[] {R.id.thumb_image, R.id.thumb_text};

        adapter = new SimpleCursorAdapter(getBaseContext(), R.layout.thumb_item, null, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    // 剛建立 loader 會 trigger 此方法
    // 主要是做查詢的動作，當查詢完畢 return 後會繼續執行 onLoadFinished
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor o) {
        // 因為在 readThumbnails() 中 gridView 使用的 adapter 內的 cursor 初始化為 null,
        // 所以載入好後要替換掉 adapter 的 cursor
        adapter.swapCursor(o);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // i 是 position, 是圖片在 External_Storage_Media 的順序
        Log.d(TAG, "onItemClick: i: " + i + ", l: " + l);
        
        
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("position", i);
        startActivity(intent);
    }
}
