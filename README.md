# Mortgage Service

This service provides the functionality to:
- Retrieve mortgage interest rates
- Check mortgage feasibility and calculate monthly costs.

## Tech stack:

- Java 21
- Spring Boot 4
- Gradle
- JUnit 5

## API Endpoints

### 1. Get Mortgage Interest Rates
- **Endpoint:** `GET /api/interest-rates`
- **Description:** Get interest rates for available maturity periods.
- **Example Response:**

```json
[
  {
    "maturityPeriod": 1,
    "interestRate": 4.5,
    "lastUpdate": "2026-01-07T14:38:53.916741Z"
  },
  {
    "maturityPeriod": 3,
    "interestRate": 4.85,
    "lastUpdate": "2026-01-07T14:38:53.916741Z"
  }
]
```

### 2. Check Mortgage Feasibility
- **Endpoint:** `POST /api/mortgage-check`
- **Description:** Checks if a mortgage is feasible based on the mortgage request and calculates  monthly
    costs.
- **Request Body:** Example Request:
```json
    {
        "income": 5000,
        "maturityPeriod": 30,
        "loanValue": 20000,
        "homeValue": 20000
    }
```
- **Response:** An object containing feasibility status and monthly cost.
- **Example Response:**
```json
{
  "feasible": true,
  "monthlyCost": 133.06,
  "messages": [
    "Mortgaged is feasible."
  ]
}
```

## Running the Service

The project uses Gradle as the build tool. Use the following commands in the project root directory:

### To build the project:

```bash
./gradlew build
```

### To run the service:

```bash
./gradlew bootRun
```

The application will start on http://localhost:8080

### To run tests:

```bash
./gradlew test
```

## Swagger UI
To ease API testing, Swagger UI is also implemented.
Once the application is running, you can access the Swagger UI for API documentation and testing at:
http://localhost:8080/swagger-ui.html
