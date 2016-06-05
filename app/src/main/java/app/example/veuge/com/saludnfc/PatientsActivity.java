package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;

import app.example.veuge.com.saludnfc.models.Patient;

public class PatientsActivity extends AppCompatActivity{

    private ArrayAdapter<String> mPatientAdapter;
    private HashMap[] patients;
    private static String token;

    @Override
    public void onStart() {
        super.onStart();
        updatePatients();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        mPatientAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_patient, // The name of the layout ID.
                R.id.list_item_patient_textview, // The ID of the textview to populate.
                new ArrayList<String>()
        );

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_patients);
        listView.setAdapter(mPatientAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String patientHistory = patients[position].get(1).toString();
                String patientID = patients[position].get(0).toString();
                Intent intent = new Intent(PatientsActivity.this, PatientActivity.class);

                intent.putExtra("patientID", patientID);
                intent.putExtra("patientHistory", patientHistory);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
    }

    /**
     * Displays patient form to create new patient
     */
    public void patientFormCreate(View view){
        Intent intent = new Intent(PatientsActivity.this, PatientFormCreateActivity.class);

        intent.putExtra("token", token);
        startActivity(intent);
    }

    /**
     * Function to update the patients list!
     */
    private void updatePatients() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente";
        String resp;
        HashMapTransformation hmt = new HashMapTransformation(patients);

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();

            JSONArray patientsArray = hmt.getJsonFromString(resp);
            patients = hmt.buildPatientHashmap(patientsArray);
            //patients = hmt.getPatientDataFromJson(resp);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (patients != null) {
            mPatientAdapter.clear();
            for (int i = 0; i < patients.length; i++) {
                mPatientAdapter.add(patients[i].get(5).toString() + ", " + patients[i].get(4).toString());
            }
        }
    }
}
