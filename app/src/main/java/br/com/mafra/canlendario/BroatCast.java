package br.com.mafra.canlendario;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Leandro on 19/02/2016.
 */
public class BroatCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent it = new Intent(context, ServiceCalendario.class);
        context.startService(it);
    }
}
