package com.dbxlab.vijjub.mirumy.adapters;

import java.io.Serializable;

/**
 * Created by vijjub on 9/1/16.
 */
public class User implements Serializable {
    private String username,fname,lname,email,gender,foodPref,clean,joined,profileImg,token,sleep;
    private int roomie_ucost,cooking,age,row_id;
    private boolean smoking;
    private boolean roomie;
    private boolean upets;
    private boolean alcohol;
    private boolean otherRoomie;
    private boolean roomieGenderOther;
    private String duration,movinDate,roomieGender,roomieAgeMax,roomieAgeMin,roomieDesc,noise,socialize;
    private double roomieLat,roomieLon;

    public User(){}

    public User(String username, String fname, String lname, String email, String gender, String foodPref, String clean, String joined, String profileImg, int ucost, int cooking, int
            age, boolean smoking, boolean roomie, boolean upets, String noise, String socialize, boolean alcohol, String token, int row_id, String roomieAgeMin, String roomieAgeMax, boolean roomieGenderOther){
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.gender = gender;
        this.foodPref = foodPref;
        this.clean = clean;
        this.joined = joined;
        this.profileImg = profileImg;
        this.roomie_ucost = ucost;
        this.cooking = cooking;
        this.age = age;
        this.smoking = smoking;
        this.roomie = roomie;
        this.upets = upets;
        this.noise = noise;
        this.socialize = socialize;
        this.alcohol = alcohol;
        this.token = token;
        this.row_id = row_id;

        this.roomieAgeMin = roomieAgeMin;
        this.roomieAgeMax = roomieAgeMax;
        this.roomieGenderOther = roomieGenderOther;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public int getRow_id() {
        return row_id;
    }

    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    public String getFName() {
        return fname;
    }

    public void setFName(String fname) {
        this.fname = fname;
    }

    public String getLName() {
        return lname;
    }

    public void setLName(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isAlcohol() {
        return alcohol;
    }

    public void setAlcohol(boolean alcohol) {
        this.alcohol = alcohol;
    }

    public void setNoise(String noise) {
        this.noise = noise;
    }

    public String isNoise() {
        return noise;
    }

    public int getCooking() {
        return cooking;
    }

    public void setCooking(int cooking) {
        this.cooking = cooking;
    }

    public String getClean() {
        return clean;
    }

    public void setClean(String clean) {
        this.clean = clean;
    }

    public String getFoodPref() {
        return foodPref;
    }

    public void setFoodPref(String foodPref) {
        this.foodPref = foodPref;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public boolean isRoomie() {
        return roomie;
    }

    public void setRoomie(boolean roomie) {
        this.roomie = roomie;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public void setSocialize(String socialize) {
        this.socialize = socialize;
    }

    public String isSocialize() {
        return socialize;
    }

    public boolean isUpets() {
        return upets;
    }

    public void setUpets(boolean upets) {
        this.upets = upets;
    }

    public String getDuration(){
        return duration;
    }

    public void setDuration(String duration){
        this.duration = duration;
    }

    public String getMovinDate(){
        return movinDate;
    }

    public void setMovinDate(String movinDate){
        this.movinDate = movinDate;
    }

    public void setOtherroomie(boolean otherRoomie){
        this.otherRoomie = otherRoomie;
    }


    public boolean isOtherRoomie(){
        return otherRoomie;
    }


    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public int getRoomie_ucost() {
        return roomie_ucost;
    }

    public void setRoomie_ucost(int roomie_ucost) {
        this.roomie_ucost = roomie_ucost;
    }

    public String getRoomieGender() {
        return roomieGender;
    }

    public void setRoomieGender(String roomieGender) {
        this.roomieGender = roomieGender;
    }



    public Boolean getRoomieGenderOther() {
        return roomieGenderOther;
    }

    public void setRoomieGenderOther(Boolean roomieGenderOther) {
        this.roomieGenderOther = roomieGenderOther;
    }

    public String getRoomieDesc() {
        return roomieDesc;
    }

    public void setRoomieDesc(String roomieDesc) {
        this.roomieDesc = roomieDesc;
    }

    public double getRoomieLat() {
        return roomieLat;
    }

    public void setRoomieLat(double roomieLat) {
        this.roomieLat = roomieLat;
    }

    public double getRoomieLon() {
        return roomieLon;
    }

    public void setRoomieLon(double roomieLon) {
        this.roomieLon = roomieLon;
    }

    public String getRoomieAgeMax() {
        return roomieAgeMax;
    }

    public void setRoomieAgeMax(String roomieAgeMax) {
        this.roomieAgeMax = roomieAgeMax;
    }

    public String getRoomieAgeMin() {
        return roomieAgeMin;
    }

    public void setRoomieAgeMin(String roomieAgeMin) {
        this.roomieAgeMin = roomieAgeMin;
    }
}
