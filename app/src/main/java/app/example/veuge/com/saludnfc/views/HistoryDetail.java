package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.HistoriesAdapter;
import app.example.veuge.com.saludnfc.models.History;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class HistoryDetail extends AppCompatActivity {

    private ImageView icHistoryType;
    private TextView historyType;
    private TextView createdAt;
    private TextView detailOneLabel;
    private TextView detailOne;
    private TextView detailTwoLabel;
    private TextView detailTwo;
    private TextView detailThreeLabel;
    private TextView detailThree;

    private History history;
    private List<History> historyAPI;
    private HistoriesAdapter adapter;

    private String codHC;
    private int patientID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail);

        Intent intent = getIntent();
        history = (History) intent.getSerializableExtra("HISTORY");

        setupUI();
        fillDetails();
    }

    public void setupUI() {
        icHistoryType = (ImageView) findViewById(R.id.ic_history_type);
        historyType = (TextView) findViewById(R.id.history_type);
        createdAt = (TextView) findViewById(R.id.created_at);
        detailOneLabel = (TextView) findViewById(R.id.detail_one_label);
        detailOne = (TextView) findViewById(R.id.detail_one);
        detailTwoLabel = (TextView) findViewById(R.id.detail_two_label);
        detailTwo = (TextView) findViewById(R.id.detail_two);
        detailThreeLabel = (TextView) findViewById(R.id.detail_three_label);
        detailThree = (TextView) findViewById(R.id.detail_three);
    }

    public void fillDetails(){
        createdAt.setText(history.createdAt);
        switch (history.historyType){
            case "Familiar":
                icHistoryType.setImageResource(R.drawable.ic_familiar);
                historyType.setText(R.string.fam);
                detailOneLabel.setText(R.string.grade_field);
                detailOne.setText(history.grade);
                detailTwoLabel.setText(R.string.illness_field);
                detailTwo.setText(history.illness);
                break;

            case "Personal":
                icHistoryType.setImageResource(R.drawable.ic_personal);
                historyType.setText(R.string.per);
                detailOneLabel.setText(R.string.personal_type);
                detailOne.setText(history.typePersonal);
                detailTwoLabel.setText(R.string.description_field);
                detailTwo.setText(history.description);
                break;

            case "Medicamentos":
                icHistoryType.setImageResource(R.drawable.ic_pill);
                historyType.setText(R.string.med);
                detailOneLabel.setText(R.string.med_field);
                detailOne.setText(history.med);
                detailTwoLabel.setText(R.string.via_field);
                detailTwo.setText(history.via);
                detailThreeLabel.setVisibility(View.VISIBLE);
                detailThreeLabel.setText(R.string.dateini_field);
                detailThree.setVisibility(View.VISIBLE);
                detailThree.setText(history.dateIni);
                break;
        }
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
            JSONArray historyArray = hmt.getJsonFromString(resp);
            historyAPI = hmt.buildHistoryObject(historyArray);
            //patient = hmt.getPatientDataFromJson(resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
