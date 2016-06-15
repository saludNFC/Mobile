package app.example.veuge.com.saludnfc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import app.example.veuge.com.saludnfc.models.Consultation;

public class ConsultationActivity extends AppCompatActivity {

    public static String codHC, codConsulta, token;
    //public HashMap[] contacts;
    public Consultation[] consultation;

    public TextView consultationTitle;
    public ViewGroup consultationMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation);

        consultationMain = (ViewGroup) findViewById((R.id.consultation_main));
        consultationTitle = (TextView) findViewById(R.id.consultation_title);

        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia_clinica");
        codConsulta = intent.getStringExtra("consultaID");
        token = intent.getStringExtra("token");

        String url = ((Variables) this.getApplication()).getUrl();
        String path = "api/paciente/" + codHC + "/consultas/" + codConsulta;

        String resp;
        //HashMapTransformation hmt = new HashMapTransformation(contacts);
        ObjectTransformation hmt = new ObjectTransformation();

        try{
            GetAsyncTask gat = new GetAsyncTask(url, path, token);
            gat.execute();
            resp = gat.get();
            JSONArray consultationArray = hmt.getJsonFromString(resp);
            consultation = hmt.buildConsultationObject(consultationArray);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        if(consultation != null){
            consultationTitle.setText("Detalle de Consulta Médica " + consultation[0].id);
            Context context = getApplicationContext();

            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Fecha registro:", consultation[0].createdAt);
            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Anamnesis:", consultation[0].anamnesis);
            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Examen físico:", consultation[0].physicalExam);
            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Diagnóstico:", consultation[0].diagnosis);
            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Tratamiento:", consultation[0].treatment);
            ((Variables)this.getApplication()).insertViews(context, consultationMain, "Justificación:", consultation[0].justification);
        }
    }
}
