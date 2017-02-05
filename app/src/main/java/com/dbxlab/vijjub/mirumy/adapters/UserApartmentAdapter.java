package com.dbxlab.vijjub.mirumy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbxlab.vijjub.mirumy.R;

import java.util.List;

/**
 * Created by vijjub on 8/17/16.
 */
public class UserApartmentAdapter extends RecyclerView.Adapter<UserApartmentAdapter.UserApartmentHolder> {

    private List<UserApartment> userAptList;

    public UserApartmentAdapter(List<UserApartment> userAptList) {
        this.userAptList = userAptList;
    }

    @Override
    public UserApartmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_apt_info_list,parent,false);
        return new UserApartmentHolder(view);
    }

    @Override
    public void onBindViewHolder(UserApartmentHolder holder, int position) {

        UserApartment userApartment = userAptList.get(position);
        holder.address.setText(userApartment.getAddress());
        holder.desc.setText(userApartment.getDesc());
        holder.rent.setText(Integer.toString(userApartment.getRent()));
        holder.vacancy.setText(Integer.toString(userApartment.getVacancy()));

    }

    @Override
    public int getItemCount() {
        return userAptList.size();
    }

    public class UserApartmentHolder extends RecyclerView.ViewHolder{
        public TextView address,desc,rent,vacancy;

        public UserApartmentHolder(View itemView) {
            super(itemView);
            address = (TextView)itemView.findViewById(R.id.address);
            desc = (TextView)itemView.findViewById(R.id.desc);
            rent = (TextView)itemView.findViewById(R.id.rent);
            vacancy = (TextView)itemView.findViewById(R.id.vacancy);
        }
    }

}
