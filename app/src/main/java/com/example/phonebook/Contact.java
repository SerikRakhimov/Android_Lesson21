package com.example.phonebook;

import java.util.ArrayList;

public class Contact {
    private int id;
    private String name;
    private ArrayList<String> phones;

    public Contact(int id, String name, ArrayList<String> phones) {
        this.id = id;
        this.name = name;
        this.phones = new ArrayList<>();
        this.phones = phones;
    }

    public String getPhonesString() {
        String result="";
        for(int i=0; i<this.phones.size(); i++){
            result = result + "\n" +this.phones.get(i);
        }
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public String getPhone(int i) {
        return this.phones.get(i);
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }
}