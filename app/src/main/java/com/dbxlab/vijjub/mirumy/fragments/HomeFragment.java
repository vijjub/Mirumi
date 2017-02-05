package com.dbxlab.vijjub.mirumy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dbxlab.vijjub.mirumy.BrowseApartmentsActivity;
import com.dbxlab.vijjub.mirumy.BrowseRoomiesActivity;
import com.dbxlab.vijjub.mirumy.R;

/**
 * Created by vijjub on 10/26/16.
 */
public class HomeFragment extends Fragment {

    private Button roomieButton;
    private Button placeButton;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.main_fragment,container,false);

        roomieButton =(Button) view.findViewById(R.id.roomie);
        placeButton = (Button) view.findViewById(R.id.place);

        roomieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent roomieIntent = new Intent(getContext(), BrowseRoomiesActivity.class);
                startActivity(roomieIntent);

            }
        });

        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent palceIntent = new Intent(getContext(), BrowseApartmentsActivity.class);
                startActivity(palceIntent);
            }
        });

        return view;
    }
}
