package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.network.PostAsyncTask;

public class PatientCreate extends AppCompatActivity {

    //  UI elements
    private TextInputLayout ciField;
    private MaterialBetterSpinner emisionField;
    private TextInputLayout nameField;
    private TextInputLayout lastnameField;
    private MaterialBetterSpinner genderField;
    private TextInputLayout birthdayField;
    private MaterialBetterSpinner birthplaceField;
    private TextInputLayout instructionField;
    private TextInputLayout civilField;
    private TextInputLayout occupationField;
    private MaterialBetterSpinner bloodField;
    private Button saveButton;

    private String token;

    private List<NameValuePair> nameValuePair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_patient);
        Intent intent = getIntent();
        token = ((Variables) this.getApplication()).getToken();
        nameValuePair = new ArrayList<NameValuePair>(2);

        setUI();
        spinnerAdapters();
    }

    public void setUI(){
        ciField = (TextInputLayout) findViewById(R.id.ci_wrapper);
        emisionField = (MaterialBetterSpinner) findViewById(R.id.ciemision_field);
        nameField = (TextInputLayout) findViewById(R.id.name_wrapper);
        lastnameField = (TextInputLayout) findViewById(R.id.lastname_wrapper);
        genderField = (MaterialBetterSpinner) findViewById(R.id.gender_field);
        birthdayField = (TextInputLayout) findViewById(R.id.birthday_wrapper);
        birthplaceField = (MaterialBetterSpinner) findViewById(R.id.birthplace_field);
        instructionField = (TextInputLayout) findViewById(R.id.instruction_wrapper);
        civilField = (TextInputLayout) findViewById(R.id.civil_wrapper);
        occupationField = (TextInputLayout) findViewById(R.id.occupation_wrapper);
        bloodField = (MaterialBetterSpinner) findViewById(R.id.bloodtype_field);
        saveButton = (Button) findViewById(R.id.patient_save);
    }

    public void spinnerAdapters(){
        String[] emision = this.getResources().getStringArray(R.array.ci_emision_options);
        String[] gender = this.getResources().getStringArray(R.array.gender_options);
        String[] birthplace = this.getResources().getStringArray(R.array.birthplace_options);
        String[] blood = this.getResources().getStringArray(R.array.bloodtype_options);

        ArrayAdapter<String> emisionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emision);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, gender);
        ArrayAdapter<String> birthplaceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, birthplace);
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, blood);

        emisionField.setAdapter(emisionAdapter);
        genderField.setAdapter(genderAdapter);
        birthplaceField.setAdapter(birthplaceAdapter);
        bloodField.setAdapter(bloodAdapter);
    }

    public void patientSave(View view){
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente";

        ciField.setErrorEnabled(false);
        emisionField.setError(null);
        nameField.setErrorEnabled(false);
        lastnameField.setErrorEnabled(false);
        genderField.setError(null);
        birthdayField.setErrorEnabled(false);
        birthplaceField.setError(null);
        instructionField.setErrorEnabled(false);
        civilField.setErrorEnabled(false);
        occupationField.setErrorEnabled(false);
        bloodField.setError(null);

        getUserInput();

        try{
            PostAsyncTask pat = new PostAsyncTask(url, path, token);
            pat.execute(nameValuePair);
            String response = pat.get();
            evaluateResponse(response);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void getUserInput(){
        String ciValue = ciField.getEditText().getText().toString();
        String emisionValue = emisionField.getText().toString();
        String nameValue = nameField.getEditText().getText().toString();
        String lastnameValue = lastnameField.getEditText().getText().toString();
        String genderValue = genderField.getText().toString();
        String birthdayValue = birthdayField.getEditText().getText().toString();
        String birthplaceValue = birthplaceField.getText().toString();
        String instructionValue = instructionField.getEditText().getText().toString();
        String civilstatusValue = civilField.getEditText().getText().toString();
        String occupationValue = occupationField.getEditText().getText().toString();
        String bloodtypeValue = bloodField.getText().toString();

        nameValuePair.add(new BasicNameValuePair("ci", ciValue));
        nameValuePair.add(new BasicNameValuePair("emision", emisionValue));
        nameValuePair.add(new BasicNameValuePair("nombre", nameValue));
        nameValuePair.add(new BasicNameValuePair("apellido", lastnameValue));
        nameValuePair.add(new BasicNameValuePair("sexo", genderValue));
        nameValuePair.add(new BasicNameValuePair("fecha_nac", birthdayValue));
        nameValuePair.add(new BasicNameValuePair("lugar_nac", birthplaceValue));
        nameValuePair.add(new BasicNameValuePair("grado_instruccion", instructionValue));
        nameValuePair.add(new BasicNameValuePair("estado_civil", civilstatusValue));
        nameValuePair.add(new BasicNameValuePair("ocupacion", occupationValue));
        nameValuePair.add(new BasicNameValuePair("grupo_sanguineo", bloodtypeValue));
    }

    private void evaluateResponse(String response) {
        ObjectTransformation hmt = new ObjectTransformation();

        try{
            JSONArray responseArray = hmt.getJsonFromString(response);
            for(int i = 0; i < responseArray.length(); i ++){
                if(responseArray.getString(i).contains("success")){
                    JSONObject x = responseArray.getJSONObject(i);
                    String newPatient = x.getString("success_message");
                    savedSuccess(newPatient);
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

    private void savedSuccess(String newPatient) {
        Intent intent = new Intent(PatientCreate.this, PatientsList.class);
        intent.putExtra("token", token);
        intent.putExtra("patientID", "");
        intent.putExtra("patientHistory", newPatient);

        Toast.makeText(this, "Paciente creado correctamente", Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }

    private void savedFailed(JSONArray errorResponse) throws JSONException {
        for(int i = 0; i < errorResponse.length(); i++){
            int length = errorResponse.getString(i).length();
            String error = errorResponse.getString(i).substring(1, length - 1);
            if(error.contains(403 + "")){
                Toast.makeText(this, errorResponse.getString(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PatientCreate.this, PatientsList.class);
                startActivity(intent);
                break;
            }
            if(errorResponse.getString(i).contains("ci")){
                ciField.setError(error);
                ciField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("emision")){
                emisionField.setError(error);
                emisionField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("nombre")){
                nameField.setError(error);
                nameField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("apellido")){
                lastnameField.setError(error);
                lastnameField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("fecha nac")){
                birthdayField.setError(error);
                birthdayField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("lugar nac")){
                birthplaceField.setError(error);
                birthplaceField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("grupo sanguineo")){
                bloodField.setError(error);
                bloodField.requestFocus();
                break;
            }
        }
    }
}
