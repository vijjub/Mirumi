package com.dbxlab.vijjub.mirumy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dbxlab.vijjub.mirumy.adapters.BrowseApartment;
import com.dbxlab.vijjub.mirumy.adapters.BrowseApartmentAdapter;
import com.dbxlab.vijjub.mirumy.adapters.User;
import com.dbxlab.vijjub.mirumy.adapters.UserApartment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQliteManager;

/**
 * Created by vijjub on 1/28/17.
 */

public class BrowseApartmentsActivity extends AppCompatActivity {
    private final String TAG = BrowseApartmentsActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private String token;
    private HashMap<String, String> user;
    private List<BrowseApartment> browseApartmentList;
    private BrowseApartmentAdapter browseApartmentAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_browse_apt);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        pDialog = new ProgressDialog(this);

        pDialog.setCancelable(false);


        final Context context = this;
        SQliteManager db = new SQliteManager(context);
        token = db.getTokenDetails();
        user = db.getProfileDetails();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_browse_apt);
        browseApartmentList = new ArrayList<BrowseApartment>();
        getBrowseAptDetails();
        for (int j = 0; j < browseApartmentList.size(); j++)
            Log.d(TAG, "Fetching Complete Apartment details from Sqlite: " + browseApartmentList.get(j).getUserApartment().getAddress());


        browseApartmentAdapter = new BrowseApartmentAdapter(browseApartmentList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(browseApartmentAdapter);

        recyclerView.addOnItemTouchListener(new BrowseApartmentsActivity.RecyclerTouchListener(context, recyclerView, new BrowseApartmentsActivity.CardClickListener() {
            @Override
            public void onClick(View view, int position) {
                BrowseApartment browseApartment = browseApartmentList.get(position);
//                onBrowseAptSelectListener.setBrowseApartment(browseApartment);
                Intent intent = new Intent(BrowseApartmentsActivity.this, BrowseApartmentDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("browseApartment", browseApartment);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(context, Integer.toString(position) + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        browseApartmentAdapter.notifyDataSetChanged();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                getBrowseAptDetails();

            }

        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                R.color.colorAccent,

                R.color.colorPrimary,

                R.color.colorPrimaryDark);


    }

    private void getBrowseAptDetails() {

        String tag_string_req = "browseAptDetails";
//        String gender = user.get("rgender");
        double lat, lon;
        int budget;
        if (user.get("roomielat") != null)
            lat = Double.parseDouble(user.get("roomielat"));
        else lat = 32.7357;

        if (user.get("roomielong") != null)
            lon = Double.parseDouble(user.get("roomielong"));
        else lon = -97.1081;

        if (user.get("rent") != null)
            budget = Integer.parseInt(user.get("rent"));
        else budget = 2000;


        String url = AppConfig.URL_BROWSE_APT_LIST + Double.toString(lon) + "," + Double.toString(lat) + "/"
                + Integer.toString(budget) + "/";


        pDialog.setMessage(" ...");
        showDialog();
        Log.d("Details %s", "inside the function");
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        browseApartmentAdapter.clear();

                        try {
                            JSONArray jsonArray = response;

                            // Check for error node in json
                            if (jsonArray.length() > 0) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    UserApartment userApartment = new UserApartment();
                                    User user = new User();
                                    BrowseApartment browseApartment = new BrowseApartment();

                                    //Apartment Details
                                    userApartment.setAddress(jsonObject.getString("address"));
                                    userApartment.setAPIid(jsonObject.getString("id"));
                                    userApartment.setDesc(jsonObject.getString("desc"));
                                    userApartment.setRooms(jsonObject.getInt("rooms"));
                                    userApartment.setRent(jsonObject.getInt("rent"));
                                    userApartment.setUtilities(jsonObject.getInt("utilities"));
                                    userApartment.setVacancy(jsonObject.getInt("vacancy"));
                                    userApartment.setInternet(jsonObject.getBoolean("internet"));
                                    userApartment.setGym(jsonObject.getBoolean("gym"));
                                    userApartment.setPets(jsonObject.getBoolean("pets"));
                                    userApartment.setSmoking(jsonObject.getBoolean("smoking"));
                                    userApartment.setDateMovin(jsonObject.getString("dateMovin"));
                                    userApartment.setDuration(jsonObject.getString("duration"));
                                    userApartment.setPlaceType(jsonObject.getString("placeType"));
                                    userApartment.setPool(jsonObject.getBoolean("pool"));
                                    userApartment.setWifi(jsonObject.getBoolean("wifi"));
                                    userApartment.setAptGender(jsonObject.getString("gender"));
                                    userApartment.setAptAgeMin(jsonObject.getInt("ageMin"));
                                    userApartment.setAptAgeMax(jsonObject.getInt("ageMax"));
                                    userApartment.setDeposit(jsonObject.getInt("deposit"));
                                    userApartment.setMusic(jsonObject.getBoolean("music"));
                                    userApartment.setGuests(jsonObject.getBoolean("guests"));
                                    userApartment.setDrugs(jsonObject.getBoolean("drugs"));
                                    userApartment.setLateNights(jsonObject.getBoolean("lateNights"));
                                    userApartment.setWasher(jsonObject.getBoolean("washer"));
                                    userApartment.setDryer(jsonObject.getBoolean("dryer"));
                                    userApartment.setFurnished(jsonObject.getBoolean("furnished"));
                                    userApartment.setKitchen(jsonObject.getBoolean("kitchen"));
                                    userApartment.setCloset(jsonObject.getBoolean("closet"));
                                    userApartment.setAC(jsonObject.getBoolean("airConditioner"));
                                    userApartment.setHeater(jsonObject.getBoolean("heater"));
                                    userApartment.setHasPet(jsonObject.getBoolean("hasPet"));
                                    userApartment.setInternet(jsonObject.getBoolean("internet"));
                                    JSONObject location = jsonObject.getJSONObject("location");
                                    JSONArray coordinates = location.getJSONArray("coordinates");
                                    userApartment.setLat((Double) coordinates.get(1));
                                    userApartment.setLon((Double) coordinates.get(0));

                                    JSONArray aptImages = jsonObject.getJSONArray("aptImages");
                                    List<String> aptImagesList = new ArrayList<>();

                                    for (int j = 0; j < aptImages.length(); j++) {
                                        JSONObject imagejson = aptImages.getJSONObject(j);
                                        String urlimage = imagejson.getString("image");
                                        aptImagesList.add(urlimage);
                                    }
                                    userApartment.setAptImages(aptImagesList);

                                    JSONObject userJson = jsonObject.getJSONObject("user");
                                    user.setUsername(userJson.getString("username"));
                                    JSONObject userJsonProfile = userJson.getJSONObject("uProfile");
                                    user.setAge(userJsonProfile.getInt("age"));
                                    user.setGender(userJsonProfile.getString("gender"));
                                    user.setCooking(userJsonProfile.getInt("cooking"));
                                    user.setClean(userJsonProfile.getString("cleanliness"));
                                    user.setSmoking(userJsonProfile.getBoolean("smoking"));
                                    user.setAlcohol(userJsonProfile.getBoolean("alcohol"));
                                    user.setNoise(userJsonProfile.getString("noise"));
                                    user.setSocialize(userJsonProfile.getString("socializing"));
                                    user.setUpets(userJsonProfile.getBoolean("uPets"));
                                    user.setSleep(userJsonProfile.getString("sleep"));
                                    user.setFoodPref(userJsonProfile.getString("foodPreference"));
                                    user.setProfileImg(userJsonProfile.getString("profileImg"));

                                    browseApartment.setUser(user);
                                    browseApartment.setUserApartment(userApartment);
                                    browseApartmentList.add(browseApartment);


                                }
//                                browseApartmentAdapter.addAll(browseApartmentList);
                                browseApartmentAdapter.notifyDataSetChanged();
                                if (swipeContainer != null)
                                    swipeContainer.setRefreshing(false);


                                Toast.makeText(getApplicationContext(), "Details: " + response.length(), Toast.LENGTH_LONG).show();
                                Log.d("Details %s", response.toString());
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token " + token);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance(getApplicationContext()).addToRequestQueue(req, tag_string_req);
        hideDialog();

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
    public void onDestroy() {
        super.onDestroy();
    }

    public interface CardClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private BrowseApartmentsActivity.CardClickListener cardClickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BrowseApartmentsActivity.CardClickListener cardClickListener) {
            this.cardClickListener = cardClickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && cardClickListener != null) {
                        cardClickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && cardClickListener != null && gestureDetector.onTouchEvent(e)) {
                cardClickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
