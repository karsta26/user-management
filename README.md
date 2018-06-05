# User management system

Project in Spring Framework

## Deployment
App is available here: https://user-management-appl.herokuapp.com/

## Info

- Application was built with: Spring Boot, Spring MVC, Spring Data.
- Application stores data in PostgreSQL database. 
- Data is available via the REST API.
- All pages were generated with Thymeleaf template engine.

## Functionalities
- Managing users
  - List of users
  - Adding a user
  - Deleting a user
  - Editing a user

- Managing user groups
  - List of user groups
  - Adding a user group
  - Deleting a user group
  - Editing user group

## Data structure
- User
  - User Name
  - Password
  - First Name
  - Last Name
  - Date of birth
  - List of User Groups to which the User belongs
  
- User Group
  - Group Name
  - List of Users who belong to the Group
