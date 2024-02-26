package com.example.gestiondecontacts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestiondecontacts.daos.ContactDao;
import com.example.gestiondecontacts.database.AppDatabase;
import com.example.gestiondecontacts.models.Contact;

public class AddEditContactActivity extends AppCompatActivity {
    private ContactViewModel contactViewModel;
    private long contactId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_contact_activity);

        contactId = getIntent().getLongExtra("CONTACT_ID", -1);

        EditText nameEditText = findViewById(R.id.editTextContactName);
        EditText phoneEditText = findViewById(R.id.editTextContactPhone);
        EditText addressEditText = findViewById(R.id.editTextContactAddress);
        EditText photoEditText = findViewById(R.id.editTextContactPhoto);
        Button saveButton = findViewById(R.id.buttonSaveContact);

        AppDatabase appDatabase = AppDatabase.getDatabase(getApplication());
        ContactDao contactDao = appDatabase.contactDao();

        ContactViewModelFactory factory = new ContactViewModelFactory(contactDao);
        contactViewModel = new ViewModelProvider(this, factory).get(ContactViewModel.class);

        saveButton.setOnClickListener(v -> {
            new AlertDialog.Builder(AddEditContactActivity.this)
                    .setTitle("Confirmer l'action")
                    .setMessage("Êtes-vous sûr de vouloir sauvegarder ces modifications ?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        String name = nameEditText.getText().toString();
                        String phone = phoneEditText.getText().toString();
                        String address = addressEditText.getText().toString();
                        String photo = photoEditText.getText().toString();

                        if (!isValidPhone(phone)) {
                            Toast.makeText(AddEditContactActivity.this, "Numéro de téléphone invalide.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Contact contact = new Contact(name, phone, address, photo);

                        if (contactId == -1) {
                            contactViewModel.insert(contact);
                            Toast.makeText(AddEditContactActivity.this, "Contact ajouté", Toast.LENGTH_SHORT).show();
                        } else {
                            contact.setId(contactId);
                            contactViewModel.update(contact);
                            Toast.makeText(AddEditContactActivity.this, "Contact mis à jour", Toast.LENGTH_SHORT).show();
                        }

                        finish();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        if (contactId != -1) {
            contactViewModel.getContactById(contactId).observe(this, contact -> {
                if (contact != null) {
                    nameEditText.setText(contact.getName());
                    phoneEditText.setText(contact.getPhone());
                    addressEditText.setText(contact.getAddress());
                    photoEditText.setText(contact.getPhoto());
                }
            });
        }
    }

    private boolean isValidPhone(String phone) {
        String regex = "^[0-9]+$";
        return phone.matches(regex);
    }
}
