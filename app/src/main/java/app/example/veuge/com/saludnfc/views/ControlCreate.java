package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

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

public class ControlCreate extends AppCompatActivity {

    private Patient currentPatient;
    private String patientHCode;
    private String token;

    private LinearLayout vaccinationForm;
    private LinearLayout growthForm;
    private LinearLayout triageForm;
    private LinearLayout ginecologicForm;
    private LinearLayout geriatricForm;

    private TextInputLayout createdAt;
    private MaterialBetterSpinner controlType;

    private TextInputLayout vaccineField;
    private MaterialBetterSpinner viaField;
    private TextInputLayout dosisField;

    private TextInputLayout weightField;
    private TextInputLayout heightField;

    private TextInputLayout temperatureField;
    private TextInputLayout heartRateField;
    private TextInputLayout systoleField;
    private TextInputLayout diastoleField;

    private TextInputLayout lastMenstField;
    private TextInputLayout lastMamoField;
    private TextInputLayout lastPapaField;

    private MaterialBetterSpinner geriatricTypeField;
    private TextInputLayout notesField;

    private Button saveControlBtn;

    private Toolbar toolbar;

    List<NameValuePair> newControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_control);

        Intent intent = getIntent();
        currentPatient = (Patient) intent.getSerializableExtra("PATIENT");
        patientHCode = intent.getStringExtra("PATIENT_CODE");
        token = ((Variables) this.getApplication()).getToken();
        newControl = new ArrayList<NameValuePair>(2);

        setupUI();
        spinnerAdapters();
    }

    public void setupUI(){
        vaccinationForm = (LinearLayout) findViewById(R.id.vaccine_form);
        vaccinationForm.setVisibility(View.GONE);

        growthForm = (LinearLayout) findViewById(R.id.growth_form);
        growthForm.setVisibility(View.GONE);

        triageForm = (LinearLayout) findViewById(R.id.triage_form);
        triageForm.setVisibility(View.GONE);

        ginecologicForm = (LinearLayout) findViewById(R.id.ginecologic_form);
        ginecologicForm.setVisibility(View.GONE);

        geriatricForm = (LinearLayout) findViewById(R.id.geriatric_form);
        geriatricForm.setVisibility(View.GONE);

        createdAt = (TextInputLayout) findViewById(R.id.createdat_wrapper);
        controlType = (MaterialBetterSpinner) findViewById(R.id.control_type_field);
        addListenerOnSpinnerItemSelection();

        vaccineField = (TextInputLayout) findViewById(R.id.vaccine_wrapper);
        viaField = (MaterialBetterSpinner) findViewById(R.id.viav_type_field);
        dosisField = (TextInputLayout) findViewById(R.id.dosis_wrapper);

        weightField = (TextInputLayout) findViewById(R.id.weight_wrapper);
        heightField = (TextInputLayout) findViewById(R.id.height_wrapper);

        temperatureField = (TextInputLayout) findViewById(R.id.temperature_wrapper);
        heartRateField = (TextInputLayout) findViewById(R.id.heartrate_wrapper);
        systoleField = (TextInputLayout) findViewById(R.id.systole_wrapper);
        diastoleField = (TextInputLayout) findViewById(R.id.diastole_wrapper);

        lastMenstField = (TextInputLayout) findViewById(R.id.lastmenst_wrapper);
        lastMamoField = (TextInputLayout) findViewById(R.id.lastmamo_wrapper);
        lastPapaField = (TextInputLayout) findViewById(R.id.lastpapa_wrapper);

        geriatricTypeField = (MaterialBetterSpinner) findViewById(R.id.geriatric_type_field);
        notesField = (TextInputLayout) findViewById(R.id.notes_wrapper);

        saveControlBtn = (Button) findViewById(R.id.control_save);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(currentPatient.nombre + " " + currentPatient.apellido);
    }

    public void spinnerAdapters(){
        String[] control = this.getResources().getStringArray(R.array.control_options);
        String[] via = this.getResources().getStringArray(R.array.viav_field_options);
        String[] geriatric = this.getResources().getStringArray(R.array.geritype_options);

        ArrayAdapter<String> controlAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, control);
        ArrayAdapter<String> viaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, via);
        ArrayAdapter<String> geriatricAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, geriatric);

        controlType.setAdapter(controlAdapter);
        viaField.setAdapter(viaAdapter);
        geriatricTypeField.setAdapter(geriatricAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        controlType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (s + ""){
                    case "Control de Vacunacion":
                        vaccinationForm.setVisibility(View.VISIBLE);
                        growthForm.setVisibility(View.GONE);
                        triageForm.setVisibility(View.GONE);
                        ginecologicForm.setVisibility(View.GONE);
                        geriatricForm.setVisibility(View.GONE);
                        break;
                    case "Control de Crecimiento":
                        vaccinationForm.setVisibility(View.GONE);
                        growthForm.setVisibility(View.VISIBLE);
                        triageForm.setVisibility(View.GONE);
                        ginecologicForm.setVisibility(View.GONE);
                        geriatricForm.setVisibility(View.GONE);
                        break;
                    case "Control de Triaje":
                        vaccinationForm.setVisibility(View.GONE);
                        growthForm.setVisibility(View.GONE);
                        triageForm.setVisibility(View.VISIBLE);
                        ginecologicForm.setVisibility(View.GONE);
                        geriatricForm.setVisibility(View.GONE);
                        break;
                    case "Control Ginecologico":
                        vaccinationForm.setVisibility(View.GONE);
                        growthForm.setVisibility(View.GONE);
                        triageForm.setVisibility(View.GONE);
                        ginecologicForm.setVisibility(View.VISIBLE);
                        geriatricForm.setVisibility(View.GONE);
                        break;
                    case "Control Geriatrico":
                        vaccinationForm.setVisibility(View.GONE);
                        growthForm.setVisibility(View.GONE);
                        triageForm.setVisibility(View.GONE);
                        ginecologicForm.setVisibility(View.GONE);
                        geriatricForm.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void controlSave(View view){
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + patientHCode + "/controles";

        String type = controlType.getText().toString();
        getUserInput(type);

        try{
            PostAsyncTask pat = new PostAsyncTask(url, path, token);
            pat.execute(newControl);
            String response = pat.get();
            evaluateResponse(response);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void getUserInput(String type) {
        newControl.clear();
        createdAt.setErrorEnabled(false);
        controlType.setError(null);

        String createdAtValue = createdAt.getEditText().getText().toString();
        if(type.length() > 0){
            if(type.contains("de")){
                type = type.substring(11);
            }
            else {
                type = type.substring(8);
            }
        }

        newControl.add(new BasicNameValuePair("created_at", createdAtValue));
        newControl.add(new BasicNameValuePair("control_type", type));

        switch (type) {
            case "Vacunacion":
                vaccineField.setErrorEnabled(false);
                viaField.setError(null);
                dosisField.setErrorEnabled(false);

                String vaccineValue = vaccineField.getEditText().getText().toString();
                String viaValue = viaField.getText().toString();
                String dosisValue = dosisField.getEditText().getText().toString();

                newControl.add(new BasicNameValuePair("vaccine", vaccineValue));
                newControl.add(new BasicNameValuePair("via", viaValue));
                newControl.add(new BasicNameValuePair("dosis", dosisValue));

                break;
            case "Crecimiento":
                weightField.setErrorEnabled(false);
                heightField.setErrorEnabled(false);

                String weightValue = weightField.getEditText().getText().toString();
                String heightValue = heightField.getEditText().getText().toString();

                newControl.add(new BasicNameValuePair("weight", weightValue));
                newControl.add(new BasicNameValuePair("height", heightValue));
                break;
            case "Triaje":
                temperatureField.setErrorEnabled(false);
                heartRateField.setErrorEnabled(false);
                systoleField.setErrorEnabled(false);
                diastoleField.setErrorEnabled(false);

                String temperatureValue = temperatureField.getEditText().getText().toString();
                String heartRateValue = heartRateField.getEditText().getText().toString();
                String systoleValue = systoleField.getEditText().getText().toString();
                String diastoleValue = diastoleField.getEditText().getText().toString();

                newControl.add(new BasicNameValuePair("temperature", temperatureValue));
                newControl.add(new BasicNameValuePair("heart_rate", heartRateValue));
                newControl.add(new BasicNameValuePair("sistole", systoleValue));
                newControl.add(new BasicNameValuePair("diastole", diastoleValue));
                break;
            case "Ginecologico":
                lastMenstField.setErrorEnabled(false);
                lastMamoField.setErrorEnabled(false);
                lastPapaField.setErrorEnabled(false);

                String lastMenstValue = lastMenstField.getEditText().getText().toString();
                String lastMamoValue = lastMamoField.getEditText().getText().toString();
                String lastPapaValue = lastPapaField.getEditText().getText().toString();

                newControl.add(new BasicNameValuePair("last_menst", lastMenstValue));
                newControl.add(new BasicNameValuePair("last_mamo", lastMamoValue));
                newControl.add(new BasicNameValuePair("last_papa", lastPapaValue));
                break;
            case "Geriatrico":
                geriatricTypeField.setError(null);
                notesField.setErrorEnabled(false);

                String geriatricTypeValue = geriatricTypeField.getText().toString();
                String notesValue = notesField.getEditText().getText().toString();

                newControl.add(new BasicNameValuePair("geriatric_type", geriatricTypeValue));
                newControl.add(new BasicNameValuePair("notes", notesValue));
                break;
        }
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
        Toast.makeText(this, "Control creado correctamente", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ControlCreate.this, ControlsList.class);
        intent.putExtra("PATIENT_CODE", patientHCode);
        intent.putExtra("PATIENT", currentPatient);
        startActivity(intent);
    }

    private void savedFailed(JSONArray errorResponse) throws JSONException {
        for(int i = 0; i < errorResponse.length(); i++){
            int length = errorResponse.getString(i).length();
            String error = errorResponse.getString(i).substring(1, length - 1);

            if(error.contains("created at")){
                createdAt.setError(error);
                createdAt.requestFocus();
                break;
            }
            if(error.startsWith("control type", 1)){
                controlType.setError(error);
                controlType.requestFocus();
                break;
            }

            if(error.startsWith("vaccine", 1)){
                vaccineField.setError(error);
                vaccineField.requestFocus();
                break;
            }
            if(error.startsWith("via", 1)){
                viaField.setError(error);
                viaField.requestFocus();
                break;
            }
            if(error.startsWith("dosis", 1)){
                dosisField.setError(error);
                dosisField.requestFocus();
                break;
            }

            if(error.startsWith("weight", 1)){
                weightField.setError(error);
                weightField.requestFocus();
                break;
            }
            if(error.startsWith("height", 1)){
                heightField.setError(error);
                heightField.requestFocus();
                break;
            }

            if(error.startsWith("temperature", 1)){
                temperatureField.setError(error);
                temperatureField.requestFocus();
                break;
            }
            if(error.startsWith("heart rate", 1)){
                heartRateField.setError(error);
                heartRateField.requestFocus();
                break;
            }
            if(error.startsWith("sistole", 1)){
                systoleField.setError(error);
                systoleField.requestFocus();
                break;
            }
            if(error.startsWith("diastole", 1)){
                diastoleField.setError(error);
                diastoleField.requestFocus();
                break;
            }

            if(error.startsWith("last menst", 1)){
                lastMenstField.setError(error);
                lastMenstField.requestFocus();
                break;
            }
            if(error.startsWith("last mamo", 1)){
                lastMamoField.setError(error);
                lastMamoField.requestFocus();
                break;
            }

            if(error.startsWith("geriatric type", 1)){
                geriatricTypeField.setError(error);
                geriatricTypeField.requestFocus();
                break;
            }
            if(error.startsWith("notes", 1)){
                notesField.setError(error);
                notesField.requestFocus();
                break;
            }
        }
    }
}
