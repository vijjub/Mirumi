package com.dbxlab.vijjub.mirumy;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dbxlab.vijjub.mirumy.adapters.User;
import com.dbxlab.vijjub.mirumy.fragments.DatePickerFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQliteManager;

/**
 * Created by vijjub on 10/19/16.
 */
public class RoomieSettings extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, PlaceSelectionListener {

    private static final String TAG = RoomieSettings.class.getSimpleName();
    int ageMin = 20;
    int ageMax = 60;
    Double lat,lon;
    private Switch roomie,otherRoomie;
    private LinearLayout roomielayout,otherRoomieLayout;
    private EditText movinDate,duration,gender,rent,desc;
    private TextView preferredLocation;
    private String[] durationList;
    private String[] genderList;
    private int genderOption = 0;
    private User userObj = new User() ;
    private LatLng latLng;
    private ProgressDialog pDialog;
    private SQliteManager db;
    private int method;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommate_settings);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQliteManager(this);
        final User userObj = new User();

        final HashMap<String,String> user = db.getProfileDetails();


        roomielayout = (LinearLayout)findViewById(R.id.roomieLinearLayout);
        otherRoomieLayout = (LinearLayout)findViewById(R.id.otherroomieLayout);


        roomie = (Switch)findViewById(R.id.roomieSwitch);
        Boolean truthvalue = Boolean.parseBoolean(user.get("roomie"));
        Toast.makeText(getApplicationContext(),Boolean.toString(truthvalue),Toast.LENGTH_LONG).show();

        if(truthvalue) {
            roomie.setChecked(true);
            roomielayout.setVisibility(View.VISIBLE);
        }

        otherRoomie = (Switch)findViewById(R.id.otherroomieSwitch);
        Boolean truthvalue2 = Boolean.parseBoolean(user.get("otherroomie"));
        Toast.makeText(getApplicationContext(),Boolean.toString(truthvalue2),Toast.LENGTH_LONG).show();

        if(truthvalue2) {
            otherRoomie.setChecked(true);
            otherRoomieLayout.setVisibility(View.VISIBLE);
        }



        roomie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    roomielayout.setVisibility(View.VISIBLE);
                else roomielayout.setVisibility(View.GONE);

            }
        });

        movinDate = (EditText) findViewById(R.id.movindate);
        if(user.get("movin")!=null)
            movinDate.setText(user.get("movin"));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        movinDate.setText(sdf.format(calendar.getTime()));
        movinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setCallBackRoomieSetting(RoomieSettings.this);
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        duration = (EditText)findViewById(R.id.duration);
        if(user.get("duration")!=null)
            duration.setText(user.get("duration"));
        duration.setText("More than a year");

        durationList = getResources().getStringArray(R.array.duration_list);
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomieSettings.this);
                builder.setTitle(R.string.duration_text)
                        .setItems(R.array.duration_list, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                duration.setText(durationList[i]);
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        desc = (EditText)findViewById(R.id.desc);
        if(user.get("desc")!=null)
            desc.setText(user.get("desc"));

        gender = (EditText)findViewById(R.id.gender);
        String roomieGenderText="";

        if(user.get("rgender")!=null) {
            gender.setText(user.get("rgender"));
            roomieGenderText=user.get("rgender");
        }

        if(roomieGenderText.equals("M"))
            gender.setText("Male");
        if(roomieGenderText.equals("F"))
            gender.setText("Female");
        if(roomieGenderText.equals("O"))
            gender.setText("Male or Female");


        genderList = getResources().getStringArray(R.array.gender);

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(RoomieSettings.this);
                builder.setTitle(R.string.gender_text)
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setSingleChoiceItems(R.array.gender, genderOption ,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        gender.setText(genderList[i]);
                                        genderOption = i;
                                        dialogInterface.dismiss();
                                    }
                                });


                AlertDialog alert = builder.create();
                alert.show();


            }
        });


        otherRoomie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    otherRoomieLayout.setVisibility(View.VISIBLE);
                else otherRoomieLayout.setVisibility(View.GONE);
            }
        });


        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(this);
        rangeSeekBar.setRangeValues(17, 60);
        rangeSeekBar.setSelectedMinValue(ageMin);
        rangeSeekBar.setSelectedMaxValue(ageMax);
        rangeSeekBar.setTextAboveThumbsColorResource(R.color.colorPrimaryDark);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                //Toast.makeText(getContext(),Integer.toString(minValue)+" "+Integer.toString(maxValue),Toast.LENGTH_LONG).show();
                ageMax = maxValue;
                ageMin = minValue;
            }


        });

        LinearLayout seekBarLayout = (LinearLayout)findViewById(R.id.seek_bar_layout);
        seekBarLayout.addView(rangeSeekBar);


        preferredLocation = (TextView)findViewById(R.id.preferredLocation);
        if(user.get("roomielat")!=null && user.get("roomielong")!=null) {

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(user.get("roomielat")), Double.parseDouble(user.get("roomielong")), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
            String preferredloc = city + " ," + state + " ," + country + " ," + " ," + postalCode;
            preferredLocation.setText(preferredloc);
            lat = Double.parseDouble(user.get("roomielat"));
            lon = Double.parseDouble(user.get("roomielong"));
            latLng = new LatLng(lat,lon);
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(this);

        rent = (EditText)findViewById(R.id.rent);
        if(user.get("rent")!=null){
            rent.setText(user.get("rent"));
        }

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        FloatingActionButton roomieSave = (FloatingActionButton)findViewById(R.id.button_roomie_settings);
        roomieSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(RoomieSettings.this, MainActivity.class);
//                startActivity(intent);
//                finish();


                int rentText = Integer.parseInt(rent.getText().toString());
                userObj.setRoomie_ucost(rentText);
                String genderText = "M";

                // String genderText = gender.getText().toString();
                if(gender.getText().toString().equals("Male"))
                    genderText = "M";
                if(gender.getText().toString().equals("Female"))
                    genderText = "F";

                if(gender.getText().toString().equals("Male or Female"))
                    genderText = "A";

                userObj.setGender(genderText);

                //                int uCostText = Integer.parseInt(ucost.getText().toString());
                //                user.setUcost(uCostText);
                String descText = desc.getText().toString();
                userObj.setRoomieDesc(descText);

                userObj.setMovinDate(movinDate.getText().toString());
                userObj.setDuration(duration.getText().toString());

                userObj.setRoomie(roomie.isChecked());
                userObj.setOtherroomie(otherRoomie.isChecked());

                lat = latLng.latitude;
                lon = latLng.longitude;
                userObj.setRoomieLat(lat);
                userObj.setRoomieLon(lon);
                try {
                    updateRoomieDetails(user,userObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void updateRoomieDetails(final HashMap<String,String> user, User userObj) throws JSONException {
        // Tag used to cancel the request
        String tag_string_req = "updateuserinfo";

        pDialog.setMessage("Updating.......");
        showDialog();

        JSONObject params = new JSONObject();
        params.put("otherRoomie",Boolean.toString(userObj.isOtherRoomie()));
        params.put("roomie",Boolean.toString(userObj.isRoomie()));
        params.put("roomieDesc",userObj.getRoomieDesc());
        params.put("duration",userObj.getDuration());
        params.put("uCost",Integer.toString(userObj.getRoomie_ucost()));
        params.put("roomieGender","A");
        params.put("dateMovin",userObj.getMovinDate());
        params.put("roomieAgeMin",Integer.toString(ageMin));
        params.put("roomieAgeMax",Integer.toString(ageMax));
        List<Double> list = new ArrayList<>();
        list.add(0,userObj.getRoomieLon());
        list.add(1,userObj.getRoomieLat());
        JSONArray coordinates = new JSONArray(list);
        JSONObject params2 = new JSONObject();
        params2.put("type","Point");
        params2.put("coordinates",coordinates);
        params.put("preferredLocation",params2);
        String finalURL = "";


        if(!user.get("api_roomie_id").equals("")){
            finalURL = AppConfig.URL_ADD_ROOMIE + user.get("api_roomie_id")+"/";
            method = Request.Method.PUT;
        }

        else {
            finalURL = AppConfig.URL_ADD_ROOMIE_LIST;
            method = Request.Method.POST;
        }
        Log.d(TAG,"In the function");



        JsonObjectRequest strReq = new JsonObjectRequest(method,
                finalURL,params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();
                Log.d(TAG,"In the function 2");


                try {
                    JSONObject jObj = response;

                    // Check for error node in json
                    if (jObj.has("id")) {
                        // session.setLogin(true);

                        // Now store the user in SQLite
//                        String imageUrl = jObj.getString("profileImg");
                        int id = jObj.getInt("id");
                        Log.d(TAG,Integer.toString(id));

                        db.updateRoomieDetails(jObj,Integer.parseInt(user.get("row_id")));
                        Intent intent = new Intent(RoomieSettings.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"In the function 3");


                NetworkResponse nresponse = error.networkResponse;
                if (error instanceof ServerError && nresponse != null) {
                    try {
                        String res = new String(nresponse.data);
                        // Now you can use any deserializer to make sense of data
                        JSONObject jObj = new JSONObject(res);
                        Log.d("StartActivity %s", res.toString());

                        if(jObj.has("non_field_errors")) {

                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("non_field_errors");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"Non Field Errors",Toast.LENGTH_LONG).show();
                        }



                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                Log.e(TAG, "Login Error: " + error.getMessage());
                // Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Token "+user.get("token"));
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq, tag_string_req);


    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        movinDate.setText(year + "-" + (monthOfYear + 1)  + "-" + dayOfMonth);

    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place Selected: " + place.getLatLng());

        // Format the returned place's details and display them in the TextView.
        preferredLocation.setText(place.getAddress().toString());
        latLng = new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);


    }

    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
