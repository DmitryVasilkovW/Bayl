package org.bayl.memory.gc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylType;
import org.bayl.runtime.object.ref.BaylArray;
import org.bayl.runtime.object.BaylRef;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GarbageCollectorTest {

    private GarbageCollector garbageCollector;

    @BeforeEach
    void setUp() {
        garbageCollector = new GarbageCollector();
    }

    @Test
    void testFreeMemory_RemovesGarbage() {
        BaylRef ref1 = new BaylRef("ref1");
        BaylRef ref2 = new BaylRef("ref2");
        BaylRef ref3 = new BaylRef("ref3");

        Map<BaylRef, BaylObject> memory = new HashMap<>();
        memory.put(ref1, new BaylArray(List.of()));
        memory.put(ref2, new BaylArray(List.of()));
        memory.put(ref3, new BaylArray(List.of()));

        Map<String, BaylType> roots = new HashMap<>();
        roots.put("root1", ref1);

        garbageCollector.freeMemory(memory, roots);

        assertTrue(memory.containsKey(ref1));

        assertFalse(memory.containsKey(ref2));
        assertFalse(memory.containsKey(ref3));
    }

    @Test
    void testFreeMemory_WithEmptyMemory() {
        Map<BaylRef, BaylObject> memory = new HashMap<>();
        Map<String, BaylType> roots = new HashMap<>();

        garbageCollector.freeMemory(memory, roots);

        assertTrue(memory.isEmpty());
    }

    @Test
    void testFreeMemory_WithNoRoots() {
        BaylRef ref1 = new BaylRef("ref1");
        BaylRef ref2 = new BaylRef("ref2");

        Map<BaylRef, BaylObject> memory = new HashMap<>();
        memory.put(ref1, new BaylArray(List.of()));
        memory.put(ref2, new BaylArray(List.of()));

        Map<String, BaylType> roots = new HashMap<>();

        garbageCollector.freeMemory(memory, roots);

        assertTrue(memory.isEmpty());
    }
}
