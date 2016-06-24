package app.example.veuge.com.saludnfc.models;

import java.io.Serializable;

/**
 * Created by veuge on 05-06-16.
 */
public class Control implements Serializable{

    public int id;
    public String controlType;
    public String vaccine;
    public String viaVac;
    public int dosis;
    public float weight;
    public float height;
    public float temperature;
    public int heartRate;
    public int systole;
    public int diastole;
    public String lastMenst;
    public String lastMamo;
    public boolean sex;
    public String lastPapa;
    public String geriatric;
    public String notes;
    public String createdAt;

    public Control(int id, String ct, String ca) {
        this.id = id;
        this.controlType = ct;
        this.createdAt = ca;
    }

    public Control(int id,
                   String ct,
                   String vac,
                   String viaV,
                   int dosis,
                   float we,
                   float he,
                   float temp,
                   int hr,
                   int sis,
                   int dia,
                   String menst,
                   String mamo,
                   boolean sex,
                   String papa,
                   String ger,
                   String notes,
                   String ca) {

        this.id = id;
        this.controlType = ct;
        this.createdAt = ca;
        this.vaccine = vac;
        this.viaVac = viaV;
        this.dosis = dosis;
        this.weight = we;
        this.height = he;
        this.temperature = temp;
        this.heartRate = hr;
        this.systole = sis;
        this.diastole = dia;
        this.lastMenst = menst;
        this.lastMamo = mamo;
        this.sex = sex;
        this.lastPapa = papa;
        this.geriatric = ger;
        this.notes = notes;
    }

    public void setVaccine(String vac){
        this.vaccine = vac;
    }

    public void setViaVac(String via){
        this.viaVac = via;
    }

    public void setDosis(int dosis){
        this.dosis = dosis;
    }

    public void setWeight(float weight){
        this.weight = weight;
    }

    public void setHeighto(float height){
        this.height = height;
    }

    public void setTemperature(float temp){
        this.temperature = temp;
    }

    public void setHeartRate(int hr){
        this.heartRate = hr;
    }

    public void setSystole(int sys){
        this.systole = sys;
    }

    public void setDiastole(int dias){
        this.diastole = dias;
    }

    public void setLastMenst(String menst){
        this.lastMenst = menst;
    }

    public void setLastMamo(String mamo){
        this.lastMamo = mamo;
    }

    public void setSex(boolean sex){
        this.sex = sex;
    }

    public void setLastPapa(String papa){
        this.lastPapa = papa;
    }

    public void setGeriatric(String ger){
        this.geriatric = ger;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }
}
