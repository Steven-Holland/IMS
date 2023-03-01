package com.example.ims;

/*
Notes:
    Singleton created for the purpose of easily sharing data between scenes and controllers
    because for some reason JavaFX doesn't have any good ways of doing it normally.

    Add new variables, getters, and setters as needed.
 */

public class SharedStorage {

    private String current_store;

    private static SharedStorage INSTANCE = new SharedStorage();
    public static SharedStorage getInstance(){
        return INSTANCE;
    }
    private SharedStorage(){}

    public void setStore(String store){
        current_store = store;
    }

    public String getStore(){
        return current_store;
    }
}
