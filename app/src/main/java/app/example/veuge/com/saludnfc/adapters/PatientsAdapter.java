package app.example.veuge.com.saludnfc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.models.Patient;
import app.example.veuge.com.saludnfc.views.PatientDetail;

/**
 * Created by veuge on 20-06-16.
 */

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolderPatients> {

    private LayoutInflater layoutInflater;
    private List<Patient> patientsList;

    public PatientsAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    public void  setPatientsList(List<Patient> patientsList){
        this.patientsList = patientsList;
    }

    @Override
    public ViewHolderPatients onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.patient_row, parent, false);
        ViewHolderPatients viewHolder = new ViewHolderPatients(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderPatients holder, int position) {
        final Patient currentPatient = patientsList.get(position);
        if(currentPatient.sexo.equals("Femenino")){
            holder.patientCard.setImageResource(R.drawable.ic_ufemale);
        }
        else {
            holder.patientCard.setImageResource(R.drawable.ic_umale);
        }
        holder.patientName.setText(currentPatient.nombre + " " + currentPatient.apellido);
        holder.patientHcode.setText(currentPatient.historia);

        holder.patientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PatientDetail.class);
                intent.putExtra("PATIENT", currentPatient);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }

    static class ViewHolderPatients extends RecyclerView.ViewHolder{
        private ImageView patientCard;
        private TextView patientName;
        private TextView patientHcode;

        public ViewHolderPatients(View itemView) {
            super(itemView);

            patientCard = (ImageView) itemView.findViewById(R.id.patient_card);
            patientName = (TextView) itemView.findViewById(R.id.patient_name);
            patientHcode = (TextView) itemView.findViewById(R.id.patient_hcode);
        }
    }
}