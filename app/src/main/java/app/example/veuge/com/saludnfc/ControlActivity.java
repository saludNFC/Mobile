package app.example.veuge.com.saludnfc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.HashMap;

import app.example.veuge.com.saludnfc.models.Control;

public class ControlActivity extends AppCompatActivity {

    public static String codHC, controlID, token;
    private final String LOG_TAG = HistoryActivity.class.getSimpleName();
    public Control[] control;
    public TextView controlTitle;
    public ViewGroup controlMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control);

        controlMain = (ViewGroup) findViewById((R.id.control_main));
        controlTitle = (TextView) findViewById(R.id.control_title);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia_clinica");
        controlID = intent.getStringExtra("control");
        token = intent.getStringExtra("token");

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/controles/" + controlID;
        Log.i(LOG_TAG, "URL => " + url + path);

        String resp;
        ObjectTransformation hmt = new ObjectTransformation();

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();
            JSONArray controlArray = hmt.getJsonFromString(resp);
            Log.i(LOG_TAG, "json array => " + controlArray);
            control = hmt.buildControlObject(controlArray);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(control != null){
            controlTitle.setText("Control " + control[0].id);
            Context context = getApplicationContext();

            ((Variables)this.getApplication()).insertViews(context, controlMain, "Tipo:", control[0].controlType);

            if((control[0].controlType).equals("Vacunacion")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Vacuna:", control[0].vaccine);
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Via administración:", control[0].viaVac);
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Dosis:", control[0].dosis);
            }
            else if((control[0].controlType).equals("Crecimiento")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Peso: (Kg)", control[0].weight);
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Altura: (cm)", control[0].height);
            }
            else if((control[0].controlType).equals("Triaje")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Temperatura: (°C)", control[0].temperature);
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Frecuencia cardiaca:", control[0].heartRate);
                String blood_pressure = control[0].sistole.concat(" / ").concat(control[0].diastole);
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Presión arterial:", blood_pressure);
            }
            else if((control[0].controlType).equals("Ginecologico")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Última menstruación:", control[0].lastMenst);
                if(! control[0].lastMamo.equals("")){
                    ((Variables)this.getApplication()).insertViews(context, controlMain, "Última mamografía:", control[0].lastMamo);
                }
                if(! control[0].sex.equals("")){
                    ((Variables)this.getApplication()).insertViews(context, controlMain, "Vida sexual activa:", control[0].sex);
                }
                if(! control[0].lastPapa.equals("")){
                    ((Variables)this.getApplication()).insertViews(context, controlMain, "Último papanicolau", control[0].lastPapa);
                }
            }
            else if((control[0].controlType).equals("Geriatrico")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Tipo de valoración:", control[0].geriatric);
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Descripción:", control[0].notes);
            }
        }
    }
}
