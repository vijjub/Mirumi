package com.dbxlab.vijjub.mirumy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dbxlab.vijjub.mirumy.R;
import com.dbxlab.vijjub.mirumy.UpdateAccountInfoActivity;
import com.dbxlab.vijjub.mirumy.adapters.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import helper.SQliteManager;

/**
 * Created by vijjub on 10/17/16.
 */
public class AccountInfoClayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQliteManager db = new SQliteManager(this);
        final User userObj = new User();

        HashMap<String,String> user = db.getProfileDetails();


        setContentView(R.layout.fragment_account_info_clayout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("My Account");
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
        String fullGenderText = null;
        if(genderText.equals("M"))
            fullGenderText = "Male";

        if(genderText.equals("F"))
            fullGenderText = "Female";

        if(genderText.equals("O"))
            fullGenderText = "Other";

        userObj.setGender(fullGenderText);

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

        TextView fullname = (TextView)findViewById(R.id.fullname);
        fullname.setText(uidNameText);

        TextView email = (TextView)findViewById(R.id.email);
        email.setText(emailText);

        TextView age = (TextView)findViewById(R.id.age);
        age.setText(ageText);

        TextView gender = (TextView)findViewById(R.id.gender);
        gender.setText(genderText);


        TextView sleep = (TextView)findViewById(R.id.sleep);
        sleep.setText(sleepText);


        TextView cooking = (TextView)findViewById(R.id.cooking);
        cooking.setText(cookingText);

        TextView foodPref = (TextView)findViewById(R.id.food_pref);
        foodPref.setText(foodprefText);

        TextView clean = (TextView)findViewById(R.id.cleanliness);
        clean.setText(cleanText);

        TextView smoking = (TextView)findViewById(R.id.smoking);
        smoking.setText(smokingText);

        TextView alcohol = (TextView)findViewById(R.id.alcohol);
        alcohol.setText(alcoholText);

        TextView socialize = (TextView)findViewById(R.id.social);
        socialize.setText(socializeText);

        TextView noise = (TextView)findViewById(R.id.noise);
        noise.setText(noiseText);

        TextView upets = (TextView)findViewById(R.id.pets);
        upets.setText(upetsText);

        TextView joinedat = (TextView)findViewById(R.id.joined);
        String dateJoined = joinedText;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date;
        try {
            date = format.parse(dateJoined);
            String month = (String) android.text.format.DateFormat.format("MMM", date);
            String year = (String) android.text.format.DateFormat.format("yyyy", date);
            String monthYear = month+" "+year;
            joinedat.append(monthYear);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        ImageView profileImg = (ImageView)findViewById(R.id.profileimage);
        Glide.with(this).load(imageurl)
                .error(R.drawable.men)
                .skipMemoryCache(true)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(profileImg);

        FloatingActionButton updateInfo = (FloatingActionButton) findViewById(R.id.button_edit_user);
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountInfoClayout.this,UpdateAccountInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",userObj);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

