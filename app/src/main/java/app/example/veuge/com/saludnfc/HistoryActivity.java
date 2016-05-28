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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {

    private ArrayAdapter<String> mHistoryAdapter;
    private HashMap[] histories;
    public final static String EXTRA_TEXT = "app.example.veuge.com.saludnfc.TEXT";

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

    private void updateHistories() {
        FetchHistoriesTask historiesTask = new FetchHistoriesTask();
        historiesTask.execute(codHC);
    }

    public class FetchHistoriesTask extends AsyncTask<String, Void, HashMap[]> {

        private final String LOG_TAG = FetchHistoriesTask.class.getSimpleName();

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
            histories = new HashMap[dataArray.length()];

            for(int i = 0; i < dataArray.length(); i++) {

                // Get the JSON object representing i-th item in the data segment
                JSONObject historyDetails = dataArray.getJSONObject(i);

                /**
                 * Key - Value Map with patient details
                 */
                HashMap history = new HashMap();
                history.put(1, historyDetails.getString(OWM_TYPE));
                switch (historyDetails.get(OWM_TYPE).toString()){
                    case "Familiar":
                        history.put(2, historyDetails.getString(OWM_GRADE));
                        history.put(3, historyDetails.getString(OWM_ILLNESS));
                        break;

                    case "Personal":
                        history.put(2, historyDetails.getString(OWM_PERSONAL_TYPE));
                        history.put(3, historyDetails.getString(OWM_DESC));
                        break;
                    case "Medicamentos":
                        history.put(2, historyDetails.getString(OWM_MED));
                        history.put(3, historyDetails.getString(OWM_VIA));

                        JSONObject date = historyDetails.getJSONObject(OWM_DATE_INI);
                        history.put(4, date.getString(OWM_DATE));
                        break;
                }

                // Add the patient hashmap to hashmap array
                histories[i] = history;
            }

            // return hashmap array
            return histories;

        }
        @Override
        protected HashMap[] doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse;
            HttpGet request = new HttpGet("http://192.168.1.164:8000/api/paciente/" + codHC + "/antecedentes");
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
                return getHistoryDataFromJson(response);
            } catch (JSONException e) {
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
