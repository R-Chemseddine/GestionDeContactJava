package com.example.gestiondecontacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.gestiondecontacts.daos.ContactDao;
import com.example.gestiondecontacts.models.Contact;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactViewModel extends ViewModel {

    private final ContactDao contactDao;
    private final ExecutorService executorService;

    public ContactViewModel(ContactDao contactDao) {
        this.contactDao = contactDao;
        this.executorService = Executors.newFixedThreadPool(2); // Utiliser un pool de threads pour les opérations DB
    }

    // Fonction pour insérer un contact
    public void insert(Contact contact) {
        executorService.execute(() -> contactDao.insert(contact));
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contactDao.getAllContacts();
    }

    public void deleteContactById(long contactId) {
        executorService.execute(() -> contactDao.deleteContactById(contactId));
    }
}
