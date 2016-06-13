package app.example.veuge.com.saludnfc;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by veuge on 28-05-16.
 */
public class Variables extends Application {
    private String url = "http://192.168.1.164:8000/";
    //private String url = "http://192.168.43.4:8000/";

    public String getUrl(){
        return url;
    }

    public void insertViews(Context context, ViewGroup father, String label, String text){
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        container.setWeightSum(1f);

        TextView labelTV = new TextView(context);
        labelTV.setGravity(Gravity.LEFT);
        labelTV.setTypeface(null, Typeface.BOLD);
        labelTV.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        (float) 0.5
                )
        );
        labelTV.setTextColor(Color.parseColor("#000000"));
        labelTV.setText(label);

        TextView textTV = new TextView(context);
        textTV.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        (float) 0.5
                )
        );
        textTV.setGravity(Gravity.RIGHT);
        textTV.setTextColor(Color.parseColor("#000000"));
        textTV.setText(text);

        container.addView(labelTV);
        container.addView(textTV);
        father.addView(container);
    }
}
