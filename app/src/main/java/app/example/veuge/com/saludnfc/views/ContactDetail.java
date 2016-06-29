package app.example.veuge.com.saludnfc.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.ObjectTransformation;
import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.Variables;
import app.example.veuge.com.saludnfc.models.Contact;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class ContactDetail extends AppCompatActivity {

    private TextView name;
    private TextView lastname;
    private TextView relationship;
    private TextView phone;
    private ImageView call;
    private Context context;

    private Contact[] contactAPI;
    //  private ControlsAdapter adapter;

    private String codHC;
    private int patientID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);

        Intent intent = getIntent();
        context = this.getBaseContext();
        codHC = intent.getStringExtra("PATIENT_CODE");

        setupUI();
        getContactFromAPI();
        fillDetails();
    }

    public void setupUI() {
        name = (TextView) findViewById(R.id.contact_name);
        lastname = (TextView) findViewById(R.id.contact_lastname);
        relationship = (TextView) findViewById(R.id.contact_relationship);
        phone = (TextView) findViewById(R.id.contact_phone);
        call = (ImageView) findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no = phone.getText().toString().replaceAll("-", "");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel: " + phone_no));
                //  callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });
    }

    public void fillDetails(){
        name.setText(contactAPI[0].name);
        lastname.setText(contactAPI[0].lastname);
        relationship.setText(contactAPI[0].relationship);
        phone.setText(contactAPI[0].phone);
    }

    public void getContactFromAPI() {
        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/contacto";

        String resp;
        ObjectTransformation hmt = new ObjectTransformation();

        try {
            GetAsyncTask gat = new GetAsyncTask(url, path, "");
            gat.execute();
            resp = gat.get();
            JSONArray contactArray = hmt.getJsonFromString(resp);
            contactAPI = hmt.buildContactObject(contactArray);
            //patient = hmt.getPatientDataFromJson(resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
