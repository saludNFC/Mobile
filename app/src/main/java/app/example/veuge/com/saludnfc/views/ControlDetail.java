package app.example.veuge.com.saludnfc.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.adapters.ControlsAdapter;
import app.example.veuge.com.saludnfc.models.Control;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class ControlDetail extends AppCompatActivity {

    private ImageView icControlType;
    private TextView controlType;
    private TextView createdAt;
    private TextView detailOneLabel;
    private TextView detailOne;
    private TextView detailTwoLabel;
    private TextView detailTwo;
    private TextView detailThreeLabel;
    private TextView detailThree;
    private TextView detailFourLabel;
    private TextView detailFour;

    private Control control;
    private List<Control> controlAPI;
    private ControlsAdapter adapter;

    private String codHC;
    private int patientID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_detail);

        Intent intent = getIntent();
        control = (Control) intent.getSerializableExtra("CONTROL");

        controlAPI = new ArrayList<>();

        setupUI();
        fillDetails();
    }

    public void setupUI() {
        icControlType = (ImageView) findViewById(R.id.ic_control_type);
        controlType = (TextView) findViewById(R.id.control_type);
        createdAt = (TextView) findViewById(R.id.created_at);
        detailOneLabel = (TextView) findViewById(R.id.detail_one_label);
        detailOne = (TextView) findViewById(R.id.detail_one);
        detailTwoLabel = (TextView) findViewById(R.id.detail_two_label);
        detailTwo = (TextView) findViewById(R.id.detail_two);
        detailThreeLabel = (TextView) findViewById(R.id.detail_three_label);
        detailThree = (TextView) findViewById(R.id.detail_three);
        detailFourLabel = (TextView) findViewById(R.id.detail_four_label);
        detailFour = (TextView) findViewById(R.id.detail_four);
    }

    public void fillDetails(){
        createdAt.setText(control.createdAt);
        switch (control.controlType){
            case "Vacunacion":
                icControlType.setImageResource(R.drawable.ic_vaccine);
                controlType.setText(R.string.vac);
                detailOneLabel.setText(R.string.vaccine_field);
                detailOne.setText(control.vaccine);
                detailTwoLabel.setText(R.string.viav_field);
                detailTwo.setText(control.viaVac);
                detailThreeLabel.setVisibility(View.VISIBLE);
                detailThreeLabel.setText(R.string.dosis_field);
                detailThree.setVisibility(View.VISIBLE);
                detailThree.setText(control.dosis + "");
                break;

            case "Crecimiento":
                icControlType.setImageResource(R.drawable.ic_growth);
                controlType.setText(R.string.cre);
                detailOneLabel.setText(R.string.weight_field);
                detailOne.setText(control.weight + " Kg");
                detailTwoLabel.setText(R.string.height_field);
                detailTwo.setText(control.height + " cm");
                break;

            case "Triaje":
                icControlType.setImageResource(R.drawable.ic_thermometer);
                controlType.setText(R.string.tri);
                detailOneLabel.setText(R.string.temperature_field);
                detailOne.setText(control.temperature + " °C");
                detailTwoLabel.setText(R.string.heartrate_field);
                detailTwo.setText(control.heartRate + " latidos por minuto");
                detailThreeLabel.setVisibility(View.VISIBLE);
                detailThreeLabel.setText(R.string.pre_arterial);
                detailThree.setVisibility(View.VISIBLE);
                detailThree.setText(control.systole + " / " + control.diastole);
                break;

            case "Ginecologico":
                icControlType.setImageResource(R.drawable.ic_gine);
                controlType.setText(R.string.gin);
                detailOneLabel.setText(R.string.lastmenst_field);
                detailOne.setText(control.lastMenst);
                detailTwoLabel.setText(R.string.lastmamo_field);
                detailTwo.setText(control.lastMamo);
                detailThreeLabel.setVisibility(View.VISIBLE);
                detailThreeLabel.setText(R.string.sexact_field);
                detailThree.setVisibility(View.VISIBLE);
                detailThree.setText(control.sex + "");
                detailFourLabel.setVisibility(View.VISIBLE);
                detailFourLabel.setText(R.string.lastpapa_field);
                detailFour.setVisibility(View.VISIBLE);
                detailFour.setText(control.lastPapa);
                break;

            case "Geriatrico":
                icControlType.setImageResource(R.drawable.ic_geriatric);
                controlType.setText(R.string.ger);
                detailOneLabel.setText(R.string.geritype_field);
                detailOne.setText(control.geriatric);
                detailTwoLabel.setText(R.string.notes_field);
                detailTwo.setText(control.notes);
                break;
        }
    }

    public void getPatientFromAPI() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/controles";

        String resp;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, "");
            gat.execute();
            resp = gat.get();
            JSONArray controlArray = hmt.getJsonFromString(resp);
            controlAPI = hmt.buildControlObject(controlArray);
            //patient = hmt.getPatientDataFromJson(resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
