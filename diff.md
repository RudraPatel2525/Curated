# Spec Drift Notes

This file records differences between the current codebase, `README.md`, and the `TESTING.md` draft shared for submission planning. `README.md` remains unchanged in this pass by design.

## README.md vs Actual Codebase

- `README.md` referenced `PlaylistTest.java` before it existed in the repo. This implementation pass adds `src/test/java/model/PlaylistTest.java`, so the codebase now matches that part of the README.
- `README.md` mentions `FilterEngine.java` and `FilterEngineTest.java`, but neither file exists in the project. Current truth: filtering is implemented primarily in `src/main/java/controller/PlaylistController.java`, with `src/main/java/model/Playlist.java` still enforcing genre, explicit, and max-song rules on construction.
- `README.md` refers to the dataset as `songs.txt`, but the real dataset file is `src/main/resources/songs.csv`. Current truth: `songs.csv`.
- `README.md` describes `Playlist.java` as the component that applies filtering logic. Current truth: filtering starts in `PlaylistController.generatePlaylist(...)`, then `Playlist` copies and enforces the same rules again when the playlist object is built.
- `README.md` refers to `CLIView.java`, while the actual source file is `src/main/java/view/CLIview.java`. Current truth: `CLIview.java`.

## TESTING.md Draft vs Actual Codebase

- The `TESTING.md` draft describes a detailed executable test plan, but no `TESTING.md` file is currently committed in the repository. Current truth: the draft exists outside the repo for now.
- Before this implementation pass, the repo had 21 executable tests across `SongTest`, `SongDatabaseTest`, `ShuffleTest`, and `PlaylistControllerTest`.
- The `TESTING.md` draft planned 49 executable tests:
  - Unit: 12
  - Integration: 9
  - Data Flow (unit): 9
  - Data Flow (integration): 4
  - Boundary Value: 7
  - Use Case: 8
- This implementation pass adds:
  - `PlaylistTest.java`
  - `DataFlowTest.java`
  - `DataFlowIntegrationTest.java`
  - `BoundaryValueTest.java`
  - `UseCaseTest.java`
- Because `PlaylistTest.java` is now included in addition to the draft plan, the executable test count after this pass is expected to be higher than the 49 tests described in the current `TESTING.md` draft.
- The chosen submission approach keeps Decision Table and State Transition testing as report-only artifacts. Current truth: no `DecisionTableTest.java` will be added in this pass.

## README.md vs TESTING.md Draft

- `README.md` includes `PlaylistTest.java` in its testing discussion, but the shared `TESTING.md` draft does not currently list `PlaylistTest.java` in its suite summary.
- `README.md` still reflects an older architecture that mentions `FilterEngine`, while the `TESTING.md` draft centers testing around `PlaylistController.generatePlaylist(String genre, boolean allowExplicit, int maxSongs)`.
- `README.md` uses older wording for data source and component responsibilities, while the `TESTING.md` draft is more precise about current controller behavior and dataset assumptions such as `genre="Pop"` with `allowExplicit=false` returning 53 songs.
- `README.md` discusses view and integration coverage more loosely, while the `TESTING.md` draft defines exact test files and counts.

## Source of Truth for This Pass

- Architecture truth: current codebase
- Dataset truth: `src/main/resources/songs.csv`
- Controller behavior truth: `PlaylistController.generatePlaylist(...)`
- Detailed testing target: the shared `TESTING.md` draft, except Decision Table and State Transition remain report-only
- Documentation drift log: this `diff.md`
