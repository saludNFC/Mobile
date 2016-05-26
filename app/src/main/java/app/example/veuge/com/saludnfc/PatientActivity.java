package app.example.veuge.com.saludnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Intent intent = getIntent();
        String message = intent.getStringExtra(PatientsActivity.EXTRA_TEXT);
        TextView tv = (TextView) findViewById(R.id.patient_name);
        tv.setText(message);
    }
}
