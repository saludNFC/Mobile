package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        /**
         * required fields set with no errors
         */
        anamnesisField.setError(null);
        physicalField.setError(null);
        diagnosisField.setError(null);

        anamnesisValue = anamnesisField.getText().toString();
        physicalValue = physicalField.getText().toString();
        diagnosisValue = diagnosisField.getText().toString();
        if(diagnosisField.getText().toString() != ""){
            treatmentValue = treatmentField.getText().toString();
        }
        if(justificationField.getText().toString() != ""){
            justificationValue = justificationField.getText().toString();
        }

        List<NameValuePair> newConsultation = new ArrayList<NameValuePair>(2);
        newConsultation.add(new BasicNameValuePair("anamnesis", anamnesisValue));
        newConsultation.add(new BasicNameValuePair("physical_exam", physicalValue));
        newConsultation.add(new BasicNameValuePair("diagnosis", diagnosisValue));
        newConsultation.add(new BasicNameValuePair("treatment", treatmentValue));
        newConsultation.add(new BasicNameValuePair("justification", justificationValue));

        PostAsyncTask pat = new PostAsyncTask(url, path, token);
        try{
            pat.execute(newConsultation);
            String response = pat.get();
            evaluateServerResponse(response);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void evaluateServerResponse(String response) {
        ObjectTransformation hmt = new ObjectTransformation();

        try{
            JSONArray responseArray = hmt.getJsonFromString(response);
            for(int i = 0; i < responseArray.length(); i ++){
                Log.i(LOG_TAG, "RESPONSE ARRAY AT POSITION " + responseArray.getString(i));
                if(responseArray.getString(i).contains("message")){
                    savedSuccess();
                    Log.i(LOG_TAG, "SUCCESS");
                }
                else{
                    savedFailed(responseArray);
                    Log.i(LOG_TAG, "FAILED");
                }
            }
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    private void savedSuccess() {
        Toast.makeText(this, "Consulta médica creada correctamente", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ConsultationFormCreateActivity.this, ConsultationsActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("patientHistory", codHC);
        startActivity(intent);
    }

    private void savedFailed(JSONArray errorResponse) throws JSONException {
        for(int i = 0; i < errorResponse.length(); i++){
            int length = errorResponse.getString(i).length();
            String error = errorResponse.getString(i).substring(1, length - 1);
            if(errorResponse.getString(i).contains("anamnesis")){
                anamnesisField.setError(error);
            }
            if(errorResponse.getString(i).contains("diagnosis")){
                diagnosisField.setError(error);
            }
            if(errorResponse.getString(i).contains("physical")){
                physicalField.setError(error);
            }
        }
    }
}
