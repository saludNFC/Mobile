package app.example.veuge.com.saludnfc.models;

/**
 * Created by veuge on 05-06-16.
 */
public class Consultation {

    public String id, idPaciente, anamnesis, physicalExam, diagnosis, treatment, justification, createdAt;

    public Consultation(String id, String anam, String pe, String diag, String treat, String jus, String ca){
        this.id = id;
        //this.idPaciente = idP;
        this.anamnesis = anam;
        this.physicalExam = pe;
        this.diagnosis = diag;
        this.treatment = treat;
        this.justification = jus;
        this.createdAt = ca;
    }
}
