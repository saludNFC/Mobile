package app.example.veuge.com.saludnfc;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by veuge on 30-05-16.
 */
public class CustomListener implements AdapterView.OnItemSelectedListener {

    public LinearLayout[] linearLayouts;
    public String selected;
    public CustomListener(LinearLayout... params){
        this.linearLayouts = params;
        selected = "";
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//        Toast.makeText(parent.getContext(),
//                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
//                Toast.LENGTH_LONG).show();

        this.selected = parent.getItemAtPosition(pos).toString();
//        Toast.makeText(parent.getContext(),
//                "SELECTED ITEM: " + selected,
//                Toast.LENGTH_LONG).show();

        /**
         * Makes visible the layout according to the item selected in the spinner
         */
        for (int i = 0; i < linearLayouts.length; i++){
            if(i == pos - 1){
                linearLayouts[i].setVisibility(View.VISIBLE);
            }
            else{
                linearLayouts[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public String getSelected(){
        return this.selected;
    }
}
