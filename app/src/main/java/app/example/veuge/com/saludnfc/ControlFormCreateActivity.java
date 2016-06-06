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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ControlFormCreateActivity extends AppCompatActivity {

    private final String LOG_TAG = ControlFormCreateActivity.class.getSimpleName();

    TextView title;
    Spinner controlType;
    LinearLayout vaccineForm, growthForm, triageForm, gineForm, geriForm;
    Button saveButton;

    /**
     * Form fields!
     */
    EditText vaccineField, dosisField, weightField, heightField, temperatureField, heartrateField, sistoleField,
            diastoleField, lastmenstField, lastmamoField, sexactField, lastpapaField, notesField;
    Spinner viaTypeField, geriTypeField;

    /**
     * Input values
     */
    String controlTypeValue, vaccineValue, viaValue, dosisValue, weightValue, heightValue, temperatureValue, heartrateValue,
            sistoleValue, diastoleValue, lastmenstValue, lastmamoValue, sexactValue, lastpapaValue, geriTypeValue, notesValue;

    public static String patientID, codHC, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_form);

        Intent intent = getIntent();
        patientID = intent.getStringExtra("patientID");
        codHC = intent.getStringExtra("patientHistory");
        token = intent.getStringExtra("token");

        title = (TextView) findViewById(R.id.title);
        title.setText("Crear control");
        saveButton = (Button) findViewById(R.id.control_save);
        saveButton.setText("Crear control");

        /**
         * This gets the parents layouts and hide them until they are selected ;)
         */
        vaccineForm = (LinearLayout) findViewById(R.id.vaccine_form);
        vaccineForm.setVisibility(View.GONE);
        growthForm = (LinearLayout) findViewById(R.id.growth_form);
        growthForm.setVisibility(View.GONE);
        triageForm = (LinearLayout) findViewById(R.id.triage_form);
        triageForm.setVisibility(View.GONE);
        gineForm = (LinearLayout) findViewById(R.id.gine_form);
        gineForm.setVisibility(View.GONE);
        geriForm = (LinearLayout) findViewById(R.id.geri_form);
        geriForm.setVisibility(View.GONE);

        vaccineField = (EditText) findViewById(R.id.vaccine_field);
        viaTypeField = (Spinner) findViewById(R.id.viav_field);
        dosisField = (EditText) findViewById(R.id.dosis_field);

        weightField = (EditText) findViewById(R.id.weight_field);
        heightField = (EditText) findViewById(R.id.height_field);

        temperatureField = (EditText) findViewById(R.id.temperature_field);
        heartrateField = (EditText) findViewById(R.id.heartrate_field);
        sistoleField = (EditText) findViewById(R.id.sistole_field);
        diastoleField = (EditText) findViewById(R.id.diastole_field);

        lastmenstField = (EditText) findViewById(R.id.lastmenst_field);
        lastmamoField = (EditText) findViewById(R.id.lastmamo_field);
        //sexactField = (Spinner) findViewById(R.id.se);
        lastpapaField = (EditText) findViewById(R.id.lastpapa_field);

        geriTypeField = (Spinner) findViewById(R.id.geritype_field);
        notesField = (EditText) findViewById(R.id.notes_field);

        controlType = (Spinner) findViewById(R.id.control_type);
        addListenerOnSpinnerItemSelection();
    }

    private void addListenerOnSpinnerItemSelection() {
        CustomListener cl = new CustomListener(vaccineForm, growthForm, triageForm, gineForm, geriForm);
        controlType.setOnItemSelectedListener(cl);
    }

    public void controlSave(View view){
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/controles";
        List<NameValuePair> newControl = new ArrayList<NameValuePair>(2);

        if(vaccineForm.getVisibility() == View.VISIBLE) {
            controlTypeValue = "Vacunacion";
            vaccineValue = vaccineField.getText().toString();
            viaValue = viaTypeField.getSelectedItem().toString();
            dosisValue = dosisField.getText().toString();

            newControl.add(new BasicNameValuePair("vaccine", vaccineValue));
            newControl.add(new BasicNameValuePair("via", viaValue));
            newControl.add(new BasicNameValuePair("dosis", dosisValue));
        }
        if(growthForm.getVisibility() == View.VISIBLE){
            controlTypeValue = "Crecimiento";
            weightValue = weightField.getText().toString();
            heightValue = heightField.getText().toString();

            newControl.add(new BasicNameValuePair("weight", weightValue));
            newControl.add(new BasicNameValuePair("height", heightValue));
        }
        if(triageForm.getVisibility() == View.VISIBLE){
            controlTypeValue = "Triaje";
            temperatureValue = temperatureField.getText().toString();
            heartrateValue = heartrateField.getText().toString();
            sistoleValue = sistoleField.getText().toString();
            diastoleValue = diastoleField.getText().toString();

            newControl.add(new BasicNameValuePair("temperature", temperatureValue));
            newControl.add(new BasicNameValuePair("heart_rate", heartrateValue));
            newControl.add(new BasicNameValuePair("sistole", sistoleValue));
            newControl.add(new BasicNameValuePair("diastole", diastoleValue));
        }
        if(gineForm.getVisibility() == View.VISIBLE){
            controlTypeValue = "Ginecologico";
            lastmenstValue = lastmenstField.getText().toString();
            lastmamoValue = lastmamoField.getText().toString();
            lastpapaValue = lastpapaField.getText().toString();

            newControl.add(new BasicNameValuePair("last_menst", lastmenstValue));
            newControl.add(new BasicNameValuePair("last_mamo", lastmamoValue));
            newControl.add(new BasicNameValuePair("last_papa", lastpapaValue));
        }
        if(geriForm.getVisibility() == View.VISIBLE){
            controlTypeValue = "Geriatrico";
            geriTypeValue = geriTypeField.getSelectedItem().toString();
            notesValue = notesField.getText().toString();

            newControl.add(new BasicNameValuePair("geriatric_type", geriTypeValue));
            newControl.add(new BasicNameValuePair("notes", notesValue));
        }
        newControl.add(new BasicNameValuePair("control_type", controlTypeValue));

        try{
            PostAsyncTask pat = new PostAsyncTask(url, path, token);
            pat.execute(newControl);
            String response = pat.get();
            Log.i(LOG_TAG, "Server response: control post " +response);
            HashMapTransformation hmt = new HashMapTransformation(null);

            // This contains form validation json array :O
            //JSONArray responseArray = hmt.getJsonFromString(response);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
