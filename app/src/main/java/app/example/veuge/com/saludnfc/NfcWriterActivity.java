package app.example.veuge.com.saludnfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class NfcWriterActivity extends AppCompatActivity {

    int patientID;
    String codHC, patientName, token;
    TextView codHcTv, patientNameTv, infoTv;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private Intent intentParam;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_writer);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("patientHistory");
        patientID = intent.getIntExtra("patientID", -1);
        Toast.makeText(this, patientID + "", Toast.LENGTH_LONG).show();
        patientName = intent.getStringExtra("patientName");

        codHcTv = (TextView) findViewById(R.id.cod_hc);
        codHcTv.setText(codHC);
        patientNameTv = (TextView) findViewById(R.id.patient_name);
        patientNameTv.setText(patientName);
        infoTv = (TextView) findViewById(R.id.info_tv);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        intentParam = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, intentParam, 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        mFilters = new IntentFilter[] {
                ndef,
        };
        mTechLists = new String[][] { new String[] { Ndef.class.getName() },
                new String[] { NdefFormatable.class.getName() }};
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                mTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String externalType = "saludnfc.com:mobile";
        String payload = codHC+":"+patientID;
        NdefRecord extRecord1 = new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, externalType.getBytes(), new byte[0], payload.getBytes());
        NdefMessage newMessage = new NdefMessage(new NdefRecord[] { extRecord1});
        writeNdefMessageToTag(newMessage, tag);
    }

    boolean writeNdefMessageToTag(NdefMessage message, Tag detectedTag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(detectedTag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is read-only.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(this, "The data cannot written to tag, Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size + " bytes.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                ndef.writeNdefMessage(message);
                ndef.close();
                Toast.makeText(this, "Datos del paciente guardados correctamente", Toast.LENGTH_SHORT).show();
                infoTv.setText("Datos guardados correctamente!.");
                return true;
            } else {
                NdefFormatable ndefFormat = NdefFormatable.get(detectedTag);
                if (ndefFormat != null) {
                    try {
                        ndefFormat.connect();
                        ndefFormat.format(message);
                        ndefFormat.close();
                        Toast.makeText(this, "The data is written to the tag ", Toast.LENGTH_SHORT).show();
                        return true;
                    } catch (IOException e) {
                        Toast.makeText(this, "Failed to format tag", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "NDEF is not supported", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Write operation is failed", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
