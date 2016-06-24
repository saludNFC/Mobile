package app.example.veuge.com.saludnfc.models;

import java.io.Serializable;

/**
 * Created by veuge on 05-06-16.
 */

public class History implements Serializable{
    public int id;
    public String historyType;
    public String grade;
    public String illness;
    public String typePersonal;
    public String description;
    public String med;
    public String via;
    public String dateIni;
    public String createdAt;

    /**
     * Constructor for common attributes in all histories
     * @param id int
     * @param ht string
     */
    public History(int id, String ht, String ca){
        this.id = id;
        this.historyType = ht;
        this.createdAt = ca;
    }

    public History(int id,
                   String ht,
                   String gr,
                   String ill,
                   String tp,
                   String des,
                   String med,
                   String via,
                   String date,
                   String ca){
        this.id = id;
        this.historyType = ht;
        this.grade = gr;
        this.illness = ill;
        this.typePersonal = tp;
        this.description = des;
        this.med = med;
        this.via = via;
        this.dateIni = date;
        this.createdAt = ca;
    }

    public void  setGrade(String gr){
        this.grade = gr;
    }

    public void setIllness(String ill){
        this.illness = ill;
    }

    public void setTypePersonal(String tp){
        this.typePersonal = tp;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    public void setMed(String med){
        this.med = med;
    }

    public void setVia(String via){
        this.via = via;
    }

    public void setDateIni(String dini){
        this.dateIni = dini;
    }
}
