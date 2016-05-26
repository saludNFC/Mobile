package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class PatientActivity extends AppCompatActivity {
    public static String codHC;
    public HashMap patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Intent intent = getIntent();
        codHC = intent.getStringExtra(PatientsActivity.EXTRA_TEXT);

        FetchPatientTask patientDataTask = new FetchPatientTask();
        patientDataTask.execute(codHC);
    }

    public void patientForm(View view){
        Intent intent = new Intent(PatientActivity.this, PatientFormActivity.class);

        intent.putExtra("historia", codHC);
        intent.putExtra("ci", patient.get(1).toString());
        intent.putExtra("emision", patient.get(2).toString());
        intent.putExtra("nombre", patient.get(3).toString());
        intent.putExtra("apellido", patient.get(4).toString());
        intent.putExtra("sexo", patient.get(5).toString());
//        intent.putExtra("fecha_nacimiento", patient.get(6).toString());
        intent.putExtra("lugar_nacimiento", patient.get(7).toString());
        intent.putExtra("grado_instruccion", patient.get(8).toString());
        intent.putExtra("estado_civil", patient.get(9).toString());
        intent.putExtra("ocupacion", patient.get(10).toString());
        intent.putExtra("grupo_sanguineo", patient.get(11).toString());

        startActivity(intent);
    }

    public class FetchPatientTask extends AsyncTask<String, Void, HashMap> {

        private final String LOG_TAG = FetchPatientTask.class.getSimpleName();

        /**
         * Take the String representing the complete patients list in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         */
        private HashMap getPatientDataFromJson(String patientJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_DATA = "data";
            final String OWM_CI = "ci";
            final String OWM_EMISION = "emision";
            final String OWM_NOMBRE = "nombre";
            final String OWM_APELLIDO = "apellido";
            final String OWM_SEXO = "sexo";
            final String OWM_FECHA_NAC = "fecha_nacimiento";
            final String OWM_DATE = "date";
            final String OWM_LUGAR_NAC = "lugar_nacimiento";
            final String OWM_GRADO_INST= "grado_instruccion";
            final String OWM_ESTADO = "estado_civil";
            final String OWM_OCUPACION = "ocupacion";
            final String OWM_GRUPO = "grupo_sanguineo";

            /**
             * Creates a JSONObject from the string obtained
             */
            JSONObject patientJson = new JSONObject(patientJsonStr);

            /**
             * Creates a JSONObject from the previous JSONObject but parsing from the data subtree
             * in the JSON returned.
             */
            JSONObject patientDetails = patientJson.getJSONObject(OWM_DATA);

            patient = new HashMap();

            /**
             * Key - Value Map with patient details
             */
            patient.put(1, patientDetails.getString(OWM_CI));
            patient.put(2, patientDetails.getString(OWM_EMISION));
            patient.put(3, patientDetails.getString(OWM_NOMBRE));
            patient.put(4, patientDetails.getString(OWM_APELLIDO));
            patient.put(5, patientDetails.getString(OWM_SEXO));
            patient.put(7, patientDetails.getString(OWM_FECHA_NAC));
            patient.put(8, patientDetails.getString(OWM_LUGAR_NAC));
            patient.put(9, patientDetails.getString(OWM_GRADO_INST));
            patient.put(10, patientDetails.getString(OWM_ESTADO));
            patient.put(11, patientDetails.getString(OWM_OCUPACION));
            patient.put(12, patientDetails.getString(OWM_GRUPO));

            // return hashmap
            return patient;
        }

        @Override
        protected HashMap<Integer, String> doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String patientJsonStr = null;

            try {
                // Construct the URL for the saludNFC API
                URL url = new URL("http://192.168.1.159:8000/api/paciente" + "/" + codHC);

                Log.v(LOG_TAG, "Built URI " + url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                // Sets the GET method for the request!
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                patientJsonStr = buffer.toString();

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

            try {
                return getPatientDataFromJson(patientJsonStr);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap result) {
            TextView name = (TextView) findViewById(R.id.patient_name);
            TextView misc = (TextView) findViewById(R.id.patient_misc);
            String misce = "";

            if (result != null) {
                name.setText(result.get(3).toString() + " " + result.get(4).toString());

                misce += codHC + ", " + result.get(5).toString() + ", " + result.get(1).toString() + " " + result.get(2).toString();
                misc.setText(misce);
            }
        }
    }
}
