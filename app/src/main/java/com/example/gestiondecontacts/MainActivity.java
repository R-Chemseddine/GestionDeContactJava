package com.example.gestiondecontacts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondecontacts.models.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {
    private RecyclerView contactsRecyclerView;
    private ContactAdapter contactAdapter;
    private ContactViewModel contactViewModel;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation de RecyclerView
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de l'adaptateur avec une liste vide et 'this' comme listener
        contactAdapter = new ContactAdapter(emptyList(), this);
        contactsRecyclerView.setAdapter(contactAdapter);

        // Initialisation du ViewModel
        contactViewModel = new ViewModelProvider(this, new ContactViewModelFactory(
                ((ContactsApplication) getApplication()).getContactDao()
        )).get(ContactViewModel.class);

        // Observation des contacts pour mise à jour de l'UI
        contactViewModel.getAllContacts().observe(this, newContacts -> {
            contactAdapter.updateContacts(newContacts);
        });

        // Gestion du FloatingActionButton pour ajouter un contact
        FloatingActionButton fab = findViewById(R.id.fab_add_contact);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
            startActivity(intent);
        });

        contactViewModel.getAllContacts().observe(this, contacts -> {
            contactAdapter.submitList(contacts);
        });
    }

    @Override
    public void onContactClicked(Contact contact) {
        Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
        intent.putExtra("CONTACT_ID", contact.getId());
        // Passez les détails du contact en tant qu'extra
        intent.putExtra("CONTACT_NAME", contact.getName());
        intent.putExtra("CONTACT_PHONE", contact.getPhone());
        startActivity(intent);
    }

    // Méthode helper pour créer une liste vide (puisque Java n'a pas de fonction `emptyList` directe comme Kotlin)
    private <T> List<T> emptyList() {
        return new ArrayList<>();
    }
}
