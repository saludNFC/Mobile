package app.example.veuge.com.saludnfc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

import app.example.veuge.com.saludnfc.models.Consultation;
import app.example.veuge.com.saludnfc.models.History;

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

    public History[] buildHistoryObject(JSONArray historyDetails) throws JSONException{
        final String OWM_PATIENT = "identificador_paciente";
        final String OWM_ID = "identificador_antecedente";
        final String OWM_TYPE = "tipo_antecedente";
        final String OWM_GRADE = "grado_parentesco";
        final String OWM_ILLNESS = "enfermedad";
        final String OWM_PERSONAL_TYPE = "tipo_personal";
        final String OWM_DESC = "descripcion";
        final String OWM_MED = "medicamento";
        final String OWM_VIA = "via_administracion";
        final String OWM_DATE_INI = "fecha_inicio";

        String id = "", type = "", grade = "", illness = "", ptype = "", desc = "", med = "", via = "", date = "";

        History[] histories = new History[historyDetails.length()];
        for(int i = 0; i < historyDetails.length(); i++){

            JSONObject historyObject = historyDetails.getJSONObject(i);

            id = historyObject.getString(OWM_ID);
            type = historyObject.getString(OWM_TYPE);

            switch (historyObject.get(OWM_TYPE).toString()){
                case "Familiar":
                    grade = historyObject.getString(OWM_GRADE);
                    illness = historyObject.getString(OWM_ILLNESS);
                    break;

                case "Personal":
                    ptype = historyObject.getString(OWM_PERSONAL_TYPE);
                    desc = historyObject.getString(OWM_DESC);
                    break;
                case "Medicamentos":
                    med = historyObject.getString(OWM_MED);
                    via = historyObject.getString(OWM_VIA);
                    date = historyObject.getString(OWM_DATE_INI);
                    break;
            }
            History history = new History(id, type, grade, illness, ptype, desc, med, via, date);
            histories[i] = history;
        }
        return histories;
    }
}
