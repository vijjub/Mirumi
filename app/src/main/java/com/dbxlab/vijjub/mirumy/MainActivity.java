package com.dbxlab.vijjub.mirumy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.bumptech.glide.Glide;
import com.dbxlab.vijjub.mirumy.adapters.User;
import com.dbxlab.vijjub.mirumy.adapters.UserApartment;
import com.dbxlab.vijjub.mirumy.fragments.AccountInfo;
import com.dbxlab.vijjub.mirumy.fragments.AccountInfoClayout;
import com.dbxlab.vijjub.mirumy.fragments.HomeFragment;
import com.dbxlab.vijjub.mirumy.fragments.NewBrowseRoomieFragment;
import com.dbxlab.vijjub.mirumy.fragments.UserApartmentInfo;
import com.dbxlab.vijjub.mirumy.fragments.UserAptCompleteInfo;
import com.dbxlab.vijjub.mirumy.pushnotification.FCMRegistrationUtils;

import java.util.HashMap;

import auth.LoginActivity;
import helper.SQliteManager;
import helper.SessionManager;
//import userprofile.UserProfile;

public class MainActivity extends AppCompatActivity implements UserApartmentInfo.OnAptSelectListener,AccountInfo.OnAccountInfoListener,NewBrowseRoomieFragment.OnBrowseRoomieSelectListener{
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    SessionManager session;
    private TextView uidName;
    private TextView email;
    private String applozicUsername,applozicPassword,applozicDisplayName;
    SQliteManager db;
    private UserLoginTask mAuthTask = null;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());

        db = new SQliteManager(getApplicationContext());

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        if(getSupportActionBar()!=null){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);}
        drawerToggle.syncState();
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        HashMap<String,String> user = db.getHeaderDetails();
        String uidNameText = user.get("username");
        applozicUsername = user.get("username")+"MiRumy";
        String emailText = user.get("email");
        applozicPassword = emailText;
        applozicDisplayName = user.get("username");
        String imageurl = user.get("profileimg");
        attemptLogin(com.applozic.mobicomkit.api.account.user.User.AuthenticationType.APPLOZIC);


        View navHeaderView = nvDrawer.inflateHeaderView(R.layout.navheader);

        uidName = (TextView)navHeaderView.findViewById(R.id.fullname);
        uidName.setText(uidNameText);
        email = (TextView)navHeaderView.findViewById(R.id.email);
        email.setText(emailText);
        ImageView imageView = (ImageView)navHeaderView.findViewById(R.id.profileimage);

        Glide.with(this).load(imageurl)
                .error(R.drawable.men)
                .override(50,50)
                .centerCrop()
                .into(imageView);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        Fragment fragmentHome = null; Class fragmentClassHome = null;
        fragmentClassHome = HomeFragment.class;
        try {
            fragmentHome = (Fragment)fragmentClassHome.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } ;

        FragmentManager fragmentManagerHome = getSupportFragmentManager();
        fragmentManagerHome.beginTransaction().replace(R.id.flContent,fragmentHome).commit();


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){

            int aid = bundle.getInt("OPEN_APARTMENT_LIST");

            if(aid == 2){
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = UserApartmentInfo.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            }


        }


    }

    public void attemptLogin(com.applozic.mobicomkit.api.account.user.User.AuthenticationType authenticationType) {
        if (mAuthTask != null) {
            return;
        }

        final Activity activity = MainActivity.this;
        UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {

            @Override
            public void onSuccess(RegistrationResponse registrationResponse, final Context context) {
                mAuthTask = null;
                ApplozicSetting.getInstance(context).showStartNewButton().showPriceOption();

                //Basic settings...

                //ApplozicSetting.getInstance(context).hideConversationContactImage().hideStartNewButton().hideStartNewFloatingActionButton();

                ApplozicSetting.getInstance(context).showStartNewGroupButton()
                        .setCompressedImageSizeInMB(5)
                        .enableImageCompression()
                        .setMaxAttachmentAllowed(5);
                ApplozicClient.getInstance(context).setContextBasedChat(true).setHandleDial(true);
                ApplozicSetting.getInstance(context).enableRegisteredUsersContactCall();//To enable the applozic Registered Users Contact Note:for disable that you can comment this line of code

                //Set activity callbacks
                    /*Map<ApplozicSetting.RequestCode, String> activityCallbacks = new HashMap<ApplozicSetting.RequestCode, String>();
                    activityCallbacks.put(ApplozicSetting.RequestCode.MESSAGE_TAP, MainActivity.class.getName());
                    ApplozicSetting.getInstance(context).setActivityCallbacks(activityCallbacks);*/

                //Start GCM registration....
                FCMRegistrationUtils gcmRegistrationUtils = new FCMRegistrationUtils(activity);
                gcmRegistrationUtils.setUpFcmNotification();

                //buildContactData();
                //starting main MainActivity
//                Intent mainActvity = new Intent(context, MainActivity.class);
//                startActivity(mainActvity);
////                finish();
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                mAuthTask = null;

//                mEmailSignInButton.setVisibility(View.VISIBLE);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage(exception.toString());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                if (!isFinishing()) {
                    alertDialog.show();
                }
            }
        };

        com.applozic.mobicomkit.api.account.user.User user = new com.applozic.mobicomkit.api.account.user.User();
        user.setUserId(applozicUsername);
        user.setPassword(applozicPassword);
        user.setDisplayName(applozicDisplayName);
        user.setAuthenticationTypeId(authenticationType.getValue());

        mAuthTask = new UserLoginTask(user, listener, this);
//        mEmailSignInButton.setVisibility(View.VISIBLE);
        mAuthTask.execute((Void) null);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close)
        {
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
         switch(menuItem.getItemId()) {
            case R.id.account_settings:
                Intent intentAccount = new Intent(MainActivity.this,AccountInfoClayout.class);
                startActivity(intentAccount);
                break;

//

            case R.id.myaptdetails:
                fragmentClass = UserApartmentInfo.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManagerapt = getSupportFragmentManager();
                fragmentManagerapt.beginTransaction().replace(R.id.flContent, fragment).commit();
                break;

             case R.id.roomiesearch :
                 Intent intentBrowseRoomie = new Intent(MainActivity.this, BrowseRoomiesActivity.class);
                 startActivity(intentBrowseRoomie);
                 break;


            case R.id.three:
                session.setLogin(false);
                db.deleteUserInfo();
                Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent1);
                finish();
                break;

             case R.id.addnewapt:
                 Intent intentApt = new Intent(MainActivity.this,AddApartmentActivity.class);
                 startActivity(intentApt);
                 break;

             case R.id.message:
                 Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
                 startActivity(intent);
                 break;

             case R.id.roomiesetting:
                 Intent roomieIntent = new Intent(MainActivity.this, RoomieSettings.class);
                 startActivity(roomieIntent);
                 break;

             case R.id.aptsearch:
                 Intent browseAptIntent = new Intent(MainActivity.this, BrowseApartmentsActivity.class);
                 startActivity(browseAptIntent);
                 break;




        }

        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setApartment(UserApartment userApartment) {
        UserAptCompleteInfo userAptCompleteInfo = new UserAptCompleteInfo();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userApartment",userApartment);
        userAptCompleteInfo.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, userAptCompleteInfo);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void setApartmentRoomies(UserApartment userApartment) {
        NewBrowseRoomieFragment browseRoomiesFragment = new NewBrowseRoomieFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userApartment",userApartment);
        browseRoomiesFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, browseRoomiesFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void setAccountInfo(User user) {
        Intent intent = new Intent(MainActivity.this,AccountInfoClayout.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void setBrowseRoomie(User user) {
        Intent intent = new Intent(MainActivity.this, BrowseUserDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);
        startActivity(intent);;

    }
}