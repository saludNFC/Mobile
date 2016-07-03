package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;

import java.util.List;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.ControlsAdapter;
import app.example.veuge.com.saludnfc.models.Control;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class ControlsList extends AppCompatActivity {

    private String patientHCode;
    private String token;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManagerH;
    private ControlsAdapter adapter;
    private List<Control> controls;
    private FloatingActionButton addControlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controls_list);

        patientHCode = getIntent().getStringExtra("PATIENT_CODE");
        token = ((Variables) this.getApplication()).getToken();

        setupUI();
    }

    private void setupUI(){
        mLayoutManagerH = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.control_recycler);
        mRecyclerView.setLayoutManager(mLayoutManagerH);
        mRecyclerView.setHasFixedSize(true);

        adapter = new ControlsAdapter(mRecyclerView.getContext());
        controls = getControlsList();
        adapter.setControlsList(controls);
        mRecyclerView.setAdapter(adapter);
        addControlBtn = (FloatingActionButton) findViewById(R.id.add_control);
    }
    private List<Control> getControlsList() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + patientHCode + "/controles";
        String resp;
        List<Control> controls = null;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();

            JSONArray controlsArray = hmt.getJsonFromString(resp);
            controls = hmt.buildControlObject(controlsArray);
            //patients = hmt.getPatientDataFromJson(resp);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return controls;
    }

    public void createControl(View view){
        Intent intent = new Intent(this, ControlCreate.class);
        intent.putExtra("PATIENT_CODE", patientHCode);
        startActivity(intent);
    }
}
