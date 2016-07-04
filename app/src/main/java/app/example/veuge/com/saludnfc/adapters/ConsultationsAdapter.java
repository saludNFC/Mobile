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
import app.example.veuge.com.saludnfc.models.Consultation;
import app.example.veuge.com.saludnfc.views.ConsultationDetail;

/**
 * Created by veuge on 22-06-16.
 */
public class ConsultationsAdapter extends RecyclerView.Adapter <ConsultationsAdapter.ViewHolderConsultations>{
    private LayoutInflater layoutInflater;
    private List<Consultation> consultationsList;

    public ConsultationsAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setConsultationsList(List<Consultation> consultationsList) {
        this.consultationsList = consultationsList;
    }

    @Override
    public ViewHolderConsultations onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.consultation_row, parent, false);
        ViewHolderConsultations viewHolder = new ViewHolderConsultations(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderConsultations holder, int position) {
        final Consultation currentConsultation = consultationsList.get(position);

        holder.consultation.setImageResource(R.drawable.ic_stethoscope);
        holder.anamnesis.setText(currentConsultation.anamnesis);
        holder.diagnosis.setText(currentConsultation.diagnosis);
        holder.createdAt.setText(currentConsultation.createdAt);

        holder.anamnesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ConsultationDetail.class);
                intent.putExtra("CONSULTATION", currentConsultation);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consultationsList.size();
    }

    static class ViewHolderConsultations extends RecyclerView.ViewHolder {

        private ImageView consultation;
        private TextView anamnesis;
        private TextView diagnosis;
        private TextView createdAt;

        public ViewHolderConsultations(View itemView) {
            super(itemView);

            consultation = (ImageView) itemView.findViewById(R.id.consultation);
            anamnesis = (TextView) itemView.findViewById(R.id.anamnesis);
            diagnosis = (TextView) itemView.findViewById(R.id.diagnosis);
            createdAt = (TextView) itemView.findViewById(R.id.created_at);
        }
    }
}
