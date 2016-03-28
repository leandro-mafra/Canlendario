package br.com.mafra.canlendario;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Leandro on 19/02/2016.
 */
public class BroatCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calen = Calendar.getInstance();
        calen.setTimeInMillis(System.currentTimeMillis());

        Intent it = new Intent(context, ServiceCalendario.class);
        PendingIntent iit = PendingIntent.getService(context, 0, it, 0);

        AlarmManager alarme = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarme.setRepeating(AlarmManager.RTC_WAKEUP,calen.getTimeInMillis(), 60000, iit);

    }
}
