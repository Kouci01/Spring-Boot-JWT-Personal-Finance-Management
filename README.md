# Spring Security Implementation in Personal Finance Management
 Spring Boot Security with JWT

## The Personal Finance Management application 
is a robust and secure platform designed to help users manage their finances effectively. It allows users to track income, expenses, savings, and investments while providing insights to promote better financial decisions.

## Technology Stack
- Spring Boot: The backbone of the application, providing a streamlined framework for developing the RESTful APIs and core business logic.
- Spring Security with JWT: Ensures secure authentication and authorization. JWT (JSON Web Tokens) enables stateless, scalable security mechanisms for user sessions.
- MyBatis: Facilitates efficient and straightforward integration with the database, ensuring clean and maintainable SQL mappings.
- MySQL: The relational database used for storing all financial data, user information, and configurations.
- Spring RESTful: The architecture software style that uses in this project, where the API is called and response the requested resources state to the client system

# Key Features
### 1. User Authentication and Authorization:
* Implements Spring Security with JWT for stateless authentication.
* Passwords are securely hashed using BCrypt.
* Role-based access control ensures secure API endpoints. (Still in process ..)
### 2. User Management:
* Users can register with email and password.
* Includes endpoints for user login and token renewal.
* Profile management features. (Still in process...)
### 3. Financial Management:
* Users can record and categorize incomes and expenses.
* Budget setting and tracking to ensure users stay within limits.
* Savings goals to help users achieve financial milestones.
* Investment tracking for portfolio management.
### 4. Insights and Reporting:
* Monthly and yearly reports summarizing financial activity.
### 5. Secure API Endpoints:
* All API endpoints are protected using JWT authentication. (Except Login & Register access)
* Sensitive user data is encrypted and follows best practices for secure data handling.

# Application Architecture
## 1. Controller Layer:
- Handles user requests and maps them to appropriate services.
- Implements endpoints for authentication, financial management, and reporting.
## 2. Service Layer:
- Contains business logic for managing user accounts, and transactions (including income and outcome).
- Interacts with the mapper layer to fetch or persist data.
## 3. Mapper Layer:
- Uses MyBatis to communicate with the MySQL database.
- Ensures efficient database queries and clean separation of SQL logic.
## 4. Security Layer:
- Configures Spring Security to intercept unauthorized requests.
- JWT filters validate tokens for protected resources.
- Implements custom UserDetailsService for loading user-specific data.

# JWT Integration
* Token Generation: Upon successful login, the application generates a JWT containing user details and roles.
* Token Validation: Incoming requests to protected endpoints must include the JWT in the Authorization header. A filter validates the token, ensuring its integrity and authenticity.
* Refresh Tokens: To avoid frequent logins, the application uses a mechanism to refresh expired tokens securely. (The token nows is expired within 2 hours start from token generated)

# MyBatis and MySQL Integration
* MyBatis simplifies database operations using mapper interfaces and XML-based SQL configurations.

# How It All Works
1. User Registration and Login:
   * Users register by providing an email and password.
   * Upon login, credentials are validated, and a JWT is issued. 
2. Financial Tracking:
   * Users log transactions, assign categories, and track their budgets.
3. Secure Access:
   * All API requests are secured by JWT authentication.
   * Admin-specific endpoints are protected using role-based security. (Still in the process..)
4. Data Management:
   * MyBatis efficiently manages SQL operations with MySQL.
   * Complex queries for reporting and insights are handled seamlessly.

## Benefits of the Project
1. Scalability: The modular architecture and stateless JWT security enable horizontal scaling.
2. Performance: MyBatis ensures optimized database interaction, reducing overhead.
3. Security: Leveraging Spring Security with JWT ensures robust protection against unauthorized access.
4. Maintainability: Clean separation of layers and the use of MyBatis for SQL management make the application easy to maintain and extend.
