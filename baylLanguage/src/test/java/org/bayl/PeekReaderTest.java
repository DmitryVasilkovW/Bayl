package org.bayl;

import java.io.IOException;
import java.io.StringReader;

import org.junit.*;
import static org.junit.Assert.*;

public class PeekReaderTest {
    private PeekReader in;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        in = new PeekReader(new StringReader("hello"), 3);
    }

    @Test
    public void testPeek() {
        try {
            assertTrue(in.peek(1) == 'h'); // peek: h
            assertTrue(in.peek(2) == 'e'); // peek: e
            assertTrue(in.peek(3) == 'l'); // peek: l

            assertTrue(in.read() == 'h'); // h
            assertTrue(in.peek(1) == 'e'); // peek: e
            assertTrue(in.peek(2) == 'l'); // peek: l
            assertTrue(in.peek(3) == 'l'); // peek: l

            assertTrue(in.read() == 'e');
            assertTrue(in.peek(1) == 'l'); // peek: l
            assertTrue(in.peek(2) == 'l'); // peek: l
            assertTrue(in.peek(3) == 'o'); // peek: o

            assertTrue(in.read() == 'l');
            assertTrue(in.peek(1) == 'l'); // peek: l
            assertTrue(in.peek(2) == 'o'); // peek: o
            assertTrue(in.peek(3) == -1); // peek: -1 (End of stream)

            assertTrue(in.read() == 'l');
            assertTrue(in.read() == 'o');
            assertTrue(in.read() == -1);
        } catch (IOException e) {
            fail();
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void exceedMinPeek() {
        in.peek(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void exceedMaxPeek() {
        in.peek(4);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        in.close();
    }
}
