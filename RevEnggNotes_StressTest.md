# Notes from Reverse Engineering StreesTest.java:

1. Player IDs are assumed unique, i.e. the stress test creates unique IDs. We do not need to handle conflicting IDs.
2.Two different types of moves: fast and slow. Fast moves are automated, and slow ones are manually aided.
3. Player addition to the game must be quick and robust.
4. Maze Size = 10 X 10, Treasures = 10 (to be defined before  starting the tracker)
5. No. of players = 5
6. Time difference between player crash = 3 seconds (as used in StressTest. The requirements list it as 2 seconds though)
7. There is no player exit scenario. There is only player crash scenario. We can treat player exit and crash similarly.
