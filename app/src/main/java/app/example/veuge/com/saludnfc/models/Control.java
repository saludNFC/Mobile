package app.example.veuge.com.saludnfc.models;

/**
 * Created by veuge on 05-06-16.
 */
public class Control {

    public String id, controlType, vaccine, viaVac, dosis,
            weight, height,
            temperature, heartRate, sistole, diastole,
            lastMenst, lastMamo, sex, lastPapa,
            geriatric, notes;

    public Control(String id, String ct, String vac, String viaV, String dosis, String we, String he,
            String temp, String hr, String sis, String dia, String menst, String mamo, String sex, String papa, String ger, String notes){

        this.id = id;
        this.controlType = ct;
        this.vaccine = vac;
        this.viaVac = viaV;
        this.dosis = dosis;
        this.weight = we;
        this.height = he;
        this.temperature = temp;
        this.heartRate = hr;
        this.sistole = sis;
        this.diastole = dia;
        this.lastMenst = menst;
        this.lastMamo = mamo;
        this.sex = sex;
        this.lastPapa = papa;
        this.geriatric = ger;
        this.notes = notes;
    }

}
