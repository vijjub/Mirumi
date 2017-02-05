package com.dbxlab.vijjub.mirumy.fragments.updateapartment;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dbxlab.vijjub.mirumy.R;
import com.dbxlab.vijjub.mirumy.adapters.AptAddress;
import com.dbxlab.vijjub.mirumy.adapters.UserApartment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by vijjub on 9/19/16.
 */
public class UpdateAddressFormFragment extends Fragment {

    private static final String TAG = UpdateAddressFormFragment.class.getCanonicalName();
    public AptAddress address;
    LatLng latlng;
    Double lat,lon;
    UserApartment apartment;
    String addresstext;
    OnUpdateAddressListener onUpdateAddressListener;
    private EditText street,city,country,state,zipcode;
    private ProgressDialog pDialog;

    public static UpdateAddressFormFragment newInstance(Bundle bundle) {
        UpdateAddressFormFragment updateAddressFormFragment = new UpdateAddressFormFragment();
        updateAddressFormFragment.setArguments(bundle);
        return updateAddressFormFragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.address_form, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Address");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        address = new AptAddress();
        StringTokenizer tokenizer = new StringTokenizer(addresstext,",");

        street = (EditText)view.findViewById(R.id.street);
        street.setText(tokenizer.nextElement().toString(), TextView.BufferType.EDITABLE);

        city = (EditText)view.findViewById(R.id.town_district);
        city.setText(tokenizer.nextElement().toString(),TextView.BufferType.EDITABLE);

        state = (EditText)view.findViewById(R.id.state_province);
        String statezip = tokenizer.nextElement().toString();
        StringTokenizer tokenizer1 = new StringTokenizer(statezip," ");
        state.setText(tokenizer1.nextElement().toString(),TextView.BufferType.EDITABLE);

        zipcode = (EditText)view.findViewById(R.id.zip_postal_code);
        zipcode.setText(tokenizer1.nextElement().toString(),TextView.BufferType.EDITABLE);


        country = (EditText)view.findViewById(R.id.country);
        country.setText(tokenizer.nextElement().toString(),TextView.BufferType.EDITABLE);


        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);




        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onUpdateAddressListener = (OnUpdateAddressListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnAccountInfoListener");

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apartment = (UserApartment)getArguments().getSerializable("userApartment");
        addresstext = apartment.getAddress();
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
                boolean result = checkForm();
                if(result==true){
                    address.setStreet(street.getText().toString());
                    address.setCity(city.getText().toString());
                    address.setCountry(country.getText().toString());
                    address.setState(state.getText().toString());
                    address.setZipcode(zipcode.getText().toString());
                    latlng = getLocationFromAddress(getContext(),address.toString());


                    if(latlng!= null){
                        lat = latlng.latitude;address.setLat(lat);
                        lon = latlng.longitude;address.setLon(lon);
                        apartment.setAddress(address.toString());
                        apartment.setLon(lon);
                        apartment.setLat(lat);
                        //Toast.makeText(getActivity(),apartment.getAddress(),Toast.LENGTH_LONG).show();
                        onUpdateAddressListener.setUpdateAddressInfo(apartment);
                    } else {
                        latlng = getLocationFromAddress(getContext(),address.shorterAddress());

                        if(latlng != null){
                            lat = latlng.latitude;address.setLat(lat);
                            lon = latlng.longitude;address.setLon(lon);
                            apartment.setAddress(address.toString());
                            apartment.setLon(lon);
                            apartment.setLat(lat);
                           // Toast.makeText(getActivity(),apartment.getAddress(),Toast.LENGTH_LONG).show();
                            onUpdateAddressListener.setUpdateAddressInfo(apartment);
                        }
                        else  Toast.makeText(getActivity(),"Enter a Valid Address",Toast.LENGTH_LONG).show();
                    }

                }
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

    private boolean checkForm() {

        return (street.getText() != null) && (city.getText() != null) && (country.getText() != null) && (state.getText() != null) && (zipcode.getText() != null);
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
