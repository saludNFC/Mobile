package app.example.veuge.com.saludnfc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

import app.example.veuge.com.saludnfc.models.Consultation;

/**
 * Created by veuge on 05-06-16.
 */
public class ObjectTransformation {

    public JSONArray getJsonFromString(String jsonStr) throws JSONException {
        final String OWM_DATA = "data";
        // Gets a JSONObject from the string passed.
        JSONObject jsonObject = new JSONObject(jsonStr);
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(OWM_DATA);
            return jsonArray;
        }
        catch (JSONException e){
            Iterator iterator = jsonObject.keys();
            JSONArray jsonArray = new JSONArray();

            while (iterator.hasNext()){
                String key = (String) iterator.next();
                jsonArray.put(jsonObject.get(key));
            }
            return jsonArray;
        }
    }

    public Consultation[] buildConsultationObject(JSONArray consultationArray) throws JSONException{
        final String OWM_ID = "identificador_consulta";
        final String OWM_ANAMNESIS = "anamnesis";
        final String OWM_PHYSICAL_EXAM = "examen_físico";
        final String OWM_DIAGNOSIS = "diagnostico";
        final String OWM_TREATMENT = "tratamiento";
        final String OWM_JUSTIFICATION = "justificacion";

        Consultation[] consultations = new Consultation[consultationArray.length()];

        for(int i = 0; i < consultationArray.length(); i++){
            JSONObject consultationObject = consultationArray.getJSONObject(i);

            String id = consultationObject.getString(OWM_ID);
            String anamnesis = consultationObject.getString(OWM_ANAMNESIS);
            String physicalE = consultationObject.getString(OWM_PHYSICAL_EXAM);
            String diagnosis = consultationObject.getString(OWM_DIAGNOSIS);
            String treatment = consultationObject.getString(OWM_TREATMENT);
            String justification = consultationObject.getString(OWM_JUSTIFICATION);

            Consultation consultation = new Consultation(id, anamnesis, physicalE, diagnosis, treatment, justification);
            consultations[i] = consultation;
        }
        return consultations;
    }
}
