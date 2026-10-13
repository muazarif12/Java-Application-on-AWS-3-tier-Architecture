# Java Login App

Spring Boot 2.7 web app (JSP pages, packaged as a WAR) where users register and log in. User records are stored in a MySQL database called `UserDB`, in the `Employee` table.

## Run locally

Requirements: Java 11 and Docker Desktop. Maven is not needed: the project includes the Maven wrapper (`mvnw`).

1. **Start MySQL** (from this folder):
   ```bash
   docker compose up -d --wait
   ```

2. **Build the app:**
   ```bash
   ./mvnw clean package            # Windows: .\mvnw.cmd clean package
   ```

3. **Run it** with the local database password from `docker-compose.yml`:
   ```bash
   # macOS/Linux
   DB_PASSWORD=localpass java -jar target/dptweb-1.0.war

   # Windows PowerShell
   $env:DB_PASSWORD="localpass"; java -jar target/dptweb-1.0.war
   ```

4. **Open** http://localhost:8080, register a user, then log in.

5. **Stop** with `Ctrl+C`, then `docker compose down`. Add `-v` to also delete the database data.

## Configuration

The database connection is read from environment variables, with defaults for local use:

| Variable | Default |
|---|---|
| `DB_HOST` | `localhost` |
| `DB_PORT` | `3306` |
| `DB_NAME` | `UserDB` |
| `DB_USERNAME` | `admin` |
| `DB_PASSWORD` | *(empty)* |

When deployed, set these as environment variables on the server.

## Database

The app creates the `Employee` table on startup if it does not exist (`DatabaseInitializer.java`):

```sql
CREATE TABLE IF NOT EXISTS Employee (
  id int unsigned auto_increment not null,
  first_name varchar(250),
  last_name varchar(250),
  email varchar(250),
  username varchar(250),
  password varchar(250),
  regdate timestamp,
  primary key (id),
  unique key uk_employee_username (username)
);
```

To look at the data locally:

```bash
docker exec -it java-login-mysql mysql -uadmin -plocalpass UserDB -e "SELECT id, username, email, regdate FROM Employee;"
```
