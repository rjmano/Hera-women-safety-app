package com.shainfotech.abhaya;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class AbhayaMainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,CompoundButton.OnCheckedChangeListener {




    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    SharedPreferences sharedPreferences;
    SettingsActivity settingsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abhaya_main);

        //Context context = getApplicationContext();
        //sharedPreferences = context.getSharedPreferences(settingsActivity.MyPreferences, 0);
        //SharedPreferences.Editor editor = sharedPreferences.edit();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        settingsActivity = new SettingsActivity(AbhayaMainActivity.this);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        //Switch Code here...
        Switch securityOnOff = (Switch) findViewById(R.id.security_on_off);
        Boolean security = settingsActivity.checkSecurity();
        String securityState = (security) ? "ON" : "OFF";
        Toast.makeText(this,"Security is " + securityState, Toast.LENGTH_LONG).show();
        securityOnOff.setChecked(security);


        //Toast.makeText(this, "Security Current Position " + (securityOnOff.isChecked() ? "On" : "Off"),Toast.LENGTH_SHORT).show();
        if (securityOnOff != null) {
            securityOnOff.setOnCheckedChangeListener(this);
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked){
        Context context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(settingsActivity.MyPreferences, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(isChecked){
            editor.putBoolean(settingsActivity.securityOnOffs, true);
            Toast.makeText(this, "Security is ON", Toast.LENGTH_LONG).show();
        } else {
            editor.putBoolean(settingsActivity.securityOnOffs, false);
            Toast.makeText(this, "Security is OFF", Toast.LENGTH_LONG).show();
        }
        editor.commit();
    }
//    @Override
//    public void startService(view View){
//        startService(new Intent(getBaseContext(), BackGroundServices.class));
//    }
//
//    @Override
//    public void stopService(view View){
//        stopService(new Intent(getBaseContext(), BackGroundServices.class));
//    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "HERA 2.0";
                break;
            case 2:
                mTitle = "HERA 2.0";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.abhaya_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_abhaya_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((AbhayaMainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /* Function Name : shareWithWhatsApp(View view)
       Usage : Send Message through WhatsApp via intent
     */
    public void shareWithWhatsApp (View view){
        Context context = getApplicationContext();
        GPSTracker gpsTracker = new GPSTracker(context);
        //Boolean checkInternet = checkInternetConnection(context);
        sharedPreferences = context.getSharedPreferences(settingsActivity.MyPreferences,
                0);
        SmsManager smsManager = SmsManager.getDefault();
        Boolean security = sharedPreferences.getBoolean(settingsActivity.securityOnOffs, false);
        String message = sharedPreferences.getString(settingsActivity.Messages, null);
        String pContact = sharedPreferences.getString(settingsActivity.pContacts, null);
        String sContact = sharedPreferences.getString(settingsActivity.sContacts, null);
        if(security) {
                if (sharedPreferences.contains(settingsActivity.Messages)) {

                        String urlWithPrefix = "";
                        if(gpsTracker.isGPSEnabled) {
                            String stringLatitude = String.valueOf(gpsTracker.latitude);
                            String stringLongitude = String.valueOf(gpsTracker.longitude);
                            urlWithPrefix = " and I am at https://www.google.co.in/maps/@" + stringLatitude + "," + stringLongitude + ",19z";
                        }else{
                            Toast.makeText(context,"Your GPS is OFF", Toast.LENGTH_LONG).show();
                        }
                    /* WhatsApp Intent will come here */
                    PackageManager pm=getPackageManager();
                    try {

                        Intent waIntent = new Intent(Intent.ACTION_SEND);
                        waIntent.setType("text/plain");
                        String text = "YOUR TEXT HERE";

                        PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                        //Check if package exists or not. If not then code
                        //in catch block will be called
                        waIntent.setPackage("com.whatsapp");

                        waIntent.putExtra(Intent.EXTRA_TEXT, text);
                        startActivity(Intent.createChooser(waIntent, "Share with"));

                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                                .show();
                    }
                    /* WhatsApp Intent code will end here */
                } else {
                    Toast.makeText(context,
                            "You haven't setup any Emergency Message in ABHAYA App",
                            Toast.LENGTH_LONG).show();
                }
        } else {
            Toast.makeText(context,
                    "Your Security is OFF in ABHAYA App",
                    Toast.LENGTH_LONG).show();
        }


    }
    /* Function Name : sendMessage (View view)
       Usage : Send SMS to configured Number with GPS Location if it is on
     */
    public void sendMessage (View view){
        Context context = getApplicationContext();
        GPSTracker gpsTracker = new GPSTracker(context);
        //Boolean checkInternet = checkInternetConnection(context);
        sharedPreferences = context.getSharedPreferences(settingsActivity.MyPreferences,
                0);
        SmsManager smsManager = SmsManager.getDefault();
        Boolean security = sharedPreferences.getBoolean(settingsActivity.securityOnOffs, false);
        String message = sharedPreferences.getString(settingsActivity.Messages, null);
        String pContact = sharedPreferences.getString(settingsActivity.pContacts, null);
        String sContact = sharedPreferences.getString(settingsActivity.sContacts, null);
        if(security) {
            if (sharedPreferences.contains(settingsActivity.pContacts) || sharedPreferences.contains(settingsActivity.sContacts)) {
                if (sharedPreferences.contains(settingsActivity.Messages)) {
                    if (sharedPreferences.contains(settingsActivity.pContacts)) {
                        String urlWithPrefix = "";
                        if(gpsTracker.isGPSEnabled) {
                            String stringLatitude = String.valueOf(gpsTracker.latitude);
                            String stringLongitude = String.valueOf(gpsTracker.longitude);
                            urlWithPrefix = " and I am at https://www.google.co.in/maps/@" + stringLatitude + "," + stringLongitude + ",19z";
                        }else{
                            Toast.makeText(context,"Your GPS is OFF", Toast.LENGTH_LONG).show();
                        }

                        if (pContact != null && !pContact.isEmpty()) {
                            message = message + urlWithPrefix;
                            smsManager.sendTextMessage(pContact, null, message, null, null);
                            Toast.makeText(context, "Message sent : " + pContact, Toast.LENGTH_LONG).show();

                            if(sContact != null && !sContact.isEmpty()){
                                String url = (pContact != null && !pContact.isEmpty()) ? "" : urlWithPrefix;
                                message = message + url;
                                smsManager.sendTextMessage(sContact, null, message, null, null);
                                Toast.makeText(context, "Message sent : " + sContact, Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(context,
                                    "Please setup Primary Contact in ABHAYA App",
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(context,
                                "Please setup Primary Contact in ABHAYA App",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context,
                            "You haven't setup any Emergency Message in ABHAYA App",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context,
                        "Please Configure contact details in ABHAYA App",
                        Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(context,
                    "Your Security is OFF in ABHAYA App",
                    Toast.LENGTH_LONG).show();
        }

    }

}
