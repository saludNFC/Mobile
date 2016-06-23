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

import app.example.veuge.com.saludnfc.models.History;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class HistoriesActivity extends AppCompatActivity {

    private ArrayAdapter<String> mHistoryAdapter;
    private History[] histories;

    private static int patientID;
    private static String codHC, token;

    @Override
    public void onStart() {
        super.onStart();
        updateHistories();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histories);

        Intent intent = getIntent();
        patientID = intent.getIntExtra("patientID", -1);
        codHC = intent.getStringExtra("patientHistory");
        token = intent.getStringExtra("token");

        mHistoryAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_history, // The name of the layout ID.
                R.id.list_item_history_textview, // The ID of the textview to populate.
                new ArrayList<String>()
        );

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_histories);
        listView.setAdapter(mHistoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int patientHistory = histories[position].id;
                Intent intent = new Intent(HistoriesActivity.this, HistoryActivity.class);

                intent.putExtra("historia_clinica", codHC);
                intent.putExtra("antecedente", patientHistory);
                intent.putExtra("token", token);

                startActivity(intent);
            }
        });
    }

    private void updateHistories() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/antecedentes";
        String resp;
        //HashMapTransformation hmt = new HashMapTransformation(histories);
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();

            JSONArray historiesArray = hmt.getJsonFromString(resp);
            histories = hmt.buildHistoryObject(historiesArray);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (histories != null) {
            mHistoryAdapter.clear();

            for (int i = 0; i < histories.length; i++) {
                //mHistoryAdapter.add(histories[i].id + ", " + histories[i].historyType);

                if(histories[i].historyType.equals("Familiar")){
                    mHistoryAdapter.add(histories[i].createdAt + ", " + histories[i].historyType + ", " +
                            histories[i].grade + ", " + histories[i].illness);
                }

                if(histories[i].historyType.equals("Personal")){
                    mHistoryAdapter.add(histories[i].createdAt + ", " + histories[i].historyType + ", " +
                            histories[i].typePersonal + ", " + histories[i].description);
                }

                if(histories[i].historyType.equals("Medicamentos")){
                    mHistoryAdapter.add(histories[i].createdAt + ", " + histories[i].historyType + ", " +
                            histories[i].med + ", " + histories[i].via);
                }
            }
        }
    }

    public void historyFormCreate(View view){
        Intent intent = new Intent(HistoriesActivity.this, HistoryFormCreateActivity.class);
        intent.putExtra("patientID", patientID);
        intent.putExtra("patientHistory", codHC);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
