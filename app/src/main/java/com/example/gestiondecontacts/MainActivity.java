package com.example.gestiondecontacts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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

        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactAdapter = new ContactAdapter(emptyList(), this);
        contactsRecyclerView.setAdapter(contactAdapter);

        contactViewModel = new ViewModelProvider(this, new ContactViewModelFactory(
                ((ContactsApplication) getApplication()).getContactDao()
        )).get(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, newContacts -> {
            contactAdapter.updateContacts(newContacts);
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_contact);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
            startActivity(intent);
        });
        contactViewModel.getAllContacts().observe(this, contacts -> {
            contactAdapter.submitList(contacts);
        });

        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return false;
            }

            private void performSearch(String query) {
                contactViewModel.searchContacts(query).observe(MainActivity.this, contacts -> {
                    contactAdapter.submitList(contacts);
                });
            }
        });
    }

    @Override
    public void onContactClicked(Contact contact) {
        Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
        intent.putExtra("CONTACT_ID", contact.getId());
        intent.putExtra("CONTACT_NAME", contact.getName());
        intent.putExtra("CONTACT_PHONE", contact.getPhone());
        intent.putExtra("CONTACT_ADDRESS", contact.getAddress());
        startActivity(intent);
    }

    @Override
    public void onCallButtonClicked(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private <T> List<T> emptyList() {
        return new ArrayList<>();
    }
}
