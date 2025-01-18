package org.bayl.memory;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.bayl.SourcePosition;
import org.bayl.runtime.BaylMeaningful;
import org.bayl.runtime.BaylObject;
import org.bayl.runtime.BaylType;
import org.bayl.runtime.exception.UnsetVariableException;
import org.bayl.runtime.object.BaylRef;

@Getter
public class BaylMemory {

    private final Map<String, BaylType> globalStorage;
    private final Map<BaylRef, BaylObject> heap;

    public BaylMemory(Map<String, BaylType> globalStorage, Map<BaylRef, BaylObject> heap) {
        this.heap = heap;
        this.globalStorage = globalStorage;
    }

    public BaylMemory() {
        this.heap = new HashMap<>();
        this.globalStorage = new HashMap<>();
    }

    public BaylObject getVariable(String name, SourcePosition pos) {
        BaylType value = globalStorage.get(name);

        if (isMeaningful(value)) {
            return (BaylObject) value;
        } else if (isRef(value)) {
            return heap.get((BaylRef) value);
        }
        throw new UnsetVariableException(name, pos);
    }

    public void setVariable(String name, BaylObject value) {
        if (isMeaningful(value)) {
            globalStorage.put(name, value);
        } else if (isRef(value)) {
            BaylRef ref = getNewRef(name);

            globalStorage.put(name, ref);
            heap.put(ref, value);
        }
    }

    private BaylRef getNewRef(String name) {
        return new BaylRef(name);
    }

    private boolean isMeaningful(Object o) {
        return o instanceof BaylObject
                && o instanceof BaylMeaningful;
    }

    private boolean isRef(Object o) {
        return o instanceof BaylRef;
    }
}
