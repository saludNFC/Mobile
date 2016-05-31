package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.HashMap;

public class PatientActivity extends AppCompatActivity {

    public HashMap[] patient; // array of length 1
    public static String patientID;
    public static String codHC;

    TextView name, misc, history, ci, blood;

    String misce = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        name = (TextView) findViewById(R.id.patient_name);
        misc = (TextView) findViewById(R.id.patient_misc);
        history = (TextView) findViewById(R.id.patient_history);
        ci = (TextView) findViewById(R.id.patient_ci);
        blood = (TextView) findViewById(R.id.patient_bloodtype);

        Intent intent = getIntent();
        patientID = intent.getStringExtra("patientID");
        codHC = intent.getStringExtra("patientHistory");

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC;

        String resp;
        HashMapTransformation hmt = new HashMapTransformation(patient);

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path);
            gat.execute();
            resp = gat.get();
            JSONArray patientArray = hmt.getJsonFromString(resp);
            patient = hmt.buildPatientHashmap(patientArray);
            //patient = hmt.getPatientDataFromJson(resp);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(patient != null){
            if (patient != null && patient[0].get(3) != null && patient[0].get(4) != null ) {
                name.setText(patient[0].get(5).toString() + " " + patient[0].get(4).toString());

                misce += patient[0].get(6).toString() + ", " + patient[0].get(7).toString();
                misc.setText(misce);
            }

            history.setText(codHC);
            ci.setText(patient[0].get(2).toString() + " " + patient[0].get(3).toString());
            blood.setText(patient[0].get(12).toString());
        }
    }


    public void patientFormCreate(View view){
        Intent intent = new Intent(PatientActivity.this, PatientFormCreateActivity.class);
        intent.putExtra("historia", codHC);
        startActivity(intent);
    }

    public void patientHistories(View view){
        Intent intent = new Intent(PatientActivity.this, HistoriesActivity.class);

        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        startActivity(intent);
    }

    public void patientControls(View view){
        Intent intent = new Intent(PatientActivity.this, ControlsActivity.class);

        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        startActivity(intent);
    }

    public void patientConsultations(View view){
        Intent intent = new Intent(PatientActivity.this, ConsultationsActivity.class);

        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        startActivity(intent);
    }
}
