package app.example.veuge.com.saludnfc.views;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.ButtonsAdapter;
import app.example.veuge.com.saludnfc.adapters.PatientsAdapter;
import app.example.veuge.com.saludnfc.models.Patient;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class PatientDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView patientName;
    private TextView patientCode;
    private TabLayout tabs;
    private GridView gridView;

    private Patient patient;
    private List<Patient> patientNFC;
    private PatientsAdapter adapter;

    private String payload, codHC;
    private int patientID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_detail);

        Intent intent = getIntent();
        if (intent.getAction() == null) {
            // if data comes from patientslist activity
            patient = (Patient) getIntent().getSerializableExtra("PATIENT");
        } else {
            // if data comes from NFC tag
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
            getPatientFromAPI();
            patient = patientNFC.get(0);
        }

        setupUI();

        patientName.setText(patient.nombre + " " + patient.apellido);
        patientCode.setText(patient.historia);

        tabs.addTab(tabs.newTab().setText(patient.grupo_sanguineo));
        if (patient.sexo.equals("Femenino")) {
            tabs.addTab(tabs.newTab().setIcon(R.drawable.ic_female));
        } else {
            tabs.addTab(tabs.newTab().setIcon(R.drawable.ic_male));
        }
        tabs.addTab(tabs.newTab().setText(patient.fecha_nac));
    }

    public void setupUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabs = (TabLayout) findViewById(R.id.tabs);

        patientName = (TextView) findViewById(R.id.patient_name);
        patientCode = (TextView) findViewById(R.id.patient_hcode);

        gridView = (GridView) findViewById(R.id.buttons);
        gridView.setAdapter(new ButtonsAdapter(this));
        Log.d("WHATEVER", patient.historia);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Context context = view.getContext();
                Intent intent = null;

                switch (position) {
                    case 0:
                        intent = new Intent(context, NfcWriter.class);
                        break;
                    case 1:
                        intent = new Intent(context, ContactDetail.class);
                        break;
                    case 2:
                        intent = new Intent(context, LogActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, HistoriesList.class);
                        break;
                    case 4:
                        intent = new Intent(context, ControlsList.class);
                        break;
                    case 5:
                        intent = new Intent(context, ConsultationsList.class);
                }
                intent.putExtra("PATIENT", patient);
                intent.putExtra("PATIENT_CODE", patient.historia);
                context.startActivity(intent);
            }
        });
    }

    public NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{
                        record
                });
                msgs = new NdefMessage[]{
                        msg
                };
            }
        } else {
            finish();
        }

        return msgs;
    }

    public void getPatientFromAPI() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC;

        String resp;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, "");
            gat.execute();
            resp = gat.get();
            JSONArray patientArray = hmt.getJsonFromString(resp);
            patientNFC = hmt.buildPatientObject(patientArray);
            //patient = hmt.getPatientDataFromJson(resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
