# REPORT.md — Curated Playlist Generator

**Course:** ENSE 375 — Software Testing and Validation  
**Institution:** University of Regina  
**Submission Date:** April 10, 2026  
**Team Members:**
- Rudra Patel - 200498392
- Nathan Okoh — 2004929890
- Youssef Abdelaziz - 200511755
- Rida Hashmi- 200504477
---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Problem Definition](#2-problem-definition)
   - [2.1 Problem Statement](#21-problem-statement)
   - [2.2 Design Requirements](#22-design-requirements)
3. [Engineering Design Process](#3-engineering-design-process)
   - [3.1 Solution 1 — Monolithic Design](#31-solution-1--monolithic-design)
   - [3.2 Solution 2 — Partial MVC Design](#32-solution-2--partial-mvc-design)
   - [3.3 Solution 3 — Final MVC Design and Implementation](#33-solution-3--final-mvc-design-and-implementation)
4. [Collaborative Teamwork and Communication](#4-collaborative-teamwork-and-communication)
5. [Project Management](#5-project-management)
6. [Application Usage](#6-application-usage)

---

## 1. Introduction

Manually building a playlist is a problem most people have encountered but rarely think of as a solvable one. You want to listen to music, but instead of listening you spend time scrolling, mentally filtering out genres you are not in the mood for, skipping songs that are too explicit for the setting, and rebuilding the list when the shuffle is wrong. The time and effort this takes is disproportionate to the task.

This project addresses that problem directly. The Curated Playlist Generator is a command-line Java application that accepts three user inputs — genre, explicit content preference, and a maximum song count — and immediately produces a filtered, shuffled playlist drawn from a local song database. The user gets what they asked for in seconds, with no manual selection required.

The primary objective of the project is not the application itself, but the rigorous and systematic testing of it. The application was designed with testability as a first principle, using Model-View-Controller architecture and test-driven development with JUnit 5. The test suite covers white-box structural testing through unit tests, integration tests, and data flow coverage at both the unit and integration levels, and black-box functional testing through boundary value analysis. Decision table, state transition, and use case models are documented in TESTING.md as part of the formal test plan.

---

## 2. Problem Definition

### 2.1 Problem Statement

The problem being solved is that manually curating a playlist is a repetitive and time-consuming task. Every time a user wants background music for studying, a workout playlist, or something to put on at a gathering, they face the same friction: browse the library, mentally filter by mood and genre, pick songs individually, shuffle the result, decide it is wrong, and start again.

There is no lightweight tool that allows a user to specify genre, explicit content preference, and a song count limit together, and immediately receive a ready-to-use randomized playlist. This project builds that tool and tests it comprehensively.

The system must accept the three user parameters, load a song database from a CSV file, filter the songs to match the criteria exactly, shuffle the filtered result using a seeded random algorithm for reproducibility, and present the playlist cleanly. It must also handle edge cases gracefully — an unrecognized genre, an empty result, or a song count that exceeds what is available — without crashing or returning incorrect data.

---

### 2.2 Design Requirements

#### Functions

The system must perform the following operations:

- Load songs from a CSV file at startup, parsing each row into a Song object with title, artist, genre, and explicit flag fields.
- Filter songs by genre using a case-insensitive match, or return all songs when the genre is set to ANY.
- Filter songs by explicit content preference, excluding explicit songs when the user disables them.
- Accept a maximum song count where values of zero or below indicate no limit.
- Shuffle the filtered result using a seeded random algorithm.
- Display the playlist as a numbered list showing title, artist, genre, and explicit status.
- Allow the user to view the current playlist without regenerating it.
- Allow the user to reset the current playlist.
- Exit the application gracefully.

#### Objectives

| Objective | Description |
|---|---|
| Correctness | Filtering must produce only songs that exactly match the user's genre and explicit preference. |
| Reliability | Invalid inputs such as unknown genres, empty results, or out-of-range song counts must be handled without exceptions reaching the user. |
| Testability | All business logic must be isolated from the view layer so that every component can be unit tested independently using JUnit 5. |
| Maintainability | MVC architecture must be strictly observed so that model, controller, and view layers can be modified independently without side effects. |
| Reproducibility | The shuffle algorithm must use a fixed seed so that test results are deterministic and repeatable across runs. |

#### Design Constraints

| Constraint | Application to This Project |
|---|---|
| Economic | All tools and libraries used are free and open-source: Java, JUnit 5, and Maven. No paid APIs, paid datasets, or licensed music services are used. |
| Regulatory and Access Control | The explicit content filter implements a direct access control mechanism. When a user disables explicit content, the system guarantees that no explicit songs appear in the output. This restriction is enforced and verified by automated tests. |
| Reliability | Every edge case — unknown genre, empty match set, song count exceeding available songs, no-limit sentinel values — is handled and covered by a JUnit test case. |
| Ethical and Societal | Giving users explicit control over the content they are exposed to is a deliberate design decision. The system does not make assumptions about what a user wants; it enforces the parameters provided. |

---

## 3. Engineering Design Process

The team followed the structured engineering design process from the project description, iterating through three design solutions before arriving at the final implementation.

---

### 3.1 Solution 1 — Monolithic Design

#### Application Design

The first iteration placed all logic — CSV loading, filtering, shuffling, and output display — inside a single class with no separation of concerns. Songs were represented as parallel arrays, and filtering was implemented as nested conditionals directly inside the main method.

#### Analysis

This design was quick to prototype but had fundamental flaws that made it unsuitable for the project's testing objectives. There were no boundaries between data, logic, and presentation, which meant no component could be tested in isolation. Any change to the filtering logic required understanding and potentially modifying the display logic. The shuffle had no seed control, making any test of it non-deterministic. This solution was rejected because it could not support a meaningful JUnit test suite and did not satisfy the testability objective.

---

### 3.2 Solution 2 — Partial MVC Design

#### Application Design

The second iteration introduced dedicated model classes: Song to represent a single song, and SongDatabase to handle CSV loading and storage. These were separated from the main application logic. However, filtering and shuffling remained inside the application entry point with no controller layer, and the shuffle used an unseeded Collections.shuffle call.

#### Analysis

This was a meaningful improvement. Song and SongDatabase could be unit tested independently with JUnit 5, and the CSV parsing logic was isolated and reusable. However, the filtering logic remained entangled with the display layer and could not be tested in isolation. The lack of seed control on the shuffle made deterministic testing of the full generation flow impossible. The design partially met the testability objective but did not meet it fully.

---

### 3.3 Solution 3 — Final MVC Design and Implementation

#### Application Design

The final design implements strict MVC separation across five model classes, one controller, and one view. The project structure is as follows:

```
src/
├── main/
│   ├── java/
│   │   ├── App.java
│   │   ├── model/
│   │   │   ├── Song.java
│   │   │   ├── SongDatabase.java
│   │   │   ├── Playlist.java
│   │   │   └── Shuffle.java
│   │   ├── controller/
│   │   │   └── PlaylistController.java
│   │   └── view/
│   │       └── CLIview.java
│   └── resources/
│       └── songs.csv
└── test/
    └── java/
        ├── model/
        │   ├── SongTest.java
        │   ├── SongDatabaseTest.java
        │   ├── ShuffleTest.java
        │   ├── PlaylistTest.java
        │   └── DataFlowTest.java
        └── controller/
            ├── PlaylistControllerTest.java
            ├── DataFlowIntegrationTest.java
            ├── BoundaryValueTest.java
            └── UseCaseTest.java

```

**Class responsibilities:**

| Class | Layer | Responsibility |
|---|---|---|
| Song | Model | Immutable data object. Stores title, artist, genre, and explicit flag. Auto-generates a unique ID via System.identityHashCode. |
| SongDatabase | Model | Reads songs.csv on construction. Parses each row into a Song object. Exposes an unmodifiable list of all songs. |
| Shuffle | Model | Wraps Collections.shuffle with a configurable seed for deterministic, reproducible shuffling. |
| Playlist | Model | Receives a pre-filtered, pre-shuffled list from the controller. Stores it internally and exposes getSongs, isEmpty, and toDisplayString. |
| PlaylistController | Controller | Receives generation requests from the view. Performs filtering, invokes Shuffle, constructs Playlist, and maintains the current playlist state. |
| CLIview | View | Renders the CLI menu and reads user input. Delegates all logic and decisions to the controller with no business logic of its own. |

**Song database composition:**

| Genre | Total Songs | Explicit | Clean |
|---|---|---|---|
| Pop | 59 | 6 | 53 |
| Hip-Hop | 43 | 34 | 9 |
| Rock | 37 | 7 | 30 |
| EDM | 36 | 3 | 33 |
| Jazz | 31 | 3 | 28 |
| Total | 206 | 53 | 153 |

#### Analysis

Solution 3 fully satisfies all design requirements and objectives. The clean MVC separation made it possible to test every layer independently using JUnit 5, and the seeded shuffle made the full generation pipeline deterministic and reproducible across all test runs.

**Requirement coverage:**

| Requirement | Met | How |
|---|---|---|
| Load songs from CSV | Yes | SongDatabase constructor parses the file at startup |
| Filter by genre and explicit | Yes | PlaylistController.generatePlaylist applies both filters |
| Enforce maxSongs | Yes | subList trim applied when maxSongs is positive and less than available |
| Shuffle results reproducibly | Yes | Shuffle class with fixed seed 42 |
| Display playlist | Yes | Playlist.toDisplayString rendered through CLIview |
| Reset playlist | Yes | PlaylistController.reset clears the current state |
| Handle invalid input | Yes | Returns null and prints a message with no exception propagation |
| Full JUnit testability | Yes | Every class is independently testable |

**Solution comparison:**

| Criterion | Solution 1 | Solution 2 | Solution 3 |
|---|---|---|---|
| MVC Architecture | No | Partial | Yes |
| Unit Testable Model Classes | No | Yes | Yes |
| Unit Testable Controller | No | No | Yes |
| Deterministic Shuffle | No | No | Yes |
| Graceful Edge Case Handling | No | Partial | Yes |
| Supports Full JUnit Test Suite | No | Partial | Yes |

---

#### Testing Summary

All tests are written in Java using JUnit 5 and are located in the src/test directory. The test framework is configured through Maven. For the complete test plan including data flow graphs, decision tables, state transition diagrams, and use case tables, see [TESTING.md](TESTING.md).

**White-Box Structural Testing:**

| Technique | Test File | Tests |
|---|---|---|
| Unit — Song | SongTest.java | 3 |
| Unit — SongDatabase | SongDatabaseTest.java | 6 |
| Unit — Shuffle | ShuffleTest.java | 3 |
| Unit — Shuffle | PlaylistTest.java | 9 |
| Integration | PlaylistControllerTest.java | 9 |
| Data Flow — Unit Level | DataFlowTest.java | 9 |
| Data Flow — Integration Level | DataFlowIntegrationTest.java | 4 |

**Black-Box Functional Testing:**

| Technique | Test File | Tests |
|---|---|---|
| Boundary Value | BoundaryValueTest.java | 7 |
| Use Case | UseCaseTest.java | 9 |

**Report-Only Models documented in TESTING.md:**

| Technique | Location |
|---|---|
| Decision Table | TESTING.md |
| State Transition | TESTING.md |

All 58 executable tests pass.

---

## 4. Collaborative Teamwork and Communication

### Team Roles and Responsibilities

| Member | Primary Responsibilities |
|---|---|
| Rudra Patel | [Role — e.g., PlaylistController, SongDatabaseTest , SongDatabase, integration testing] |
| Nathan | [Role — e.g.,App, CLI, Song, SongTest, BoundaryValueTest] |
| Youssef | [Role — e.g., Playlist, Shuffle, ShuffleTest, DataFlowTest, DataFlowIntegrationTest] |
| Rida | [Role — e.g., PlaylistController, PlaylistControllerTest, REPORT.md, TESTING.md] |

### Teamwork Strategy

The team held weekly meetings to review progress and resolve any outstanding design or implementation questions. Work was divided by component so that each team member had clear ownership of specific classes and their corresponding JUnit test files. This minimized merge conflicts and kept each person accountable for the correctness of their own code.

Design decisions were made by consensus. Where there was disagreement about an implementation approach, the team resolved it by writing JUnit test cases first — whichever design produced cleaner, more straightforward tests was adopted. This kept the team aligned with the project's core objective of testability.

### Communication

The team communicated through [platform] for day-to-day updates and used GitHub for all code collaboration. Commits were made regularly throughout the project, not only at milestone deadlines, to keep the repository up to date and provide a clear record of incremental progress.

---

## 5. Project Management

### Schedule

| Deliverable | Section | Target Date | Status |
|---|---|---|---|
| Problem Definition | Section 2.1 | January 23 | Complete |
| Design Constraints and Requirements | Section 2.2 | January 30 | Complete |
| Iterative Design — Solutions 1 and 2 | Sections 3.1 and 3.2 | February 13 | Complete |
| Final Design, Implementation, and Testing | Section 3.3 | March 27 | Complete |
| Collaborative Teamwork and Communication | Section 4 | April 10 | Complete |
| Final Report and Test Plan | REPORT.md, TESTING.md | April 10 | Complete |

### Weekly Progress

| Week | Work Completed |
|---|---|
| 1 — 2 | Project topic approved, team formed, GitHub repository created, songs.csv assembled with 206 songs across five genres |
| 3 | Problem definition drafted, Song and SongDatabase stubs created, initial CSV parsing implemented |
| 4 | Design constraints documented, CSV loading completed, SongTest and SongDatabaseTest written with JUnit 5 |
| 5 — 6 | Solution 1 prototyped and rejected, Solution 2 designed and partially implemented, model classes unit tested |
| 7 — 8 | Solution 3 architecture finalized, Playlist and Shuffle classes implemented, ShuffleTest written, full MVC structure in place |
| 9 — 10 | PlaylistController implemented, PlaylistControllerTest written, data flow graphs drafted for all three model classes |
| 11 — 12 | DataFlowTest and DataFlowIntegrationTest completed, BoundaryValueTest written, all 41 tests passing |
| 13 — 14 | REPORT.md and TESTING.md finalized, presentation script written, video demo recorded |

---

## 6. Application Usage

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Running the Application

```bash
git clone https://github.com/RudraPatel2525/Curated.git
cd Curated
mvn compile
mvn exec:java -Dexec.mainClass="App"
```

### Running the Tests

```bash
mvn test
```

### CLI Menu

```
=== Curated Playlist Generator ===

1. Generate Playlist
2. View Playlist
3. Reset Playlist
0. Exit

Choose option:
```

### Example Usage

```
Genre (e.g., Pop, Hip-Hop, Rock, EDM, Jazz or ANY): Hip-Hop
Allow explicit? (yes/no): no
Max songs (0 = no limit): 10

=== PLAYLIST: Generated Playlist ===
1. Sicko Mode - Travis Scott (Hip-Hop)
2. HUMBLE. - Kendrick Lamar (Hip-Hop)
...
Total songs: 9
```

### CSV Format

Songs are stored in src/main/resources/songs.csv with the following format:

```
title,artist,genre,explicit
Blinding Lights,The Weeknd,Pop,false
God's Plan,Drake,Hip-Hop,true
```

Each row contains four comma-separated fields: title, artist, genre, and explicit (true or false). The first row is a header and is skipped automatically by SongDatabase during parsing.
