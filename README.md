![University of Regina Logo](https://www.uregina.ca/communications-marketing/assets/visual-identity/tagline-urlogo-white-background/ur_logo-w-1-line-tagline_horiz_full-colour_rgb.png)

# ENSE 375 Project - Software Testing and Validation

**Team Members:**
- Nathan Okoh - 200492890
- Rida Hashmi- 200504477
- Rudra Patel - 200498392
- Youssef Abdelaziz - 200511755

---

## Table of Contents
1. [Introduction](#10-introduction)
2. [Design Problem](#20-design-problem)
    - [2.1 Problem Definition](#21-problem-definition)
    - [2.2 Design Requirements](#22-design-requirements)
        - [2.2.1 Functions](#221-functions)
        - [2.2.2 Objectives](#222-objectives)
        - [2.2.3 Constraints](#223-constraints)
3. [Solution](#30-solution)
4. [Team Work](#40-team-work)
    - [4.1 Meeting 1](#41-meeting-1)
    - [4.2 Meeting 2](#42-meeting-2)


---

## List of Figures

---

## List of Tables

---

## 1.0 Introduction

---


## 2.0 Design Problem

### 2.1 Problem Definition
This project is defined as the design, development, and comprehensive testing of a playlist generator. The application will generate music playlists based on the users preferences and inputs like mood, time of day, age restricitions, genre, etc. It will use rules and states machines to ensure predictible behaviour, consistent outcomes, and high testability. The focus of the project will not be the UI or the complexity and "taste" of the recommendations themselves but rather the design and implementation of an automated, rule-based system with well defined logic, state machines, and clear constraints. This allows us to apply a wide range of software testing and validation techniques.

### 2.2 Design Requirements

### 2.2.1 Functions
The primary function of the Playlist Generator are as follows...
- Accept user input for playlist criteria, including genre, mood, explicit/non-explicit, max playlist size, max playlist duration
- Read song data and song attributes from a text file
- Filter songs based on user-defined playlist criteria
- Use a seeded "shuffle" to generate playlist
- Detect and handle cases where insufficient valid songs exist to generate a playlist
- Display the generated playlist, without saving

### 2.2.2 Objectives
The primary motivation for this project is to practice and demonstrate rigorous software testing and validation techniques in a controlled, realistic setting. The project is intentionally designed to emphasize deterministic, testable logic over UI complexity or data persistence.
Specifically, this project aims to:
- Apply Test-Driven Development (TDD) throughout the development lifecycle.
- Use a state-machine–based design to support required testing methods such as state transition testing.
- Implement a discrete, rule-based system to ensure deterministic behavior and verifiable outcomes.
- Maintain a realistic yet manageable scope suitable for comprehensive verification.
- Prioritize core logic and backend behavior over UI and database implementation to focus on testing quality rather than presentation.

To achieve these objectives, the project will:
- Develop a testable MVC architecture, ensuring modularity and separation of concerns to support TDD.
- Implement comprehensive testing suites, including:
    - Unit and Path Testing to verify internal logic and control flow.
    - Black-Box Testing, applying boundary value analysis and equivalence class testing to user inputs (e.g., age, mood, genre).
    - State Transition Testing to verify correct transitions between system states (e.g., Selection → Generation → Error).
- Ensure deterministic system behavior, enabling reliable automation and repeatable test results.
- Address regulatory and ethical design constraints, such as enforcing age-based restrictions.
- Maintain strict requirement-to-test traceability through a traceability matrix that links each requirement to one or more test cases (Features without explicit requirements will not be implemented).
- Use a minimalist UI/interface to keep focus on validation, verification, and system behavior rather than aesthetics.

### 2.2.3 Constraints
---
| Constraint | Description |
|----------|----------|
| UI / Interface Constraints | <ul><li>The application will use a simple, minimalist interface (text-based or basic GUI).</li><li>
UI design prioritizes functionality and testability over visual design.</li><li>No advanced frontend frameworks or UI-heavy features will be used.</li></ul> | 
| Regulatory Compliance (Security & Access) | <ul><li>Age restrictions must be strictly enforced based on user input.</li><li>The system must comply with basic data protection principles:</li><li>No storage of personally identifiable information (PII) such as real names, emails, or listening history.</li><li>All inputs are transient and used solely for playlist generation logic.</li></ul>| 
| Reliability & Determinism |  |
| Technical & Development Constraints | |
| Economic Constraints | |
| Societal & Ethical Considerations | |

## 3.0 Solution

--

## 4.0 Team Work

### 4.1 Meeting 1

Time: Jan 8, 2026 11:30am to 12:15pm
Agenda: Brainstorm Software Application
| Team Member | Previous Task | Completion State | Next Task |
|------------|---------------|------------------|-----------|
| Nathan | N/A | N/A | Set meeting with proffessor for feedback on ideas |
| Rudra | N/A | N/A | Set up Github, and invite members |
| Rida | N/A | N/A | Brainstorm ideas |

* Youssef had not yet been added to the team.

### 4.2 Meeting 2

Time: Jan 14, 2026 9:20pm to 9:35pm
Agenda: Decide on Software Application
| Team Member | Previous Task | Completion State | Next Task |
|------------|---------------|------------------|-----------|
| Nathan | Set meeting with proffessor | Done| Finish problem definition |
| Rudra | Set up Github, and invite members | Done | Meet and discuss application function, objectives, and constraints|
| Rida | Brainstorm ideas | done | Meet and discuss application function, objectives, and constraints |

* Youssef had not yet been added to the team.

### 4.3 Meeting 3

Time: Jan 20, 2026 11:45m to 1:00pm
Agenda: Discuss application function, requirements, objectives, constraints, and name.
| Team Member | Previous Task | Completion State | Next Task |
|------------|---------------|------------------|-----------|
| Nathan |Finish problem definition | Done| Finish Section 2.2.1 Functions |
| Rudra | Meet and discuss application function, objectives, and constraints | Done | Finish Section 2.2.3 Constraints|
| Rida | Meet and discuss application function, objectives, and constraintss | Done | Finish Section 2.2.2 Objectives |
| Youssef | Meet and discuss application function, objectives, and constraints | Done | Finish Section 2.2.3 Constraints|







