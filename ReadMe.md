# Service Monitor

A full-stack web application for monitoring the availability of external services and APIs.

The application allows users to register services, monitor their availability automatically, and manage them through a React-based dashboard.

## Features

- Add services using a name and URL.
- View all registered services.
- Update existing services.
- Delete services.
- Automatically monitor service availability.
- Display service status as UP, DOWN, or UNKNOWN.
- Validate service names and URLs.
- Display meaningful error messages.
- Store service information in a MySQL database.
- Automatically refresh the dashboard.

## Technology Stack

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Web
- Maven

### Frontend
- React
- JavaScript
- HTML
- CSS
- Vite

### Database and Infrastructure
- MySQL 8.4
- Docker
- Docker Compose

### Testing
- JUnit 5
- Mockito

## Architecture

The application consists of three main components:

1. **Frontend:** React dashboard for managing and displaying monitored services.
2. **Backend:** Spring Boot REST API handling business logic, validation, and service monitoring.
3. **Database:** MySQL for persistent storage of monitored services.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/services` | Retrieve all services |
| POST | `/api/services` | Register a new service |
| PUT | `/api/services/{id}` | Update a service |
| DELETE | `/api/services/{id}` | Delete a service |

## Service Monitoring

The backend periodically checks registered services using HTTP GET requests.

- HTTP responses with status codes between 200 and 399 are considered UP.
- Other HTTP responses and connection failures are considered DOWN.
- Services are checked automatically every 30 seconds.

## Running the Application

### Prerequisites

- Java 21
- Node.js and npm
- Docker Desktop
- IntelliJ IDEA or another Java IDE

### 1. Start the database

From the project root directory, run:

```bash
docker compose up -d
```

### 2. Start the backend

Open the project in IntelliJ IDEA and run the Spring Boot application:

`ServiceMonitorApplication.java`

The backend starts at:

`http://localhost:8080`

### 3. Start the frontend

Open a terminal and navigate to the frontend directory:

```bash
cd frontend
npm install
npm run dev
```

The frontend is available at:

`http://localhost:5173`

## Running Tests

Run the backend tests using Maven:

```bash
./mvnw test
```

The project includes unit tests using JUnit 5 and Mockito.

## Future Improvements

- Add user authentication and authorization.
- Implement monitoring history and uptime statistics.
- Add configurable polling intervals.
- Introduce automated integration tests.
- Improve monitoring performance for larger numbers of services.

## Shahow Kakavandy

Developed as a full-stack software development project.