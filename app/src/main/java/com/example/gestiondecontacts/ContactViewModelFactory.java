package com.example.gestiondecontacts;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestiondecontacts.daos.ContactDao;

public class ContactViewModelFactory implements ViewModelProvider.Factory {

    private final ContactDao contactDao;

    public ContactViewModelFactory(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ContactViewModel.class)) {
            return (T) new ContactViewModel(contactDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
