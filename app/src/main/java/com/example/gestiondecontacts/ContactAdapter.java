package com.example.gestiondecontacts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gestiondecontacts.models.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contacts;
    private final OnContactClickListener listener;

    public ContactAdapter(List<Contact> contacts, OnContactClickListener listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact currentContact = contacts.get(position);
        holder.bind(currentContact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateContacts(List<Contact> newContacts) {
        contacts = newContacts;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<Contact> newContacts) {
        contacts = newContacts;
        notifyDataSetChanged();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        private final ImageView contactImage;
        private final TextView contactName;
        private final TextView contactPhone;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contact_image);
            contactName = itemView.findViewById(R.id.contact_name);
            contactPhone = itemView.findViewById(R.id.contact_phone);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Contact clickedContact = contacts.get(position);
                    listener.onContactClicked(clickedContact);
                }
            });
        }

        public void bind(Contact contact) {
            contactName.setText(contact.getName());
            contactPhone.setText(contact.getPhone());
            Glide.with(itemView.getContext()).load(contact.getPhoto()).into(contactImage);
        }
    }

    public interface OnContactClickListener {
        void onContactClicked(Contact contact);
    }
}