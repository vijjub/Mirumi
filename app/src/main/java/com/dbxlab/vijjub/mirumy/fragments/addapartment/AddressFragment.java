package com.dbxlab.vijjub.mirumy.fragments.addapartment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dbxlab.vijjub.mirumy.R;
import com.dbxlab.vijjub.mirumy.adapters.AptAddress;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vijjub on 9/12/16.
 */
public class AddressFragment extends Fragment {
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String TAG = "AddressFragment";
    public AptAddress address;
    OnAddressSelectedListener onAddressSelectedListener;
    private TextView completeAddress;
    private Button addressNotFound;

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.apartment_address, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Address");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        address = new AptAddress();

        RelativeLayout search = (RelativeLayout) view.findViewById(R.id.address_relative);
        completeAddress = (TextView) view.findViewById(R.id.complete_address);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });
        addressNotFound = (Button)view.findViewById(R.id.buttonAddressNotFound);
        addressNotFound.setVisibility(View.GONE);
        addressNotFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressFormFragment nextFrag= new AddressFormFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onAddressSelectedListener = (OnAddressSelectedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnAccountInfoListener");

        }
    }

    private void openAutocompleteActivity() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY ).setFilter(typeFilter)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {

            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),0 ).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place Selected: " + place.getAddress()   );


                completeAddress.setText(place.getAddress());
                address.setFullAddress((String) place.getAddress());
                LatLng latLng = place.getLatLng();
                address.setLat(latLng.latitude);
                address.setLon(latLng.longitude);
                Fragment fragment = Fragment.instantiate(getContext(), MapsActivity.class.getName());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Bundle location = new Bundle();
                location.putDouble("lat", place.getLatLng().latitude);
                location.putDouble("lon",place.getLatLng().longitude);
                location.putString("address", String.valueOf(place.getAddress()));
                fragment.setArguments(location);
                ft.replace(R.id.map, fragment,"Detail Fragment");
                ft.commit();
                addressNotFound.setVisibility(View.VISIBLE);




            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_address,menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case R.id.done:
                boolean result = checkAddress();
                if(result==true)
                    onAddressSelectedListener.setAddressInfo(address);
                else Toast.makeText(getActivity(),"Address is Empty.Please select an Address from search tool",Toast.LENGTH_LONG).show();
                return true;

            case android.R.id.home:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }

    private boolean checkAddress() {

        return address.getFullAddress() != null;
    }


}
