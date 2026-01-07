package com.contactbook;

import com.contactbook.Entity.ContactEntity;
import com.contactbook.Repository.ContactRepository;
import com.contactbook.Service.ContactService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest{

    ContactEntity entity;

    @Mock
    ContactRepository repository;

    @InjectMocks
    ContactService service;

  @BeforeEach
    public void setUp() {
      entity = new ContactEntity();
      entity.setFirstName("TestContact");
      entity.setNumber("0123");
    }

    @Test
    public void testAddContact(){

        when(repository.addContact(any(ContactEntity.class))).thenReturn(1);

        int result = service.addContact(entity);

        assertTrue(result > 0);

        verify(repository).addContact(any(ContactEntity.class));
    }

    @Test
    public void testGetContact(){

        when(repository.getContact(anyLong())).thenReturn(entity);

        ContactEntity contactEntity = service.getContact(6l);

        assertNotNull(contactEntity);

        verify(repository).getContact(anyLong());
    }

    @Test
    public void testGetAllContacts(){

        when(repository.getAllContacts()).thenReturn(List.of());

        List<ContactEntity> list = service.getAllContacts();

        assertNotNull(list);

        verify(repository).getAllContacts();
    }

    @Test
    public void testUpdateContact() {

        when(repository.updateContact(any(ContactEntity.class),anyLong())).thenReturn(1);

        boolean result = service.updateContact(entity,1l);

        assertTrue(result);

        verify(repository).updateContact(any(ContactEntity.class),anyLong());
    }

    @Test
    public void testMoveToBin() throws Exception{

        when(repository.moveToBin(anyLong())).thenReturn(1);

        boolean result = service.moveToBin(anyLong());

        assertTrue(result);

        verify(repository).moveToBin(anyLong());
    }

    @Test
    public void testRestoreFromBin() throws Exception{
        when(repository.restoreFromBin(anyLong())).thenReturn(1);

        boolean result = service.restoreFromBin(anyLong());

        assertTrue(result);

        verify(repository).restoreFromBin(anyLong());
    }
}