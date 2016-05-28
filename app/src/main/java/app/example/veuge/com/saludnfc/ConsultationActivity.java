package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ConsultationActivity extends AppCompatActivity {

    private ArrayAdapter<String> mConsultationAdapter;
    private HashMap[] consultations;
    public final static String EXTRA_TEXT = "app.example.veuge.com.saludnfc.TEXT";

    private String codHC;
    private final String LOG_TAG = ConsultationActivity.class.getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        updateConsultations();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia");
        Log.i(LOG_TAG, "ALERT" + codHC);

        mConsultationAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_consultation, // The name of the layout ID.
                R.id.list_item_consultation_textview, // The ID of the textview to populate.
                new ArrayList<String>()
        );

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_consultations);
        listView.setAdapter(mConsultationAdapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String patientHistory = resultStrs[position].get(1).toString();
                Intent intent = new Intent(PatientsActivity.this, PatientActivity.class)
                        .putExtra(EXTRA_TEXT, patientHistory);
                startActivity(intent);
            }
        });*/
    }

    private void updateConsultations() {
        FetchConsultationsTask historiesTask = new FetchConsultationsTask();
        historiesTask.execute(codHC);
    }

    public class FetchConsultationsTask extends AsyncTask<String, Void, HashMap[]> {

        private final String LOG_TAG = FetchConsultationsTask.class.getSimpleName();

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

        /**
         * Take the String representing the complete patients list in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         */
        private HashMap[] getConsultationDataFromJson(String consultationJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_DATA = "data";
            final String OWM_ANAMNESIS = "anamnesis";
            final String OWM_PHYSICAL_EXAM = "examen_físico";
            final String OWM_DIAGNOSIS = "diagnostico";
            final String OWM_TREATMENT = "tratamiento";
            final String OWM_JUSTIFICATION = "justificacion";

            /**
             * Creates a JSONObject from the string obtained
             */
            JSONObject consultationsJson = new JSONObject(consultationJsonStr);

            /**
             * Creates a JSONObject from the previous JSONObject but parsing from the data subtree
             * in the JSON returned.
             */
            JSONArray dataArray = consultationsJson.getJSONArray(OWM_DATA);

            /**
             * Array of hashmaps of length numPatient
             */
            consultations = new HashMap[dataArray.length()];

            for(int i = 0; i < dataArray.length(); i++) {

                // Get the JSON object representing i-th item in the data segment
                JSONObject consultationDetails = dataArray.getJSONObject(i);

                /**
                 * Key - Value Map with patient details
                 */
                HashMap consultation = new HashMap();
                consultation.put(1, consultationDetails.getString(OWM_ANAMNESIS));
                consultation.put(2, consultationDetails.getString(OWM_PHYSICAL_EXAM));
                consultation.put(3, consultationDetails.getString(OWM_DIAGNOSIS));
                consultation.put(4, consultationDetails.getString(OWM_TREATMENT));
                consultation.put(5, consultationDetails.getString(OWM_JUSTIFICATION));

                // Add the patient hashmap to hashmap array
                consultations[i] = consultation;
            }

            // return hashmap array
            return consultations;

        }
        @Override
        protected HashMap[] doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse;
            HttpGet request = new HttpGet("http://192.168.1.164:8000/api/paciente/" + codHC + "/consultas");
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
                return getConsultationDataFromJson(response);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap[] result) {
            if (result != null) {
                mConsultationAdapter.clear();
                for(int i = 0; i < result.length; i++) {
                    mConsultationAdapter.add(result[i].get(1).toString() + ", " + result[i].get(2).toString()
                            + ", " + result[i].get(3).toString() + ", " + result[i].get(4).toString() + ", "
                            + result[i].get(5).toString());
                }
            }
        }
    }
}
