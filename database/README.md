# The Database Source of this project
## Create database by Docker
1. Pull image using:
    ```
    docker pull postgres
    ```
2. Create a PostgreSQL database by name ***postgres-container*** and the password is ***admin***, map the internal port (5432) to external port (5432) like the following command:
   ```
   docker run --name postgres-container -e POSTGRES_PASSWORD=admin -p 5432:5432 -d postgres
   ```
3. Run the container in **bash**:
    ```
    docker exec -it <ID of container> bash
    ```
4. Connect to docker:
   ```
   psql -h localhost -p 5432 -U postgres
   ```
5. Create two databases:
    ```
    postgres=# create database user_center;
    CREATE DATABASE
    psotgres=# create database file_manager;
    CREATE DATABASE
    ```
6. Use DBeaver to connect to the database with the following information:
- Host: localhost
- Port: 5432
- Database: "user_center" or "file_manager" ...
- Username: postgres
- Password: admin

