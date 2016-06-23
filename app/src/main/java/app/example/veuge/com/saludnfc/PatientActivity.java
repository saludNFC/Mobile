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

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.models.Patient;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class PatientActivity extends AppCompatActivity {

    public Patient[] patient; // array of length 1
    public int patientID;
    public static String codHC, token, payload;

    TextView name, misc, history, ci, blood;

    String misce = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient);

        name = (TextView) findViewById(R.id.patient_name);
        misc = (TextView) findViewById(R.id.patient_misc);
        history = (TextView) findViewById(R.id.patient_history);
        ci = (TextView) findViewById(R.id.patient_ci);
        blood = (TextView) findViewById(R.id.patient_bloodtype);

        Intent intent = getIntent();

        if(intent.getAction() == null){
            patientID = intent.getIntExtra("patientID", -1);
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
                        patientID = Integer.valueOf(temp[1]);
                    }
                }
            }
        }



        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC;

        String resp;
        ObjectTransformation hmt = new ObjectTransformation();

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();
            JSONArray patientArray = hmt.getJsonFromString(resp);
            patient = hmt.buildPatientObject(patientArray);
            //patient = hmt.getPatientDataFromJson(resp);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(patient != null){
           name.setText(patient[0].nombre + " " + patient[0].apellido);

           misce += patient[0].sexo + ", " + patient[0].fecha_nac;
           misc.setText(misce);

           history.setText(codHC);
           ci.setText(patient[0].ci + " " + patient[0].emision);
           blood.setText(patient[0].grupo_sanguineo);
        }
    }

    public void patientContact(View view){
        Intent intent = new Intent(PatientActivity.this, ContactActivity.class);

        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        intent.putExtra("token", token);
        startActivity(intent);
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
        intent.putExtra("patientName", patient[0].nombre + " " + patient[0].apellido);
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