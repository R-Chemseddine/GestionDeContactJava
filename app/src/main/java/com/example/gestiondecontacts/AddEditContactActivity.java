package com.example.gestiondecontacts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestiondecontacts.daos.ContactDao;
import com.example.gestiondecontacts.database.AppDatabase;
import com.example.gestiondecontacts.models.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditContactActivity extends AppCompatActivity {
    private ContactViewModel contactViewModel;
    private long contactId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_contact_activity);

        contactId = getIntent().getLongExtra("CONTACT_ID", -1);

        // Initialisation des composants UI
        EditText nameEditText = findViewById(R.id.editTextContactName);
        EditText phoneEditText = findViewById(R.id.editTextContactPhone);
        EditText addressEditText = findViewById(R.id.editTextContactAddress);
        EditText photoEditText = findViewById(R.id.editTextContactPhoto);
        Button saveButton = findViewById(R.id.buttonSaveContact);

        AppDatabase appDatabase = AppDatabase.getDatabase(getApplication());
        ContactDao contactDao = appDatabase.contactDao();

        ContactViewModelFactory factory = new ContactViewModelFactory(contactDao);
        contactViewModel = new ViewModelProvider(this, factory).get(ContactViewModel.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérez les valeurs des champs de texte
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String photo = photoEditText.getText().toString();

                // Créez un objet Contact avec ces valeurs
                Contact contact = new Contact(name, phone, address, photo);

                if (contactId == -1) {
                    // Si contactId est -1, c'est un nouveau contact, donc insérez-le
                    contactViewModel.insert(contact);
                } else {
                    // Sinon, c'est une mise à jour, donc définissez l'ID et mettez à jour le contact
                    contact.setId(contactId);
                    contactViewModel.update(contact);
                }

                finish(); // Fermez l'activité après l'opération
            }
        });

        long contactId = getIntent().getLongExtra("CONTACT_ID", -1);
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
}