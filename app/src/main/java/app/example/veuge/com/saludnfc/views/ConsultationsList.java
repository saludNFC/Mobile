package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.ConsultationsAdapter;
import app.example.veuge.com.saludnfc.models.Consultation;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class ConsultationsList extends AppCompatActivity {

    private String patientHCode;
    private String token;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManagerH;
    private ConsultationsAdapter adapter;
    private List<Consultation> consultations = null;
    private FloatingActionButton addConsultationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultations_list);
        setupUI();

        patientHCode = getIntent().getStringExtra("PATIENT_CODE");
        token = ((Variables) this.getApplication()).getToken();
        consultations = getConsultationsList();

        adapter = new ConsultationsAdapter(mRecyclerView.getContext());
        adapter.setConsultationsList(consultations);
        mRecyclerView.setAdapter(adapter);
    }

    public void setupUI(){
        mLayoutManagerH = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.consultation_recycler);
        mRecyclerView.setLayoutManager(mLayoutManagerH);
        mRecyclerView.setHasFixedSize(true);
        addConsultationBtn = (FloatingActionButton) findViewById(R.id.add_consultation);
    }

    public List<Consultation> getConsultationsList() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + patientHCode + "/consultas";
        String resp;
        List<Consultation> consultations = null;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();

            JSONArray consultationsArray = hmt.getJsonFromString(resp);
            consultations = hmt.buildConsultationObject(consultationsArray);
            //patients = hmt.getPatientDataFromJson(resp);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return consultations;
    }

    public void createConsultation(View view){
        Intent intent = new Intent(this, ConsultationCreate.class);
        intent.putExtra("PATIENT_CODE", patientHCode);
        startActivity(intent);
    }
}
