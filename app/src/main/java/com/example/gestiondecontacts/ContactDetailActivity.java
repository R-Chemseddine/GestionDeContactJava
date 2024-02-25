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

        // Initialisation du ViewModel
        contactViewModel = new ViewModelProvider(this, new ContactViewModelFactory(((ContactsApplication) getApplication()).getContactDao())).get(ContactViewModel.class);

        // Récupérez les données du contact
        String contactName = getIntent().getStringExtra("CONTACT_NAME");
        String contactPhone = getIntent().getStringExtra("CONTACT_PHONE");

        // Trouvez les vues et assignez-leur les valeurs récupérées
        TextView contactNameDetail = findViewById(R.id.contact_name_detail);
        TextView contactPhoneDetail = findViewById(R.id.contact_phone_detail);
        contactNameDetail.setText(contactName);
        contactPhoneDetail.setText(contactPhone);

        Button deleteButton = findViewById(R.id.delete_contact_button);
        deleteButton.setOnClickListener(view -> {
            // Récupérez l'ID du contact ou un autre identifiant unique
            long contactId = getIntent().getLongExtra("CONTACT_ID", -1);
            if (contactId != -1) {
                // Supprimez le contact de la base de données
                // Note: La suppression doit être effectuée de manière asynchrone, assurez-vous que contactViewModel.deleteContactById soit bien géré dans une tâche asynchrone.
                // Ceci est un exemple simplifié. Vous devrez adapter votre ViewModel pour exécuter les opérations de base de données de manière asynchrone.
                contactViewModel.deleteContactById(contactId);
                // Montrez un message de confirmation ou fermez l'activité
                Toast.makeText(ContactDetailActivity.this, "Contact supprimé", Toast.LENGTH_SHORT).show();
                finish(); // Ferme l'activité et retourne à l'activité précédente
            } else {
                Toast.makeText(ContactDetailActivity.this, "Erreur lors de la suppression du contact", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
