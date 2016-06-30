package app.example.veuge.com.saludnfc.views;

import android.app.SearchManager;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.PatientFormCreateActivity;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.PatientsAdapter;
import app.example.veuge.com.saludnfc.models.Patient;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class PatientsList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PatientsAdapter adapter;
    private Patient[] patients;
    private FloatingActionButton fab;

    @Override
    protected void onStart() {
        super.onStart();
        getPatientsList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patients_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.patient_recycler);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        adapter = new PatientsAdapter(mRecyclerView.getContext());
        patients = getPatientsList();
        adapter.setPatientsList(patients);
        mRecyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private Patient[] getPatientsList() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente";
        String resp;
        Patient[] patients = null;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, "");
            gat.execute();
            resp = gat.get();

            JSONArray patientsArray = hmt.getJsonFromString(resp);
            patients = hmt.buildPatientObject(patientsArray);
            //patients = hmt.getPatientDataFromJson(resp);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return patients;
    }

    public void floatingButton(View view){
        Toast.makeText(this, "CHAU", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PatientFormCreateActivity.class);
        intent.putExtra("token", "");
        this.startActivity(intent);
    }
}