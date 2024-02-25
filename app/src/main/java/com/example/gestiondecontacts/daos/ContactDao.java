package com.example.gestiondecontacts.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.gestiondecontacts.models.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contacts")
    LiveData<List<Contact>> getAll();

    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("SELECT * FROM contacts WHERE name LIKE :searchQuery")
    LiveData<List<Contact>> searchContacts(String searchQuery);

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("DELETE FROM contacts WHERE id = :contactId")
    void deleteContactById(long contactId);
}
