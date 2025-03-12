
# FIFA Management System (FMS)

## Overview
The **FIFA Management System (FMS)** is a Java-based application designed to manage and analyze FIFA-related data, including player and club information. It leverages **Spring Boot**, **MongoDB**, and **Neo4j** to provide efficient data storage and processing.

## Features
- **User Authentication & Security**: Secured APIs using **Spring Security**.
- **Player & Club Management**: CRUD operations for FIFA players and clubs.
- **Neo4j Integration**: Graph database support for managing player-club relationships.
- **MongoDB Aggregation**: High-performance queries for analyzing FIFA statistics.
- **RESTful APIs**: Modular API architecture for seamless integration.

## Tech Stack
- **Backend**: Java, Spring Boot, Spring Security
- **Database**: MongoDB, Neo4j
- **Security**: BCrypt, JWT Authentication
- **Build Tool**: Maven

## Installation & Setup
### Prerequisites
- Java 17+
- MongoDB
- Neo4j Database
- Maven

### Clone Repository
```sh
git clone https://github.com/Rezacs/Fifa.git
cd Fifa
```

### Configuration
1. **MongoDB Setup**:
   - Ensure MongoDB is running on `localhost:27017`.
   - Create a database named `fifaDB`.
   
2. **Neo4j Setup**:
   - Ensure Neo4j is running on `localhost:7687`.
   - Update credentials in `application.properties`.
   
3. **Environment Variables** (Optional for secrets):
   - Configure in `src/main/resources/application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/fifaDB
   spring.neo4j.uri=bolt://localhost:7687
   spring.neo4j.authentication.username=neo4j
   spring.neo4j.authentication.password=password
   ```

### Build & Run
```sh
mvn clean install
mvn spring-boot:run
```

## API Endpoints
### Authentication
| Method | Endpoint          | Description            |
|--------|------------------|------------------------|
| POST   | `/api/v1/auth/register` | Register a new userNode |
| GET    | `/api/v1/auth/me` | Get logged-in userNode info |

### Player Management
| Method | Endpoint          | Description            |
|--------|------------------|------------------------|
| POST   | `/api/v1/p/create-new-player` | Add a new player |
| PUT    | `/api/v1/p/edit/{mongoId}` | Edit player details |
| DELETE | `/api/v1/p/deletePlayer` | Remove a player |

### Club Management
| Method | Endpoint          | Description            |
|--------|------------------|------------------------|
| POST   | `/api/v1/c/create-new-club` | Add a new club |
| PUT    | `/api/v1/c/edit/{mongoId}` | Edit club details |
| DELETE | `/api/v1/c/deleteClub` | Remove a club |

## Security & Authentication
- Uses **Spring Security** for authentication.
- **BCrypt** password encoding for secure storage.

## Contributions
1. **Fork the repository**
2. **Create a feature branch**: `feature/new-feature`
3. **Commit your changes**
4. **Submit a pull request**

## License
This project's Database (https://www.kaggle.com/datasets/stefanoleone992/ea-sports-fc-24-complete-player-dataset?select=male_teams.csv) is licensed under the MIT License.

