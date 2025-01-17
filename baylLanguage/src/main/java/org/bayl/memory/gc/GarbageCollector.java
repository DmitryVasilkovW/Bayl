package org.bayl.memory.gc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylType;
import org.bayl.runtime.object.BaylRef;

public class GarbageCollector {

    private Map<BaylRef, Boolean> visited;
    private Map<String, BaylRef> memoryGraph;

    public void freeMemory(Map<BaylRef, BaylObject> memory, Map<String, BaylType> roots) {
        init(memory, roots);
        Set<BaylRef> garbage = getGarbage();
        memory.keySet().removeIf(garbage::contains);
    }

    private Set<BaylRef> getGarbage() {
        mark();
        var garbage = new HashSet<BaylRef>();
        for (BaylRef node : visited.keySet()) {
            if (!visited.get(node)) {
                garbage.add(node);
            }
        }

        return garbage;
    }

    private void init(Map<BaylRef, BaylObject> memory, Map<String, BaylType> roots) {
        initVisited(memory);
        initGraph(roots);
    }

    private void initVisited(Map<BaylRef, BaylObject> memory) {
        visited = new HashMap<>();

        for (BaylRef ref : memory.keySet()) {
            visited.put(ref, false);
        }
    }

    private void initGraph(Map<String, BaylType> roots) {
        memoryGraph = new HashMap<>();

        for (String root : roots.keySet()) {
            Optional<BaylRef> refO
                    = tryToCastToRef(roots.get(root));
            refO.ifPresent(baylRef -> memoryGraph.put(root, baylRef));
        }
    }

    private Optional<BaylRef> tryToCastToRef(BaylType ref) {
        if (ref instanceof BaylRef) {
            return Optional.of((BaylRef) ref);
        }
        return Optional.empty();
    }

    private void mark() {
        for (BaylRef node : memoryGraph.values()) {
            if (!visited.get(node)) {
                visited.put(node, true);
            }
        }
    }
}
