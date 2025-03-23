# Game Festival Optimization

## Problem Overview

During a board game festival, organizers face the challenge of optimally distributing games on tables and seating players at those tables. The problem arises because:

- Players have different preferences for games they would like to play.
- Games have varying player count requirements.
- Tables have a limited number of available seats.

The goal is to maximize the number of players, their satisfaction with the games, and minimize penalties from misallocations. The optimization problem can be expressed using the following criteria:

### Objective Function:
MAX ( W1 * Number of players + W2 * Total satisfaction of all players + W3 * Penalty )

Where:
- **Satisfaction of a player** (`s`) is calculated as:
s = 1/i
where `i` is the position of the game in the player's preference list.
- **Penalty** is calculated as the number of tables minus the number of games distributed (penalty occurs when multiple games are played at the same table).

### Input Files:

1. **Games Information** (`gry.txt`):
   - Format: `game_id; number_of_copies; min_players; max_players`
   - Example:
     ```
     1; 2; 2; 4
     2; 3; 2; 2
     ```

2. **Tables Information** (`stoliki.txt`):
   - Format: `table_id; number_of_seats`
   - Example:
     ```
     1; 4
     2; 6
     ```

3. **Player Preferences** (`preferencje.txt`):
   - Format: `player_id; list_of_preferred_game_ids`
   - Example:
     ```
     1; 1, 2
     2; 3, 1, 2
     ```

### Weights:
- **W1**, **W2**, **W3** are weights in the objective function, which can be provided either:
  - As command-line arguments during program execution.
  - Read from standard input during program execution.
  - Read from a separate file.

## Goal:
Maximize the number of players, their satisfaction, and minimize the penalty caused by misallocating games to tables, ensuring that the games match the players' preferences and the number of players fits the available slots at the tables.


## Usage

1. Prepare the input files (`gry.txt`, `stoliki.txt`, `preferencje.txt`).
2. Run the program and specify the weights via command-line arguments or input files.
3. The program will prompt you to enter the number of seconds to search for the best solution.
4. The program will output the optimized seating arrangement, game distribution, and the final satisfaction score.

