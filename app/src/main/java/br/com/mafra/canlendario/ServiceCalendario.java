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
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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
    private Context contesto;
    private ConecSql sql;
    private SQLiteDatabase conn = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate (){
        super.onCreate();
        contesto = this;

        try {
            sql = new ConecSql(contesto);
            conn = sql.getWritableDatabase();
        }catch (SQLiteException e){

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int starid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TretaDoSQL();
            }
        }).start();
        return(START_STICKY);
    }


    public void TretaDoSQL(){

            long tempo = System.currentTimeMillis();

                Cursor cursor = null;

                /********************** notificações futuras ********************/
                try {
                    cursor = conn.rawQuery("select _id, data, conteudo from CalendarioAgendMafraSoft where data >= " + tempo + " and data <= " + (tempo + 59999) + ";", null);
                } catch (SQLiteException e) {

                }

                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        NotificationManager nota = (NotificationManager) getSystemService(contesto.NOTIFICATION_SERVICE);
                        PendingIntent pi = PendingIntent.getActivity(contesto, 0, new Intent(contesto, MainActivity.class), 0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(contesto);
                        builder.setTicker(getResources().getString(R.string.eventcome));
                        builder.setContentTitle(getResources().getString(R.string.eventcome));
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
                        RemoteViews viewremotacontra;

                        if(Build.VERSION.SDK_INT >= 21){
                            viewremotacontra = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_contraida_api21);
                            viewremotacontra.setTextViewText(R.id.textnotifcontraido21, getResources().getString(R.string.data)+": " + texto);
                            viewremotacontra.setTextViewText(R.id.textonotificatitulo21, getResources().getString(R.string.novoevento));
                            viewremotacontra.setTextColor(R.id.textnotifcontraido21, Color.rgb(0, 0, 0));
                            viewremotacontra.setTextColor(R.id.textonotificatitulo21, Color.rgb(0, 0, 0));
                        }else{
                            viewremotacontra = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_contraida);
                            viewremotacontra.setTextViewText(R.id.textnotifcontraido, getResources().getString(R.string.data)+": " + texto);
                            viewremotacontra.setTextViewText(R.id.textonotificatitulo, getResources().getString(R.string.novoevento));
                        }

                        n.contentView = viewremotacontra;
                        /********** notificação expandida *************/
                        RemoteViews viewremotaexpand;

                        if(Build.VERSION.SDK_INT >= 21){
                            viewremotaexpand = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_expandido_api21);
                            viewremotaexpand.setTextViewText(R.id.ntextnotifcontraido21, getResources().getString(R.string.data)+": "+texto+"\n"+testo);
                            viewremotaexpand.setTextViewText(R.id.ntextonotificatitulo21, getResources().getString(R.string.novoevento));
                            viewremotaexpand.setTextColor(R.id.ntextnotifcontraido21, Color.rgb(0, 0, 0));
                            viewremotaexpand.setTextColor(R.id.ntextonotificatitulo21, Color.rgb(0, 0, 0));
                        }else{
                            viewremotaexpand = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_expandido);
                            viewremotaexpand.setTextViewText(R.id.ntextnotifcontraido, getResources().getString(R.string.data)+": "+texto+"\n"+testo);
                            viewremotaexpand.setTextViewText(R.id.ntextonotificatitulo, getResources().getString(R.string.novoevento));
                        }

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
                    cursor = conn.rawQuery("select _id, data, conteudo from CalendarioAgendMafraSoft where data < " + tempo + " order by data desc;", null);
                } catch (SQLiteException e) {

                }

                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        NotificationManager nota = (NotificationManager) getSystemService(contesto.NOTIFICATION_SERVICE);
                        PendingIntent pi = PendingIntent.getActivity(contesto, 0, new Intent(contesto, MainActivity.class), 0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(contesto);
                        builder.setTicker(getResources().getString(R.string.eventperd));
                        builder.setContentTitle(getResources().getString(R.string.eventperd));
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
                        RemoteViews viewremotacontra;
                        
                        if(Build.VERSION.SDK_INT >= 21){
                            viewremotacontra = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_contraida_api21);
                            viewremotacontra.setTextViewText(R.id.textnotifcontraido21, getResources().getString(R.string.data)+": "+texto);
                            viewremotacontra.setTextViewText(R.id.textonotificatitulo21, getResources().getString(R.string.eventperd));
                            viewremotacontra.setTextColor(R.id.textnotifcontraido21, Color.rgb(0, 0, 0));
                            viewremotacontra.setTextColor(R.id.textonotificatitulo21, Color.rgb(0, 0, 0));
                        }else{
                            viewremotacontra = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_contraida);
                            viewremotacontra.setTextViewText(R.id.textnotifcontraido, getResources().getString(R.string.data)+": "+texto);
                            viewremotacontra.setTextViewText(R.id.textonotificatitulo, getResources().getString(R.string.eventperd));
                        }

                        n.contentView = viewremotacontra;
                        /********** notificação expandida *************/
                        RemoteViews viewremotaexpand;

                        if(Build.VERSION.SDK_INT >= 21){
                            viewremotaexpand = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_expandido_api21);
                            viewremotaexpand.setTextViewText(R.id.ntextnotifcontraido21, getResources().getString(R.string.data)+": "+texto+"\n"+testo);
                            viewremotaexpand.setTextViewText(R.id.ntextonotificatitulo21, getResources().getString(R.string.eventperd));
                            viewremotaexpand.setTextColor(R.id.ntextnotifcontraido21, Color.rgb(0, 0, 0));
                            viewremotaexpand.setTextColor(R.id.ntextonotificatitulo21, Color.rgb(0, 0, 0));
                        }else{
                            viewremotaexpand = new RemoteViews(getPackageName(), R.layout.layout_da_notificacao_expandido);
                            viewremotaexpand.setTextViewText(R.id.ntextnotifcontraido, getResources().getString(R.string.data)+": "+texto+"\n"+testo);
                            viewremotaexpand.setTextViewText(R.id.ntextonotificatitulo, getResources().getString(R.string.eventperd));
                        }


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

    }


}
