package com.contactbook.Service;

import com.contactbook.Database.DataBase;
import com.contactbook.Entity.ContactEntity;
import com.contactbook.Entity.Status;
import com.contactbook.Repository.ContactRepository;

import java.sql.*;
import java.util.*;

public class ContactService {
    private ContactRepository repository;

    public ContactService(){
        repository = new ContactRepository();
    }

    public int addContact(ContactEntity contact) {

       return repository.addContact(contact);

    }

    public ContactEntity getContact(Long contactId) {

       return repository.getContact(contactId);
    }

    public List<ContactEntity> getAllContacts() {

       return repository.getAllContacts();
    }

    public boolean updateContact(ContactEntity contact, Long id){
        int result = repository.updateContact(contact,id);

       return result > 0;

    }

    public boolean moveToBin(Long id){
        int result = repository.moveToBin(id);

        return result > 0;
    }

    public boolean restoreFromBin(Long id) {
       int result = repository.restoreFromBin(id);

       return result > 0;

    }

    public boolean deleteContact(Long id){
       int result = repository.deleteContact(id);

       return result > 0;
    }
}
