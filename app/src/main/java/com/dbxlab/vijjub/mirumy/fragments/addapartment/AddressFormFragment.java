package com.dbxlab.vijjub.mirumy.fragments.addapartment;

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
import android.widget.Toast;

import com.dbxlab.vijjub.mirumy.R;
import com.dbxlab.vijjub.mirumy.adapters.AptAddress;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by vijjub on 9/12/16.
 */
public class AddressFormFragment extends Fragment {

    private static final String TAG = AddressFormFragment.class.getCanonicalName();
    public AptAddress address;
    private EditText street,city,country,state,zipcode;
    private ProgressDialog pDialog;
    LatLng latlng;
    Double lat,lon;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.address_form, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Address");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        street = (EditText)view.findViewById(R.id.street);
        city = (EditText)view.findViewById(R.id.town_district);
        state = (EditText)view.findViewById(R.id.state_province);
        country = (EditText)view.findViewById(R.id.country);
        zipcode = (EditText)view.findViewById(R.id.zip_postal_code);
        address = new AptAddress();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);




        return  view;
    }
    OnAddressSelectedListener onAddressFormSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onAddressFormSelectedListener = (OnAddressSelectedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnAccountInfoListener");

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
                    Toast.makeText(getActivity(),latlng.toString(),Toast.LENGTH_LONG).show();
                    onAddressFormSelectedListener.setAddressInfo(address);
                    } else {
                        latlng = getLocationFromAddress(getContext(),address.shorterAddress());

                        if(latlng != null){
                            lat = latlng.latitude;address.setLat(lat);
                            lon = latlng.longitude;address.setLon(lon);
                            Toast.makeText(getActivity(),latlng.toString(),Toast.LENGTH_LONG).show();
                            onAddressFormSelectedListener.setAddressInfo(address);
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

        if((street.getText()!=null) && (city.getText()!= null) && (country.getText()!= null) && (state.getText()!= null) && (zipcode.getText() != null)){

            return true;

        }
        else return false;
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


}
