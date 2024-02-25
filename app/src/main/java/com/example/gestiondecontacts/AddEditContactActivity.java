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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_contact_activity);

        // Initialisation des composants UI
        EditText nameEditText = findViewById(R.id.editTextContactName);
        EditText phoneEditText = findViewById(R.id.editTextContactPhone);
        EditText addressEditText = findViewById(R.id.editTextContactAddress);
        EditText photoEditText = findViewById(R.id.editTextContactPhoto);
        Button saveButton = findViewById(R.id.buttonSaveContact);

        // Obtenez une instance de la base de données et du DAO
        AppDatabase appDatabase = AppDatabase.getDatabase(getApplication());
        ContactDao contactDao = appDatabase.contactDao();

        // Initialisez le ViewModel avec le ViewModelFactory
        ContactViewModelFactory factory = new ContactViewModelFactory(contactDao);
        contactViewModel = new ViewModelProvider(this, factory).get(ContactViewModel.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String photo = photoEditText.getText().toString().trim();

                if (!name.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
                    Contact newContact = new Contact(name, phone, address, photo);

                    // Utilisation d'Executors pour exécuter de manière asynchrone
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            contactViewModel.insert(newContact);
                            // Retour au thread UI pour afficher le Toast
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddEditContactActivity.this, "Contact ajouté avec succès", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    executorService.shutdown();
                    finish();
                } else {
                    Toast.makeText(AddEditContactActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}