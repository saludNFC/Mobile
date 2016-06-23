package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

import app.example.veuge.com.saludnfc.models.Control;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class ControlsActivity extends AppCompatActivity {

    private ArrayAdapter<String> mControlAdapter;
    private Control[] controls;

    private static String codHC, token;
    private static int patientID;

    @Override
    public void onStart() {
        super.onStart();
        updateControls();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controls);

        Intent intent = getIntent();
        patientID = intent.getIntExtra("patientID", -1);
        codHC = intent.getStringExtra("patientHistory");
        token = intent.getStringExtra("token");

        mControlAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_control, // The name of the layout ID.
                R.id.list_item_control_textview, // The ID of the textview to populate.
                new ArrayList<String>()
        );

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_controls);
        listView.setAdapter(mControlAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int patientControl = controls[position].id;
                Intent intent = new Intent(ControlsActivity.this, ControlActivity.class);

                intent.putExtra("historia_clinica", codHC);
                intent.putExtra("control", patientControl);
                intent.putExtra("token", token);

                startActivity(intent);
            }
        });
    }

    /**
     * Function to update the patients list!
     */
    private void updateControls() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/controles";
        String resp;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();

            JSONArray controlsArray = hmt.getJsonFromString(resp);
            controls = hmt.buildControlObject(controlsArray);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (controls != null) {
            mControlAdapter.clear();

            for (int i = 0; i < controls.length; i++) {
                if(controls[i].controlType.equals("Vacunacion")){
                    mControlAdapter.add(controls[i].createdAt + ", " + controls[i].controlType + ", " +
                            controls[i].vaccine + ", " + controls[i].viaVac);
                }

                if(controls[i].controlType.equals("Crecimiento")){
                    mControlAdapter.add(controls[i].createdAt + ", " + controls[i].controlType + ", " +
                            controls[i].weight + "Kg, " + controls[i].height + "cm.");
                }

                if(controls[i].controlType.equals("Triaje")){
                    mControlAdapter.add(controls[i].createdAt + ", " + controls[i].controlType + ", " +
                            controls[i].temperature + "°C, " + controls[i].heartRate);
                }
                if(controls[i].controlType.equals("Ginecologico")){
                    mControlAdapter.add(controls[i].createdAt + ", " + controls[i].controlType + ", " +
                            controls[i].lastMenst + ", " + controls[i].lastMamo);
                }
                if(controls[i].controlType.equals("Geriatrico")){
                    mControlAdapter.add(controls[i].createdAt + ", " + controls[i].controlType + ", " +
                            controls[i].geriatric + ", " + controls[i].notes);
                }
            }
        }
    }

    public void controlFormCreate(View view){
        Intent intent = new Intent(ControlsActivity.this, ControlFormCreateActivity.class);
        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}