package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ConsultationsActivity extends AppCompatActivity {

    private ArrayAdapter<String> mConsultationAdapter;
    private HashMap[] consultations;

    private String codHC, patientID;
    private final String LOG_TAG = ConsultationsActivity.class.getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        updateConsultations();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("patientHistory");
        patientID = intent.getStringExtra("patientID");
        Log.i(LOG_TAG, "ALERT" + codHC);

        mConsultationAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_consultation, // The name of the layout ID.
                R.id.list_item_consultation_textview, // The ID of the textview to populate.
                new ArrayList<String>()
        );

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_consultations);
        listView.setAdapter(mConsultationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String consultationID = consultations[position].get(1).toString();
                Intent intent = new Intent(ConsultationsActivity.this, ConsultationActivity.class);

                intent.putExtra("historia_clinica", codHC);
                intent.putExtra("consultaID", consultationID);

                startActivity(intent);
            }
        });
    }

    private void updateConsultations() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/consultas";
        String resp;
        HashMapTransformation hmt = new HashMapTransformation(consultations);

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path);
            gat.execute();
            resp = gat.get();

            JSONArray consultationArray = hmt.getJsonFromString(resp);
            consultations = hmt.buildConsultationHashmap(consultationArray);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (consultations != null) {
            mConsultationAdapter.clear();

            for (int i = 0; i < consultations.length; i++) {
                mConsultationAdapter.add(consultations[i].get(1).toString() + ", " + trimString(consultations[i].get(2).toString()) + ", "
                        + trimString(consultations[i].get(3).toString()) + ", " + trimString(consultations[i].get(4).toString()));
            }
        }
    }

    public void consultationFormCreate(View view){
        Intent intent = new Intent(ConsultationsActivity.this, ConsultationFormCreateActivity.class);
        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);

        startActivity(intent);
    }

    private String trimString(String longString){
        String trimmed;
        if(longString.length() >= 60){
            trimmed = longString.substring(0, 30);
            trimmed += "...";
        }
        else{
            trimmed = longString;
        }
        return trimmed;
    }
}
