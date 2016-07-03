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
import app.example.veuge.com.saludnfc.models.Control;
import app.example.veuge.com.saludnfc.views.ControlDetail;

/**
 * Created by veuge on 22-06-16.
 */
public class ControlsAdapter extends RecyclerView.Adapter <ControlsAdapter.ViewHolderControls> {
    private LayoutInflater layoutInflater;
    private List<Control> controlsList;

    public ControlsAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    public void setControlsList(List<Control> controlsList){
        this.controlsList = controlsList;
    }

    @Override
    public ViewHolderControls onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.control_row, parent, false);
        ViewHolderControls viewHolder = new ViewHolderControls(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderControls holder, int position) {
        final Control currentControl = controlsList.get(position);

        switch (currentControl.controlType){
            case "Vacunacion":
                holder.controlType.setImageResource(R.drawable.ic_vaccine);
                holder.detailOne.setText(currentControl.vaccine);
                holder.detailTwo.setText(currentControl.viaVac);
                break;
            case "Crecimiento":
                holder.controlType.setImageResource(R.drawable.ic_growth);
                holder.detailOne.setText(currentControl.weight + " Kg");
                holder.detailTwo.setText(currentControl.height + " cm");
                break;
            case "Triaje":
                holder.controlType.setImageResource(R.drawable.ic_thermometer);
                holder.detailOne.setText(currentControl.temperature + " °C");
                holder.detailTwo.setText(currentControl.systole + " / " + currentControl.diastole);
                break;
            case "Ginecologico":
                holder.controlType.setImageResource(R.drawable.ic_gine);
                holder.detailOne.setText("Ultima menstruación " + currentControl.lastMenst);
                holder.detailTwo.setText("Ultima mamografía " + currentControl.lastMamo);
                break;
            case "Geriatrico":
                holder.controlType.setImageResource(R.drawable.ic_geriatric);
                holder.detailOne.setText(currentControl.geriatric);
                holder.detailTwo.setText(currentControl.notes);
                break;
        }

        holder.createdAt.setText(currentControl.createdAt);

        holder.detailOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ControlDetail.class);
                intent.putExtra("CONTROL", currentControl);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return controlsList.size();
    }

    static class ViewHolderControls extends RecyclerView.ViewHolder{

        private ImageView controlType;
        private TextView detailOne;
        private TextView detailTwo;
        private TextView createdAt;

        public ViewHolderControls(View itemView) {
            super(itemView);

            controlType = (ImageView) itemView.findViewById(R.id.control_type);
            detailOne = (TextView) itemView.findViewById(R.id.detail_one);
            detailTwo = (TextView) itemView.findViewById(R.id.detail_two);
            createdAt = (TextView) itemView.findViewById(R.id.created_at);
        }
    }
}
