# URL Shortener API

A Spring Boot REST API service that converts long URLs into compact, manageable short URLs with full CRUD operations and persistence.

## 🚀 Features

- **URL Shortening**: Convert long URLs to short 6-character alphanumeric codes
- **URL Retrieval**: Retrieve original URLs from short URLs with automatic redirection
- **Duplicate Handling**: Same long URL always produces the same short URL
- **URL Validation**: Validates URL format before processing
- **Collision Prevention**: Handles potential collisions by increasing URL length if needed
- **CRUD Operations**: Create, read, update, and delete URL mappings
- **Enable/Disable**: Enable or disable URL mappings without deletion
- **Statistics**: Get usage statistics and mapping counts
- **Persistence**: H2 database with file-based storage
- **Health Checks**: Built-in health monitoring with Spring Actuator
- **Docker Support**: Full containerization with Docker and Docker Compose

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: H2 (file-based)
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Validation**: Spring Validation
- **Monitoring**: Spring Actuator

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (for containerized deployment)

## 🏃‍♂️ Quick Start

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd DesafioMeli
   ```

2. **Build the application**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   java -jar target/url-shortener-api-1.0.0.jar
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
   - Health Check: `http://localhost:8080/actuator/health`

### Docker Deployment

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Or build and run manually**
   ```bash
   # Build the image
   docker build -t url-shortener-api .
   
   # Run the container
   docker run -p 8080:8080 url-shortener-api
   ```

## 📚 API Documentation

### Base URL
```
http://localhost:8080
```

### Endpoints

#### 1. Shorten URL
**POST** `/shorten`

Creates a new short URL or returns existing one if the long URL was already shortened.

**Request Body:**
```json
{
  "longUrl": "https://www.example.com/very-long-url-with-many-parameters"
}
```

**Response:**
```json
{
  "shortUrl": "aB3x9K",
  "longUrl": "https://www.example.com/very-long-url-with-many-parameters",
  "isNew": true
}
```

**Status Codes:**
- `200 OK`: URL shortened successfully
- `400 Bad Request`: Invalid URL format

#### 2. Redirect to Original URL
**GET** `/{shortUrl}`

Redirects to the original long URL.

**Parameters:**
- `shortUrl` (path): The short URL code

**Response:**
- `302 Found`: Redirects to original URL
- `404 Not Found`: Short URL doesn't exist or is disabled

#### 3. Get All Mappings
**GET** `/`

Returns all URL mappings in the system.

**Response:**
```json
[
  {
    "shortUrl": "aB3x9K",
    "longUrl": "https://www.example.com/very-long-url",
    "enabled": true
  },
  {
    "shortUrl": "mN7pQ2",
    "longUrl": "https://github.com/features/actions",
    "enabled": true
  }
]
```

#### 4. Update URL Mapping
**PUT** `/{shortUrl}`

Updates the long URL for an existing short URL.

**Parameters:**
- `shortUrl` (path): The short URL to update

**Request Body:**
```json
{
  "newLongUrl": "https://www.new-example.com/updated-url"
}
```

**Status Codes:**
- `200 OK`: URL updated successfully
- `400 Bad Request`: Invalid URL format
- `404 Not Found`: Short URL doesn't exist

#### 5. Enable/Disable URL Mapping
**PUT** `/{shortUrl}/enable`

Enables or disables a URL mapping without deleting it.

**Parameters:**
- `shortUrl` (path): The short URL to enable/disable

**Request Body:**
```json
{
  "enabled": false
}
```

**Status Codes:**
- `200 OK`: Status updated successfully
- `404 Not Found`: Short URL doesn't exist

#### 6. Get Statistics
**GET** `/stats`

Returns statistics about the URL shortener.

**Response:**
```json
{
  "totalMappings": 42
}
```

#### 7. Check URL Existence
**GET** `/{shortUrl}/exists`

Checks if a short URL exists in the system.

**Parameters:**
- `shortUrl` (path): The short URL to check

**Response:**
```json
{
  "exists": true
}
```

## 🐳 Docker Documentation

### Building the Image

```bash
# Build the Docker image
docker build -t url-shortener-api .

# Build with custom tag
docker build -t url-shortener-api:v1.0.0 .
```

### Running with Docker

```bash
# Run the container
docker run -p 8080:8080 url-shortener-api

# Run with custom port mapping
docker run -p 9000:8080 url-shortener-api

# Run with volume for data persistence
docker run -p 8080:8080 -v url-data:/app/data url-shortener-api
```

### Docker Compose

The project includes a `docker-compose.yml` file for easy deployment:

```bash
# Start all services
docker compose up

# Start in background
docker compose up -d

# Rebuild and start
docker compose up --build

# Stop services
docker compose down

# Stop and remove volumes
docker compose down -v
```

### Docker Configuration

The Docker setup includes:

- **Multi-stage build** for optimized image size
- **Health checks** with curl
- **Volume persistence** for H2 database
- **Environment variables** for JVM tuning
- **Network isolation** with custom bridge network

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `JAVA_OPTS` | `-Xmx512m -Xms256m` | JVM memory settings |
| `SPRING_PROFILES_ACTIVE` | `docker` | Spring profile for Docker |

## 🗄️ Database

The application uses H2 database with file-based persistence:

- **Database Type**: H2 (in-memory/file)
- **Storage**: File-based (`./data/urlshortener`)
- **Console**: Available at `/h2-console`
- **Credentials**: 
  - Username: `sa`
  - Password: `password`

### Database Schema

```sql
CREATE TABLE url_mapping_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    short_url VARCHAR(255) NOT NULL UNIQUE,
    long_url TEXT NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔧 Configuration

### Application Properties

Key configuration options in `application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:file:./data/urlshortener
    username: sa
    password: password
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

### Profiles

- **Default**: Standard development configuration
- **Docker**: Optimized for containerized deployment

## 📊 Monitoring

### Health Checks

- **Endpoint**: `/actuator/health`
- **Docker Health Check**: Built into Dockerfile
- **Response**: JSON with application status

### Metrics

Available metrics at `/actuator/metrics`:
- HTTP request metrics
- JVM metrics
- Database connection metrics

## 🧪 Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/urlshortener/
│   │       ├── controller/     # REST controllers
│   │       ├── dto/           # Data transfer objects
│   │       ├── entity/        # JPA entities
│   │       ├── repository/    # Data access layer
│   │       ├── service/       # Business logic
│   │       └── UrlShortenerApplication.java
│   └── resources/
│       ├── application.yml
│       └── application-docker.yml
└── test/
    └── java/
        └── com/example/urlshortener/
            └── UrlShortenerApplicationTests.java
```

## 🚀 Deployment

### Production Considerations

1. **Database**: Consider using PostgreSQL or MySQL for production
2. **Security**: Implement authentication and authorization
3. **Rate Limiting**: Add rate limiting to prevent abuse
4. **Logging**: Configure proper logging and monitoring
5. **SSL/TLS**: Use HTTPS in production
6. **Caching**: Add Redis for caching frequently accessed URLs

### Environment Variables

```bash
# Database configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/urlshortener
SPRING_DATASOURCE_USERNAME=dbuser
SPRING_DATASOURCE_PASSWORD=dbpassword

# JVM settings
JAVA_OPTS=-Xmx2g -Xms1g

# Application settings
SERVER_PORT=8080
```
