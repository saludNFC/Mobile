package app.example.veuge.com.saludnfc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by veuge on 28-05-16.
 */
public class HashMapTransformation {

    public HashMap[] result;

    public HashMapTransformation(HashMap[] res) {
        this.result = res;
    }

    public JSONArray getJsonFromString(String jsonStr) throws JSONException{
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

    public HashMap[] buildPatientHashmap(JSONArray patientDetails) throws JSONException{

        final String OWM_ID = "identificador_paciente";
        final String OWM_HC = "historia_clinica";
        final String OWM_CI = "ci";
        final String OWM_EMISION = "emision";
        final String OWM_NAME = "nombre";
        final String OWM_LASTNAME = "apellido";
        final String OWM_GENDER = "sexo";
        final String OWM_BIRTHDAY = "fecha_nacimiento";
        final String OWM_BIRTHPLACE = "lugar_nacimiento";
        final String OWM_INSTRUCTION = "grado_instruccion";
        final String OWM_CIVILSTATUS = "estado_civil";
        final String OWM_OCUPATION = "ocupacion";
        final String OWM_BLOODTYPE = "grupo_sanguineo";

        HashMap[] patients = new HashMap[patientDetails.length()];

        for(int i = 0; i < patientDetails.length(); i++){
            HashMap patient = new HashMap();
            JSONObject patientObject = patientDetails.getJSONObject(i);

            patient.put(0, patientObject.getString(OWM_ID));
            patient.put(1, patientObject.getString(OWM_HC));
            patient.put(2, patientObject.getString(OWM_CI));
            patient.put(3, patientObject.getString(OWM_EMISION));
            patient.put(4, patientObject.getString(OWM_NAME));
            patient.put(5, patientObject.getString(OWM_LASTNAME));
            patient.put(6, patientObject.getString(OWM_GENDER));
            patient.put(7, patientObject.getString(OWM_BIRTHDAY));
            patient.put(8, patientObject.getString(OWM_BIRTHPLACE));
            patient.put(9, patientObject.getString(OWM_INSTRUCTION));
            patient.put(10, patientObject.getString(OWM_CIVILSTATUS));
            patient.put(11, patientObject.getString(OWM_OCUPATION));
            patient.put(12, patientObject.getString(OWM_BLOODTYPE));

            patients[i] = patient;
        }
        return patients;
    }

    public HashMap[] buildHistoryHashmap(JSONArray historyDetails) throws JSONException{
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

        HashMap[] histories = new HashMap[historyDetails.length()];
        for(int i = 0; i < historyDetails.length(); i++){
            HashMap history = new HashMap();
            JSONObject historyObject = historyDetails.getJSONObject(i);

            history.put(0, historyObject.getString(OWM_PATIENT));
            history.put(1, historyObject.getString(OWM_ID));
            history.put(2, historyObject.getString(OWM_TYPE));

            switch (historyObject.get(OWM_TYPE).toString()){
                case "Familiar":
                    history.put(3, historyObject.getString(OWM_GRADE));
                    history.put(4, historyObject.getString(OWM_ILLNESS));
                    break;

                case "Personal":
                    history.put(3, historyObject.getString(OWM_PERSONAL_TYPE));
                    history.put(4, historyObject.getString(OWM_DESC));
                    break;
                case "Medicamentos":
                    history.put(3, historyObject.getString(OWM_MED));
                    history.put(4, historyObject.getString(OWM_VIA));
                    history.put(5, historyObject.getString(OWM_DATE_INI));
                    break;
            }
            histories[i] = history;
        }
        return histories;
    }

    public HashMap[] buildControlHashmap(JSONArray controlDetails) throws JSONException{
        final String OWM_ID = "identificador_control";
        final String OWM_TYPE = "tipo_control";

        final String OWM_VACCINE = "vacuna";
        final String OWM_VIA = "via_administracion";
        final String OWM_DOSIS = "dosis";

        final String OWM_WEIGHT = "peso";
        final String OWM_HEIGHT = "altura";

        final String OWM_TEMPERATURE = "temperatura";
        final String OWM_HEARTRATE = "frecuencia_cardiaca";
        final String OWM_SISTOLE = "sistole";
        final String OWM_DIASTOLE = "diastole";

        final String OWM_LASTMENST = "ultima_menstruacion";
        final String OWM_LASTMAMOG = "ultima_mamografia";
        final String OWM_SEXACT = "vida_sexual";
        final String OWM_LASTPAPAN = "ultimo_papanicolau";

        final String OWM_GER_TYPE = "tipo_valoracion";
        final String OWM_NOTES = "descripcion";

        HashMap[] controls = new HashMap[controlDetails.length()];
        for(int i = 0; i < controlDetails.length(); i++){
            HashMap control = new HashMap();
            JSONObject controlObject = controlDetails.getJSONObject(i);

            control.put(1, controlObject.getString(OWM_ID));
            control.put(2, controlObject.getString(OWM_TYPE));

            switch (controlObject.get(OWM_TYPE).toString()){
                case "Vacunacion":
                    control.put(3, controlObject.getString(OWM_VACCINE));
                    control.put(4, controlObject.getString(OWM_VIA));
                    control.put(5, controlObject.getString(OWM_DOSIS));
                    break;

                case "Crecimiento":
                    control.put(3, controlObject.getString(OWM_WEIGHT));
                    control.put(4, controlObject.getString(OWM_HEIGHT));
                    break;
                case "Triaje":
                    control.put(3, controlObject.getString(OWM_TEMPERATURE));
                    control.put(4, controlObject.getString(OWM_HEARTRATE));
                    control.put(5, controlObject.getString(OWM_SISTOLE));
                    control.put(6, controlObject.getString(OWM_DIASTOLE));
                    break;
                case "Ginecologico":
                    control.put(3, controlObject.getString(OWM_LASTMENST));
                    control.put(4, controlObject.getString(OWM_LASTMAMOG));
                    control.put(5, controlObject.getString(OWM_SEXACT));
                    control.put(6, controlObject.getString(OWM_LASTPAPAN));
                    break;
                case "Geriatrico":
                    control.put(3, controlObject.getString(OWM_GER_TYPE));
                    control.put(4, controlObject.getString(OWM_NOTES));
                    break;
            }
            controls[i] = control;
        }
        return controls;
    }

    public HashMap[] buildConsultationHashmap(JSONArray consultationArray) throws JSONException{
        final String OWM_ID = "identificador_consulta";
        final String OWM_ANAMNESIS = "anamnesis";
        final String OWM_PHYSICAL_EXAM = "examen_físico";
        final String OWM_DIAGNOSIS = "diagnostico";
        final String OWM_TREATMENT = "tratamiento";
        final String OWM_JUSTIFICATION = "justificacion";

        HashMap[] consultations = new HashMap[consultationArray.length()];
        for(int i = 0; i < consultationArray.length(); i++){
            HashMap consultation = new HashMap();

            JSONObject consultationObject = consultationArray.getJSONObject(i);

            consultation.put(1, consultationObject.getString(OWM_ID));
            consultation.put(2, consultationObject.getString(OWM_ANAMNESIS));
            consultation.put(3, consultationObject.getString(OWM_PHYSICAL_EXAM));
            consultation.put(4, consultationObject.getString(OWM_DIAGNOSIS));
            consultation.put(5, consultationObject.getString(OWM_TREATMENT));
            consultation.put(6, consultationObject.getString(OWM_JUSTIFICATION));

            consultations[i] = consultation;
        }
        return consultations;
    }

    public HashMap buildLoginHashmap (JSONArray loginArray) throws JSONException{
        final String OWM_TOKEN = "token";
        final String OWM_MESSAGE = "message";
        final String OWM_STATUS_CODE = "status_code";

        HashMap login = new HashMap();
        JSONObject loginObject = loginArray.getJSONObject(0);

        try{
            login.put(1, loginObject.getString(OWM_TOKEN));
        }
        catch (Exception e){
            login.put(1, loginObject.getString(OWM_MESSAGE));
            login.put(2, loginObject.getString(OWM_STATUS_CODE));
        }
        return login;
    }
}
