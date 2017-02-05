package com.dbxlab.vijjub.mirumy;

/**
 * Created by vijjub on 9/19/16.
 */


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dbxlab.vijjub.mirumy.adapters.UserApartment;
import com.dbxlab.vijjub.mirumy.fragments.updateapartment.OnApartmentUpdateListener;
import com.dbxlab.vijjub.mirumy.fragments.updateapartment.OnUpdateAddressListener;
import com.dbxlab.vijjub.mirumy.fragments.updateapartment.UpdateAptFragment;
import com.dbxlab.vijjub.mirumy.fragments.updateapartment.UpdateAptImagesFragment;

import java.util.ArrayList;

/**
 * Created by vijjub on 9/11/16.
 */
public class UpdateAptActivity extends AppCompatActivity implements OnUpdateAddressListener,OnApartmentUpdateListener {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_apartment);
        Bundle bundle = getIntent().getExtras();
        UpdateAptFragment updateAptFragment = UpdateAptFragment.newInstance(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, updateAptFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    @Override
    public void onBackPressed(){

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            getSupportFragmentManager().popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }





//    @Override
//    public void setApartmentId(int apartmentId, String token,long aiid) {
//        UpdateAptImagesFragment updateAptImagesFragment = new UpdateAptImagesFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("apartmentId",apartmentId);
//        bundle.putString("token",token);
//        bundle.putLong("aiid",aiid);
//        updateAptImagesFragment.setArguments(bundle);
//        Log.i(AddApartmentActivity.class.getSimpleName(),apartmentId+" "+token);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.flContent, updateAptImagesFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//    }

    @Override
    public void setUpdateAddressInfo(UserApartment apartment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("userApartment",apartment);
        UpdateAptFragment updateAptFragment = UpdateAptFragment.newInstance(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, updateAptFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void setAptUpdate(int apartmentId, String token, long aiid, ArrayList<String> arrayList) {
        UpdateAptImagesFragment updateAptImagesFragment = new UpdateAptImagesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("apartmentId",apartmentId);
        bundle.putString("token",token);
        bundle.putLong("aiid",aiid);
        bundle.putStringArrayList("images",arrayList);
        updateAptImagesFragment.setArguments(bundle);
        Log.i(AddApartmentActivity.class.getSimpleName(),apartmentId+" "+token);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, updateAptImagesFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
