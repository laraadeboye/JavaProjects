# Student Record Management System
A simple Java console application for managing student records.
Features

## Add new students
- Update student information
- View student details
- View all students
- Delete students
- Input validation and error handling

## Requirements

- Java JDK 8 or higher

## How to Run


- Compile the program:
```
bashjavac AdministratorInterface.java
```
- Run the program:
```
bashjava AdministratorInterface
```
## Usage
The program displays a menu with 6 options:

- Add New Student - Enter student ID, name, age, and grade
- Update Student Information - Modify existing student details
- View Student Details - Display information for a specific student
- View All Students - Show all registered students
- Delete Student - Remove a student from the system
- Exit System - Close the application

## Example
```
--- ADMINISTRATOR MENU ---
1. Add New Student
2. Update Student Information
3. View Student Details
4. View All Students
5. Delete Student
6. Exit System
--------------------------

Enter your choice (1-6): 1

========================================
       ADD NEW STUDENT
========================================
Enter Student ID: S001
Enter Student Name: John Smith
Enter Student Age: 18
Enter Student Grade: Grade 12

Student added successfully!
Total students in system: 1
```
## Project Structure
The application contains three classes:

- Student - Stores student information (name, ID, age, grade)
- StudentManagement - Manages all student records using static methods
- AdministratorInterface - Provides the user interface and menu system


### Validation Rules

- Student ID must be unique
- Age must be between 5 and 100
- Name cannot exceed 100 characters
- Grade cannot exceed 20 characters
- All required fields must be filled