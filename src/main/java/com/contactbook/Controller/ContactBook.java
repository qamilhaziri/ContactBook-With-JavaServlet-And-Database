package com.contactbook.Controller;

import com.google.gson.Gson;
import com.contactbook.Entity.ContactEntity;
import com.contactbook.Service.ContactService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;
import java.util.Objects;

@WebServlet("/contactBook")
public class ContactBook extends HttpServlet {

    ContactService service = new ContactService();
    Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ContactEntity contact = gson.fromJson(request.getReader(),ContactEntity.class);

        int exec = service.addContact(contact);

        PrintWriter writer = response.getWriter();

        if(exec >= 1){
            response.setStatus(201);
            response.setContentType("text/plain");
            writer.println(exec + " records inserted");

        }else {
            response.setStatus(400);
            response.setContentType("text/plain");
            writer.println(exec + " records inserted");
        }
        writer.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{

        String idString = request.getParameter("id");

        PrintWriter writer = response.getWriter();

        response.setContentType("application/json");

       if(idString != null){
           Long contactId = null;

           try{
               contactId = Long.parseLong(idString);

           }catch(NumberFormatException ex){
               response.setStatus(400);
               writer.println(ex.getMessage());
               writer.close();
           }

           ContactEntity contact =  service.getContact(contactId);

           writer.println(gson.toJson(contact));

       }else {
           List<ContactEntity> contacts = service.getAllContacts();

           writer.println(gson.toJson(contacts));

       }
        writer.close();
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ContactEntity contact = gson.fromJson(request.getReader(),ContactEntity.class);

        String idString = request.getParameter("id");

        String action = request.getParameter("action");

        if(Objects.isNull(idString)) throw new NullPointerException("No id specified");

        Long contactId = Long.parseLong(idString);

        PrintWriter writer = response.getWriter();

        boolean updated = false;

        if(Objects.isNull(action))
            updated = service.updateContact(contact,contactId);
        else if(action.toLowerCase().equals("trash"))
            updated = service.moveToBin(contactId);
        else if(action.toLowerCase().equals("restore"))
            updated = service.restoreFromBin(contactId);

        if(updated){
            writer.println("Successfully updated");
        }else{
            response.setStatus(400);
            writer.println("Unsuccessfully operation");
        }

            writer.close();
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idString = request.getParameter("id");

        if(Objects.isNull(idString)) throw new NullPointerException("No id specified");

        Long contactId = Long.parseLong(idString);

        boolean deleted = false;


        deleted = service.deleteContact(contactId);


        PrintWriter writer = response.getWriter();

        if(deleted)
            writer.println("Successfully deleted contact");
        else{
            response.setStatus(500);
            writer.println("The process failed and was not deleted.");
        }

        writer.close();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(req.getMethod().equals("PATCH"))
            this.doPatch(req,resp);
        else
            super.service(req,resp);
    }


}
