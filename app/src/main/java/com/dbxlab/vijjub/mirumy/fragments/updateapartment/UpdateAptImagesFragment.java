package com.dbxlab.vijjub.mirumy.fragments.updateapartment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dbxlab.vijjub.mirumy.MainActivity;
import com.dbxlab.vijjub.mirumy.R;
import com.dbxlab.vijjub.mirumy.adapters.AddAptImagesAdapter;
import com.dbxlab.vijjub.mirumy.fragments.addapartment.AptImagesSlideshowFragment;
import com.dbxlab.vijjub.mirumy.imageutils.CameraImage;
import com.dbxlab.vijjub.mirumy.imageutils.GalleryImage;
import com.dbxlab.vijjub.mirumy.imageutils.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
 * Created by vijjub on 9/19/16.
 */
@RuntimePermissions
public class UpdateAptImagesFragment extends Fragment {

    private static final String TAG = UpdateAptImagesFragment.class.getSimpleName();
    final int CAMERA_REQUEST = 1000;
    final int GALLERY_REQUEST = 1001;
    private CameraImage cameraPhoto;
    private GalleryImage galleryPhoto;
    private ArrayList<String> images = new ArrayList<>();
    private RecyclerView recyclerView;
    private AddAptImagesAdapter addAptImagesAdapter;
    private Bitmap bitmap;
    private String encbitmap=null;
    private ProgressDialog pDialog;
    private int apartmentId;
    private String token;
    private long aiid;
    private SQliteManager db;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_apt_images, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if(((AppCompatActivity) getActivity()).getSupportActionBar()!= null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Images");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        Bundle bundle = getArguments();
        if(bundle != null){
            apartmentId = bundle.getInt("apartmentId");
            token = bundle.getString("token");
            aiid = bundle.getLong("aiid");
            images = bundle.getStringArrayList("images");
        }
        db = new SQliteManager(getContext());

        cameraPhoto = new CameraImage(getContext());
        galleryPhoto = new GalleryImage(getContext());

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        addAptImagesAdapter = new AddAptImagesAdapter(getContext(),images);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(addAptImagesAdapter);

        recyclerView.addOnItemTouchListener(new AddAptImagesAdapter.RecyclerTouchListener(getContext(), recyclerView, new AddAptImagesAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", images);
                bundle.putInt("position", position);

                FragmentManager ft = getActivity().getSupportFragmentManager();
                AptImagesSlideshowFragment newFragment = AptImagesSlideshowFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return  view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_apt_photos,menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case R.id.camera:
                UpdateAptImagesFragmentPermissionsDispatcher.openCameraWithCheck(this);
//                try {
//                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getContext(),
//                            "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
//                }
//                cameraPhoto.addToGallery();
                return true;

            case R.id.gallery:
//                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                UpdateAptImagesFragmentPermissionsDispatcher.openGalleryWithCheck(this);
                return true;

            case R.id.done:
                Intent intent = new Intent(getContext(),MainActivity.class);
                intent.putExtra("OPEN_APARTMENT_LIST",2);
                startActivity(intent);
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }


    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void openGallery() {
        startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);

    }

    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void openCamera() {
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
        }
        cameraPhoto.addToGallery();
    }


    @OnShowRationale({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForCamera(PermissionRequest request){
        showRationaleDialog(R.string.permission_camera_rationale,request);
    }


    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForGallery(PermissionRequest request){
        showRationaleDialog(R.string.permission_camera_rationale,request);
    }


    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void openCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(getActivity(), R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void openGalleryDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(getActivity(), R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }



    @OnNeverAskAgain({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void openCameraNeverAskAgain() {
        Toast.makeText(getActivity(), R.string.permission_camera_never_askagain, Toast.LENGTH_SHORT).show();
    }


    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void openGalleryNeverAskAgain() {
        Toast.makeText(getActivity(), R.string.permission_camera_never_askagain, Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(getActivity())
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
        UpdateAptImagesFragmentPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                images.add(photoPath);
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    encbitmap = getEncodedBitmap(bitmap);
                    uploadAptImage(encbitmap);

                } catch (FileNotFoundException e) {
                    Toast.makeText(getContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
                addAptImagesAdapter.notifyDataSetChanged();
            }
            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                images.add(photoPath);
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    encbitmap = getEncodedBitmap(bitmap);
                    uploadAptImage(encbitmap);

                } catch (FileNotFoundException e) {
                    Toast.makeText(getContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
                addAptImagesAdapter.notifyDataSetChanged();
//
            }
        }
    }

    private void uploadAptImage(String image) {
        // Tag used to cancel the request
        String tag_string_req = "uploadaptinfo";

        pDialog.setMessage("Getting Address info.......");
        showDialog();

        Map<String, String> params = new HashMap<String, String>();
        params.put("apartment",Integer.toString(apartmentId));
        params.put("image",image);


        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_ADD_APT_IMAGE,new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = response;
                    // Check for error node in json
                    if (jObj.has("id")) {
                        // Now store the user in SQLite
                        String imageUrl = jObj.getString("image");
                        int apartmentid = jObj.getInt("apartment");

                        db.addApartmentImages(imageUrl,apartmentid,aiid);

                        Log.d(TAG,imageUrl+aiid);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Log.i("UpdateAptImagesFrag %s", res.toString());

                        if(jObj.has("non_field_errors")) {

                            String errorMsg = jObj.getString("non_field_errors");
                            Toast.makeText(getContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                            Toast.makeText(getContext(),"Invalid Username/Password",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
                Log.e(TAG, "Upload Error: " + error.getMessage());
                // Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Token "+token);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance(getActivity()).addToRequestQueue(strReq, tag_string_req);
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

    public String getEncodedBitmap(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("Encoded String:",encodedImage);
        return encodedImage;
    }

}
