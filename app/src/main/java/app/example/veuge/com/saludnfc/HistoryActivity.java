package app.example.veuge.com.saludnfc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.models.History;
import app.example.veuge.com.saludnfc.network.GetAsyncTask;

public class HistoryActivity extends AppCompatActivity {

    public static String codHC, token;
    public static int antecedente;
    private final String LOG_TAG = HistoryActivity.class.getSimpleName();
    //public HashMap[] history;
    public History[] history;
    public TextView historyTitle;
    public ViewGroup historyMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        historyMain = (ViewGroup) findViewById((R.id.history_main));
        historyTitle = (TextView) findViewById(R.id.history_title);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia_clinica");
        antecedente = intent.getIntExtra("antecedente", -1);
        token = intent.getStringExtra("token");

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/antecedentes/" + antecedente;

        String resp;
        ObjectTransformation hmt = new ObjectTransformation();

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();
            JSONArray historyArray = hmt.getJsonFromString(resp);
            history = hmt.buildHistoryObject(historyArray);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(history != null){
            historyTitle.setText("Antedecente " + history[0].id);
            //historyType.setText(history[0].get(2).toString());
            Context context = getApplicationContext();

            ((Variables)this.getApplication()).insertViews(context, historyMain, "Tipo:", history[0].historyType);
            ((Variables)this.getApplication()).insertViews(context, historyMain, "Fecha registro:", history[0].createdAt);

            if((history[0].historyType).equals("Familiar")){
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Grado parentesco:", history[0].grade);
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Enfermedad:", history[0].illness);
            }
            else if((history[0].historyType).equals("Personal")){
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Tipo personal:", history[0].typePersonal);
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Descripción:", history[0].description);
            }
            else if((history[0].historyType).equals("Medicamentos")){
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Medicina:", history[0].med);
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Via administración:", history[0].via);
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Fecha inicio:", history[0].dateIni);
            }
        }
    }
}
