# Cloud drive project

![image](https://github.com/user-attachments/assets/b4e40cac-98b0-4771-84dd-780324ae55ba)
![image](https://github.com/user-attachments/assets/137ed35c-6879-462e-9e91-09a21eb3264a)

## Overview

Multi-user file cloud.
Service users can use it to upload(up to 500MB) and store files.

## Technologies

- Maven
- Backend
  - Spring Boot, Spring Security, Spring Sessions
  - Thymeleaf
- Frontend
  - HTML/CSS
  - Bootstrap 5
- DB
  - PostgreSQL
  - Spring Data JPA
  - Minio
- Test
  - JUnit5, Mockito
  - AssertJ
  - Testcontainers
- Docker Compose

## App Functionality

Work with users:
- Registration
- Authorization
- Logout

Work with files and folders:
- Upload files and folders
- Delete
- Rename

## Run
To run an application, you must set environment variables used in docker-compose file.
