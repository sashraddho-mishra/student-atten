# Attendance Management (Spring Boot)

This is a simple Spring Boot attendance management system. It uses Spring Data JPA, Spring Security and Thymeleaf.

Default configuration assumes a local MySQL (XAMPP) database named `attendance` on `localhost:3306` with user `root` and no password. Adjust `src/main/resources/application.properties` if your setup differs.

Default teacher account (created by data initializer):
- username: `admin`
- password: `admin`
- course: `BBA` (you can change in `DataInitializer`)

Quick run (Windows PowerShell):

1. Create the database in MySQL (XAMPP):

```powershell
# Start MySQL in XAMPP, then in a MySQL client run:
# CREATE DATABASE attendance CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Build and run the application from project root:

```powershell
# Make sure you are in the project root where mvnw resides
.\mvnw -DskipTests=true spring-boot:run
```

3. Open in browser:
http://localhost:8081/login

Notes:
- To change the port or datasource, edit `src/main/resources/application.properties`.
- The add-student form will show the message `cannot add students with duplicate roll` when a duplicate roll is attempted.
- Attendance marking now updates an existing attendance record if one exists for the same student, subject and date.

If you want, I can add a few unit tests next or prepare a small export of the DB schema.
