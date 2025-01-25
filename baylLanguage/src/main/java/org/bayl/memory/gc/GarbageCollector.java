package org.bayl.memory.gc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bayl.memory.BaylMemory;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.ContainedTypes;
import org.bayl.runtime.ValueType;
import org.bayl.runtime.object.BaylRef;

public class GarbageCollector {

    private Map<BaylObject, Boolean> visited;
    private Map<BaylObject, List<BaylObject>> memoryGraph;

    public void freeMemory(BaylMemory memory) {
        init(memory);
        Set<BaylObject> garbage = getGarbage();

        memory.getHeap().values()
                .removeIf(garbage::contains);
    }

    private Set<BaylObject> getGarbage() {
        mark();

        var garbage = new HashSet<BaylObject>();
        for (BaylObject node : visited.keySet()) {
            if (!visited.get(node)) {
                garbage.add(node);
            }
        }

        return garbage;
    }

    private void init(BaylMemory memory) {
        initVisited(memory.getHeap());
        initGraph(memory);
    }

    private void initVisited(Map<BaylRef, BaylObject> memory) {
        visited = new IdentityHashMap<>();

        memory.values().forEach(obj -> {
            if (obj instanceof ContainedTypes) {
                ((ContainedTypes) obj).getAllTypes().forEach(
                        element -> visited.put(element, false)
                );
            }

            visited.put(obj, false);
        });
    }

    private void initGraph(BaylMemory memory) {
        memoryGraph = new IdentityHashMap<>();

        memory.getGlobalStorage().forEach((key, value) -> {
            if (!(value instanceof ValueType)) {
                BaylObject obj = memory.getVariable(key, null);
                List<BaylObject> refs = new ArrayList<>();

                if (obj instanceof ContainedTypes) {
                    refs = ((ContainedTypes) obj).getAllTypes();
                }

                memoryGraph.put(obj, refs);
            }
        });
    }

    private void mark() {
        for (BaylObject root : memoryGraph.keySet()) {
            dfsMark(root);
        }
    }

    private void dfsMark(BaylObject node) {
        if (node == null || visited.get(node)) {
            return;
        }

        visited.put(node, true);
        for (BaylObject
                ref
                : memoryGraph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.get(ref)) {
                dfsMark(ref);
            }
        }
    }
}
