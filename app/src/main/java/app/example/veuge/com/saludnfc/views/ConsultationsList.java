package app.example.veuge.com.saludnfc.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.ConsultationsAdapter;
import app.example.veuge.com.saludnfc.adapters.ControlsAdapter;
import app.example.veuge.com.saludnfc.models.Consultation;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class ConsultationsList extends AppCompatActivity {

    private String patientHCode;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManagerH;
    private ConsultationsAdapter adapter;
    private Consultation[] consultations = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultations_list);

        mLayoutManagerH = new LinearLayoutManager(this);

        patientHCode = getIntent().getStringExtra("PATIENT_CODE");
        consultations = getConsultationsList();

        mRecyclerView = (RecyclerView) findViewById(R.id.consultation_recycler);
        mRecyclerView.setLayoutManager(mLayoutManagerH);
        mRecyclerView.setHasFixedSize(true);

        adapter = new ConsultationsAdapter(mRecyclerView.getContext());


        adapter.setConsultationsList(consultations);
        mRecyclerView.setAdapter(adapter);
    }

    private Consultation[] getConsultationsList() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + patientHCode + "/consultas";
        String resp;
        Consultation[] consultations = null;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, "");
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
}
