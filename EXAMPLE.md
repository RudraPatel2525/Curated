flowchart TD

    %% ── Top-level ────────────────────────────────────────────────────────
    IT["**Integration Testing**\nPlaylistControllerTest.java\nTests SongDatabase → PlaylistController → Playlist → Shuffle\nusing the real songs.csv file · 9 tests"]

    %% ── SongDatabase ─────────────────────────────────────────────────────
    subgraph DB ["SongDatabase"]
        DB1["**Node 1**\nsongs = new ArrayList&lt;&gt;()\ndef(songs)"]
        DB2["**Node 2**\nLoad songs.csv + songs.add(song)\ndef(songs)"]
        DB3["**Node 3**\ngetAllSongs()\nuse(songs)"]
        DB1 --> DB2 --> DB3
    end

    %% ── PlaylistController ───────────────────────────────────────────────
    subgraph PC ["PlaylistController"]
        PC1["**Node 4**\ngeneratePlaylist(genre, allowExplicit, maxSongs)\nuse(getAllSongs()) — receives song list from DB"]
        PC2["**Node 5**\nFilter by genre\ndef(filteredSongs) — genre match applied"]
        PC3["**Node 6**\nFilter by explicit flag\ndef(filteredSongs) — explicit filter applied"]
        PC4["**Node 7**\nApply maxSongs cap\ndef(filteredSongs) — list trimmed if needed"]
        PC5["**Node 8**\nNo matches → return null\nhasPlaylist = false"]
        PC6["**Node 9**\nPass filteredSongs to Shuffle\ndef(filteredSongs) — sent to shuffle"]
        PC1 --> PC2 --> PC3 --> PC4
        PC4 -->|"no matches"| PC5
        PC4 -->|"matches found"| PC6
    end

    %% ── Shuffle ──────────────────────────────────────────────────────────
    subgraph SH ["Shuffle"]
        SH1["**Node 10**\nshuffle(playlist)\nuse(playlist) — receives filteredSongs"]
        SH2["**Node 11**\nCollections.shuffle(playlist, random)\ndef(playlist order) — mutated in-place"]
        SH3["**Node 12**\nreturn playlist\nuse(playlist) — shuffled list returned"]
        SH1 --> SH2 --> SH3
    end

    %% ── Playlist ─────────────────────────────────────────────────────────
    subgraph PL ["Playlist"]
        PL1["**Node 13**\nPlaylist(name, allSongs) constructor\nuse(allSongs), def(this.songs) via add loop"]
        PL2["**Node 14**\ngetSongs()\nuse(this.songs) — returned to controller"]
        PL1 --> PL2
    end

    %% ── Back to controller ───────────────────────────────────────────────
    PC7["**Node 15**\ngetCurrentPlaylist().getSongs()\nuse(playlist) — final result accessible\nhasPlaylist = true"]

    %% ── Cross-unit data flow edges ───────────────────────────────────────
    IT --> DB1

    DB3 -->|"INT-DF1: songs passed to controller"| PC1
    PC6 -->|"INT-DF2: filteredSongs passed to shuffle"| SH1
    SH3 -->|"INT-DF3: shuffled list passed to Playlist"| PL1
    PL2 -->|"INT-DF4: getSongs() returned to controller"| PC7

    %% ── Test cases ───────────────────────────────────────────────────────
    T1["**testInitialState_noPlaylistExists**\nController starts with no playlist\nhasPlaylist=false, getCurrentPlaylist()=null"]
    T2["**testGeneratePlaylist_returnsPlaylist_whenMatchesExist**\nValid generate returns a non-null result"]
    T3["**testGeneratePlaylist_filtersByGenre**\nGenre filter applied correctly"]
    T4["**testExplicitNotAllowed_filtersExplicitSongs**\nExplicit songs excluded when allowExplicit=false"]
    T5["**testExplicitAllowed_includesExplicitSongs**\nExplicit songs included when allowExplicit=true"]
    T6["**testMaxSongs_limitRespected**\nPlaylist size capped at maxSongs"]
    T7["**testNoMatch_returnsNull**\nNo-match case returns null"]
    T8["**testReset_clearsPlaylist**\nreset() clears state, hasPlaylist=false"]
    T9["**testGenerateTwice_overwritesPrevious**\nSecond generate() overwrites first playlist"]

    PC7 --> T2
    PC7 --> T3
    PC7 --> T4
    PC7 --> T5
    PC7 --> T6
    PC5 --> T7
    PC1 --> T1
    PC1 --> T8
    PC1 --> T9
