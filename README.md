### 📖 Introduction

**Deepseek WebChat** is a lightweight web chat system built with **Spring Boot (backend)** and **HTML/JavaScript (frontend)**.
It uses **Deepseek API** to support **real-time streaming responses** with markdown rendering.
A backend proxy is included to handle CORS and API security.

---

### ⚙️ Tech Stack

* **Backend:** Spring Boot + WebFlux + Maven
* **Frontend:** HTML + JS + Markdown + Highlight.js
* **API:** Deepseek Chat API (streaming)

---

### 🚀 How to Run

#### 1️⃣ Clone the repository

```bash
git clone https://github.com/MTQ851/deepseek-webchat.git
cd deepseek-webchat
```

#### 2️⃣ Configure your API Key

Edit:

```java
private static final String DEEP_SEEK_API_KEY = "Bearer sk-xxxxxxxxxxxxxxxx";
```

in
`src/main/java/com/deepseek/controller/ai/DeepSeekController.java`.

#### 3️⃣ Start the backend

Make sure Maven is installed (`mvn -v`), then run:

```bash
mvn clean package
mvn spring-boot:run
```

The backend starts on `http://localhost:8080`.

#### 4️⃣ Open the frontend

Visit:

```
http://localhost:8080/index.html
```

to start chatting.

---

### 📁 Project Structure

```
deepseek-webchat/
├── src/
│   ├── main/
│   │   ├── java/com/deepseek/controller/ai/DeepSeekController.java
│   │   ├── resources/static/index.html
│   │   └── resources/application.yml
├── pom.xml
└── README.md
```

---

### 📜 License

Released under the **MIT License** — free for learning, modification, and redistribution.

---
![界面预览](docs/preview.png)
