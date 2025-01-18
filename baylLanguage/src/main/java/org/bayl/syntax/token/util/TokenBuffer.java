package org.bayl.syntax.token.util;

import java.util.LinkedList;
import org.bayl.model.Token;
import org.bayl.model.TokenType;
import org.bayl.syntax.Lexer;

public class TokenBuffer {
    private final LinkedList<Token> tokenQueue;
    private final Lexer lexer;

    public TokenBuffer(Lexer lexer, int size) {
        this.lexer = lexer;
        tokenQueue = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            Token token = nextToken();
            if (token == null) {
                break;
            }
            tokenQueue.addLast(token);
        }
    }

    private Token nextToken() {
        Token token = lexer.getNextToken();
        while (token != null && token.getType() == TokenType.COMMENT) {
            token = lexer.getNextToken();
        }
        return token;
    }

    public boolean isEmpty() {
        return tokenQueue.isEmpty();
    }

    public int size() {
        return tokenQueue.size();
    }

    public Token getToken(int i) {
        return tokenQueue.get(i);
    }

    public Token readToken() {
        if (tokenQueue.isEmpty()) {
            return null;
        }
        Token token = tokenQueue.removeFirst();

        Token newToken = nextToken();
        if (newToken != null) {
            tokenQueue.addLast(newToken);
        }
        return token;
    }
}
