package com.dbxlab.vijjub.mirumy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dbxlab.vijjub.mirumy.AddApartmentActivity;
import com.dbxlab.vijjub.mirumy.R;
import com.dbxlab.vijjub.mirumy.adapters.UserApartment;
import com.dbxlab.vijjub.mirumy.adapters.UserApartmentAdapter;

import java.util.List;

import helper.SQliteManager;
//import userprofile.Apartment;

/**
 * Created by vijjub on 8/8/16.
 */
public class UserApartmentInfo extends Fragment{

    private final String TAG = UserApartmentInfo.class.getSimpleName();
    OnAptSelectListener onAptSelectListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.user_apartment_info, container, false);
        final List<UserApartment> userAptList;

        final Context context = getActivity();
        SQliteManager db = new SQliteManager(context);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        userAptList = db.getUserApartmentDetails();
        if(userAptList.size() == 0)
            Toast.makeText(getContext(),"No Apartments to show!!!",Toast.LENGTH_LONG);
//        for(int j =0;j<userAptList.size();j++)
//            Log.d(TAG, "Fetching Complete Apartment details from Sqlite: " + userAptList.get(j).getAddress());


        UserApartmentAdapter userApartmentAdapter = new UserApartmentAdapter(userAptList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userApartmentAdapter);
        userApartmentAdapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserApartment userApartment = userAptList.get(position);
                onAptSelectListener.setApartment(userApartment);
                //Toast.makeText(context, userApartment.getAddress() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                UserApartment userApartment = userAptList.get(position);
                onAptSelectListener.setApartmentRoomies(userApartment);

            }
        }));

        FloatingActionButton newApartment = (FloatingActionButton)view.findViewById(R.id.new_user_apartment);
        newApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddApartmentActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onAptSelectListener = (OnAptSelectListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnAptSelectListener");

        }
    }

    public interface OnAptSelectListener{
        void setApartment(UserApartment userApartment);
        void setApartmentRoomies(UserApartment userApartment);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private UserApartmentInfo.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final UserApartmentInfo.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
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
