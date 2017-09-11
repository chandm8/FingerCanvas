package com.example.chandler.fingercanvas;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    MyCanvas myCanvas;
    TouchHandler touchHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCanvas = (MyCanvas) findViewById(R.id.myCanvas);
        
        touchHandler = new TouchHandler(this);
        myCanvas.setOnTouchListener(touchHandler);
    }

    public void addPath (int key, float x, float y) {
        myCanvas.addPath(key, x, y);
    }
    public void updatePath (int key, float x, float y) {
        myCanvas.updatePath(key, x, y);
    }
    public void removePath (int key) {
        myCanvas.removePath(key);
    }

    Random random = new Random();

    public void onDoubleTap() {
        myCanvas.setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
    }

    static final int REQUEST_CODE = 1;

    private Uri imgUri;

    public void onLongPress() {
        try {
            File storageDir = this.getFilesDir();
            File outputFile = File.createTempFile("" + Calendar.getInstance().getTimeInMillis(), "jpg", storageDir);
            imgUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", outputFile);
            Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            if (takePicIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePicIntent, REQUEST_CODE);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            this.getContentResolver().notifyChange(imgUri, null);
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, imgUri);
                myCanvas.setBackground(new BitmapDrawable(getResources(), bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Bundle extra = data.getExtras();
//            Bitmap thumbnail = (Bitmap) extra.get("data");
//            myCanvas.setBackground(new BitmapDrawable(getResources(), thumbnail));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
