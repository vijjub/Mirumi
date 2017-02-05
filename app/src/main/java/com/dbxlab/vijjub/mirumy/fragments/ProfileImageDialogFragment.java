package com.dbxlab.vijjub.mirumy.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.io.IOException;

/**
 * Created by vijjub on 9/1/16.
 */
public class ProfileImageDialogFragment extends DialogFragment {
    public static ProfileImageDialogFragment newInstance() {
        return new ProfileImageDialogFragment();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        ((ProfileImageDialogInterface) this.getActivity()).onCancel();
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(this.getActivity()).setTitle("Select Picture").setMessage("Select an image from gallery or take a new picture").setPositiveButton("CAMERA",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    ((ProfileImageDialogInterface) getActivity()).onSelectCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).setNegativeButton("GALLERY",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((ProfileImageDialogInterface) getActivity()).onSelectGallery();

            }
        }).create();
    }


}
