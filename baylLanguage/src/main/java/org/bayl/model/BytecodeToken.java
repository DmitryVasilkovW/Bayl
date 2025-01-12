package org.bayl.model;

public enum BytecodeToken {
    PUSH_S,
    PUSH_N,
    PUSH_T,
    PUSH_F,
    LOAD,

    BLOCK_START, BLOCK_END,

    FOREACH, ON_VAR, AS, LOOP_BODY,
    WHILE,

    IF, CONDITION, THEN, ELSE,

    ARRAY_INIT, ARRAY_STORE, ARRAY_END,
    DICT_INIT, DICT_PAIR, DICT_END,
    LOOKUP,

    CALL, CALL_DYNAMIC, CALL_END, CALL_DYNAMIC_END, ARG,
    FUNC, BODY,
    RETURN,

    MOD,
    SET,
    POWER,
    ADD,
    DIVIDE,
    MULTIPLY,
    SUBTRACT,
    CONCAT,

    NEGATE,
    NOT,

    EQUALS,
    GREATER_THAN,
    LESS_THAN,
    NOT_EQUALS,
    GREATER_EQUAL,
    LESS_EQUAL,

    AND,
    OR
}
