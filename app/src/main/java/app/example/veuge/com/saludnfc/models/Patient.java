package app.example.veuge.com.saludnfc.models;

/**
 * Created by veuge on 03-06-16.
 */

public class Patient {

    public String id;
    public String historia;
    public String ci;
    public String emision;
    public String nombre;
    public String apellido;
    public String sexo;
    public String fecha_nac;
    public String lugar_nac;
    public String grado_instruccion;
    public String estado_civil;
    public String ocupacion;
    public String grupo_sanguineo;

    public Patient(String id, String historia, String ci, String emision, String nombre, String apellido,
                   String fecha_nac, String lugar_nac, String grupo_sanguineo){
        this.id = id;
        this.historia = historia;
        this.ci = ci;
        this.emision = emision;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fecha_nac = fecha_nac;
        this.lugar_nac = lugar_nac;
        this.grupo_sanguineo = grupo_sanguineo;
    }

    public void setSexo(String gender){
        this.sexo = gender;
    }

    public void setGradoInstruccion(String instruction){
        this.grado_instruccion = instruction;
    }

    public void setEstadoCivil(String civil){
        this.estado_civil = civil;
    }

    public void setOcupacion(String ocupation){
        this.ocupacion = ocupation;
    }
}