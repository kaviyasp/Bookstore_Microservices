# 📚 Bookstore E-Commerce — Microservices Architecture

A full-stack **Bookstore E-Commerce backend** built with **Java 25**, **Spring Boot 4.x**, and **Spring Cloud**, decomposed into **12 independent microservices**. Each service owns its own database, exposes RESTful APIs, and communicates over HTTP (synchronous via Feign) or Kafka (asynchronous events).

---

> **Note:**  
> The complete source code for this project is available in the `dev` branch.  
> The `main` branch contains only project overview and documentation.

---

## 🏗️ Architecture Overview
```
Client → API Gateway (8080)
├── User Service (8081)
├── Admin Service (8082)
├── Product Service (8083)
├── Cart Service (8084)
├── WishList Service (8085)
├── Customer Details Service (8086)
├── Order Service (8087)
├── Feedback Service (8088)
└── Notification Service (8089)
Infrastructure:
Eureka Server (8761) — Service Discovery
Config Server (8888) — Centralized Configuration
```

---

## 🛠️ Tech Stack

| Component | Technology |
|---|---|
| Language / Framework | Java 25 / Spring Boot 4.x |
| Service Discovery | Netflix Eureka (Spring Cloud) |
| API Gateway | Spring Cloud Gateway |
| Config Management | Spring Cloud Config Server |
| Auth / Security | Spring Security + JWT (JJWT 0.12.3) |
| Messaging | Apache Kafka |
| Databases | PostgreSQL (per service) + Redis (Cart) |
| ORM | Hibernate 7 / Spring Data JPA |
| Documentation | SpringDoc OpenAPI 3 / Swagger UI |
| Inter-service Calls | OpenFeign |
| Containerization | Docker + Docker Compose |

---

## 📦 Microservices

| # | Service | Port | Database | Responsibility |
|---|---|---|---|---|
| 1 | User Service | 8081 | user_db (PG:5432) | Registration, login, JWT issuance, profile |
| 2 | Admin Service | 8082 | admin_db (PG:5433) | Admin management, role assignment |
| 3 | Product Service | 8083 | product_db (PG:5434) | CRUD for books, categories, inventory |
| 4 | Cart Service | 8084 | Redis | Add/remove items, cart total |
| 5 | WishList Service | 8085 | wishlist_db (PG:5435) | Manage user wish lists |
| 6 | Customer Details Service | 8086 | customer_db (PG:5436) | Addresses, phone, preferences |
| 7 | Order Service | 8087 | order_db (PG:5437) | Place orders, track status, history |
| 8 | Feedback Service | 8088 | feedback_db (PG:5438) | Product reviews and ratings |
| 9 | Notification Service | 8089 | — | Email/SMS via Kafka events |
| 10 | Eureka Server | 8761 | — | Service discovery |
| 11 | Config Server | 8888 | — | Centralized config |
| 12 | API Gateway | 8080 | — | Routing, JWT validation |

---

## 🔐 Security

- **JWT-based stateless authentication** — tokens issued by User Service
- **Role-Based Access Control (RBAC)** — `GUEST`, `USER`, `ADMIN`, `SUPER_ADMIN`
- **API Gateway** validates JWT before forwarding requests to downstream services
- All passwords hashed with **BCrypt**

### Auth Flow

POST /api/users/login → returns JWT token
Client sends: Authorization: Bearer <token>
API Gateway validates token
JWT claims (userId, role) forwarded to services as headers


---

## 📡 Inter-Service Communication

### Synchronous (OpenFeign)
Used when a real-time response is needed — e.g., Cart → Product Service to validate stock.

### Asynchronous (Apache Kafka)

| Topic | Producer | Consumer | Trigger |
|---|---|---|---|
| order-events | Order Service | Notification Service | Order placed / status changed |
| user-events | User Service | Notification Service | User registered |
| inventory-events | Order Service | Product Service | Reduce stock on order confirmed |
| review-events | Feedback Service | Product Service | Recalculate product rating |

---

## 🗄️ Database Design

Each microservice owns its own **PostgreSQL schema** — no shared tables. Cross-service data is composed at the application layer via API calls.

| Service | DB | Key Tables |
|---|---|---|
| User | user_db | users, roles |
| Product | product_db | products, categories |
| Cart | Redis | cart:{userId} (hash with TTL) |
| WishList | wishlist_db | wishlist_items |
| Customer | customer_db | customer_profiles, addresses |
| Order | order_db | orders, order_items |
| Feedback | feedback_db | reviews, ratings |
| Admin | admin_db | admins, audit_logs |

---

## 🚀 Getting Started

### Prerequisites
- Java 25
- Docker Desktop
- IntelliJ IDEA

### 1. Clone the repository
```bash
git clone https://github.com/your-username/bookstore-microservices.git
cd bookstore-microservices
```

### 2. Start infrastructure (PostgreSQL, Redis, Kafka)
```bash
docker-compose up -d
```

### 3. Run services in this order

Eureka Server      → http://localhost:8761
Config Server      → http://localhost:8888
User Service       → http://localhost:8081
Product Service    → http://localhost:8083
Cart Service       → http://localhost:8084
Order Service      → http://localhost:8087
Feedback Service   → http://localhost:8088
WishList Service   → http://localhost:8085
Customer Service   → http://localhost:8086
Admin Service     → http://localhost:8082
Notification Service → http://localhost:8089
API Gateway       → http://localhost:8080


> ⚠️ Add VM option `-Duser.timezone=Asia/Kolkata` to each IntelliJ Run Configuration

---

## 📖 API Documentation

Each service exposes Swagger UI at:
http://localhost:{PORT}/swagger-ui/index.html

| Service | Swagger URL |
|---|---|
| User Service | http://localhost:8081/swagger-ui/index.html |
| Product Service | http://localhost:8083/swagger-ui/index.html |
| Cart Service | http://localhost:8084/swagger-ui/index.html |
| Order Service | http://localhost:8087/swagger-ui/index.html |
| Feedback Service | http://localhost:8088/swagger-ui/index.html |
| WishList Service | http://localhost:8085/swagger-ui/index.html |
| Customer Service | http://localhost:8086/swagger-ui/index.html |

---

## 🔑 Key API Endpoints

### User Service
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | /api/users/register | No | Register new user |
| POST | /api/users/login | No | Login and get JWT |
| GET | /api/users/profile | JWT | Get user profile |

### Product Service
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | /api/products | No | List all products (paginated) |
| GET | /api/products/{id} | No | Get product by ID |
| GET | /api/products/search?q= | No | Search by title/author |
| POST | /api/products | ADMIN | Create product |
| PUT | /api/products/{id} | ADMIN | Update product |
| DELETE | /api/products/{id} | ADMIN | Delete product |

### Order Service
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | /api/orders | JWT | Place a new order |
| GET | /api/orders | JWT | Get user's orders |
| PUT | /api/orders/{id}/cancel | JWT | Cancel an order |
| GET | /api/orders/all | ADMIN | List all orders |

### Order Status Flow
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED
↘ CANCELLED

---

## 🏛️ Project Structure
```
bookstore-microservices/
├── eureka-server/
├── config-server/
├── api-gateway/
├── user-service/
│   └── src/main/java/com/bookstore/user/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       ├── dto/
│       ├── security/
│       └── exception/
├── product-service/
├── cart-service/
├── wishlist-service/
├── customer-details-service/
├── order-service/
├── feedback-service/
├── notification-service/
└── docker-compose.yml
```

---

## ✅ Key Design Principles

| Principle | Implementation |
|---|---|
| Single Responsibility | Each microservice owns exactly one bounded context |
| Database Isolation | Separate PostgreSQL schema per service — no shared tables |
| Stateless Auth | JWT tokens — no server-side session state |
| Async Decoupling | Kafka events for notifications — services never block each other |
| Externalized Config | Spring Cloud Config Server |
| API Documentation | SpringDoc auto-generates Swagger UI from annotations |
| Container First | Every service runs via Docker Compose |

---

## 👨‍💻 Author

Built as a full microservices learning project covering Spring Boot, Spring Cloud, JWT security, Kafka messaging, Docker, and PostgreSQL.
