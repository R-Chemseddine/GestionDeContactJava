package com.example.gestiondecontacts.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gestiondecontacts.daos.ContactDao;
import com.example.gestiondecontacts.models.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "contact_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
