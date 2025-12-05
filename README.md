# payment-svc
My first microservice

payment-svc

payment-svc is a microservice designed to handle subscription payment processing for the MyRabbitry platform.
It manages transactions, generates weekly profit reports, and provides endpoints for retrieving and updating financial data.

⭐ Features

💳 Payment & Transaction Management

Create new transactions for subscription purchases

Retrieve all transactions or specific transaction details

📊 Profit Report Management

Automatically generate weekly profit reports

Retrieve:

The latest profit report

Older historical profit reports

Update the status of profit reports (e.g., marked as processed, reviewed, etc.)

🏗 Tech Stack

Language: Java 17

Framework: Spring Boot (REST API)

Tools & Libraries:

Lombok

Database: MySQL

Architecture: Three-layered architecture (Controller → Service → Repository)

No Thymeleaf, no Spring Security.

⚙️ Installation & Setup

No special installation steps are required.
Clone the repository and run it as a standard Spring Boot REST application:
Run the service:

No additional configuration or environment variables are needed.

▶️ Usage

The microservice exposes REST endpoints for processing subscription payments, generating reports, and retrieving transaction data.

There are no special runtime requirements.

🗂 Project Structure

The project follows a three-layered architecture:

Controller Layer: Handles REST requests

Service Layer: Business logic

Repository Layer: Persistence and database interactions

⚠️ Limitations

The service cannot validate real credit/debit card details.
It only supports card validation using mock data stored in cards.yml.

🚀 Future Improvements

Planned enhancements include:

Full real-world card validation

Integration with external payment gateways

👩‍💻 Author

Monika Nikolova — Bulgaria