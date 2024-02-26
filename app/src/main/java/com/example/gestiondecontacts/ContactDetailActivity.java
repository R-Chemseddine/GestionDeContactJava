package com.example.gestiondecontacts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

public class ContactDetailActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;
    private static final int REQUEST_CALL_PHONE = 1;
    private String contactPhone;
    private long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        contactViewModel = new ViewModelProvider(this, new ContactViewModelFactory(((ContactsApplication) getApplication()).getContactDao())).get(ContactViewModel.class);

        String contactName = getIntent().getStringExtra("CONTACT_NAME");
        contactPhone = getIntent().getStringExtra("CONTACT_PHONE");
        String contactAddress = getIntent().getStringExtra("CONTACT_ADDRESS");

        TextView contactNameDetail = findViewById(R.id.contact_name_detail);
        TextView contactPhoneDetail = findViewById(R.id.contact_phone_detail);
        TextView contactAddressDetail = findViewById(R.id.contact_address_detail);
        contactNameDetail.setText(contactName);
        contactPhoneDetail.setText(contactPhone);
        contactAddressDetail.setText(contactAddress);

        contactId = getIntent().getLongExtra("CONTACT_ID", -1);
        if (contactId != -1) {
            contactViewModel.getContactById(contactId).observe(this, contact -> {
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
                Log.d("CallIntent", "Dialing number: " + contactPhone);
                // Vérifier si la permission CALL_PHONE a été accordée
                if (ContextCompat.checkSelfPermission(ContactDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Si la permission n'a pas été accordée, demandez-la
                    ActivityCompat.requestPermissions(ContactDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                } else {
                    // Si la permission a été accordée, lancez l'intention d'appel
                    startCallIntent(contactPhone);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCallIntent(contactPhone);
            } else {
                Toast.makeText(this, "Permission pour passer des appels refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCallIntent(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateContactDetails(contactId);
    }

    private void updateContactDetails(long contactId) {
        if (contactId != -1) {
            contactViewModel.getContactById(contactId).observe(this, contact -> {
                if (contact != null) {
                    TextView contactNameDetail = findViewById(R.id.contact_name_detail);
                    TextView contactPhoneDetail = findViewById(R.id.contact_phone_detail);
                    TextView contactAddressDetail = findViewById(R.id.contact_address_detail);

                    contactNameDetail.setText(contact.getName());
                    contactPhoneDetail.setText(contact.getPhone());
                    contactAddressDetail.setText(contact.getAddress());

                    contactPhone = contact.getPhone();
                }
            });
        }
    }
}
