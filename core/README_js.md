[![npm](https://img.shields.io/npm/v/chess4js?logo=npm)](https://www.npmjs.com/package/chess4js)
![License](https://img.shields.io/github/license/lunalobos/chess4kt)

# Chess4js

# OVERVIEW

**Chess4js** is a JavaScript transpilation of **Chess4kt**, my Kotlin Multiplatform chess library.

This library offers nearly all the features of its Kotlin counterpart. It is currently in **beta**; while fully
functional, initialization performance is still being optimized. At present, initialization takes approximately a few
hundred milliseconds on modern machines.

---

# Classes

### Square

`Square` is a non-instantiable class representing a square on the board. It contains two main properties:

| Property | Type   | Description                                            |
|----------|--------|--------------------------------------------------------|
| ordinal  | number | Internal numerical representation used by the library. |
| name     | string | The algebraic name of the square (e.g., "A1").         |

The library provides predefined instances for every square (A1, B1, ..., H8). You can import them directly:

```js
import { A1, G4, D6 } from "chess4js";
```

## Piece

`Piece` is a non-instantiable class representing a chess piece.

| Property | Type   | Description                                            |
|----------|--------|--------------------------------------------------------|
| ordinal  | number | Internal numerical representation used by the library. |
| name     | string | The name of the piece.                                 |

Predefined instances are available for all pieces:

- White: WP, WN, WB, WR , WQ, WK.
- Black: BP, BN, BB, BR, BQ, BK.

You can import them directly:

```js
import {WP, BK, WQ, BN} from "chess4js" 
```

## Side

`Side` is a non-instantiable class representing the players. It has a `name` property (string). Only two instances
exist: `WHITE` and `BLACK`. You can import them directly:

```js
import { WHITE, BLACK } from "chess4js" 
```

## Bitboard

A non-directly instantiable class representing a 64-bit bitboard. Instances are immutable.

### Methods

| Method        | Arguments       | Return Type             | Description                                                                                 |
|---------------|-----------------|-------------------------|---------------------------------------------------------------------------------------------|
| peekLastBit   | None            | Bitboard                | Returns a bitboard containing only the Least Significant Bit (LSB).                         |
| peekFirstBit  | None            | Bitboard                | Returns a bitboard containing only the Most Significant Bit (MSB).                          |          
| trailingZeros | None            | number                  | Returns the number of zero bits following the LSB.                                          |
| leadingZeros  | None            | number                  | Returns the number of zero bits preceding the MSB.                                          |
| and           | other: Bitboard | Bitboard                | Performs a bitwise AND operation.                                                           |
| or            | other: Bitboard | Bitboard                | Performs a bitwise OR operation.                                                            |
| xor           | other: Bitboard | Bitboard                | Performs a bitwise XOR operation.                                                           |
| inv           | None            | Bitboard                | Performs a bitwise NOT operation (inverts all bits).                                        |
| shl           | i: number       | Bitboard                | Returns a bitboard with bits shifted left by i positions.                                   |
| ushr          | i: number       | Bitboard                | Returns a bitboard with bits shifted right (unsigned) by `i` positions.                     |
| toString      | none            | string                  | Returns a formatted string representation.                                                  |
| toArray       | none            | ReadonlyArray\<number\> | Returns an array containing the indices of all set bits (bits equal to 1) in this bitboard. | 

Instances of this class can be obtained from `Position` instances.

## Move

A non directly instantiable class that represents a move made on the board.

### Properties

| Property       | Type   | Description                                                             |
|----------------|--------|-------------------------------------------------------------------------|
| origin         | number | The zero-based index (0-63) of the move's origin square.                |
| target         | number | The zero-based index (0-63) of the move's target square.                |
| promotionPiece | number | The ordinal value of the promotion piece, or -1 if no promotion occurs. |

### Methods

| Method   | Arguments | Return Type | Description                             |
|----------|-----------|-------------|-----------------------------------------|
| toString | None      | string      | Returns the UCI notation for this move. |

### Factories

| Function | Arguments    | Return Type | Description                                                           |
|----------|--------------|-------------|-----------------------------------------------------------------------|
| moveOf   | move: String | Move        | Creates a `Move` object from its UCI notation string (e.g., "e7e8q"). |

### Example

```js
import { moveOf } from "chess4js"

const myMove = moveOf("e2e4");
```

## Position

Represents a specific board state. Instances of this class are immutable. Modifying properties directly will lead to
inconsistent behavior. Use factory methods to generate new positions.

```js
import { startpos, positionOf } from "chess4js"

const initialPosition = startpos(); 
const somePosition = positionOf("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

```

### Properties

| Property             | Type                                     | Description                                                                                                                                                                                                                                                                                                                                                                                                                                  |
|----------------------|------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| bitboards            | ReadonlyArray\<Bitboard\>                | The index in the array is equal to the ordinal value minus one of the corresponding piece (e.g., Black Knight distribution is at index `BN.ordinal - 1`). Each Bitboard has 64 bits, where each bit represents a square in the board, following the Square ordinal order (A1 is bit 0, B1 is bit 1, up to H8 which is bit 63). A bit set to 1 indicates the presence of the piece corresponding to the array index, and 0 indicates absence. |
| whiteMove            | boolean                                  | True if it is White's turn to move. False if it is Black's turn.                                                                                                                                                                                                                                                                                                                                                                             |
| enPassant            | number                                   | The square index (0-63) where a pawn can be captured en passant. Returns -1 if no en passant capture is possible.                                                                                                                                                                                                                                                                                                                            |
| whiteCastleKingside  | boolean                                  | True if White can castle kingside (short castling). False otherwise.                                                                                                                                                                                                                                                                                                                                                                         |
| whiteCastleQueenside | boolean                                  | True if White can castle queenside (long castling). False otherwise.                                                                                                                                                                                                                                                                                                                                                                         |
| blackCastleKingside  | boolean                                  | True if Black can castle kingside (short castling). False otherwise.                                                                                                                                                                                                                                                                                                                                                                         |
| blackCastleQueenside | boolean                                  | True if Black can castle queenside (long castling). False otherwise.                                                                                                                                                                                                                                                                                                                                                                         |
| movesCounter         | number                                   | The full move number in the game (starts at 1 and is incremented after Black's move).                                                                                                                                                                                                                                                                                                                                                        |
| halfMovesCounter     | number                                   | The number of half-moves since the last pawn move or capture. This is used for the 50-move rule.                                                                                                                                                                                                                                                                                                                                             |
| check                | boolean                                  | True if the current side to move is in check. False otherwise.                                                                                                                                                                                                                                                                                                                                                                               |
| checkmate            | boolean                                  | True if the position is a checkmate (the current side is in check and has no legal moves). False otherwise.                                                                                                                                                                                                                                                                                                                                  |
| stalemate            | boolean                                  | True if the position is a stalemate (the current side is not in check but has no legal moves). False otherwise.                                                                                                                                                                                                                                                                                                                              |
| lackOfMaterial       | boolean                                  | True if the position is a draw due to insufficient mating material (e.g., King vs. King). False otherwise.                                                                                                                                                                                                                                                                                                                                   |
| fiftyMoves           | boolean                                  | True if the position has reached or exceeded 50 half-moves without a pawn move or capture.                                                                                                                                                                                                                                                                                                                                                   |
| zobrist              | Bitboard                                 | The Zobrist hash key of the position. This is used for efficient position lookup and repetition detection.                                                                                                                                                                                                                                                                                                                                   |
| squares              | Int32Array                               | A 64-element array where the index corresponds to the square order defined in Square (A1=0, H8=63). The value of each element is the ordinal of the Piece occupying that square (0 for EMPTY, 1 for WP, and so on).                                                                                                                                                                                                                          |
| fen                  | string                                   | The FEN (Forsyth-Edwards Notation) string representation of the position.                                                                                                                                                                                                                                                                                                                                                                    |
| children             | ReadonlyArray\<Tuple\<Position, Move\>\> | A list of tuples, where each tuple represents a legal move from this position and the resulting new position (Tuple<Position, Move>). This list effectively defines the legal branches of the game tree from the current position.                                                                                                                                                                                                           |
| draw                 | boolean                                  | True if the position is a forced draw. This is true if the position results in a stalemate or lackOfMaterial. False otherwise.                                                                                                                                                                                                                                                                                                               |
| enPassantSquare      | Nullable\<Square\>                       | The square exposed to an en passant capture, if one exists. Returns null if no en passant capture is possible in the current position.                                                                                                                                                                                                                                                                                                       |
| gameOver             | boolean                                  | True if the game state is concluded (terminal position), either due to a forced draw or checkmate. False otherwise.                                                                                                                                                                                                                                                                                                                          |
| sideToMove           | Side                                     | The side (WHITE or BLACK) whose turn it is to move.                                                                                                                                                                                                                                                                                                                                                                                          |

### Methods

| Method               | Arguments                              | Return Type | Description                                                                                                                                                                                            |
|----------------------|----------------------------------------|-------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| whiteLacksOfMaterial | None                                   | boolean     | Determines if White has insufficient material to win the game. Returns true if the current pieces for White cannot potentially lead to a checkmate.                                                    |
| blackLacksOfMaterial | None                                   | boolean     | Determines if Black has insufficient material to win the game. Returns true if the current pieces for Black cannot potentially lead to a checkmate.                                                    |
| pieceAt              | square: Square                         | Piece       | Retrieves the piece object that occupies the given square.                                                                                                                                             |
| isLegal              | move: Move                             | boolean     | Returns true if the evaluated Move is legal in the current position, and false otherwise.                                                                                                              |
| move                 | move: Move                             | Position    | Retrieves the new Position that results from executing the provided legal Move. Throws a MoveException if the provided move is not legal in the current position.                                      |
| moveFromString       | move: String, notation: Notation = UCI | Position    | Retrieves the new Position that results from executing the move specified in the given notation. Throws a MoveException if the move is not legal. If no notation is provided, UCI notation is assumed. |
| toString             | None                                   | string      | retrieves a nice string representation                                                                                                                                                                 |

### Factories

| Function   | Arguments   | Return Type | Description                                                                                                                                         |
|------------|-------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| startpos   | None        | Position    | Returns the standard starting `Position` (the startpos FEN).                                                                                        |
| positionOf | fen: String | Position    | Factory function to create a `Position` object from a FEN string. Throws an exception if the FEN string is invalid or leads to an illegal position. |

## Tuple

A utility class designed to group a `Position` and a `Move` together.

### Properties

| Property | Type     | Description  |
|----------|----------|--------------|
| position | Position | The position |
| move     | Move     | The move     |

## Notation

This non-instantiable class represents the types of move notation supported by this library.
The library provides two predefined instances: `UCI` (long algebraic notation used in the UCI protocol) and `SAN` (
standard
algebraic notation).

### Properties

| Property | Type   | Description       |
|----------|--------|-------------------|
| name     | String | The notation name |

## Game and Node

The `Game` class represents a match or an analysis game.
It is basically a tree of `Node` objects with some other properties like tags.
If an instance is used to represent a documented match then its structure becomes like a linked list.

### Properties

For the `Game` class:

| Property                | Type                | Description                                                                                                                                                                                          |
|-------------------------|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| root                    | Node                | The root node of the game tree. The setter is protected if the game is immutable.                                                                                                                    |
| ecoInfo                 | Nullable\<EcoInfo\> | Stores the current ECO (Encyclopedia of Chess Openings) information for the main line. Setting this property will be ignored if the game is immutable (i.e., MATCH and result is set).               |
| id                      | Nullable\<any\>     | A developer-provided unique identifier for serialization or tracking purposes (e.g., UUID or String).                                                                                                |
| result                  | Nullable\<string\>  | The final result of the game. Setting this property will update the "Result" tag in tags and set the game as immutable if MATCH is used. The values can only be set to WHITE_WIN, BLACK_WIN or DRAW. |
| fiftyMoves              | boolean             | Indicates whether the 50-move half-move limit has been reached (non-terminal).                                                                                                                       |
| finalComment            | Nullable\<string\>  | An optional comment placed immediately after the game's result tag in PGN.                                                                                                                           |
| fiftyMovesRuleMode      | string              | Determines how the fifty-move rule is enforced for this game instance. The values can only be set to IGNORE, STRICT or AWARE.                                                                        |
| finalEndLineComment     | Nullable\<string\>  | An optional end-of-line comment placed immediately after the game's result tag in PGN.                                                                                                               |
| fiveRepetitions         | boolean             | Indicates whether a position has been repeated five times, leading to an automatic draw according to FIDE rules (terminal).                                                                          |
| seventyFiveMoves        | boolean             | Indicates whether the 75-move half-move limit has been reached, leading to an automatic draw (terminal).                                                                                             |
| threeRepetitionsMode    | string              | Determines how the three-fold repetition rule is enforced for this game instance. The values can only be set to IGNORE, STRICT or AWARE.                                                             |
| threeRepetitionsWarning | boolean             | Indicates that a three-fold repetition draw can be claimed, as the repetition is impending (e.g., the current move will complete the third repetition).                                              |
| tags                    | any                 | A read-only object with keys as PGN tags names in lowercase (e.g., Event, Site, Date, Round, White, Black, Result). and values as PGN tags values                                                    |

For the `Node` class:

| Property          | Type                                | Description                                                                                                  |
|-------------------|-------------------------------------|--------------------------------------------------------------------------------------------------------------|
| position          | Position                            | The position of the node, which is the result after executing the move.                                      |
| move              | Nullable\<Move\>                    | The move of the node. It is null when it is the root node, which only has the starting position of the game. |
| children          | ReadonlyArray\<Node\>               | The children of the node. The first child corresponds to the main line. The rest are variations (RAVs).      |
| initialComment    | Nullable\<string\>                  | The comment that precedes the move number in PGN (e.g., "{Comment} 1. e4").                                  |
| comment           | Nullable\<string\>                  | The regular comment that follows the move and any suffix annotations (e.g., 1. e4 {Comment}).                |
| endLineComment    | Nullable\<string\>                  | The end-of-line comment for the node, which follows a semicolon ; and goes until the end of the line.        |
| suffixAnnotations | Nullable\<ReadonlyArray\<number\>\> | The list of suffix annotations (NAGs) for the node.                                                          |
| parent            | Nullable\<Node\>                    | The parent node. It can only be null when it is the root node, which evidently cannot have a parent.         |

### Methods

For the class `Game`

| Method              | Arguments                          | Return type      | Description                                                                                                                                                             |
|---------------------|------------------------------------|------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| setTag              | name: String, value: String        | undefined        | Sets a tag pair (name and value).                                                                                                                                       |
| getTag              | name: String                       | Nullable<String> | Retrieves the tag's value.                                                                                                                                              |
| toString            | None                               | String           | Returns the game in pgn format.                                                                                                                                         |
| toAnalysis          | idSupplier?: () => Nullable\<any\> | Game             | Creates a deep copy of this game, converting its mode to ANALYSIS and setting both repetition rules to AWARE. This makes the new instance fully mutable for analysis.   |
| deleteFromExclusive | node: Node                         | Node             | Deletes all moves (the main line continuation and any variations) that follow the provided node. The node provided remains in the game.                                 |
| deleteFromInclusive | node: Node                         | Node             | Deletes all moves (the main line continuation and any variations) that follow the provided node. The move represented by the node is effectively removed from the game. | 
| deleteBefore        | node: Node                         | Node             | Deletes all moves that preceded the provided node in the main line. The node (and its position) becomes the new effective start of the game, creating a new root Node.  |

For the class `Node`

| Method            | Arguments                                                                                                                                                                                          | Return type | Description                                                                                                                                                                                                  |
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| appendMove        | move: string, initialComment?: Nullable\<string\>, comment?: Nullable\<string\>, endLineComment?: Nullable\<string\>, suffixAnnotations?: Nullable\<ReadonlyArray\<number\>\>, notation?: Notation | Node        | Appends a move and returns the added node. If the node already has a child, the added move will be a variation (RAV). Returns the new node if the move is legal, or the current node if the move is illegal. |
| promoteChild      | index: number                                                                                                                                                                                      | boolean     | Promotes the child at the given index to the primary variation (children[0]).                                                                                                                                |
| promoteNode       | None                                                                                                                                                                                               | boolean     | Promotes this node to the main line.                                                                                                                                                                         |
| removeChild       | node: Node                                                                                                                                                                                         | boolean     | Removes the specified child node (variation) from the current node's list of children.                                                                                                                       |
| hasChildren       | None                                                                                                                                                                                               | boolean     | Checks if the node has children.                                                                                                                                                                             |
| belongsToMainLine | None                                                                                                                                                                                               | boolean     | Indicates whether this node belongs to the main line (i.e., it is the first child of all its ancestors).                                                                                                     |
| copy              | parent: Nullable\<Node\>                                                                                                                                                                           | Node        | Creates a deep copy of this node and its entire subtree, assigning the specified parent to the new copy. This process is recursive; copying the root node copies the entire game tree.                       |

### Factories and PGN parsing

| Function     | Arguments                                                                                                                                       | Return type           | Description                                                                                                                                                       |
|--------------|-------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| strictMatch  | idSupplier?: () => Nullable\<any\>                                                                                                              | Game                  | Creates a new Game instance configured for strict competitive match play. Uses MATCH with strict enforcement for the three-fold repetition and 50-move rules.     |
| analysisGame | idSupplier?: () => Nullable\<any\>                                                                                                              | Game                  | Creates a new Game instance configured for analysis. Uses ANALYSIS with AWARE, making the game fully mutable.                                                     |
| customGame   | gameMode: string, threeRepetitionsMode: string, fiftyMovesRuleMode: string, initialFen?: Nullable\<string\>, idSupplier?: () => Nullable\<any\> | Game                  | Creates a new Game instance with fully customizable parameters. Allows setting the game mode, rule enforcement, initial FEN, and PGN tags.                        |
| parseGames   | pgnInput: string, idSupplier?: () => Nullable\<any\>                                                                                            | ReadonlyArray\<Game\> | Parses a string containing one or more games in Portable Game Notation (PGN) format. Games are returned in ANALYSIS mode, making them mutable for subsequent use. |

### Example

```js
import { strictMatch } from "chess4js";

const myGame = strictMatch(() => "someId");
myGame.setTag("white", "foo")
myGame.setTag("black", "bar")
myGame.setTag("event", "foobared event")
myGame.setTag("site", "foobared place")
myGame.setTag("date", "1999.06.10")

myGame.root.appendMove("e4")
           .appendMove("e5")
           .appendMove("Bc4")
           .appendMove("Nc6")
           .appendMove("Qh5")
           .appendMove("Nf6")
           .appendMove("Qxf7#")

```

## EcoInfo

A non-instantiable class that provides basic information about ECO classification.

### Properties

| Property | Type   | Description                                                                              |
|----------|--------|------------------------------------------------------------------------------------------|
| name     | string | The name of the opening or variation (e.g., "Nimzo-Indian, 4.e3 O-O 5.Bd3 d5 6.Nf3 c5"). |
| eco      | string | The ECO code (e.g., "E57", "B40").                                                       |

