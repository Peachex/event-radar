# Event Radar

**Event Radar** is an event aggregator web application designed to parse and display up-to-date information about various entertainment activities. The platform automatically collects events data from different websites and presents it to users, ensuring they are always informed about upcoming events.

One of the core features of this system is its flexibility and ease in adding support for new event websites. The process of integrating a new event source is simple and involves the following steps:
1. **Create a parser implementation** to extract event data from the new site.
2. **Define an event DTO (Data Transfer Object)** to standardize the transfer of event data across modules and to the user.
3. **Create an event entity class** for interaction with the database.
4. **Create an event model** to display new events in the UI.

## Project Modules

The `event-radar` project is composed of several modules, each responsible for specific tasks:

### 1. EventParser
- **Description**: A library used for parsing event data from various websites.
- **Details**: This module contains separate parser classes for each supported site. It also includes common utility methods to parse event details such as dates, prices, and more.

### 2. EventRadarCommon
- **Description**: A shared library that contains common logic used across different modules in the project.

### 3. EventPersistor
- **Description**: A web application responsible for performing CRUD operations on events in the database.
- **Details**: This module is not intended for direct customer interaction but is used by other modules to interact with the events database.

### 4. SyncTaskScheduler
- **Description**: A web application used to schedule and manage tasks.
- **Details**: Tasks are scheduled using the Quartz framework. When a scheduled task is triggered, it sends a message to the `event-manager` module, which performs specific tasks (e.g. keeping the events database up to date).

### 5. EventManager
- **Description**: A web application that processes tasks triggered by the `sync-task-scheduler`.
- **Details**: `event-manager` contains various tasks for maintaining and updating/synchronizing the events database based on messages received via a message broker.

### 6. EventWebApp
- **Description**: A web application used by customers to retrieve event information.
- **Details**: It communicates with the `event-persistor` module via a REST endpoint, using a Feign client to fetch data and display it to the user.

### 7. SyncTaskSchedulerUI
- **Description**: User interface component of the `sync-task-scheduler` module.

### 8. EventWebAppUI
- **Description**: User interface component of the `event-web-app` module.

## Getting Started

### Prerequisites

- Docker & Docker Compose (**necessary** for the **automatic startup** of the application via the `start_event-radar.sh` script).
- Java 17 (**necessary** for the **manual startup** of the application).
- Gradle (**optional**). All modules include a Gradle Wrapper, which can be used to perform tasks.
- Node.js v20.13.1 (**necessary** for the **manual startup** of the UI part of the application).
- Angular CLI 19.0.7 (**necessary** for the **manual startup** of the UI part of the application).


To start the `event-radar` application, use the provided `start_event_radar.sh` script in the `root` folder. This script will automatically set up all required backend and frontend services, including databases, message brokers, and other necessary containers.

### Steps to Run
To start the `event-radar` application, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Peachex/event-radar.git
2. **Run the `start_event_radar.sh` script**.
   This will start all required backend/frontend services and containers, including the event parsers, task scheduler, database, and more:
   ```bash
   cd event-radar/
   ./start_event_radar.sh
3. **Access Swagger Documentation**.
   Each web module in the system provides Swagger documentation to explore and test available REST endpoints. You can access it by navigating to the following URL:
     ```bash
     http://localhost:PORT/CONTEXT_PATH/swagger-ui/index.html
  - Replace `PORT` with the appropriate port number for the module.
  - Replace `CONTEXT_PATH` with the context path for the module.
4. **Access UI applications**.
    Access the UI applications by navigating to the following URLs in your browser:
   ```bash
   http://localhost:4210  # sync-task-scheduler  
   http://localhost:4211  # event-web-app
  
  ### Local development
  **Before launching the `event-persistor` module locally, verify the `Add the *.docker.internal names to the host's /etc/hosts file (Requires password)` checkbox status in Docker settings.** If it is unchecked, check it. If it is already checked, uncheck it and then check it again to ensure Docker is properly set up. This is necessary to enable the local `event-persistor` module (outside the Docker container) to connect to the MondoDB database inside the Docker container.
   
   ![image](https://github.com/user-attachments/assets/c22bcf1c-8a3c-4e63-b2b7-2723644cb8ab)

