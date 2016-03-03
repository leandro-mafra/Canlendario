package br.com.mafra.canlendario;

import java.util.GregorianCalendar;

/**
 * Created by Leandro on 14/02/2016.
 */
public class AuxiliarRunOn {

    private GregorianCalendar data;
    private String conteudo;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public GregorianCalendar getData() {
        return data;
    }

    public void setData(GregorianCalendar data) {
        this.data = data;
    }

}
