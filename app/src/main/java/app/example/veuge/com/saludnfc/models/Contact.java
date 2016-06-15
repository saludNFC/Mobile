package app.example.veuge.com.saludnfc.models;

/**
 * Created by veuge on 14-06-16.
 */
public class Contact {
    public String id;
    public String name;
    public String lastname;
    public String relationship;
    public String phone;

    public Contact(String id, String name, String lastname, String relationship, String phone){
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.relationship = relationship;
        this.phone = phone;
    }
}
