package app.example.veuge.com.saludnfc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.HashMap;

public class ConsultationActivity extends AppCompatActivity {

    public static String codHC, codConsulta;
    private final String LOG_TAG = ConsultationActivity.class.getSimpleName();
    public HashMap[] consultation;
    public TextView consultationTitle;
    public ViewGroup consultationMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        consultationMain = (ViewGroup) findViewById((R.id.consultation_main));
        consultationTitle = (TextView) findViewById(R.id.consultation_title);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia_clinica");
        codConsulta = intent.getStringExtra("consultaID");

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/consultas/" + codConsulta;

        String resp;
        HashMapTransformation hmt = new HashMapTransformation(consultation);

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path);
            gat.execute();
            resp = gat.get();
            JSONArray consultationArray = hmt.getJsonFromString(resp);
            consultation = hmt.buildConsultationHashmap(consultationArray);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(consultation != null){
            consultationTitle.setText("Consulta Médica " + consultation[0].get(1).toString());
            Context context = getApplicationContext();

            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Anamnesis:", consultation[0].get(2).toString());
            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Examen físico:", consultation[0].get(3).toString());
            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Diagnóstico:", consultation[0].get(4).toString());
            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Tratamiento:", consultation[0].get(5).toString());
            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Justificación:", consultation[0].get(6).toString());
        }
    }
}
