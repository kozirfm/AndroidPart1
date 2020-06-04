package ru.geekbrains.kozirfm.weatherapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import androidx.core.app.NotificationCompat;

public class LowBatteryModeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("LowBatteryModeReceiver")
                .setContentText("Battery LOW");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            int messageId = 1;
            notificationManager.notify(messageId, builder.build());
        }
    }

}

