package com.shainfotech.abhaya;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.SmsManager;
import android.widget.RemoteViews;
import android.widget.Toast;


/**
 * Implementation of App Widget functionality.
 */
public class abhaya_widget extends AppWidgetProvider {
    SharedPreferences sharedPreferences;
    SettingsActivity settingsActivity;



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            CharSequence widgetText = context.getString(R.string.appwidget_text);
            Intent intent = new Intent(context, abhaya_widget.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.abhaya_widget);
            views.setOnClickPendingIntent(R.id.imageView,pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i],views);
        }

    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        //Toast.makeText(context, "Widget Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {



        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.abhaya_widget);

    }

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context,intent);
        GPSTracker gpsTracker = new GPSTracker(context);
        Boolean checkInternet = checkInternetConnection(context);
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

    public boolean checkInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}


