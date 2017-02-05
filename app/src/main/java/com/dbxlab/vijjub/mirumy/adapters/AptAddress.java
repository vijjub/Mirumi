package com.dbxlab.vijjub.mirumy.adapters;

import java.io.Serializable;

/**
 * Created by vijjub on 9/13/16.
 */
public class AptAddress implements Serializable {
    String street,city,state,country,fullAddress;
    String zipcode;
    double lon,lat;

    public AptAddress(){}

    public AptAddress(String street,String city,String state,String country, String zipcode,double lon, double lat,String fullAddress){
        this.street = street;
        this.city = city;
        this.country = country;
        this.zipcode = zipcode;
        this.lon = lon;
        this.lat = lat;
        this.state = state;
        this.fullAddress = fullAddress;
    }

    public String getStreet(){
        return street;
    }

    public void  setStreet(String street){
        this.street = street;
    }

    public String getFullAddress(){
        return fullAddress;
    }

    public void  setFullAddress(String fullAddress){
        this.fullAddress = fullAddress;
    }

    public String getCity(){
        return city;
    }

    public void  setCity(String city){
        this.city = city;
    }

    public String getCountry(){
        return country;
    }

    public void  setCountry(String country){
        this.country = country;
    }

    public String getState(){
        return state;
    }

    public void  setState(String state){
        this.state = state;
    }

    public String getZipcode(){
        return zipcode;
    }

    public void  setZipcode(String zipcode){
        this.zipcode = zipcode;
    }

    public double getLon(){
        return lon;
    }

    public void setLon(double lon){
        this.lon = lon;
    }

    public double getLat(){
        return lat;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    @Override
    public String toString(){
        return street +","+city+","+state+" "+zipcode+","+country;
    }


    public String shorterAddress(){
        return city+","+state+" "+zipcode+","+country;
    }




}
