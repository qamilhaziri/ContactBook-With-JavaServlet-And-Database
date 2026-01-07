# ContactBook Servlet API

A Java web application for managing personal contact records through a servlet-based REST-style API. The project demonstrates how a small business workflow can be modeled with layered Java architecture, PostgreSQL persistence, JSON request/response handling, and unit-tested service logic.

## Key Features

- Create new contact records with first name, last name, and phone number data.
- Retrieve all contacts sorted by first name.
- Retrieve a single contact by ID.
- Update contact fields partially without replacing the entire record.
- Move contacts to trash instead of deleting them immediately.
- Restore trashed contacts back to active status.
- Permanently delete contacts when they are no longer needed.
- Return JSON responses using Gson.
- Use prepared statements for database operations.
- Cover service-layer behavior with JUnit 5 and Mockito tests.

## Technical Architecture

The application follows a simple layered architecture:

```text
HTTP Client
    |
    v
ContactBook Servlet
    |
    v
ContactService
    |
    v
ContactRepository
    |
    v
PostgreSQL Database
```

### Controller Layer

`ContactBook` is a Jakarta Servlet mapped to `/contactBook`. It handles HTTP requests, parses JSON bodies with Gson, reads query parameters such as `id` and `action`, and returns appropriate HTTP responses.

Supported operations:

- `POST /contactBook` creates a contact.
- `GET /contactBook` returns all contacts.
- `GET /contactBook?id={id}` returns one contact.
- `PATCH /contactBook?id={id}` updates contact fields.
- `PATCH /contactBook?id={id}&action=trash` moves a contact to trash.
- `PATCH /contactBook?id={id}&action=restore` restores a trashed contact.
- `DELETE /contactBook?id={id}` permanently deletes a contact.

### Service Layer

`ContactService` contains the business-facing operations used by the servlet. It keeps controller code focused on HTTP concerns while delegating contact actions to the repository.

### Repository Layer

`ContactRepository` is responsible for PostgreSQL access through JDBC. It uses prepared statements for inserts, reads, updates, status changes, and deletes.

One important implementation detail is the dynamic update query: the repository only updates fields that are present in the incoming request, which supports flexible partial updates.

### Data Model

`ContactEntity` represents a contact record:

- `contactID`
- `firstName`
- `lastName`
- `number`
- `email`
- `notes`
- `dateCreated`
- `status`

`Status` is an enum with:

- `ACTIVE`
- `TRASH`

## Technologies Used

- Java 21
- Jakarta Servlet API 6.1
- Maven
- PostgreSQL
- JDBC
- Gson
- Lombok
- JUnit 5
- Mockito
- WAR packaging for servlet container deployment

## Challenges Solved

- Built a REST-style API using plain Jakarta Servlets instead of relying on a full framework.
- Designed a clean separation between HTTP handling, business logic, and database persistence.
- Implemented partial contact updates by dynamically building SQL based on non-null fields.
- Added soft-delete behavior through a `TRASH` status before permanent deletion.
- Used PostgreSQL enum casting for status updates.
- Wrote service-layer tests with Mockito to validate behavior without depending on a live database.
- Overrode servlet request handling to support the `PATCH` method.

## What I Learned

- How servlet-based Java web applications process HTTP requests and responses.
- How to structure a backend project into controller, service, repository, entity, and database layers.
- How JDBC connections, prepared statements, and result sets work in a real CRUD application.
- How to serialize and deserialize JSON in Java using Gson.
- How to design safer delete workflows with trash and restore behavior.
- How unit tests can isolate business logic from database implementation details.
- How Maven manages dependencies, builds WAR files, and supports repeatable project setup.

## Project Structure

```text
src
+-- main
|   +-- java/com/contactbook
|   |   +-- Controller/ContactBook.java
|   |   +-- Database/DataBase.java
|   |   +-- Entity/ContactEntity.java
|   |   +-- Entity/Status.java
|   |   +-- Repository/ContactRepository.java
|   |   +-- Service/ContactService.java
|   +-- webapp
|       +-- WEB-INF/web.xml
|       +-- index.jsp
+-- test/java/com/contactbook
    +-- ContactServiceTest.java
```

## Running Locally

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- A servlet container such as Apache Tomcat that supports Jakarta Servlet 6.x

### Database Setup

Create a PostgreSQL database named `ContactBook` and a `contacts` table compatible with the fields in `ContactEntity`.

Example schema:

```sql
CREATE TYPE contact_status AS ENUM ('ACTIVE', 'TRASH');

CREATE TABLE contacts (
    contactid BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    number VARCHAR(50),
    email VARCHAR(150),
    notes TEXT,
    datecreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status contact_status DEFAULT 'ACTIVE'
);
```

Update the database connection values in:

```text
src/main/java/com/contactbook/Database/DataBase.java
```

### Build

```bash
./mvnw clean package
```

The WAR file is generated in:

```text
target/ProjectServlet.war
```

Deploy this WAR file to a compatible Tomcat server.

### Run Tests

```bash
./mvnw test
```

## Example Requests

Create a contact:

```bash
curl -X POST http://localhost:8080/ProjectServlet/contactBook \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ada","lastName":"Lovelace","number":"123-456-7890"}'
```

Get all contacts:

```bash
curl http://localhost:8080/ProjectServlet/contactBook
```

Update a contact:

```bash
curl -X PATCH "http://localhost:8080/ProjectServlet/contactBook?id=1" \
  -H "Content-Type: application/json" \
  -d '{"email":"ada@example.com","notes":"First computer programmer"}'
```

Move a contact to trash:

```bash
curl -X PATCH "http://localhost:8080/ProjectServlet/contactBook?id=1&action=trash"
```

Restore a contact:

```bash
curl -X PATCH "http://localhost:8080/ProjectServlet/contactBook?id=1&action=restore"
```

Delete a contact:

```bash
curl -X DELETE "http://localhost:8080/ProjectServlet/contactBook?id=1"
```

## Why This Project Matters

This project shows practical backend engineering fundamentals: HTTP request handling, JSON processing, relational persistence, layered architecture, unit testing, and deployment packaging. It is intentionally built with core Java web technologies, making the implementation transparent and useful for understanding what larger frameworks often handle automatically.
