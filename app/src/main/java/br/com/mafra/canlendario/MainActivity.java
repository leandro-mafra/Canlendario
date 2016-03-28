package br.com.mafra.canlendario;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private TextView se1dom, se1seg, se1ter, se1qua, se1qui, se1sex, se1sab;
    private TextView se2dom, se2seg, se2ter, se2qua, se2qui, se2sex, se2sab;
    private TextView se3dom, se3seg, se3ter, se3qua, se3qui, se3sex, se3sab;
    private TextView se4dom, se4seg, se4ter, se4qua, se4qui, se4sex, se4sab;
    private TextView se5dom, se5seg, se5ter, se5qua, se5qui, se5sex, se5sab;
    private TextView se6dom, se6seg, se6ter, se6qua, se6qui, se6sex, se6sab;
    private Spinner spinnermes, spinnerano;
    private List<TextView> listadeText;
    private Button btnevento;
    private ListView listview;
    private RelativeLayout main;
    private RelativeLayout loader;
    private boolean up = false;

    private List<String> meses = new ArrayList<>();
    private List<String> anos = new ArrayList<String>();
    private String marcadia;

    private ArrayAdapter<String> AdMeses;
    private ArrayAdapter<String> AdAnos;

    private View viu, viuu = null;

    private boolean libeAno = false, libeMes = false, libegeral = false;
    private int marcaAno = 0, marcaMes = 0;

    private SQLiteDatabase conn;

    private Context contesto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contesto = this;

        main = (RelativeLayout)findViewById(R.id.layoutmain);
        loader = (RelativeLayout)findViewById(R.id.layoutloadermain);

        meses.add(0, getResources().getString(R.string.jan));
        meses.add(1, getResources().getString(R.string.fev));
        meses.add(2, getResources().getString(R.string.mar));
        meses.add(3, getResources().getString(R.string.abr));
        meses.add(4, getResources().getString(R.string.mai));
        meses.add(5, getResources().getString(R.string.jun));
        meses.add(6, getResources().getString(R.string.jul));
        meses.add(7, getResources().getString(R.string.ago));
        meses.add(8, getResources().getString(R.string.set));
        meses.add(9, getResources().getString(R.string.out));
        meses.add(10, getResources().getString(R.string.nov));
        meses.add(11, getResources().getString(R.string.dez));

        for(int i = 2000 ; i<2200 ; i++){
            anos.add(""+i);
        }

        spinnermes = (Spinner)findViewById(R.id.spinnermes);
        AdMeses = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, meses);
        spinnermes.setAdapter(AdMeses);
        spinnermes.setOnItemSelectedListener(spiclick);

        spinnerano = (Spinner)findViewById(R.id.spinnerano);
        AdAnos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, anos);
        spinnerano.setAdapter(AdAnos);
        spinnerano.setOnItemSelectedListener(spiclick);

        listadeText = new ArrayList<TextView>();

        listview = (ListView)findViewById(R.id.listadeeventos);
        listview.setOnItemLongClickListener(listadeviews);

        se1dom = (TextView)findViewById(R.id.se1dom);
        listadeText.add(se1dom);
        se1seg = (TextView)findViewById(R.id.se1seg);
        listadeText.add(se1seg);
        se1ter = (TextView)findViewById(R.id.se1ter);
        listadeText.add(se1ter);
        se1qua = (TextView)findViewById(R.id.se1qua);
        listadeText.add(se1qua);
        se1qui = (TextView)findViewById(R.id.se1qui);
        listadeText.add(se1qui);
        se1sex = (TextView)findViewById(R.id.se1sex);
        listadeText.add(se1sex);
        se1sab = (TextView)findViewById(R.id.se1sab);
        listadeText.add(se1sab);

        se2dom = (TextView)findViewById(R.id.se2dom);
        listadeText.add(se2dom);
        se2seg = (TextView)findViewById(R.id.se2seg);
        listadeText.add(se2seg);
        se2ter = (TextView)findViewById(R.id.se2ter);
        listadeText.add(se2ter);
        se2qua = (TextView)findViewById(R.id.se2qua);
        listadeText.add(se2qua);
        se2qui = (TextView)findViewById(R.id.se2qui);
        listadeText.add(se2qui);
        se2sex = (TextView)findViewById(R.id.se2sex);
        listadeText.add(se2sex);
        se2sab = (TextView)findViewById(R.id.se2sab);
        listadeText.add(se2sab);

        se3dom = (TextView)findViewById(R.id.se3dom);
        listadeText.add(se3dom);
        se3seg = (TextView)findViewById(R.id.se3seg);
        listadeText.add(se3seg);
        se3ter = (TextView)findViewById(R.id.se3ter);
        listadeText.add(se3ter);
        se3qua = (TextView)findViewById(R.id.se3qua);
        listadeText.add(se3qua);
        se3qui = (TextView)findViewById(R.id.se3qui);
        listadeText.add(se3qui);
        se3sex = (TextView)findViewById(R.id.se3sex);
        listadeText.add(se3sex);
        se3sab = (TextView)findViewById(R.id.se3sab);
        listadeText.add(se3sab);

        se4dom = (TextView)findViewById(R.id.se4dom);
        listadeText.add(se4dom);
        se4seg = (TextView)findViewById(R.id.se4seg);
        listadeText.add(se4seg);
        se4ter = (TextView)findViewById(R.id.se4ter);
        listadeText.add(se4ter);
        se4qua = (TextView)findViewById(R.id.se4qua);
        listadeText.add(se4qua);
        se4qui = (TextView)findViewById(R.id.se4qui);
        listadeText.add(se4qui);
        se4sex = (TextView)findViewById(R.id.se4sex);
        listadeText.add(se4sex);
        se4sab = (TextView)findViewById(R.id.se4sab);
        listadeText.add(se4sab);

        se5dom = (TextView)findViewById(R.id.se5dom);
        listadeText.add(se5dom);
        se5seg = (TextView)findViewById(R.id.se5seg);
        listadeText.add(se5seg);
        se5ter = (TextView)findViewById(R.id.se5ter);
        listadeText.add(se5ter);
        se5qua = (TextView)findViewById(R.id.se5qua);
        listadeText.add(se5qua);
        se5qui = (TextView)findViewById(R.id.se5qui);
        listadeText.add(se5qui);
        se5sex = (TextView)findViewById(R.id.se5sex);
        listadeText.add(se5sex);
        se5sab = (TextView)findViewById(R.id.se5sab);
        listadeText.add(se5sab);

        se6dom = (TextView)findViewById(R.id.se6dom);
        listadeText.add(se6dom);
        se6seg = (TextView)findViewById(R.id.se6seg);
        listadeText.add(se6seg);
        se6ter = (TextView)findViewById(R.id.se6ter);
        listadeText.add(se6ter);
        se6qua = (TextView)findViewById(R.id.se6qua);
        listadeText.add(se6qua);
        se6qui = (TextView)findViewById(R.id.se6qui);
        listadeText.add(se6qui);
        se6sex = (TextView)findViewById(R.id.se6sex);
        listadeText.add(se6sex);
        se6sab = (TextView)findViewById(R.id.se6sab);
        listadeText.add(se6sab);

        btnevento = (Button)findViewById(R.id.btnevento);
        btnevento.setOnClickListener(clickbtn);
        btnevento.setVisibility(View.GONE);

        try {

            ConecSql sql = new ConecSql(this);
            conn = sql.getWritableDatabase();

        }catch (SQLiteException e){}

        Calendar dataatual = Calendar.getInstance();

        GregorianCalendar data = new GregorianCalendar();
        data.setTime(dataatual.getTime());

        ConstroiCalendario(data);

        Boolean alar = PendingIntent.getService(this, 0, new Intent(this, ServiceCalendario.class), PendingIntent.FLAG_NO_CREATE) == null;

        if(alar){

            Calendar calen = Calendar.getInstance();
            calen.setTimeInMillis(System.currentTimeMillis());

            Intent it = new Intent(this, ServiceCalendario.class);
            PendingIntent iit = PendingIntent.getService(this, 0, it, 0);

            AlarmManager alarme = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, calen.getTimeInMillis(), 60000, iit);
        }

    }

    public void ConstroiCalendario(GregorianCalendar data){
        LimpaCalendario();
        final GregorianCalendar datasql = data;


        Calendar caca = Calendar.getInstance();
        GregorianCalendar veridata = new GregorianCalendar();
        veridata.setTime(caca.getTime());
        final int dia;
        if((veridata.get(Calendar.MONTH) == data.get(Calendar.MONTH)) && (veridata.get(Calendar.YEAR) == data.get(Calendar.YEAR))) {
            dia = data.get(Calendar.DAY_OF_MONTH);
        }else{
            dia = 0;
        }

        spinnermes.setSelection(data.get(Calendar.MONTH));

        int posicao=0;
        for(int i = 2000 ; i<data.get(Calendar.YEAR) ; i++){
            posicao++;
        }
        spinnerano.setSelection(posicao);

        data.set(Calendar.DAY_OF_MONTH, 1);
        int semana = data.get(Calendar.DAY_OF_WEEK) - 1;
        for(int i=0; i<data.getActualMaximum(Calendar.DAY_OF_MONTH); i++){


                listadeText.get(semana).setText("" + (i + 1));
                listadeText.get(semana).setContentDescription("" + (i + 1));
                listadeText.get(semana).setOnTouchListener(touch);
                if (i + 1 == dia) {
                    listadeText.get(semana).setBackgroundResource(R.drawable.circulo_vermelho_borda);
                    listadeText.get(semana).setTag(R.drawable.circulo_vermelho_borda);
                    marcadia = (String) listadeText.get(semana).getContentDescription();
                }
                semana++;

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                boolean para = true;
                try {
                    cursor = conn.rawQuery("select _id, data, conteudo from CalendarioAgendMafraSoft where mes = " + datasql.get(Calendar.MONTH) + " and ano = " + datasql.get(Calendar.YEAR) + " order by data ASC;", null);
                }catch (SQLiteException e){
                    para = false;
                    e.getMessage();
                }
                final List<AuxiliarRunOn> listaRun;
                if(para) {
                    listaRun = new ArrayList<AuxiliarRunOn>();
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        AuxiliarRunOn auxi = new AuxiliarRunOn();
                        long dataDia = cursor.getLong(cursor.getColumnIndex("data"));
                        GregorianCalendar dia = new GregorianCalendar();
                        dia.setTimeInMillis(dataDia);
                        auxi.setData(dia);
                        auxi.setConteudo(cursor.getString(cursor.getColumnIndex("conteudo")));
                        auxi.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                        listaRun.add(auxi);
                        cursor.moveToNext();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MarcaData(listaRun);
                        }
                    });

                }
            }
        }).start();


    }

    public void MarcaData(List<AuxiliarRunOn> listaRun){

        List<AuxiliarRunOn> listaadapter = new ArrayList<AuxiliarRunOn>();
        if(listaRun.size() > 0) {


            AuxiliarRunOn auxi2;


            GregorianCalendar data = new GregorianCalendar();
            data.setTimeInMillis(listaRun.get(0).getData().getTimeInMillis());
            data.set(Calendar.DAY_OF_MONTH, 1);
            int semana = data.get(Calendar.DAY_OF_WEEK) - 1;

            for (int i = 0; i < listaRun.size(); i++) {

                if (i == 0) {
                    listadeText.get((listaRun.get(i).getData().get(Calendar.DAY_OF_MONTH) - 1) + (semana)).setBackgroundResource(R.drawable.circulo_verde);
                    listadeText.get((listaRun.get(i).getData().get(Calendar.DAY_OF_MONTH) - 1) + (semana)).setTag(R.drawable.circulo_verde);
                } else {
                    listadeText.get((listaRun.get(i).getData().get(Calendar.DAY_OF_MONTH) - 1) + (semana)).setBackgroundResource(R.drawable.circulo_azul);
                    listadeText.get((listaRun.get(i).getData().get(Calendar.DAY_OF_MONTH) - 1) + (semana)).setTag(R.drawable.circulo_azul);
                }
                auxi2 = new AuxiliarRunOn();
                auxi2.setData(listaRun.get(i).getData());
                auxi2.setConteudo(listaRun.get(i).getConteudo());
                auxi2.setId(listaRun.get(i).getId());
                listaadapter.add(auxi2);
            }
        }

        PersonalAdapter adap = new PersonalAdapter(this, listaadapter);
        listview.setAdapter(adap);

    }

    public void LimpaCalendario(){
        for(int i=0 ; i<listadeText.size() ; i++){
            /******* limpa backgroud *******/
            listadeText.get(i).setBackgroundResource(0);

            /****** lipa o texto *****/
            listadeText.get(i).setText("");

            /********** limpa OnTouchListener **********/
            listadeText.get(i).setOnTouchListener(null);

            /************* limpa ContentDescription ************/
            listadeText.get(i).setContentDescription("" + (i + 1));

            /***************** limpa Tag *****************/
            listadeText.get(i).setTag(0);

        }


    }

    public AdapterView.OnItemLongClickListener listadeviews = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final long idd = id;
            AlertDialog.Builder alerta = new AlertDialog.Builder(contesto);
            alerta.setTitle(R.string.excluir);
            alerta.setMessage(R.string.deleteevent);
            alerta.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    main.setVisibility(View.INVISIBLE);
                    loader.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            conn.execSQL("delete from CalendarioAgendMafraSoft where _id=" + idd + ";");
                            Calendar calendar = Calendar.getInstance();
                            final GregorianCalendar dataTemp = new GregorianCalendar();
                            dataTemp.setTimeInMillis(calendar.getTimeInMillis());
                            dataTemp.set(Calendar.YEAR, Integer.parseInt(anos.get(marcaAno)));
                            dataTemp.set(Calendar.MONTH, marcaMes);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    main.setVisibility(View.VISIBLE);
                                    loader.setVisibility(View.GONE);
                                    ConstroiCalendario(dataTemp);
                                }
                            });

                        }
                    }).start();

                }
            });
            alerta.setNegativeButton(R.string.no, null);
            alerta.show();

            return false;
        }
    };

    public View.OnClickListener clickbtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(contesto, Evento.class);
            it.putExtra("ano", anos.get(marcaAno));
            it.putExtra("mes", marcaMes);
            it.putExtra("dia", marcadia);
            startActivity(it);

        }
    };

    public View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(MotionEvent.ACTION_DOWN == event.getAction()){
                v.setBackgroundResource(R.drawable.circulo_cinza);
                viu = v;
                up = true;
            }

            if(MotionEvent.ACTION_MOVE == event.getAction()){
                if(viuu != null){
                    viuu.setBackgroundResource((Integer)viuu.getTag());
                }
                viu.setBackgroundResource((Integer)viu.getTag());
                btnevento.setVisibility(View.GONE);
                up = false;
            }

            if(MotionEvent.ACTION_UP == event.getAction() && up){

                if(viuu != null){
                    viuu.setBackgroundResource((Integer)viuu.getTag());
                }

                viuu = v;
                v.setBackgroundResource(R.drawable.circulo_vermelho);
                btnevento.setVisibility(View.VISIBLE);
                marcadia = ""+v.getContentDescription();

            }
                return true;

        }
    };

    public AdapterView.OnItemSelectedListener spiclick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            /**************** spinner mes ***************/
            if(parent.getId() == spinnermes.getId()){
                marcaMes = position;
                libeMes = true;
            }

            /**************** spinner ano ***************/
            if(parent.getId() == spinnerano.getId()){
                marcaAno = position;
                libeAno = true;
            }

            if(libeMes && libeAno && libegeral) {
                Calendar dataatual = Calendar.getInstance();

                GregorianCalendar data = new GregorianCalendar();
                data.setTime(dataatual.getTime());

                data.set(Calendar.MONTH, marcaMes);
                data.set(Calendar.YEAR, Integer.parseInt(anos.get(marcaAno)));

                ConstroiCalendario(data);
            }

            if(libeAno && libeMes){
                libegeral = true;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
