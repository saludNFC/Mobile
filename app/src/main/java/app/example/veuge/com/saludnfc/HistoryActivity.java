package app.example.veuge.com.saludnfc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {

    public static String codHC, antecedente, token;
    private final String LOG_TAG = HistoryActivity.class.getSimpleName();
    public HashMap[] history;
    public TextView historyTitle;
    public ViewGroup historyMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyMain = (ViewGroup) findViewById((R.id.history_main));
        historyTitle = (TextView) findViewById(R.id.history_title);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia_clinica");
        antecedente = intent.getStringExtra("antecedente");
        token = intent.getStringExtra("token");

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/antecedentes/" + antecedente;

        String resp;
        HashMapTransformation hmt = new HashMapTransformation(history);

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();
            JSONArray historyArray = hmt.getJsonFromString(resp);
            history = hmt.buildHistoryHashmap(historyArray);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(history != null){
            historyTitle.setText("Antedecente " + history[0].get(1).toString());
            //historyType.setText(history[0].get(2).toString());
            Context context = getApplicationContext();

            ((Variables)this.getApplication()).insertViews(context, historyMain, "Tipo:", history[0].get(2).toString());

            if((history[0].get(2).toString()).equals("Familiar")){
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Grado parentesco:", history[0].get(3).toString());
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Enfermedad:", history[0].get(4).toString());
            }
            else if((history[0].get(2).toString()).equals("Personal")){
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Tipo personal:", history[0].get(3).toString());
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Descripción:", history[0].get(4).toString());
            }
            else if((history[0].get(2).toString()).equals("Medicamentos")){
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Medicina:", history[0].get(3).toString());
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Via administración:", history[0].get(4).toString());
                ((Variables)this.getApplication()).insertViews(context, historyMain, "Fecha inicio:", history[0].get(5).toString());
            }
        }
    }
}
