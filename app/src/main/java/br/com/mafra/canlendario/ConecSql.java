package br.com.mafra.canlendario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Leandro on 13/02/2016.
 */
public class ConecSql extends SQLiteOpenHelper {
    public ConecSql(Context context) {
        super(context, "CalendarioAgendMafraSoft", null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(("CREATE TABLE CalendarioAgendMafraSoft (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, data INTEGER, ano INTEGER, mes INTEGER, conteudo VARCHAR(255));"));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
