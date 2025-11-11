# HealthRx Spring Boot Webhook Assignment (JAVA)

### 👤 Developer
**Name:** Ansh T Shetty  
**Reg No:** PES2UG22CS080  
**Email:** pes2ug22cs080@pesu.pes.edu  

---

## 🧠 Overview
This Spring Boot application was built as part of the **HealthRx Hiring Assignment (JAVA)**.

The app:
1. Automatically runs on startup (no controller endpoints required).
2. Sends a `POST` request to generate a webhook using:
3. Receives:
- A unique webhook URL
- A JWT `accessToken`
4. Determines the SQL question based on the last two digits of the `regNo`:
- **Odd** → Question 1  
- **Even** → Question 2
5. Sends the final SQL query back to the given webhook URL using the received JWT token.

---

## 🧩 Technology Stack
- **Spring Boot** (v3)
- **Java 17+**
- **Maven**
- **RestTemplate** for HTTP requests

---

## 🚀 Running the Application

### Option 1: Using Maven Wrapper
```bash
.\mvnw.cmd spring-boot:run
