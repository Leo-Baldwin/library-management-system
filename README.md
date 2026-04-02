# Library Management System

A Java console application demonstrating clean OOP architecture for managing media items, members, loans, fines, and reservations.

## Overview

The Library Management System (LMS) is designed for an Advanced OOP module and emphasizes maintainable design, clear separation of concerns, and extensibility.

Key capabilities include:
- Media item management (Books, DVDs, Magazines)
- Member registration and validation
- Loan processing (checkout/return) and fine calculation
- Reservation queueing with lifecycle states
- Strategy-based policies for loan durations and fines
- Custom exception handling for domain validation

## Features

- Media management with availability tracking (`AVAILABLE`, `ON_LOAN`, `RESERVED`)
- Member status and borrowing restrictions
- Loan history tracking with automatic due date and fine calculation
- FIFO reservation queue with auto-fulfillment
- Console-driven workflow for common tasks

## Architecture

The project follows a layered, OOP-focused structure:
- `domain/`: core entities, policies, and domain services
- `app/`: application entry point
- `presentation/`: console UI and interaction layer
- `infrastructure/`: storage or integration concerns (as needed)
- `common/`: shared utilities and exceptions

### Class Relationships (High-Level)

```mermaid
classDiagram
    class MediaItem
    class Book
    class Dvd
    class Magazine
    class Person
    class Member
    class Librarian
    class Loan
    class Reservation
    class LoanPolicy
    class FinePolicy

    MediaItem <|-- Book
    MediaItem <|-- Dvd
    MediaItem <|-- Magazine
    Person <|-- Member
    Person <|-- Librarian
    Loan --> MediaItem
    Loan --> Member
    Reservation --> MediaItem
    Reservation --> Member
    LoanPolicy <|.. StandardLoanPolicy
    LoanPolicy <|.. MediaTypeLoanPolicy
    FinePolicy <|.. StandardFinePolicy
    FinePolicy <|.. MediaTypeFinePolicy
    Library --> LoanPolicy
    Library --> FinePolicy
```

## Project Structure

```
src/
  main/
    java/
      app/             # App entry point (main) and web server
      common/          # shared utilities/exceptions
      domain/          # core model, policies, services
      infrastructure/  # persistence/integration
      presentation/    # console UI
    resources/
      data/            # CSV demo data
  test/
    java/              # unit tests
pom.xml                # Maven build configuration
```

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+

### Build and Run

```bash
mvn compile exec:java -Dexec.mainClass="app.App"
```

Or to run the web server:

```bash
mvn compile exec:java -Dexec.mainClass="app.WebServer"
```

### Run the Tests

```bash
mvn test
```

## Usage

When you start the app, follow the console menu to:
- List items and members
- Check out and return media
- Add new media
- Reserve items

### Example Session (Abbreviated)

```text
1) List items
2) List members
3) Checkout item
4) Return item
5) Reserve item
6) Add new media
Select option: 3
```

## Design Patterns & OOP Concepts

- Strategy pattern for `LoanPolicy` and `FinePolicy`
- Custom `ValidationException` for domain rule violations
- Inheritance for media types and people
- Encapsulation with controlled accessors

## Future Improvements

- GUI (JavaFX or Swing)
- Authentication for librarians
- Enhanced searching/reporting

## Author

Leo Baldwin
