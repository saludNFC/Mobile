package app.example.veuge.com.saludnfc.models;

/**
 * Created by veuge on 03-06-16.
 */

import java.io.Serializable;

/**
 * I don't care for getters and setters ¬¬
 */

public class Patient implements Serializable{

    public int id;
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

    public Patient(int id,
                   String historia,
                   String ci,
                   String emision,
                   String nombre,
                   String apellido,
                   String sexo,
                   String fecha_nac,
                   String lugar_nac,
                   String gi,
                   String ec,
                   String oc,
                   String grupo_sanguineo){
        this.id = id;
        this.historia = historia;
        this.ci = ci;
        this.emision = emision;
        this.nombre = nombre;
        this.apellido = apellido;
        this.sexo = sexo;
        this.fecha_nac = fecha_nac;
        this.lugar_nac = lugar_nac;
        this.grado_instruccion = gi;
        this.estado_civil = ec;
        this.ocupacion = oc;
        this.grupo_sanguineo = grupo_sanguineo;
    }
}