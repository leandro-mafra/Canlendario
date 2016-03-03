package br.com.mafra.canlendario;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Evento extends AppCompatActivity {

    private GregorianCalendar data;

    private TextView textodata;
    private EditText conteudo;
    private NumberPicker hora;
    private NumberPicker minuto;
    private Button btnsalva;
    private ScrollView scrol;
    private Context context;
    private RelativeLayout main;
    private RelativeLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        context = this;
        main = (RelativeLayout)findViewById(R.id.layoutevent);
        loader= (RelativeLayout)findViewById(R.id.layoutloaderevento);
        data = new GregorianCalendar();

        Bundle bundle = getIntent().getExtras();

        try {
            String ano = (String) bundle.get("ano");
            data.set(Calendar.YEAR, Integer.parseInt(ano));

            int mes = (int) bundle.get("mes");
            data.set(Calendar.MONTH, mes);

            String dia = (String) bundle.get("dia");
            data.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
        }catch (Exception e){
            e.getMessage();
            super.finish();
        }

        textodata = (TextView)findViewById(R.id.testodata);
        conteudo = (EditText)findViewById(R.id.novoconteudo);
        hora = (NumberPicker)findViewById(R.id.Pickerhora);
        minuto = (NumberPicker)findViewById(R.id.Pickerminuto);
        btnsalva = (Button)findViewById(R.id.salvaevento);
        scrol = (ScrollView)findViewById(R.id.scrollconteudo);

        textodata.setText(data.get(Calendar.DAY_OF_MONTH) + "/" + (data.get(Calendar.MONTH)+1) + "/" + data.get(Calendar.YEAR));

        hora.setMinValue(0);
        hora.setMaxValue(23);
        hora.setValue(12);

        minuto.setMinValue(0);
        minuto.setMaxValue(59);
        minuto.setValue(0);

        scrol.setOnTouchListener(touch);

        btnsalva = (Button)findViewById(R.id.salvaevento);
        btnsalva.setOnClickListener(click);

        InputMethodManager inp = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
        inp.showSoftInput(conteudo, InputMethodManager.HIDE_IMPLICIT_ONLY);


    }

    public View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            main.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.VISIBLE);
            data.set(Calendar.HOUR_OF_DAY, hora.getValue());
            data.set(Calendar.MINUTE, minuto.getValue());
            final long datafinal = data.getTimeInMillis();
            final int anofinal = data.get(Calendar.YEAR);
            final int mesfinal= data.get(Calendar.MONTH);
            final String conteudofinal = conteudo.getText().toString();
            try {
                ConecSql sql = new ConecSql(context);
                final SQLiteDatabase conn = sql.getWritableDatabase();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            conn.execSQL("insert into CalendarioAgendMafraSoft(data, ano, mes, conteudo) values(" + datafinal + ", " + anofinal + ", " + mesfinal + ", \"" + conteudofinal + "\");");
                            Intent it = new Intent(context, MainActivity.class);
                            startActivity(it);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            });
                        }catch (SQLiteException e){

                        }
                    }
                }).start();

            }catch (SQLiteException e){

            }

        }
    };

    public View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            conteudo.requestFocus();
            conteudo.setSelection(conteudo.length());
            InputMethodManager inp = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
            inp.showSoftInput(conteudo, InputMethodManager.SHOW_FORCED);
            return false;
        }
    };
}
