package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.HashMap;

public class PatientActivity extends AppCompatActivity {

    public HashMap[] patient; // array of length 1
    public static String patientID, codHC, token, payload;

    TextView name, misc, history, ci, blood;

    String misce = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        name = (TextView) findViewById(R.id.patient_name);
        misc = (TextView) findViewById(R.id.patient_misc);
        history = (TextView) findViewById(R.id.patient_history);
        ci = (TextView) findViewById(R.id.patient_ci);
        blood = (TextView) findViewById(R.id.patient_bloodtype);

        Intent intent = getIntent();
        String x = intent.getAction();
        Toast.makeText(this, "Action: " + x, Toast.LENGTH_LONG).show();

        if(intent.getAction() == null){
            patientID = intent.getStringExtra("patientID");
            codHC = intent.getStringExtra("patientHistory");
            token = intent.getStringExtra("token");
        }
        else {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
                NdefMessage[] messages = getNdefMessages(getIntent());
                for (int i = 0; i < messages.length; i++) {
                    for (int j = 0; j < messages[0].getRecords().length; j++) {
                        NdefRecord record = messages[i].getRecords()[j];
                        payload = new String(record.getPayload());

                        String delimiter = ":";
                        String[] temp = payload.split(delimiter);
                        codHC = temp[0];
                        patientID = temp[1];
                    }
                }
            }
        }

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC;

        String resp;
        HashMapTransformation hmt = new HashMapTransformation(patient);

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();
            JSONArray patientArray = hmt.getJsonFromString(resp);
            patient = hmt.buildPatientHashmap(patientArray);
            //patient = hmt.getPatientDataFromJson(resp);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(patient != null){
            if (patient != null && patient[0].get(3) != null && patient[0].get(4) != null ) {
                name.setText(patient[0].get(5).toString() + " " + patient[0].get(4).toString());

                misce += patient[0].get(6).toString() + ", " + patient[0].get(7).toString();
                misc.setText(misce);
            }

            history.setText(codHC);
            ci.setText(patient[0].get(2).toString() + " " + patient[0].get(3).toString());
            blood.setText(patient[0].get(12).toString());
        }
    }

    public void patientFormCreate(View view){
        Intent intent = new Intent(PatientActivity.this, PatientFormCreateActivity.class);
        intent.putExtra("historia", codHC);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    public void patientHistories(View view){
        Intent intent = new Intent(PatientActivity.this, HistoriesActivity.class);

        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    public void patientControls(View view){
        Intent intent = new Intent(PatientActivity.this, ControlsActivity.class);

        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    public void patientConsultations(View view){
        Intent intent = new Intent(PatientActivity.this, ConsultationsActivity.class);

        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    public void patientRecord(View view){
        Intent intent = new Intent(PatientActivity.this, NfcWriterActivity.class);

        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        intent.putExtra("patientName", patient[0].get(4).toString() + " " + patient[0].get(5).toString());
        intent.putExtra("token", token);

        startActivity(intent);
    }

    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {
                        msg
                };
            }
        }
        else {
            Log.d("NFC Transportation", "Unknown intent.");
            finish();
        }

        return msgs;
    }
}