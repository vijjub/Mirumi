package com.dbxlab.vijjub.mirumy.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.dbxlab.vijjub.mirumy.RoomieSettings;
import com.dbxlab.vijjub.mirumy.fragments.addapartment.AddAptFragment;
import com.dbxlab.vijjub.mirumy.fragments.updateapartment.UpdateAptFragment;

import java.util.Calendar;

/**
 * Created by vijjub on 10/13/16.
 */
public class DatePickerFragment extends DialogFragment {
    private UpdateAptFragment mFragment = null;
    private AddAptFragment nFragment = null;
    private RoomieSettings rActivity = null;

    public void setCallbackAddApt(AddAptFragment fragment) {
        nFragment = fragment;
    }

    public void setCallBackUpdateApt(UpdateAptFragment fragment){
        mFragment = fragment;
    }

    public void setCallBackRoomieSetting(RoomieSettings roomieSetting) { rActivity = roomieSetting;}



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if(mFragment!= null){
            return new DatePickerDialog(getActivity(), mFragment, year, month, day);
        }

        if(nFragment!=null){
            return new DatePickerDialog(getActivity(),nFragment,year,month,day);
        }

        if(rActivity!=null){
            return new DatePickerDialog(getContext(),rActivity,year,month,day);
        }
            return super.onCreateDialog(savedInstanceState);

    }


//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        movin.setText(year + "-" + (monthOfYear + 1)  + "-" + dayOfMonth);
//
//    }
}

