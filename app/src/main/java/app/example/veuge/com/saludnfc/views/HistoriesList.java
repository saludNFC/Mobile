package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

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
    private List<History> histories = null;
    private FloatingActionButton addHistoryBtn;
    private String token;

    /*@Override
    protected void onStart() {
        super.onStart();
        getHistoriesList();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histories_list);

        setupUI();

        patientHCode = getIntent().getStringExtra("PATIENT_CODE");
        token = ((Variables) this.getApplication()).getToken();
        histories = getHistoriesList();

        adapter = new HistoriesAdapter(mRecyclerView.getContext());

        adapter.setHistoriesList(histories);
        mRecyclerView.setAdapter(adapter);
    }

    private List<History> getHistoriesList() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + patientHCode + "/antecedentes";
        String resp;
        List<History> histories = null;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
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

    private void setupUI() {
        mLayoutManagerH = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler);
        mRecyclerView.setLayoutManager(mLayoutManagerH);
        mRecyclerView.setHasFixedSize(true);
        addHistoryBtn = (FloatingActionButton) findViewById(R.id.add_history);
    }

    public void createHistory(View view){
        Intent intent = new Intent(this, HistoryCreate.class);
        intent.putExtra("PATIENT_CODE", patientHCode);
        startActivity(intent);
    }
}
