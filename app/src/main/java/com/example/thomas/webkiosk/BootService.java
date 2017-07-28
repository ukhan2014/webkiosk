package com.example.thomas.webkiosk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Thomas Rahn on 2017-05-11.
 */

public class BootService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // here you can add whatever you want this service to do
        Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }

}
