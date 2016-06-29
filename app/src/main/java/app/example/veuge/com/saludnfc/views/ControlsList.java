package app.example.veuge.com.saludnfc.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.ControlsAdapter;
import app.example.veuge.com.saludnfc.models.Control;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class ControlsList extends AppCompatActivity {

    private String patientHCode;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManagerH;
    private ControlsAdapter adapter;
    private Control[] controls = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controls_list);

        mLayoutManagerH = new LinearLayoutManager(this);

        patientHCode = getIntent().getStringExtra("PATIENT_CODE");
        controls = getControlsList();

        mRecyclerView = (RecyclerView) findViewById(R.id.control_recycler);
        mRecyclerView.setLayoutManager(mLayoutManagerH);
        mRecyclerView.setHasFixedSize(true);

        adapter = new ControlsAdapter(mRecyclerView.getContext());


        adapter.setControlsList(controls);
        mRecyclerView.setAdapter(adapter);
    }

    private Control[] getControlsList() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + patientHCode + "/controles";
        String resp;
        Control[] controls = null;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, "");
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
}
