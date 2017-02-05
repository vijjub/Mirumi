package com.dbxlab.vijjub.mirumy.adapters;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vijjub on 8/17/16.
 */
public class UserApartment implements Serializable{

    private String address,desc,created;
    private int vacancy, rent,utilities,rooms,id,fkey;
    private boolean washer,dryer,internet,gym,pets,smoking;
    private List<String> aptImages;
    private double lat,lon;
    private String APIid;
    private String duration;
    private String dateMovin;
    private String city,shortAddress;
    private boolean pool;
    private boolean wifi;
    private String aptGender,placeType;
    private boolean music;
    private int aptAgeMin,aptAgeMax;
    private int deposit;
    private boolean guests,lateNights,furnished;
    private boolean drugs;
    private boolean kitchen;
    private boolean closet,AC,heater,hasPet,occupant;

    public UserApartment(){

    }

    public UserApartment(int id, String address, String desc, int rent, int vacancy, int utilities, int rooms, boolean laundry, boolean internet,
                         boolean gym, boolean pets, double lat, double lon, List<String> aptImages, int fkey, String created, boolean smoking, boolean occupant){
        this.id = id;
        this.address = address;
        this.desc = desc;
        this.rent = rent;
        this.vacancy = vacancy;
        this.utilities = utilities;
        this.rooms = rooms;
        this.internet = internet;
        this.gym = gym;
        this.pets = pets;
        this.aptImages = aptImages;
        this.lat = lat;
        this.lon = lon;
        this.fkey = fkey;
        this.created = created;
        this.smoking= smoking;
        this.occupant = occupant;
    }


    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }



    public String getAddress() {
       return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }


    public int getRent() {
        return rent;
    }

    public void setRent(int rent){
        this.rent = rent;
    }


    public int getVacancy() {
        return vacancy;
    }

    public void setVacancy(int vacancy){
        this.vacancy = vacancy;
    }

    public int getUtilities() {
        return utilities;
    }

    public void setUtilities(int utilities){
        this.utilities = utilities;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms){
        this.rooms = rooms;
    }


    public boolean isInternet() {
        return internet;
    }

    public void setInternet(boolean internet){
        this.internet = internet;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets){
        this.pets = pets;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym){
        this.gym = gym;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat){
        this.lat = lat;
    }


    public double getLon() {
        return lon;
    }

    public void setLon(double lon){
        this.lon = lon;
    }


    public List getAptImages() {
        return aptImages;
    }

    public void setAptImages(List aptImages){
        this.aptImages = aptImages;
    }

    public boolean getSmoking(){
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public int getFkey(){
        return fkey;
    }

    public void setFkey(int fkey){
        this.fkey = fkey;
    }

    public String getCreated(){
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getAPIid(){
        return APIid;
    }

    public void setAPIid(String APIid) {
        this.APIid = APIid;
    }

    public String getDuration(){
        return duration;
    }

    public void setDuration(String duration){
        this.duration = duration;
    }

    public String getDateMovin(){
        return dateMovin;
    }

    public void setDateMovin(String dateMovin){
        this.dateMovin = dateMovin;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public boolean isWasher() {
        return washer;
    }

    public void setWasher(boolean washer) {
        this.washer = washer;
    }

    public boolean isDryer() {
        return dryer;
    }

    public void setDryer(boolean dryer) {
        this.dryer = dryer;
    }

    public String getAptGender() {
        return aptGender;
    }

    public void setAptGender(String aptGender) {
        this.aptGender = aptGender;
    }

    public boolean isPool() {
        return pool;
    }

    public void setPool(boolean pool) {
        this.pool = pool;

    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public int isAptAgeMin() {
        return aptAgeMin;
    }

    public int isAptAgeMax() {
        return aptAgeMax;
    }

    public int isDeposit() {
        return deposit;
    }

    public boolean isGuests() {
        return guests;
    }

    public void setGuests(boolean guests) {
        this.guests = guests;
    }

    public boolean isDrugs() {
        return drugs;
    }

    public void setDrugs(boolean drugs) {
        this.drugs = drugs;
    }

    public boolean isKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

    public boolean isCloset() {
        return closet;
    }

    public void setCloset(boolean closet) {
        this.closet = closet;
    }

    public boolean isAC() {
        return AC;
    }

    public void setAC(boolean AC) {
        this.AC = AC;
    }

    public boolean isHeater() {
        return heater;
    }

    public void setHeater(boolean heater) {
        this.heater = heater;
    }

    public boolean isHasPet() {
        return hasPet;
    }

    public void setHasPet(boolean hasPet) {
        this.hasPet = hasPet;
    }

    public boolean isLateNights() {
        return lateNights;
    }

    public void setLateNights(boolean lateNights) {
        this.lateNights = lateNights;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public void setAptAgeMin(int aptAgeMin) {
        this.aptAgeMin = aptAgeMin;
    }

    public void setAptAgeMax(int aptAgeMax) {
        this.aptAgeMax = aptAgeMax;
    }

    public String getShortAddress() {
        return shortAddress;
    }

    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }

    public boolean isOccupant() {
        return occupant;
    }

    public void setOccupant(boolean occupant) {
        this.occupant = occupant;
    }
}
