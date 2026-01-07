package com.contactbook.Repository;

import com.contactbook.Database.DataBase;
import com.contactbook.Entity.ContactEntity;
import com.contactbook.Entity.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactRepository {

    public int addContact(ContactEntity contact) {
        String query = "Insert into Contacts(firstName,lastName,number) values (?, ? , ? )";

        int exec = 0;
        try(Connection con = DataBase.getConnection();){

            PreparedStatement statement = con.prepareStatement(query);

            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getNumber());

            exec = statement.executeUpdate();

        }catch (SQLException ex){
           throw new RuntimeException(ex.getMessage());
        }

        return exec;
    }

    public ContactEntity getContact(Long contactId) {

        String query = "Select * from contacts where contactid = ?";

        ContactEntity contactEntity = null;

        try(Connection con = DataBase.getConnection()) {

            PreparedStatement statement = con.prepareStatement(query);

            statement.setLong(1,contactId);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next())
                contactEntity = new ContactEntity(resultSet.getLong(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5),resultSet.getString(6),
                        resultSet.getTimestamp(7),
                        Status.valueOf(resultSet.getString(8)));

        }catch(SQLException ex){

            throw new RuntimeException(ex.getMessage());

        }

        return contactEntity;
    }

    public List<ContactEntity> getAllContacts() {
        List<ContactEntity> contacts = new ArrayList<>();

        String query = "Select * from contacts order by firstname";

        try (Connection con = DataBase.getConnection()) {

            ResultSet resultSet = con.createStatement().executeQuery(query);

            while(resultSet.next()) {

                ContactEntity contact = new ContactEntity(resultSet.getLong(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5),resultSet.getString(6),
                        resultSet.getTimestamp(7),
                        Status.valueOf(resultSet.getString(8)));

                contacts.add(contact);
            }

        }catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return contacts;
    }

    public int updateContact(ContactEntity contact, Long id) {

        StringBuilder query = new StringBuilder("Update contacts set ");
        List<Object> params = new ArrayList<>();

        if(contact.getFirstName() != null){
            query.append("firstname = ?,");
            params.add(contact.getFirstName());
        }
        if (contact.getLastName() != null){
            query.append("lastname = ?,");
            params.add(contact.getLastName());
        }
        if (contact.getNumber() != null){
            query.append("number = ?,");
            params.add(contact.getNumber());
        }
        if (contact.getEmail() != null){
            query.append("email = ?,");
            params.add(contact.getEmail());
        }
        if (contact.getNotes() != null){
            query.append("notes = ?,");
            params.add(contact.getNotes());
        }
        if (contact.getStatus() != null){
            query.append("status = ?::contact_status,");
            params.add(contact.getStatus().name());
        }

        query.setLength(query.length() - 1);

        if(params.isEmpty())
            throw new RuntimeException("No fields to update");

        if(id != null){
            query.append(" where contactid = ?");
            params.add(id);
        }else
            throw new RuntimeException("Missing id");


        int result = 0;
        try(Connection con = DataBase.getConnection()) {
            PreparedStatement statement = con.prepareStatement(query.toString());

            for(int param = 0; param < params.size(); param++)
                statement.setObject(param + 1, params.get(param));

            result = statement.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }

    public int moveToBin(Long id) {

        ContactEntity contact = this.getContact(id);

        if(contact.getStatus() == Status.TRASH)
            return 0;

        contact.setStatus(Status.TRASH);
        int result = this.updateContact(contact,id);


        return result ;
    }

    public int restoreFromBin(Long id) {

        ContactEntity contact = this.getContact(id);

        if(contact.getStatus() != Status.TRASH)
            return 0;

        contact.setStatus(Status.ACTIVE);
        int result = this.updateContact(contact,id);

        return result;
    }

    public int deleteContact(Long id) {
        String query = "Delete from contacts where contactid = ?";
        int result = 0;

        try(Connection con = DataBase.getConnection()){

            PreparedStatement statement = con.prepareStatement(query);
            statement.setLong(1,id);

            ContactEntity contactEntity =  this.getContact(id);

            if(Objects.isNull(contactEntity))
                throw new RuntimeException("No contact found");

            result = statement.executeUpdate();

        }catch(SQLException ex){
            throw new RuntimeException(ex.getMessage());
        }

        return result;
    }

}
