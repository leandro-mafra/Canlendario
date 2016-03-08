package br.com.mafra.canlendario;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Leandro on 19/02/2016.
 */
public class ServiceCalendario extends Service {
    private boolean thred = true;
    private Context contesto;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate (){
        super.onCreate();
        contesto = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                TretaDoSQL();
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int starid){

        return(START_STICKY);
    }


    public void TretaDoSQL(){
        ConecSql sql;
        SQLiteDatabase conn = null;
        try {
            sql = new ConecSql(contesto);
            conn = sql.getWritableDatabase();
        }catch (SQLiteException e){
            thred = false;
        }

        long loop = 0;
        while (thred) {
            Calendar tempo = Calendar.getInstance();
            if(loop <= tempo.getTimeInMillis()) {
                Cursor cursor = null;

                /********************** notificações futuras ********************/
                try {
                    cursor = conn.rawQuery("select _id, data, conteudo from CalendarioAgendMafraSoft where data >= " + tempo.getTimeInMillis() + " and data <= " + (tempo.getTimeInMillis() + 59999) + ";", null);
                } catch (SQLiteException e) {
                    thred = false;
                }

                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        NotificationManager nota = (NotificationManager) getSystemService(contesto.NOTIFICATION_SERVICE);
                        PendingIntent pi = PendingIntent.getActivity(contesto, 0, new Intent(contesto, MainActivity.class), 0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(contesto);
                        builder.setTicker("Novo evento começou");
                        builder.setContentTitle("Novo evento começou");
                        String testo = cursor.getString(cursor.getColumnIndex("conteudo"));
                        builder.setSmallIcon(R.drawable.icone_notificacao);
                        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icone_notificacao));
                        builder.setContentIntent(pi);

                        Date dataEvePerd = new Date();

                        dataEvePerd.setTime(cursor.getLong(cursor.getColumnIndex("data")));

                        Notification n = builder.build();

                        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String texto = dt.format(dataEvePerd);

                        /*************** notificação resumida **************/
                        RemoteViews viewremotacontra = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_contraida);

                        viewremotacontra.setTextViewText(R.id.textnotifcontraido, "Data: " + texto);
                        viewremotacontra.setTextViewText(R.id.textonotificatitulo, "Novo Evento");

                        n.contentView = viewremotacontra;
                        /********** notificação expandida *************/
                        RemoteViews viewremotaexpand = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_expandido);

                        viewremotaexpand.setTextViewText(R.id.ntextnotifcontraido, "Data: "+texto+"\n"+testo);
                        viewremotaexpand.setTextViewText(R.id.ntextonotificatitulo, "Novo Evento");

                        n.bigContentView = viewremotaexpand;


                        n.vibrate = new long[]{150, 300, 150, 300, 150, 300};
                        n.flags = Notification.FLAG_AUTO_CANCEL;
                        nota.notify((int) (Math.random() * 9999), n);

                        try {
                            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone toque = RingtoneManager.getRingtone(contesto, som);
                            toque.play();
                        } catch (Exception e) {
                        }
                        try {
                            conn.execSQL("delete from CalendarioAgendMafraSoft where _id = " + cursor.getInt(cursor.getColumnIndex("_id")) + ";");
                        } catch (SQLiteException e) {}

                        cursor.moveToNext();
                    }

                }

                /********************** notificações passadas ********************/
                cursor = null;
                try {
                    cursor = conn.rawQuery("select _id, data, conteudo from CalendarioAgendMafraSoft where data < " + tempo.getTimeInMillis() + " order by data desc;", null);
                } catch (SQLiteException e) {
                    thred = false;
                }

                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        NotificationManager nota = (NotificationManager) getSystemService(contesto.NOTIFICATION_SERVICE);
                        PendingIntent pi = PendingIntent.getActivity(contesto, 0, new Intent(contesto, MainActivity.class), 0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(contesto);
                        builder.setTicker("Evento perdido");
                        builder.setContentTitle("Evento perdido");
                        String testo = cursor.getString(cursor.getColumnIndex("conteudo"));
                        builder.setSmallIcon(R.drawable.icone_notificacao);
                        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icone_notificacao));
                        builder.setContentIntent(pi);

                        Date dataEvePerd = new Date();

                        dataEvePerd.setTime(cursor.getLong(cursor.getColumnIndex("data")));

                        Notification n = builder.build();

                        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String texto = dt.format(dataEvePerd);

                        /*************** notificação resumida **************/
                        RemoteViews viewremotacontra = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_contraida);

                        viewremotacontra.setTextViewText(R.id.textnotifcontraido, "Data: "+texto);
                        viewremotacontra.setTextViewText(R.id.textonotificatitulo, "evento perdido");

                        n.contentView = viewremotacontra;
                        /********** notificação expandida *************/
                        RemoteViews viewremotaexpand = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_expandido);

                        viewremotaexpand.setTextViewText(R.id.ntextnotifcontraido, "Data: "+texto+"\n"+testo);
                        viewremotaexpand.setTextViewText(R.id.ntextonotificatitulo, "evento perdido");

                        n.bigContentView = viewremotaexpand;


                        n.vibrate = new long[]{150, 300, 150, 300, 150, 300};
                        n.flags = Notification.FLAG_AUTO_CANCEL;
                        nota.notify((int) (Math.random() * 9999), n);


                        try {
                            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone toque = RingtoneManager.getRingtone(contesto, som);
                            toque.play();
                        } catch (Exception e) {
                        }

                        try {
                            conn.execSQL("delete from CalendarioAgendMafraSoft where _id = " + cursor.getInt(cursor.getColumnIndex("_id")) + ";");
                        }catch (SQLiteException e){}

                        cursor.moveToNext();
                    }

                }
                tempo.add(Calendar.MINUTE, 1);
                tempo.set(Calendar.SECOND, 0);
                tempo.set(Calendar.MILLISECOND, 0);
                loop = tempo.getTimeInMillis();
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        thred = false;
    }

}
