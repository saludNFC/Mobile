package app.example.veuge.com.saludnfc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.example.veuge.com.saludnfc.R;

/**
 * Created by veuge on 21-06-16.
 */
public class ButtonsAdapter extends BaseAdapter {
    Context mContext;
    private Integer[] mThumbIds = {
            R.drawable.ic_card, R.drawable.ic_contact, R.drawable.ic_log,
            R.drawable.ic_history, R.drawable.ic_control, R.drawable.ic_consultation
    };

    private String[] mTexts = {
            "Carnet NFC", "Contacto", "Registro de\nActividades",
            "Antecedentes", "Controles", "Consultas\nMédicas"
    };

    public ButtonsAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.buttons, null);
            TextView textView = (TextView) grid.findViewById(R.id.button_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.button_image);

            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setColorFilter(R.color.primary_dark);

            textView.setText(mTexts[position]);
            imageView.setImageResource(mThumbIds[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
