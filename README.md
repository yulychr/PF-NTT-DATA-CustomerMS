# Customer Management Microservice (CustomerMS)

## Description

This microservice manages the information of the bank's customers. It allows creating, reading, updating, and deleting customer records. Each customer has a unique identifier (ID) and a set of required attributes such as first name, last name, DNI (ID number), and email address.

## Endpoints

- **POST /clientes**: Creates a new customer.
- **GET /clientes**: Lists all registered customers.
- **GET /clientes/{id}**: Retrieves the details of a customer by their ID.
- **PUT /clientes/{id}**: Updates the information of an existing customer.
- **DELETE /clientes/{id}**: Deletes a customer by their ID.
  
## Business Rules

- The **DNI** of each customer must be unique.
- A customer cannot be deleted if they have active bank accounts associated with them.

## Customer Deletion Logic

To ensure that a customer can only be deleted if they have no active bank accounts associated, the CustomerMS uses RestTemplate to interact with the AccountMS microservice. When a deletion request is made, CustomerMS first sends a request to AccountMS to check if the customer has any active bank accounts.

- If the customer has active accounts, the deletion is blocked, and the customer cannot be removed.
- If no active accounts are found, the customer can be safely deleted from the CustomerMS.

This approach ensures the integrity of customer data and prevents accidental deletion of customers with active banking relationships.

## Technologies used

- **Programming Language**: Java 17
- **Framework**: Spring Boot
- **Database**: MySQL
- **ORM (Object-Relational Mapping)**: JPA (Java Persistence API)
- **Helper Libraries**: Lombok
- **Testing**:
  - **JUnit** (Unit Testing)
  - **Mockito** (Framework for mocking in unit tests)
- **Code Coverage**: JaCoCo
- **Code Style**: Checkstyle
- **API Documentation**: OpenAPI (Swagger)

## Postman Collection

You can find the Postman collection for testing the **CustomerMS** API at the following link:

[CustomerMS Postman Collection](https://www.postman.com/yulyschr/test-api-customerms/overview)

This collection includes all the endpoints and examples for testing the API.

## Database Setup

To set up the database for the project, you can use the provided SQL script:

- **[Download core_banking_system2.sql]()**: This script contains the necessary database schema and initial data for the core banking system.

You can run this script to create the required tables and populate the database with initial data for the system to function correctly.

### How to Run the Script
1. Download the script by clicking the link above.
2. Access your database management tool (MySQL).
3. Open the SQL script.
4. Execute the script in your database to set up the schema.

This will create the necessary database structure and ensure that the system has the required data to operate.

## DIAGRAMs

### Sequence diagram 

- **POST /customers**: This endpoint creates a new customer in the database. The diagram should show how CustomerMS interacts with the database to insert the new customer.
- **GET /customers**: This endpoint lists all registered customers, retrieving the data from the database.
- **GET /customers/{id}**: Similar to the previous one, but this endpoint is used to retrieve the details of a specific customer. The diagram should show the query to the database to get the customer’s data.
- **PUT /customers/{id}**: This endpoint allows updating the information of an existing customer. The diagram should show how the request is made to modify the customer’s data in the database.
- **DELETE /customers/{id}**: This endpoint checks whether a customer has active accounts associated with them. If the customer has active accounts, the deletion is blocked; otherwise, the customer is deleted from the database. The diagram should show how CustomerMS interacts with AccountMS to check for active accounts, and if there are none, delete the customer from the database.
  

![DIAGRAMA DE SECUENCIA DE -- CLIENTES -- FINAL drawio](https://github.com/user-attachments/assets/149333cc-b74f-4581-b3aa-cd8a614e4fa6)

## Documentation

You can find additional documentation for the project in the following documents:

- [Analysis of SOLID Principles and Design Patterns](https://github.com/yulychr/PF-NTT-DATA-CustomerMS/blob/main/docs/Analis%20de%20principios%20solid%20y%20patrones%20de%20dise%C3%B1o.docx)
- [Unit Testing and CheckStyle](https://github.com/yulychr/PF-NTT-DATA-CustomerMS/blob/main/docs/Pruebas%20unitarias%20y%20checkStyle.docx)


Please refer to these documents for a detailed analysis of the SOLID principles, design patterns used, unit testing practices, and code style checks.
