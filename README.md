# 🏠 Security_Project_java 

![Java](https://img.shields.io/badge/Java-17+-blue) ![JavaFX](https://img.shields.io/badge/JavaFX-21.0.9-orange) ![License](https://img.shields.io/badge/License-MIT-green)

A **Home Security System GUI** built using **JavaFX**, simulating a real-time home security system with camera monitoring, sensor management, and system control functionality.

---

## 🔹 Overview

This project provides a GUI-based simulation of a home security system:

- Displays camera images and dynamically updates **system status**.
- Detects cats in the camera feed (simulated) and triggers alerts.
- Provides user interface to manage sensors and control system arming status.
- Fully interactive GUI using JavaFX.

---

## ⚡ Features

- **Camera Simulation**
  - Switch between `cat_placeholder.png` and `no_cat_placeholder.png`.
  - System status updates automatically:
    - **No cat:** "System Status: Cool and Good"
    - **Cat detected:** "System Status: DANGER - CAT DETECTED"
  - **Scan Picture** button shows corresponding alerts.

- **System Control**
  - Disarm, Armed-Home, Armed-Away options.
  - Changes system state dynamically.

- **Sensor Management**
  - Add new sensors (Door, Window, Motion).
  - Activate/Deactivate sensors.
  - Remove sensors from the system.

---

## 📁 Project Structure

SecurityService/
│
├─ src/
│ ├─ main/
│ │ ├─ java/
│ │ │ └─ com/udasecurity/gui/SecurityGUI.java
│ │ │ └─ com/udasecurity/security/SecurityService.java
│ │ │ └─ com/udasecurity/security/ArmingStatus.java
│ │ │ └─ com/udasecurity/imageservice/ImageService.java
│ │ └─ resources/
│ │ └─ images/
│ │ ├─ no_cat_placeholder.png
│ │ └─ cat_placeholder.png
│
└─ target/
---
## 📸 Screenshots




<img width="1237" height="765" alt="Screenshot 2025-10-24 000756" src="https://github.com/user-attachments/assets/6ccf0c87-cf81-4be2-ba97-f00bf9433947" />



<img width="1238" height="766" alt="Screenshot 2025-10-24 000823" src="https://github.com/user-attachments/assets/ccdec8cd-ef04-4a4f-955d-7638e145a297" />


<img width="1734" height="858" alt="Screenshot 2025-10-24 123925" src="https://github.com/user-attachments/assets/cdb4b079-4c41-45d9-92b2-862e9960946e" />



---

## 🛠 Prerequisites

- **Java 17 or higher**
- **JavaFX 21** ([Download here](https://openjfx.io))
- IDE: IntelliJ, Eclipse, VSCode (recommended)

---

## 🚀 How to Run

1. Clone the repository:
```bash
git clone <repository-url>
cd SecurityService
Compile the project:

bash
Copy code
javac --module-path "C:\path\to\javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d target\classes src\main\java\com\udasecurity\gui\SecurityGUI.java src\main\java\com\udasecurity\security\*.java src\main\java\com\udasecurity\imageservice\*.java
Run the application:

bash
Copy code
java --module-path "C:\path\to\javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp target\classes com.udasecurity.gui.SecurityGUI
🎮 Usage
Refresh Camera: Switches between images and updates system status.

Scan Picture: Shows alert matching current image (Everything is fine! / Cat detected!).

System Control: Toggle Disarm / Armed-Home / Armed-Away.

Add Sensor: Input name and type, click "Add New Sensor".

Toggle Sensor: Activate / Deactivate sensors.

Remove Sensor: Delete sensors from the list.




