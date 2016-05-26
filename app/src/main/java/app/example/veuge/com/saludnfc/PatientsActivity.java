package app.example.veuge.com.saludnfc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PatientsActivity extends AppCompatActivity {

    private ArrayAdapter<String> mPatientAdapter;
    private HashMap[] resultStrs;
    public final static String EXTRA_TEXT = "com.mycompany.myfirstapp.TEXT";

    @Override
    public void onStart() {
        super.onStart();
        updatePatients();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        mPatientAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_patient, // The name of the layout ID.
                R.id.list_item_patient_textview, // The ID of the textview to populate.
                new ArrayList<String>()
        );

        //View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_patients);
        listView.setAdapter(mPatientAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String patientHistory = resultStrs[position].get(1).toString();
                Intent intent = new Intent(PatientsActivity.this, PatientActivity.class)
                        .putExtra(EXTRA_TEXT, patientHistory);
                startActivity(intent);
            }
        });
    }

   /**
     * Function to update the patients list!
     */
    private void updatePatients() {
        FetchPatientsTask patientsTask = new FetchPatientsTask();
        patientsTask.execute();
    }

    public class FetchPatientsTask extends AsyncTask<Void, Void, HashMap[]> {

        private final String LOG_TAG = FetchPatientsTask.class.getSimpleName();

        /**
         * Take the String representing the complete patients list in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         */
        private HashMap[] getPatientDataFromJson(String patientJsonStr, int numPatient) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_DATA = "data";
            final String OWM_HC = "historia_clinica";
            final String OWM_CI = "ci";
            final String OWM_NOMBRE = "nombre";
            final String OWM_APELLIDO = "apellido";
            final String OWM_FECHA_NAC = "fecha_nacimiento";

            /**
             * Creates a JSONObject from the string obtained
             */
            JSONObject patientsJson = new JSONObject(patientJsonStr);

            /**
             * Creates a JSONObject from the previous JSONObject but parsing from the data subtree
             * in the JSON returned.
             */
            JSONArray dataArray = patientsJson.getJSONArray(OWM_DATA);

            /**
             * Array of hashmaps of length numPatient
             */
            resultStrs = new HashMap[numPatient];

            for(int i = 0; i < dataArray.length(); i++) {

                // Get the JSON object representing i-th item in the data segment
                JSONObject patientDetails = dataArray.getJSONObject(i);

                /**
                 * Key - Value Map with patient details
                 */
                HashMap patient = new HashMap();
                patient.put(1, patientDetails.getString(OWM_HC));
                patient.put(2, patientDetails.getString(OWM_CI));
                patient.put(3, patientDetails.getString(OWM_NOMBRE));
                patient.put(4, patientDetails.getString(OWM_APELLIDO));
                patient.put(5, patientDetails.getString(OWM_FECHA_NAC));

                // Add the patient hashmap to hashmap array
                resultStrs[i] = patient;
            }

            // return hashmap array
            return resultStrs;

        }
        @Override
        protected HashMap[] doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String patientJsonStr = null;

            int numPatient = 50;

            try {
                // Construct the URL for the saludNFC API
                URL url = new URL("http://192.168.1.159:8000/api/paciente");

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
                return getPatientDataFromJson(patientJsonStr, numPatient);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap[] result) {
            if (result != null) {
                mPatientAdapter.clear();
                for(int i = 0; i < result.length; i++) {
                    mPatientAdapter.add(result[i].get(4).toString() + ", " + result[i].get(3).toString());
                }
            }
        }
    }
}
