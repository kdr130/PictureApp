package com.example.kevin.pictureapp;

import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    public static final String TAG = "PictureAPP";
    
    private int position;
    private ImageView imageView;
    private Cursor cursor;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        gestureDetector = new GestureDetector(this, this);

        position = getIntent().getIntExtra("position", -1);

        imageView = (ImageView) findViewById(R.id.imageView);

        // set cursorLoader to load picture
        CursorLoader loader = new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        // 要求以背景方法查詢
        cursor = loader.loadInBackground();
        cursor.moveToPosition(position);
        updateImage();
    }

    private void updateImage() {
        String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Log.d(TAG, "onDown: ");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.d(TAG, "onShowPress: ");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.d(TAG, "onSingleTapUp: ");
        
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onScroll: ");
        
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.d(TAG, "onLongPress: ");

    }

    // e1 滑動起始點, e2 滑動結束點
    // vx 橫向移動速度, xy 直向移動速度
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
        Log.d(TAG, "onFling: fling distance: " + (e2.getX() - e1.getX()) );

        float distance = e2.getX() - e1.getX();


        if (distance > 100) {
            // 向右滑動，往前一張圖
            if (!cursor.moveToPrevious()) {
                cursor.moveToLast();
            }
            updateImage();
        } else if (distance < -100) {
            // 向左滑動，往後一張圖
            if (!cursor.moveToNext()) {
                cursor.moveToFirst();
            }
            updateImage();
        }

        return false;
    }
}
