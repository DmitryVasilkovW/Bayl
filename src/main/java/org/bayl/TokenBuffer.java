package org.bayl;

import java.util.LinkedList;

public class TokenBuffer {
    private LinkedList<Token> tokenQueue;
    private Lexer lexer;

    public TokenBuffer(Lexer lexer, int size) {
        this.lexer = lexer;
        tokenQueue = new LinkedList<Token>();

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
