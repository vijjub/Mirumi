package com.dbxlab.vijjub.mirumy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dbxlab.vijjub.mirumy.adapters.BrowseApartment;
import com.dbxlab.vijjub.mirumy.adapters.BrowseRoomieAptAdapter;
import com.dbxlab.vijjub.mirumy.adapters.MapsActivityCircle;
import com.dbxlab.vijjub.mirumy.adapters.User;
import com.dbxlab.vijjub.mirumy.adapters.UserApartment;
import com.dbxlab.vijjub.mirumy.adapters.UserAptImgAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQliteManager;

/**
 * Created by vijjub on 10/24/16.
 */
public class BrowseApartmentDetailsActivity extends AppCompatActivity {
//    private SwipeRefreshLayout swipeContainer;
    private final String TAG = BrowseApartmentDetailsActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private String token;
    private ArrayList<User> roomieList;
    private BrowseRoomieAptAdapter browseRoomieAdapter;
    private HashMap<String,String> userinfo;
    private UserApartment userApartment;


    @Override
    protected void onCreate(Bundle savedBundleInstanceState){
        super.onCreate(savedBundleInstanceState);
        setContentView(R.layout.activity_browseapartment_details);
        SQliteManager db = new SQliteManager(getApplicationContext());
        token = db.getTokenDetails();
        userinfo = db.getProfileDetails();

        final Bundle bundle = this.getIntent().getExtras();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final BrowseApartment browseApartment = (BrowseApartment) bundle.getSerializable("browseApartment");
        userApartment = browseApartment.getUserApartment();
        final User user = browseApartment.getUser();

        getSupportActionBar().setTitle(userApartment.getShortAddress());


        ViewPager mViewPager = (ViewPager)findViewById(R.id.aptimageslist);
        UserAptImgAdapter adapterView = new UserAptImgAdapter(this,userApartment.getAptImages());
        mViewPager.setAdapter(adapterView);

        TextView desc = (TextView)findViewById(R.id.desc);
        desc.setText(userApartment.getDesc());

        final TextView ownername = (TextView)findViewById(R.id.ownername);
        ownername.setText(user.getUsername());

        ImageView profileImgUser = (ImageView)findViewById(R.id.profileimage);
        Glide.with(this).load(user.getProfileImg()).centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                into(profileImgUser);

        TextView rent = (TextView)findViewById(R.id.rent);
        rent.append(Integer.toString(userApartment.getRent()));

        CheckedTextView gender = (CheckedTextView) findViewById(R.id.gender);
        String genderoption = userApartment.getAptGender();

        if(genderoption.equals("M"))
            gender.setText("Male");

        if(genderoption.equals("F"))
            gender.setText("Female");

        if(genderoption.equals("A"))
            gender.setText("Male or Female");

        CheckedTextView age = (CheckedTextView)findViewById(R.id.age);
        int minAge = userApartment.isAptAgeMin();
        int maxAge = userApartment.isAptAgeMax();
        String ageText = minAge + " - " +maxAge+" Yrs";
        age.setText(ageText);


        CheckedTextView vacancy = (CheckedTextView) findViewById(R.id.vacancy);
        String roomieText = Integer.toString(userApartment.getVacancy())+" Roomies";
        vacancy.setText(roomieText);

        TextView utilities = (TextView)findViewById(R.id.utilities);
        utilities.setText(Integer.toString(userApartment.getUtilities()));

        TextView deposit = (TextView)findViewById(R.id.deposit);
        deposit.setText(Integer.toString(userApartment.isDeposit()));

        TextView movin = (TextView)findViewById(R.id.movindate);
        movin.setText(userApartment.getDateMovin());

        TextView placeType = (TextView)findViewById(R.id.placetype);
        placeType.setText(userApartment.getPlaceType());

        TextView duration = (TextView)findViewById(R.id.duration);
        duration.setText(userApartment.getDuration());

        LinearLayout aLayout = (LinearLayout)findViewById(R.id.alayout);
        LinearLayout rLayout = (LinearLayout)findViewById(R.id.rlayout);


        LayoutInflater layoutInflater = LayoutInflater.from(this);

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

        Fragment fragment = Fragment.instantiate(BrowseApartmentDetailsActivity.this, MapsActivityCircle.class.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle location = new Bundle();
        location.putDouble("lat", userApartment.getLat());
        location.putDouble("lon", userApartment.getLon());
        location.putString("address", String.valueOf(userApartment.getAddress()));
        fragment.setArguments(location);
        ft.replace(R.id.map, fragment,"Detail Fragment");
        ft.commit();

        FloatingActionButton chat = (FloatingActionButton) findViewById(R.id.fab_chat);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), UpdateAptActivity.class);
//                Bundle bundle1 = new Bundle();
//                bundle1.putSerializable("userApartment",userApartment);
//                intent.putExtras(bundle1);
//                startActivity(intent);

                Intent intent = new Intent(BrowseApartmentDetailsActivity.this, ConversationActivity.class);
                intent.putExtra(ConversationUIService.USER_ID,user.getUsername()+"MiRumy" );
                intent.putExtra(ConversationUIService.DISPLAY_NAME, user.getUsername()); //put it for displaying the title.
                startActivity(intent);

            }
        });

        LinearLayout profileLayout = (LinearLayout)findViewById(R.id.profile_layout);
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrowseApartmentDetailsActivity.this,BrowseUserDetailsActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user",user);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });

        //TODO Roomies Segment

//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        pDialog = new ProgressDialog(this);

        pDialog.setCancelable(false);


//        final Context context = getContext();
//        SQliteManager db = new SQliteManager(this);
//        token = db.getTokenDetails();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view_browse_roomie);
        roomieList = new ArrayList<User>();

        if(userApartment.getVacancy()>1)
                getBrowseAptDetails();

//        for(int j =0;j<roomieList.size();j++)
            Log.d(TAG, "Fetching Complete Apartment details from Sqlite: " + Integer.toString(roomieList.size()));


        browseRoomieAdapter = new BrowseRoomieAptAdapter(roomieList,this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(browseRoomieAdapter);
        browseRoomieAdapter.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new CardClickListener() {
            @Override
            public void onClick(View view, int position) {
                User user = roomieList.get(position);
//                onBrowseRoomieSelectListener.setBrowseRoomie(user);
                Intent intent = new Intent(BrowseApartmentDetailsActivity.this, BrowseUserDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent.putExtras(bundle);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), Integer.toString(position)+ " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


//        FloatingActionButton newApartment = (FloatingActionButton)view.findViewById(R.id.new_user_apartment);
//        newApartment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),AddApartmentActivity.class);
//                startActivity(intent);
//            }
//        });

        //browseRoomieAdapter.notifyDataSetChanged();

//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//
//            public void onRefresh() {
//                browseRoomieAdapter.clear();
//                getBrowseAptDetails();
//
//            }
//
//        });
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//
//                android.R.color.holo_green_light,
//
//                android.R.color.holo_orange_light,
//
//                android.R.color.holo_red_light);
//



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

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


    private void getBrowseAptDetails() {

        String tag_string_req = "browseRoomieDetails";
        String user_id = userinfo.get("api_user_id");
        double lat = userApartment.getLat();
        double lon = userApartment.getLon();
        int budget = userApartment.getRent();
        int vacancy = userApartment.getVacancy() - 1;

        String finalURL = AppConfig.URL_APT_TO_ROOMIE_NEW +Double.toString(lon)+","+Double.toString(lat)+"/"+user_id+"/"
                +Integer.toString(budget)+"/"+Integer.toString(vacancy)+"/";


        pDialog.setMessage(" ...");
        showDialog();
        Log.d("Details %s","insidethe function");
        JsonArrayRequest req = new JsonArrayRequest(finalURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        roomieList.clear();

                        try {
                            JSONArray jsonArray = response;
                            roomieList.clear();

                            // Check for error node in json
                            if (jsonArray.length()>0) {

                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    User user = new User();

                                    user.setMovinDate(jsonObject.getString("dateMovin"));
                                    user.setOtherroomie(jsonObject.getBoolean("otherRoomie"));
                                    user.setDuration(jsonObject.getString("duration"));
                                    user.setRoomieDesc(jsonObject.getString("roomieDesc"));
                                    user.setRoomie(jsonObject.getBoolean("roomie"));
                                    user.setRoomie_ucost(jsonObject.getInt("uCost"));
                                    user.setRoomieGender(jsonObject.getString("roomieGender"));
                                    user.setRoomieAgeMax(jsonObject.getString("roomieAgeMax"));
                                    user.setRoomieAgeMin(jsonObject.getString("roomieAgeMin"));
                                    user.setRoomieGenderOther(jsonObject.getBoolean("roomieGenderOther"));
                                    JSONObject userJson = jsonObject.getJSONObject("user");
                                    user.setUsername(userJson.getString("username"));
                                    JSONObject userJsonProfile = userJson.getJSONObject("uProfile");
                                    user.setAge(userJsonProfile.getInt("age"));
                                    user.setGender(userJsonProfile.getString("gender"));
                                    user.setCooking(userJsonProfile.getInt("cooking"));
                                    user.setClean(userJsonProfile.getString("cleanliness"));
                                    user.setSmoking(userJsonProfile.getBoolean("smoking"));
                                    user.setAlcohol(Boolean.parseBoolean(userJsonProfile.getString("alcohol")));
                                    user.setNoise(userJsonProfile.getString("noise"));
                                    user.setSocialize(userJsonProfile.getString("socializing"));
                                    user.setUpets(userJsonProfile.getBoolean("uPets"));
                                    user.setSleep(userJsonProfile.getString("sleep"));
                                    user.setFoodPref(userJsonProfile.getString("foodPreference"));
                                    JSONObject preferredLocation = jsonObject.getJSONObject("preferredLocation");
                                    JSONArray coordinates = preferredLocation.getJSONArray("coordinates");
                                    user.setRoomieLat((Double) coordinates.get(1));
                                    user.setRoomieLon((Double) coordinates.get(0));


                                    String profileImgText = AppConfig.URL_DEFAULT + userJsonProfile.getString("profileImg");
                                    user.setProfileImg(profileImgText);
                                    roomieList.add(user);


                                }
//                                browseRoomieAdapter.addAll(roomieList);
                                // browseApartmentAdapter.notifyDataSetChanged();
//                                if(swipeContainer!=null)
//                                    swipeContainer.setRefreshing(false);


                                Toast.makeText(getApplicationContext(),"Details: "+response.length(),Toast.LENGTH_LONG).show();
                                Log.d("Details %s",response.toString());
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token "+token);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(req,tag_string_req);
        hideDialog();

    }

//    private void getBrowseAptDetails() {
//
//        String tag_string_req = "browseRoomieDetails";
//
//
//        pDialog.setMessage(" ...");
//        showDialog();
//        Log.d("Details %s","insidethe function");
//        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_BROWSE_ROOMIE_LIST,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//                        browseRoomieAdapter.clear();
//
//                        try {
//                            JSONArray jsonArray = response;
//                            roomieList.clear();
//
//                            // Check for error node in json
//                            if (jsonArray.length()>0) {
//
//                                for(int i=0;i<jsonArray.length();i++){
//
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    User user = new User();
//
//                                    user.setMovinDate(jsonObject.getString("dateMovin"));
//                                    user.setOtherroomie(jsonObject.getBoolean("otherRoomie"));
//                                    user.setDuration(jsonObject.getString("duration"));
//                                    user.setRoomieDesc(jsonObject.getString("roomieDesc"));
//                                    user.setRoomie(jsonObject.getBoolean("roomie"));
//                                    user.setRoomie_ucost(jsonObject.getInt("uCost"));
//                                    user.setRoomieGender(jsonObject.getString("roomieGender"));
//                                    user.setRoomieAgeMax(jsonObject.getString("roomieAgeMax"));
//                                    user.setRoomieAgeMin(jsonObject.getString("roomieAgeMin"));
//                                    user.setRoomieGenderOther(jsonObject.getBoolean("roomieGenderOther"));
//                                    JSONObject userJson = jsonObject.getJSONObject("user");
//                                    user.setUsername(userJson.getString("username"));
//                                    JSONObject userJsonProfile = userJson.getJSONObject("uProfile");
//                                    user.setAge(userJsonProfile.getInt("age"));
//                                    user.setGender(userJsonProfile.getString("gender"));
//                                    user.setCooking(userJsonProfile.getInt("cooking"));
//                                    user.setClean(userJsonProfile.getString("cleanliness"));
//                                    user.setSmoking(userJsonProfile.getBoolean("smoking"));
//                                    user.setAlcohol(Boolean.parseBoolean(userJsonProfile.getString("alcohol")));
//                                    user.setNoise(userJsonProfile.getString("noise"));
//                                    user.setSocialize(userJsonProfile.getString("socializing"));
//                                    user.setUpets(userJsonProfile.getBoolean("uPets"));
//                                    user.setSleep(userJsonProfile.getString("sleep"));
//                                    user.setFoodPref(userJsonProfile.getString("foodPreference"));
//                                    JSONObject preferredLocation = jsonObject.getJSONObject("preferredLocation");
//                                    JSONArray coordinates = preferredLocation.getJSONArray("coordinates");
//                                    user.setRoomieLat((Double) coordinates.get(1));
//                                    user.setRoomieLon((Double) coordinates.get(0));
//
//
//                                    String profileImgText = AppConfig.URL_DEFAULT + userJsonProfile.getString("profileImg");
//                                    user.setProfileImg(profileImgText);
//                                    roomieList.add(user);
//
//
//                                }
//                                browseRoomieAdapter.addAll(roomieList);
//                                // browseApartmentAdapter.notifyDataSetChanged();
////                                if(swipeContainer!=null)
////                                    swipeContainer.setRefreshing(false);
//
//
//                                Toast.makeText(getApplicationContext(),"Details: "+response.length(),Toast.LENGTH_LONG).show();
//                                Log.d("Details %s",response.toString());
//                            }
//                        } catch (JSONException e) {
//                            // JSON error
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//
//                    }}, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Token "+token);
//                return headers;
//            }
//        };
//
//        // Adding request to request queue
//        AppController.getInstance(this).addToRequestQueue(req,tag_string_req);
//        hideDialog();
//
//    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public interface CardClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private CardClickListener cardClickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final CardClickListener cardClickListener) {
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

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }



}
