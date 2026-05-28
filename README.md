# 💰 Expense Tracker

A simple and easy-to-use **Expense Tracker** application built with **Spring Boot**. Track your daily spending, organize expenses by category, and get monthly summaries!

---

## 🛠️ Tech Stack (What Technologies Are Used?)

This project uses the following tools and technologies:

- **Java 17** → Programming language
- **Spring Boot 4.0.6** → Web framework for building the API
- **Spring Data JPA** → To work with database (PostgreSQL)
- **PostgreSQL** → Database to store data
- **Lombok** → Helps reduce repetitive code
- **Maven** → Tool to build the project
- **Spring Security** → For security features

---

## ✨ What Can You Do With This App?

✅ Create new users
✅ Add daily expenses
✅ Organize expenses by category (Food, Transport, Shopping, etc.)
✅ View all your expenses
✅ See your last 10 expenses
✅ Get monthly spending summary
✅ Filter expenses by category
✅ Update or delete expenses
✅ Track remaining money

---

## 🚀 How to Run This Project?

### Step 1: Install Required Software
You need to have these installed on your computer:
- Java 17 (or higher)
- PostgreSQL Database
- Maven (for building)

### Step 2: Get the Project
```bash
git clone https://github.com/Bharat8327/Expense_Tracker.git
cd Expense_Tracker
```

### Step 3: Set Up Database
- Open PostgreSQL
- Create a new database for this project
- Update database details in `application.properties` file

### Step 4: Build & Run
```bash
mvn clean build
mvn spring-boot:run
```

The application will start on: **http://localhost:8080**

---

## 📚 How to Use the APIs? (Complete Guide)

### **PART 1: USER APIS** 
(Manage User Accounts)

Base URL: `http://localhost:8080/users`

---

### 1️⃣ Check If API is Working

**What it does:** Checks if the server is running

```
GET http://localhost:8080/users/health
```

**In Postman:**
- Select: **GET**
- URL: `http://localhost:8080/users/health`
- Click: **Send**

**Response:**
```
Success
```

---

### 2️⃣ Get All Users

**What it does:** Shows list of all users in the system

```
GET http://localhost:8080/users/all
```

**In Postman:**
- Select: **GET**
- URL: `http://localhost:8080/users/all`
- Click: **Send**

**Response (Example):**
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "totalMoney": 5000,
    "remainingMoney": 3500
  },
  {
    "id": 2,
    "username": "jane_smith",
    "totalMoney": 10000,
    "remainingMoney": 8500
  }
]
```

---

### 3️⃣ Create a New User

**What it does:** Register a new user account

```
POST http://localhost:8080/users
```

**In Postman:**
- Select: **POST**
- URL: `http://localhost:8080/users`
- Go to **Body** → Select **raw** → Select **JSON**
- Copy & Paste:

```json
{
  "username": "john_doe",
  "password": "mypassword123",
  "totalMoney": 5000,
  "remainingMoney": 5000
}
```

- Click: **Send**

**Response (Example):**
```json
{
  "id": 1,
  "username": "john_doe",
  "totalMoney": 5000,
  "remainingMoney": 5000
}
```

---

### 4️⃣ Get User by ID

**What it does:** Get details of a specific user

```
GET http://localhost:8080/users/1
```

(Replace `1` with actual user ID)

**In Postman:**
- Select: **GET**
- URL: `http://localhost:8080/users/1`
- Click: **Send**

**Response (Example):**
```json
{
  "id": 1,
  "username": "john_doe",
  "totalMoney": 5000,
  "remainingMoney": 3500
}
```

---

### 5️⃣ Get User by Username

**What it does:** Find a user using their username

```
GET http://localhost:8080/users/username/john_doe
```

(Replace `john_doe` with actual username)

**In Postman:**
- Select: **GET**
- URL: `http://localhost:8080/users/username/john_doe`
- Click: **Send**

**Response (Example):**
```json
{
  "id": 1,
  "username": "john_doe",
  "totalMoney": 5000,
  "remainingMoney": 3500
}
```

---

### 6️⃣ Update User Information

**What it does:** Edit user details

```
PUT http://localhost:8080/users/1
```

(Replace `1` with actual user ID)

**In Postman:**
- Select: **PUT**
- URL: `http://localhost:8080/users/1`
- Go to **Body** → Select **raw** → Select **JSON**
- Copy & Paste:

```json
{
  "username": "john_updated",
  "password": "newpassword456",
  "totalMoney": 6000,
  "remainingMoney": 4000
}
```

- Click: **Send**

**Response (Example):**
```json
{
  "id": 1,
  "username": "john_updated",
  "totalMoney": 6000,
  "remainingMoney": 4000
}
```

---

### 7️⃣ Delete User

**What it does:** Remove a user account

```
DELETE http://localhost:8080/users/1
```

(Replace `1` with actual user ID)

**In Postman:**
- Select: **DELETE**
- URL: `http://localhost:8080/users/1`
- Click: **Send**

**Response (Example):**
```json
"User deleted successfully"
```

---

## 📚 How to Use the APIs? (Part 2)

### **PART 2: EXPENSE APIS**
(Manage User Expenses)

Base URL: `http://localhost:8080/users/{userId}/expenses`

---

### 1️⃣ Create a New Expense

**What it does:** Add a new expense for a user

```
POST http://localhost:8080/users/1/expenses
```

(Replace `1` with actual user ID)

**In Postman:**
- Select: **POST**
- URL: `http://localhost:8080/users/1/expenses`
- Go to **Body** → Select **raw** → Select **JSON**
- Copy & Paste:

```json
{
  "title": "Lunch",
  "message": "Lunch at pizza restaurant",
  "amount": 350,
  "category": "FOOD",
  "date": "2025-05-28"
}
```

- Click: **Send**

**Available Categories:** FOOD, TRANSPORT, SHOPPING, HEALTH, ENTERTAINMENT, BILLS, EDUCATION, TRAVEL, OTHER

**Response (Example):**
```json
{
  "id": 1,
  "title": "Lunch",
  "message": "Lunch at pizza restaurant",
  "amount": 350,
  "category": "FOOD",
  "date": "2025-05-28"
}
```

---

### 2️⃣ Get All Expenses of a User

**What it does:** Show all expenses for a user

```
GET http://localhost:8080/users/1/expenses/all
```

(Replace `1` with actual user ID)

**In Postman:**
- Select: **GET**
- URL: `http://localhost:8080/users/1/expenses/all`
- Click: **Send**

**Response (Example):**
```json
[
  {
    "id": 1,
    "title": "Lunch",
    "message": "Lunch at pizza restaurant",
    "amount": 350,
    "category": "FOOD",
    "date": "2025-05-28"
  },
  {
    "id": 2,
    "title": "Bus Ticket",
    "message": "Travel to office",
    "amount": 100,
    "category": "TRANSPORT",
    "date": "2025-05-28"
  },
  {
    "id": 3,
    "title": "Book",
    "message": "Bought a new book",
    "amount": 250,
    "category": "EDUCATION",
    "date": "2025-05-27"
  }
]
```

---

### 3️⃣ Get a Specific Expense

**What it does:** View details of one expense

```
GET http://localhost:8080/users/1/expenses/1
```

(Replace first `1` with user ID, second `1` with expense ID)

**In Postman:**
- Select: **GET**
- URL: `http://localhost:8080/users/1/expenses/1`
- Click: **Send**

**Response (Example):**
```json
{
  "id": 1,
  "title": "Lunch",
  "message": "Lunch at pizza restaurant",
  "amount": 350,
  "category": "FOOD",
  "date": "2025-05-28"
}
```

---

### 4️⃣ Get Latest 10 Expenses

**What it does:** View your 10 most recent expenses

```
GET http://localhost:8080/users/1/expenses
```

(Replace `1` with actual user ID)

**In Postman:**
- Select: **GET**
- URL: `http://localhost:8080/users/1/expenses`
- Click: **Send**

**Response (Example):**
```json
[
  {
    "id": 10,
    "title": "Movie",
    "message": "Movie ticket",
    "amount": 250,
    "category": "ENTERTAINMENT",
    "date": "2025-05-28"
  },
  {
    "id": 9,
    "title": "Groceries",
    "message": "Weekly shopping",
    "amount": 1200,
    "category": "SHOPPING",
    "date": "2025-05-27"
  }
]
```

---

### 5️⃣ Get Last Month Summary

**What it does:** See total spending and remaining money for last month

```
GET http://localhost:8080/users/1/expenses/month
```

(Replace `1` with actual user ID)

**In Postman:**
- Select: **GET**
- URL: `http://localhost:8080/users/1/expenses/month`
- Click: **Send**

**Response (Example):**
```json
{
  "totalExpenseLastMonth": 5000,
  "remainingMoney": 3500,
  "totalMoney": 8500
}
```

---

### 6️⃣ Get Expenses by Category

**What it does:** See all expenses in one category

```
GET http://localhost:8080/users/1/expenses/c/FOOD
```

(Replace `1` with user ID, replace `FOOD` with category name)

**In Postman:**
- Select: **GET**
- URL: `http://localhost:8080/users/1/expenses/c/FOOD`
- Click: **Send**

**Response (Example):**
```json
[
  {
    "id": 1,
    "title": "Lunch",
    "message": "Lunch at pizza restaurant",
    "amount": 350,
    "category": "FOOD",
    "date": "2025-05-28"
  },
  {
    "id": 4,
    "title": "Dinner",
    "message": "Dinner at restaurant",
    "amount": 500,
    "category": "FOOD",
    "date": "2025-05-27"
  }
]
```

---

### 7️⃣ Update an Expense

**What it does:** Edit an existing expense

```
PUT http://localhost:8080/users/1/expenses/1
```

(Replace first `1` with user ID, second `1` with expense ID)

**In Postman:**
- Select: **PUT**
- URL: `http://localhost:8080/users/1/expenses/1`
- Go to **Body** → Select **raw** → Select **JSON**
- Copy & Paste:

```json
{
  "title": "Lunch Updated",
  "message": "Lunch at new restaurant",
  "amount": 400,
  "category": "FOOD",
  "date": "2025-05-28"
}
```

- Click: **Send**

**Response (Example):**
```json
{
  "id": 1,
  "title": "Lunch Updated",
  "message": "Lunch at new restaurant",
  "amount": 400,
  "category": "FOOD",
  "date": "2025-05-28"
}
```

---

### 8️⃣ Delete an Expense

**What it does:** Remove an expense

```
DELETE http://localhost:8080/users/1/expenses/1
```

(Replace first `1` with user ID, second `1` with expense ID)

**In Postman:**
- Select: **DELETE**
- URL: `http://localhost:8080/users/1/expenses/1`
- Click: **Send**

**Response (Example):**
```json
"Expense deleted successfully"
```

---

## 📋 Quick Reference (All APIs)

| # | Method | URL | What it does |
|---|--------|-----|------------|
| 1 | GET | `/users/health` | Check if API is working |
| 2 | GET | `/users/all` | Get all users |
| 3 | POST | `/users` | Create new user |
| 4 | GET | `/users/1` | Get user by ID |
| 5 | GET | `/users/username/john` | Get user by username |
| 6 | PUT | `/users/1` | Update user |
| 7 | DELETE | `/users/1` | Delete user |
| 8 | POST | `/users/1/expenses` | Create expense |
| 9 | GET | `/users/1/expenses/all` | Get all expenses |
| 10 | GET | `/users/1/expenses` | Get latest 10 expenses |
| 11 | GET | `/users/1/expenses/1` | Get specific expense |
| 12 | GET | `/users/1/expenses/month` | Get monthly summary |
| 13 | GET | `/users/1/expenses/c/FOOD` | Get expenses by category |
| 14 | PUT | `/users/1/expenses/1` | Update expense |
| 15 | DELETE | `/users/1/expenses/1` | Delete expense |

---

## 🎯 Step-by-Step Example

Let's create and track an expense:

### Step 1: Create a User
```
POST http://localhost:8080/users
Body:
{
  "username": "ram",
  "password": "pass123",
  "totalMoney": 10000,
  "remainingMoney": 10000
}
```
You'll get: User ID = 1

### Step 2: Add an Expense
```
POST http://localhost:8080/users/1/expenses
Body:
{
  "title": "Movie",
  "message": "Movie ticket",
  "amount": 200,
  "category": "ENTERTAINMENT",
  "date": "2025-05-28"
}
```

### Step 3: View All Expenses
```
GET http://localhost:8080/users/1/expenses/all
```

### Step 4: Get Food Expenses Only
```
GET http://localhost:8080/users/1/expenses/c/FOOD
```

### Step 5: Get Monthly Summary
```
GET http://localhost:8080/users/1/expenses/month
```

---

## ❌ Error Messages (What They Mean)

| Error Code | Meaning |
|-----------|---------|
| 400 | Bad Request - Something wrong with your data |
| 404 | Not Found - User or expense doesn't exist |
| 500 | Server Error - Something went wrong on server |

**Example Error:**
```json
{
  "timestamp": "2025-05-28T10:30:00",
  "status": 404,
  "message": "User not found",
  "path": "/users/999"
}
```

---

## 💡 Important Notes

- **Date Format:** Always use `YYYY-MM-DD` (example: 2025-05-28)
- **Amount:** Use numbers only (example: 350.50)
- **Category:** Must be UPPERCASE (example: FOOD, not food)
- **Money:** Remaining money decreases when you add expense, increases when you delete it

---

## 🔒 Categories You Can Use

- `FOOD` → Food & Restaurants
- `TRANSPORT` → Travel & Travel tickets
- `SHOPPING` → Buying items
- `HEALTH` → Medical & Health
- `ENTERTAINMENT` → Movies, Games, Fun
- `BILLS` → Electricity, Water, Internet
- `EDUCATION` → Books, Courses, Learning
- `TRAVEL` → Long trips & holidays
- `OTHER` → Anything else

---

## ✅ How to Test APIs in Postman (Complete Steps)

1. Download and open **Postman**
2. Click **+ New Tab** to create new request
3. Select **GET/POST/PUT/DELETE** from dropdown
4. Copy any API URL from above
5. Paste in URL field
6. If POST/PUT:
   - Click **Body**
   - Select **raw**
   - Select **JSON**
   - Paste the JSON example
7. Click **Send** button
8. See the response below

---

## 👨‍💼 Created By

**Bharat_Patel 🧑‍💻** 

---

## 📞 Need Help?

1. Check API URL is correct
2. Make sure JSON format is right
3. Verify user ID exists
4. Check category is uppercase
5. Use correct date format (YYYY-MM-DD)

---

## 🎉 Happy Expense Tracking!

Now you can easily track all your expenses! 💰
