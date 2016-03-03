package br.com.mafra.canlendario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Leandro on 17/02/2016.
 */
public class PersonalAdapter extends BaseAdapter{

    private List<AuxiliarRunOn> lista;
    private Context contesto;

    public PersonalAdapter(Context contesto,List<AuxiliarRunOn> lista){
        this.contesto = contesto;
        this.lista = lista;

    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout;

            AuxiliarRunOn auxi = lista.get(position);
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) contesto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.layoutadapter, null);
            }else {
                layout = convertView;
            }

            TextView hora = (TextView) layout.findViewById(R.id.adapterhora);
            TextView conteudo = (TextView) layout.findViewById(R.id.adapterconteudo);
            TextView data = (TextView) layout.findViewById(R.id.adpterdata);

            String minutoTemp;
            if(auxi.getData().get(Calendar.MINUTE) < 10){
                minutoTemp = "0"+auxi.getData().get(Calendar.MINUTE);
            }else{
                minutoTemp = ""+auxi.getData().get(Calendar.MINUTE);
            }

            hora.setText(auxi.getData().get(Calendar.HOUR_OF_DAY)+":"+minutoTemp);
            conteudo.setText(auxi.getConteudo());
            data.setText(auxi.getData().get(Calendar.DAY_OF_MONTH)+"/"+(auxi.getData().get(Calendar.MONTH)+1)+"/"+auxi.getData().get(Calendar.YEAR));


        return layout;
    }
}
