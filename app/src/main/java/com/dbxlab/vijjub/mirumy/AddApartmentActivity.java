package com.dbxlab.vijjub.mirumy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dbxlab.vijjub.mirumy.adapters.AptAddress;
import com.dbxlab.vijjub.mirumy.fragments.addapartment.AddAptFragment;
import com.dbxlab.vijjub.mirumy.fragments.addapartment.AddAptImagesFragment;
import com.dbxlab.vijjub.mirumy.fragments.addapartment.OnAddressSelectedListener;
import com.dbxlab.vijjub.mirumy.fragments.addapartment.OnApartmentAddedListener;

/**
 * Created by vijjub on 9/11/16.
 */
public class AddApartmentActivity extends AppCompatActivity implements OnAddressSelectedListener,OnApartmentAddedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_apartment);
                Fragment fragment = null; Class fragmentClass = null;
                fragmentClass = AddAptFragment.class;
                try {
                    fragment = (Fragment)fragmentClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

        FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent,fragment).addToBackStack(null).commit();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setAddressInfo(AptAddress address) {

        AddAptFragment addAptFragment = new AddAptFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("address",address);
        addAptFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, addAptFragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }


    @Override
    public void setApartmentId(int apartmentId, String token,long aiid) {
        AddAptImagesFragment addAptFragment = new AddAptImagesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("apartmentId",apartmentId);
        bundle.putString("token",token);
        bundle.putLong("aiid",aiid);
        addAptFragment.setArguments(bundle);
        Log.i(AddApartmentActivity.class.getSimpleName(),apartmentId+" "+token);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, addAptFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


}
