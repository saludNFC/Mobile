package app.example.veuge.com.saludnfc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.example.veuge.com.saludnfc.R;
import app.example.veuge.com.saludnfc.models.History;
import app.example.veuge.com.saludnfc.views.HistoryDetail;

/**
 * Created by veuge on 22-06-16.
 */
public class HistoriesAdapter extends RecyclerView.Adapter <HistoriesAdapter.ViewHolderHistories> {

    private LayoutInflater layoutInflater;
    private History[] historiesList;

    public HistoriesAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    public void  setHistoriesList(History[] historiesList){
        this.historiesList = historiesList;
    }

    @Override
    public ViewHolderHistories onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.history_row, parent, false);
        ViewHolderHistories viewHolder = new ViewHolderHistories(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderHistories holder, int position) {
        final History currentHistory = historiesList[position];

        switch (currentHistory.historyType){
            case "Familiar":
                holder.historyType.setImageResource(R.drawable.ic_familiar);
                holder.detailOne.setText(currentHistory.grade);
                holder.detailTwo.setText(currentHistory.illness);
                break;
            case "Personal":
                holder.historyType.setImageResource(R.drawable.ic_personal);
                holder.detailOne.setText(currentHistory.typePersonal);
                holder.detailTwo.setText(currentHistory.description);
                break;
            case "Medicamentos":
                holder.historyType.setImageResource(R.drawable.ic_pill);
                holder.detailOne.setText(currentHistory.med);
                holder.detailTwo.setText(currentHistory.via);
                break;
        }

        holder.createdAt.setText(currentHistory.createdAt);

        holder.detailOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, HistoryDetail.class);
                intent.putExtra("HISTORY", currentHistory);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historiesList.length;
    }

    static class ViewHolderHistories extends RecyclerView.ViewHolder{

        private ImageView historyType;
        private TextView createdAt;
        private TextView detailOne;
        private TextView detailTwo;

        public ViewHolderHistories(View itemView) {
            super(itemView);

            historyType = (ImageView) itemView.findViewById(R.id.history_type);
            createdAt = (TextView) itemView.findViewById(R.id.created_at);
            detailOne = (TextView) itemView.findViewById(R.id.detail_one);
            detailTwo = (TextView) itemView.findViewById(R.id.detail_two);
        }
    }
}
