package com.dbxlab.vijjub.mirumy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dbxlab.vijjub.mirumy.adapters.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vijjub on 10/24/16.
 */
public class BrowseUserDetailsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_roomie_info);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle bundle = this.getIntent().getExtras();
        final User user = (User)bundle.getSerializable("user");


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(user.getUsername());
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.apploizc_white_color));


        String genderText = user.getGender();
        String fullGenderText = null;
        if(genderText.equals("M"))
            fullGenderText = "Male";

        if(genderText.equals("F"))
            fullGenderText = "Female";

        if(genderText.equals("O"))
            fullGenderText = "Other";



        CheckedTextView age = (CheckedTextView) findViewById(R.id.age);
        age.setText(Integer.toString(user.getAge()));

        CheckedTextView gender = (CheckedTextView) findViewById(R.id.gender);
        gender.setText(fullGenderText);

        CheckedTextView rent = (CheckedTextView) findViewById(R.id.rent);
        rent.setText(Integer.toString(user.getRoomie_ucost()));

        TextView sleep = (TextView)findViewById(R.id.sleep);
        sleep.setText(user.getSleep());


        TextView cooking = (TextView)findViewById(R.id.cooking);
        cooking.setText(Integer.toString(user.getCooking()));

        TextView foodPref = (TextView)findViewById(R.id.food_pref);
        foodPref.setText(user.getFoodPref());

        TextView clean = (TextView)findViewById(R.id.cleanliness);
        clean.setText(user.getClean());

        TextView smoking = (TextView)findViewById(R.id.smoking);
        Boolean smoke = user.isSmoking(); String smoketext;
        if(smoke)
            smoketext = "Yes";
        else smoketext = "No";

        smoking.setText(smoketext);

        TextView alcohol = (TextView)findViewById(R.id.alcohol);
        Boolean alcoholbool = user.isAlcohol(); String alcoholtext;
        if(alcoholbool)
            alcoholtext = "Yes";
        else alcoholtext = "No";

        alcohol.setText(alcoholtext);


        TextView socialize = (TextView)findViewById(R.id.social);
        socialize.setText(user.isSocialize());

        TextView noise = (TextView)findViewById(R.id.noise);
        noise.setText(user.isNoise());

        TextView upets = (TextView)findViewById(R.id.pets);

        Boolean pets = user.isUpets(); String petsText;
        if(pets)
            petsText = "I love pets";
        else petsText = "Not Interested";

        upets.setText(petsText);


        TextView joinedat = (TextView)findViewById(R.id.joined);
        String dateJoined = user.getJoined();
        if(dateJoined!=null){
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
        }}


        ImageView profileImg = (ImageView)findViewById(R.id.profileimage);
        Glide.with(this).load(user.getProfileImg())
                .error(R.drawable.men)
                .skipMemoryCache(true)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(profileImg);

        FloatingActionButton updateInfo = (FloatingActionButton) findViewById(R.id.message);
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BrowseUserDetailsActivity.this, ConversationActivity.class);
                intent.putExtra(ConversationUIService.USER_ID, user.getUsername()+"MiRumy");
                intent.putExtra(ConversationUIService.DISPLAY_NAME, user.getUsername()); //put it for displaying the title.
                startActivity(intent);

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
