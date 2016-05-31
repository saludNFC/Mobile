package app.example.veuge.com.saludnfc;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PatientFormCreateActivity extends AppCompatActivity {

    private String ci_value, ciemision_value, name_value, lastname_value, gender_value, birthday_value,
        birthplace_value, instruction_value, civilstatus_value, ocupation_value, bloodtype_value;

    private EditText ci, name, lastname, birthday, instruction, civilstatus, ocupation;
    private Spinner emision, gender, birthplace, bloodtype;
    private Button saveButton;

    private String params = "";

    private final String LOG_TAG = PatientFormCreateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);

        saveButton = (Button) findViewById(R.id.patient_save);
        saveButton.setText("Crear Paciente");

        ci = (EditText) findViewById(R.id.ci_field);
        emision = (Spinner) findViewById(R.id.ciemision_field);
        name = (EditText) findViewById(R.id.name_field);
        lastname = (EditText) findViewById(R.id.lastname_field);
        gender = (Spinner) findViewById(R.id.gender_field);
        birthday = (EditText) findViewById(R.id.birthday_field);
        birthplace = (Spinner) findViewById(R.id.birthplace_field);
        instruction = (EditText) findViewById(R.id.instruction_field);
        civilstatus = (EditText) findViewById(R.id.civilstatus_field);
        ocupation = (EditText) findViewById(R.id.ocupation_field);
        bloodtype = (Spinner) findViewById(R.id.bloodtype_field);
    }

    public void patientSave(View view) {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente";

        ci_value = ci.getText().toString();
        ciemision_value = emision.getSelectedItem().toString();
        name_value = name.getText().toString();
        lastname_value = lastname.getText().toString();
        gender_value = gender.getSelectedItem().toString();
        birthday_value = birthday.getText().toString();
        birthplace_value = birthplace.getSelectedItem().toString();
        instruction_value = instruction.getText().toString();
        civilstatus_value = civilstatus.getText().toString();
        ocupation_value = ocupation.getText().toString();
        bloodtype_value = bloodtype.getSelectedItem().toString();

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("ci", ci_value));
        nameValuePair.add(new BasicNameValuePair("emision", ciemision_value));
        nameValuePair.add(new BasicNameValuePair("nombre", name_value));
        nameValuePair.add(new BasicNameValuePair("apellido", lastname_value));
        nameValuePair.add(new BasicNameValuePair("sexo", gender_value));
        nameValuePair.add(new BasicNameValuePair("fecha_nac", birthday_value));
        nameValuePair.add(new BasicNameValuePair("lugar_nac", birthplace_value));
        nameValuePair.add(new BasicNameValuePair("grado_instruccion", instruction_value));
        nameValuePair.add(new BasicNameValuePair("estado_civil", civilstatus_value));
        nameValuePair.add(new BasicNameValuePair("ocupacion", ocupation_value));
        nameValuePair.add(new BasicNameValuePair("grupo_sanguineo", bloodtype_value));

        try{
            PostAsyncTask pat = new PostAsyncTask(url, path);
            pat.execute(nameValuePair);
            String response = pat.get();
            HashMapTransformation hmt = new HashMapTransformation(null);

            // This contains form validation json array :O
            JSONArray responseArray = hmt.getJsonFromString(response);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
