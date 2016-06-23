package app.example.veuge.com.saludnfc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import app.example.veuge.com.saludnfc.models.Consultation;
import app.example.veuge.com.saludnfc.models.Contact;
import app.example.veuge.com.saludnfc.models.Control;
import app.example.veuge.com.saludnfc.models.History;
import app.example.veuge.com.saludnfc.models.Patient;

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
        final String OWM_DATE = "fecha_creacion";

        Consultation[] consultations = new Consultation[consultationArray.length()];

        for(int i = 0; i < consultationArray.length(); i++){
            JSONObject consultationObject = consultationArray.getJSONObject(i);

            int id = consultationObject.getInt(OWM_ID);
            String anamnesis = consultationObject.getString(OWM_ANAMNESIS);
            String physicalE = consultationObject.getString(OWM_PHYSICAL_EXAM);
            String diagnosis = consultationObject.getString(OWM_DIAGNOSIS);
            String treatment = consultationObject.getString(OWM_TREATMENT);
            String justification = consultationObject.getString(OWM_JUSTIFICATION);
            String created = consultationObject.getString(OWM_DATE);

            Consultation consultation = new Consultation(id, anamnesis, physicalE, diagnosis, treatment, justification, created);
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
        final String OWM_DATE = "fecha_creacion";

        int id = -1;
        String type = "", grade = "", illness = "", ptype = "", desc = "", med = "", via = "", date = "",
                created = "";

        History[] histories = new History[historyDetails.length()];
        for(int i = 0; i < historyDetails.length(); i++){

            JSONObject historyObject = historyDetails.getJSONObject(i);

            id = historyObject.getInt(OWM_ID);
            type = historyObject.getString(OWM_TYPE);
            created = historyObject.getString(OWM_DATE);

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
            History history = new History(id, type, grade, illness, ptype, desc, med, via, date, created);
            histories[i] = history;
        }
        return histories;
    }

    public Control[] buildControlObject(JSONArray controlDetails) throws JSONException{
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

        final String OWM_DATE = "fecha_creacion";

        Control[] controls = new Control[controlDetails.length()];

        for(int i = 0; i < controlDetails.length(); i++){
            JSONObject controlObject = controlDetails.getJSONObject(i);
            int id = -1, dosis = -1, heartRate = -1, systole = -1, diastole = -1;
            float weight = -1, height = -1, temperature = -1;
            boolean sex = false;
            String type = "", vaccine = "", viaVac = "",
                    menst = "", mamo ="", papa = "",
                    typeGeri = "", notes = "",
                    ca = "";

            id = controlObject.getInt(OWM_ID);
            type = controlObject.getString(OWM_TYPE);
            ca = controlObject.getString(OWM_DATE);

            switch (controlObject.get(OWM_TYPE).toString()){
                case "Vacunacion":
                    vaccine = controlObject.getString(OWM_VACCINE);
                    viaVac = controlObject.getString(OWM_VIA);
                    dosis = controlObject.getInt(OWM_DOSIS);
                    break;

                case "Crecimiento":
                    weight = (float) controlObject.getDouble(OWM_WEIGHT);
                    height = (float) controlObject.getDouble(OWM_HEIGHT);
                    break;
                case "Triaje":
                    temperature = (float) controlObject.getDouble(OWM_TEMPERATURE);
                    heartRate = controlObject.getInt(OWM_HEARTRATE);
                    systole = controlObject.getInt(OWM_SISTOLE);
                    diastole = controlObject.getInt(OWM_DIASTOLE);
                    break;
                case "Ginecologico":
                    menst = controlObject.getString(OWM_LASTMENST);
                    mamo = controlObject.getString(OWM_LASTMAMOG);
                    sex = controlObject.getBoolean(OWM_SEXACT);
                    papa = controlObject.getString(OWM_LASTPAPAN);
                    break;
                case "Geriatrico":
                    typeGeri = controlObject.getString(OWM_GER_TYPE);
                    notes = controlObject.getString(OWM_NOTES);
                    break;
            }
            Control control = new Control(id, type, vaccine, viaVac, dosis, weight, height, temperature, heartRate,
                    systole, diastole, menst, mamo, sex, papa, typeGeri, notes, ca);
            controls[i] = control;
        }
        return controls;
    }

    public Contact[] buildContactObject(JSONArray contactArray) throws JSONException{
        final String OWM_ID = "identificador_contacto";
        final String OWM_NAME = "nombres";
        final String OWM_LASTNAME = "apellidos";
        final String OWM_RELATIONSHIP = "relacion_parentesco";
        final String OWM_PHONE = "telefono";

        Contact[] contacts = new Contact[contactArray.length()];

        for(int i = 0; i < contactArray.length(); i++){
            JSONObject contactObject = contactArray.getJSONObject(i);

            int id = contactObject.getInt(OWM_ID);
            String name = contactObject.getString(OWM_NAME);
            String lastname = contactObject.getString(OWM_LASTNAME);
            String relationship = contactObject.getString(OWM_RELATIONSHIP);
            String phone = contactObject.getString(OWM_PHONE);

            //Contact contact = new Contact(id, name, lastname, relationship, phone);
            Contact contact = new Contact(id, name, lastname, relationship, phone);
            contacts[i] = contact;
        }
        return contacts;
    }

    public Patient[] buildPatientObject(JSONArray patientDetails) throws JSONException{
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

        Patient[] patients = new Patient[patientDetails.length()];

        for(int i = 0; i < patientDetails.length(); i++){
            int id;
            String hc = "", ci = "", emi = "", nom = "", ape = "", sex = "", fnac = "",
                    lnac = "", gi = "", ec = "", ocu = "", gs = "";
            JSONObject patientObject = patientDetails.getJSONObject(i);

            id = patientObject.getInt(OWM_ID);
            hc = patientObject.getString(OWM_HC);
            ci = patientObject.getString(OWM_CI);
            emi = patientObject.getString(OWM_EMISION);
            nom = patientObject.getString(OWM_NAME);
            ape = patientObject.getString(OWM_LASTNAME);
            sex = patientObject.getString(OWM_GENDER);
            fnac = patientObject.getString(OWM_BIRTHDAY);
            lnac = patientObject.getString(OWM_BIRTHPLACE);
            gi = patientObject.getString(OWM_INSTRUCTION);
            ec = patientObject.getString(OWM_CIVILSTATUS);
            ocu = patientObject.getString(OWM_OCUPATION);
            gs = patientObject.getString(OWM_BLOODTYPE);

            Patient patient = new Patient(id, hc, ci, emi, nom, ape, sex, fnac, lnac, gi, ec, ocu, gs);
            patients[i] = patient;
        }
        return patients;
    }
}
