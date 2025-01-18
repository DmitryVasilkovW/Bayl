package org.bayl.model;

public enum TokenType {
    COMMENT, ASSIGN,
    PLUS, MINUS, MULTIPLY, DIVIDE, MOD, POWER,
    LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET, COMMA, COLON, END_STATEMENT,
    CONCAT,
    NOT, AND, OR,
    LESS_THEN, LESS_EQUAL, EQUAL, GREATER_EQUAL, GREATER_THEN, NOT_EQUAL,
    NUMBER, STRING_LITERAL, TRUE, FALSE,
    IF, ELSE, WHILE, FOR_EACH, AS,
    VARIABLE, FUNCTION, RETURN,
    CLASS, CLASS_CALL
}
