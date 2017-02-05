package com.dbxlab.vijjub.mirumy.adapters;

import java.io.Serializable;

/**
 * Created by vijjub on 10/20/16.
 */
public class BrowseApartment implements Serializable{

    private UserApartment userApartment;
    private User user;

    public BrowseApartment(User user,UserApartment userApartment){
        this.userApartment = userApartment;
        this.user = user;
    }

    public BrowseApartment() {

    }

    public UserApartment getUserApartment(){
        return userApartment;
    }

    public void setUserApartment(UserApartment userApartment){
        this.userApartment = userApartment;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
