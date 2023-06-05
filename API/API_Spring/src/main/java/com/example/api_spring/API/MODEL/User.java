package com.example.api_spring.API.MODEL;

public class User {
    private String cip;
    private String firstname;
    private String lastname;

    public User(String cip,String firstname, String lastname){

        this.cip = cip;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public void setcip(String cip){
        this.cip = cip;
    }
    public void setfirstname(String firstname){
        this.firstname = firstname;
    }
    public void setlastname(String lastname){
        this.lastname = lastname;
    }
    public String getcip(){
        return cip;
    }
    public String getfirstname(){
        return firstname;
    }
    public String getlastname(){
        return lastname;
    }
}
