package app.example.veuge.com.saludnfc;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                // Construct the URL for the saludNFC API
                URL url = new URL("http://192.168.1.159:8000/api/paciente");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setInstanceFollowRedirects(false);

                // Sets the POST method for the request!
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //urlConnection.setRequestProperty("charset", "utf-8");
                urlConnection.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6XC9cLzE5Mi4xNjguMS4xNTk6ODAwMFwvYXBpXC9hdXRoIiwiaWF0IjoxNDY0MzEyMzI0LCJleHAiOjE0NjQzMTU5MjQsIm5iZiI6MTQ2NDMxMjMyNCwianRpIjoiYzNmYThjZmZmMTVlMjU1Mjc0MDhhOTY3MTIzY2VmYzcifQ.zWCYaDAjXVoSIMDijFlm6fqw65vXkTkZgvReY4umvpw");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                urlConnection.setRequestProperty("Accept","*/*");

                try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
                    Log.i(LOG_TAG, "######" + params[0]);
                    wr.writeChars(params[0]);
                    String status = urlConnection.getResponseCode() + " ";
                    String response = urlConnection.getResponseMessage();

                    BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getErrorStream())));
                    String output = "";
                    while ((br.readLine()) != null) {
                        output += br.readLine();
                        Log.i(LOG_TAG, "ERROR MESS: " + output);
                    }

                    Log.i(LOG_TAG, "&&&&&&&&&&& " + status + " " + response);
                }
                catch (IOException e){
                    Log.e(LOG_TAG, "XDXD", e);
                }
            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
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
