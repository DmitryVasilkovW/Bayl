package org.bayl.memory.gc;

import java.util.ArrayList;
import java.util.List;
import org.bayl.memory.BaylMemory;
import org.bayl.runtime.object.BaylRef;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.runtime.object.value.BaylNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class GarbageCollectorTest {

    private final GarbageCollector garbageCollector = new GarbageCollector();
    private final BaylMemory memory = new BaylMemory();

    @Test
    void testFreeMemoryRemovesGarbage() {
        BaylRef ref1 = new BaylRef("ref1");
        BaylRef ref2 = new BaylRef("ref2");
        BaylRef ref3 = new BaylRef("ref3");

        memory.getHeap().put(ref1, new BaylArray(List.of()));
        memory.getHeap().put(ref2, new BaylArray(List.of()));
        memory.getHeap().put(ref3, new BaylArray(List.of()));

        memory.getGlobalStorage().put("root1", ref1);

        garbageCollector.freeMemory(memory);

        assertTrue(memory.getHeap().containsKey(ref1));

        assertFalse(memory.getHeap().containsKey(ref2));
        assertFalse(memory.getHeap().containsKey(ref3));
    }

    @Test
    void testFreeMemoryWithNoRoots() {
        memory.setVariable("a", new BaylNull());
        memory.setVariable("b", new BaylNull());
        memory.getHeap().put(new BaylRef("a"), new BaylArray(List.of()));
        memory.getHeap().put(new BaylRef("b"), new BaylArray(List.of()));

        garbageCollector.freeMemory(memory);

        assertFalse(memory.getHeap().containsKey(new BaylRef("a")));
        assertFalse(memory.getHeap().containsKey(new BaylRef("b")));
    }

    @Test
    void testCycleReference() {
        var a = new BaylArray(new ArrayList<>());
        var b = new BaylArray(new ArrayList<>());
        a.push(b);
        b.push(a);

        memory.setVariable("a", a);
        memory.setVariable("b", b);
        memory.setVariable("a", new BaylNull());
        memory.setVariable("b", new BaylNull());

        garbageCollector.freeMemory(memory);

        assertFalse(memory.getHeap().containsKey(a));
        assertFalse(memory.getHeap().containsKey(b));
    }

    @Test
    void testCycleReferenceWithOneNull() {
        var a = new BaylArray(new ArrayList<>());
        var b = new BaylArray(new ArrayList<>());
        a.push(b);
        b.push(a);

        memory.setVariable("a", a);
        memory.setVariable("b", b);
        memory.setVariable("a", new BaylNull());

        garbageCollector.freeMemory(memory);

        assertFalse(memory.getHeap().containsKey(a));
    }
}
