package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ControlsActivity extends AppCompatActivity {

    private ArrayAdapter<String> mControlAdapter;
    private HashMap[] controls;

    private static String patientID;
    private String codHC;

    @Override
    public void onStart() {
        super.onStart();
        updateControls();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);

        Intent intent = getIntent();
        patientID = intent.getStringExtra("patientID");
        codHC = intent.getStringExtra("patientHistory");

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
                String patientControl = controls[position].get(1).toString();
                Intent intent = new Intent(ControlsActivity.this, ControlActivity.class);

                intent.putExtra("historia_clinica", codHC);
                intent.putExtra("control", patientControl);

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
        HashMapTransformation hmt = new HashMapTransformation(controls);

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path);
            gat.execute();
            resp = gat.get();

            JSONArray controlsArray = hmt.getJsonFromString(resp);
            controls = hmt.buildControlHashmap(controlsArray);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (controls != null) {
            mControlAdapter.clear();

            for (int i = 0; i < controls.length; i++) {
                mControlAdapter.add(controls[i].get(1).toString() + ", " + controls[i].get(2).toString()
                        + ", " + controls[i].get(3).toString());
            }
        }
    }

    public void controlFormCreate(View view){
        Intent intent = new Intent(ControlsActivity.this, ControlFormCreateActivity.class);
        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        startActivity(intent);
    }
}