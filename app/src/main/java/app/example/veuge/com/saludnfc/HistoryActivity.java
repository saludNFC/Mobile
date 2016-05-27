package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class HistoryActivity extends AppCompatActivity {

    private ArrayAdapter<String> mHistoryAdapter;
    private HashMap[] resultStrs;
    public final static String EXTRA_TEXT = "com.mycompany.myfirstapp.TEXT";
    private String codHC;
    private final String LOG_TAG = HistoryActivity.class.getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        updateHistories();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia");
        Log.i(LOG_TAG, "ALERT" + codHC);

        mHistoryAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_history, // The name of the layout ID.
                R.id.list_item_history_textview, // The ID of the textview to populate.
                new ArrayList<String>()
        );

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_histories);
        listView.setAdapter(mHistoryAdapter);

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

    /**
     * Function to update the patients list!
     */
    private void updateHistories() {
        FetchHistoriesTask patientsTask = new FetchHistoriesTask();
        patientsTask.execute(codHC);
    }

    public class FetchHistoriesTask extends AsyncTask<String, Void, HashMap[]> {

        private final String LOG_TAG = FetchHistoriesTask.class.getSimpleName();

        /**
         * Take the String representing the complete patients list in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         */
        private HashMap[] getHistoryDataFromJson(String historyJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_DATA = "data";
            final String OWM_TYPE = "tipo_antecedente";
            final String OWM_GRADE = "grado_parentesco";
            final String OWM_ILLNESS = "enfermedad";
            final String OWM_PERSONAL_TYPE = "tipo_personal";
            final String OWM_DESC = "descripcion";
            final String OWM_MED = "medicamento";
            final String OWM_VIA = "via_administracion";
            final String OWM_DATE_INI = "fecha_inicio";
            final String OWM_DATE = "date";

            /**
             * Creates a JSONObject from the string obtained
             */
            JSONObject historiesJson = new JSONObject(historyJsonStr);

            /**
             * Creates a JSONObject from the previous JSONObject but parsing from the data subtree
             * in the JSON returned.
             */
            JSONArray dataArray = historiesJson.getJSONArray(OWM_DATA);

            /**
             * Array of hashmaps of length numPatient
             */
            resultStrs = new HashMap[dataArray.length()];

            for(int i = 0; i < dataArray.length(); i++) {

                // Get the JSON object representing i-th item in the data segment
                JSONObject historyDetails = dataArray.getJSONObject(i);

                /**
                 * Key - Value Map with patient details
                 */
                HashMap patient = new HashMap();
                patient.put(1, historyDetails.getString(OWM_TYPE));
                switch (historyDetails.get(OWM_TYPE).toString()){
                    case "Familiar":
                        patient.put(2, historyDetails.getString(OWM_GRADE));
                        patient.put(3, historyDetails.getString(OWM_ILLNESS));
                        break;

                    case "Personal":
                        patient.put(2, historyDetails.getString(OWM_PERSONAL_TYPE));
                        patient.put(3, historyDetails.getString(OWM_DESC));
                        break;
                    case "Medicamentos":
                        patient.put(2, historyDetails.getString(OWM_MED));
                        patient.put(3, historyDetails.getString(OWM_VIA));

                        JSONObject date = historyDetails.getJSONObject(OWM_DATE_INI);
                        patient.put(4, date.getString(OWM_DATE));

                        break;
                }

                // Add the patient hashmap to hashmap array
                resultStrs[i] = patient;
            }

            // return hashmap array
            return resultStrs;

        }
        @Override
        protected HashMap[] doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String historyJsonStr = null;

            try {
                // Construct the URL for the saludNFC API
                URL url = new URL("http://192.168.1.159:8000/api/paciente/" + params[0] + "/antecedentes");

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
                historyJsonStr = buffer.toString();

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
                return getHistoryDataFromJson(historyJsonStr);
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
                mHistoryAdapter.clear();
                for(int i = 0; i < result.length; i++) {
                    mHistoryAdapter.add(result[i].get(1).toString() + ", " + result[i].get(2).toString() + ", " + result[i].get(3).toString());
                }
            }
        }
    }
}
