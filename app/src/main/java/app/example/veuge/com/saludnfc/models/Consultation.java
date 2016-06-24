package app.example.veuge.com.saludnfc.models;

import java.io.Serializable;

/**
 * Created by veuge on 14-06-16.
 */
public class Consultation implements Serializable{
    public int id;
    public String anamnesis;
    public String physicalExam;
    public String diagnosis;
    public String treatment;
    public String justification;
    public String createdAt;

    public Consultation(int id, String anam, String pe, String diag, String treat, String jus, String ca){
        this.id = id;
        this.anamnesis = anam;
        this.physicalExam = pe;
        this.diagnosis = diag;
        this.treatment = treat;
        this.justification = jus;
        this.createdAt = ca;
    }
}
