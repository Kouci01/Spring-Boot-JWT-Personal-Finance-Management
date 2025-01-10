# Spring Security Implementation in Personal Finance Management

## Spring Boot Security with JWT and OAuth 2.0 (Google Authentication)

The Personal Finance Management application is a robust and secure platform designed to help users manage their finances effectively. It allows users to track income, expenses, savings, and investments while providing insights to promote better financial decisions.

---

## Technology Stack

- **Spring Boot**: The backbone of the application, providing a streamlined framework for developing the RESTful APIs and core business logic.
- **Spring Security with JWT**: Ensures secure authentication and authorization. JWT (JSON Web Tokens) enables stateless, scalable security mechanisms for user sessions.
- **OAuth 2.0 with Google Authentication**: Allows users to log in using their Google accounts, offering a convenient and secure authentication method.
- **MyBatis**: Facilitates efficient and straightforward integration with the database, ensuring clean and maintainable SQL mappings.
- **MySQL**: The relational database used for storing all financial data, user information, and configurations.
- **Spring RESTful**: The architecture software style used in this project, where the API is called and responds with the requested resource state to the client system.

---

## Key Features

### 1. User Authentication and Authorization:
- Implements Spring Security with JWT for stateless authentication.
- Integrates OAuth 2.0 for Google Authentication to allow Google account login.
- Passwords are securely hashed using BCrypt.
- Role-based access control ensures secure API endpoints. *(Still in process ...)*

### 2. User Management:
- Users can register with email and password.
- Includes endpoints for user login, token renewal, and Google login.
- Profile management features. *(Still in process...)*

### 3. Financial Management:
- Users can record and categorize incomes and expenses.
- Budget setting and tracking to ensure users stay within limits.
- Savings goals to help users achieve financial milestones.
- Investment tracking for portfolio management.

### 4. Insights and Reporting:
- Monthly and yearly reports summarizing financial activity.

### 5. Secure API Endpoints:
- All API endpoints are protected using JWT authentication (except Login & Register access).
- OAuth 2.0 endpoints ensure secure access through Google authentication.
- Sensitive user data is encrypted and follows best practices for secure data handling.

---

## Application Architecture

### 1. Controller Layer:
- Handles user requests and maps them to appropriate services.
- Implements endpoints for authentication (including Google OAuth), financial management, and reporting.

### 2. Service Layer:
- Contains business logic for managing user accounts, and transactions (including income and expenses).
- Interacts with the mapper layer to fetch or persist data.

### 3. Mapper Layer:
- Uses MyBatis to communicate with the MySQL database.
- Ensures efficient database queries and clean separation of SQL logic.

### 4. Security Layer:
- Configures Spring Security to intercept unauthorized requests.
- JWT filters validate tokens for protected resources.
- Integrates OAuth 2.0 for handling Google Authentication.
- Implements custom `UserDetailsService` for loading user-specific data.

---

## JWT and OAuth Integration

### JWT Integration:
- **Token Generation**: Upon successful login, the application generates a JWT containing user details and roles.
- **Token Validation**: Incoming requests to protected endpoints must include the JWT in the Authorization header. A filter validates the token, ensuring its integrity and authenticity.
- **Refresh Tokens**: To avoid frequent logins, the application uses a mechanism to refresh expired tokens securely. *(The token is currently set to expire within 2 hours of generation.)*

### OAuth 2.0 Integration:
- **Google Authentication**: Users can log in using their Google accounts for a seamless authentication experience.
- **Token Exchange**: The application interacts with Google’s OAuth 2.0 endpoints to validate user credentials and retrieve profile information.
- **Secure Access**: OAuth 2.0 ensures enhanced security and convenience, especially for users who prefer not to create new credentials.

---

## MyBatis and MySQL Integration

- MyBatis simplifies database operations using mapper interfaces and XML-based SQL configurations.
- Ensures efficient handling of complex queries for reporting and insights.

---

## How It All Works

### User Registration and Login:
- Users register by providing an email and password or by using their Google accounts.
- Upon login, credentials are validated, and a JWT is issued.

### Financial Tracking:
- Users log transactions, assign categories, and track their budgets.

### Secure Access:
- All API requests are secured by JWT authentication.
- Admin-specific endpoints are protected using role-based security. *(Still in process...)*

### Data Management:
- MyBatis efficiently manages SQL operations with MySQL.
- Complex queries for reporting and insights are handled seamlessly.

---

## Benefits of the Project

- **Scalability**: The modular architecture, stateless JWT security, and OAuth 2.0 integration enable horizontal scaling.
- **Performance**: MyBatis ensures optimized database interaction, reducing overhead.
- **Security**: Leveraging Spring Security with JWT and OAuth 2.0 ensures robust protection against unauthorized access.
- **Maintainability**: Clean separation of layers and the use of MyBatis for SQL management make the application easy to maintain and extend.

---

### Development Notes:
- **OAuth 2.0 with Google Authentication**:
    - For oAuth2.0 if got redirect back to authentication form, make sure debug the flow of authentication in the backend including the token authorization such as doFilterinternal
    - JWT only can parse email without '.' before @ (ex: test.test@example.com). Solution: Change '.' in the email to another character such as $,# (ex: test#test@example.com) but don't forget each JWT translation change it back to the correct email format.
    - DefaultAuthorizationCodeTokenResponseClient for OAuth2AccessTokenResponseClient is deprecated, use other such as RestClientAuthorizationCodeTokenResponseClient
    - For oAuth2.0 if use custom authenticationManager for JWT, make sure to set authenticationProvider (the parameter can use the default one).
- **Role-based Access Control and Profile Management**: Currently under development to enhance user and admin functionalities.
- **The flow of oAuth and JWT**: User -> Front End request Back End -> Google authentication -> give respond and generate JWT for the Google authentication user info -> Use the JWT for each request -> Backend use the JWT to get the currect user information.
