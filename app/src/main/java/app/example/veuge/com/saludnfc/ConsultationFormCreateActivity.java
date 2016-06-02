package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ConsultationFormCreateActivity extends AppCompatActivity {

    private final String LOG_TAG = ConsultationFormCreateActivity.class.getSimpleName();
    public static String patientID, codHC, token;
    TextView title;
    Button saveBtn;

    EditText anamnesisField, physicalField, diagnosisField, treatmentField, justificationField;

    String anamnesisValue, physicalValue, diagnosisValue, treatmentValue, justificationValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_form);

        Intent intent = getIntent();
        patientID = intent.getStringExtra("patientID");
        codHC = intent.getStringExtra("patientHistory");
        token = intent.getStringExtra("token");

        title = (TextView) findViewById(R.id.title);
        title.setText("Crear consulta médica");

        saveBtn = (Button) findViewById(R.id.consultation_save);
        saveBtn.setText("Crear consulta médica");

        anamnesisField = (EditText) findViewById(R.id.anamnesis_field);
        physicalField = (EditText) findViewById(R.id.physical_field);
        diagnosisField = (EditText) findViewById(R.id.diagnosis_field);
        treatmentField = (EditText) findViewById(R.id.treatment_field);
        justificationField = (EditText) findViewById(R.id.justification_field);
    }

    public void consultationSave(View view){
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/consultas";

        Log.i(LOG_TAG, "URL => " + url + path);

        List<NameValuePair> newConsultation = new ArrayList<NameValuePair>(2);
        anamnesisValue = anamnesisField.getText().toString();
        physicalValue = physicalField.getText().toString();
        diagnosisValue = diagnosisField.getText().toString();
        treatmentValue = treatmentField.getText().toString();
        justificationValue = justificationField.getText().toString();

        newConsultation.add(new BasicNameValuePair("anamnesis", anamnesisValue));
        newConsultation.add(new BasicNameValuePair("physical_exam", physicalValue));
        newConsultation.add(new BasicNameValuePair("diagnosis", diagnosisValue));
        newConsultation.add(new BasicNameValuePair("treatment", treatmentValue));
        newConsultation.add(new BasicNameValuePair("justification", justificationValue));

        try{
            PostAsyncTask pat = new PostAsyncTask(url, path, token);
            pat.execute(newConsultation);
            String response = pat.get();
            Log.i(LOG_TAG, "Server response: consultation post " +response);
            HashMapTransformation hmt = new HashMapTransformation(null);

            // This contains form validation json array :O
            //JSONArray responseArray = hmt.getJsonFromString(response);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
