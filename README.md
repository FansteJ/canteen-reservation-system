# Canteen Reservation System

A system for managing meal reservations in student canteens, supporting management of canteens, students, and reservations.

---

## Technologies and Versions
| Technology        | Version         |
|------------------|----------------|
| Java              | 17             |
| Spring Boot       | 3.x            |
| Spring Data JPA   | 3.x            |
| H2 Database       | 2.x (in-memory)|
| Maven             | 4.x            |
| Lombok            | 1.18.x         |

---

## Environment Setup
1. Install [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)  
2. Install [Maven](https://maven.apache.org/install.html)  
3. Clone the repository:  

```bash
git clone https://github.com/FansteJ/canteen-reservation-system.git
cd canteen-reservation-system
```

## Build Application

From the root folder of the project, run:

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

### The application will be available at:

http://localhost:8080

## API Documentation

### Students API

#### Create Student
**POST** `/students`  
**Request Payload:**
```json
{
  "name": "Nikola Petrovic",
  "email": "nikola.petrovic@etf.com",
  "isAdmin": false
}
```
**Response:**
**201 Created**
```json
{
  "id": "1",
  "name": "Nikola Petrovic",
  "email": "nikola.petrovic@etf.com",
  "isAdmin": false
}
```
#### Get Student by ID
**GET** `/students/{id}`  
**Response:**
**200 OK**
```json
{
  "id": "1",
  "name": "Nikola Petrovic",
  "email": "nikola.petrovic@etf.com",
  "isAdmin": false
}
```
### Canteens API
#### Create Canteen (Admin Only)
POST `/canteens`  
Headers: studentId: {adminId}  
Request Payload:

```json
{
  "name": "Tri Kostura",
  "location": "Obilićev venac 4, Belgrade",
  "capacity": 100,
  "workingHours": [
    { "meal": "breakfast", "from": "08:00", "to": "10:00" },
    { "meal": "lunch", "from": "11:00", "to": "13:00" },
    { "meal": "dinner", "from": "17:00", "to": "19:00" }
  ]
}
```
Response:

201 Created

```json
{
  "id": "1",
  "name": "Tri Kostura",
  "location": "Obilićev venac 4, Belgrade",
  "capacity": 100,
  "workingHours": [
    { "meal": "breakfast", "from": "08:00", "to": "10:00" },
    { "meal": "lunch", "from": "11:00", "to": "13:00" },
    { "meal": "dinner", "from": "17:00", "to": "19:00" }
  ]
}
```
#### Get All Canteens
GET `/canteens`  
Response:

200 OK

```json
[
  {
    "id": "1",
    "name": "Tri Kostura",
    "location": "Obilićev venac 4, Belgrade",
    "capacity": 100,
    "workingHours": [ ... ]
  },
  {
    "id": "2",
    "name": "Velika Menza",
    "location": "Dr Sime Miloševića 4, Novi Sad",
    "capacity": 200,
    "workingHours": [ ... ]
  }
]
```
#### Get Canteen by ID
GET `/canteens/{id}`  
Response:

200 OK

```json
{
  "id": "1",
  "name": "Tri Kostura",
  "location": "Obilićev venac 4, Belgrade",
  "capacity": 100,
  "workingHours": [ ... ]
}
```
#### Update Canteen (Admin Only)
PUT `/canteens/{id}`  
Headers: studentId: {adminId}  
Request Payload:

```json
{
  "name": "Tri Kostura Updated"
}
```
Response:

200 OK

```json
{
  "id": "1",
  "name": "Tri Kostura Updated",
  "location": "Obilićev venac 4, Belgrade",
  "capacity": 100,
  "workingHours": [ ... ]
}
```
#### Delete Canteen (Admin Only)
DELETE `/canteens/{id}`  
Headers: studentId: {adminId}  
Response:

204 No Content

Get Status of All Canteens
GET `/canteens/status?startDate={date}&endDate={date}&startTime={time}&endTime={time}&duration={minutes}`  
Response:

200 OK

```json
[
  {
    "canteenId": "1",
    "slots": [
      { "date": "2025-10-31", "meal": "breakfast", "startTime": "09:00", "remainingCapacity": 30 },
      { "date": "2025-10-31", "meal": "lunch", "startTime": "11:00", "remainingCapacity": 22 }
    ]
  },
  ...
]
```
#### Get Status of One Canteen
GET `/canteens/{id}/status?startDate={date}&endDate={date}&startTime={time}&endTime={time}&duration={minutes}`  
Response:

200 OK

```json
{
  "canteenId": "1",
  "slots": [
    { "date": "2025-10-31", "meal": "breakfast", "startTime": "08:00", "remainingCapacity": 30 },
    { "date": "2025-10-31", "meal": "lunch", "startTime": "11:00", "remainingCapacity": 22 }
  ]
}
```
### Reservations API
#### Create Reservation
POST `/reservations`  
Request Payload:

```json
{
  "studentId": "123456",
  "canteenId": "2",
  "date": "2025-10-27",
  "time": "12:00",
  "duration": 30
}
```
Response:

201 Created

```json
{
  "id": "1",
  "status": "Active",
  "studentId": "123456",
  "canteenId": "2",
  "date": "2025-10-27",
  "time": "12:00",
  "duration": 30
}
```
#### Cancel Reservation
DELETE `/reservations/{id}`  
Headers: studentId: {id}  
Response:

200 OK

```json
{
  "id": "1",
  "status": "Cancelled",
  "studentId": "123456",
  "canteenId": "2",
  "date": "2025-10-27",
  "time": "12:00",
  "duration": 30
}
```

## Tests

The application contains unit and integration tests for key functionalities.

### Tested Features

#### Student Management

- Creating a student

- Fetching a student by ID

#### Canteen Management

- Creating a canteen (admin only)

- Fetching all canteens

- Fetching a canteen by ID

- Updating a canteen (admin only)

- Deleting a canteen (admin only)

#### Reservation Management

- Creating a reservation

- Only in the future

- Duration of 30 or 60 minutes

- Starts at full hour or half hour

- No overlapping reservation for the same student

- Canteen capacity is not exceeded

- Canceling a reservation

### Running Tests

From the project root directory, run:
```
mvn test
```

The results will show:

- Number of tests run

- Number of successful tests

- Number of failed tests (if any)

All implemented tests are automated and verify that the core business logic is working correctly.
