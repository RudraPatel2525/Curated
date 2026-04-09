# TESTING.md — Curated Playlist Generator (ENSE 375)

---

## Overview

This document describes the complete test plan for the Curated Playlist Generator.  
Testing is organized into two categories:

| Category | Techniques | In Code? |
|---|---|---|
| **White-Box (Structural)** | Unit Testing, Data Flow (unit + integration) | ✅ Yes |
| **Black-Box (Functional)** | Boundary Value, Use Case | ✅ Yes |
| **Report-Only** | Decision Table, State Transition | 📄 Tables/diagrams below |

The primary function under test is `PlaylistController.generatePlaylist(String genre, boolean allowExplicit, int maxSongs)`.

---

## 1. Unit Testing

Unit tests validate each model class in isolation.

| Test File | Class Tested | # Tests |
|---|---|---|
| `SongTest.java` | `Song.java` | 3 |
| `SongDatabaseTest.java` | `SongDatabase.java` | 6 |
| `ShuffleTest.java` | `Shuffle.java` | 3 |
| `PlaylistTest.java` | `Playlist.java` | 9 |

See individual test files for full test case details.

---

## 2. Integration Testing

`PlaylistControllerTest.java` tests the integration of `SongDatabase → PlaylistController → Playlist → Shuffle` using the real `songs.csv` file.

| Test | Description |
|---|---|
| `testInitialState_noPlaylistExists` | Controller starts with no playlist |
| `testGeneratePlaylist_returnsPlaylist_whenMatchesExist` | Valid generate returns a result |
| `testGeneratePlaylist_filtersByGenre` | Genre filter applied correctly |
| `testExplicitNotAllowed_filtersExplicitSongs` | Explicit filter works |
| `testExplicitAllowed_includesExplicitSongs` | Explicit allowed works |
| `testMaxSongs_limitRespected` | maxSongs cap respected |
| `testNoMatch_returnsNull` | No-match case returns null |
| `testReset_clearsPlaylist` | reset() clears state |
| `testGenerateTwice_overwritesPrevious` | Second generate overwrites first |

---

## 3. Data Flow Testing

### 3a. Unit-Level Data Flow

**Test file:** `DataFlowTest.java` (package `model`)

Data flow graphs are drawn below for each unit. Each graph annotates nodes with `def` and `use` sets. Test cases cover all DU pairs.

---

#### Unit 1 — `Song.java`

*See Data Flow Diagram*

| DU Pair | From → To | Test Case |
|---|---|---|
| def(title) → use(getTitle) | Node 1 → Node 2 | `tcS1` |
| def(artist) → use(getArtist) | Node 1 → Node 2 | `tcS1` |
| def(genre) → use(getGenre) | Node 1 → Node 3 | `tcS1` |
| def(explicit) → use(isExplicit) | Node 1 → Node 3 | `tcS1` |
| def(title) → use(toString) | Node 1 → Node 4 | `tcS2` |
| def(explicit) → use(toString, branch) | Node 1 → Node 4 | `tcS2` |

---

#### Unit 2 — `SongDatabase.java`

*See Data Flow Diagram*

| DU Pair | From → To | Test Case |
|---|---|---|
| DEF-2(songs.add) → use(getAllSongs) | Node 2 → Node 3 | `tcDB1` |
| DEF-2(songs.add) → use(size) | Node 2 → Node 4 | `tcDB2` |
| DEF-1(empty) + blank lines skipped → use(getAllSongs) | Node 1,2 → Node 3 | `tcDB3` |
| DEF-2(songs.add) → use(getAllSongs) returns unmodifiable | Node 2 → Node 3 | `tcDB4` |

---

#### Unit 3 — `Shuffle.java`

*See Data Flow Diagram*

| DU Pair | From → To | Test Case |
|---|---|---|
| caller-def(songs) → use(Node 1 param) | Caller → Node 1 | `tcSH1`, `tcSH2`, `tcSH3` |
| def(mutated order, Node 2) → use(return, Node 3) | Node 2 → Node 3 | `tcSH1`, `tcSH2` |
| caller-def(empty list) → use(Node 1) → use(Node 3, no mutation) | Caller → Node 1 → Node 3 | `tcSH3` |

---

### 3b. Integration-Level Data Flow

*See Data Flow Diagram*

| Cross-Unit DU Pair | Description | Test Case |
|---|---|---|
| INT-DF1 | SongDatabase.def(songs) → Controller.use(getAllSongs()) | `intDF1` |
| INT-DF2 | Controller.def(filteredSongs) → Shuffle.use(playlist param) | `intDF2` |
| INT-DF3 | Shuffle.def(mutated order) → Playlist.use(allSongs param) | `intDF3` |
| INT-DF4 | Playlist.def(this.songs) → Controller.use(getCurrentPlaylist().getSongs()) | `intDF4` |

---

## 4. Boundary Value Testing

**Test file:** `BoundaryValueTest.java` (package `controller`)

**Parameter:** `maxSongs` in `generatePlaylist()`  
**Dataset reference:** `"Pop"` + `explicit=false` → **N = 53** available songs

| BV ID | maxSongs Value | Boundary Type | Expected Result | Test Method |
|---|---|---|---|---|
| BV1 | 0 | Below valid (no-limit zone) | All 53 songs returned | `bv1_maxSongsZero_treatedAsNoLimit` |
| BV2 | 1 | Minimum valid positive | Exactly 1 song | `bv2_maxSongsOne_returnsExactlyOneSong` |
| BV3 | 2 | Just above minimum | Exactly 2 songs | `bv3_maxSongsTwo_returnsExactlyTwoSongs` |
| BV4 | 52 (N−1) | One below maximum | 52 songs | `bv4_maxSongsNMinusOne_returnsTrimmedList` |
| BV5 | 53 (N) | Exactly equal to available | All 53 songs | `bv5_maxSongsEqualsAvailable_returnsAll` |
| BV6 | 54 (N+1) | One above available | All 53 songs (no crash) | `bv6_maxSongsExceedsAvailable_returnsAll` |
| BV7 | −1 | Canonical NO_LIMIT sentinel | All 53 songs | `bv7_maxSongsNegativeOne_canonicalNoLimit` |

---

## 5. Use Case Testing

**Test file:** `UseCaseTest.java` (package `controller`)

Use cases are taken directly from the Curated Playlist Generator use case diagram.

### UC1 — Generate Playlist

| Flow | Inputs | Expected Output | Test Method |
|---|---|---|---|
| Main flow | genre="Pop", explicit=false, max=10 | Non-null playlist, ≤10 non-explicit Pop songs | `uc1_mainFlow_generatePlaylistSuccessfully` |
| Alt — Validate Input fails | genre="Classical" | null, hasPlaylist=false | `uc1_alternativeFlow_invalidGenreReturnsNull` |
| Include — Filter Songs | genre="Pop", explicit=false | No explicit songs in result | `uc1_include_filterSongs_explicitExcluded` |
| Include — Load Songs | Read songs.csv | 206 songs loaded | `uc1_include_loadSongs_databaseLoadedCorrectly` |

### UC2 — View Playlist

| Flow | Precondition | Expected Output | Test Method |
|---|---|---|---|
| Main flow | Playlist generated | Non-null, non-empty playlist returned | `uc2_mainFlow_viewExistingPlaylist` |
| Alternative | No playlist generated | hasPlaylist=false, null returned | `uc2_alternativeFlow_noPlaylistToView` |

> **Demo note:** The CLI `toDisplayString()` output for View Playlist is demonstrated verbally during the project demo walkthrough.

### UC3 — Reset Playlist

| Flow | Precondition | Expected Output | Test Method |
|---|---|---|---|
| Main flow | Playlist exists | Playlist cleared, hasPlaylist=false | `uc3_mainFlow_resetClearsPlaylist` |
| Alternative | No playlist exists | No exception, hasPlaylist remains false | `uc3_alternativeFlow_resetWithNoPlaylist_noCrash` |

### UC4 — Exit Application

Verified manually during demo. Selecting option `0` in the CLI prints "Goodbye!" and exits cleanly. Not unit-testable at the controller layer.

---

## 6. Decision Table Testing  *(Report only — no code required)*

**Component:** `PlaylistController.generatePlaylist()`  
**Conditions:** C1 = Genre validity, C2 = allowExplicit

| TC | C1: Genre | C2: Explicit | A1: Return playlist | A2: Return null |
|---|---|---|---|---|
| DT-1 | Valid (e.g., "Pop") | false | Non-explicit songs only | |
| DT-2 | Valid (e.g., "Pop") | true | All songs incl. explicit | |
| DT-3 | ANY | false | All non-explicit songs | |
| DT-4 | ANY | true | All songs in DB | |
| DT-5 | Invalid (e.g., "Classical") | false | | Yes |
| DT-6 | Invalid (e.g., "Classical") | true | | Yes |

> The decision table confirms 6 distinct input combinations. DT-1 through DT-4 are exercised by `DecisionTableTest.java` (available in test suite). DT-5 and DT-6 are covered by `testNoMatch_returnsNull` in `PlaylistControllerTest.java`.

---

## 7. State Transition Testing  *(Report only — no code required)*

**Component:** `PlaylistController`

### States

| State | Description | Observable Condition |
|---|---|---|
| S0 — NO_PLAYLIST | Initial / after reset | `hasPlaylist()=false`, `getCurrentPlaylist()=null` |
| S1 — HAS_PLAYLIST | Valid playlist generated | `hasPlaylist()=true`, `getCurrentPlaylist()!=null` |
| S2 — NULL_RESULT | generate() called, no matches | `hasPlaylist()=false`, `getCurrentPlaylist()=null` |

### State Transition Table

|  | generate() [matches] | generate() [no match] | reset() |
|---|---|---|---|
| **S0 — No Playlist** | → S1 | → S2 | — (stay S0) |
| **S1 — Has Playlist** | → S1 (overwrite) | → S2 | → S0 |
| **S2 — Null Result** | → S1 | → S2 (stay) | — (stay S0) |

### State Transition Diagram

```
                   generate() [matches]
                  ┌───────────────────────┐
                  │                       ▼
   ┌──────────────┴──┐   generate()   ┌────────────────┐
   │  S0             │──[matches]────▶│  S1            │
   │  NO_PLAYLIST    │                │  HAS_PLAYLIST  │
   └──────────────┬──┘◀───reset()─────└────────────────┘
                  │                       │
         generate()│                      │ generate()
         [no match]│                      │ [no match]
                  ▼                       ▼
           ┌──────────────┐         ┌──────────────┐
           │  S2          │         │  S2          │
           │  NULL_RESULT │         │  NULL_RESULT │
           └──────┬───────┘         └──────────────┘
                  │
         generate() [matches]
                  │
                  ▼
                  S1
```

### State Transition Test Cases

| TC | Start State | Event | End State |
|---|---|---|---|
| ST-1 | S0 | generate("Pop", false, 10) — matches | S1 |
| ST-2 | S1 | reset() | S0 |
| ST-3 | S0 | generate("Classical", false, 10) — no match | S2 |
| ST-4 | S1 | generate("Rock", true, 5) — matches | S1 (overwrite) |
| ST-5 | S1 | generate("Classical", false, 10) — no match | S2 |
| ST-6 | S2 | generate("Jazz", false, 5) — matches | S1 (recover) |

---

## Test Suite Summary

| Technique | File | # Tests | Executable? |
|---|---|---|---|
| Unit — Song | `SongTest.java` | 3 | Yes |
| Unit — SongDatabase | `SongDatabaseTest.java` | 6 | Yes |
| Unit — Shuffle | `ShuffleTest.java` | 3 | Yes |
| Unit — Playlist | `PlaylistTest.java` | 9 | Yes |
| Integration | `PlaylistControllerTest.java` | 9 | Yes |
| Data Flow — Units | `DataFlowTest.java` | 9 | Yes |
| Data Flow — Integration | `DataFlowIntegrationTest.java` | 4 | Yes |
| Boundary Value | `BoundaryValueTest.java` | 7 | Yes |
| Use Case | `UseCaseTest.java` | 8 | Yes |
| Decision Table | TESTING.md (Section 6) | — | 📄 Report only |
| State Transition | TESTING.md (Section 7) | — | 📄 Report only |
| **Total executable** | | **67** | |
