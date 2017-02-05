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
import java.util.StringTokenizer;

/**
 * Created by vijjub on 10/20/16.
 */

public class BrowseApartmentAdapter extends RecyclerView.Adapter<BrowseApartmentAdapter.BrowseApartmentHolder> {

    private List<BrowseApartment> browseAptList;
    private Context mContext;

    public BrowseApartmentAdapter(List<BrowseApartment> browseAptList,Context mcontext) {
        this.browseAptList = browseAptList;
        this.mContext = mcontext;
    }

    @Override
    public BrowseApartmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_apt_info_list,parent,false);
        return new BrowseApartmentHolder(view);
    }

    @Override
    public void onBindViewHolder(BrowseApartmentHolder holder, int position) {

        BrowseApartment browseApartment = browseAptList.get(position);
        UserApartment userApartment = browseApartment.getUserApartment();
        if(userApartment.getAptImages().size()>0) {
            Glide.with(mContext).load(userApartment.getAptImages().get(0)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
        }

        String addressText = userApartment.getAddress();
        String shortAddress;
        StringTokenizer addressTokenizer = new StringTokenizer(addressText,",");
        addressTokenizer.nextElement();
        shortAddress = addressTokenizer.nextElement().toString();
        shortAddress = shortAddress + addressTokenizer.nextToken().toString();


        holder.address.setText(addressText);
        userApartment.setShortAddress(shortAddress);
        holder.movin.setText(userApartment.getDateMovin());
        String rentText = "$ "+Integer.toString(userApartment.getRent());
        holder.rent.setText(rentText);
        String genderText = "";
        if(userApartment.getAptGender().equals("M"))
            genderText = "Male";
        if(userApartment.getAptGender().equals("F"))
            genderText = "Female";
        if(userApartment.getAptGender().equals("A"))
            genderText = "Male or Female";

        String vacancyString = "Looking for "+Integer.toString(userApartment.getVacancy())+" "+genderText+" Roomies";
        holder.vacancy.setText(vacancyString);

    }

    @Override
    public int getItemCount() {
        return browseAptList.size();
    }

    public void clear() {

        browseAptList.clear();

        notifyDataSetChanged();

    }

    public void addAll(List<BrowseApartment> list) {

        browseAptList.addAll(list);

        notifyDataSetChanged();

    }

    public class BrowseApartmentHolder extends RecyclerView.ViewHolder{
        public TextView address,movin,rent,vacancy,gender;
        public ImageView image;

        public BrowseApartmentHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.thumbnail);
            address = (TextView)itemView.findViewById(R.id.placeName);
            movin = (TextView)itemView.findViewById(R.id.movindate);
            rent = (TextView)itemView.findViewById(R.id.rent);
            vacancy = (TextView)itemView.findViewById(R.id.vacancy);
        }
    }

}
