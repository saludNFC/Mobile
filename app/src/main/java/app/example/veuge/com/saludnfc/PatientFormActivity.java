package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;

public class PatientFormActivity extends AppCompatActivity {
    String [] em;

    public static String codHC, ci, emision, nombre, apellido, sexo, fecha_nac,
        lugar_nac, grado, estado, ocupacion, grupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);

        Spinner sp = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ci_emision_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp.setAdapter(adapter);


        Intent intent = getIntent();
        codHC = intent.getStringExtra("historia");
        ci = intent.getStringExtra("ci");
        emision = intent.getStringExtra("emision");
        nombre = intent.getStringExtra("nombre");
        apellido = intent.getStringExtra("apellido");
        sexo = intent.getStringExtra("sexo");
        fecha_nac = intent.getStringExtra("fecha_nacimiento");
        lugar_nac = intent.getStringExtra("lugar_nacimiento");
        grado = intent.getStringExtra("grado_instruccion");
        estado = intent.getStringExtra("estado_civil");
        ocupacion = intent.getStringExtra("ocupacion");
        grupo = intent.getStringExtra("grupo_sanguineo");

        EditText ci_field = (EditText) findViewById(R.id.ci_field);
        ci_field.setText(ci);

        sp.setSelection(findIndex(emision));
    }

    private int findIndex(String emision) {
        em = getResources().getStringArray(R.array.ci_emision_options);
        int index = -1;
        for(int i = 0; i < em.length; i++){
            if(em[i] == emision){
                index = i;
                break;
            }
        }
        return index;
    }
}
