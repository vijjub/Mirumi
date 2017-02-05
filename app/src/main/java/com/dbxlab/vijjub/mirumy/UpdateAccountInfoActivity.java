package com.dbxlab.vijjub.mirumy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dbxlab.vijjub.mirumy.adapters.User;
import com.dbxlab.vijjub.mirumy.fragments.AccountInfoClayout;
import com.dbxlab.vijjub.mirumy.fragments.ProfileImageDialogFragment;
import com.dbxlab.vijjub.mirumy.fragments.ProfileImageDialogInterface;
import com.dbxlab.vijjub.mirumy.imageutils.CameraImage;
import com.dbxlab.vijjub.mirumy.imageutils.GalleryImage;
import com.dbxlab.vijjub.mirumy.imageutils.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQliteManager;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by vijjub on 9/1/16.
 */
@RuntimePermissions
public class UpdateAccountInfoActivity extends AppCompatActivity implements ProfileImageDialogInterface {

    private static final String TAG = UpdateAccountInfoActivity.class.getSimpleName();
    final int CAMERA_REQUEST = 1000;
    final int GALLERY_REQUEST = 1001;
int genderOption=0;
int foodoption=0;
int cleanoption = 0;
    String[] genderList;
    String[] cleanlist;
    private TextView name, email, joined;
        private ImageView profileImage;    private CameraImage cameraPhoto;    private GalleryImage galleryPhoto;
    private Bitmap bitmap;
    private String encbitmap=null;
    private ProgressDialog pDialog;
    private SQliteManager db;
    private int checkedColor ;
    private RadioGroup socialGroup,noisegroup,sleepGroup;
    private RadioButton vsocial,msocial,nsocial,keeptomyself,loud,dontcaremusic,morning,night,nospecifictime;
    private CheckedTextView smoking,alcohol,pets;
    private String genderText;
    private String[] foodList;
    private ColorStateList textColorUnchecked;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update_clayout);
        Bundle bundle = this.getIntent().getExtras();
        final User user = (User) bundle.getSerializable("user");
        cameraPhoto = new CameraImage(getApplicationContext());
        galleryPhoto = new GalleryImage(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQliteManager(getApplicationContext());

        String fullNameText = user.getFName()+" "+user.getLName();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Edit Account");
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.apploizc_transparent_color));

        TextView name = (TextView)findViewById(R.id.fullname);
        name.setText(fullNameText);


        TextView email = (TextView)findViewById(R.id.email);
        email.setText(user.getEmail());

        profileImage = (ImageView) findViewById(R.id.profileimage);
        Glide.with(this).load(user.getProfileImg())
                .error(R.drawable.men)
                .skipMemoryCache(true)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(profileImage);


        FloatingActionButton cameraIcon = (FloatingActionButton) findViewById(R.id.ivCamera);
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateAccountInfoActivityPermissionsDispatcher.openCameraWithCheck(UpdateAccountInfoActivity.this);
                //ProfileImageDialogFragment.newInstance().show(getFragmentManager(), "dialog");
            }
        });

        final EditText age = (EditText) findViewById(R.id.age);
        age.setText(Integer.toString(user.getAge()), TextView.BufferType.EDITABLE);


        final TextView gender = (TextView) findViewById(R.id.gender);
        genderList = getResources().getStringArray(R.array.gender);

        genderText = user.getGender();
        gender.setText(genderText);
        if(genderText.equals("M"))
            genderOption = 0;
        if(genderText.equals("F"))
            genderOption = 1;
        if(genderText.equals("A"))
            genderOption = 2;
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAccountInfoActivity.this);
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

        if(genderOption == 0)
            genderText = "M";
        if(genderOption ==1)
            genderText = "F";
        if(genderOption == 2)
            genderText = "O";


//        final EditText ucost = (EditText) findViewById(R.id.ucost);
//        ucost.setText(Integer.toString(user.getUcost()),TextView.BufferType.EDITABLE);

        final TextView cooking = (TextView) findViewById(R.id.cookingno);
        cooking.setText(Integer.toString(user.getCooking()), TextView.BufferType.EDITABLE);

        final TextView foodPref = (TextView) findViewById(R.id.food_pref);
        foodList = getResources().getStringArray(R.array.foodPreference);

        for(String s: foodList){
            if(s.equals(user.getFoodPref()))
                foodoption = Arrays.asList(foodList).indexOf(user.getFoodPref());

        }

        foodPref.setText(user.getFoodPref());
        foodPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAccountInfoActivity.this);
                builder.setTitle(R.string.food_text1)
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setSingleChoiceItems(R.array.foodPreference, foodoption ,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        foodPref.setText(foodList[i]);
                                        foodoption = i;
                                        dialogInterface.dismiss();
                                    }
                                });


                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        final TextView clean = (TextView) findViewById(R.id.cleanliness);
        cleanlist = getResources().getStringArray(R.array.cleanliness);
        for(String s: cleanlist){
            if(s.equals(user.getClean()))
                cleanoption = Arrays.asList(cleanlist).indexOf(user.getClean());
        }
        clean.setText(user.getClean());

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAccountInfoActivity.this);
                builder.setTitle(R.string.cleanText)
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setSingleChoiceItems(R.array.cleanliness, cleanoption ,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        clean.setText(cleanlist[i]);
                                        cleanoption = i;
                                        dialogInterface.dismiss();
                                    }
                                });


                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        smoking = (CheckedTextView) findViewById(R.id.checkedBoxSmking);
        textColorUnchecked = smoking.getTextColors();
        checkedColor= ContextCompat.getColor(this,R.color.colorPrimary);
        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);

//        if(user.isSmoking()){
//            smoking.toggle();
//            smoking.setTextColor(checkedColor);
//        }
//
//        smoking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                smoking.toggle();
//                smoking.startAnimation(myFadeInAnimation);
//                toggleTextColor(smoking);
//
//            }
//        });

//        if(user.isSmoking()){
//            smoking.toggle();
//            smoking.setTextColor(checkedColor);
//        }
//        smoking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                smoking.toggle();
//                smoking.startAnimation(myFadeInAnimation);
//                toggleTextColor(smoking);
//
//            }
//        });
        smoking = (CheckedTextView) findViewById(R.id.checkedBoxSmking);
        if(user.isSmoking()){
            smoking.toggle();
            toggleTextColor(smoking);
        }
        smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoking.toggle();
                smoking.startAnimation(myFadeInAnimation);
                toggleTextColor(smoking);
            }
        });

        alcohol = (CheckedTextView)findViewById(R.id.alchol);
        if(user.isAlcohol()){
            alcohol.toggle();
            alcohol.setTextColor(checkedColor);
        }
        alcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alcohol.toggle();
                alcohol.startAnimation(myFadeInAnimation);
                toggleTextColor(alcohol);

            }
        });

        pets = (CheckedTextView)findViewById(R.id.lovePets);
        if(user.isUpets()){
            pets.toggle();
            pets.setTextColor(checkedColor);
        }
        pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pets.toggle();
                pets.startAnimation(myFadeInAnimation);
                toggleTextColor(pets);

            }
        });



        socialGroup = (RadioGroup) findViewById(R.id.socialgroup);
        vsocial = (RadioButton)findViewById(R.id.vsocial);
        msocial = (RadioButton)findViewById(R.id.msocial);
        nsocial = (RadioButton)findViewById(R.id.nsocial);

        if(user.isSocialize().equals("Very Social"))
            vsocial.toggle();
        if(user.isSocialize().equals("Moderately Social"))
            msocial.toggle();
        if(user.isSocialize().equals("I keep to myself"))
            nsocial.toggle();


        socialGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        int selectedSocialId = socialGroup.getCheckedRadioButtonId();

        // find which radioButton is checked by id

        if(selectedSocialId == vsocial.getId()) {
            user.setSocialize("Very Social");

        } else if(selectedSocialId == msocial.getId()) {
            user.setSocialize("Moderately Social");


        } else {
            user.setSocialize("I keep to myself");

        }


        noisegroup = (RadioGroup) findViewById(R.id.socialgroup);
        keeptomyself = (RadioButton)findViewById(R.id.keeptomyself);
        loud = (RadioButton)findViewById(R.id.loud);
        dontcaremusic = (RadioButton)findViewById(R.id.dontcaremusic);

        if(user.isNoise().equals("No"))
            keeptomyself.toggle();
        if(user.isSocialize().equals("Yes"))
            loud.toggle();
        if(user.isSocialize().equals("I don't care"))
            dontcaremusic.toggle();


        noisegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        int selectedNoiseId = noisegroup.getCheckedRadioButtonId();

        // find which radioButton is checked by id

        if(selectedNoiseId == keeptomyself.getId()) {
            user.setNoise("No");

        } else if(selectedNoiseId == loud.getId()) {
            user.setNoise("Yes");


        } else {
            user.setNoise("I don't care");

        }


        sleepGroup = (RadioGroup) findViewById(R.id.sleepGroup);
        morning = (RadioButton)findViewById(R.id.morning);
        night = (RadioButton)findViewById(R.id.night);
        nospecifictime = (RadioButton)findViewById(R.id.nospecifictime);

        if(user.getSleep().equals("Morning Person"))
            morning.toggle();
        if(user.getSleep().equals("Night Owl"))
            loud.toggle();
        if(user.getSleep().equals("No Specific time"))
            dontcaremusic.toggle();


        noisegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        int selectedSleepId = sleepGroup.getCheckedRadioButtonId();

        // find which radioButton is checked by id

        if(selectedSleepId == morning.getId()) {
            user.setSleep("Morning Person");

        } else if(selectedSleepId == night.getId()) {
            user.setSleep("Night Owl");


        } else {
            user.setSleep("No Specific time");

        }





//        final TextView sleep = (TextView) findViewById(R.id.);
//        sleep.setText(user.getSleep());

        TextView joinedat = (TextView)findViewById(R.id.joined);
        String dateJoined = user.getJoined();
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
//        joinedat.append();



        FloatingActionButton updateInfo = (FloatingActionButton) findViewById(R.id.button_edit_account);
        if (updateInfo != null) {
            updateInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int ageText = Integer.parseInt(age.getText().toString());
                    user.setAge(ageText);

                   // String genderText = gender.getText().toString();
                    user.setGender(genderText);

    //                int uCostText = Integer.parseInt(ucost.getText().toString());
    //                user.setUcost(uCostText);

                    int cookingText = Integer.parseInt(cooking.getText().toString());
                    user.setCooking(cookingText);

                    String foodPrefText = foodPref.getText().toString();
                    user.setFoodPref(foodPrefText);

    //                Boolean roomieText = Boolean.parseBoolean(roomie.getText().toString());
    //                user.setRoomie(roomieText);

                    String cleanText = clean.getText().toString();
                    user.setClean(cleanText);

//                    String sleepText = sleep.getText().toString();
//                    user.setSleep(sleepText);

                    user.setSmoking(smoking.isChecked());

                    user.setAlcohol(alcohol.isChecked());

                    user.setUpets(pets.isChecked());

                    if(encbitmap!=null)
                        user.setProfileImg(encbitmap);

                    updateUser(user);



                }
            });
        }

    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void openCamera() {
        ProfileImageDialogFragment.newInstance().show(getFragmentManager(), "dialog");

    }

    @OnShowRationale({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForCamera(PermissionRequest request){
        showRationaleDialog(R.string.permission_camera_rationale,request);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void onCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_never_askagain, Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        UpdateAccountInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }




    private void toggleTextColor(CheckedTextView checkbox) {

        checkbox.toggle();
        if(checkbox.isChecked())
            checkbox.setTextColor(getResources().getColor(R.color.colorPrimary));
        else
            checkbox.setTextColor(textColorUnchecked);
    }


    public String getEncodedBitmap(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("Encoded String:",encodedImage);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String photoPath = cameraPhoto.getPhotoPath();

                Glide.with(this).load(new File(photoPath))
                        .error(R.drawable.men)
                        .centerCrop()
                        .into(profileImage);

                // images.add(photoPath);
                //  galleryAdapter.notifyDataSetChanged();
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    encbitmap = getEncodedBitmap(bitmap);
                    //profileImage.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();

                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                Glide.with(this).load(new File(photoPath))
                        .error(R.drawable.men)
                        .centerCrop()
                        .into(profileImage);

                // images.add(photoPath);
                // galleryAdapter.notifyDataSetChanged();
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    encbitmap = getEncodedBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onCancel() {
        return;
    }

    @Override
    public void onSelectCamera() throws IOException {
        startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
        cameraPhoto.addToGallery();
    }

    @Override
    public void onSelectGallery() {
        startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);

    }

    private void updateUser(final User user) {
        // Tag used to cancel the request
        String tag_string_req = "updateuserinfo";

        pDialog.setMessage("Updating.......");
        showDialog();

        Map<String, String> params = new HashMap<String, String>();
        params.put("username",user.getUsername());
        params.put("email",user.getEmail());
        params.put("first_name",user.getFName());
        params.put("last_name",user.getLName());
        params.put("age",Integer.toString(user.getAge()));
        params.put("gender",user.getGender());
        params.put("profileImg",user.getProfileImg());
//        params.put("otherRoomie",Boolean.toString(user.isOtherRoomie()));
//        params.put("duration",user.getDuration());
//        params.put("dateMovin",user.getMovinDate());


        Log.d(TAG,user.getProfileImg());



//        params.put("uCost",Integer.toString(user.getUcost()));
        params.put("cooking",Integer.toString(user.getCooking()));
        params.put("foodPreference",user.getFoodPref());
//        params.put("roomie", Boolean.toString(user.isRoomie()));
        params.put("cleanliness",user.getClean());
        params.put("smoking",Boolean.toString(user.isSmoking()));
        params.put("alcohol",Boolean.toString(user.isAlcohol()));
        params.put("noise",user.isNoise());
        params.put("socializing",user.isSocialize());
        params.put("uPets",Boolean.toString(user.isUpets()));
        params.put("sleep",user.getSleep());

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.PUT,
                AppConfig.URL_UPDATE_DETAILS,new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = response;

                    // Check for error node in json
                    if (jObj.has("username")) {
                       // session.setLogin(true);

                        // Now store the user in SQLite
                        String imageUrl = jObj.getString("profileImg");
                        Log.d(TAG,imageUrl);

                        db.updateUserDetails(jObj,user.getRow_id());
                        // Launch main activity
                        Intent intent = new Intent(UpdateAccountInfoActivity.this,AccountInfoClayout.class);
//                        intent.putExtra("OPEN_ACCOUNT_INFO",1);
//                        Log.d(TAG,"Started Activity 1");

                        startActivity(intent);
//                        Log.d(TAG,"Started Activity 2");
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

                NetworkResponse nresponse = error.networkResponse;
                if (error instanceof ServerError && nresponse != null) {
                    try {
                        String res = new String(nresponse.data);
                        // Now you can use any deserializer to make sense of data
                        JSONObject jObj = new JSONObject(res);
                        Log.i("StartActivity %s", res.toString());

                        if(jObj.has("non_field_errors")) {

                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("non_field_errors");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"Invalid Username/Password",Toast.LENGTH_LONG).show();
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
                headers.put("Authorization", "Token "+user.getToken());
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance(this).addToRequestQueue(strReq, tag_string_req);
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
    protected void onDestroy() {
        super.onDestroy();

    }
}
