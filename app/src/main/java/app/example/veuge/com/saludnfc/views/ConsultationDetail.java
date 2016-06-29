package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.ConsultationsAdapter;
import app.example.veuge.com.saludnfc.models.Consultation;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class ConsultationDetail extends AppCompatActivity {

    private TextView createdAt;
    private TextView anamnesis;
    private TextView physical;
    private TextView diagnosis;
    private TextView treatment;
    private TextView justification;

    private Consultation consultation;
    private Consultation[] consultationAPI;
    private ConsultationsAdapter adapter;

    private String codHC;
    private int patientID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation_detail);

        Intent intent = getIntent();
        consultation = (Consultation) intent.getSerializableExtra("CONSULTATION");

        setupUI();
        fillDetails();
    }

    public void setupUI() {
        createdAt = (TextView) findViewById(R.id.created_at);
        anamnesis = (TextView) findViewById(R.id.anamnesis);
        physical = (TextView) findViewById(R.id.physical);
        diagnosis = (TextView) findViewById(R.id.diagnosis);
        treatment = (TextView) findViewById(R.id.treatment);
        justification = (TextView) findViewById(R.id.justification);
    }

    public void fillDetails() {
        createdAt.setText(consultation.createdAt);
        anamnesis.setText(consultation.anamnesis);
        physical.setText(consultation.physicalExam);
        diagnosis.setText(consultation.diagnosis);
        treatment.setText(consultation.treatment);
        justification.setText(consultation.justification);

    }

    public void getPatientFromAPI() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/consultas";

        String resp;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, "");
            gat.execute();
            resp = gat.get();
            JSONArray consultationArray = hmt.getJsonFromString(resp);
            consultationAPI = hmt.buildConsultationObject(consultationArray);
            //patient = hmt.getPatientDataFromJson(resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
