package org.bayl.memory;

import java.util.List;
import org.bayl.memory.gc.GarbageCollector;
import org.bayl.runtime.object.BaylRef;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.runtime.object.value.BaylNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BaylMemoryTest {

    private final BaylMemory memory = new BaylMemory();

    @Test
    void testHeapFillPercentage() {
        BaylRef ref1 = new BaylRef("ref1");
        BaylRef ref2 = new BaylRef("ref2");
        BaylRef ref3 = new BaylRef("ref3");
        BaylRef ref4 = new BaylRef("ref4");
        BaylRef ref5 = new BaylRef("ref5");
        BaylRef ref6 = new BaylRef("ref6");
        BaylRef ref7 = new BaylRef("ref7");

        memory.getHeap().put(ref1, new BaylArray(List.of()));
        memory.getHeap().put(ref2, new BaylArray(List.of()));
        memory.getHeap().put(ref3, new BaylArray(List.of()));
        memory.getHeap().put(ref4, new BaylArray(List.of()));
        memory.getHeap().put(ref5, new BaylArray(List.of()));
        memory.getHeap().put(ref6, new BaylArray(List.of()));
        memory.getHeap().put(ref7, new BaylArray(List.of()));

        memory.setVariable("a", new BaylNull());
        memory.setVariable("b", new BaylNull());
        memory.setVariable("c", new BaylNull());

        assertTrue(memory.getHeapFillPercentage() >= 10);
    }

    @Test
    void testFreeMemory() {
        BaylRef ref1 = new BaylRef("ref1");
        BaylRef ref2 = new BaylRef("ref2");
        BaylRef ref3 = new BaylRef("ref3");
        BaylRef ref4 = new BaylRef("ref4");
        BaylRef ref5 = new BaylRef("ref5");
        BaylRef ref6 = new BaylRef("ref6");
        BaylRef ref7 = new BaylRef("ref7");

        memory.getHeap().put(ref1, new BaylArray(List.of()));
        memory.getHeap().put(ref2, new BaylArray(List.of()));
        memory.getHeap().put(ref3, new BaylArray(List.of()));
        memory.getHeap().put(ref4, new BaylArray(List.of()));
        memory.getHeap().put(ref5, new BaylArray(List.of()));
        memory.getHeap().put(ref6, new BaylArray(List.of()));
        memory.getHeap().put(ref7, new BaylArray(List.of()));

        memory.setVariable("a", new BaylNull());
        memory.setVariable("b", new BaylNull());
        memory.setVariable("c", new BaylNull());

        new GarbageCollector().freeMemory(memory);

        assertFalse(memory.getHeap().containsKey(ref1));
        assertFalse(memory.getHeap().containsKey(ref2));
        assertFalse(memory.getHeap().containsKey(ref3));
        assertFalse(memory.getHeap().containsKey(ref4));
        assertFalse(memory.getHeap().containsKey(ref5));
        assertFalse(memory.getHeap().containsKey(ref6));
        assertFalse(memory.getHeap().containsKey(ref7));
    }
}
