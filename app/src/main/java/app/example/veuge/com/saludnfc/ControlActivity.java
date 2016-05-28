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

public class ControlActivity extends AppCompatActivity {

    private ArrayAdapter<String> mControlAdapter;
    private HashMap[] resultStrs;
    public final static String EXTRA_TEXT = "app.example.veuge.com.saludnfc.TEXT";
    private String codHC;
    private final String LOG_TAG = HistoryActivity.class.getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        updateControls();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia");
        Log.i(LOG_TAG, "ALERT" + codHC);

        mControlAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_control, // The name of the layout ID.
                R.id.list_item_control_textview, // The ID of the textview to populate.
                new ArrayList<String>()
        );

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_controls);
        listView.setAdapter(mControlAdapter);
    }

    /**
     * Function to update the patients list!
     */
    private void updateControls() {
        FetchControlsTask patientsTask = new FetchControlsTask();
        patientsTask.execute(codHC);
    }

    public class FetchControlsTask extends AsyncTask<String, Void, HashMap[]> {

        private final String LOG_TAG = FetchControlsTask.class.getSimpleName();

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
        private HashMap[] getHistoryDataFromJson(String controlJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_DATA = "data";
            final String OWM_TYPE = "tipo_control";

            final String OWM_VACCINE = "vacuna";
            final String OWM_VIA = "via_administracion";
            final String OWM_DOSIS = "dosis";

            final String OWM_WEIGHT = "peso";
            final String OWM_HEIGHT = "altura";

            final String OWM_TEMPERATURE = "temperatura";
            final String OWM_HEART_RATE = "frecuencia_cardiaca";
            final String OWM_SISTOLE = "sistole";
            final String OWM_DIASTOLE = "diastole";

            final String OWM_LAST_MEN = "ultima_menstruacion";
            final String OWM_LAST_MAM = "ultima_mamografia";
            final String OWM_SEX_ACT = "vida_sexual";
            final String OWM_LAST_PAP = "ultimo_papanicolau";
            final String OWM_DATE = "date";

            final String OWM_GERIATRIC_TYPE = "tipo_valoracion";
            final String OWM_NOTES = "descripcion";

            /**
             * Creates a JSONObject from the string obtained
             */
            JSONObject controlsJson = new JSONObject(controlJsonStr);

            /**
             * Creates a JSONObject from the previous JSONObject but parsing from the data subtree
             * in the JSON returned.
             */
            JSONArray dataArray = controlsJson.getJSONArray(OWM_DATA);

            /**
             * Array of hashmaps
             */
            resultStrs = new HashMap[dataArray.length()];

            for(int i = 0; i < dataArray.length(); i++) {

                // Get the JSON object representing i-th item in the data segment
                JSONObject controlDetails = dataArray.getJSONObject(i);

                /**
                 * Key - Value Map with patient details
                 */
                HashMap control = new HashMap();
                control.put(1, controlDetails.getString(OWM_TYPE));
                switch (controlDetails.get(OWM_TYPE).toString()){
                    case "Vacunacion":
                        control.put(2, controlDetails.getString(OWM_VACCINE));
                        control.put(3, controlDetails.getString(OWM_VIA));
                        control.put(4, controlDetails.getString(OWM_DOSIS));
                        break;

                    case "Crecimiento":
                        control.put(2, controlDetails.getString(OWM_WEIGHT));
                        control.put(3, controlDetails.getString(OWM_HEIGHT));
                        break;
                    case "Triaje":
                        control.put(2, controlDetails.getString(OWM_TEMPERATURE));
                        control.put(3, controlDetails.getString(OWM_HEART_RATE));
                        control.put(4, controlDetails.getString(OWM_SISTOLE));
                        control.put(5, controlDetails.getString(OWM_DIASTOLE));
                        break;
                    case "Ginecologico":
                        JSONObject date1 = controlDetails.getJSONObject(OWM_LAST_MEN);
                        control.put(2, date1.getString(OWM_DATE));

                        if(!controlDetails.isNull(OWM_LAST_MAM)){
                            JSONObject date2 = controlDetails.getJSONObject(OWM_LAST_MAM);
                            Log.i(LOG_TAG, "WTH! " + date2);
                            control.put(3, date2.getString(OWM_DATE));
                        }
                        else{
                            control.put(3, "");
                        }

                        control.put(4, controlDetails.getString(OWM_SEX_ACT));

                        if(!controlDetails.isNull(OWM_LAST_PAP)){
                            JSONObject date3 = controlDetails.getJSONObject(OWM_LAST_PAP);
                            control.put(5, date3.getString(OWM_DATE));
                        }

                        break;
                    case "Geriatrico":
                        control.put(2, controlDetails.getString(OWM_GERIATRIC_TYPE));
                        control.put(3, controlDetails.getString(OWM_NOTES));
                        break;
                }

                // Add the patient hashmap to hashmap array
                resultStrs[i] = control;
            }

            // return hashmap array
            return resultStrs;

        }
        @Override
        protected HashMap[] doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse;
            HttpGet request = new HttpGet("http://192.168.1.164:8000/api/paciente/" + codHC + "/controles");
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
                mControlAdapter.clear();
                for(int i = 0; i < result.length; i++) {
                    mControlAdapter.add(result[i].get(1).toString() + ", " + result[i].get(2).toString() + ", " + result[i].get(3).toString());
                }
            }
        }
    }
}