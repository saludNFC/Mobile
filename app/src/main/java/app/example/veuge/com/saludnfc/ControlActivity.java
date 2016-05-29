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

public class ControlActivity extends AppCompatActivity {

    public static String codHC, controlID;
    private final String LOG_TAG = HistoryActivity.class.getSimpleName();
    public HashMap[] control;
    public TextView controlTitle;
    public ViewGroup controlMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        controlMain = (ViewGroup) findViewById((R.id.control_main));
        controlTitle = (TextView) findViewById(R.id.control_title);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia_clinica");
        controlID = intent.getStringExtra("control");

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/controles/" + controlID;
        Log.i(LOG_TAG, "URL => " + url + path);

        String resp;
        HashMapTranformation hmt = new HashMapTranformation(control);

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path);
            gat.execute();
            resp = gat.get();
            JSONArray controlArray = hmt.getJsonFromString(resp);
            Log.i(LOG_TAG, "json array => " + controlArray);
            control = hmt.buildControlHashmap(controlArray);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(control != null){
            controlTitle.setText("Control " + control[0].get(1).toString());
            Context context = getApplicationContext();

            ((Variables)this.getApplication()).insertViews(context, controlMain, "Tipo:", control[0].get(2).toString());

            if((control[0].get(2).toString()).equals("Vacunacion")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Vacuna:", control[0].get(3).toString());
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Via administración:", control[0].get(4).toString());
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Dosis:", control[0].get(5).toString());
            }
            else if((control[0].get(2).toString()).equals("Crecimiento")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Peso:", control[0].get(3).toString());
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Altura:", control[0].get(4).toString());
            }
            else if((control[0].get(2).toString()).equals("Triaje")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Temperatura:", control[0].get(3).toString());
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Frecuencia cardiaca:", control[0].get(4).toString());
                String blood_pressure = control[0].get(5).toString().concat(" / ").concat(control[0].get(6).toString());
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Presión arterial:", blood_pressure);
            }
            else if((control[0].get(2).toString()).equals("Ginecologico")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Última menstruación:", control[0].get(3).toString());
                if(control[0].get(4) != null){
                    ((Variables)this.getApplication()).insertViews(context, controlMain, "Última mamografía:", control[0].get(4).toString());
                }
                if(control[0].get(5) != null){
                    ((Variables)this.getApplication()).insertViews(context, controlMain, "Vida sexual activa:", control[0].get(5).toString());
                }
                if(control[0].get(6) != null){
                    ((Variables)this.getApplication()).insertViews(context, controlMain, "Último papanicolau", control[0].get(6).toString());
                }
            }
            else if((control[0].get(2).toString()).equals("Geriatrico")){
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Tipo de valoración:", control[0].get(3).toString());
                ((Variables)this.getApplication()).insertViews(context, controlMain, "Descripción:", control[0].get(4).toString());
            }
        }
    }
}
