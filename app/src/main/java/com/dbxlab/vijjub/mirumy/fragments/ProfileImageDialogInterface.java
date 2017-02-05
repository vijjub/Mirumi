package com.dbxlab.vijjub.mirumy.fragments;

import java.io.IOException;

/**
 * Created by vijjub on 9/1/16.
 */
public interface ProfileImageDialogInterface {


        void onCancel();

        void onSelectCamera() throws IOException;

        void onSelectGallery();


}
