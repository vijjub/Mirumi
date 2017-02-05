package com.dbxlab.vijjub.mirumy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dbxlab.vijjub.mirumy.adapters.BrowseRoomieAdapter;
import com.dbxlab.vijjub.mirumy.adapters.User;

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

public class BrowseRoomiesActivity extends AppCompatActivity {
    private final String TAG = BrowseRoomiesActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private String token;
    private List<User> roomieList;
    private BrowseRoomieAdapter browseRoomieAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_browse_roomie);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        pDialog = new ProgressDialog(this);

        pDialog.setCancelable(false);


        final Context context = getApplicationContext();
        SQliteManager db = new SQliteManager(context);
        token = db.getTokenDetails();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_browse_roomie);
        roomieList = new ArrayList<User>();
        getBrowseAptDetails();
        for (int j = 0; j < roomieList.size(); j++)
            Log.d(TAG, "Fetching Complete Apartment details from Sqlite: " + roomieList.get(j).getLName());


        browseRoomieAdapter = new BrowseRoomieAdapter(roomieList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new BrowseRoomiesActivity.GridSpacingItemDecoration(2, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(browseRoomieAdapter);
        recyclerView.addOnItemTouchListener(new BrowseRoomiesActivity.RecyclerTouchListener(context, recyclerView, new BrowseRoomiesActivity.CardClickListener() {
            @Override
            public void onClick(View view, int position) {
                User user = roomieList.get(position);
//                onBrowseRoomieSelectListener.setBrowseRoomie(user);
                Intent intent = new Intent(BrowseRoomiesActivity.this, BrowseUserDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(context, Integer.toString(position) + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                browseRoomieAdapter.clear();
                getBrowseAptDetails();

            }

        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);


    }

    private void getBrowseAptDetails() {

        String tag_string_req = "browseRoomieDetails";


        pDialog.setMessage(" ...");
        showDialog();
        Log.d("Details %s", "insidethe function");
        JsonArrayRequest req = new JsonArrayRequest(AppConfig.URL_BROWSE_ROOMIE_LIST,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        browseRoomieAdapter.clear();

                        try {
                            JSONArray jsonArray = response;
                            roomieList.clear();

                            // Check for error node in json
                            if (jsonArray.length() > 0) {

                                for (int i = 0; i < jsonArray.length(); i++) {

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


                                    String profileImgText = userJsonProfile.getString("profileImg");
                                    user.setProfileImg(profileImgText);
                                    roomieList.add(user);


                                }
                                browseRoomieAdapter.notifyDataSetChanged();
                                // browseApartmentAdapter.notifyDataSetChanged();
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
        AppController.getInstance(this).addToRequestQueue(req, tag_string_req);
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
        private BrowseRoomiesActivity.CardClickListener cardClickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BrowseRoomiesActivity.CardClickListener cardClickListener) {
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
