package com.dbxlab.vijjub.mirumy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dbxlab.vijjub.mirumy.R;

import java.util.List;

/**
 * Created by vijjub on 10/23/16.
 */
public class BrowseRoomieAdapter extends RecyclerView.Adapter<BrowseRoomieAdapter.BrowseRoomieHolder> {

    private List<User> roomieList;
    private Context mContext;

    public BrowseRoomieAdapter(List<User> roomieList,Context mcontext) {
        this.roomieList = roomieList;
        this.mContext = mcontext;
    }

    @Override
    public BrowseRoomieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_roomie_list,parent,false);
        return new BrowseRoomieHolder(view);
    }

    @Override
    public void onBindViewHolder(BrowseRoomieHolder holder, int position) {

        User user = roomieList.get(position);
        Glide.with(mContext).load(user.getProfileImg()).error(R.drawable.gender_neutral_user).centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);

//
//        String addressText = userApartment.getAddress();
//        String shortAddress;
//        StringTokenizer addressTokenizer = new StringTokenizer(addressText,",");
//        addressTokenizer.nextElement();
//        shortAddress = addressTokenizer.nextElement().toString();
//        shortAddress = shortAddress + addressTokenizer.nextToken().toString();
//
//
        holder.name.setText(user.getUsername());
//        holder.movin.setText(userApartment.getDateMovin());
        String rentText = "$ "+Integer.toString(user.getRoomie_ucost());
        holder.budget.setText(rentText);
//        String genderText = "";
//        if(userApartment.getAptGender().equals("M"))
//            genderText = "Male";
//        if(userApartment.getAptGender().equals("F"))
//            genderText = "Female";
//        if(userApartment.getAptGender().equals("A"))
//            genderText = "Male or Female";
//
//        String vacancyString = "Looking for "+Integer.toString(userApartment.getVacancy())+" "+genderText+" Roomies";
//        holder.vacancy.setText(vacancyString);

    }

    @Override
    public int getItemCount() {
        return roomieList.size();
    }

    public void clear() {

        roomieList.clear();

        notifyDataSetChanged();

    }

    public void addAll(List<User> list) {

        roomieList.addAll(list);

        notifyDataSetChanged();

    }

    public class BrowseRoomieHolder extends RecyclerView.ViewHolder{
        public TextView name,budget;
        public ImageView image;

        public BrowseRoomieHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.thumbnail);
            name = (TextView)itemView.findViewById(R.id.name);
            budget = (TextView)itemView.findViewById(R.id.budget);

        }
    }

}
