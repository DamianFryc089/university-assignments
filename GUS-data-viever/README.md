# GUS Data Viewer

## Project Description

This project is an application that retrieves and visualizes statistical data from the GUS (Central Statistical Office of Poland) through its open API. The application is built with a modular architecture, split into two separate parts:

- **lab04_client**: This module handles the business logic, such as sending API requests, receiving responses, and parsing the data.
- **lab04_gui**: This module provides the graphical user interface (GUI) for displaying the data and interacting with the **lab04_client** module.

### Features:

- **API Data Fetching**: The application connects to the GUS public API to retrieve various statistical data (such as demographics and migration statistics) and parses the JSON responses.
- **Data Visualization**: The data is displayed through a graphical interface, built using JavaFX. The data is presented in tables or other relevant formats for easy understanding.
- **Data Caching**: To reduce the number of requests to the GUS API and avoid overloading it, the application caches the fetched data locally. If the data is already present in the cache, it will be loaded from there instead of making a new API request.
- **Modular Design**: The application follows a modular structure using Java's Platform Module System (JPMS). The logic and GUI are separated into different modules, ensuring maintainability and clarity.
- **Data Interaction**: The user can view, filter, interact with, and browse through different sets of statistical data fetched from the GUS API. The interface is designed to be user-friendly and easy to navigate.

### Technologies Used:
- **Java 9 HTTPClient**: Used for sending HTTP requests and receiving responses from the GUS API.
- **JavaFX**: Used to build the graphical user interface for data visualization.
- **Local Caching System**: A file-based cache is implemented to store previously fetched data to minimize API requests and improve performance.
- **JSON Parsing Library**: `org.json` was used for parsing the JSON responses from the API.
- **Java Platform Module System (JPMS)**: To create a modular application with separate modules for the client and GUI logic.
