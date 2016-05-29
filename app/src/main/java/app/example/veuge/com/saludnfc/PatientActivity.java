package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.HashMap;

public class PatientActivity extends AppCompatActivity {

    public static String codHC;
    public HashMap[] patient; // array of length 1

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
        codHC = intent.getStringExtra(PatientsActivity.EXTRA_TEXT);

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC;

        String resp;
        HashMapTranformation hmt = new HashMapTranformation(patient);

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

    public void patientForm(View view){
        Intent intent = new Intent(PatientActivity.this, PatientFormUpdateActivity.class);

        intent.putExtra("historia", codHC);
        intent.putExtra("ci", patient[0].get(1).toString());
        intent.putExtra("emision", patient[0].get(2).toString());
        intent.putExtra("nombre", patient[0].get(3).toString());
        intent.putExtra("apellido", patient[0].get(4).toString());
        intent.putExtra("sexo", patient[0].get(5).toString());
//        intent.putExtra("fecha_nacimiento", patient[0].get(6).toString());
        intent.putExtra("lugar_nacimiento", patient[0].get(7).toString());
        intent.putExtra("grado_instruccion", patient[0].get(8).toString());
        intent.putExtra("estado_civil", patient[0].get(9).toString());
        intent.putExtra("ocupacion", patient[0].get(10).toString());
        intent.putExtra("grupo_sanguineo", patient[0].get(11).toString());

        startActivity(intent);
    }

    public void patientHistories(View view){
        Intent intent = new Intent(PatientActivity.this, HistoriesActivity.class);

        intent.putExtra("historia", codHC);
        startActivity(intent);
    }

    public void patientControls(View view){
        Intent intent = new Intent(PatientActivity.this, ControlsActivity.class);

        intent.putExtra("historia", codHC);
        startActivity(intent);
    }

    public void patientConsultations(View view){
        Intent intent = new Intent(PatientActivity.this, ConsultationActivity.class);

        intent.putExtra("historia", codHC);
        startActivity(intent);
    }
}
