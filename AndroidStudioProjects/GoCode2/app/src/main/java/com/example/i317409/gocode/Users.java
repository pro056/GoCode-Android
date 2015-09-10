package com.example.i317409.gocode;

/**
 * Created by I317409 on 8/14/2015.
 */

public class Users {

    String password;
    String name;
    String phone_no;
    String email_id;

    Users (String s1, String s2, String s3, String s4) {
        password = s1;
        name = s2;
        phone_no = s3;
        email_id = s4;
    }

    public String getPassword () {
        return password;
    }
    public String getName () {
        return name;
    }
    public String getPhone_no () {
        return  phone_no;
    }
    public String getEmail_id () {
        return email_id;
    }


}
