package org.bayl;

import org.bayl.syntax.token.util.PeekReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PeekReaderTest {

    private PeekReader in;

    @BeforeEach
    public void setUp() throws Exception {
        in = new PeekReader(new StringReader("hello"), 3);
    }

    @Test
    public void testPeek() {
        try {
            assertTrue(in.peek(1) == 'h');
            assertTrue(in.peek(2) == 'e');
            assertTrue(in.peek(3) == 'l');

            assertTrue(in.read() == 'h');
            assertTrue(in.peek(1) == 'e');
            assertTrue(in.peek(2) == 'l');
            assertTrue(in.peek(3) == 'l');

            assertTrue(in.read() == 'e');
            assertTrue(in.peek(1) == 'l');
            assertTrue(in.peek(2) == 'l');
            assertTrue(in.peek(3) == 'o');

            assertTrue(in.read() == 'l');
            assertTrue(in.peek(1) == 'l');
            assertTrue(in.peek(2) == 'o');
            assertTrue(in.peek(3) == -1);

            assertTrue(in.read() == 'l');
            assertTrue(in.read() == 'o');
            assertTrue(in.read() == -1);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void exceedMinPeek() {
        assertThrows(IndexOutOfBoundsException.class, () -> in.peek(0));
    }

    @Test
    public void exceedMaxPeek() {
        assertThrows(IndexOutOfBoundsException.class, () -> in.peek(4));
    }

    @AfterEach
    public void tearDown() throws Exception {
        in.close();
    }
}
