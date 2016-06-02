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
import java.util.HashMap;

public class HistoriesActivity extends AppCompatActivity {

    private ArrayAdapter<String> mHistoryAdapter;
    private HashMap[] histories;
    public final static String EXTRA_TEXT = "app.example.veuge.com.saludnfc.TEXT";

    private static String patientID, codHC, token;
    private final String LOG_TAG = HistoriesActivity.class.getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        updateHistories();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histories);

        Intent intent = getIntent();
        patientID = intent.getStringExtra("patientID");
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
                String patientHistory = histories[position].get(1).toString();
                Intent intent = new Intent(HistoriesActivity.this, HistoryActivity.class);

                intent.putExtra("historia_clinica", codHC);
                intent.putExtra("antecedente", patientHistory);

                startActivity(intent);
            }
        });
    }

    private void updateHistories() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/antecedentes";
        String resp;
        HashMapTransformation hmt = new HashMapTransformation(histories);

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();

            JSONArray historiesArray = hmt.getJsonFromString(resp);
            histories = hmt.buildHistoryHashmap(historiesArray);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (histories != null) {
            mHistoryAdapter.clear();

            for (int i = 0; i < histories.length; i++) {
                mHistoryAdapter.add(histories[i].get(1).toString() + ", " + histories[i].get(2).toString() + ", "
                        + histories[i].get(3).toString() + ", " + histories[i].get(4).toString());
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
