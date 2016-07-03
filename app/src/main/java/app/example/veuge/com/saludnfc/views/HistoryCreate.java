package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import app.example.veuge.com.saludnfc.network.PostAsyncTask;

public class HistoryCreate extends AppCompatActivity {

    private String patientHCode;
    private String token;

    private LinearLayout familiarForm;
    private LinearLayout personalForm;
    private LinearLayout medicineForm;

    private TextInputLayout createdAt;
    private MaterialBetterSpinner historyType;

    private TextInputLayout gradeField;
    private TextInputLayout illnessField;

    private MaterialBetterSpinner personalTypeField;
    private TextInputLayout descriptionField;

    private TextInputLayout medicineField;
    private MaterialBetterSpinner viaField;
    private TextInputLayout dateIniField;

    private Button saveHistoryBtn;

    List<NameValuePair> newHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_history);

        Intent intent = getIntent();
        patientHCode = intent.getStringExtra("PATIENT_CODE");
        token = ((Variables) this.getApplication()).getToken();
        newHistory = new ArrayList<NameValuePair>(2);

        setupUI();
        spinnerAdapters();
    }

    public void setupUI(){
        familiarForm = (LinearLayout) findViewById(R.id.familiar_form);
        familiarForm.setVisibility(View.GONE);

        personalForm = (LinearLayout) findViewById(R.id.personal_form);
        personalForm.setVisibility(View.GONE);

        medicineForm = (LinearLayout) findViewById(R.id.medicine_form);
        medicineForm.setVisibility(View.GONE);

        createdAt = (TextInputLayout) findViewById(R.id.createdat_wrapper);
        historyType = (MaterialBetterSpinner) findViewById(R.id.history_type_field);
        addListenerOnSpinnerItemSelection();

        gradeField = (TextInputLayout) findViewById(R.id.grade_wrapper);
        illnessField = (TextInputLayout) findViewById(R.id.illness_wrapper);

        personalTypeField = (MaterialBetterSpinner) findViewById(R.id.personal_type_field);
        descriptionField = (TextInputLayout) findViewById(R.id.description_wrapper);

        medicineField = (TextInputLayout) findViewById(R.id.medicine_wrapper);
        viaField = (MaterialBetterSpinner) findViewById(R.id.via_type_field);
        dateIniField = (TextInputLayout) findViewById(R.id.dateini_wrapper);

        saveHistoryBtn = (Button) findViewById(R.id.history_save);
    }

    public void spinnerAdapters(){
        String[] history = this.getResources().getStringArray(R.array.history_options);
        String[] personal = this.getResources().getStringArray(R.array.personal_field_options);
        String[] via = this.getResources().getStringArray(R.array.via_field_options);

        ArrayAdapter<String> historyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, history);
        ArrayAdapter<String> personalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, personal);
        ArrayAdapter<String> viaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, via);

        historyType.setAdapter(historyAdapter);
        personalTypeField.setAdapter(personalAdapter);
        viaField.setAdapter(viaAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        /**
         * MaterialBetterSpinner extends AutoCompleteTextView to get the selected value
         * instead of implementing setOnItemSelectedListener() it should implement the
         * addTextChangedListener()
         * whatever xD
         */

        historyType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String a = "Antecedente Familiar";
                switch (s + ""){
                    case "Antecedente Familiar":
                        familiarForm.setVisibility(View.VISIBLE);
                        personalForm.setVisibility(View.GONE);
                        medicineForm.setVisibility(View.GONE);
                        break;
                    case "Antecedente Personal":
                        familiarForm.setVisibility(View.GONE);
                        personalForm.setVisibility(View.VISIBLE);
                        medicineForm.setVisibility(View.GONE);
                        break;
                    case "Antecedente Medicamentos":
                        familiarForm.setVisibility(View.GONE);
                        personalForm.setVisibility(View.GONE);
                        medicineForm.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void historySave(View view){
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + patientHCode + "/antecedentes";

        String type = historyType.getText().toString();
        getUserInput(type);

        try{
            PostAsyncTask pat = new PostAsyncTask(url, path, token);
            pat.execute(newHistory);
            String response = pat.get();
            evaluateResponse(response);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void getUserInput(String type){
        newHistory.clear();
        createdAt.setErrorEnabled(false);
        historyType.setError(null);

        String createdAtValue = createdAt.getEditText().getText().toString();
        if(type.length() > 0){
            type = type.substring(12);
        }

        newHistory.add(new BasicNameValuePair("created_at", createdAtValue));
        newHistory.add(new BasicNameValuePair("history_type", type));

        switch (type){
            case "Familiar":
                gradeField.setErrorEnabled(false);
                illnessField.setErrorEnabled(false);

                String gradeValue = gradeField.getEditText().getText().toString();
                String illnessValue = illnessField.getEditText().getText().toString();

                newHistory.add(new BasicNameValuePair("grade", gradeValue));
                newHistory.add(new BasicNameValuePair("illness", illnessValue));

                break;
            case "Personal":
                personalTypeField.setError(null);
                descriptionField.setErrorEnabled(false);

                String personalTypeValue = personalTypeField.getText().toString();
                String descriptionValue = descriptionField.getEditText().getText().toString();

                newHistory.add(new BasicNameValuePair("type_personal", personalTypeValue));
                newHistory.add(new BasicNameValuePair("description", descriptionValue));
                break;
            case "Medicamentos":
                medicineField.setErrorEnabled(false);
                viaField.setError(null);
                dateIniField.setErrorEnabled(false);

                String medicineValue = medicineField.getEditText().getText().toString();
                String viaValue = viaField.getText().toString();
                String dateIniValue = dateIniField.getEditText().getText().toString();

                newHistory.add(new BasicNameValuePair("med", medicineValue));
                newHistory.add(new BasicNameValuePair("via", viaValue));
                newHistory.add(new BasicNameValuePair("date_ini", dateIniValue));
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
        Toast.makeText(this, "Antecedente médico creado correctamente", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(HistoryCreate.this, HistoriesList.class);
        intent.putExtra("PATIENT_CODE", patientHCode);
        startActivity(intent);
    }

    private void savedFailed(JSONArray errorResponse) throws JSONException {
        for(int i = 0; i < errorResponse.length(); i++){
            int length = errorResponse.getString(i).length();
            String error = errorResponse.getString(i).substring(1, length - 1);
            Log.d("error =>", error);

            if(error.contains("created at")){
                createdAt.setError(error);
                createdAt.requestFocus();
                break;
            }
            if(error.startsWith("history type", 1)){
                historyType.setError(error);
                historyType.requestFocus();
                break;
            }
            if(error.startsWith("grade", 1)){
                gradeField.setError(error);
                gradeField.requestFocus();
                break;
            }
            if(error.startsWith("illness", 1)){
                illnessField.setError(error);
                illnessField.requestFocus();
                break;
            }
            if(error.startsWith("type personal", 1)){
                personalTypeField.setError(error);
                personalTypeField.requestFocus();
                break;
            }
            if(error.startsWith("description", 1)){
                descriptionField.setError(error);
                descriptionField.requestFocus();
                break;
            }
            if(error.startsWith("med", 1)){
                medicineField.setError(error);
                medicineField.requestFocus();
                break;
            }
            if(error.startsWith("via", 1)){
                viaField.setError(error);
                viaField.requestFocus();
                break;
            }
            if(error.startsWith("date ini", 1)){
                dateIniField.setError(error);
                dateIniField.requestFocus();
                break;
            }
        }
    }
}
