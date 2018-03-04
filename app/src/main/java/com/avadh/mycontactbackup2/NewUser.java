package com.avadh.mycontactbackup2;

/**
 * Created by avadh on 2/25/2018.
 */

public class NewUser {
    String name;
    String surname;
    String mail;
    String password;
    String phoneno;

    public NewUser() {
    }

    public NewUser(String name, String surname, String mail, String password, String phoneno) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.password = password;
        this.phoneno = phoneno;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneno() {
        return phoneno;
    }
}
