package com.example.gestiondecontacts.models;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Random;
import java.util.UUID;

@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "address")
    private final String address;

    @ColumnInfo(name = "phone")
    private final String phone;

    @ColumnInfo(name = "photo")
    private final String photo;


    public Contact(String name, String phone, String address, String photo) {
        this.id = new Random().nextLong();
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.photo = photo;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setId(long contactId) {
        this.id = contactId;
    }
}
