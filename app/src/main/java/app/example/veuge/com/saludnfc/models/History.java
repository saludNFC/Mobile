package app.example.veuge.com.saludnfc.models;

/**
 * Created by veuge on 05-06-16.
 */
public class History {
    public String id, historyType, grade, illness,
            typePersonal, description,
            med, via, dateIni;

    public History(String id, String ht, String gr, String ill, String tp, String des, String med, String via,
                   String date){
        this.id = id;
        this.historyType = ht;
        this.grade = gr;
        this.illness = ill;
        this.typePersonal = tp;
        this.description = des;
        this.med = med;
        this.via = via;
        this.dateIni = date;
    }

    /**
     * Constructor for history type -> Familiar
     */
    public History(String id, String ht, String gr, String ill){
        this.id = id;
        this.historyType = ht;
        this.grade = gr;
        this.illness = ill;
    }

//    /**
//     * Constructor for history type -> Personal
//     * @param id
//     * @param ht
//     * @param tp
//     * @param des
//     */
//    public History(String id, String ht, String tp, String des){
//        this.id = id;
//        this.historyType = ht;
//        this.typePersonal = tp;
//        this.description = des;
//    }
}
