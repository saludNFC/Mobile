package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HistoryFormCreateActivity extends AppCompatActivity {

    TextView title;
    Spinner historyType;
    LinearLayout familiarForm, personalForm, medForm;
    Button saveButton;

    /**
     * Form fields!
     */
    EditText gradeField, illnessField, descriptionField, medField, dateiniField, createdField;
    Spinner personalTypeField, viaTypeField;

    /**
     * Input values
     */
    String historyTypeValue, gradeValue, illnessValue, personalTypeValue, descriptionValue, medValue, dateiniValue,
            viaTypeValue, createdValue;

    public static String patientID, codHC, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_form);

        Intent intent = getIntent();
        patientID = intent.getStringExtra("patientID");
        codHC = intent.getStringExtra("patientHistory");
        token = intent.getStringExtra("token");

        title = (TextView) findViewById(R.id.title);
        title.setText("Crear antecedente");
        saveButton = (Button) findViewById(R.id.history_save);
        saveButton.setText("Crear antecedente");

        /**
         * This gets the parents layouts and hide them until they are selected ;)
         */
        familiarForm = (LinearLayout) findViewById(R.id.familiar_form);
        familiarForm.setVisibility(View.GONE);
        personalForm = (LinearLayout) findViewById(R.id.personal_form);
        personalForm.setVisibility(View.GONE);
        medForm = (LinearLayout) findViewById(R.id.medicine_form);
        medForm.setVisibility(View.GONE);

        createdField = (EditText) findViewById(R.id.created_field);

        gradeField = (EditText) findViewById(R.id.grade_field);
        illnessField = (EditText) findViewById(R.id.illness_field);

        personalTypeField = (Spinner) findViewById(R.id.personal_type_field);
        descriptionField = (EditText) findViewById(R.id.description_field);

        medField = (EditText) findViewById(R.id.medicine_field);
        viaTypeField = (Spinner) findViewById(R.id.via_type_field);
        dateiniField = (EditText) findViewById(R.id.dateini_field);

        historyType = (Spinner) findViewById(R.id.history_type);
        addListenerOnSpinnerItemSelection();
    }

    private void addListenerOnSpinnerItemSelection() {
        CustomListener cl = new CustomListener(familiarForm, personalForm, medForm);
        historyType.setOnItemSelectedListener(cl);
    }

    public void historySave(View view){
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/antecedentes";
        List<NameValuePair> newHistory = new ArrayList<NameValuePair>(2);

        createdValue = createdField.getText().toString();

        if(familiarForm.getVisibility() == View.VISIBLE) {
            historyTypeValue = "Familiar";
            gradeValue = gradeField.getText().toString();
            illnessValue = illnessField.getText().toString();

            newHistory.add(new BasicNameValuePair("grade", gradeValue));
            newHistory.add(new BasicNameValuePair("illness", illnessValue));
        }
        if(personalForm.getVisibility() == View.VISIBLE){
            historyTypeValue = "Personal";
            personalTypeValue = personalTypeField.getSelectedItem().toString();
            descriptionValue = descriptionField.getText().toString();

            newHistory.add(new BasicNameValuePair("type_personal", personalTypeValue));
            newHistory.add(new BasicNameValuePair("description", descriptionValue));
        }
        if(medForm.getVisibility() == View.VISIBLE){
            historyTypeValue = "Medicamentos";
            medValue = medField.getText().toString();
            viaTypeValue = viaTypeField.getSelectedItem().toString();
            dateiniValue = dateiniField.getText().toString();

            newHistory.add(new BasicNameValuePair("med", medValue));
            newHistory.add(new BasicNameValuePair("via", viaTypeValue));
            newHistory.add(new BasicNameValuePair("date_ini", dateiniValue));
        }
        newHistory.add(new BasicNameValuePair("history_type", historyTypeValue));
        newHistory.add(new BasicNameValuePair("created_at", createdValue));

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

    private void evaluateResponse(String response) {
        ObjectTransformation hmt = new ObjectTransformation();

        try{
            JSONArray responseArray = hmt.getJsonFromString(response);
            for(int i = 0; i < responseArray.length(); i ++){
                if(responseArray.getString(i).contains("message")){
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

        Intent intent = new Intent(HistoryFormCreateActivity.this, HistoriesActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("patientHistory", codHC);
        startActivity(intent);
    }

    private void savedFailed(JSONArray errorResponse) throws JSONException {
        for(int i = 0; i < errorResponse.length(); i++){
            int length = errorResponse.getString(i).length();
            String error = errorResponse.getString(i).substring(1, length - 1);
            if(errorResponse.getString(i).contains("grade")){
                gradeField.setError(error);
                gradeField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("illness")){
                illnessField.setError(error);
                illnessField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("description")){
                descriptionField.setError(error);
                descriptionField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("med")){
                medField.setError(error);
                medField.requestFocus();
                break;
            }
            if(errorResponse.getString(i).contains("date_ini")){
                dateiniField.setError(error);
                dateiniField.requestFocus();
                break;
            }
        }
    }
}
