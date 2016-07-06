package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.models.Patient;
import app.example.veuge.com.saludnfc.network.PostAsyncTask;

public class ConsultationCreate extends AppCompatActivity {

    private Patient currentPatient;
    private String patientHCode;
    private String token;

    private TextInputLayout createdAtField;
    private TextInputLayout anamnesisField;
    private TextInputLayout physicalField;
    private TextInputLayout diagnosisField;
    private TextInputLayout treatmentField;
    private TextInputLayout justificationField;
    private Button saveConsultationBtn;
    private Toolbar toolbar;

    List<NameValuePair> newConsultation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_consultation);

        Intent intent = getIntent();
        patientHCode = intent.getStringExtra("PATIENT_CODE");
        currentPatient = (Patient) intent.getSerializableExtra("PATIENT");
        token = ((Variables) this.getApplication()).getToken();
        newConsultation = new ArrayList<>();

        setupUI();
    }

    public void setupUI(){
        createdAtField = (TextInputLayout) findViewById(R.id.createdat_wrapper);
        anamnesisField = (TextInputLayout) findViewById(R.id.anamnesis_wrapper);
        physicalField = (TextInputLayout) findViewById(R.id.physical_wrapper);
        diagnosisField = (TextInputLayout) findViewById(R.id.diagnosis_wrapper);
        treatmentField = (TextInputLayout) findViewById(R.id.treatment_wrapper);
        justificationField = (TextInputLayout) findViewById(R.id.justification_wrapper);

        saveConsultationBtn = (Button) findViewById(R.id.consultation_save);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(currentPatient.nombre + " " + currentPatient.apellido);
    }

    public void consultationSave(View view){
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + patientHCode + "/consultas";

        getUserInput();

        try{
            PostAsyncTask pat = new PostAsyncTask(url, path, token);
            pat.execute(newConsultation);
            String response = pat.get();
            evaluateResponse(response);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void getUserInput(){
        newConsultation.clear();

        createdAtField.setErrorEnabled(false);
        anamnesisField.setErrorEnabled(false);
        physicalField.setErrorEnabled(false);
        diagnosisField.setErrorEnabled(false);
        treatmentField.setErrorEnabled(false);
        justificationField.setErrorEnabled(false);

        String createdAtValue = createdAtField.getEditText().getText().toString();
        String anamnesisValue = anamnesisField.getEditText().getText().toString();
        String physicalValue = physicalField.getEditText().getText().toString();
        String diagnosisValue = diagnosisField.getEditText().getText().toString();
        String treatmentValue = treatmentField.getEditText().getText().toString();
        String justificationValue = justificationField.getEditText().getText().toString();

        newConsultation.add(new BasicNameValuePair("created_at", createdAtValue));
        newConsultation.add(new BasicNameValuePair("anamnesis", anamnesisValue));
        newConsultation.add(new BasicNameValuePair("physical_exam", physicalValue));
        newConsultation.add(new BasicNameValuePair("diagnosis", diagnosisValue));
        newConsultation.add(new BasicNameValuePair("treatment", treatmentValue));
        newConsultation.add(new BasicNameValuePair("justification", justificationValue));
    }

    private void evaluateResponse(String response) {
        ObjectTransformation hmt = new ObjectTransformation();

        try{
            JSONArray responseArray = hmt.getJsonFromString(response);

            for(int i = 0; i < responseArray.length(); i ++){
                if(responseArray.getString(i).contains("success_message")){
                    savedSuccess();
                }
                else{
                    savedFailed(responseArray);
                }
            }
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    private void savedSuccess() {
        Toast.makeText(this, "Consulta médica creada correctamente", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ConsultationCreate.this, ConsultationsList.class);
        intent.putExtra("PATIENT_CODE", patientHCode);
        startActivity(intent);
    }

    private void savedFailed(JSONArray errorResponse) throws JSONException {
        for(int i = 0; i < errorResponse.length(); i++){
            int length = errorResponse.getString(i).length();
            String error = errorResponse.getString(i).substring(1, length - 1);
            Log.d("ERROR", error);

            if(error.contains("created at")){
                createdAtField.setError(error);
                createdAtField.requestFocus();
                break;
            }
            if(error.contains("anamnesis")){
                anamnesisField.setError(error);
                anamnesisField.requestFocus();
                break;
            }
            if(error.contains("physical")){
                physicalField.setError(error);
                physicalField.requestFocus();
                break;
            }
            if(error.contains("diagnosis")){
                diagnosisField.setError(error);
                diagnosisField.requestFocus();
                break;
            }
            if(error.contains("treatment")){
                treatmentField.setError(error);
                treatmentField.requestFocus();
                break;
            }
            if(error.contains("justification")){
                justificationField.setError(error);
                justificationField.requestFocus();
                break;
            }
        }
    }
}
