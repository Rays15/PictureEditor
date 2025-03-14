package com.vachel.editing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vachel.editor.EmojiPicEditActivity;
import com.vachel.editor.PictureEditActivity;
import com.vachel.editor.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyTestImageToLocalIfNeed();
        findViewById(R.id.edit_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    editImage();
                }
            }
        });
        checkPermission();
    }

    public void copyTestImageToLocalIfNeed() {
        String directory = getCacheDir().getAbsolutePath();
        final File file = new File(directory + File.separator + "test_image.jpg");
        if (file.exists() && file.length() > 0) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = getAssets().open("test_image.jpg");
                    outputStream = new FileOutputStream(file);
                    Utils.copyStream(inputStream, outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Utils.closeAll(inputStream, outputStream);
                }
            }
        }.start();
    }


    private boolean checkPermission() {
        int hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }

    private void editImage() {
        String directory = getCacheDir().getAbsolutePath();
        final File file = new File(directory + File.separator + "test_image.jpg");
        Uri uri = Uri.fromFile(file);
        Intent editIntent = new Intent(this, EmojiPicEditActivity.class);
//        Intent editIntent = new Intent(this, MyPicEditActivity.class);
        editIntent.putExtra(PictureEditActivity.EXTRA_IMAGE_URI, uri);
        editIntent.putExtra(PictureEditActivity.EXTRA_SAVE_PATH, directory + File.separator + "img_" + System.currentTimeMillis() + ".jpg");
        startActivityForResult(editIntent, 201);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("MainActivity", "onActivityResult: requestCode:" + requestCode + " resultCode:" + resultCode);
        if (requestCode == 201 && resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra(PictureEditActivity.RESULT_IMAGE_SAVE_PATH);
            Log.i("MainActivity", "onActivityResult: path:" + path);
            Toast.makeText(this, "保存到：" + path, Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}