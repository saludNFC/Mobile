package app.example.veuge.com.saludnfc.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.HistoriesAdapter;
import app.example.veuge.com.saludnfc.models.History;
import app.example.veuge.com.saludnfc.models.Patient;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class HistoriesList extends AppCompatActivity {

    private String patientHCode;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManagerH;
    private HistoriesAdapter adapter;
    private History[] histories = null;

    /*@Override
    protected void onStart() {
        super.onStart();
        getHistoriesList();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histories_list);

        mLayoutManagerH = new LinearLayoutManager(this);

        patientHCode = getIntent().getStringExtra("PATIENT_CODE");
        histories = getHistoriesList();

        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler);
        mRecyclerView.setLayoutManager(mLayoutManagerH);
        mRecyclerView.setHasFixedSize(true);

        adapter = new HistoriesAdapter(mRecyclerView.getContext());


        adapter.setHistoriesList(histories);
        mRecyclerView.setAdapter(adapter);
    }

    private History[] getHistoriesList() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + patientHCode + "/antecedentes";
        String resp;
        History[] histories = null;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, "");
            gat.execute();
            resp = gat.get();

            JSONArray historiesArray = hmt.getJsonFromString(resp);
            histories = hmt.buildHistoryObject(historiesArray);
            //patients = hmt.getPatientDataFromJson(resp);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return histories;
    }
}
