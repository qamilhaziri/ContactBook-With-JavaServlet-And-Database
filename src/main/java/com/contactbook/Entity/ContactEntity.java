package com.contactbook.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactEntity {
    private Long contactID;
    private String firstName;
    private String lastName;
    private String number;
    private String email;
    private String notes;
    private Timestamp dateCreated;
    private Status status;
}
