package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class PatientsActivity extends AppCompatActivity {

    private ArrayAdapter<String> mPatientAdapter;
    private HashMap[] patients;
    public final static String EXTRA_TEXT = "app.example.veuge.com.saludnfc.TEXT";

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

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_patients);
        listView.setAdapter(mPatientAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String patientHistory = patients[position].get(1).toString();
                Intent intent = new Intent(PatientsActivity.this, PatientActivity.class)
                        .putExtra(EXTRA_TEXT, patientHistory);
                startActivity(intent);
            }
        });
    }

    /**
     * Displays patient form to create new patient
     */
    public void patientFormCreate(View view){
        Intent intent = new Intent(PatientsActivity.this, PatientFormCreateActivity.class);
        startActivity(intent);
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
        private String convertStreamToString(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        private HashMap[] getPatientDataFromJson(String patientJsonStr) throws JSONException {

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
            patients = new HashMap[dataArray.length()];

            for (int i = 0; i < dataArray.length(); i++) {

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
                patients[i] = patient;
            }

            // return hashmap array
            return patients;

        }

        @Override
        protected HashMap[] doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse;
            HttpGet request = new HttpGet("http://192.168.1.164:8000/api/paciente");
            String response = "";

            try {
                httpResponse = client.execute(request);
                int responseCode = httpResponse.getStatusLine().getStatusCode();
                String message = httpResponse.getStatusLine().getReasonPhrase();

                HttpEntity entity = httpResponse.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    response = convertStreamToString(instream);

                    // Closing the input stream will trigger connection release
                    instream.close();
                }
            } catch (ClientProtocolException e) {
                client.getConnectionManager().shutdown();
                e.printStackTrace();
            } catch (IOException e) {
                client.getConnectionManager().shutdown();
                e.printStackTrace();
            }

            try {
                return getPatientDataFromJson(response);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap[] result) {
            if (result != null) {
                mPatientAdapter.clear();
                for (int i = 0; i < result.length; i++) {
                    mPatientAdapter.add(result[i].get(4).toString() + ", " + result[i].get(3).toString());
                }
            }
        }
    }
}
