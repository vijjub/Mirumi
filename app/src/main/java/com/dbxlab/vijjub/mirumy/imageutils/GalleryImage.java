package com.dbxlab.vijjub.mirumy.imageutils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * Created by vijjub on 9/2/16.
 */
public class GalleryImage {

    final String TAG = this.getClass().getSimpleName();
    private Context context;
    private Uri photoUri;

    public GalleryImage(Context context) {
        this.context = context;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public Intent openGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");

        return Intent.createChooser(intent, this.getChooserTitle());
    }

    public String getChooserTitle() {
        return "Select Pictures";
    }

    public String getPath() {
        String path;
        if(Build.VERSION.SDK_INT < 11) {
            path = ImagePathUtil.getRealPathFromURI_BelowAPI11(this.context, this.photoUri);
        } else if(Build.VERSION.SDK_INT < 19) {
            path = ImagePathUtil.getRealPathFromURI_API11to18(this.context, this.photoUri);
        } else {
            path = ImagePathUtil.getRealPathFromURI_API19(this.context, this.photoUri);
        }

        return path;
    }
}
