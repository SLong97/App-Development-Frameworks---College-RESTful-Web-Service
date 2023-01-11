# App-Development-Frameworks---College-RESTful-Web-Service

### For each department:

    department title (unique, so perhaps the ID(PK))
    department email (unique)

### For each office

    office number (unique so perhaps the ID(PK))
    maximum occupancy
    current occupancy

### The REST API service should provide the following functionality

  1. find all departments
  2. find all offices
  3. find all offices in a department
  4. find a department by its ID
  5. find an office by its ID
  6. find all empty offices
  7. find all offices with space for staff
  8. add a new department
  9. add a new office

### Offices are often repurposed, so your service should 

- delete an office

### Offices are often reallocated to a different department

- move an office to a different department

### Staff mobility means that your service should

- update the number in an office, subject to not exceeding the maximum and not being a negative number.


&nbsp;
&nbsp;


## Implementation and Architecture

### You must use

  - H2 embedded database
  - Maven
  - Spring Boot
  - JPA
  - REST
  - HATEOAS
  - Security

### You may use

  - Lombok

A service layer is not required in this application to reduce the amount of coding you have to do. Please note that in industry you would (probably) be told to use one. 

This means that the controller will deal with exceptions, taking on the role of the service layer.


### AOP

Demonstrate your knowledge of Aspect Oriented Programming in Spring by creating two AOP pointcuts, logging data from those pointcuts.

The pointcuts can be at class level or method level.

Aside: Logging is not the only thing to be done at a pointcut but it serves to demonstrate how to implement AOP


### Requests

Requests should be applied as follows:

  - PUT - for updates that overwrite the record (not needed in this project but here for reference)
  - PATCH - for updates that edit a part of the record
  - POST - for new 
  - DELETE - deleting
  - GET - getting data

Requests should return status codes
Links to an external site. appropriate to the result.


### HATEOAS

Apply HATEOAS principles to your RESTful API. In practical terms this means you add links to your DTOs.


&nbsp;
&nbsp;


## Security

There are two roles - HOS (Head of School) and HOD (head of department).

### HOS and HOD staff can

    add a new office
    delete an office
    modify the number of people in an office

### HOS staff

    add a department
    move an office from one department to another

All other requests require no authentication.

### Validation

When data is received on the client side it should be validated and your web service should return an appropriate response code and message to let the client know what went wrong.


&nbsp;
&nbsp;


## Unit Tests

If you have not implemented security just omit the authorisation in the following tests. 

Create units tests for the web service using MockMvc, which is built into Spring tests

  - get all offices 
  - delete an office
    - office exists and is deleted (by HOD or HOS)
    - office does not exist and cannot be deleted
    - office exists but cannot be deleted because not user was provided
  - posting a department you must write several requests
    - data is correct and it can be created (by a HOS)
    - data is correct but the user is not HOS (only if you have added security)
    - data is correct but no user provided (only if you have added security)
    - data correct but cannot be created because of a conflict
    - data not able to bind to the DTO e.g. a required field is blank or a constraint is not adhered to
