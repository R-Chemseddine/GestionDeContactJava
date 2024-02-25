package com.example.gestiondecontacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        String contactAddress = getIntent().getStringExtra("CONTACT_ADDRESS");

        TextView contactNameDetail = findViewById(R.id.contact_name_detail);
        TextView contactPhoneDetail = findViewById(R.id.contact_phone_detail);
        TextView contactAddressDetail = findViewById(R.id.contact_address_detail);
        contactNameDetail.setText(contactName);
        contactPhoneDetail.setText(contactPhone);
        contactAddressDetail.setText(contactAddress);

        long contactId = getIntent().getLongExtra("CONTACT_ID", -1);
        if (contactId != -1) {
            contactViewModel.getContactById(contactId).observe(this, contact -> {
                // Mettez à jour l'interface utilisateur avec les données observées
                if (contact != null) {
                    contactNameDetail.setText(contact.getName());
                    contactPhoneDetail.setText(contact.getPhone());
                }
            });
        }

        Button deleteButton = findViewById(R.id.delete_contact_button);
        deleteButton.setOnClickListener(view -> {
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
            if (contactId != -1) {
                Intent intent = new Intent(ContactDetailActivity.this, AddEditContactActivity.class);
                intent.putExtra("CONTACT_ID", contactId);
                startActivity(intent);
            } else {
                Toast.makeText(ContactDetailActivity.this, "Erreur lors de l'édition du contact", Toast.LENGTH_SHORT).show();
            }
        });

        Button callButton = findViewById(R.id.call_contact_button);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contactPhone)); // Utilisez le numéro récupéré de l'intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(ContactDetailActivity.this, "Aucune application d'appel trouvée.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
