package helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dbxlab.vijjub.mirumy.adapters.UserApartment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

//import userprofile.Apartment;

/**
 * Created by vijjub on 7/25/16.
 */
public class    SQliteManager extends SQLiteOpenHelper{

    private static final String TAG = SQliteManager.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "myrumi_db";

    // All tables names
    private static final String TABLE_USER = "user";
    private static final String TABLE_USER_APT = "u_apt";
    private static final String TABLE_APT_IMAGES = "u_apt_images";

    // User And USERPROFILE details in one table Table Columns names
    private static final String KEY_ID = "id";
    private static final String API_KEY_ID = "api_user_id";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_LNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERID = "userid";
    private static final String KEY_JOINED_AT = "joined_at";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_LAST_LOGIN = "last_login";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PROFILEIMAGE  = "profileimage";//?? need to think about it
    private static final String KEY_COOKING = "cooking";
    private static final String KEY_FOOD_PREFERENCE = "foodPreference";
    private static final String KEY_CLEAN = "cleanliness";
    private static final String KEY_SMOKING = "smoking";
    private static final String KEY_ALCOHOL = "alcohol";
    private static final String KEY_NOISE = "noise";
    private static final String KEY_SOCIALIZING = "social";
    private static final String KEY_SLEEP = "sleep";
    private static final String KEY_UPETS = "upets";
    private static final String KEY_R_ROOMIE = "roomie";//BOOLEAN
    private static final String KEY_R_MOVIN = "dateMovin";
    private static final String KEY_R_DURATION = "duration";
    private static final String KEY_UCOST = "ucost";
    private static final String KEY_R_API_ID = "roomieid";
    private static final String KEY_R_OTHERROOMIE = "otherroomie";
    private static final String KEY_R_DESC = "roomiedesc";
    private static final String KEY_R_LON = "longitude";//POINT FIELD LONGITUDE
    private static final String KEY_R_LAT = "latitude"; //POINT FIELD LATITUDE
    private static final String KEY_R_GENDER = "roomiegender";
    private static final String KEY_R_AGE = "roomieage";
    private static final String KEY_R_GENDER_OTHER = "roomieothergender";

    //APartmetn Table details
    private static final String KEY_AID = "aptid";
    private static final String KEY_APIAID = "apiaptid";
    private static final String KEY_ADDRESS = "address";//Need to think about it lat and long
    private static final String KEY_DESC = "desc";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LONG = "long";
    private static final String KEY_CREATED = "created";
    private static final String KEY_ROOMS = "rooms";
    private static final String KEY_RENT = "rent";
    private static final String KEY_UTILITIES = "utilities";
    private static final String KEY_VACANCY = "vacancy";
    private static final String KEY_INTERNET = "internet";
    private static final String KEY_GYM = "gym";
    private static final String KEY_Pets = "pets";
    private static final String KEY_ASMOKING = "aptsmoking";
    private static final String KEY_MOVIN = "dateMovin";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_AUID = "aptuserid";//Foreign key
    private static final String KEY_PLACETYPE = "placetype";
    private static final String KEY_POOL = "pool";
    private static final String KEY_WIFI = "wifi";
    private static final String KEY_APT_GENDER = "aptgender";
    private static final String KEY_APT_AGE_MIN = "aptagemin";
    private static final String KEY_APT_AGE_MAX = "aptagemax";
    private static final String KEY_DEPOSIT = "deposit";
    private static final String KEY_MUSIC = "music";
    private static final String KEY_GUESTS = "guests";
    private static final String KEY_DRUGS = "drugs";
    private static final String KEY_LATENIGHTS = "latenights";
    private static final String KEY_WASHER = "washer";
    private static final String KEY_DRYER = "dryer";
    private static final String KEY_FURNISHED = "furnished";
    private static final String KEY_KITCHEN = "kitchen";
    private static final String KEY_CLOSET = "closet";
    private static final String KEY_AC = "ac";
    private static final String KEY_HEATER = "heater";
    private static final String KEY_HASPET = "haspet";
   // private static final String KEY_OCCUPANT = "occupant";

    //Apartment IMages
    private static final String KEY_AIID = "aptimgid";//Forign ky for the apartment
    private static final String KEY_APIIID = "apiimgid";
    private static final String KEY_IID = "imgid";
    private static final String KEY_APT_IMAGE = "aptimageurl";



    public SQliteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                +KEY_ID + " INTEGER PRIMARY KEY," + API_KEY_ID + " TEXT, " +KEY_R_DURATION+" TEXT,"+KEY_R_MOVIN+" TEXT,"
                +KEY_R_ROOMIE+" TEXT,"+ KEY_FNAME + " TEXT,"+KEY_LNAME+" TEXT,"+ KEY_EMAIL + " TEXT UNIQUE," + KEY_USERID + " TEXT,"
                +KEY_TOKEN+ " TEXT, "+KEY_JOINED_AT + " TEXT, " +KEY_LAST_LOGIN+" TEXT,"+KEY_AGE+" INTEGER,"+KEY_GENDER+" TEXT,"+KEY_UCOST+" INTEGER,"
                +KEY_COOKING+" INTEGER,"+KEY_FOOD_PREFERENCE+" TEXT,"+KEY_SLEEP+" TEXT,"
                +KEY_CLEAN +" TEXT,"+KEY_SMOKING+" TEXT,"+KEY_ALCOHOL+" TEXT,"+KEY_SOCIALIZING+" TEXT,"
                +KEY_NOISE+" TEXT,"+KEY_UPETS+" TEXT,"+KEY_PROFILEIMAGE+" TEXT,"+KEY_R_API_ID+" TEXT,"+KEY_R_OTHERROOMIE+" TEXT,"
                +KEY_R_DESC+" TEXT,"+KEY_R_AGE+" TEXT,"+KEY_R_GENDER+" TEXT,"+KEY_R_GENDER_OTHER+" TEXT,"+KEY_R_LAT+" TEXT,"
                +KEY_R_LON+" TEXT"+")";

        String CREATE_APT_TABLE = "CREATE TABLE " + TABLE_USER_APT + "("
                +KEY_AID + " INTEGER PRIMARY KEY," +KEY_APIAID+" TEXT, "+KEY_DURATION+" TEXT,"+KEY_MOVIN+" TEXT,"
                +KEY_ADDRESS + " TEXT,"+KEY_DESC+" TEXT,"+ KEY_LAT + " REAL," + KEY_LONG + " REAL,"
                +KEY_CREATED + " TEXT," +KEY_ROOMS+" INTEGER,"+KEY_RENT+" INTEGER,"+KEY_UTILITIES+" INTEGER,"+KEY_VACANCY+" INTEGER,"+KEY_PLACETYPE+" TEXT,"+KEY_INTERNET+" TEXT,"
                +KEY_GYM +" TEXT,"+KEY_Pets+" TEXT,"+KEY_ASMOKING+" TEXT,"+KEY_POOL+" TEXT,"+KEY_WIFI+" TEXT,"+KEY_APT_GENDER+" TEXT,"
                +KEY_APT_AGE_MIN+" TEXT,"+KEY_APT_AGE_MAX+" TEXT,"+KEY_DEPOSIT+" TEXT,"+KEY_MUSIC+" TEXT,"+KEY_GUESTS+" TEXT,"+KEY_DRUGS+" TEXT,"
                +KEY_LATENIGHTS+" TEXT,"+KEY_WASHER+" TEXT,"+KEY_DRYER+" TEXT,"+KEY_FURNISHED+" TEXT,"+KEY_KITCHEN+" TEXT,"
                +KEY_CLOSET+" TEXT,"+KEY_AC+" TEXT,"+KEY_HEATER+" TEXT,"+KEY_HASPET+" TEXT,"+KEY_AUID+" INTEGER, FOREIGN KEY("+KEY_AUID+") REFERENCES "+TABLE_USER+"("+KEY_ID+")"+")";

        String CREATE_APT_IMAGE_TABLE = "CREATE TABLE "+TABLE_APT_IMAGES+"("
                +KEY_IID+" INTEGER PRIMARY KEY,"+KEY_APIIID+" TEXT,"+KEY_APT_IMAGE+" TEXT,"+KEY_AIID+" INTEGER, FOREIGN KEY("+KEY_AIID+") REFERENCES "+TABLE_USER_APT+"("+KEY_AID+")"+")";

        sqLiteDatabase.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG,"TABLE 1 created");
        sqLiteDatabase.execSQL(CREATE_APT_TABLE);
        Log.d(TAG,"TABLE 2 created");
        sqLiteDatabase.execSQL(CREATE_APT_IMAGE_TABLE);
        Log.d(TAG,"TABLE 3 created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USER_APT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_APT_IMAGES);

        onCreate(sqLiteDatabase);
    }

    public void updateUserDetails(JSONObject jObj, int row_id) throws JSONException {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AGE,jObj.getInt("age"));
        values.put(KEY_GENDER,jObj.getString("gender"));
        values.put(KEY_PROFILEIMAGE,jObj.getString("profileImg"));
        values.put(KEY_COOKING,jObj.getInt("cooking"));
        values.put(KEY_FOOD_PREFERENCE,jObj.getString("foodPreference"));
        values.put(KEY_SLEEP,jObj.getString("sleep"));
        values.put(KEY_SOCIALIZING,jObj.getString("socializing"));
        values.put(KEY_SMOKING,jObj.getString("smoking"));
        values.put(KEY_NOISE,jObj.getString("noise"));
        values.put(KEY_UPETS,jObj.getString("uPets"));
        values.put(KEY_ALCOHOL,jObj.getString("alcohol"));
        values.put(KEY_CLEAN,jObj.getString("cleanliness"));
                        //        values.put(KEY_R_DURATION,jObj.getString("duration"));
                        //        values.put(KEY_R_MOVIN,jObj.getString("dateMovin"));
                        //        values.put(KEY_R_ROOMIE,jObj.getString("otherRoomie"));

        long id = db.update(TABLE_USER,values,KEY_ID + "=" + row_id,null);
        Log.d(TAG, "UserDetails updated into USER sqlite: " + id);

    }

    public void updateRoomieDetails(JSONObject jObj, int row_id) throws JSONException{

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_R_MOVIN,jObj.getString("dateMovin"));
        values.put(KEY_R_OTHERROOMIE,jObj.getString("otherRoomie"));
        values.put(KEY_UCOST,jObj.getInt("uCost"));
        values.put(KEY_R_ROOMIE,jObj.getString("roomie"));
        values.put(KEY_R_GENDER,jObj.getString("roomieGender"));
        values.put(KEY_R_DURATION,jObj.getString("duration"));
        values.put(KEY_R_DESC,jObj.getString("roomieDesc"));
        values.put(KEY_R_API_ID,jObj.getInt("id"));

        JSONObject location = jObj.getJSONObject("preferredLocation");
        JSONArray coordinates = location.getJSONArray("coordinates");
        values.put(KEY_R_LAT,Double.toString((Double) coordinates.get(1)));
        values.put(KEY_R_LON,Double.toString((Double) coordinates.get(0)));



        long id = db.update(TABLE_USER,values,KEY_ID + "=" + row_id,null);
        Log.d(TAG, "UserDetails updated into USER sqlite: " + id);


    }

    public void addCompleteDetails(JSONObject jObj, String token) throws JSONException {

        //Important: User Account Details
        String username = jObj.getString("username");
        String first_name = jObj.getString("first_name");
        String last_name = jObj.getString("last_name");
        String email = jObj.getString("email");
        String last_login = jObj.getString("last_login");
        String date_joined = jObj.getString("date_joined");
        SQLiteDatabase db = this.getWritableDatabase();
        long uid= 0;


        //Important: User Profile Details
        Object prfoileobj = jObj.get("uProfile");
        if(prfoileobj!=JSONObject.NULL) {
            JSONObject userProfile = jObj.getJSONObject("uProfile");
            int age = userProfile.getInt("age");
            String gender = userProfile.getString("gender");
            // Igonred roomie from the object
            String sleep = userProfile.getString("sleep");
            int cooking = userProfile.getInt("cooking");
            String cleanliness = userProfile.getString("cleanliness");
            String smoking = userProfile.getString("smoking");
            String alcohol = userProfile.getString("alcohol");
            String noise = userProfile.getString("noise");
            String uPets = userProfile.getString("uPets");
            String foodPreference = userProfile.getString("foodPreference");
            String profileImg = userProfile.getString("profileImg");
            String social = userProfile.getString("socializing");

            //Adding user info to local db

             db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_TOKEN, token);
            values.put(KEY_USERID, username);
            values.put(KEY_FNAME, first_name);
            values.put(KEY_LNAME, last_name);
            values.put(KEY_EMAIL, email);
            values.put(KEY_LAST_LOGIN, last_login);
            values.put(KEY_JOINED_AT, date_joined);
            values.put(KEY_AGE, age);
            values.put(KEY_GENDER, gender);
            values.put(KEY_COOKING, cooking);
            values.put(KEY_CLEAN, cleanliness);
            values.put(KEY_SMOKING, smoking);
            values.put(KEY_ALCOHOL, alcohol);
            values.put(KEY_NOISE, noise);
            values.put(KEY_UPETS, uPets);
            values.put(KEY_FOOD_PREFERENCE, foodPreference);
            values.put(KEY_PROFILEIMAGE, profileImg);
            values.put(KEY_SLEEP, sleep);
            values.put(KEY_SOCIALIZING, social);
            values.put(API_KEY_ID, jObj.getString("id"));


            Object roomieObject = jObj.get("roomie");
            if (roomieObject != JSONObject.NULL) {
                JSONObject roomieProfile = jObj.getJSONObject("roomie");
                values.put(KEY_R_API_ID, roomieProfile.getString("id"));
                values.put(KEY_R_DURATION, roomieProfile.getString("duration"));
                values.put(KEY_R_MOVIN, roomieProfile.getString("dateMovin"));
                values.put(KEY_R_ROOMIE, roomieProfile.getString("roomie"));
                values.put(KEY_UCOST, roomieProfile.getInt("uCost"));
                values.put(KEY_R_GENDER, roomieProfile.getString("roomieGender"));
                values.put(KEY_R_DESC, roomieProfile.getString("roomieDesc"));
                values.put(KEY_R_OTHERROOMIE, roomieProfile.getString("otherRoomie"));
                JSONObject preferredLocation = roomieProfile.getJSONObject("preferredLocation");
                JSONArray coordinates = preferredLocation.getJSONArray("coordinates");
                values.put(KEY_R_LAT, Double.toString((Double) coordinates.get(1)));
                values.put(KEY_R_LON, Double.toString((Double) coordinates.get(0)));
            }

            uid = db.insert(TABLE_USER, null, values);

            Log.d(TAG, "New user inserted into USER sqlite: " + uid);
        }
        //Important: Apartment List

        Object apartmentArray = jObj.get("uApartments");
        if(apartmentArray != JSONObject.NULL) {

            JSONArray userApartments = jObj.getJSONArray("uApartments");
            for (int i = 0; i < userApartments.length(); i++) {

                JSONObject apartment = userApartments.getJSONObject(i);
                String address = apartment.getString("address");
                String desc = apartment.getString("desc");
                int rooms = apartment.getInt("rooms");
                int rent = apartment.getInt("rent");
                int utilities = apartment.getInt("utilities");
                int vacancy = apartment.getInt("vacancy");
                String internet = apartment.getString("internet");
                String gym = apartment.getString("gym");
                String pets = apartment.getString("pets");
                String apiid = apartment.get("id").toString();
                JSONObject location = apartment.getJSONObject("location");
                JSONArray coordinates1 = location.getJSONArray("coordinates");


                ContentValues aptvalues = new ContentValues();
                aptvalues.put(KEY_ADDRESS, address);
                aptvalues.put(KEY_DESC, desc);
                aptvalues.put(KEY_LAT, Double.toString((Double) coordinates1.get(1)));
                aptvalues.put(KEY_LONG, Double.toString((Double) coordinates1.get(0)));
                aptvalues.put(KEY_ROOMS, rooms);
                aptvalues.put(KEY_RENT, rent);
                aptvalues.put(KEY_UTILITIES, utilities);
                aptvalues.put(KEY_VACANCY, vacancy);
                aptvalues.put(KEY_INTERNET, internet);
                aptvalues.put(KEY_GYM, gym);
                aptvalues.put(KEY_Pets, pets);
                aptvalues.put(KEY_ASMOKING, apartment.getString("smoking"));
                aptvalues.put(KEY_AUID, uid);
                aptvalues.put(KEY_APIAID, Integer.toString(apartment.getInt("id")));
                aptvalues.put(KEY_DURATION, apartment.getString("duration"));
                aptvalues.put(KEY_MOVIN, apartment.getString("dateMovin"));
                aptvalues.put(KEY_PLACETYPE, apartment.getString("placeType"));
                aptvalues.put(KEY_POOL, apartment.getString("pool"));
                aptvalues.put(KEY_WIFI, apartment.getString("wifi"));
                aptvalues.put(KEY_APT_GENDER, apartment.getString("gender"));
                aptvalues.put(KEY_APT_AGE_MIN, apartment.getString("ageMin"));
                aptvalues.put(KEY_APT_AGE_MAX, apartment.getString("ageMax"));
                aptvalues.put(KEY_DEPOSIT, apartment.getString("deposit"));
                aptvalues.put(KEY_MUSIC, apartment.getString("music"));
                aptvalues.put(KEY_GUESTS, apartment.getString("guests"));
                aptvalues.put(KEY_DRUGS, apartment.getString("drugs"));
                aptvalues.put(KEY_LATENIGHTS, apartment.getString("lateNights"));
                aptvalues.put(KEY_WASHER, apartment.getString("washer"));
                aptvalues.put(KEY_DRYER, apartment.getString("dryer"));
                aptvalues.put(KEY_FURNISHED, apartment.getString("furnished"));
                aptvalues.put(KEY_KITCHEN, apartment.getString("kitchen"));
                aptvalues.put(KEY_CLOSET, apartment.getString("closet"));
                aptvalues.put(KEY_AC, apartment.getString("airConditioner"));
                aptvalues.put(KEY_HEATER, apartment.getString("heater"));
                aptvalues.put(KEY_HASPET, apartment.getString("hasPet"));
               // aptvalues.put(KEY_OCCUPANT, apartment.getString("occupant"));

                long aid = db.insert(TABLE_USER_APT, null, aptvalues);
                Log.d(TAG, "New Aprtmetnt inserted into USER sqlite: " + uid);

                JSONArray aptImages = apartment.getJSONArray("aptImages");
                for (int j = 0; j < aptImages.length(); j++) {
                    JSONObject imagejson = aptImages.getJSONObject(j);
                    String urlimage = imagejson.getString("image");

                    ContentValues ivalues = new ContentValues();
                    ivalues.put(KEY_APT_IMAGE, urlimage);
                    ivalues.put(KEY_AIID, aid);
                    ivalues.put(KEY_APIIID, imagejson.getString("id"));

                    long iid = db.insert(TABLE_APT_IMAGES, null, ivalues);

                    Log.d(TAG, "New APT image inserted into USER sqlite: " + uid);

                }
            }
        }
        db.close();
    }

    public long addApartment(JSONObject jsonObject , String row_id) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_APIAID,jsonObject.getString("id"));
        values.put(KEY_ADDRESS,jsonObject.getString("address"));
        values.put(KEY_DESC,jsonObject.getString("desc"));

        JSONObject location = jsonObject.getJSONObject("location");
        JSONArray coordinates = location.getJSONArray("coordinates");
        values.put(KEY_LAT,Double.toString((Double) coordinates.get(1)));
        values.put(KEY_LONG,Double.toString((Double) coordinates.get(0)));
        values.put(KEY_ROOMS,jsonObject.getString("rooms"));
        values.put(KEY_RENT,jsonObject.getString("rent"));
        values.put(KEY_UTILITIES,jsonObject.getString("utilities"));
        values.put(KEY_VACANCY,jsonObject.getString("vacancy"));
        values.put(KEY_INTERNET,jsonObject.getString("internet"));
        values.put(KEY_GYM,jsonObject.getString("gym"));
        values.put(KEY_Pets,jsonObject.getString("pets"));
        values.put(KEY_AUID,Integer.parseInt(row_id));
        values.put(KEY_DURATION,jsonObject.getString("duration"));
        values.put(KEY_MOVIN,jsonObject.getString("dateMovin"));
        values.put(KEY_PLACETYPE,jsonObject.getString("placeType"));
        values.put(KEY_POOL,jsonObject.getString("pool"));
        values.put(KEY_WIFI,jsonObject.getString("wifi"));
        values.put(KEY_APT_GENDER,jsonObject.getString("gender"));
        values.put(KEY_APT_AGE_MIN,jsonObject.getString("ageMin"));
        values.put(KEY_APT_AGE_MAX,jsonObject.getString("ageMax"));
        values.put(KEY_DEPOSIT,jsonObject.getString("deposit"));
        values.put(KEY_MUSIC,jsonObject.getString("music"));
        values.put(KEY_GUESTS,jsonObject.getString("guests"));
        values.put(KEY_DRUGS,jsonObject.getString("drugs"));
        values.put(KEY_LATENIGHTS,jsonObject.getString("lateNights"));
        values.put(KEY_WASHER,jsonObject.getString("washer"));
        values.put(KEY_DRYER,jsonObject.getString("dryer"));
        values.put(KEY_FURNISHED,jsonObject.getString("furnished"));
        values.put(KEY_KITCHEN,jsonObject.getString("kitchen"));
        values.put(KEY_CLOSET,jsonObject.getString("closet"));
        values.put(KEY_AC,jsonObject.getString("airConditioner"));
        values.put(KEY_HEATER,jsonObject.getString("heater"));
        values.put(KEY_HASPET,jsonObject.getString("hasPet"));
        //values.put(KEY_OCCUPANT,jsonObject.getString("occupant"));


        long id = db.insert(TABLE_USER_APT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Apartment inserted into APT sqlite: " + id);

        return id;

    }


    public long updateApartment(JSONObject jsonObject , int row_id) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_APIAID,jsonObject.getString("id"));
        values.put(KEY_ADDRESS,jsonObject.getString("address"));
        values.put(KEY_DESC,jsonObject.getString("desc"));
        JSONObject location = jsonObject.getJSONObject("location");
        JSONArray coordinates = location.getJSONArray("coordinates");
        values.put(KEY_LAT,Double.toString((Double) coordinates.get(1)));
        values.put(KEY_LONG,Double.toString((Double) coordinates.get(0)));
        values.put(KEY_ROOMS,jsonObject.getString("rooms"));
        values.put(KEY_RENT,jsonObject.getString("rent"));
        values.put(KEY_UTILITIES,jsonObject.getString("utilities"));
        values.put(KEY_VACANCY,jsonObject.getString("vacancy"));
        values.put(KEY_INTERNET,jsonObject.getString("internet"));
        values.put(KEY_GYM,jsonObject.getString("gym"));
        values.put(KEY_Pets,jsonObject.getString("pets"));
        values.put(KEY_DURATION,jsonObject.getString("duration"));
        values.put(KEY_MOVIN,jsonObject.getString("dateMovin"));
        values.put(KEY_PLACETYPE,jsonObject.getString("placeType"));
        values.put(KEY_POOL,jsonObject.getString("pool"));
        values.put(KEY_WIFI,jsonObject.getString("wifi"));
        values.put(KEY_APT_GENDER,jsonObject.getString("gender"));
        values.put(KEY_APT_AGE_MIN,jsonObject.getString("ageMin"));
        values.put(KEY_APT_AGE_MAX,jsonObject.getString("ageMax"));
        values.put(KEY_DEPOSIT,jsonObject.getString("deposit"));
        values.put(KEY_MUSIC,jsonObject.getString("music"));
        values.put(KEY_GUESTS,jsonObject.getString("guests"));
        values.put(KEY_DRUGS,jsonObject.getString("drugs"));
        values.put(KEY_LATENIGHTS,jsonObject.getString("lateNights"));
        values.put(KEY_WASHER,jsonObject.getString("washer"));
        values.put(KEY_DRYER,jsonObject.getString("dryer"));
        values.put(KEY_FURNISHED,jsonObject.getString("furnished"));
        values.put(KEY_KITCHEN,jsonObject.getString("kitchen"));
        values.put(KEY_CLOSET,jsonObject.getString("closet"));
        values.put(KEY_AC,jsonObject.getString("airConditioner"));
        values.put(KEY_HEATER,jsonObject.getString("heater"));
        values.put(KEY_HASPET,jsonObject.getString("hasPet"));
       // values.put(KEY_OCCUPANT,jsonObject.getString("occupant"));


        long id = db.update(TABLE_USER_APT,values, KEY_AID+ "="+row_id, null);
        db.close(); // Closing database connection

        Log.d(TAG, "Apartment updated into APT sqlite: " + id);

        return id;

    }

    public void addApartmentImages(String imageURL,int apartmentid,long aiid){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_APT_IMAGE,imageURL);
        values.put(KEY_AIID,aiid);
        values.put(KEY_APIIID,apartmentid);

        long id = db.insert(TABLE_APT_IMAGES,null,values);
        db.close();
        Log.d(TAG, "New Apartment Image URL inserted into APT sqlite: " + id);

    }



    public HashMap<String, String> getHeaderDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        //cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                user.put("fname", cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                user.put("lname", cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                user.put("email", cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                user.put("username", cursor.getString(cursor.getColumnIndex(KEY_USERID)));
                user.put("profileimg", cursor.getString(cursor.getColumnIndex(KEY_PROFILEIMAGE)));

            } while (cursor.moveToNext());



        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }


    public HashMap<String, String> getProfileDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));

        // Move to first row
        //cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                user.put("row_id",Integer.toString(cursor.getInt(cursor.getColumnIndex(KEY_ID))));
                user.put("api_user_id",Integer.toString(cursor.getInt(cursor.getColumnIndex(API_KEY_ID))));
                user.put("fname", cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                user.put("lname", cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                user.put("email", cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                user.put("username", cursor.getString(cursor.getColumnIndex(KEY_USERID)));
                user.put("token",cursor.getString(cursor.getColumnIndex(KEY_TOKEN)));
                user.put("joined", cursor.getString(cursor.getColumnIndex(KEY_JOINED_AT)));
                user.put("age", cursor.getString(cursor.getColumnIndex(KEY_AGE)));
                user.put("gender", cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
                user.put("ucost", cursor.getString(cursor.getColumnIndex(KEY_UCOST)));
                user.put("cooking", cursor.getString(cursor.getColumnIndex(KEY_COOKING)));
                user.put("food_pref", cursor.getString(cursor.getColumnIndex(KEY_FOOD_PREFERENCE)));
                user.put("sleep", cursor.getString(cursor.getColumnIndex(KEY_SLEEP)));
                user.put("clean", cursor.getString(cursor.getColumnIndex(KEY_CLEAN)));
                user.put("smoking", cursor.getString(cursor.getColumnIndex(KEY_SMOKING)));
                user.put("alcohol", cursor.getString(cursor.getColumnIndex(KEY_ALCOHOL)));
                user.put("socialize", cursor.getString(cursor.getColumnIndex(KEY_SOCIALIZING)));
                user.put("noise", cursor.getString(cursor.getColumnIndex(KEY_NOISE)));
                user.put("upets", cursor.getString(cursor.getColumnIndex(KEY_UPETS)));
                user.put("profileimg", cursor.getString(cursor.getColumnIndex(KEY_PROFILEIMAGE)));
                user.put("api_roomie_id", cursor.getString(cursor.getColumnIndex(KEY_R_API_ID)));
                user.put("desc", cursor.getString(cursor.getColumnIndex(KEY_R_DESC)));
                user.put("rage", cursor.getString(cursor.getColumnIndex(KEY_R_AGE)));
                user.put("rgender", cursor.getString(cursor.getColumnIndex(KEY_R_GENDER)));
                user.put("othergender", cursor.getString(cursor.getColumnIndex(KEY_R_GENDER_OTHER)));
                user.put("roomielat", cursor.getString(cursor.getColumnIndex(KEY_R_LAT)));
                user.put("roomielong", cursor.getString(cursor.getColumnIndex(KEY_R_LON)));
                user.put("duration", cursor.getString(cursor.getColumnIndex(KEY_R_DURATION)));
                user.put("movin", cursor.getString(cursor.getColumnIndex(KEY_R_MOVIN)));
                user.put("roomie", cursor.getString(cursor.getColumnIndex(KEY_R_ROOMIE)));
                user.put("otherroomie", cursor.getString(cursor.getColumnIndex(KEY_R_OTHERROOMIE)));
                user.put("rent",cursor.getString(cursor.getColumnIndex(KEY_UCOST)));


            } while (cursor.moveToNext());



        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching Complete Profile details from Sqlite: " + user.toString());

        return user;
    }



    public List<UserApartment> getUserApartmentDetails(){

        List<UserApartment> userAptList= new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_APT;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        //cursor.moveToFirst();
       while(cursor.moveToNext()) {

           UserApartment userApt = new UserApartment();
           userApt.setId(cursor.getInt(cursor.getColumnIndex(KEY_AID)));
           userApt.setAPIid(cursor.getString(cursor.getColumnIndex(KEY_APIAID)));
           userApt.setDuration(cursor.getString(cursor.getColumnIndex(KEY_DURATION)));
           userApt.setDateMovin(cursor.getString(cursor.getColumnIndex(KEY_MOVIN)));
           userApt.setPlaceType(cursor.getString(cursor.getColumnIndex(KEY_PLACETYPE)));
           userApt.setAddress( cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
           userApt.setDesc( cursor.getString(cursor.getColumnIndex(KEY_DESC)));
           userApt.setLat(cursor.getDouble(cursor.getColumnIndex(KEY_LAT)));
           userApt.setLon(cursor.getDouble(cursor.getColumnIndex(KEY_LONG)));
           userApt.setCreated(cursor.getString(cursor.getColumnIndex(KEY_CREATED)));
           userApt.setRooms(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ROOMS))));
           userApt.setRent( Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_RENT))));
           userApt.setUtilities(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_UTILITIES))));
           userApt.setVacancy(cursor.getInt(cursor.getColumnIndex(KEY_VACANCY)));
           userApt.setInternet(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_INTERNET))));
           userApt.setGym(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_GYM))));
           userApt.setPets(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_Pets))));
           userApt.setSmoking(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_ASMOKING))));
           userApt.setPool(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_POOL))));
           userApt.setWifi(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_WIFI))));
           userApt.setAptGender(cursor.getString(cursor.getColumnIndex(KEY_APT_GENDER)));
           userApt.setAptAgeMin(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_APT_AGE_MIN))));
           userApt.setAptAgeMax(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_APT_AGE_MAX))));
           userApt.setDeposit(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DEPOSIT))));
           userApt.setMusic(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_MUSIC))));
           userApt.setGuests(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_GUESTS))));
           userApt.setDrugs(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_DRUGS))));
           userApt.setKitchen(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_KITCHEN))));
           userApt.setCloset(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_CLOSET))));
           userApt.setAC(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_AC))));
           userApt.setHeater(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_HEATER))));
           userApt.setHasPet(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_HASPET))));
           userApt.setFkey(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_AUID))));
           userApt.setWasher(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_WASHER))));
           userApt.setDryer(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_DRYER))));
          // userApt.setOccupant(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_OCCUPANT))));

           String selectImagesQuery = "SELECT * FROM "+TABLE_APT_IMAGES+" WHERE "+KEY_AIID+"="+userApt.getId();
           Cursor cursorAptImg = db.rawQuery(selectImagesQuery,null);
           Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursorAptImg));
           List<String> aptImages = new ArrayList<>();
           while(cursorAptImg.moveToNext()){
               aptImages.add(cursorAptImg.getString(2));
           }
           cursorAptImg.close();
           userApt.setAptImages(aptImages);
           userAptList.add(userApt);

        }

        cursor.close();
        db.close();
        // return user
        for(int j =0;j<userAptList.size();j++)
        Log.d(TAG, "Fetching Complete Apartment details from Sqlite: " + userAptList.get(j).getAddress());
        return userAptList;
    }




    public void deleteUserInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_APT_IMAGES,null,null);
        db.delete(TABLE_USER_APT,null,null);
        db.delete(TABLE_USER, null, null);

        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


    public String getTokenDetails() {

        String token = "";
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        //cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                token =  cursor.getString(cursor.getColumnIndex(KEY_TOKEN));

            } while (cursor.moveToNext());



        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + token);

        return token;

    }
}
