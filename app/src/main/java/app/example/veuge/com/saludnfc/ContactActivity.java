package app.example.veuge.com.saludnfc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.models.Contact;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class ContactActivity extends AppCompatActivity {

    public static String codHC, token;
    //public HashMap[] contacts;
    public Contact[] contacts;

    public TextView contactTitle;
    public ViewGroup contactMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        contactMain = (ViewGroup) findViewById((R.id.contact_main));
        contactTitle = (TextView) findViewById(R.id.contact_title);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("patientHistory");
        token = intent.getStringExtra("token");

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/contacto";
        Log.i("CONTACT URL ", url + path);

        String resp;
        //HashMapTransformation hmt = new HashMapTransformation(contacts);
        ObjectTransformation hmt = new ObjectTransformation();

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();
            JSONArray contactArray = hmt.getJsonFromString(resp);
            contacts = hmt.buildContactObject(contactArray);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(contacts != null){
            contactTitle.setText("Contacto de emergencia");
            Context context = getApplicationContext();

            ((Variables)this.getApplication()).insertViews(context, contactMain, "Nombre:", contacts[0].name);
            ((Variables)this.getApplication()).insertViews(context, contactMain, "Apellido:", contacts[0].lastname);
            ((Variables)this.getApplication()).insertViews(context, contactMain, "Relación de parentesco:", contacts[0].relationship);
            ((Variables)this.getApplication()).insertViews(context, contactMain, "Teléfono:", contacts[0].phone);
        }
    }
}
