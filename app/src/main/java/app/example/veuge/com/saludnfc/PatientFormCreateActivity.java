package app.example.veuge.com.saludnfc;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private String params = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);
    }

    public void patientSave(View view) {
        EditText ci = (EditText) findViewById(R.id.ci_field);
        ci_value = ci.getText().toString();

        Spinner emision = (Spinner) findViewById(R.id.ciemision_field);
        ciemision_value = emision.getSelectedItem().toString();

        EditText name = (EditText) findViewById(R.id.name_field);
        name_value = name.getText().toString();

        EditText lastname = (EditText) findViewById(R.id.lastname_field);
        lastname_value = lastname.getText().toString();

        Spinner gender = (Spinner) findViewById(R.id.gender_field);
        gender_value = gender.getSelectedItem().toString();

        EditText birthday = (EditText) findViewById(R.id.birthday_field);
        birthday_value = birthday.getText().toString();

        Spinner birthplace = (Spinner) findViewById(R.id.birthplace_field);
        birthplace_value = birthplace.getSelectedItem().toString();

        EditText instruction = (EditText) findViewById(R.id.instruction_field);
        instruction_value = instruction.getText().toString();

        EditText civilstatus = (EditText) findViewById(R.id.civilstatus_field);
        civilstatus_value = civilstatus.getText().toString();

        EditText ocupation = (EditText) findViewById(R.id.ocupation_field);
        ocupation_value = ocupation.getText().toString();

        Spinner bloodtype = (Spinner) findViewById(R.id.bloodtype_field);
        bloodtype_value = bloodtype.getSelectedItem().toString();

        /*params = "ci=" + URLEncoder.encode(ci_value) + "&emision=" + URLEncoder.encode(ciemision_value) + "&nombre=" + URLEncoder.encode(name_value) + "&apellido=";
        params += URLEncoder.encode(lastname_value) + "&sexo=" + URLEncoder.encode(gender_value) + "&fecha_nac=" + URLEncoder.encode(birthday_value) + "&lugar_nac=";
        params += URLEncoder.encode(birthplace_value) + "&grado_instruccion=" + URLEncoder.encode(instruction_value) + "&estado_civil=";
        params += URLEncoder.encode(civilstatus_value) + "&ocupacion=" + URLEncoder.encode(ocupation_value) + "&grupo_sanguineo=" + URLEncoder.encode(bloodtype_value);*/

        params = "ci=" + URLEncoder.encode(ci_value) + "&emision=" + URLEncoder.encode(ciemision_value) + "&nombre=" + URLEncoder.encode(name_value) + "&apellido=";
        params += URLEncoder.encode(lastname_value) + "&fecha_nac=" + URLEncoder.encode(birthday_value) + "&lugar_nac=";
        params += URLEncoder.encode(birthplace_value);
        params += "&grupo_sanguineo=" + URLEncoder.encode(bloodtype_value);

        InsertPatientsTask ipt = new InsertPatientsTask();
        ipt.execute(params);
    }

    public class InsertPatientsTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = InsertPatientsTask.class.getSimpleName();

        byte[] postData = params.getBytes(StandardCharsets.UTF_8);
        int len = postData.length;

        @Override
        protected Void doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.1.164:8000/api/paciente");

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("ci", "6869126"));
            nameValuePair.add(new BasicNameValuePair("emision", "LP"));
            nameValuePair.add(new BasicNameValuePair("nombre", "Veronica Eugenia"));
            nameValuePair.add(new BasicNameValuePair("apellido", "Clavijo Altamirano"));
            nameValuePair.add(new BasicNameValuePair("fecha_nac", "16-05-1992"));
            nameValuePair.add(new BasicNameValuePair("lugar_nac", "La Paz"));
            nameValuePair.add(new BasicNameValuePair("grupo_sanguineo", "O RH+"));

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                HttpResponse response = httpClient.execute(httpPost);
                // write response to log
                Log.d("Http Post Response:", response.toString());
            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }
            return null;
        }

        /*@Override
        protected void onPostExecute(HashMap[] result) {
            if (result != null) {
                mPatientAdapter.clear();
                for(int i = 0; i < result.length; i++) {
                    mPatientAdapter.add(result[i].get(4).toString() + ", " + result[i].get(3).toString());
                }
            }
        }*/
    }
}
