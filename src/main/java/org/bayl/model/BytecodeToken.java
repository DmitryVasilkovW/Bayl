package org.bayl.model;

public enum BytecodeToken {
    PUSH,
    LOAD,

    BLOCK_START, BLOCK_END,

    FOREACH, ON_VAR, AS, LOOP_BODY,
    WHILE,

    IF, CONDITION, THEN, ELSE,

    ARRAY_INIT, ARRAY_STORE, ARRAY_END,
    DICT_ENTRY, DICT_INIT, DICT_PAIR, DICT_KEY, DICT_VALUE,
    LOOKUP, LOOKUP_VAR, LOOKUP_VALUE, LOOKUP_END,

    CALL, CALL_DYNAMIC, CALL_END, CALL_DYNAMIC_END, ARG,
    FUNC, BODY, RETURN,
    RETURN_START, RETURN_END,

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
