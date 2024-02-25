package com.example.gestiondecontacts;

import android.app.Application;

import com.example.gestiondecontacts.daos.ContactDao;
import com.example.gestiondecontacts.database.AppDatabase;

public class ContactsApplication extends Application {
    private AppDatabase database;
    private ContactDao contactDao;

    @Override
    public void onCreate() {
        super.onCreate();
        database = AppDatabase.getDatabase(this);
        contactDao = database.contactDao();
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public ContactDao getContactDao() {
        return contactDao;
    }
}
