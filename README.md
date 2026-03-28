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
    - [4.3 Meeting 3](#43-meeting-3)
    - [4.4 Meeting 4](#44-meeting-4)
    - [4.5 Meeting 5](#45-meeting-5)
    - [4.6 Meeting 6](#46-meeting-6)


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
- Accept user input for playlist criteria, including genre, explicit/non-explicit, max playlist size, etc
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
### 3.1	Solution 1: GUI Based Playlist Generator with External APIs
#### Description:
The first proposed solution was a graphical user interface (GUI)-based application that integrates with external music APIs (like Spotify). Users would input preferences through a visual interface, and the system would fetch real-time song data, filter it, and generate playlists dynamically.
#### Pros:

- User-friendly and visually appealing interface
- Access to large, real-time music libraries
- Data Variety: External APIs provide access to a massive, continuously updated music library.

#### Cons:

- High Testing Complexity: Requires extensive mocking for APIs and complex UI automation.
- Dependency on external services, leading to unreliable test results
- Low Repeatability: AI generation is non-deterministic, making output unpredictable.
- Long Development Time: Backend and frontend setup consumes time meant for testing.
- High Learning Curve: Requires extensive knowledge outside the core course scope.

#### Reason for not selecting this solution (Testing Perspective):

This approach violates the core project objective of deterministic and testable logic. Because API responses and UI interactions are unpredictable, it becomes difficult to perform repeatable automated tests, making it unsuitable for TDD and rigorous verification. The high complexity also makes the development time quite long

### 3.2	Solution 2: GUI-Based System with Local Database
#### Description:
The second solution removed external APIs and replaced them with a local datasebase while keeping the GUI. The system would still provide a visual interface but operate on a fixed dataset to improve determinism.

#### Pros

- More Deterministic than Solution 1 (no API dependancy)
- Controlled dataset enables consistent results
- Supports Backend Unit Testing: Filtering logic can be tested independently.

#### Cons

- UI Testing Still Required
- Higher Testing Overhead: Requires integration testing between UI, backend, and database.
- More Configuration Required: MongoDB or SQLite setup adds complexity.

#### Reason for not selecting this solution: 
Although this solution improves determinism, it still conflicts with the project’s focus on efficient and comprehensive testing. GUI-based systems make unit testing and path testing harder, reducing the ability to fully validate internal logic.

### 3.3	Final Solution - CLI Based System with Local Dataset

The final solution is a command-line based, rule-driven playlist generator that uses a CSV file as a lightweight database. The system collects user preferences (ex: genre, explicitly/non-explicit, playlist size) through command-line prompts, filters songs from the database based on those criteria, and generates a playlist automatically. The final playlist is shuffled using a seed-based randomizer to simulate randomness while maintaining repeatability for testing.

This design deliberately narrows the project scope compared to earlier solutions. It removes graphical interfaces and external API integrations in order to prioritize determinism, modularity, and testability. The system is divided into clear components like input handling, filtering logic, etc. This allows sach component to be tested independently

| Criteria              | Solution 1 (API + GUI) | Solution 2 (GUI + Local DB) | Final Solution (CLI + CSV) |
| --------------------- | ---------------------- | --------------------------- | -------------------------- |
| Determinism           | Low                    | Medium                      | High                       |
| Testability           | Poor                   | Moderate                    | Excellent                  |
| TDD Support           | Difficult              | Limited                     | Fully supported            |
| External Dependencies | Yes                    | No                          | No                         |
| Development Time      | High                   | Medium                      | Low                        |
| Complexity            | High                   | Medium                      | Low                        |
| Requirement Alignment | Weak                   | Partial                     | Strong                     |


### 3.3.1 Components

The final solution is composed of five core components, each with a distinct 
responsibility within the MVC architecture. This separation of concerns ensures 
that each component can be tested independently, which is central to the 
project's Test-Driven Development approach.

| Component | Purpose | Testing Method |
|---|---|---|
| `Song.java` | Represents a single song with attributes: title, artist, genre, and explicit flag. Acts as the core data model. | Boundary Value Analysis, Equivalence Class Testing |
| `SongDatabase.java` | Reads and parses song data from a `.txt` file, returning a list of `Song` objects. Decouples data loading from logic. | Equivalence Class Testing (valid/invalid file input) |
| `Playlist.java` | Holds the filtered, ordered list of songs. Applies all rule-based filtering logic (genre, explicit preference, max size) and exposes a method to print the playlist. | Boundary Value Analysis, Equivalence Class Testing, Logic/Path Testing |
| `PlaylistController.java` | Acts as the MVC controller. Coordinates the flow between SongDatabase, Shuffle, and Playlist. | Use Case Testing, State Transition Testing |
| `Shuffle.java` | Shuffles the filtered song list using a seeded random algorithm to ensure deterministic, repeatable output. | Boundary Value Analysis, Equivalence Class Testing |
| `CLIView.java` | Handles all user input and output via the command line. Collects user preferences and passes them to the controller. | Decision Table Testing, Use Case Testing |

The interaction between all components follows a strict unidirectional flow, 
as illustrated in Fig. 1 below. The user provides input through the CLIView, 
which delegates to the PlaylistController. The controller loads songs from 
SongDatabase applies filters via FilterEngine, shuffles the result using 
Shuffle wraps the output in a Playlist object and returns it to the view 
for display. No data is persisted at any stage.

### Component Interaction Diagram
```
┌─────────────────────────────────────────────────────────────┐
│                        CLIView.java                         │
│                  Collects user preferences                  │
└───────────────────────────┬─────────────────────────────────┘
                            │ genre, allowExplicit, maxSongs
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  PlaylistController.java                    │
│               Coordinates the full pipeline                 │
└──────┬──────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────┐
│ SongDatabase    │
│    .java        │
│  Loads songs    │
│   from file     │
└────────┬────────┘
         │ full List<Song>
         ▼
┌─────────────────────────────────────────────────────────────┐
│                       Playlist.java                         │
│        Filters songs by genre, explicit, and max size       │
│              then holds the final song list                 │
└───────────────────────────┬─────────────────────────────────┘
                            │ filtered List<Song>
                            ▼
                  ┌──────────────────┐
                  │   Shuffle.java   │
                  │  Reorders songs  │
                  │   (seeded RNG)   │
                  └────────┬─────────┘
                           │ shuffled List<Song>
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                        CLIView.java                         │
│                 printPlaylist() → displays to user          │
└─────────────────────────────────────────────────────────────┘

Fig. 1: Block diagram showing the interaction and data flow
        between all system components.
```

### 3.3.2	Environmental, Societal, Safety, and Economic Considerations
#### Environmental Considerations
The application is entirely software-based and requires no physical hardware beyond a standard personal computer. It operates on a static local dataset, eliminating the need for network requests or cloud infrastructure, which reduces energy consumption compared to API-dependent alternatives. No data is written to disk during runtime, minimizing unnecessary storage usage.

#### Societal Considerations
The playlist generator was designed to be inclusive and neutral. Genre and explicit content filters are applied strictly based on user-defined input, with no assumptions made about the user's demographics, background, or preferences. The explicit content filter ensures the application can be used safely in environments with minors or in contexts where content restrictions are required. No personally identifiable information (PII) such as names, emails, or listening history is collected or stored at any point.

#### Safety and Reliability
The system is built on deterministic, rule-based logic. Given identical inputs, the application will always produce identical outputs, ensuring predictable and verifiable behaviour. Input validation is handled at the filter level, and edge cases such as empty datasets or no matching songs are explicitly handled with informative output rather than silent failures or crashes. The use of a seeded random algorithm in the shuffle component ensures that randomness is controlled and repeatable for testing purposes.

#### Economic Considerations
The application was developed entirely using open-source tools and libraries, incurring no licensing or API costs. The Java-based implementation runs on any standard machine without requiring paid hosting, cloud services, or external subscriptions. This makes the solution fully reproducible by any team member or evaluator at no cost.

### 3.3.3	Test Cases and results
#### Solution Evaluation
Prior to finalizing the design, the team evaluated three candidate solutions against the project's defined functions, objectives, and constraints. The table below summarizes the comparison:

| Criteria | Solution 1 (Web + AI) | Solution 2 (Web + DB) | Final Solution (CLI) |
|---|---|---|---|
| Deterministic output | ✗ | Partial | ✓ |
| No external dependencies | ✗ | ✗ | ✓ |
| Supports TDD | ✗ | Partial | ✓ |
| Testable without mocking | ✗ | ✗ | ✓ |
| Meets all defined functions | ✓ | ✓ | ✓ |
| Satisfies constraints | ✗ | Partial | ✓ |
| Open-source only | Partial | Partial | ✓ |

The final CLI-based solution was the only candidate to satisfy all defined functions, objectives, and constraints simultaneously, and was the only solution that fully supported Test-Driven Development without requiring external mocking frameworks or infrastructure setup.

#### Use Case UML
To ensure the final solution met all required system functions, the team developed a Use Case UML diagram as a testing measure. Each use case was mapped directly to one or more test cases, forming the basis of the traceability matrix between system requirements and test coverage.


#### Test Suites
Four test suites were designed and executed to validate the prototype:

| Test Suite | File | Methods Covered | Testing Techniques |
|---|---|---|---|
| Song & Shuffle | SongTest.java, ShuffleTest.java | Song construction, attribute access, seeded shuffle, order verification | Boundary Value Analysis, Equivalence Class Testing |
| Playlist | PlaylistTest.java | Genre filter, explicit filter, max size cap, filtering logic inside Playlist | Boundary Value Analysis, Equivalence Class Testing, Logic Testing |
| Playlist Controller | PlaylistControllerTest.java | Full pipeline flow, edge cases, reset, sequential generation | Use Case Testing, State Transition Testing |
| View & Integration | CLIView.java, App.java | User input handling, end-to-end flow, decision logic | Decision Table Testing, Use Case Testing |

Each test suite was executed using JUnit 5 within a Maven project
structure. Tests were run independently to isolate failures to specific
components, and the full suite was executed after each code change to
verify no regressions were introduced.

### 3.3.4 Limitations

While the final solution successfully meets the defined functional requirements 
and design constraints, several limitations are acknowledged:

**Static Dataset:** The system relies entirely on a predefined, static `songs.txt` 
file as its data source. Songs cannot be added, removed, or updated at runtime. 
This means the quality and variety of generated playlists is directly limited 
by the size and diversity of the dataset provided.

**No Mood or Tempo Filtering:** The current `Song` model only stores title, 
artist, genre, and explicit flag. Attributes such as mood, tempo, or release 
year — which are common playlist criteria — are not supported. Extending the 
system to include these would require changes to the data model, the database 
parser, and the filter engine.

**No Persistent Storage:** Playlists are generated and displayed in memory only. 
There is no functionality to save, export, or retrieve a previously generated 
playlist. Each run of the application produces a fresh result with no history.

**Command-Line Interface Only:** The application uses a minimal CLI for all 
user interaction. This limits accessibility for users who are unfamiliar with 
terminal environments and restricts the ability to present richer playlist 
information such as album art or audio previews.

**No Duplicate Detection:** If the `songs.txt` file contains duplicate entries, 
the system will treat them as distinct songs and may include duplicates in a 
generated playlist. No deduplication logic is currently implemented.

**Limited Scalability:** The system loads and filters the entire song list into 
memory on every playlist generation. For very large datasets this approach 
would become inefficient, as no indexing, caching, or lazy-loading strategy 
is employed.



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


### 4.4 Meeting 4

Time: Feb 5, 2026, 11:40 am to 1:00 pm
Agenda: Discuss different design processes, trade-offs, complexity, and testability for each solution. 
| Team Member | Previous Task | Completion State | Next Task |
|------------|---------------|------------------|-----------|
| Nathan |Finish Section 2.2.1 Functions | Done| Finish Section 3.3.3 Solution 3 |
| Rudra | Finish Section 2.2.3 Constraints | Done | Research potential database |
| Rida | Finish Section 2.2.2 Objectives | Done | Finish Section 3.3.2 Solution 2 |
| Youssef | Finish Section 2.2.3 Constraints | Done | Finish Section 3.3.1 Solution 1|



### 4.5 Meeting 5

Time: Feb 17, 2026, 11:40 am to 1:00 pm
Agenda: Set up Maven and assign roles.
| Team Member | Previous Task | Completion State | Next Task |
|------------|---------------|------------------|-----------|
| Nathan | Finish Section 3.3.3 Solution 3 | Done| Do SongTest.java and ShuffleTest.java (Boundary, Equivalance song and shuffle logic)|
| Rudra | Research potential database | Done | Finish SongdatabaseTest.java file |
| Rida | Finish Section 3.3.2 Solution 2 | Done | Finish PlaylistControllerTest.java file |
| Youssef | Finish Section 3.3.1 Solution 1 | Done | Finish FilterEngineTest.java (Boundary, Equivalence, and Filtering logic)  |


### 4.6 Meeting 6

Time: Mar 11, 2026, 9:40 pm to 10:35 pm
Agenda: Touchbase.
| Team Member | Previous Task | Completion State | Next Task |
|------------|---------------|------------------|-----------|
| Nathan | Do SongTest.java and ShuffleTest.java (Boundary, Equivalance song and shuffle logic) | Done| Finish Song.java and Shuffle.java file |
| Rudra | Finish SongdatabaseTest.java file | Done | Finish Songdatabase.java file |
| Rida | Finish PlaylistControllerTest.java file | Done | Finish playlist.java file |
| Youssef | Finish FilterEngineTest.java (Boundary, Equivalence, and Filtering logic) | Done | Finish FilterEngine.java file |



