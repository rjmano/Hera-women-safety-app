package com.shainfotech.abhaya;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity {
    private EditText message, primaryContact, secondaryContact;
    public static final String MyPreferences = "AbhayaPreference";
    public static final String Messages = "MessageKey";
    public static final String pContacts = "pContactKey";

    public static final String sContacts = "sContactKey";

    public static final String securityOnOffs = "securityKey";
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences;

    public SettingsActivity(){

    }
    public SettingsActivity (Context context){
        sharedPreferences = context.getSharedPreferences(MyPreferences, 0);
        editor=sharedPreferences.edit();
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        message = (EditText) findViewById(R.id.emergencyMessageTextField);
        primaryContact = (EditText) findViewById(R.id.primaryContactTextField);
        secondaryContact = (EditText) findViewById(R.id.secondaryContactTextField);
        Context context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(MyPreferences, 0);
        if(sharedPreferences.contains(Messages)) {
            message.setText(sharedPreferences.getString(Messages, ""));
        }
        if(sharedPreferences.contains(pContacts)) {
            primaryContact.setText(sharedPreferences.getString(pContacts, ""));
        }
        if(sharedPreferences.contains(sContacts)) {
            secondaryContact.setText(sharedPreferences.getString(sContacts, ""));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
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

    public void settingDetails(View view){
        Context context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(MyPreferences, 0);
        editor= sharedPreferences.edit();
        String emergencyMessage = message.getText().toString();
        String primaryCont = primaryContact.getText().toString();

        String sContact = secondaryContact.getText().toString();

        editor.putString(Messages, emergencyMessage);
        editor.putString(pContacts, primaryCont);

        editor.putString(sContacts, sContact);

        editor.commit();
        Toast.makeText(this,"Configuration details have been saved", Toast.LENGTH_SHORT).show();
    }

    public boolean checkSecurity () {
         return sharedPreferences.getBoolean(securityOnOffs, false);

    }


    public void setSecurity(){

    }
}
