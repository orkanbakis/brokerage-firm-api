# Brokerage Firm API

This project is a brokerage firm API built with Java, Spring Boot, and Gradle. It provides functionalities for placing and managing orders, processing transactions, and more.

## Prerequisites

- Java 21
- Docker
- Docker Compose
- Gradle

## Getting Started

### Running the Application

1. **Clone the repository:**

    ```sh
    git clone https://github.com/your-username/brokerage-firm-api.git
    cd brokerage-firm-api
    ```

2. **Build the Docker images and start the containers:**

    ```sh
    docker-compose up --build
    ```

   This command will build the Docker images and start the following services:
    - `app`: The main Spring Boot application.
    - `postgres`: PostgreSQL database.
    - `redis`: Redis server.
    - `rabbitmq`: RabbitMQ server.

3. **Access the application:**

   The application will be available at `http://localhost:8080`.

### Running the Tests

1. **Run the tests using Gradle:**

    ```sh
    ./gradlew test
    ```

   This command will execute all the unit tests in the project.

## Project Structure

- `src/main/java/com/brokeragefirm`: Contains the main application code.
- `src/test/java/com/brokeragefirm`: Contains the unit tests.
- `src/main/resources`: Contains the application configuration files.
- `build.gradle`: The Gradle build file.
- `Dockerfile`: The Dockerfile for building the application image.
- `docker-compose.yml`: The Docker Compose file for setting up the development environment.

## Configuration

The application configuration is managed through the `application.yaml` file located in `src/main/resources`. Key configurations include:

- **Server Port:** The application runs on port `8080`.
- **Database:** PostgreSQL is used as the database.
- **Redis:** Redis is used for caching.
- **RabbitMQ:** RabbitMQ is used for message queuing.

## How the Project Works

### Order Management

The project provides functionalities for placing and managing orders. The `OrderPlacingService` and `OrderProcessingService` handle the business logic for placing and processing orders, respectively.

### Transaction Management

The project also handles transactions such as deposits and withdrawals. The `TransactionService` manages the creation and completion of transactions.

### Unit Tests

Unit tests are written using JUnit and Mockito. The tests cover various scenarios, including happy paths and edge cases, to ensure the correctness of the application.

## Using Postman Collection

To test the API endpoints using Postman, follow these steps:

1. **Import the Postman Collection:**

    - Download the Postman collection file from the repository: [Brokerage Firm API.postman_collection.json](Brokerage Firm Case.postman_collection.json)
    - Open Postman and click on `Import` in the top left corner.
    - Select the downloaded JSON file to import the collection.

2. **Set Up Environment Variables:**

    - Create a new environment in Postman.
    - Add the following variables:
        - `baseUrl`: `http://localhost:8080`
        - `adminUsername`: `admin`
        - `adminPassword`: `s3cr3t`

3. **Run the Requests:**

    - Select the imported collection and the created environment.
    - Execute the requests to interact with the API.

## Additional Information

- **Docker Compose:** The `docker-compose.yml` file sets up the development environment with PostgreSQL, Redis, and RabbitMQ.
- **Gradle:** The project uses Gradle for build automation. The `build.gradle` file contains the necessary dependencies and build configurations.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
