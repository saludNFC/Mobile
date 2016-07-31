package app.example.veuge.com.saludnfc;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by veuge on 28-05-16.
 */
public class Variables extends Application {
    private String url = "http://192.168.1.159:8000/"; // Router WiFi
    //  private String url = "http://192.168.1.164:8000/";  // Router
    //  private String url = "http://192.168.43.4:8000/"; // Hotspot tethering with cellphone
    //  private String url = "http://192.168.42.132:8000/"; // USB tethering with cellphone

    /**
     * There must be a better way to handle the auth token
     * Perhaps with the AccountManager class?
     * By now I will store the token as a global variable simple with a string
     * MEH
     */
    private String token;

    public String getUrl(){
        return url;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String tok){
        this.token = tok;
    }

    public void calendarView(final EditText et){

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int day = c.get(Calendar.DAY_OF_MONTH);
                final int month = c.get(Calendar.MONTH);
                final int year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                et.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
    }
}
