package com.dbxlab.vijjub.mirumy.fragments.updateapartment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.dbxlab.vijjub.mirumy.R;
import com.dbxlab.vijjub.mirumy.adapters.AptAddress;
import com.dbxlab.vijjub.mirumy.adapters.UserApartment;
import com.dbxlab.vijjub.mirumy.fragments.DatePickerFragment;

import org.florescu.android.rangeseekbar.RangeSeekBar;
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
 * Created by vijjub on 9/19/16.
 */
public class UpdateAptFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private static final String TAG = UpdateAptFragment.class.getSimpleName();
    private static final String GenderMale = "M";
    private static final String GenderFemale = "F";
    private static final String GenderAny = "A";
    ColorStateList textColorUnchecked;
    OnApartmentUpdateListener onApartmentUpdateListener;
    private EditText desc;
    private TextView rooms;
    private EditText rent;
    private EditText utilities;
    private TextView vacancy;
    private CheckedTextView internet;
    private CheckedTextView gym;
    private CheckedTextView pets;
    private RadioButton checkbox;
    private AptAddress address;
    private String addressStr;
    private UserApartment apartment;
    private ProgressDialog pDialog;
    private double lat,lon;
    private SQliteManager db;
    private HashMap<String,String> user;
    private int apartmentID;
    private EditText duration;
    private CheckedTextView pool,wifi,music,guests,drugs,latenights,washer,genderM,genderF,genderA;
    private CheckedTextView dryer,furnished,kitchen,closet, ac, heater, smoking, hasPet;
    private EditText age,deposit;
    private int checkedColor ;
    private ImageButton minusRooms,plusRooms,minusVacancy,plusVacancy;
    private int roomsCount,vacancyCount;
    private Integer ageMin,ageMax;
    private EditText placeType;
    private String[] placeTypeList;
    private String[] durationList;
    private EditText movin;
    private String genderOption = "";
    private Switch occupant;
    private boolean occupantvalue;

    public static UpdateAptFragment newInstance(Bundle bundle) {
        UpdateAptFragment updateAptFragment = new UpdateAptFragment();
        updateAptFragment.setArguments(bundle);
        return updateAptFragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_update_apartment, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if(((AppCompatActivity) getActivity()).getSupportActionBar()!=null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Apartment");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new SQliteManager(getContext());
        user = db.getProfileDetails();

        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);


        RelativeLayout addresslayout = (RelativeLayout) view.findViewById(R.id.address_relative);
        addresslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle outState = new Bundle();
                setApartmentDetails();
//                apartment.setDesc(desc.getText().toString());
//                apartment.setRooms(Integer.parseInt(rooms.getText().toString()));
//                apartment.setRent(Integer.parseInt(rent.getText().toString()));
//                apartment.setUtilities(Integer.parseInt(utilities.getText().toString()));
//               // apartment.setLaundry(Boolean.parseBoolean(laundry.getText().toString()));
//                apartment.setInternet(Boolean.parseBoolean(internet.getText().toString()));
//                apartment.setGym(Boolean.parseBoolean(gym.getText().toString()));
//                apartment.setPets(Boolean.parseBoolean(pets.getText().toString()));
                outState.putSerializable("userApartment",apartment);
                UpdateAddressFragment nextFrag= UpdateAddressFragment.newInstance(outState);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, nextFrag)
                        .commit();
            }
        });
        checkbox = (RadioButton)view.findViewById(R.id.checkBox);
        checkbox.toggle();
//        Bundle bundle = getArguments();
//        if(bundle!= null){
//            address = (AptAddress) bundle.getSerializable("address");
//            checkbox.toggle();
//            //Toast.makeText(getActivity(),address.toString(),Toast.LENGTH_LONG).show();
//            if(address!=null) {
//                if (address.getFullAddress() != null)
//                    addressStr = address.getFullAddress();
//                else addressStr = address.toString();
//                lat = address.getLat();
//                lon = address.getLon();
//
//                apartment.setAddress(addressStr);
//                apartment.setLat(lat);
//                apartment.setLon(lon);
//            }
//        }

        desc = (EditText)view.findViewById(R.id.desc);
        desc.setText(apartment.getDesc(), TextView.BufferType.EDITABLE);

        rooms = (TextView)view.findViewById(R.id.roomcount);
        rooms.setText(Integer.toString(apartment.getRooms()),TextView.BufferType.EDITABLE);
        roomsCount = apartment.getRooms();
        vacancyCount = apartment.getVacancy();
        minusRooms = (ImageButton)view.findViewById(R.id.minusRooms);
        plusRooms = (ImageButton)view.findViewById(R.id.plusRooms);
        minusRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomsCount>1){
                    roomsCount = roomsCount-1;
                    rooms.setText(Integer.toString(roomsCount));
                }
            }
        });

        plusRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomsCount>0 && roomsCount<10) {
                    roomsCount = roomsCount + 1;
                    rooms.setText(Integer.toString(roomsCount));
                }
            }
        });

        minusVacancy = (ImageButton)view.findViewById(R.id.minusVacancy);
        plusVacancy = (ImageButton)view.findViewById(R.id.plusVacancy);
        vacancy = (TextView)view.findViewById(R.id.vacancyCount);
        vacancy.setText(Integer.toString(apartment.getVacancy()),TextView.BufferType.EDITABLE);
        minusVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vacancyCount>1){
                    vacancyCount = vacancyCount-1;
                    vacancy.setText(Integer.toString(vacancyCount));
                }
            }
        });

        plusVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vacancyCount>0 && vacancyCount<10){
                    vacancyCount = vacancyCount + 1;
                    vacancy.setText(Integer.toString(vacancyCount));
                }
            }
        });

        rent = (EditText)view.findViewById(R.id.rent);
        rent.setText(Integer.toString(apartment.getRent()),TextView.BufferType.EDITABLE);

        utilities = (EditText)view.findViewById(R.id.utilities);
        utilities.setText(Integer.toString(apartment.getUtilities()),TextView.BufferType.EDITABLE);

        deposit = (EditText)view.findViewById(R.id.deposit);
        deposit.setText(Integer.toString(apartment.isDeposit()),TextView.BufferType.EDITABLE);

        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(getContext());
        rangeSeekBar.setRangeValues(17, 60);
        ageMax = apartment.isAptAgeMax();
        ageMin = apartment.isAptAgeMin();
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
        LinearLayout seekBarLayout = (LinearLayout)view.findViewById(R.id.seek_bar_layout);
        seekBarLayout.addView(rangeSeekBar);

        placeType = (EditText)view.findViewById(R.id.placetype);
        placeType.setText(apartment.getPlaceType(),TextView.BufferType.EDITABLE);
        placeTypeList = getResources().getStringArray(R.array.placetype);
        placeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.placetype_text)
                        .setItems(R.array.placetype, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                placeType.setText(placeTypeList[i]);
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        duration = (EditText)view.findViewById(R.id.duration);
        duration.setText(apartment.getDuration(),TextView.BufferType.EDITABLE);
        durationList = getResources().getStringArray(R.array.duration_list);
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        occupant = (Switch) view.findViewById(R.id.occupantswitch);
//        occupantvalue = apartment.isOccupant();
//        if(occupantvalue)
//            occupant.setChecked(true);

        occupant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    apartment.setOccupant(true);

                else
                    apartment.setOccupant(false);

            }
        });






        movin = (EditText)view.findViewById(R.id.movindate);
        movin.setText(apartment.getDateMovin(),TextView.BufferType.EDITABLE);
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        movin.setText(sdf.format(calendar.getTime()));
        movin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setCallBackUpdateApt(UpdateAptFragment.this);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        checkedColor= ContextCompat.getColor(getContext(),R.color.colorPrimary);

        internet = (CheckedTextView) view.findViewById(R.id.checkBox_internet);
        textColorUnchecked = internet.getTextColors();

        if(apartment.isInternet()){
            internet.toggle();
            internet.setTextColor(checkedColor);
        }
        internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internet.startAnimation(myFadeInAnimation);
                toggleTextColor(internet);
            }
        });


        gym = (CheckedTextView) view.findViewById(R.id.checkBox_gym);
        if(apartment.isGym()){
            gym.toggle();
            gym.setTextColor(checkedColor);
        }
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gym.startAnimation(myFadeInAnimation);
                toggleTextColor(gym);
            }
        });


        pets = (CheckedTextView) view.findViewById(R.id.checkBox_pets);
        if(apartment.isPets()){
            pets.toggle();
            pets.setTextColor(checkedColor);
        }
        pets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pets.startAnimation(myFadeInAnimation);
                toggleTextColor(pets);
            }
        });


        pool = (CheckedTextView) view.findViewById(R.id.checkBox_pool);
        if(apartment.isPool()){
            pool.toggle();
            pool.setTextColor(checkedColor);
        }
        pool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pool.startAnimation(myFadeInAnimation);
                toggleTextColor(pool);
            }
        });


        wifi = (CheckedTextView) view.findViewById(R.id.checkbox_wifi);
        if(apartment.isWifi()){
            wifi.toggle();
            wifi.setTextColor(checkedColor);
        }
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifi.startAnimation(myFadeInAnimation);
                toggleTextColor(wifi);
            }
        });


        music = (CheckedTextView) view.findViewById(R.id.checkBox_music);
        if(apartment.isMusic()){
            music.toggle();
            music.setTextColor(checkedColor);
        }
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music.startAnimation(myFadeInAnimation);
                toggleTextColor(music);
            }
        });


        guests = (CheckedTextView) view.findViewById(R.id.checkbox_guests);
        if(apartment.isGuests()){
            guests.toggle();
            guests.setTextColor(checkedColor);
        }
        guests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guests.startAnimation(myFadeInAnimation);
                toggleTextColor(guests);
            }
        });


        drugs = (CheckedTextView) view.findViewById(R.id.checkBox_drugs);
        if(apartment.isDrugs()){
            drugs.toggle();
            drugs.setTextColor(checkedColor);
        }
        drugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drugs.startAnimation(myFadeInAnimation);
                toggleTextColor(drugs);
            }
        });


        latenights = (CheckedTextView) view.findViewById(R.id.checkBox_late_nights);
        if(apartment.isLateNights()){
            latenights.toggle();
            latenights.setTextColor(checkedColor);
        }
        latenights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latenights.startAnimation(myFadeInAnimation);
                toggleTextColor(latenights);
            }
        });


        washer = (CheckedTextView) view.findViewById(R.id.checkbox_washer);
        if(apartment.isWasher()){
            washer.toggle();
            washer.setTextColor(checkedColor);
        }
        washer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                washer.startAnimation(myFadeInAnimation);
                toggleTextColor(washer);
            }
        });

        dryer = (CheckedTextView) view.findViewById(R.id.checkBox_dryer);
        if(apartment.isDryer()){
            dryer.toggle();
            dryer.setTextColor(checkedColor);
        }
        dryer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dryer.startAnimation(myFadeInAnimation);
                toggleTextColor(dryer);
            }
        });

        furnished = (CheckedTextView) view.findViewById(R.id.checkBox_furnished);
        if(apartment.isFurnished()){
            furnished.toggle();
            furnished.setTextColor(checkedColor);
        }
        furnished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                furnished.startAnimation(myFadeInAnimation);
                toggleTextColor(furnished);
            }
        });

        kitchen = (CheckedTextView) view.findViewById(R.id.checkBox_kitchen);
        if(apartment.isPets()){
            kitchen.toggle();
            kitchen.setTextColor(checkedColor);
        }
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kitchen.startAnimation(myFadeInAnimation);
                toggleTextColor(kitchen);
            }
        });

        closet = (CheckedTextView) view.findViewById(R.id.checkBox_closet);
        if(apartment.isCloset()){
            closet.toggle();
            closet.setTextColor(checkedColor);
        }
        closet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closet.startAnimation(myFadeInAnimation);
                toggleTextColor(closet);
            }
        });

        ac = (CheckedTextView) view.findViewById(R.id.checkbox_ac);
        if(apartment.isAC()){
            ac.toggle();
            ac.setTextColor(checkedColor);
        }
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.startAnimation(myFadeInAnimation);
                toggleTextColor(ac);
            }
        });

        heater = (CheckedTextView) view.findViewById(R.id.checkBox_heater);
        if(apartment.isHeater()){
            heater.toggle();
            heater.setTextColor(checkedColor);
        }
        heater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heater.startAnimation(myFadeInAnimation);
                toggleTextColor(heater);
            }
        });

        hasPet = (CheckedTextView) view.findViewById(R.id.checkBox_pets_allowed);
        if(apartment.isHasPet()){
            hasPet.toggle();
            hasPet.setTextColor(checkedColor);
        }
        hasPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasPet.startAnimation(myFadeInAnimation);
                toggleTextColor(hasPet);
            }
        });

        smoking = (CheckedTextView) view.findViewById(R.id.checkbox_smoking);
        if(apartment.getSmoking()){
            smoking.toggle();
            smoking.setTextColor(checkedColor);
        }
        smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoking.startAnimation(myFadeInAnimation);
                toggleTextColor(smoking);
            }
        });


        genderM = (CheckedTextView)view.findViewById(R.id.checkboxMale);
        genderF = (CheckedTextView)view.findViewById(R.id.checkboxFemale);
        genderA = (CheckedTextView)view.findViewById(R.id.checkedBoxAny);

        genderOption = apartment.getAptGender();
        if(genderOption.equals("M")){
            genderM.toggle();
            genderM.setTextColor(checkedColor);
        }
        if(genderOption.equals("F")){
            genderF.toggle();
            genderF.setTextColor(checkedColor);
        }
        if(genderOption.equals("A")){
            genderA.toggle();
            genderA.setTextColor(checkedColor);
        }

        genderM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderM.toggle();
                if(genderM.isChecked()){
                    genderM.setTextColor(checkedColor);
                    if(genderA.isChecked()){
                        genderA.toggle();
                        genderA.setTextColor(textColorUnchecked);
                    }
                    if(genderF.isChecked()){
                        genderF.toggle();
                        genderF.setTextColor(textColorUnchecked);
                    }
                    genderOption = GenderMale;

                }
                else{
                    genderM.setTextColor(textColorUnchecked);genderOption = "";}

//                Toast.makeText(getContext(),genderOption,Toast.LENGTH_SHORT).show();

            }
        });

        genderF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderF.toggle();
                if(genderF.isChecked()){
                    genderF.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    if(genderM.isChecked()){
                        genderM.toggle();
                        genderM.setTextColor(textColorUnchecked);
                    }
                    if(genderA.isChecked()){
                        genderA.toggle();
                        genderA.setTextColor(textColorUnchecked);
                    }
                    genderOption = GenderFemale;

                }
                else{
                    genderF.setTextColor(textColorUnchecked);genderOption = "";}
//                Toast.makeText(getContext(),genderOption,Toast.LENGTH_SHORT).show();

            }
        });

        genderA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderA.toggle();
                if(genderA.isChecked()){
                    genderA.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    if(genderM.isChecked()){
                        genderM.toggle();
                        genderM.setTextColor(textColorUnchecked);
                    }
                    if(genderF.isChecked()){
                        genderF.toggle();
                        genderF.setTextColor(textColorUnchecked);
                    }
                    genderOption = GenderAny;

                }
                else{
                    genderA.setTextColor(textColorUnchecked);genderOption = "";}
//                Toast.makeText(getContext(),genderOption,Toast.LENGTH_SHORT).show();

            }
        });


        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        return  view;
    }

    private void toggleTextColor(CheckedTextView checkbox) {
        checkbox.toggle();
        if(checkbox.isChecked())
            checkbox.setTextColor(getResources().getColor(R.color.colorPrimary));
        else
            checkbox.setTextColor(textColorUnchecked);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onApartmentUpdateListener = (OnApartmentUpdateListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnAccountInfoListener");

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("UpdateAptFragment","enterned onCreste");

            apartment = (UserApartment) getArguments().getSerializable("userApartment");
            Log.i("UpdateAptFragment","created for first time");



//        apartment = (UserApartment) getArguments().getSerializable("userApartment");
//        if(apartment!=null) {
//            Toast.makeText(getContext(), apartment.getAddress(), Toast.LENGTH_LONG).show();
//        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_add_apt,menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case R.id.done:
                boolean result = checkDetails();
                if(result){
                    setApartmentDetails();
                    try {
                        uploadApartmentDetails();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else Toast.makeText(getActivity(),"Please Fill all the details",Toast.LENGTH_LONG).show();
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

    private void uploadApartmentDetails() throws JSONException {
        // Tag used to cancel the request
        String tag_string_req = "uploadaptinfo";

        pDialog.setMessage("Getting Address info.......");
        showDialog();


        JSONObject params = new JSONObject();
        params.put("address",apartment.getAddress());
        params.put("desc",apartment.getDesc());
        params.put("rooms", Integer.toString(apartment.getRooms()));
        params.put("rent", Integer.toString(apartment.getRent()));
        params.put("utilities", Integer.toString(apartment.getUtilities()));
        params.put("vacancy", Integer.toString(apartment.getVacancy()));
        params.put("lat", Double.toString(apartment.getLat()));
        params.put("lon", Double.toString(apartment.getLon()));
        List<Double> list = new ArrayList<>();
        list.add(0,apartment.getLon());
        list.add(1,apartment.getLat());
        JSONArray coordinates = new JSONArray(list);
        JSONObject params2 = new JSONObject();
        params2.put("type","Point");
        params2.put("coordinates",coordinates);

        params.put("location",params2);
        params.put("user_id",user.get("api_user_id"));
        params.put("dateMovin",apartment.getDateMovin());
        params.put("duration",apartment.getDuration());
        params.put("placeType",apartment.getPlaceType());
        params.put("smoking",Boolean.toString(apartment.getSmoking()));
        params.put("pool",Boolean.toString(apartment.isPool()));
        params.put("music",Boolean.toString(apartment.isMusic()));
        params.put("wifi",Boolean.toString(apartment.isWifi()));
        params.put("guests",Boolean.toString(apartment.isGuests()));
        params.put("drugs",Boolean.toString(apartment.isDrugs()));
        params.put("lateNights",Boolean.toString(apartment.isLateNights()));
        params.put("washer",Boolean.toString(apartment.isWasher()));
        params.put("dryer",Boolean.toString(apartment.isDryer()));
        params.put("furnished",Boolean.toString(apartment.isFurnished()));
        params.put("kitchen",Boolean.toString(apartment.isKitchen()));
        params.put("closet",Boolean.toString(apartment.isCloset()));
        params.put("airConditioner",Boolean.toString(apartment.isAC()));
        params.put("heater",Boolean.toString(apartment.isHeater()));
        params.put("hasPet",Boolean.toString(apartment.isHasPet()));
        params.put("deposit", Integer.toString(apartment.isDeposit()));
        params.put("ageMin", Integer.toString(apartment.isAptAgeMin()));

        params.put("ageMax", Integer.toString(apartment.isAptAgeMax()));
        params.put("gender",apartment.getAptGender());
        params.put("internet",Boolean.toString(apartment.isInternet()));
        params.put("gym",Boolean.toString(apartment.isGym()));
        params.put("pets",Boolean.toString(apartment.isPets()));
//        params.put("occupant",Boolean.toString(apartment.isOccupant()));


        String URLUPDATE = AppConfig.URL_UPDATE_APT + apartment.getAPIid() +"/";


        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.PUT,
                URLUPDATE,params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = response;
                    // Check for error node in json
                    if (jObj.has("id")) {
                        // Now store the user in SQLite
                        apartmentID = jObj.getInt("id");
                        String imageUrl = Integer.toString(apartmentID);
                        db.updateApartment(jObj,apartment.getId());

                        Log.d(TAG,imageUrl);
                        onApartmentUpdateListener.setAptUpdate(apartmentID,user.get("token"),apartment.getId(), (ArrayList<String>) apartment.getAptImages());
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
                        Log.i("UpdateAptFragment %s", res.toString());

                        if(jObj.has("non_field_errors")) {

                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("non_field_errors");
                            Toast.makeText(getContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                            Toast.makeText(getContext(),"Unexpected network Error!! Please try again.",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                Log.e(TAG, "UpdateAptFragment Error: " + error.getMessage());
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

    private void setApartmentDetails() {
        apartment.setDesc(desc.getText().toString());
        apartment.setRooms(Integer.parseInt(rooms.getText().toString()));


        apartment.setRent(Integer.parseInt(rent.getText().toString()));


        apartment.setUtilities(Integer.parseInt(utilities.getText().toString()));

        apartment.setVacancy(Integer.parseInt(vacancy.getText().toString()));
        apartment.setInternet(internet.isChecked());
        apartment.setGym(gym.isChecked());
        apartment.setPets(pets.isChecked());
        apartment.setDuration(duration.getText().toString());
        apartment.setPlaceType(placeType.getText().toString());
        apartment.setDateMovin(movin.getText().toString());
        apartment.setPool(pool.isChecked());
        apartment.setWifi(wifi.isChecked());
        apartment.setAptGender(genderOption);
        apartment.setAptAgeMin(ageMin);
        apartment.setAptAgeMax(ageMax);


        apartment.setDeposit(Integer.parseInt(deposit.getText().toString()));

        apartment.setMusic(music.isChecked());
        apartment.setGuests(guests.isChecked());
        apartment.setDrugs(drugs.isChecked());
        apartment.setLateNights(latenights.isChecked());
        apartment.setWasher(washer.isChecked());
        apartment.setDryer(dryer.isChecked());
        apartment.setFurnished(furnished.isChecked());
        apartment.setKitchen(kitchen.isChecked());
        apartment.setCloset(closet.isChecked());
        apartment.setAC(ac.isChecked());
        apartment.setHeater(heater.isChecked());
        apartment.setSmoking(smoking.isChecked());
        apartment.setHasPet(hasPet.isChecked());
    }

    private boolean checkDetails() {

        Boolean errorFlag = true;
      if(rent.getText().toString().equals("")){
            rent.setError("PLease Enter a value");
            errorFlag = false;
        }

        if(utilities.getText().toString().equals("")){
            utilities.setError("Please Enter a value");
            errorFlag=false;
        }
        if(deposit.getText().toString().equals("")) {
            deposit.setError("Please Enter a value");
            errorFlag = false;
        }

        if(desc.getText().toString().equals("")){
            desc.setError("Please Enter a small description");
            errorFlag = false;
        }

        if(genderOption.equals("")){
            Toast.makeText(getContext(),"Please Select a Gender",Toast.LENGTH_SHORT).show();
            errorFlag=false;
        }

        if(duration.getText().toString().equals("Select a Value")){
            Toast.makeText(getContext(),"Please select duration",Toast.LENGTH_SHORT).show();
            errorFlag=false;
        }

        if(placeType.getText().toString().equals("Select a Value")){
            Toast.makeText(getContext(),"Please select a placetype",Toast.LENGTH_SHORT).show();
            errorFlag=false;
        }

        if(movin.getText().toString().equals("Select a Value")){
            Toast.makeText(getContext(),"Please select Movin-Date",Toast.LENGTH_SHORT).show();
            errorFlag=false;
        }

        if(!checkbox.isChecked()){
            Toast.makeText(getContext(),"Address is required",Toast.LENGTH_SHORT).show();
            errorFlag=false;

        }
       return errorFlag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
              movin.setText(year + "-" + (monthOfYear + 1)  + "-" + dayOfMonth);

    }
}
