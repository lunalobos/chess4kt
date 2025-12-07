package io.github.lunalobos.chess4kt

internal enum class TokenType {
    // Delimiters (Self-terminating)
    LBRACKET,   // [
    RBRACKET,   // ]
    LPAREN,     // ( (RAV begin)
    RPAREN,     // ) (RAV end)
    PERIOD,     // . (Move number indication separator)

    // Annotations & Markers
    NAG,        // Numeric Annotation Glyph ($dd)
    STAR,       // * (Game termination marker)

    // Keywords/Results (Symbols) [cite: 195]
    RESULT,     // "1-0", "0-1", "1/2-1/2"

    // Core Data Types [cite: 101, 121]
    STRING,     // "..." (Used for Tag Values)
    SYMBOL,     // identifier (Used for Tag Names, SAN moves, and custom symbols)
    INTEGER,    // 1, 2, 3... (Used for move number indication) [cite: 106]
    COMMENT, // used for regurlar comments
    END_LINE_COMMENT, // used for end line comments

    // Special Markers
    EOF // end of file
}