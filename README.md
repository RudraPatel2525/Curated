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
This project is defined as the design, development, and comprehensive testing of a playlist generator. The application will generate music playlists based on the users preferences and inputs like mood, time of day, age restrictions, genre, etc. It will use rules and states machines to ensure predictable behaviour, consistent outcomes, and high testability. The focus of the project will not be the UI or the complexity and "taste" of the recommendations themselves but rather the design and implementation of an automated, rule-based system with well defined logic, state machines, and clear constraints. This allows us to apply a wide range of software testing and validation techniques.

### 2.2 Design Requirements

### 2.2.1 Functions
The primary function of the Playlist Generator are as follows...
- Accept user input for playlist criteria, including genre, mood, explicit/non-explicit, max playlist size, max playlist duration
- Read song data and song attributes from a text file
- Filter songs based on user-defined playlist criteria
- Use a seeded "shuffle" to generate a playlist
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
| Constraint Category | Description |
|-------------------|-------------|
| UI / Interface | The application will use a simple, minimalist interface (text-based or basic GUI). UI design prioritizes functionality and testability over aesthetics. No advanced frontend frameworks or UI-heavy features will be used. |
| Regulatory Compliance (Security & Access) | Age restrictions must be strictly enforced based on user input. The system must comply with basic data protection principles and must not store any personally identifiable information (PII) such as real names, emails, or listening history. |
| Reliability & Determinism | Given identical inputs (e.g., Mood: Happy, Time: Morning), the state machine must traverse the exact same sequence of states and produce identical playlist rules and outputs every time. |
| Input Constraints | User inputs must be restricted to a fixed, predefined set, including mood, explicit/non-explicit preference, genre, playlist size, and playlist duration. |
| Data Constraints | The system must operate on a static or predefined dataset of music metadata (e.g., genre, mood tags, age ratings) and must not rely on live streaming services or external APIs. |
| Architecture & Testability | The system must be designed using a modular MVC architecture to support Test Driven Development (TDD), comprehensive testing, and deterministic verification. |
| Version Control | Git must be used for version control throughout the entire development process. |
| Economic Constraints | The application must be developed using open-source tools and libraries only, with no paid APIs or licensing costs. Deployment must be possible on a low-cost hosting platform or via self-hosting, with scalability to moderate traffic. |
| Societal & Ethical Considerations | The application must be inclusive and neutral, avoiding cultural, gender, or demographic bias in playlist generation logic and rule definitions. |
---

## 3.0 Solution
In this section, you will provide an account of some solutions your team brainstormed to implement and test the project. Some solutions might not have all the desired features, some might not satisfy the constraints, or both. These solutions come up in your mind while you brainstorm ways of implementing all the features while meeting the constraints. Towards, the end you select a solution that you think has all the features, testable and satisfies all the constraints. Remember that an engineering design is iterative in nature! 
### 3.1	Solution 1: Dynamic Web App with External APIs
#### Description:
This solution proposed a web-based application featuring a dynamic user interface. Instead of using a static local dataset, this version would utilize external APIs for retrieving song data and leverage a generative AI model (OpenAI) to create the playlists. The goal was to create a highly engaging user experience with a modern feature set.

#### Pros

- Engaging User Experience: The dynamic UI and AI integration would provide a modern, interactive experience for the user.
- Feature-Rich: Allows for complex features beyond simple filtering, such as "mood-based" generation using natural language processing.
- Data Variety: Accessing external APIs would provide a significantly wider variety of songs compared to a static text file.

 #### Cons

- High Testing Complexity: Testing requires extensive mocking of external services and UI automation.
- Low Repeatability: External APIs and AI models are non-deterministic; they may produce different outputs for the same input, making regression testing difficult.
- Long Development Time: The complexity of setting up a web server and API integration would consume time needed for writing test suites.
- Extensive Knowledge Requirement: Requires specialized knowledge of web frameworks and API authentication that is outside the core scope of this course.

####  Reason for not selecting this solution:

- Reliability Issues: For effective testing (especially regression testing), the system must be deterministic. The "Low Repeatability" of AI responses means we cannot write stable assertions (e.g., assert that 'Happy' input always returns 'Song A').
- Scope Creep: The "Long Dev Time" required to build the web infrastructure would leave insufficient time to implement the required testing strategies (TDD, Path Testing, State Transition).
- Validation Difficulties: Validating a dynamic web UI is far more complex than validating a modular backend logic, forcing us to focus on "making it work" rather than "testing it thoroughly."
### 3.2	Solution 2: Simplified Web App with Local Database
#### Description:
This solution proposed a simplified web-based application with a minimal graphical interface for collecting user preferences. Instead of external APIs, song data would be stored locally in a MongoDB database. Playlist generation would rely on rule-based scoring logic rather than AI.

#### Pros

- More Deterministic than Solution 1: Rule-based filtering ensures consistent outputs.
- No External Dependencies: Local database avoids API mocking.
- Better Control Over Data: Test datasets can be inserted into MongoDB for validation.
- Supports Backend Unit Testing: Filtering logic can be tested independently.

#### Cons

- UI Testing Still Required: Web interface requires UI automation testing (Selenium, etc.).
- Database Dependency: Tests depend on database setup and teardown.
- Higher Testing Overhead: Requires integration testing between UI, backend, and database.
- More Configuration Required: MongoDB setup adds environmental complexity.

#### Reason for not selecting this solution: 

- Database coupling complicates unit testing — requires mock databases or test containers.
- Web UI still distracts from core testing goals.
- Environment setup affects repeatability (database state must be reset before tests).

### 3.3	Final Solution
The final solution is a command-line based, rule-driven playlist generator that uses a structured text file as a lightweight database. The system collects user preferences (ex: genre, mood, explicity/non-explicit, playlist size) through command-line prompts, filters songs from the database based on those criteria, and generates a playlist automatically. The final playlist is shuffled using a seed-based randomizer to simulate randomness while maintaining repeatability for testing.

This design deliberately narrows the project scope compared to earlier solutions. It removes graphical interfaces and external API integrations in order to prioritize determinism, modularity, and testability. The system is divided into clear components like input handling, filtering logic, etc. This allows sach component to be tested independently

### 3.3.1	Components
#### Component 1: Input Handler
#### Purpose:
Collects and validates user input (genre, mood, explicit flag, size).

#### Testing Method:
- Unit testing (valid/invalid input cases)
- Boundary testing (playlist size limits)
- Path testing (valid vs invalid branches)


#### Component 2: Song Database Loader
#### Purpose:
Reads songs from structured text file and converts them into Song objects.

#### Testing Method:
- File parsing tests
- Edge case testing (empty file, malformed data)
- Assertion of correct object creation


#### Component 3: Filtering Engine
#### Purpose:
Filters songs based on user criteria (genre, mood, explicit, etc.).

#### Testing Method:
- Unit testing of each filtering rule
- Path testing (multiple condition combinations)
- Assertion testing for correct subset selection


#### Component 4: Playlist Generator
#### Purpose:
Selects requested number of songs and applies seed-based shuffle.

#### Testing Method:
- Deterministic output validation (same seed → same playlist)
- Boundary testing (requesting more songs than available)
- State testing (empty filtered list behavior)


#### Component 5: Output Formatter
#### Purpose:
Formats playlist for console display.

#### Testing Method:
- String comparison tests
- Formatting validation

### 3.3.2	Environmental, Societal, Safety, and Economic Considerations
#### Environmental Considerations
- No external APIs → reduced server/network dependency.
- Lightweight design → minimal computational resources.
- No persistent cloud infrastructure → lower energy footprint.
  
#### Societal Considerations
- Explicit content filtering allows user control.
- Deterministic behavior avoids unpredictable or biased AI output.
- Transparent rule-based logic improves trustworthiness.
  
#### Economic Considerations
- No paid APIs or hosting services required.
- No database licensing costs.
- Uses open-source tools (Java, JUnit).
  
#### Decision made:
We deliberately chose a low-cost architecture to avoid financial overhead and ensure accessibility.

#### Safety and Reliability
- Deterministic system ensures predictable outputs.
- Input validation prevents crashes from invalid entries.
- Modular design reduces risk of cascading failures.
- Extensive unit testing ensures reliability.
  
### 3.3.3	Test Cases and results
#### Test Suites Designed
#### Input Validation Test Suite
- Invalid genre
- Invalid playlist size
- Negative numbers
- Empty input

#### File Parsing Test Suite
- Properly formatted file
- Empty file
- Corrupted line format

#### Filtering Logic Test Suite
- Genre-only filter
- Mood-only filter
- Explicit filter ON/OFF
- Combined criteria

#### Playlist Generation Test Suite
- Deterministic shuffle test (same seed consistency)
- Different seed produces different order
- Size > available songs
- Zero matching songs

#### Integration Test Suite
- Full flow simulation
- Input → filter → generate → output
---

### 3.3.4	Limitations
#### 1. Limited User Interface
No graphical interface reduces user engagement.

#### 2. Static Dataset
Song database must be manually updated.

#### 3. No AI-Based Recommendations
Lacks advanced personalization.

#### 4. Scalability Limitations
Text file database not ideal for large-scale systems.

#### 5. No Real-Time Updates
Cannot retrieve live trending songs.

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

Time: Jan 20, 2026 11:45 am to 1:00 pm
Agenda: Discuss application function, requirements, objectives, constraints, and name.
| Team Member | Previous Task | Completion State | Next Task |
|------------|---------------|------------------|-----------|
| Nathan |Finish problem definition | Done| Finish Section 2.2.1 Functions |
| Rudra | Meet and discuss application function, objectives, and constraints | Done | Finish Section 2.2.3 Constraints|
| Rida | Meet and discuss application function, objectives, and constraintss | Done | Finish Section 2.2.2 Objectives |
| Youssef | Meet and discuss application function, objectives, and constraints | Done | Finish Section 2.2.3 Constraints|


### 4.3 Meeting 4

Time: Feb 5, 2026, 11:40 am to 1:00 pm
Agenda: Discuss different design processes, trade-offs, complexity, and testability for each solution. 
| Team Member | Previous Task | Completion State | Next Task |
|------------|---------------|------------------|-----------|
| Nathan |Finish Section 2.2.1 Functions | Done| FFinish Section 3.3.3 Solution 3 |
| Rudra | Finish Section 2.2.3 Constraints | Done | Research potential database |
| Rida | Finish Section 2.2.2 Objectives | Done | Finish Section 3.3.2 Solution 2 |
| Youssef | Finish Section 2.2.3 Constraints | Done | Finish Section 3.3.1 Solution 1|







