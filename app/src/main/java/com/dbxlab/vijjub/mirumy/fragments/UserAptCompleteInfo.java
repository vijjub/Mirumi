package com.dbxlab.vijjub.mirumy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dbxlab.vijjub.mirumy.R;
import com.dbxlab.vijjub.mirumy.UpdateAptActivity;
import com.dbxlab.vijjub.mirumy.adapters.MapsActivityCircle;
import com.dbxlab.vijjub.mirumy.adapters.UserApartment;
import com.dbxlab.vijjub.mirumy.adapters.UserAptImgAdapter;


/**
 * Created by vijjub on 8/18/16.
 */
public class UserAptCompleteInfo extends Fragment {
    private final String TAG = UserAptCompleteInfo.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final Bundle bundle = getArguments();
        final UserApartment userApartment = (UserApartment) bundle.getSerializable("userApartment");
        View view = inflater.inflate(R.layout.user_apt_complete_info, container, false);

        ViewPager mViewPager = (ViewPager)view.findViewById(R.id.aptimageslist);
        UserAptImgAdapter adapterView = new UserAptImgAdapter(getActivity(),userApartment.getAptImages());
        mViewPager.setAdapter(adapterView);

        TextView desc = (TextView)view.findViewById(R.id.desc);
        desc.setText(userApartment.getDesc());

        TextView rent = (TextView)view.findViewById(R.id.rent);
        rent.append(Integer.toString(userApartment.getRent()));

        CheckedTextView gender = (CheckedTextView) view.findViewById(R.id.gender);
        String genderoption = userApartment.getAptGender();

        if(genderoption.equals("M"))
            gender.setText("Male");

        if(genderoption.equals("F"))
            gender.setText("Female");

        if(genderoption.equals("A"))
            gender.setText("Male or Female");

        CheckedTextView age = (CheckedTextView)view.findViewById(R.id.age);
        int minAge = userApartment.isAptAgeMin();
        int maxAge = userApartment.isAptAgeMax();
        String ageText = minAge + " - " +maxAge+" Yrs";
        age.setText(ageText);


        CheckedTextView vacancy = (CheckedTextView) view.findViewById(R.id.vacancy);
        String roomieText = Integer.toString(userApartment.getVacancy())+" Roomies";
        vacancy.setText(roomieText);

        TextView utilities = (TextView)view.findViewById(R.id.utilities);
        utilities.setText(Integer.toString(userApartment.getUtilities()));

        TextView deposit = (TextView)view.findViewById(R.id.deposit);
        deposit.setText(Integer.toString(userApartment.isDeposit()));

        TextView movin = (TextView)view.findViewById(R.id.movindate);
        movin.setText(userApartment.getDateMovin());

        TextView placeType = (TextView)view.findViewById(R.id.placetype);
        placeType.setText(userApartment.getPlaceType());

        TextView duration = (TextView)view.findViewById(R.id.duration);
        duration.setText(userApartment.getDuration());

        LinearLayout aLayout = (LinearLayout)view.findViewById(R.id.alayout);
        LinearLayout rLayout = (LinearLayout)view.findViewById(R.id.rlayout);


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        if(userApartment.isAC()){
            View acView = layoutInflater.inflate(R.layout.amenity_ac,null);
            aLayout.addView(acView);

        }

        if(userApartment.isHeater()){
            View heaterView = layoutInflater.inflate(R.layout.amenity_heater,null);
            aLayout.addView(heaterView);

        }

        if(userApartment.isPool()){
            View poolView = layoutInflater.inflate(R.layout.amenity_pool,null);
            aLayout.addView(poolView);

        }

        if(userApartment.isInternet()){
            View internetView = layoutInflater.inflate(R.layout.amenity_internet,null);
            aLayout.addView(internetView);

        }

        if(userApartment.isWifi()){
            View wifiView = layoutInflater.inflate(R.layout.amenity_wifi,null);
            aLayout.addView(wifiView);

        }

        if(userApartment.isGym()){
            View gymView = layoutInflater.inflate(R.layout.amenity_gym,null);
            aLayout.addView(gymView);

        }

        if(userApartment.isKitchen()){
            View kitchenView = layoutInflater.inflate(R.layout.amenity_kitchen,null);
            aLayout.addView(kitchenView);

        }

        if(userApartment.isCloset()){
            View closetView = layoutInflater.inflate(R.layout.amenity_closet,null);
            aLayout.addView(closetView);

        }

        if(userApartment.isWasher()){
            View washerView = layoutInflater.inflate(R.layout.amenity_washer,null);
            aLayout.addView(washerView);

        }

        if(userApartment.isDryer()){
            View dryerView = layoutInflater.inflate(R.layout.amenity_dryer,null);
            aLayout.addView(dryerView);

        }

        if(userApartment.isFurnished()){
            View furnishedView = layoutInflater.inflate(R.layout.amenity_furnished,null);
            aLayout.addView(furnishedView);

        }

        if(userApartment.isHasPet()){
            View hasPetView = layoutInflater.inflate(R.layout.amenity_pets_live_here,null);
            aLayout.addView(hasPetView);

        }

        if(userApartment.isDrugs()){
            View drugsView = layoutInflater.inflate(R.layout.rule_drugs,null);
            rLayout.addView(drugsView);

        }

        if(userApartment.isMusic()){
            View loudMusicView = layoutInflater.inflate(R.layout.rule_loud_music,null);
            rLayout.addView(loudMusicView);

        }

        if(userApartment.isGuests()){
            View guestsView = layoutInflater.inflate(R.layout.rule_guests,null);
            rLayout.addView(guestsView);

        }

        if(userApartment.isLateNights()){
            View latenightView = layoutInflater.inflate(R.layout.rule_late_nights,null);
            rLayout.addView(latenightView);

        }

        if(userApartment.isPets()){
            View petView = layoutInflater.inflate(R.layout.rule_pets_allowed,null);
            rLayout.addView(petView);

        }

        if(userApartment.getSmoking()){
            View smokingView = layoutInflater.inflate(R.layout.rule_no_smoking,null);
            rLayout.addView(smokingView);

        }

        Fragment fragment = Fragment.instantiate(getContext(), MapsActivityCircle.class.getName());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Bundle location = new Bundle();
        location.putDouble("lat", userApartment.getLat());
        location.putDouble("lon", userApartment.getLon());
        location.putString("address", String.valueOf(userApartment.getAddress()));
        fragment.setArguments(location);
        ft.replace(R.id.map, fragment,"Detail Fragment");
        ft.commit();

        FloatingActionButton update = (FloatingActionButton) view.findViewById(R.id.button_edit_apartment);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateAptActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("userApartment",userApartment);
                intent.putExtras(bundle1);
                startActivity(intent);

            }
        });

        return view;
    }


}
