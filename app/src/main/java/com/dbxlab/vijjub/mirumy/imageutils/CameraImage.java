package com.dbxlab.vijjub.mirumy.imageutils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vijjub on 9/2/16.
 */
public class CameraImage {

    final String TAG = this.getClass().getSimpleName();
    private String photoPath;
    private Context context;

    public CameraImage(Context context) {
        this.context = context;
    }

    public String getPhotoPath() {
        return this.photoPath;
    }

    public Intent takePhotoIntent() throws IOException {
        Intent in = new Intent("android.media.action.IMAGE_CAPTURE");
        if(in.resolveActivity(this.context.getPackageManager()) != null) {
            File photoFile = this.createImageFile();
            if(photoFile != null) {
                in.putExtra("output", Uri.fromFile(photoFile));
            }
        }

        return in;
    }

    private File createImageFile() throws IOException {
        String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        this.photoPath = image.getAbsolutePath();
        return image;
    }

    public void addToGallery() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(this.photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.context.sendBroadcast(mediaScanIntent);
    }
}
