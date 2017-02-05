package com.dbxlab.vijjub.mirumy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dbxlab.vijjub.mirumy.R;
import com.dbxlab.vijjub.mirumy.adapters.User;

import java.util.HashMap;

import helper.SQliteManager;

/**
 * Created by vijjub on 8/8/16.
 */
public class AccountInfo extends Fragment {


    OnAccountInfoListener onAccountInfoListener;
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_account_info_clayout, container, false);
        Context context = getActivity();
        SQliteManager db = new SQliteManager(context);
        final User userObj = new User();

        HashMap<String,String> user = db.getProfileDetails();


        final Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_update_account);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Edit Account");
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.apploizc_transparent_color));

        userObj.setFName(user.get("fname"));
        userObj.setLName(user.get("lname"));
        userObj.setUsername(user.get("username"));
        String uidNameText = userObj.getFName()+" "+userObj.getLName();


        userObj.setToken(user.get("token"));
        userObj.setRow_id(Integer.parseInt(user.get("row_id")));

        String emailText = user.get("email");
        userObj.setEmail(emailText);

        String ageText = user.get("age");
        userObj.setAge(Integer.parseInt(ageText));

        String genderText = user.get("gender");
        userObj.setGender(genderText);

        String sleepText =  user.get("sleep");
        userObj.setSleep(sleepText);

        String cookingText = user.get("cooking");
        userObj.setCooking(Integer.parseInt(cookingText));

        String foodprefText = user.get("food_pref");
        userObj.setFoodPref(foodprefText);


        String cleanText = user.get("clean");
        userObj.setClean(cleanText);

        String smokingText = user.get("smoking");
        userObj.setSmoking(Boolean.parseBoolean(smokingText));

        String alcoholText= user.get("alcohol");
        userObj.setAlcohol(Boolean.parseBoolean(alcoholText));

        String socializeText = user.get("socialize");
        userObj.setSocialize(socializeText);

        String noiseText = user.get("noise");
        userObj.setNoise(noiseText);

        String upetsText = user.get("upets");
        userObj.setUpets(Boolean.parseBoolean(upetsText));

        String joinedText = user.get("joined");
        userObj.setJoined(joinedText);

        String imageurl = user.get("profileimg");
        userObj.setProfileImg(imageurl);

        TextView fullname = (TextView)view.findViewById(R.id.fullname);
        fullname.setText(uidNameText);

        TextView email = (TextView)view.findViewById(R.id.email);
        email.setText(emailText);

        TextView age = (TextView)view.findViewById(R.id.age);
        age.setText(ageText);

        TextView gender = (TextView)view.findViewById(R.id.gender);
        gender.setText(genderText);


        TextView sleep = (TextView)view.findViewById(R.id.sleep);
        sleep.setText(sleepText);


        TextView cooking = (TextView)view.findViewById(R.id.cooking);
        cooking.setText(cookingText);

        TextView foodPref = (TextView)view.findViewById(R.id.food_pref);
        foodPref.setText(foodprefText);

        TextView clean = (TextView)view.findViewById(R.id.clean);
        clean.setText(cleanText);

        TextView smoking = (TextView)view.findViewById(R.id.smoking);
        smoking.setText(smokingText);

        TextView alcohol = (TextView)view.findViewById(R.id.alcohol);
        alcohol.setText(alcoholText);

        TextView socialize = (TextView)view.findViewById(R.id.social);
        socialize.setText(socializeText);

        TextView noise = (TextView)view.findViewById(R.id.noise);
        noise.setText(noiseText);

        TextView upets = (TextView)view.findViewById(R.id.upets);
        upets.setText(upetsText);

        TextView joinedat = (TextView)view.findViewById(R.id.joined);
        joinedat.setText(joinedText);

        ImageView profileImg = (ImageView)view.findViewById(R.id.profileimage);
        Glide.with(this).load(imageurl)
                .error(R.drawable.men)
                .skipMemoryCache(true)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(profileImg);

        FloatingActionButton updateInfo = (FloatingActionButton) view.findViewById(R.id.button_edit_user);
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onAccountInfoListener.setAccountInfo(userObj);
            }
        });

        return  view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onAccountInfoListener = (OnAccountInfoListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnAccountInfoListener");

        }
    }

    public interface OnAccountInfoListener{
         void setAccountInfo(User user);
    }
}
