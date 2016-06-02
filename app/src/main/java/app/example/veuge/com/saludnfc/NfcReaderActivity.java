package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NfcReaderActivity extends AppCompatActivity {

    String patientID, patientCod, payload;
    TextView patientIdTv, patientCodTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_reader);

        patientIdTv = (TextView) findViewById(R.id.patient_id);
        patientCodTv = (TextView) findViewById(R.id.patient_cod);

        Intent intent = getIntent();
        Log.i("Intent Value ", intent.toString());
        //Toast.makeText(this, "intent value " + intent.toString(), Toast.LENGTH_LONG).show();
        String x = intent.getAction();
        Toast.makeText(this, "Action NFC " + x, Toast.LENGTH_LONG).show();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            for(int i = 0; i < messages.length; i++){
                for(int j = 0; j < messages[0].getRecords().length; j++){
                    NdefRecord record = messages[i].getRecords()[j];
                    payload=new String(record.getPayload());

                    String delimiter = ":";
                    String[] temp = payload.split(delimiter);
                    patientCod=temp[0];
                    patientID=temp[1];
                }
            }
            patientIdTv.setText(patientID);
            patientCodTv.setText(patientCod);
        }
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
