package app.example.veuge.com.saludnfc.models;

/**
 * Created by veuge on 05-07-16.
 */

import java.io.Serializable;

public class User implements Serializable{

    public int id;
    public String name;
    public String email;
    public String pro_registration;
    public String speciality;

    public User(int id, String name, String email, String pro_registration, String speciality){
        this.id = id;
        this.name = name;
        this.email = email;
        this.pro_registration = pro_registration;
        this.speciality = speciality;
    }
}
