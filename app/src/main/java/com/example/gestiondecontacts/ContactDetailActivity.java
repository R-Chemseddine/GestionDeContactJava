package com.example.gestiondecontacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class ContactDetailActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        contactViewModel = new ViewModelProvider(this, new ContactViewModelFactory(((ContactsApplication) getApplication()).getContactDao())).get(ContactViewModel.class);

        String contactName = getIntent().getStringExtra("CONTACT_NAME");
        String contactPhone = getIntent().getStringExtra("CONTACT_PHONE");

        TextView contactNameDetail = findViewById(R.id.contact_name_detail);
        TextView contactPhoneDetail = findViewById(R.id.contact_phone_detail);
        contactNameDetail.setText(contactName);
        contactPhoneDetail.setText(contactPhone);

        Button deleteButton = findViewById(R.id.delete_contact_button);
        deleteButton.setOnClickListener(view -> {
            long contactId = getIntent().getLongExtra("CONTACT_ID", -1);
            if (contactId != -1) {
                new AlertDialog.Builder(ContactDetailActivity.this)
                        .setTitle("Supprimer Contact")
                        .setMessage("Êtes-vous sûr de vouloir supprimer ce contact ?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            contactViewModel.deleteContactById(contactId);
                            Toast.makeText(ContactDetailActivity.this, "Contact supprimé", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Toast.makeText(ContactDetailActivity.this, "Erreur lors de la suppression du contact", Toast.LENGTH_SHORT).show();
            }
        });

        Button editButton = findViewById(R.id.edit_contact_button);
        editButton.setOnClickListener(view -> {
            long contactId = getIntent().getLongExtra("CONTACT_ID", -1);
            if (contactId != -1) {
                Intent intent = new Intent(ContactDetailActivity.this, AddEditContactActivity.class);
                intent.putExtra("CONTACT_ID", contactId);
                startActivity(intent);
            } else {
                Toast.makeText(ContactDetailActivity.this, "Erreur lors de l'édition du contact", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
