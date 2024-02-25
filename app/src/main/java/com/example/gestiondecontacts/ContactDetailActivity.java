package com.example.gestiondecontacts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
                contactViewModel.deleteContactById(contactId);
                Toast.makeText(ContactDetailActivity.this, "Contact supprimé", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ContactDetailActivity.this, "Erreur lors de la suppression du contact", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
