package org.bayl.model;

import lombok.Getter;

@Getter
public class Token {
    private final SourcePosition position;
    private final TokenType type;
    private final String text;

    public Token(SourcePosition position, TokenType type, String text) {
        this.position = position;
        this.type = type;
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Token))
            return false;
        Token other = (Token) obj;
        return this.type == other.type && this.text.equals(other.text) && this.position.equals(other.position);
    }

    @Override
    public String toString() {
        return type + ",'" + text + "'";
    }
}
