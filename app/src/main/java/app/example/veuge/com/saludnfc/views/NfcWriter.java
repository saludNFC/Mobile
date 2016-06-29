package app.example.veuge.com.saludnfc.views;

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

import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.models.Patient;

public class NfcWriter extends AppCompatActivity {

    private Patient patient;

    private TextView nfcPatientName;
    private TextView nfcPatientHC;

    // NFC related
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private Intent intentParam;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writer_nfc);

        Intent intent = getIntent();
        patient = (Patient) intent.getSerializableExtra("PATIENT");
        setupUI();
        fillData();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        intentParam = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, intentParam, 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        mFilters = new IntentFilter[] {
                ndef,
        };
        mTechLists = new String[][] {
                new String[] { Ndef.class.getName() },
                new String[] { NdefFormatable.class.getName() }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  // gets the read tag!

        String externalType = "saludnfc.com:mobile";
        String payload = patient.historia+":"+patient.id;
        NdefRecord extRecord = new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, externalType.getBytes(), new byte[0], payload.getBytes());
        NdefMessage newMessage = new NdefMessage(new NdefRecord[] { extRecord });
        writeNdefMessageToTag(newMessage, tag);
    }

    public boolean writeNdefMessageToTag(NdefMessage message, Tag detectedTag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(detectedTag); // gets NDEF from the detected tag
            if (ndef != null) {
                ndef.connect(); // opens tag to read and write

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "La etiqueta es de solo lectura.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(this, "Error. La capacidad de NDEF de la etiqueta es " + ndef.getMaxSize() + " bytes, el mensaje es " + size + " bytes.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                ndef.writeNdefMessage(message); // writes NDEF message to tag.
                ndef.close();   // closes tag to read and write
                Toast.makeText(this, "Datos del paciente guardados correctamente", Toast.LENGTH_SHORT).show();
                return true;
            }
            // if the tag isn't formatted with NDEF
            else {
                NdefFormatable ndefFormat = NdefFormatable.get(detectedTag);
                if (ndefFormat != null) {
                    try {
                        ndefFormat.connect();
                        ndefFormat.format(message);
                        ndefFormat.close();
                        Toast.makeText(this, "Datos escritos en la etiqueta.", Toast.LENGTH_SHORT).show();
                        return true;
                    } catch (IOException e) {
                        Toast.makeText(this, "Falló en formatear la etiqueta.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                // if the tag isn't NDEF formatted and can't be formatted
                else {
                    Toast.makeText(this, "NDEF no soportado.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Write operation is failed", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void setupUI(){
        nfcPatientName = (TextView) findViewById(R.id.nfc_patient_name);
        nfcPatientHC = (TextView) findViewById(R.id.nfc_patient_hc);
    }

    public void fillData(){
        nfcPatientName.setText(patient.nombre + " " + patient.apellido);
        nfcPatientHC.setText(patient.historia);
    }
}
