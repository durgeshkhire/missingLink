# 🚗 SaathiRide — Carpooling Web Application

A full-stack carpooling web application inspired by BlaBlaCar, built for India. SaathiRide allows users to post rides, search for available rides, book seats, chat with drivers/passengers, and track live ride locations — all on a single platform.

---

## 🌐 Live Demo

- **Frontend:** [https://saathiride.vercel.app/](https://saathiride.vercel.app/)
- **Backend API:** [http://13.232.83.99:8080](http://13.232.83.99:8080)

---

## 📌 Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [Deployment](#deployment)
- [Screenshots](#screenshots)

---

## 📖 About the Project

SaathiRide is a cost-sharing carpooling platform where:
- **Drivers** post rides with their vehicle details and available seats
- **Riders** search, find, and request to join rides
- **No commission** is charged — only actual fuel cost is split between passengers
- Users can rate and review each other after a completed ride
- Drivers can track and share live GPS location during a ride

---

## ✨ Features

### Authentication
- Register with name, email and password
- Email OTP verification before registration
- JWT-based stateless authentication
- Secure login with email and password

### Rides
- Post a ride with origin, destination, date, time, seats and price
- Search rides by city and date
- View all upcoming rides on landing page (paginated)
- Auto-cancel expired rides via scheduled job
- Mark ride as started, completed or cancelled

### Bookings
- Request to join a ride
- Driver can approve or reject booking requests
- Instant booking option (auto-approve)
- Cancel bookings with automatic seat restoration

### Vehicles
- Add multiple vehicles to your profile
- Choose vehicle when creating a ride
- Seat availability validated against vehicle capacity

### Live Location Tracking
- Driver shares real-time GPS location via WebSocket (STOMP)
- Passengers see live driver marker on map during active ride
- Location updates every 3 seconds

### Reviews
- Rate and review after ride completion
- Average rating auto-calculated and shown on profile
- View all reviews on public profile page

### Profile
- View and edit your public profile
- Change password
- View ride history and booking history

---

## 🛠 Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 3 | Backend framework |
| Spring Security 6 | Authentication and authorization |
| JWT (jjwt 0.12.6) | Stateless token-based auth |
| Spring Data JPA | ORM and database access |
| Hibernate | JPA implementation |
| PostgreSQL (Neon) | Cloud database |
| Spring WebSocket (STOMP) | Real-time live location tracking |
| JavaMailSender | Email OTP delivery |
| Jsoup + JSON | SMS OTP via third-party API |
| Spring Scheduler | Auto-cancel expired rides |
| Lombok | Reduce boilerplate code |
| Maven | Build tool |

### Frontend
| Technology | Purpose |
|---|---|
| React 18 | UI framework |
| TypeScript | Type-safe JavaScript |
| Vite 5 | Build tool and dev server |
| React Router v6 | Client-side routing |
| Zustand | Global state management |
| Axios | HTTP client with interceptors |
| React Hook Form + Zod | Form handling and validation |
| Tailwind CSS | Utility-first styling |
| Leaflet / React-Leaflet | Interactive maps |
| SockJS + STOMP | WebSocket client for live tracking |
| date-fns | Date formatting |
| Lucide React | Icon library |

### Infrastructure
| Service | Purpose |
|---|---|
| AWS EC2 (Amazon Linux) | Backend server hosting |
| Neon PostgreSQL | Managed cloud database |
| Vercel | Frontend hosting and deployment |
| GitHub | Version control |

---

## 📁 Project Structure

```
saathiride/
│
├── backend/                          # Spring Boot application
│   └── src/main/java/com/missinglink/backend/
│       ├── BackendApplication.java
│       ├── config/
│       │   ├── SecurityConfig.java
│       │   └── WebSocketConfig.java
│       ├── common/
│       │   ├── response/ApiResponse.java
│       │   ├── util/JwtUtil.java
│       │   ├── util/OtpStore.java
│       │   ├── util/EmailService.java
│       │   └── util/SmsServiceImpl.java
│       ├── entity/
│       │   ├── User.java
│       │   ├── Ride.java
│       │   ├── Booking.java
│       │   ├── Vehicle.java
│       │   ├── Review.java
│       │   ├── Message.java
│       │   └── enums/
│       └── module/
│           ├── auth/
│           ├── user/
│           ├── ride/
│           ├── booking/
│           ├── vehicle/
│           ├── review/
│           └── location/
│
└── frontend/                         # React application
    └── src/
        ├── api/
        ├── components/
        │   ├── common/
        │   ├── layout/
        │   ├── ride/
        │   ├── booking/
        │   └── map/
        ├── pages/
        │   ├── auth/
        │   ├── rides/
        │   ├── bookings/
        │   ├── vehicles/
        │   └── profile/
        ├── store/
        ├── types/
        └── utils/
```

---

## 🔗 API Endpoints

### Authentication (Public)
```
POST /auth/send-otp          → Send OTP to email
POST /auth/verify-otp        → Verify OTP
POST /auth/register          → Register new user
POST /auth/login             → Login with email + password
```

### Rides
```
GET  /api/rides              → Get all upcoming rides (public, paginated)
GET  /api/rides/search       → Search rides by city and date (public)
GET  /api/rides/{id}         → Get ride by ID (public)
GET  /api/rides/my-rides     → Get my posted rides 🔒
POST /api/rides              → Create a ride 🔒
PATCH /api/rides/{id}/start    → Start ride 🔒
PATCH /api/rides/{id}/complete → Complete ride 🔒
PATCH /api/rides/{id}/cancel   → Cancel ride 🔒
```

### Bookings
```
POST  /api/bookings                  → Request to join a ride 🔒
GET   /api/bookings/my-bookings      → My bookings 🔒
GET   /api/bookings/ride/{rideId}    → Bookings for a ride 🔒
PATCH /api/bookings/{id}/approve     → Approve booking 🔒
PATCH /api/bookings/{id}/reject      → Reject booking 🔒
PATCH /api/bookings/{id}/cancel      → Cancel booking 🔒
```

### Vehicles
```
POST   /api/vehicles              → Add vehicle 🔒
GET    /api/vehicles/my-vehicles  → Get my vehicles 🔒
PUT    /api/vehicles/{id}         → Update vehicle 🔒
DELETE /api/vehicles/{id}         → Delete vehicle 🔒
```

### Reviews
```
POST /api/reviews              → Submit review 🔒
GET  /api/reviews/user/{id}    → Reviews for a user (public)
GET  /api/reviews/by/{id}      → Reviews written by a user 🔒
```

### Users
```
GET    /api/users/me                    → My profile 🔒
GET    /api/users/{id}                  → Public profile (public)
PUT    /api/users/me                    → Update profile 🔒
PATCH  /api/users/me/change-password    → Change password 🔒
DELETE /api/users/me                    → Delete account 🔒
```

### WebSocket
```
STOMP /ws                                    → WebSocket connection
SEND  /app/location/{rideId}                 → Driver sends GPS location
SUB   /topic/ride/{rideId}/location          → Passengers receive location
```

---

## 🚀 Getting Started

### Prerequisites
- Java 21
- Node.js 18+
- PostgreSQL or Neon account
- Maven

### Backend Setup

```bash
# Clone the repo
git clone https://github.com/yourusername/saathiride.git
cd saathiride/backend

# Configure application-dev.yml with your database credentials

# Run the application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Frontend Setup

```bash
cd saathiride/frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

---

## 🔐 Environment Variables

### Backend (`application-prod.yml`)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://<DB_HOST>:5432/<DB_NAME>?sslmode=require
    username: <DB_USERNAME>
    password: <DB_PASSWORD>
  mail:
    username: <MAIL_USERNAME>
    password: <MAIL_APP_PASSWORD>
app:
  jwt:
    secret: <JWT_SECRET_MIN_32_CHARS>
```

### Frontend (`.env.production`)
```env
VITE_API_BASE_URL=http://your-ec2-ip:8080
```

---

## ☁️ Deployment

### Backend — AWS EC2 (Amazon Linux)
```bash
# Build JAR
./mvnw clean package -DskipTests

# Upload to EC2
scp -i key.pem target/backend-0.0.1-SNAPSHOT.jar ec2-user@<EC2_IP>:/home/ec2-user/

# Run as systemd service
sudo systemctl start missinglink
```

### Frontend — Vercel
```bash
# Push to GitHub — Vercel auto deploys
git push origin main
```

### `vercel.json` (proxy to EC2)
```json
{
  "rewrites": [
    {
      "source": "/:path*",
      "destination": "http://<EC2_IP>:8080/:path*"
    }
  ]
}
```

---

## 📊 Database Schema

| Table | Description |
|---|---|
| `users` | User accounts with email, password, rating |
| `rides` | Posted rides with route, seats, price |
| `bookings` | Ride booking requests and status |
| `vehicles` | User vehicles linked to rides |
| `reviews` | Post-ride ratings and comments |
| `messages` | In-ride chat messages |

---

## 👨‍💻 Author

**Durgesh Khire**
- GitHub: [@durgeshkhire](https://github.com/durgeshkhire)
- LinkedIn: [Durgesh Khire](https://www.linkedin.com/in/durgesh-khire2498/)

---

## 📄 License

This project is licensed under the MIT License.

---

> Built with ❤️ for India's carpooling community
