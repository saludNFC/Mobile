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

import java.util.ArrayList;
import java.util.List;

public class HistoryFormCreateActivity extends AppCompatActivity {

    private final String LOG_TAG = HistoryFormCreateActivity.class.getSimpleName();

    TextView title;
    Spinner historyType;
    LinearLayout familiarForm, personalForm, medForm;
    Button saveButton;

    /**
     * Form fields!
     */
    EditText gradeField, illnessField, descriptionField, medField, dateiniField;
    Spinner personalTypeField, viaTypeField;

    /**
     * Input values
     */
    String historyTypeValue, gradeValue, illnessValue, personalTypeValue, descriptionValue, medValue, dateiniValue, viaTypeValue;

    public static String patientID;
    public static String codHC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_form);

        Intent intent = getIntent();
        patientID = intent.getStringExtra("patientID");
        codHC = intent.getStringExtra("patientHistory");

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
//        String selected = cl.getSelected();
//        Toast.makeText(this,
//                "SELECTED ITEM: " + selected,
//                Toast.LENGTH_SHORT).show();
    }

    public void historySave(View view){
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/antecedentes";
        Log.i(LOG_TAG, "URL => " + url + path);
        List<NameValuePair> newHistory = new ArrayList<NameValuePair>(2);

        if(familiarForm.getVisibility() == View.VISIBLE) {
            historyTypeValue = "Familiar";
            gradeValue = gradeField.getText().toString();
            illnessValue = illnessField.getText().toString();

            newHistory.add(new BasicNameValuePair("grade", gradeValue));
            newHistory.add(new BasicNameValuePair("illness", illnessValue));

            Log.i(LOG_TAG, "HISTORY TYPE => " + historyTypeValue);
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

        try{
            PostAsyncTask pat = new PostAsyncTask(url, path);
            pat.execute(newHistory);
            String response = pat.get();
            Log.i(LOG_TAG, "Server response: " +response);
            HashMapTransformation hmt = new HashMapTransformation(null);

            // This contains form validation json array :O
            //JSONArray responseArray = hmt.getJsonFromString(response);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
