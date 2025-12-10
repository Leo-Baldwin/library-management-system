📚 Library Management System

A Java Console Application demonstrating Clean OOP Architecture

⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻

📖 Overview

The Library Management System (LMS) is a fully object-oriented Java console application designed for an Advanced OOP module. The project focuses on clean architecture, maintainable code, and strong use of OOP principles.

The application supports:

•	Media item management (Books, DVDs, Magazines)

•	Member registration and validation

•	Loan processing (checkout and return)

•	Fine calculation

•	Reservation queueing

•	Console-based interaction

•	Strategy pattern for flexible business rules

•	Custom exception handling for domain validation

It is built for clarity, extensibility, and demonstration of good software engineering practices.

⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻

✨ Features

Media Management

•	Add, list, and manage media items

•	Supports Books, DVDs, and Magazines

•	Status tracking: AVAILABLE, ON_LOAN, RESERVED

Member Management

•	Register new library members

•	Track active/inactive status

•	Borrowing restrictions applied automatically

Loan System

•	Checkout items with automatic due date calculation

•	Return items with automatic fine calculation

•	Full loan history tracking

•	Strategy-based policies (LoanPolicy, FinePolicy)

Reservation Handling

•	FIFO reservation queue

•	Auto-fulfillment when items are returned

•	Multiple media types supported

•	Clear lifecycle: ACTIVE, FULFILLED, CANCELLED

Exception Handling

•	Custom ValidationException for domain rule violations

•	Clean and consistent error feedback

•	Separation of business errors from programming errors

Console UI

A simple, intuitive menu for interacting with the system:

•	List items

•	List members

•	Checkout item

•	Return item

•	Reserve item

•	Add new media

⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻

🧩 Design Patterns & OOP Concepts

Strategy Pattern

Used for loan duration and fine calculation policies.
Enables swapping rules without touching domain logic.

Custom Exceptions

ValidationException clearly separates domain-level rule violations from internal technical errors.

Inheritance & Abstraction

MediaItem → Book, Dvd, Magazine
Person → Member (and optionally Librarian)

Encapsulation

All fields are private with controlled access via getters/setters.

⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻

🔧 Technologies

•	Java (Standard JDK)

•	Console I/O

•	UML

•	No frameworks or external libraries

⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻

🚀 Future Improvements

•	JSON or file-based persistence

•	GUI (JavaFX or Swing)

•	Authentication for librarians

•	Enhanced searching & reporting tools

•	Configurable loan/fine rules per media type

⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻

📄 License

MIT License — free to modify and use.

⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻⸻

🙋‍♂️ Author

Leo Baldwin
