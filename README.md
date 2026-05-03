
# Java ATM with GUI

A complete ATM (Automated Teller Machine) simulator built in Java with a 
graphical user interface — featuring multiple account types, transaction 
handling, and custom exception management.

## 🎯 Project Overview

Object-oriented simulation of real-world banking operations: authentication, 
balance enquiry, deposits, withdrawals, transfers and transaction history — 
across four different account types with their own rules and behaviour.

## 🏗️ Architecture

The project follows OOP principles with clear separation of concerns:

```
atm-java-gui/
├── Account.java                    → Abstract base class for all accounts
├── SavingsAccount.java             → Savings account implementation
├── ChequeAccount.java              → Cheque/Current account
├── FixedAccount.java               → Fixed-term deposit account
├── NetSaverAccount.java            → High-interest online savings
├── ATMBank.java                    → Bank logic, account management
├── ATMGUI.java                     → Swing-based graphical interface
├── InsuficientBalanceException.java → Custom exception (overdraft)
├── NegativeValueException.java     → Custom exception (invalid input)
└── TestATM.java                    → Unit tests
```

## 🔧 Key OOP Concepts Applied

| Concept | Implementation |
|---|---|
| Inheritance | `Account` as abstract base; 4 specialised account subclasses |
| Polymorphism | Common operations (deposit, withdraw) overridden per account type |
| Encapsulation | Private balance/account data with controlled access via methods |
| Custom Exceptions | `InsuficientBalanceException` and `NegativeValueException` for safe error handling |
| GUI Programming | Swing-based interface with event-driven user interactions |
| Testing | `TestATM` validates core banking logic |

## 💡 Features

- 🔐 User authentication and account selection
- 💰 Deposit, withdraw and balance check operations
- 📜 Transaction history per account
- 🛡️ Robust input validation (no negative amounts, no overdraft beyond limits)
- 🪟 Clean, intuitive GUI built with Java Swing
- 🧪 Test class to validate core functionality

## 🚀 How to Run

1. Make sure you have Java JDK installed (version 8+)
2. Compile all `.java` files:
```bash
   javac *.java
```
3. Run the GUI:
```bash
   java ATMGUI
```
4. Or run the test class:
```bash
   java TestATM
```

## 📚 What I Learned

- Designing a class hierarchy with abstract classes and inheritance
- Applying polymorphism to handle different account behaviours uniformly
- Creating and throwing custom exceptions for domain-specific errors
- Building event-driven user interfaces with Java Swing
- Structuring a multi-class project for maintainability and testability

---

📂 Part of my IT learning journey at the Academy of Interactive Technology (AIT) — see more projects at [github.com/Lmg15186](https://github.com/Lmg15186)
