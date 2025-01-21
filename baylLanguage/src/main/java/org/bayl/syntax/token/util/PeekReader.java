package org.bayl.syntax.token.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class PeekReader {
    private final Reader in;
    private final CharBuffer peakBuffer;

    public PeekReader(Reader in, int peekLimit) throws IOException {
        if (!in.markSupported()) {
            in = new BufferedReader(in);
        }
        this.in = in;
        peakBuffer = CharBuffer.allocate(peekLimit);
        fillPeekBuffer();
    }

    public void close() throws IOException {
        in.close();
    }

    private void fillPeekBuffer() throws IOException {
        peakBuffer.clear();
        in.mark(peakBuffer.capacity());
        in.read(peakBuffer);
        in.reset();
        peakBuffer.flip();
    }

    public int read() throws IOException {
        int c = in.read();
        fillPeekBuffer();
        return c;
    }

    public int peek(int lookAhead) {
        if (lookAhead < 1 || lookAhead > peakBuffer.capacity()) {
            throw new IndexOutOfBoundsException("lookAhead must be between 1 and " + peakBuffer.capacity());
        }
        if (lookAhead > peakBuffer.limit()) {
            return -1;
        }
        return peakBuffer.get(lookAhead - 1);
    }
}
